package estructuras;

// Clase que implementa la interfaz Pila utilizando nodos enlazados
public class PilaEnlazada<T> implements Pila<T> {

    // Clase interna que representa cada nodo de la pila
    private static class Nodo<T> {
        T dato;                 // Elemento que almacena el nodo
        Nodo<T> siguiente;      // Referencia al siguiente nodo

        // Constructor que inicializa el dato y la referencia al siguiente nodo
        Nodo(T dato, Nodo<T> sig) {
            this.dato = dato;
            siguiente = sig;
        }
    }

    // Referencia al elemento que se encuentra en la parte superior de la pila
    private Nodo<T> tope;

    // Cantidad de elementos que contiene la pila
    private int cantidad;

    // Agrega un nuevo elemento en la parte superior de la pila
    public void apilar(T elemento) {
        tope = new Nodo<>(elemento, tope);
        cantidad++;
    }

    // Elimina y devuelve el elemento que se encuentra en la parte superior de la pila
    public T desapilar() {

        // Comprueba que la pila no esté vacía
        if (estaVacia()) throw new IllegalStateException("La pila está vacía");

        T dato = tope.dato;
        tope = tope.siguiente;
        cantidad--;
        return dato;
    }

    // Devuelve el elemento que está en la parte superior sin eliminarlo
    public T tope() {

        // Comprueba que la pila no esté vacía
        if (estaVacia()) throw new IllegalStateException("La pila está vacía");

        return tope.dato;
    }

    // Indica si la pila está vacía
    public boolean estaVacia() {
        return cantidad == 0;
    }

    // Devuelve la cantidad de elementos que contiene la pila
    public int tamanio() {
        return cantidad;
    }

    // Elimina todos los elementos de la pila
    public void vaciar() {
        tope = null;
        cantidad = 0;
    }
}