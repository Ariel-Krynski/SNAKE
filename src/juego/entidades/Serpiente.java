
package juego.entidades;

import estructuras.Lista;
import java.awt.Color;
import java.awt.Graphics2D;

public class Serpiente {
    private final Lista<Posicion> cuerpo; // indice 0 cabeza, ultimos cola>
    private Direccion direccion; // puede contener 4 valores
    private int crecer;
    private int puntos;
// Constructor
    public Serpiente(Lista<Posicion> cuerpo) { // Estructura de datos que recibe
        if (cuerpo == null) throw new IllegalArgumentException("El cuerpo no puede ser nulo"); // Validacion que el cuerpo no sea null
        this.cuerpo = cuerpo;
        reiniciar(); // deja a la serpiente en su estado inicial
    }
// Deja  la serpiente en su estado inicial
    public void reiniciar() {
        cuerpo.vaciar(); // elimina segmentos anteriores
        cuerpo.agregar(new Posicion(10, 10));
        cuerpo.agregar(new Posicion(9, 10));
        cuerpo.agregar(new Posicion(8, 10));
// empieza la serpiente con tres segmentos, contando la cabeza
        direccion = Direccion.DERECHA; //empieza mirando hacia la derecha
        // Esta no crecimiento ni puntos por eso se encuentran en 0
        crecer = 0;
        puntos = 0;
    }
// Getters y Setters
    public Lista<Posicion> getCuerpo() {
        return cuerpo;
    }

    public Posicion getCabeza() {
        return cuerpo.obtener(0);// Siempre tiene una cabeza
    }

    public Direccion getDireccion() {
        return direccion;
    }
    // Cambia la dirección evitando giros de 180° que causarían una colisión automática
    public void cambiarDireccion(Direccion nueva) {
        if (nueva != null && !nueva.esOpuesta(direccion)) {
            direccion = nueva;
        }
    }
    // Fuerza una nueva dirección sin validaciones (usado al reiniciar o cargar el estado)
    public void restaurarDireccion(Direccion nueva) {
        if (nueva == null) throw new IllegalArgumentException("Dirección inválida");
        direccion = nueva;
    }
    // Mueve la serpiente un paso adelante: inserta la nueva cabeza según la dirección actual
// y elimina la cola al final (a menos que esté en proceso de crecimiento tras comer).
    public void mover() {
        Posicion cabeza = getCabeza();
// Se inserta la cabeza de la serpiente
        cuerpo.insertar(0, new Posicion(
                cabeza.getX() + direccion.getDx(),
                cabeza.getY() + direccion.getDy()
        ));

        if (crecer > 0) {
            crecer--;
        } else {
            cuerpo.eliminar(cuerpo.tamanio() - 1);
        }
    }

    public boolean chocaConsigoMisma() {
        Posicion cabeza = getCabeza(); // obtiene la posicion de la cabeza

        for (int i = 1; i < cuerpo.tamanio(); i++) { // Comienza en 1 ya que en 0 es comnparar con la cabeza y siempre da true
            if (cabeza.equals(cuerpo.obtener(i))) return true; // compara las coordenadas usadas por los segementos si coinciden dan true
        }

        return false;
    }
// indica si una posición dada está ocupada por algún segmento de la serpiente. sirve para saber si una celda está ocupada (pared, otra entidad)
    public boolean contiene(Posicion p) {
        for (int i = 0; i < cuerpo.tamanio(); i++) {
            if (p.equals(cuerpo.obtener(i))) return true;
        }

        return false;
    }

    public void crecer() {
        crecer++; // Incrementa el contendor
    }

    // Regla del deshacer: el crecimiento pendiente se guarda en cada foto y se restaura
    // con Z, para que deshacer el paso en que comió también anule el crecimiento.
    public int getCrecer() {
        return crecer;
    }

    public void setCrecer(int crecer) {
        if (crecer < 0) throw new IllegalArgumentException("Crecimiento inválido");
        this.crecer = crecer;
    }

    public void encoger() {
        if (cuerpo.tamanio() > 3) { // Pregunta primero si el tamaño es mayor a 3 ya que si o si la serpiente debe medir 3
            cuerpo.eliminar(cuerpo.tamanio() - 1); // elimina el ultimo elemento de la cola
        }
    }

    public void sumarPuntos(int valor) {
        puntos += valor; // suma los puntos
        if (puntos < 0) puntos = 0; // si los puntos son menores a 0 los deja en 0
    }

    public void restarPuntos(int valor) {
        sumarPuntos(-valor); // resta los puntos valor de la manzana comida
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {//Asigna directamente la puntuación, validando que no sea negativa.
        if (puntos < 0) throw new IllegalArgumentException("Puntaje inválido");
        this.puntos = puntos;
    }
//Parte visual
    public void dibujar(Graphics2D g, int celda) {
        for (int i = cuerpo.tamanio() - 1; i >= 0; i--) { //dibuja de atrás hacia adelante. Para que la cabeza quede en la capa de arriba
            Posicion p = cuerpo.obtener(i);

            g.setColor(i == 0
                    ? new Color(130, 245, 150) // Cabeza
                    : new Color(55, 190, 105)); // Cuerpo
// Dibuja un cuadrado relleno con esquinas redondeadas dentro de la celda, dejando 1px de margen alrededor.
            g.fillRoundRect(
                    p.getX() * celda + 1,
                    p.getY() * celda + 1,
                    celda - 2,
                    celda - 2,
                    7, 7
            );
// ojos de la serpiente
            if (i == 0) {
                g.setColor(new Color(20, 50, 35));
                g.fillOval(
                        p.getX() * celda + celda / 3,
                        p.getY() * celda + celda / 4,
                        4, 4
                );

                g.fillOval(
                        p.getX() * celda + 2 * celda / 3 - 3,
                        p.getY() * celda + celda / 4,
                        4, 4
                );
            }
        }
    }
}
