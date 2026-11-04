package estructuras;

// Interfaz que define las operaciones básicas de una lista genérica y permite recorrer sus elementos mediante Iterable
public interface Lista<T> extends Iterable<T> {

    // Altas

    // Agrega un elemento al final de la lista
    void agregar(T elemento);

    // Inserta un elemento en la posición indicada
    void insertar(int indice, T elemento);

    // Consultas

    // Devuelve el elemento ubicado en el índice indicado
    T obtener(int indice);

    // Devuelve el índice donde se encuentra el elemento, o -1 si no existe
    int indiceDe(T elemento);

    // Devuelve la cantidad de elementos de la lista
    int tamanio();

    // Indica si la lista está vacía
    boolean estaVacia();

    // Bajas

    // Elimina y devuelve el elemento ubicado en el índice indicado
    T eliminar(int indice);

    // Elimina todos los elementos de la lista
    void vaciar();
}