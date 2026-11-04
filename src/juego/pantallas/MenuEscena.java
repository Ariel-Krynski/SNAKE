package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import motor.Escena;
import ui.Texto;


 // Escena 1 de 3: menú principal.
 // Muestra el título, las instrucciones y los puntos de cada manzana.
 // Desde acá se pasa a la partida (ENTER o click) o se sale del programa (ESC).

 // Herencia: extiende Escena (clase abstracta del kit) y sobreescribe
 // actualizar(dt) y dibujar(g), que son los dos métodos que el motor llama
 // en cada paso.

public class MenuEscena extends Escena {

    // private final: la escena de la partida se crea una sola vez, al armar el menú.
    private final JuegoEscena juego;

    public MenuEscena() {
        juego = new JuegoEscena();
    }

    // El motor lo llama 120 veces por segundo (pasos de 1/120 s). dt = segundos del paso.
    @Override
    public void actualizar(double dt) {
        // fuePresionada: true SOLO en el paso en que se apretó la tecla (una vez por pulsación).
        // huboClick: true en el paso en que se hizo click con el mouse.
        if (getEntrada().fuePresionada(KeyEvent.VK_ENTER) || getEntrada().huboClick()) {
            // irA no cambia la escena al instante: se aplica al comienzo del paso siguiente.
            getContexto().irA(juego);
        }
        if (getEntrada().fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().salir(); // cierra el programa
        }
    }

    // Solo dibuja; no modifica el estado del juego.
    @Override
    public void dibujar(Graphics2D grafico) {
        // Fondo: rectángulo del tamaño de toda la ventana (ancho y alto vienen del contexto).
        grafico.setColor(new Color(16, 24, 34));
        grafico.fillRect(0, 0, getContexto().getAncho(), getContexto().getAlto());

        // Texto.centrado(g, texto, x, y, tamaño, color): 400 es la mitad de los 800 px de la ventana.
        // La y es la línea base del texto (donde "apoyan" las letras).
        Texto.centrado(grafico, "SNAKE: DESHACER", 400, 130, 38, new Color(130, 245, 150));
        Texto.centrado(grafico, "Come manzanas y evita chocar con las paredes o contigo mismo.", 400, 200, 16, Color.WHITE);
        Texto.centrado(grafico, "Flechas: mover    Z: deshacer (-5 puntos)    P: pausa", 400, 240, 16, Color.LIGHT_GRAY);
        Texto.centrado(grafico, "Manzana roja= +10   Dorada= +30 y crece doble   Podrida= -15", 400, 275, 14, new Color(255, 205, 100));
        Texto.centrado(grafico, "Click para jugar", 400, 370, 24, Color.WHITE);
        Texto.centrado(grafico, "Juguemos", 400, 430, 15, new Color(150, 170, 185));
    }
}
