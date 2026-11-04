package juego.entidades;

public interface EfectoComida { // definicion de que hace la comida
    void aplicar(Serpiente serpiente); // Efecto que tiene la serpiente al comer.
    int getPuntos();// puntos que otorga esta comida al jugador al comerla
    String getNombre();// devolver el nombre de la comida
}
