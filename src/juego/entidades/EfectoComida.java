package juego.entidades;

public interface EfectoComida {
    void aplicar(Serpiente serpiente);
    int getPuntos();
    String getNombre();
}
