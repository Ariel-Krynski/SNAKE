package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

// Clase abstracta que implementa operaciones comunes de una lista genérica
public abstract class ListaBase<T> implements Lista<T> {

    // Operaciones derivadas

    // Indica si la lista está vacía comprobando si su tamaño es igual a cero
    @Override
    public boolean estaVacia() {
        return tamanio() == 0;
    }

    // Busca un elemento en la lista y devuelve su posición, o -1 si no se encuentra
    @Override
    public int indiceDe(T elemento) {
        for (int i = 0; i < tamanio(); i++) {
            T actual = obtener(i);

            // Compara los elementos teniendo en cuenta que pueden ser null
            boolean iguales = actual == null ? elemento == null : actual.equals(elemento);

            if (iguales) {
                return i;
            }
        }
        return -1;
    }


    // Recorrido con for-each

    // Permite recorrer los elementos de la lista utilizando un for-each
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int actual = 0;

            // Comprueba si quedan elementos por recorrer
            @Override
            public boolean hasNext() {
                return actual < tamanio();
            }

            // Devuelve el siguiente elemento de la lista y avanza la posición
            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return obtener(actual++);
            }
        };
    }
}