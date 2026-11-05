package juego.pantallas;

import estructuras.Cola;
import estructuras.ColaEnlazada;
import estructuras.ListaDoble;
import estructuras.Pila;
import estructuras.PilaEnlazada;
import juego.entidades.*;
import juego.reglas.EstadoMovimiento;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;
import motor.Escena;
import motor.Temporizador;
import ui.Texto;


//  Escena 2 de 3: la partida de Snake con deshacer.
//
//  Acá se usan las TRES estructuras propias, cada una con una función real:
//   - Lista doblemente enlazada (ListaDoble): el CUERPO de la serpiente (dentro de Serpiente).
//   - Cola (ColaEnlazada): los GIROS pendientes. FIFO: se aplican en el orden en que se apretaron.
//   - Pila (PilaEnlazada): el HISTORIAL de movimientos. LIFO: Z deshace primero el último.
//
//  No usa ninguna colección de java.util ni arreglos []; solo java.util.Random, que está permitido.

public class JuegoEscena extends Escena {

    // Tablero: 40 columnas x 25 filas, cada celda de 20 px  =>  800 x 500 px.
    // Los 100 px que faltan hasta los 600 de la ventana son la franja del HUD.
    private static final int CELDA = 20, COLUMNAS = 40, FILAS = 25;

    // Reglas con número: cuánto cuesta deshacer, cuántos pasos sin comer terminan la
    // partida y desde cuántos pasos se avisa en el HUD que se está por terminar.
    private static final int COSTO_DESHACER = 5;
    private static final int MAX_PASOS_SIN_COMER = 800;
    private static final int AVISO_PASOS_SIN_COMER = 650;

    // La serpiente da un paso cada 0,13 s. La podrida dura 10 s, pero se cuenta en pasos
    // y no en segundos de reloj: así la pausa la congela y Z la rebobina junto con el resto.
    private static final double SEGUNDOS_POR_PASO = 0.13;
    private static final int SEGUNDOS_PODRIDA = 10;
    private static final int PASOS_PODRIDA = (int) Math.round(SEGUNDOS_PODRIDA / SEGUNDOS_POR_PASO);

    // Un ejemplar de cada tipo para la referencia del HUD: el nombre, los puntos y el
    // color salen de la clase misma, así la referencia no se desactualiza si cambian.
    private static final Comida REF_MANZANA = new Manzana(new Posicion(0, 0));
    private static final Comida REF_DORADA = new ManzanaDorada(new Posicion(0, 0));
    private static final Comida REF_PODRIDA = new ManzanaPodrida(new Posicion(0, 0));

    // La serpiente recibe por constructor la lista donde guardará su cuerpo
    // (se depende de lo que hace la lista, no de cómo está hecha por dentro).
    private final Serpiente serpiente = new Serpiente(new ListaDoble<Posicion>());

    // Declarados con la INTERFAZ (Pila, Cola) y creados con la clase concreta (PilaEnlazada, ColaEnlazada).
    private final Pila<EstadoMovimiento> historial = new PilaEnlazada<>();
    private final Cola<Direccion> giros = new ColaEnlazada<>();

    private final Random azar = new Random();

    // Temporizador del kit: avanzar(dt) devuelve true cada 0,13 s.
    // La serpiente da un paso cada 0,13 s sin importar cuántas veces se llame a actualizar.
    private final Temporizador paso = new Temporizador(SEGUNDOS_POR_PASO);

    private Comida comida;
    private boolean terminado, pausa; // "terminado" hoy solo se asigna; no se lee en ningún lado
    private int pasosDesdeComida;     // cuenta pasos sin comer, para cortar partidas trabadas
    private int pasosDeLaComida;      // cuánto lleva en el tablero la comida actual (vence la podrida)

    // El motor lo llama cada vez que esta escena se activa.
    // Deja todo en estado inicial, así se puede volver a jugar con la misma instancia.
    @Override
    public void alEntrar() {
        serpiente.reiniciar();
        historial.vaciar();
        giros.vaciar();
        terminado = false;
        pausa = false;
        pasosDesdeComida = 0;
        paso.reiniciar();
        generarComida(true);
    }

