package estructuras;

// Interfaz que define las operaciones básicas de una pila genérica
public interface Pila<T> {


    // Agrega un elemento en la parte superior de la pila
    void apilar(T elemento);

    // Elimina y devuelve el elemento que se encuentra en la parte superior de la pila
    T desapilar();

    // Devuelve el elemento que está en la parte superior sin eliminarlo
    T tope();

    // Consultas

    // Indica si la pila está vacía
    boolean estaVacia();

    // Devuelve la cantidad de elementos que contiene la pila
    int tamanio();

    // Elimina todos los elementos de la pila
    void vaciar();
}