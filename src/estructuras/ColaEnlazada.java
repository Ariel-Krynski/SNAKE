package estructuras;

// Clase que implementa la interfaz Cola utilizando nodos enlazados
public class ColaEnlazada<T> implements Cola<T> {

    // Clase interna que representa cada nodo de la cola
    private static class Nodo<T> {
        T dato;                 // Elemento que almacena el nodo
        Nodo<T> siguiente;      // Referencia al siguiente nodo

        // Constructor que inicializa el dato del nodo
        Nodo(T dato) {
            this.dato = dato;
        }
    }

    // Referencias al primer y último nodo de la cola
    private Nodo<T> primero, ultimo;

    // Cantidad de elementos que contiene la cola
    private int cantidad;

    // Agrega un nuevo elemento al final de la cola
    public void encolar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);

        // Si la cola está vacía, el nuevo nodo será el primero y el último
        if (ultimo == null) primero = ultimo = nuevo;
            // Si ya contiene elementos, se enlaza el último nodo con el nuevo.
        else {
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }

        cantidad++;
    }

    // Elimina y devuelve el primer elemento de la cola
    public T desencolar() {
        // Comprobamos que la cola no esté vacía.
        if (estaVacia()) throw new IllegalStateException("La cola está vacía");

        T dato = primero.dato;
        primero = primero.siguiente;

        // Si no quedan elementos, el último también pasa a ser null.
        if (primero == null) ultimo = null;

        cantidad--;
        return dato;
    }

    // Devuelve el primer elemento sin eliminarlo de la cola
    public T frente() {
        if (estaVacia()) throw new IllegalStateException("La cola está vacía");

        return primero.dato;
    }

    // Indica si la cola está vacía
    public boolean estaVacia() {
        return cantidad == 0;
    }

    // Devuelve la cantidad de elementos de la cola
    public int tamanio() {
        return cantidad;
    }

    // Elimina todos los elementos de la cola
    public void vaciar() {
        primero = ultimo = null;
        cantidad = 0;
    }
}