    // Se llama en cada paso del motor (1/120 s): lee teclas y, si corresponde, avanza el juego.
    @Override
    public void actualizar(double dt) {
        // ESC: abandona la partida y vuelve al menú.
        if (getEntrada().fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().irA(new MenuEscena());
            return;
        }
        // P: alterna la pausa (si estaba en pausa sale, si no entra).
        if (getEntrada().fuePresionada(KeyEvent.VK_P)) {
            pausa = !pausa;
        }
        // Flechas: no mueven la serpiente directamente, encolan un giro.
        if (getEntrada().fuePresionada(KeyEvent.VK_UP)) {
            registrarGiro(Direccion.ARRIBA);
        }
        if (getEntrada().fuePresionada(KeyEvent.VK_DOWN)) {
            registrarGiro(Direccion.ABAJO);
        }
        if (getEntrada().fuePresionada(KeyEvent.VK_LEFT)) {
            registrarGiro(Direccion.IZQUIERDA);
        }
        if (getEntrada().fuePresionada(KeyEvent.VK_RIGHT)) {
            registrarGiro(Direccion.DERECHA);
        }
        // Z: deshace, solo si hay algo en la pila y alcanzan los puntos para pagarlo.
        if (getEntrada().fuePresionada(KeyEvent.VK_Z) && puedeDeshacer()) {
            deshacer();
        }
        // En pausa no avanza el temporizador, por eso el juego se congela.
        if (pausa) {
            return;
        }
        // Cada 0,13 s da un paso de juego.
        if (paso.avanzar(dt)) {
            avanzarJuego();
        }
    }

    // Encola un giro SOLO si es válido:
    //  - hay lugar (máximo 2 giros pendientes), para que no se acumulen pulsaciones;
    //  - no es un giro de 180° (la serpiente no puede darse vuelta sobre sí misma).
    // Si la cola está vacía se compara con la dirección actual; si no, con el giro que ya espera.
    // Como el máximo es 2, cuando hay uno encolado ese uno es el "frente" y también el último.
    private void registrarGiro(Direccion d) {
        if (giros.tamanio() < 2
                && (giros.estaVacia()
                ? !d.esOpuesta(serpiente.getDireccion())
                : !d.esOpuesta(giros.frente()))) {
            giros.encolar(d);
        }
    }

    // Un paso de juego (ocurre cada 0,13 s). El orden importa:
    private void avanzarJuego() {
        // 1) Aplica UN giro pendiente (el más viejo, por ser Cola/FIFO).
        if (!giros.estaVacia()) {
            serpiente.cambiarDireccion(giros.desencolar());
        }
        // 2) Saca la foto ANTES de mover: así Z vuelve a como estaba antes de este paso.
        //    Todavía no se apila: falta saber cuántos puntos gana el paso (ver el punto 6).
        int puntosAntes = serpiente.getPuntos();
        EstadoMovimiento foto = new EstadoMovimiento(serpiente.getCuerpo(), serpiente.getDireccion(),
                serpiente.getCrecer(), comida, pasosDesdeComida, pasosDeLaComida);
        // 3) Mueve la serpiente.
        serpiente.mover();
        pasosDesdeComida++;
        Posicion cabeza = serpiente.getCabeza();
        // 4) Choque con pared (fuera del tablero) o consigo misma: fin de la partida.
        //    Se le pasa el puntaje a FinEscena por constructor.
        if (cabeza.getX() < 0 || cabeza.getY() < 0 || cabeza.getX() >= COLUMNAS || cabeza.getY() >= FILAS
                || serpiente.chocaConsigoMisma()) {
            terminado = true;
            getContexto().irA(new FinEscena(serpiente.getPuntos()));
            return;
        }
        // 5) Si la cabeza llegó a la comida: POLIMORFISMO. aplicar() hace algo distinto según
        //    la subclase real (Manzana, ManzanaDorada, ManzanaPodrida) sin preguntar el tipo.
        if (cabeza.equals(comida.getPosicion())) {
            comida.aplicar(serpiente);
            generarComida(true);
            pasosDesdeComida = 0;
        } else {
            // 5b) La comida que vence (la podrida) desaparece si nadie la comió a tiempo, y en
            //     su lugar aparece una buena: si saliera otra podrida, habría que esperar de nuevo.
            pasosDeLaComida++;
            if (comida.vence() && pasosDeLaComida >= PASOS_PODRIDA) {
                generarComida(false);
            }
        }
        // 6) Ya se sabe cuánto sumó o restó el paso: se completa la foto y se apila.
        foto.setPuntosGanados(serpiente.getPuntos() - puntosAntes);
        historial.apilar(foto);
        // 7) Anti-trabado: más de 800 pasos sin comer termina la partida.
        //    Desde los 650 el HUD avisa cuántos pasos quedan (ver dibujar).
        if (pasosDesdeComida > MAX_PASOS_SIN_COMER) {
            terminado = true;
            getContexto().irA(new FinEscena(serpiente.getPuntos()));
        }
    }

