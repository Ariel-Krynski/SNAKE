package juego.reglas;

// Foto de todo lo que un paso de juego puede cambiar. JuegoEscena apila una por paso
// en el historial (Pila), y Z desapila la última: se deshace primero lo último que pasó.
//
// Regla del deshacer: Z rebobina el paso completo. Vuelven el cuerpo, la dirección,
// el crecimiento pendiente, la comida y el contador de pasos sin comer; a los puntos
// se les descuenta lo ganado en ese paso y el costo de deshacer.

import estructuras.Lista;
import estructuras.ListaDoble;
import juego.entidades.Comida;
import juego.entidades.Direccion;
import juego.entidades.Posicion;

public class EstadoMovimiento {

    private final Lista<Posicion> cuerpo = new ListaDoble<>();
    private final Direccion direccion;
    // Segmentos que la serpiente todavía tenía que crecer. Sin esto, deshacer el paso en
    // que comió le quitaba los puntos pero la dejaba crecer igual.
    private final int crecer;
    // La comida que había en el tablero antes del paso. Se guarda la referencia sin
    // copiarla porque cada comida nueva es un objeto nuevo: la vieja no cambia.
    private final Comida comida;
    private final int pasosDesdeComida;
    // Lo que el paso sumó o restó al puntaje (negativo si comió una podrida). Se guarda
    // la diferencia y no el puntaje: así cada Z cobra sobre los puntos actuales, y
    // varios Z seguidos cobran el costo cada uno.
    private int puntosGanados;

    // Constructor: se llama ANTES de mover, para copiar el estado previo al paso.
    public EstadoMovimiento(Lista<Posicion> original, Direccion direccion, int crecer,
                            Comida comida, int pasosDesdeComida) {
        if (original == null || direccion == null || comida == null) {
            throw new IllegalArgumentException("El estado guardado está incompleto");
        }
        if (crecer < 0 || pasosDesdeComida < 0) {
            throw new IllegalArgumentException("El estado guardado tiene valores negativos");
        }
        // Se copia el cuerpo: la lista de la serpiente cambia en cada paso, y si se
        // guardara la misma, todas las fotos terminarían iguales a la actual.
        for (int i = 0; i<original.tamanio(); i++) {
            Posicion p = original.obtener(i);
            cuerpo.agregar(new Posicion(p.getX(),p.getY()));
        }

        this.direccion = direccion;
        this.crecer = crecer;
        this.comida = comida;
        this.pasosDesdeComida = pasosDesdeComida;
    }

    // Se completa al final del paso, cuando ya se sabe si comió. Acepta negativos a
    // propósito: la manzana podrida resta puntos.
    public void setPuntosGanados(int puntosGanados) {
        this.puntosGanados = puntosGanados;
    }

    public Lista<Posicion> getCuerpo() {
        return cuerpo;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public int getCrecer() {
        return crecer;
    }

    public Comida getComida() {
        return comida;
    }

    public int getPasosDesdeComida() {
        return pasosDesdeComida;
    }

    public int getPuntosGanados() {
        return puntosGanados;
    }

}
