package juego.entidades;

import java.awt.Color;

public class Manzana extends Comida {
    public Manzana(Posicion p) {
        super(p, new Color(240, 70, 80));
    }

    public void aplicar(Serpiente serpiente) {
        serpiente.crecer(); serpiente.sumarPuntos(getPuntos());
    }

    public int getPuntos() {
        return 10;
    }

    public String getNombre() {
        return "Manzana";
    }

}
