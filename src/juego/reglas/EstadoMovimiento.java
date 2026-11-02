package juego.reglas;

import estructuras.Lista;
import estructuras.ListaDoble;
import juego.entidades.Direccion;
import juego.entidades.Posicion;

public class EstadoMovimiento {

    private final Lista<Posicion> cuerpo = new ListaDoble<>();
    private final Direccion direccion;
    private final int puntos;

    //Constructor
    public EstadoMovimiento(Lista<Posicion> original, Direccion direccion, int puntos) {
        for (int i = 0; i<original.tamanio(); i++) {
            Posicion p = original.obtener(i);
            cuerpo.agregar(new Posicion(p.getX(),p.getY()));
        }

        this.direccion=direccion; this.puntos=puntos;
    }

    public Lista<Posicion> getCuerpo() {
        return cuerpo;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public int getPuntos() {
        return puntos;
    }

}
