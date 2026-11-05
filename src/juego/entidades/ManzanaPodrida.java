package juego.entidades;

import java.awt.Color;

public class ManzanaPodrida extends Comida {

    public ManzanaPodrida(Posicion p) {
        super(p, new Color(165, 95, 210));
    }// recibe posicion y da el color a la manzana

    public void aplicar(Serpiente serpiente) {
        serpiente.restarPuntos(15); // Resta 15 puntos al marcador del jugador
        serpiente.encoger();// Reduce a la serpiente -1
    }

    public int getPuntos() {
        return -15;
    }

    public String getNombre() {
        return "Manzana podrida";
    }

    // Se pudre del todo y desaparece: no tiene sentido obligar a comerla para seguir.
    @Override
    public boolean vence() {
        return true;
    }
}
