package juego.pantallas;

import motor.Ventana;

public class Main {
    public static void main(String[] args) {
        Ventana.abrir("Snake: Deshacer", 800, 600, new MenuEscena());
    }
}
