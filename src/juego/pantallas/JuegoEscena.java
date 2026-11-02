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

public class JuegoEscena extends Escena {
    private static final int CELDA=20, COLUMNAS=40, FILAS=25;
    private final Serpiente serpiente = new Serpiente(new ListaDoble<Posicion>());
    private final Pila<EstadoMovimiento> historial = new PilaEnlazada<>();
    private final Cola<Direccion> giros = new ColaEnlazada<>();
    private final Random azar = new Random();
    private final Temporizador paso = new Temporizador(0.13);
    private Comida comida;
    private boolean terminado, pausa;
    private int pasosDesdeComida;

    public void alEntrar() {
        serpiente.reiniciar(); historial.vaciar(); giros.vaciar();
        terminado=false; pausa=false; pasosDesdeComida=0; paso.reiniciar(); generarComida();
    }
    public void actualizar(double dt) {
        if (getEntrada().fuePresionada(KeyEvent.VK_ESCAPE)) { getContexto().irA(new MenuEscena()); return; }
        if (getEntrada().fuePresionada(KeyEvent.VK_P)) pausa=!pausa;
        if (getEntrada().fuePresionada(KeyEvent.VK_UP)) registrarGiro(Direccion.ARRIBA);
        if (getEntrada().fuePresionada(KeyEvent.VK_DOWN)) registrarGiro(Direccion.ABAJO);
        if (getEntrada().fuePresionada(KeyEvent.VK_LEFT)) registrarGiro(Direccion.IZQUIERDA);
        if (getEntrada().fuePresionada(KeyEvent.VK_RIGHT)) registrarGiro(Direccion.DERECHA);
        if (getEntrada().fuePresionada(KeyEvent.VK_Z) && !historial.estaVacia()) deshacer();
        if (pausa) return;
        if (paso.avanzar(dt)) avanzarJuego();
    }
    private void registrarGiro(Direccion d) {
        if (giros.tamanio()<2 && (giros.estaVacia() ? !d.esOpuesta(serpiente.getDireccion()) : !d.esOpuesta(giros.frente()))) giros.encolar(d);
    }
    private void avanzarJuego() {
        if (!giros.estaVacia()) serpiente.cambiarDireccion(giros.desencolar());
        guardarEstado();
        serpiente.mover(); pasosDesdeComida++;
        Posicion cabeza=serpiente.getCabeza();
        if (cabeza.getX()<0 || cabeza.getY()<0 || cabeza.getX()>=COLUMNAS || cabeza.getY()>=FILAS || serpiente.chocaConsigoMisma()) {
            terminado=true; getContexto().irA(new FinEscena(serpiente.getPuntos())); return;
        }
        if (cabeza.equals(comida.getPosicion())) {
            comida.aplicar(serpiente); generarComida(); pasosDesdeComida=0;
        }
        if (pasosDesdeComida>800) { terminado=true; getContexto().irA(new FinEscena(serpiente.getPuntos())); }
    }
    private void guardarEstado() {
        historial.apilar(new EstadoMovimiento(serpiente.getCuerpo(),serpiente.getDireccion(),serpiente.getPuntos()));
    }
    private void deshacer() {
        EstadoMovimiento estado=historial.desapilar();
        serpiente.getCuerpo().vaciar();
        for (int i=0;i<estado.getCuerpo().tamanio();i++) {
            Posicion p=estado.getCuerpo().obtener(i);
            serpiente.getCuerpo().agregar(new Posicion(p.getX(),p.getY()));
        }
        serpiente.restaurarDireccion(estado.getDireccion());
        serpiente.setPuntos(Math.max(0,estado.getPuntos()-5));
        giros.vaciar();
    }
    private void generarComida() {
        Posicion p;

        do {
            p = new Posicion(azar.nextInt(COLUMNAS), azar.nextInt(FILAS));
        } while (serpiente.contiene(p));

        int tipo = azar.nextInt(3);

        if (tipo == 0) {
            comida = new ManzanaPodrida(p);
        } else if (tipo == 1) {
            comida = new ManzanaDorada(p);
        } else {
            comida = new Manzana(p);
        }
    }
    
    public void dibujar(Graphics2D grafico) {
        grafico.setColor(new Color(13,20,28)); grafico.fillRect(0,0,COLUMNAS*CELDA,FILAS*CELDA);
        grafico.setColor(new Color(27,39,49));
        for (int x=0;x<=COLUMNAS;x++) grafico.drawLine(x*CELDA,0,x*CELDA,FILAS*CELDA);
        for (int y=0;y<=FILAS;y++) grafico.drawLine(0,y*CELDA,COLUMNAS*CELDA,y*CELDA);
        serpiente.dibujar(grafico,CELDA); comida.dibujar(grafico,CELDA);
        grafico.setColor(new Color(20,28,38)); grafico.fillRect(0,FILAS*CELDA,COLUMNAS*CELDA,100);
        Texto.izquierda(grafico,"Puntaje: "+serpiente.getPuntos(),16,FILAS*CELDA+30,18,Color.WHITE);
        Texto.izquierda(grafico,"Tamaño: "+serpiente.getCuerpo().tamanio(),190,FILAS*CELDA+30,16,new Color(130,245,150));
        Texto.izquierda(grafico,"Z deshacer (-5)   P pausa   ESC menú",16,FILAS*CELDA+62,15,Color.LIGHT_GRAY);
        if (pausa) {
            grafico.setColor(new Color(0,0,0,170)); grafico.fillRect(0,0,COLUMNAS*CELDA,FILAS*CELDA);
            Texto.centrado(grafico,"PAUSA",COLUMNAS*CELDA/2.0,FILAS*CELDA/2.0,38,Color.WHITE);
        }
    }
}
