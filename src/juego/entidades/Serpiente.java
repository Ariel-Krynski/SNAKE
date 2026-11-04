
package juego.entidades;

import estructuras.Lista;
import java.awt.Color;
import java.awt.Graphics2D;

public class Serpiente {
    private final Lista<Posicion> cuerpo;
    private Direccion direccion;
    private int crecer;
    private int puntos;

    public Serpiente(Lista<Posicion> cuerpo) {
        if (cuerpo == null) throw new IllegalArgumentException("El cuerpo no puede ser nulo");
        this.cuerpo = cuerpo;
        reiniciar();
    }

    public void reiniciar() {
        cuerpo.vaciar();
        cuerpo.agregar(new Posicion(10, 10));
        cuerpo.agregar(new Posicion(9, 10));
        cuerpo.agregar(new Posicion(8, 10));

        direccion = Direccion.DERECHA;
        crecer = 0;
        puntos = 0;
    }

    public Lista<Posicion> getCuerpo() {
        return cuerpo;
    }

    public Posicion getCabeza() {
        return cuerpo.obtener(0);
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void cambiarDireccion(Direccion nueva) {
        if (nueva != null && !nueva.esOpuesta(direccion)) {
            direccion = nueva;
        }
    }

    public void restaurarDireccion(Direccion nueva) {
        if (nueva == null) throw new IllegalArgumentException("Dirección inválida");
        direccion = nueva;
    }

    public void mover() {
        Posicion cabeza = getCabeza();

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
        Posicion cabeza = getCabeza();

        for (int i = 1; i < cuerpo.tamanio(); i++) {
            if (cabeza.equals(cuerpo.obtener(i))) return true;
        }

        return false;
    }

    public boolean contiene(Posicion p) {
        for (int i = 0; i < cuerpo.tamanio(); i++) {
            if (p.equals(cuerpo.obtener(i))) return true;
        }

        return false;
    }

    public void crecer() {
        crecer++;
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
        if (cuerpo.tamanio() > 3) {
            cuerpo.eliminar(cuerpo.tamanio() - 1);
        }
    }

    public void sumarPuntos(int valor) {
        puntos += valor;
        if (puntos < 0) puntos = 0;
    }

    public void restarPuntos(int valor) {
        sumarPuntos(-valor);
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        if (puntos < 0) throw new IllegalArgumentException("Puntaje inválido");
        this.puntos = puntos;
    }

    public void dibujar(Graphics2D g, int celda) {
        for (int i = cuerpo.tamanio() - 1; i >= 0; i--) {
            Posicion p = cuerpo.obtener(i);

            g.setColor(i == 0
                    ? new Color(130, 245, 150)
                    : new Color(55, 190, 105));

            g.fillRoundRect(
                    p.getX() * celda + 1,
                    p.getY() * celda + 1,
                    celda - 2,
                    celda - 2,
                    7, 7
            );

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
