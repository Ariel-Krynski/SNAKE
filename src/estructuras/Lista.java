package estructuras;

import java.util.Iterator;

public interface Lista<T> {

    // Agregar...
    void agregar(T elemento);

    void insertar(int indice, T elemento);

    // Consultas
    T obtener(int indice);

    int indiceDe(T elemento);

    int tamanio();

    boolean estaVacia();

    // Eliminar...
    T eliminar(int indice);

    void vaciar();

    boolean contiene(T elemento);

    boolean quitar(T elemento);

    Iterator<T> iterator();
}
