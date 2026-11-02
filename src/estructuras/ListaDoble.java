
package estructuras;

public class ListaDoble<T> extends ListaBase<T> {

    private static class Nodo<T> {
        T dato;
        Nodo<T> anterior, siguiente;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo<T> primero, ultimo;
    private int cantidad;

    // Agregar al final
    @Override
    public void agregar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);

        if (primero == null) {
            primero = ultimo = nuevo;
        } else {
            nuevo.anterior = ultimo;
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }

        cantidad++;
    }

    // Insertar en una posición
    @Override
    public void insertar(int indice, T elemento) {
        if (indice < 0 || indice > cantidad) {
            throw new IndexOutOfBoundsException();
        }

        if (indice == cantidad) {
            agregar(elemento);
            return;
        }

        Nodo<T> actual = nodo(indice);
        Nodo<T> nuevo = new Nodo<>(elemento);

        nuevo.siguiente = actual;
        nuevo.anterior = actual.anterior;

        if (actual.anterior == null) {
            primero = nuevo;
        } else {
            actual.anterior.siguiente = nuevo;
        }

        actual.anterior = nuevo;
        cantidad++;
    }

    // Obtener un elemento
    @Override
    public T obtener(int indice) {
        return nodo(indice).dato;
    }

    // Consultar cantidad
    @Override
    public int tamanio() {
        return cantidad;
    }

    // Eliminar por índice
    @Override
    public T eliminar(int indice) {
        Nodo<T> actual = nodo(indice);

        if (actual.anterior == null) {
            primero = actual.siguiente;
        } else {
            actual.anterior.siguiente = actual.siguiente;
        }

        if (actual.siguiente == null) {
            ultimo = actual.anterior;
        } else {
            actual.siguiente.anterior = actual.anterior;
        }

        cantidad--;

        return actual.dato;
    }

    // Vaciar lista
    @Override
    public void vaciar() {
        primero = ultimo = null;
        cantidad = 0;
    }

    // Buscar nodo por índice
    private Nodo<T> nodo(int indice) {
        if (indice < 0 || indice >= cantidad) {
            throw new IndexOutOfBoundsException(
                    "Índice: " + indice
            );
        }

        if (indice < cantidad / 2) {
            Nodo<T> n = primero;

            for (int i = 0; i < indice; i++) {
                n = n.siguiente;
            }

            return n;
        }

        Nodo<T> n = ultimo;

        for (int i = cantidad - 1; i > indice; i--) {
            n = n.anterior;
        }

        return n;
    }
}