    // Regla: deshacer es «a cambio de puntos». Solo se puede si, sacando lo que ganó el
    // último paso, quedan al menos 5 puntos para pagarlo. tope() mira la foto sin sacarla.
    private boolean puedeDeshacer() {
        if (historial.estaVacia()) {
            return false;
        }
        return serpiente.getPuntos() - historial.tope().getPuntosGanados() >= COSTO_DESHACER;
    }

    // Deshace el último movimiento: desapila la foto más reciente (LIFO) y la restaura.
    // Rebobina el paso completo: cuerpo, dirección, crecimiento, comida y contador de pasos.
    private void deshacer() {
        EstadoMovimiento estado = historial.desapilar();
        // Vacía el cuerpo actual y lo reconstruye copiando posición por posición.
        // Se crean Posicion nuevas para que la serpiente viva no comparta objetos con el historial.
        // (Recorrer con obtener(i) en lista enlazada es O(n) por acceso: aceptable con tablero chico.)
        serpiente.getCuerpo().vaciar();
        for (int i = 0; i < estado.getCuerpo().tamanio(); i++) {
            Posicion p = estado.getCuerpo().obtener(i);
            serpiente.getCuerpo().agregar(new Posicion(p.getX(), p.getY()));
        }
        serpiente.restaurarDireccion(estado.getDireccion());
        // Si el paso deshecho fue el que comió, la serpiente deja de crecer por esa comida.
        serpiente.setCrecer(estado.getCrecer());
        // Vuelve la comida que había antes del paso (la manzana comida reaparece en su lugar)
        // y el contador de pasos sin comer de ese momento.
        comida = estado.getComida();
        pasosDesdeComida = estado.getPasosDesdeComida();
        pasosDeLaComida = estado.getPasosDeLaComida();
        // Puntos: a los actuales se les saca lo que ganó ese paso y el costo de deshacer.
        // Se calcula sobre los actuales, no sobre los guardados, para que cada Z cobre sus 5.
        // puedeDeshacer() ya garantizó que el resultado no queda negativo.
        serpiente.setPuntos(serpiente.getPuntos() - estado.getPuntosGanados() - COSTO_DESHACER);
        // Los giros que estaban esperando ya no corresponden a la situación restaurada.
        giros.vaciar();
    }

    // Crea comida nueva en una celda libre y de tipo al azar. Con permitirPodrida en false
    // sale sólo roja o dorada: se usa para reemplazar a una podrida que venció.
    private void generarComida(boolean permitirPodrida) {
        pasosDeLaComida = 0;
        Posicion p;
        // Repite hasta encontrar una celda que NO esté ocupada por la serpiente.
        do {
            p = new Posicion(azar.nextInt(COLUMNAS), azar.nextInt(FILAS));
        } while (serpiente.contiene(p));

        // Tres subclases de Comida; la variable "comida" es del tipo padre (polimorfismo).
        int tipo = permitirPodrida ? azar.nextInt(3) : 1 + azar.nextInt(2);
        if (tipo == 0) {
            comida = new ManzanaPodrida(p);
        } else if (tipo == 1) {
            comida = new ManzanaDorada(p);
        } else {
            comida = new Manzana(p);
        }
    }

