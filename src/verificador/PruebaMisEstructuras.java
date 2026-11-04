package verificador;

import estructuras.ListaDoble;
import estructuras.PilaEnlazada;
import estructuras.ColaEnlazada;

// Clase que permite probar el funcionamiento de las estructuras implementadas
public class PruebaMisEstructuras {

    // Método principal desde donde se ejecutan las pruebas
    public static void main(String[] args) {

        // Verifica el funcionamiento de la lista doblemente enlazada
        boolean lista = VerificadorEstructuras.verificarLista(new ListaDoble<Integer>());

        // Verifica el funcionamiento de la pila enlazada
        boolean pila = VerificadorEstructuras.verificarPila(new PilaEnlazada<Integer>());

        // Verifica el funcionamiento de la cola enlazada
        boolean cola = VerificadorEstructuras.verificarCola(new ColaEnlazada<Integer>());

        // Muestra el resultado general según el resultado de las tres verificaciones
        System.out.println("Resultado general: " + (lista && pila && cola ? "OK" : "REVISAR"));
    }
}