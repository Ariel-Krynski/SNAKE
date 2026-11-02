package estructuras;

import java.util.Iterator;

public class ListaDoble<T> implements Lista<T> {
    private static class Nodo<T> {
        T dato;
        Nodo<T> anterior, siguiente;
        Nodo(T dato) { this.dato = dato; }
    }
    private Nodo<T> primero, ultimo;
    private int cantidad;

    public void agregar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (primero == null) primero = ultimo = nuevo;
        else { nuevo.anterior = ultimo; ultimo.siguiente = nuevo; ultimo = nuevo; }
        cantidad++;
    }
    public void insertar(int indice, T elemento) {
        if (indice < 0 || indice > cantidad) throw new IndexOutOfBoundsException();
        if (indice == cantidad) { agregar(elemento); return; }
        Nodo<T> actual = nodo(indice);
        Nodo<T> nuevo = new Nodo<>(elemento);
        nuevo.siguiente = actual; nuevo.anterior = actual.anterior;
        if (actual.anterior == null) primero = nuevo;
        else actual.anterior.siguiente = nuevo;
        actual.anterior = nuevo; cantidad++;
    }
    public T obtener(int indice) { return nodo(indice).dato; }
    public int indiceDe(T elemento) {
        Nodo<T> actual = primero; int i = 0;
        while (actual != null) {
            if (elemento == null ? actual.dato == null : elemento.equals(actual.dato)) return i;
            actual = actual.siguiente; i++;
        }
        return -1;
    }
    public int tamanio() { return cantidad; }
    public boolean estaVacia() { return cantidad == 0; }
    public T eliminar(int indice) {
        Nodo<T> actual = nodo(indice);
        if (actual.anterior == null) primero = actual.siguiente;
        else actual.anterior.siguiente = actual.siguiente;
        if (actual.siguiente == null) ultimo = actual.anterior;
        else actual.siguiente.anterior = actual.anterior;
        cantidad--; return actual.dato;
    }
    public void vaciar() { primero = ultimo = null; cantidad = 0; }

    @Override
    public boolean contiene(T elemento) {
        return false;
    }

    @Override
    public boolean quitar(T elemento) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    private Nodo<T> nodo(int indice) {
        if (indice < 0 || indice >= cantidad) throw new IndexOutOfBoundsException("Índice: " + indice);
        if (indice < cantidad / 2) {
            Nodo<T> n = primero;
            for (int i = 0; i < indice; i++) n = n.siguiente;
            return n;
        }
        Nodo<T> n = ultimo;
        for (int i = cantidad - 1; i > indice; i--) n = n.anterior;
        return n;
    }
}