    @Override
    public void dibujar(Graphics2D grafico) {
        // Fondo del tablero y grilla (líneas verticales y horizontales cada CELDA px).
        grafico.setColor(new Color(13, 20, 28));
        grafico.fillRect(0, 0, COLUMNAS * CELDA, FILAS * CELDA);
        grafico.setColor(new Color(27, 39, 49));
        for (int x = 0; x <= COLUMNAS; x++) {
            grafico.drawLine(x * CELDA, 0, x * CELDA, FILAS * CELDA);
        }
        for (int y = 0; y <= FILAS; y++) {
            grafico.drawLine(0, y * CELDA, COLUMNAS * CELDA, y * CELDA);
        }
        // Cada entidad sabe dibujarse a sí misma; la escena solo le pasa el lápiz y el tamaño de celda.
        serpiente.dibujar(grafico, CELDA);
        comida.dibujar(grafico, CELDA);

        // HUD: franja de 100 px debajo del tablero con puntaje, tamaño y ayuda de teclas.
        grafico.setColor(new Color(20, 28, 38));
        grafico.fillRect(0, FILAS * CELDA, COLUMNAS * CELDA, 100);
        Texto.izquierda(grafico, "Puntaje: " + serpiente.getPuntos(), 16, FILAS * CELDA + 30, 18, Color.WHITE);
        Texto.izquierda(grafico, "Tamaño: " + serpiente.getCuerpo().tamanio(), 190, FILAS * CELDA + 30, 16, new Color(130, 245, 150));
        // El récord de las partidas anteriores (lo lleva FinEscena, sólo en memoria).
        Texto.izquierda(grafico, "Récord: " + FinEscena.getRecord(), 320, FILAS * CELDA + 30, 16, new Color(255, 205, 55));
        Texto.izquierda(grafico, "Z deshacer (-5)   P pausa   ESC menú", 16, FILAS * CELDA + 62, 15, Color.LIGHT_GRAY);
        // Regla del límite de pasos: se avisa antes de cortar la partida por no comer.
        // Va abajo a la izquierda porque la derecha la ocupa la referencia de manzanas.
        if (pasosDesdeComida > AVISO_PASOS_SIN_COMER) {
            int quedan = MAX_PASOS_SIN_COMER - pasosDesdeComida;
            Texto.izquierda(grafico, "¡Comé algo! Quedan " + quedan + " pasos", 16, FILAS * CELDA + 88, 14, new Color(255, 120, 120));
        }
        // Cuenta regresiva de la podrida: se redondea para arriba, así nunca muestra 0 s
        // mientras todavía está en el tablero.
        if (comida.vence()) {
            int pasosQueQuedan = PASOS_PODRIDA - pasosDeLaComida;
            int segundos = (int) Math.ceil(pasosQueQuedan * SEGUNDOS_POR_PASO);
            Texto.izquierda(grafico, "Podrida: se va en " + segundos + " s", 320, FILAS * CELDA + 62, 16, REF_PODRIDA.getColor());
        }
        // Referencia de manzanas, abajo a la derecha.
        dibujarReferencia(grafico, REF_MANZANA, "", FILAS * CELDA + 30);
        dibujarReferencia(grafico, REF_DORADA, ", crece x2", FILAS * CELDA + 55);
        dibujarReferencia(grafico, REF_PODRIDA, ", dura " + SEGUNDOS_PODRIDA + " s", FILAS * CELDA + 80);

        // Pausa: velo negro semitransparente (el 4.º valor, 170, es la opacidad) y cartel centrado.
        if (pausa) {
            grafico.setColor(new Color(0, 0, 0, 170));
            grafico.fillRect(0, 0, COLUMNAS * CELDA, FILAS * CELDA);
            Texto.centrado(grafico, "PAUSA", COLUMNAS * CELDA / 2.0, FILAS * CELDA / 2.0, 38, Color.WHITE);
        }
    }

    // Una fila de la referencia: el círculo con el color de la manzana y, al lado, su
    // nombre y sus puntos con signo. "y" es la línea base del texto.
    private void dibujarReferencia(Graphics2D grafico, Comida ejemplo, String detalle, int y) {
        int x = 540;
        grafico.setColor(ejemplo.getColor());
        grafico.fillOval(x, y - 12, 14, 14);
        String puntos = ejemplo.getPuntos() > 0 ? "+" + ejemplo.getPuntos() : String.valueOf(ejemplo.getPuntos());
        Texto.izquierda(grafico, ejemplo.getNombre() + "  " + puntos + detalle, x + 22, y, 13, Color.LIGHT_GRAY);
    }
}
