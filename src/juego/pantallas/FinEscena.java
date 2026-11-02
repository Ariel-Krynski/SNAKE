package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import motor.Escena;
import ui.Texto;

public class FinEscena extends Escena {
    private final int puntaje;

    public FinEscena(int puntaje) {
        this.puntaje=puntaje;
    }

    public void actualizar(double dt) {
        if (getEntrada().fuePresionada(KeyEvent.VK_ENTER)) getContexto().irA(new JuegoEscena());
        if (getEntrada().fuePresionada(KeyEvent.VK_M) || getEntrada().fuePresionada(KeyEvent.VK_ESCAPE)) getContexto().irA(new MenuEscena());
    }

    public void dibujar(Graphics2D grafico) {
        grafico.setColor(new Color(16,24,34)); grafico.fillRect(0,0,getContexto().getAncho(),getContexto().getAlto());
        Texto.centrado(grafico,"FIN DE LA PARTIDA",400,160,34,new Color(255,120,120));
        Texto.centrado(grafico,"Puntaje final: "+puntaje,400,235,27,Color.WHITE);
        Texto.centrado(grafico,"ENTER: volver a jugar",400,320,19,new Color(130,245,150));
        Texto.centrado(grafico,"M o ESC: menú principal",400,360,17,Color.LIGHT_GRAY);
    }

}
