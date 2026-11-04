package juego.entidades;

import java.awt.Color; // Importacion del color de la manzana

public class Manzana extends Comida {
    public Manzana(Posicion p) {
        super(p, new Color(240, 70, 80));
    } // Envia la posicion  de la manzana y construye su color en RGB

    public void aplicar(Serpiente serpiente) {
        serpiente.crecer();// La serpiente crece un segmento o una cuadricula
        serpiente.sumarPuntos(getPuntos()); // suma los puntos que otorga esta comida
    }

    public int getPuntos() {
        return 10;
    } // implementa cuantas puntos otorga la manzana

    public String getNombre() {
        return "Manzana";
    } // Devuelve el nombre de la comida

}
