package juego.pantallas;

import motor.Ventana;


 // Punto de entrada del juego. Solo arranca la ventana; no tiene lógica.
 // Se ejecuta con:  ./ejecutar.sh juego.pantallas.Main   (o ejecutar.bat)
 // Si se ejecuta sin argumento corre la demo del kit (demo.Main), no este juego.

public class Main {

    // main es el método que la JVM busca para empezar.
    public static void main(String[] args) {
        // Ventana.abrir(título, ancho, alto, escenaInicial) es del kit:
        // crea la ventana de Swing y arranca el bucle de juego.
        // 800x600 = tablero de 40x20 = 800 px de ancho por 25x20 = 500 px de alto,
        // más 100 px de franja inferior para el HUD (ver JuegoEscena).
        // La escena inicial es el menú; de ahí el juego fluye por las escenas.
        Ventana.abrir("Snake: Deshacer", 800, 600, new MenuEscena());
    }
}
