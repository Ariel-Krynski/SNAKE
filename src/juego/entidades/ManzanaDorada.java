package juego.entidades;

import java.awt.Color;

public class ManzanaDorada extends Comida {
    public ManzanaDorada(Posicion p) {
        super(p, new Color(255, 205, 55)); // Define el color de la manzana
    }

    public void aplicar(Serpiente serpiente)
    { serpiente.crecer(); serpiente.crecer(); // Llama dos veces a crecer haciendo que la serpiente tenga dos segmentos mas
        serpiente.sumarPuntos(getPuntos()); // puntos
    }

    public int getPuntos() {
        return 30;
    } // puntos

    public String getNombre()
    { return "Manzana dorada"; // nombre de manzana
    }
}
