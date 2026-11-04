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

    // La serpiente recibe por constructor la lista donde guardará su cuerpo
    // (se depende de lo que hace la lista, no de cómo está hecha por dentro).
    private final Serpiente serpiente = new Serpiente(new ListaDoble<Posicion>());

    // Declarados con la INTERFAZ (Pila, Cola) y creados con la clase concreta (PilaEnlazada, ColaEnlazada).
    private final Pila<EstadoMovimiento> historial = new PilaEnlazada<>();
    private final Cola<Direccion> giros = new ColaEnlazada<>();

    private final Random azar = new Random();

    // Temporizador del kit: avanzar(dt) devuelve true cada 0,13 s.
    // La serpiente da un paso cada 0,13 s sin importar cuántas veces se llame a actualizar.
    private final Temporizador paso = new Temporizador(0.13);

    private Comida comida;
    private boolean terminado, pausa; // "terminado" hoy solo se asigna; no se lee en ningún lado
    private int pasosDesdeComida;     // cuenta pasos sin comer, para cortar partidas trabadas

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
        generarComida();
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
        // Z: deshace, pero solo si hay algo en la pila (desapilar con pila vacía lanzaría excepción).
        if (getEntrada().fuePresionada(KeyEvent.VK_Z) && !historial.estaVacia()) {
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
        // 2) Guarda el estado ANTES de mover: así Z vuelve a como estaba antes de este paso.
        guardarEstado();
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
            generarComida();
            pasosDesdeComida = 0;
        }
        // 6) Anti-trabado: 800 pasos sin comer termina la partida.
        if (pasosDesdeComida > 800) {
            terminado = true;
            getContexto().irA(new FinEscena(serpiente.getPuntos()));
        }
    }

    // Apila una "foto" del estado (cuerpo, dirección y puntos). EstadoMovimiento es de juego.reglas.
    private void guardarEstado() {
        historial.apilar(new EstadoMovimiento(serpiente.getCuerpo(), serpiente.getDireccion(), serpiente.getPuntos()));
    }

    // Deshace el último movimiento: desapila la foto más reciente (LIFO) y la restaura.
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
        // El deshacer cuesta 5 puntos; Math.max(0, ...) evita que el puntaje quede negativo.
        serpiente.setPuntos(Math.max(0, estado.getPuntos() - 5));
        // Los giros que estaban esperando ya no corresponden a la situación restaurada.
        giros.vaciar();
    }

    // Crea comida nueva en una celda libre y de tipo al azar.
    private void generarComida() {
        Posicion p;
        // Repite hasta encontrar una celda que NO esté ocupada por la serpiente.
        do {
            p = new Posicion(azar.nextInt(COLUMNAS), azar.nextInt(FILAS));
        } while (serpiente.contiene(p));

        // Tres subclases de Comida; la variable "comida" es del tipo padre (polimorfismo).
        int tipo = azar.nextInt(3);
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
        Texto.izquierda(grafico, "Z deshacer (-5)   P pausa   ESC menú", 16, FILAS * CELDA + 62, 15, Color.LIGHT_GRAY);

        // Pausa: velo negro semitransparente (el 4.º valor, 170, es la opacidad) y cartel centrado.
        if (pausa) {
            grafico.setColor(new Color(0, 0, 0, 170));
            grafico.fillRect(0, 0, COLUMNAS * CELDA, FILAS * CELDA);
            Texto.centrado(grafico, "PAUSA", COLUMNAS * CELDA / 2.0, FILAS * CELDA / 2.0, 38, Color.WHITE);
        }
    }
}
