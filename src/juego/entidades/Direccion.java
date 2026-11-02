package juego.entidades;

public enum Direccion {

    ARRIBA(0,-1),
    ABAJO(0,1),
    IZQUIERDA(-1,0),
    DERECHA(1,0);

    private final int dx, dy;

    Direccion(int dx, int dy) {
        this.dx=dx; this.dy=dy;

    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public boolean esOpuesta(Direccion otra) {
        return dx + otra.dx == 0 && dy + otra.dy == 0;

    }
}
