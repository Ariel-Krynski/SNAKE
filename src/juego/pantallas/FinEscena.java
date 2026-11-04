package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import motor.Escena;
import ui.Texto;

 // Escena 3 de 3: pantalla de fin de la partida.
 // Muestra el puntaje final SOLO en pantalla (el enunciado prohíbe guardarlo en un archivo).
 // Desde acá se vuelve a jugar (ENTER) o se va al menú (M o ESC).
public class FinEscena extends Escena {

    // private final: el puntaje se fija al crear la escena y nadie lo puede cambiar después.
    private final int puntaje;

    // El puntaje llega por constructor: la escena de la partida crea
    // new FinEscena(serpiente.getPuntos()) cuando la serpiente choca.
    public FinEscena(int puntaje) {
        this.puntaje = puntaje;
    }

    @Override
    public void actualizar(double dt) {
        // ENTER: nueva partida. Se crea una JuegoEscena nueva (arranca de cero).
        if (getEntrada().fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(new JuegoEscena());
        }
        // M o ESC: vuelve al menú principal.
        if (getEntrada().fuePresionada(KeyEvent.VK_M) || getEntrada().fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().irA(new MenuEscena());
        }
    }

    @Override
    public void dibujar(Graphics2D grafico) {
        // Fondo de toda la ventana.
        grafico.setColor(new Color(16, 24, 34));
        grafico.fillRect(0, 0, getContexto().getAncho(), getContexto().getAlto());

        // Textos centrados en x = 400 (mitad de 800 px). El puntaje se concatena como texto.
        Texto.centrado(grafico, "FIN DE LA PARTIDA", 400, 160, 34, new Color(255, 120, 120));
        Texto.centrado(grafico, "Puntaje final: " + puntaje, 400, 235, 27, Color.WHITE);
        Texto.centrado(grafico, "ENTER: volver a jugar", 400, 320, 19, new Color(130, 245, 150));
        Texto.centrado(grafico, "M o ESC: menú principal", 400, 360, 17, Color.LIGHT_GRAY);
    }
}
