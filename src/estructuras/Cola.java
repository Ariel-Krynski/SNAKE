package estructuras;

// Interfaz, define las operaciones básicas de una cola de cualquier tipo de datos
public interface Cola<T> {

    // Agrega un elemento al final de la cola
    void encolar(T elemento);

    // Elimina y devuelve el primer elemento de la cola
    T desencolar();

    // Devuelve el primer elemento de la cola sin eliminarlo
    T frente();

    // Consultas

    // Indica si la cola está vacía
    boolean estaVacia();

    // Devuelve la cantidad de elementos que contiene la cola
    int tamanio();

    // Elimina todos los elementos de la cola
    void vaciar();
}