package demo;

import fisica.Cuerpo;
import fisica.Material;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class PelotaDemo extends Cuerpo {

    // Constructor
    public PelotaDemo(double x, double y, double radio, Material material) {
        super(x, y, radio, material);
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D grafico) {
        grafico.setColor(getMaterial().getColor());
        grafico.fill(getForma());
        grafico.setColor(new Color(0, 0, 0, 120));
        grafico.setStroke(new BasicStroke(2f));
        grafico.draw(getForma());
    }
}
