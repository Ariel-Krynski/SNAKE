package juego.entidades;

import java.awt.Color;

public class ManzanaPodrida extends Comida {

    public ManzanaPodrida(Posicion p) {
        super(p, new Color(165, 95, 210));
    }

    public void aplicar(Serpiente serpiente) {
        serpiente.restarPuntos(15); serpiente.encoger();
    }

    public int getPuntos() {
        return -15;
    }

    public String getNombre() {
        return "Manzana podrida";
    }
}
