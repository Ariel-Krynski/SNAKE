package estructuras;

public class PilaEnlazada<T> implements Pila<T> {
    private static class Nodo<T> { T dato; Nodo<T> siguiente; Nodo(T dato, Nodo<T> sig) { this.dato=dato; siguiente=sig; } }
    private Nodo<T> tope;
    private int cantidad;
    public void apilar(T elemento) { tope = new Nodo<>(elemento, tope); cantidad++; }
    public T desapilar() {
        if (estaVacia()) throw new IllegalStateException("La pila está vacía");
        T dato = tope.dato; tope = tope.siguiente; cantidad--; return dato;
    }
    public T tope() {
        if (estaVacia()) throw new IllegalStateException("La pila está vacía");
        return tope.dato;
    }
    public boolean estaVacia() { return cantidad == 0; }
    public int tamanio() { return cantidad; }
    public void vaciar() { tope = null; cantidad = 0; }
}
