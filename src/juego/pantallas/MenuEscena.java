package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import motor.Escena;
import ui.Texto;

public class MenuEscena extends Escena {
    private final JuegoEscena juego;

    public MenuEscena() {
        juego = new JuegoEscena();
    }

    public void actualizar(double dt) {
        if (getEntrada().fuePresionada(KeyEvent.VK_ENTER) || getEntrada().huboClick()) getContexto().irA(juego);
        if (getEntrada().fuePresionada(KeyEvent.VK_ESCAPE)) getContexto().salir();
    }

    public void dibujar(Graphics2D grafico) {
        grafico.setColor(new Color(16,24,34)); grafico.fillRect(0,0,getContexto().getAncho(),getContexto().getAlto());
        Texto.centrado(grafico,"SNAKE: DESHACER",400,130,38,new Color(130,245,150));
        Texto.centrado(grafico,"Come manzanas y evita chocar con las paredes o contigo mismo.",400,200,16,Color.WHITE);
        Texto.centrado(grafico,"Flechas: mover    Z: deshacer (-5 puntos)    P: pausa",400,240,16,Color.LIGHT_GRAY);
        Texto.centrado(grafico,"Manzana roja= +10   Dorada= +30 y crece doble   Podrida= -15",400,275,14,new Color(255,205,100));
        Texto.centrado(grafico,"Click para jugar",400,370,24,Color.WHITE);
        Texto.centrado(grafico,"Juguemos",400,430,15,new Color(150,170,185));
    }
}
