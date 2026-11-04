package estructuras;

// Clase que implementa una lista doblemente enlazada
public class ListaDoble<T> extends ListaBase<T> {

    // Clase interna que representa cada nodo de la lista
    private static class Nodo<T> {
        T dato;                         // Elemento que almacena el nodo
        Nodo<T> anterior, siguiente;    // Referencias al nodo anterior y al siguiente

        // Constructor que inicializa el dato del nodo
        Nodo(T dato) {
            this.dato = dato;
        }
    }

    // Referencias al primer y último nodo de la lista
    private Nodo<T> primero, ultimo;

    // Cantidad de elementos que contiene la lista
    private int cantidad;

    // Agregar al final

    // Agrega un nuevo elemento al final de la lista
    @Override
    public void agregar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);

        // Si la lista está vacía, el nuevo nodo será el primero y el último
        if (primero == null) {
            primero = ultimo = nuevo;
        } else {
            // Enlaza el nuevo nodo con el último y actualiza la referencia
            nuevo.anterior = ultimo;
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }

        cantidad++;
    }

    // Insertar en una posición

    // Inserta un elemento en la posición indicada
    @Override
    public void insertar(int indice, T elemento) {

        // Comprueba que el índice esté dentro de los límites permitidos
        if (indice < 0 || indice > cantidad) {
            throw new IndexOutOfBoundsException();
        }

        // Si el índice coincide con el tamaño, agrega el elemento al final
        if (indice == cantidad) {
            agregar(elemento);
            return;
        }

        // Busca el nodo que actualmente ocupa la posición indicada
        Nodo<T> actual = nodo(indice);
        Nodo<T> nuevo = new Nodo<>(elemento);

        // Enlaza el nuevo nodo con el nodo actual y su anterior
        nuevo.siguiente = actual;
        nuevo.anterior = actual.anterior;

        // Si no existe un nodo anterior, el nuevo pasa a ser el primero
        if (actual.anterior == null) {
            primero = nuevo;
        } else {
            actual.anterior.siguiente = nuevo;
        }

        // Actualiza la referencia anterior del nodo actual
        actual.anterior = nuevo;
        cantidad++;
    }

    // Obtener un elemento

    // Devuelve el dato del nodo ubicado en el índice indicado
    @Override
    public T obtener(int indice) {
        return nodo(indice).dato;
    }

    // Consultar cantidad

    // Devuelve la cantidad de elementos que contiene la lista
    @Override
    public int tamanio() {
        return cantidad;
    }

    // Eliminar por índice

    // Elimina y devuelve el elemento ubicado en la posición indicada
    @Override
    public T eliminar(int indice) {
        Nodo<T> actual = nodo(indice);

        // Si es el primer nodo, actualiza la referencia al primero
        if (actual.anterior == null) {
            primero = actual.siguiente;
        } else {
            actual.anterior.siguiente = actual.siguiente;
        }

        // Si es el último nodo, actualiza la referencia al último
        if (actual.siguiente == null) {
            ultimo = actual.anterior;
        } else {
            actual.siguiente.anterior = actual.anterior;
        }

        cantidad--;

        return actual.dato;
    }

    // Vaciar lista

    // Elimina todos los elementos de la lista
    @Override
    public void vaciar() {
        primero = ultimo = null;
        cantidad = 0;
    }

    // Buscar nodo por índice

    // Busca y devuelve el nodo ubicado en el índice indicado
    private Nodo<T> nodo(int indice) {

        // Comprueba que el índice corresponda a una posición existente
        if (indice < 0 || indice >= cantidad) {
            throw new IndexOutOfBoundsException(
                    "Índice: " + indice
            );
        }

        // Si el índice está en la primera mitad, comienza a recorrer desde el principio
        if (indice < cantidad / 2) {
            Nodo<T> n = primero;

            for (int i = 0; i < indice; i++) {
                n = n.siguiente;
            }

            return n;
        }

        // Si está en la segunda mitad, comienza a recorrer desde el final
        Nodo<T> n = ultimo;

        for (int i = cantidad - 1; i > indice; i--) {
            n = n.anterior;
        }

        return n;
    }
}