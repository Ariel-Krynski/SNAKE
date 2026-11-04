package juego.entidades;

import java.awt.Color;
import java.awt.Graphics2D; // perimite dibujar/mostrar formas en la pantalla

public abstract class Comida implements EfectoComida {// Clase abstracta que obliga a que existe comida y implementa a la interfaz efectocomida
    private Posicion posicion;// indica en donde se encuentra la comida
    private final Color color; // color de la comida

        //Posicion no es final xq este cambia a diferencia de color.
    protected Comida(Posicion posicion, Color color) {
        if (posicion == null) throw new IllegalArgumentException("La posición es obligatoria"); // evita que posicion no tenga una lugar, haciendolo invalido
        this.posicion=posicion; this.color=color;
    }
// Getter y Setters
    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        if (posicion == null) throw new IllegalArgumentException("La posición es obligatoria");
        this.posicion=posicion;
    }

    public void dibujar(Graphics2D g, int celda) {// Asigna las variables
        g.setColor(color); // Selecciona el color de la comida como actual
        g.fillOval(posicion.getX()*celda+3, posicion.getY()*celda+3, celda-6, celda-6); //Permite que la comida quede centrada dentro de las cuadriculas, creando un margen de 3 centimetros para que no queden pegados al borde
    }
}
