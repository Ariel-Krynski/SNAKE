package juego.entidades;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Comida implements EfectoComida {
    private Posicion posicion;
    private final Color color;

    protected Comida(Posicion posicion, Color color) {
        if (posicion == null) throw new IllegalArgumentException("La posición es obligatoria");
        this.posicion=posicion; this.color=color;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        if (posicion == null) throw new IllegalArgumentException("La posición es obligatoria");
        this.posicion=posicion;
    }

    public void dibujar(Graphics2D g, int celda) {
        g.setColor(color);
        g.fillOval(posicion.getX()*celda+3, posicion.getY()*celda+3, celda-6, celda-6);
    }
}
