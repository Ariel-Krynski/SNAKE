package juego.entidades;

import java.awt.Color;

public class ManzanaDorada extends Comida {
    public ManzanaDorada(Posicion p) {
        super(p, new Color(255, 205, 55));
    }

    public void aplicar(Serpiente serpiente)
    { serpiente.crecer(); serpiente.crecer(); serpiente.sumarPuntos(getPuntos());
    }

    public int getPuntos() {
        return 30;
    }

    public String getNombre()
    { return "Manzana dorada";
    }
}
