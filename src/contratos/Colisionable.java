package contratos;

import java.awt.geom.Rectangle2D;  //Permite trabajar con rectángulos geométricos

public interface Colisionable {

    // Caja que ocupa en el mundo de tipo Rectangle2D
    Rectangle2D getLimites();

    // Reacción al tocar a otro objeto
    void alColisionar(Colisionable otro);
}
