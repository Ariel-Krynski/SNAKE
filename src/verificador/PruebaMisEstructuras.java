package verificador;

import estructuras.ListaDoble;
import estructuras.PilaEnlazada;
import estructuras.ColaEnlazada;

public class PruebaMisEstructuras {
    public static void main(String[] args) {
        boolean lista = VerificadorEstructuras.verificarLista(new ListaDoble<Integer>());
        boolean pila = VerificadorEstructuras.verificarPila(new PilaEnlazada<Integer>());
        boolean cola = VerificadorEstructuras.verificarCola(new ColaEnlazada<Integer>());
        System.out.println("Resultado general: " + (lista && pila && cola ? "OK" : "REVISAR"));
    }
}
