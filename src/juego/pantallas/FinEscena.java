package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import motor.Escena;
import ui.Texto;

 // Escena 3 de 3: pantalla de fin de la partida.
 // Muestra el puntaje final y el récord SOLO en pantalla (el enunciado prohíbe guardarlo
 // en un archivo): el récord vive en memoria mientras el juego está abierto.
 // Desde acá se vuelve a jugar (ENTER) o se va al menú (M o ESC).
public class FinEscena extends Escena {

    // static: lo comparten todas las FinEscena. Cada partida crea una nueva, así que
    // un campo de instancia arrancaría en 0 cada vez y nunca recordaría nada.
    // Arranca en 0: una partida que termina en negativo (por las podridas) no lo bate.
    private static int record;

    // private final: el puntaje se fija al crear la escena y nadie lo puede cambiar después.
    private final int puntaje;
    private final boolean nuevoRecord;

    // El puntaje llega por constructor: la escena de la partida crea
    // new FinEscena(serpiente.getPuntos()) cuando la serpiente choca.
    // Este es el único lugar por donde pasan todos los puntajes finales, por eso
    // el récord se actualiza acá.
    public FinEscena(int puntaje) {
        this.puntaje = puntaje;
        nuevoRecord = puntaje > record;
        if (nuevoRecord) {
            record = puntaje;
        }
    }

    // Sólo lectura: el menú y el HUD lo muestran, pero sólo esta clase lo cambia.
    public static int getRecord() {
        return record;
    }

    public boolean esNuevoRecord() {
        return nuevoRecord;
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
        // Si se batió, se festeja en dorado (el color de la manzana dorada); si no, se
        // muestra el récord a superar.
        if (nuevoRecord) {
            Texto.centrado(grafico, "¡Nuevo récord!", 400, 280, 22, new Color(255, 205, 55));
        } else {
            Texto.centrado(grafico, "Récord: " + record, 400, 280, 20, new Color(150, 170, 185));
        }
        Texto.centrado(grafico, "ENTER: volver a jugar", 400, 350, 19, new Color(130, 245, 150));
        Texto.centrado(grafico, "M o ESC: menú principal", 400, 390, 17, Color.LIGHT_GRAY);
    }
}
