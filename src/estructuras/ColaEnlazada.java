package estructuras;

//Implementamos interfaz Cola
public class ColaEnlazada<T> implements Cola<T> {

    //Constructor
    private static class Nodo<T> { T dato; Nodo<T> siguiente; Nodo(T dato) { this.dato=dato; } }

    private Nodo<T> primero, ultimo;
    private int cantidad;

    //Usamos los metodos de la interfaz
    public void encolar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);

        if (ultimo == null) primero = ultimo = nuevo;
        else { ultimo.siguiente = nuevo; ultimo = nuevo; }
        cantidad++;
    }

    public T desencolar() {
        if (estaVacia()) throw new IllegalStateException("La cola está vacía");

        T dato = primero.dato;
        primero = primero.siguiente;

        if (primero == null) ultimo = null;
        cantidad--; return dato;
    }

    public T frente() {
        if (estaVacia()) throw new IllegalStateException("La cola está vacía");
        return primero.dato;
    }

    public boolean estaVacia() {
        return cantidad == 0;

    }

    public int tamanio() {
        return cantidad;

    }

    public void vaciar() {
        primero = ultimo = null; cantidad = 0;
    }
}
