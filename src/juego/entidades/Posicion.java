package juego.entidades;

public class Posicion {
    private final int x, y;
    public Posicion(int x, int y) {
        this.x=x; this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Object otro) {
        if (!(otro instanceof Posicion)) return false;
        Posicion p = (Posicion) otro;
        return x == p.x && y == p.y;
    }

    public int hashCode() { return 31*x+y; }
}
