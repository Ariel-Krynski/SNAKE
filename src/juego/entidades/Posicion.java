package juego.entidades;

public class Posicion {
    private final int x, y;// Declara las posiciones de la serpiente
    public Posicion(int x, int y) {
        this.x=x; this.y=y; // asigna los parámetros a los atributos final. Esta es la única asignación posible para un final (dentro del constructor)
    }
// Getters de X y Y,estos no se modifican no es necesario setters, se reemplazan
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
// Define si dos posiciones son iguales o no, se utiliza para saber si la serpiente esta sobre una manzana
    public boolean equals(Object otro) {
        if (!(otro instanceof Posicion)) return false; // verifica si el objeto es del mismo tipo(int, String, ect), sino lo es devuelve falso
        Posicion p = (Posicion) otro; // trata a otro como posicion ya que este es tipo object
        return x == p.x && y == p.y; // Compara las coordenadas preguntando si son iguales
    }
// Devuelve en codigo Hash un entero que representa la posicion.
    public int hashCode() { return 31*x+y; } // Multiplicar por 31 evita colisiones (ej. que (2,3) y (3,2) den el mismo resultado)
}
