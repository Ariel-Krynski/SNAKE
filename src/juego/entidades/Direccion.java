package juego.entidades;

public enum Direccion { // clase que representa un conjunto fijo y y cerrado de constantes, donde solo existen 4 direcciones
//Llama a enum con sus constructores dx y dy
    ARRIBA(0,-1), //No cambia la columna, sube una fila
    ABAJO(0,1),  //No cambia la columna, baja una fila
    IZQUIERDA(-1,0),// Retrocede una columna, no cambia la fila
    DERECHA(1,0); //Avanza una columna, no cambia la fila

    private final int dx, dy; // dx= horizontal. dy= vertical.

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

    } // impide que el jugador no pueda moverse en sentido contrario, ejemplo en 180 grados de si mismo
}
