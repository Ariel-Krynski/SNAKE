package verificador;

import estructuras.Cola;
import estructuras.Lista;
import estructuras.Pila;
import java.util.Random;

// Clase encargada de verificar el correcto funcionamiento de las estructuras implementadas
public final class VerificadorEstructuras {

    // Estado de una verificación
    private final String nombre; // Nombre de la estructura que se está verificando
    private int total;            // Cantidad total de verificaciones realizadas
    private int fallas;           // Cantidad de verificaciones que fallaron

    // Constructor privado que inicializa el nombre de la estructura
    private VerificadorEstructuras(String nombre) {
        this.nombre = nombre;
    }

    // Entradas públicas

    // Crea un verificador y ejecuta las pruebas correspondientes a la lista
    public static boolean verificarLista(Lista<Integer> lista) {
        VerificadorEstructuras v = new VerificadorEstructuras("Lista");
        v.pruebasLista(lista);
        return v.terminar();
    }

    // Crea un verificador y ejecuta las pruebas correspondientes a la pila
    public static boolean verificarPila(Pila<Integer> pila) {
        VerificadorEstructuras v = new VerificadorEstructuras("Pila");
        v.pruebasPila(pila);
        return v.terminar();
    }

    // Crea un verificador y ejecuta las pruebas correspondientes a la cola
    public static boolean verificarCola(Cola<Integer> cola) {
        VerificadorEstructuras v = new VerificadorEstructuras("Cola");
        v.pruebasCola(cola);
        return v.terminar();
    }

    // Pruebas de Lista

    // Ejecuta diferentes pruebas para comprobar el funcionamiento de la lista
    private void pruebasLista(Lista<Integer> lista) {
        System.out.println("Verificando " + nombre + "...");

        // Comprueba que la lista esté vacía al comenzar
        protegido("estado inicial", () -> {
            lista.vaciar();
            comprobar("recién vaciada: estaVacia() debe ser true", lista.estaVacia());
            comprobar("recién vaciada: tamanio() debe ser 0", lista.tamanio() == 0);
        });

        // Comprueba que se puedan agregar elementos y obtenerlos manteniendo el orden
        protegido("agregar y obtener", () -> {
            lista.agregar(10);
            lista.agregar(20);
            lista.agregar(30);
            comprobar("tras agregar 3: tamanio() debe ser 3", lista.tamanio() == 3);
            comprobar("tras agregar 3: estaVacia() debe ser false", !lista.estaVacia());
            comprobar("agregar mantiene el orden: contenido " + contenido(lista), contenido(lista).equals("10,20,30"));
        });

        // Comprueba que se puedan insertar elementos al inicio, en el medio y al final
        protegido("insertar", () -> {
            lista.insertar(0, 5);
            comprobar("insertar al inicio: contenido " + contenido(lista), contenido(lista).equals("5,10,20,30"));
            lista.insertar(2, 15);
            comprobar("insertar en el medio: contenido " + contenido(lista), contenido(lista).equals("5,10,15,20,30"));
            lista.insertar(lista.tamanio(), 99);
            comprobar("insertar al final: contenido " + contenido(lista), contenido(lista).equals("5,10,15,20,30,99"));
            comprobar("insertar: tamanio() debe ser 6", lista.tamanio() == 6);
        });

        // Comprueba que se puedan eliminar elementos de distintas posiciones y que se devuelva el valor correcto
        protegido("eliminar por índice", () -> {
            Integer primero = lista.eliminar(0);
            comprobar("eliminar(0) debe devolver 5", primero != null && primero == 5);
            comprobar("tras eliminar el primero: " + contenido(lista), contenido(lista).equals("10,15,20,30,99"));
            Integer ultimo = lista.eliminar(lista.tamanio() - 1);
            comprobar("eliminar el último debe devolver 99", ultimo != null && ultimo == 99);
            Integer medio = lista.eliminar(1);
            comprobar("eliminar(1) debe devolver 15", medio != null && medio == 15);
            comprobar("tras eliminar el del medio: " + contenido(lista), contenido(lista).equals("10,20,30"));
            comprobar("eliminar: tamanio() debe ser 3", lista.tamanio() == 3);
        });

        // Comprueba que la búsqueda devuelva el índice correcto y -1 cuando el elemento no existe
        protegido("búsqueda", () -> {
            comprobar("indiceDe(20) debe ser 1", lista.indiceDe(20) == 1);
            comprobar("indiceDe(10) debe ser 0", lista.indiceDe(10) == 0);
            comprobar("indiceDe(30) debe ser 2", lista.indiceDe(30) == 2);
            comprobar("indiceDe de un valor ausente debe ser -1", lista.indiceDe(77) == -1);
            lista.eliminar(1);
            comprobar("tras eliminar el 20: " + contenido(lista), contenido(lista).equals("10,30"));
            comprobar("indiceDe(20) tras eliminarlo debe ser -1", lista.indiceDe(20) == -1);
        });

        // Comprueba que los métodos que utilizan índices rechacen posiciones inválidas
        protegido("índices inválidos", () -> {
            esperar("obtener(-1)", IndexOutOfBoundsException.class, () -> lista.obtener(-1));
            esperar("obtener(tamanio())", IndexOutOfBoundsException.class, () -> lista.obtener(lista.tamanio()));
            esperar("insertar(-1, x)", IndexOutOfBoundsException.class, () -> lista.insertar(-1, 1));
            esperar("insertar(tamanio()+1, x)", IndexOutOfBoundsException.class, () -> lista.insertar(lista.tamanio() + 1, 1));
            esperar("eliminar(-1)", IndexOutOfBoundsException.class, () -> lista.eliminar(-1));
            esperar("eliminar(tamanio())", IndexOutOfBoundsException.class, () -> lista.eliminar(lista.tamanio()));
            lista.vaciar();
            esperar("obtener(0) en lista vacía", IndexOutOfBoundsException.class, () -> lista.obtener(0));
            esperar("eliminar(0) en lista vacía", IndexOutOfBoundsException.class, () -> lista.eliminar(0));
        });

        // Comprueba que se puedan eliminar todos los elementos y volver a utilizar la lista
        protegido("vaciar", () -> {
            lista.agregar(1);
            lista.agregar(2);
            lista.vaciar();
            comprobar("tras vaciar: estaVacia() debe ser true", lista.estaVacia());
            lista.agregar(7);
            comprobar("tras vaciar y agregar: " + contenido(lista), contenido(lista).equals("7"));
            lista.vaciar();
        });

        // Comprueba que la lista pueda almacenar una cantidad elevada de elementos
        protegido("crecimiento (2000 elementos)", () -> {
            for (int i = 0; i < 2000; i++) {
                lista.agregar(i);
            }
            comprobar("2000 elementos: tamanio() debe ser 2000", lista.tamanio() == 2000);
            comprobar("2000 elementos: obtener(0) debe ser 0", lista.obtener(0) == 0);
            comprobar("2000 elementos: obtener(1999) debe ser 1999", lista.obtener(1999) == 1999);

            // Calcula la suma de todos los elementos para comprobar que se puedan recorrer correctamente
            long suma = 0;
            for (int i = 0; i < lista.tamanio(); i++) {
                suma += lista.obtener(i);
            }
            comprobar("2000 elementos: la suma debe ser 1999000", suma == 1999000L);
            lista.vaciar();
        });

        // Comprueba el funcionamiento de la lista mediante operaciones aleatorias comparándola con un modelo
        protegido("prueba aleatoria contra un modelo", () -> {
            Random azar = new Random(42); // Generador de números aleatorios
            int[] modelo = new int[4000]; // Arreglo que representa el resultado esperado
            int n = 0;                     // Cantidad de elementos del modelo
            boolean coincide = true;      // Indica si la lista coincide con el modelo
            String detalle = "";          // Almacena información sobre el error encontrado

            // Realiza hasta 3000 operaciones aleatorias mientras los resultados coincidan
            for (int paso = 0; paso < 3000 && coincide; paso++) {
                int operacion = azar.nextInt(10);
                int valor = azar.nextInt(1000);

                // Agrega un elemento al final cuando corresponde
                if (n == 0 || operacion <= 3) {
                    lista.agregar(valor);
                    modelo[n++] = valor;

                    // Inserta un elemento en una posición aleatoria
                } else if (operacion <= 6) {
                    int indice = azar.nextInt(n + 1);
                    lista.insertar(indice, valor);

                    // Desplaza los elementos del modelo para dejar espacio al nuevo
                    System.arraycopy(modelo, indice, modelo, indice + 1, n - indice);
                    modelo[indice] = valor;
                    n++;

                    // Elimina un elemento de una posición aleatoria
                } else {
                    int indice = azar.nextInt(n);
                    Integer devuelto = lista.eliminar(indice);
                    int esperado = modelo[indice];

                    // Desplaza los elementos del modelo para ocupar el espacio liberado
                    System.arraycopy(modelo, indice + 1, modelo, indice, n - indice - 1);
                    n--;

                    // Comprueba que el elemento eliminado sea el esperado
                    if (devuelto == null || devuelto != esperado) {
                        coincide = false;
                        detalle = "paso " + paso + ": eliminar(" + indice + ") devolvió " + devuelto + " y debía devolver " + esperado;
                    }
                }

                // Comprueba que la cantidad de elementos de la lista coincida con el modelo
                if (coincide && lista.tamanio() != n) {
                    coincide = false;
                    detalle = "paso " + paso + ": tamanio() es " + lista.tamanio() + " y debía ser " + n;
                }
            }

            // Si hasta el momento no hubo errores, compara todos los elementos de ambas estructuras
            if (coincide) {
                for (int i = 0; i < n; i++) {
                    if (lista.obtener(i) != modelo[i]) {
                        coincide = false;
                        detalle = "posición " + i + ": hay " + lista.obtener(i) + " y debía haber " + modelo[i];
                        break;
                    }
                }
            }

            comprobar("3000 operaciones al azar coinciden con el modelo" + (coincide ? "" : " (" + detalle + ")"), coincide);
            lista.vaciar();
        });
    }

    // Pruebas de Pila

    // Ejecuta diferentes pruebas para comprobar el funcionamiento de la pila
    private void pruebasPila(Pila<Integer> pila) {
        System.out.println("Verificando " + nombre + "...");

        // Comprueba el estado inicial y el comportamiento de los métodos cuando la pila está vacía
        protegido("estado inicial", () -> {
            pila.vaciar();
            comprobar("recién vaciada: estaVacia() debe ser true", pila.estaVacia());
            comprobar("recién vaciada: tamanio() debe ser 0", pila.tamanio() == 0);
            esperar("desapilar() en pila vacía", RuntimeException.class, () -> pila.desapilar());
            esperar("tope() en pila vacía", RuntimeException.class, () -> pila.tope());
        });

        // Comprueba que la pila respete el principio LIFO
        protegido("orden LIFO", () -> {
            pila.apilar(1);
            pila.apilar(2);
            pila.apilar(3);
            comprobar("tras apilar 3: tamanio() debe ser 3", pila.tamanio() == 3);
            comprobar("tope() debe ser 3", pila.tope() == 3);
            comprobar("tope() no debe quitar el elemento", pila.tamanio() == 3);
            comprobar("desapilar() debe devolver 3", pila.desapilar() == 3);
            comprobar("desapilar() debe devolver 2", pila.desapilar() == 2);
            comprobar("desapilar() debe devolver 1", pila.desapilar() == 1);
            comprobar("al vaciarse: estaVacia() debe ser true", pila.estaVacia());
        });

        // Comprueba que se puedan intercalar operaciones de apilado y desapilado
        protegido("operaciones intercaladas", () -> {
            pila.apilar(1);
            pila.apilar(2);
            comprobar("desapilar() debe devolver 2", pila.desapilar() == 2);
            pila.apilar(3);
            comprobar("tope() debe ser 3", pila.tope() == 3);
            comprobar("desapilar() debe devolver 3", pila.desapilar() == 3);
            comprobar("desapilar() debe devolver 1", pila.desapilar() == 1);
            esperar("desapilar() tras vaciarse", RuntimeException.class, () -> pila.desapilar());
        });

        // Comprueba que la pila pueda almacenar y devolver una cantidad elevada de elementos
        protegido("crecimiento (3000 elementos)", () -> {
            for (int i = 0; i < 3000; i++) {
                pila.apilar(i);
            }
            comprobar("3000 elementos: tamanio() debe ser 3000", pila.tamanio() == 3000);

            // Comprueba que los elementos salgan en orden inverso al que ingresaron
            boolean ordenCorrecto = true;
            for (int i = 2999; i >= 0; i--) {
                if (pila.desapilar() != i) {
                    ordenCorrecto = false;
                    break;
                }
            }
            comprobar("3000 elementos salen en orden inverso", ordenCorrecto);
            comprobar("al terminar: estaVacia() debe ser true", pila.estaVacia());
        });

        // Comprueba que la pila pueda vaciarse y volver a utilizarse
        protegido("vaciar", () -> {
            pila.apilar(5);
            pila.apilar(6);
            pila.vaciar();
            comprobar("tras vaciar: estaVacia() debe ser true", pila.estaVacia());
            comprobar("tras vaciar: tamanio() debe ser 0", pila.tamanio() == 0);
            pila.apilar(9);
            comprobar("tras vaciar y apilar: tope() debe ser 9", pila.tope() == 9);
            pila.vaciar();
        });

        // Comprueba el funcionamiento de la pila mediante operaciones aleatorias comparándola con un modelo
        protegido("prueba aleatoria contra un modelo", () -> {
            Random azar = new Random(7);
            int[] modelo = new int[5000]; // Arreglo que representa el contenido esperado de la pila
            int n = 0;                    // Cantidad de elementos del modelo
            boolean coincide = true;
            String detalle = "";

            // Realiza hasta 4000 operaciones aleatorias mientras los resultados coincidan
            for (int paso = 0; paso < 4000 && coincide; paso++) {

                // Decide aleatoriamente si agrega o elimina un elemento
                if (n == 0 || azar.nextInt(10) < 6) {
                    int valor = azar.nextInt(1000);
                    pila.apilar(valor);
                    modelo[n++] = valor;
                } else {
                    int devuelto = pila.desapilar();
                    int esperado = modelo[--n];

                    // Comprueba que el elemento eliminado sea el último que ingresó
                    if (devuelto != esperado) {
                        coincide = false;
                        detalle = "paso " + paso + ": desapilar() devolvió " + devuelto + " y debía devolver " + esperado;
                    }
                }

                // Comprueba que la cantidad de elementos coincida con el modelo
                if (coincide && pila.tamanio() != n) {
                    coincide = false;
                    detalle = "paso " + paso + ": tamanio() es " + pila.tamanio() + " y debía ser " + n;
                }

                // Comprueba que el elemento del tope sea el esperado
                if (coincide && n > 0 && pila.tope() != modelo[n - 1]) {
                    coincide = false;
                    detalle = "paso " + paso + ": tope() es " + pila.tope() + " y debía ser " + modelo[n - 1];
                }
            }

            comprobar("4000 operaciones al azar coinciden con el modelo" + (coincide ? "" : " (" + detalle + ")"), coincide);
            pila.vaciar();
        });
    }

    // Pruebas de Cola

    // Ejecuta diferentes pruebas para comprobar el funcionamiento de la cola
    private void pruebasCola(Cola<Integer> cola) {
        System.out.println("Verificando " + nombre + "...");

        // Comprueba el estado inicial y el comportamiento de los métodos cuando la cola está vacía
        protegido("estado inicial", () -> {
            cola.vaciar();
            comprobar("recién vaciada: estaVacia() debe ser true", cola.estaVacia());
            comprobar("recién vaciada: tamanio() debe ser 0", cola.tamanio() == 0);
            esperar("desencolar() en cola vacía", RuntimeException.class, () -> cola.desencolar());
            esperar("frente() en cola vacía", RuntimeException.class, () -> cola.frente());
        });

        // Comprueba que la cola respete el principio FIFO
        protegido("orden FIFO", () -> {
            cola.encolar(1);
            cola.encolar(2);
            cola.encolar(3);
            comprobar("tras encolar 3: tamanio() debe ser 3", cola.tamanio() == 3);
            comprobar("frente() debe ser 1", cola.frente() == 1);
            comprobar("frente() no debe quitar el elemento", cola.tamanio() == 3);
            comprobar("desencolar() debe devolver 1", cola.desencolar() == 1);
            comprobar("desencolar() debe devolver 2", cola.desencolar() == 2);
            comprobar("desencolar() debe devolver 3", cola.desencolar() == 3);
            comprobar("al vaciarse: estaVacia() debe ser true", cola.estaVacia());
        });

        // Comprueba que la cola pueda seguir agregando elementos después de varias eliminaciones
        protegido("crecimiento con el inicio desplazado", () -> {
            for (int i = 1; i <= 5; i++) {
                cola.encolar(i);
            }
            for (int k = 1; k <= 3; k++) {
                cola.desencolar();
            }
            for (int i = 6; i <= 105; i++) {
                cola.encolar(i);
            }
            comprobar("tamanio() debe ser 102", cola.tamanio() == 102);

            // Comprueba que todos los elementos salgan respetando el orden de llegada
            boolean ordenCorrecto = true;
            for (int esperado = 4; esperado <= 105; esperado++) {
                if (cola.desencolar() != esperado) {
                    ordenCorrecto = false;
                    break;
                }
            }
            comprobar("los elementos salen en el orden en que entraron", ordenCorrecto);
            comprobar("al terminar: estaVacia() debe ser true", cola.estaVacia());
        });

        // Comprueba que se puedan realizar muchas operaciones consecutivas sin perder elementos
        protegido("uso prolongado (10000 ciclos)", () -> {
            boolean correcto = true;
            for (int i = 0; i < 10000; i++) {
                cola.encolar(i);
                if (cola.desencolar() != i) {
                    correcto = false;
                    break;
                }
            }
            comprobar("encolar y desencolar 10000 veces sin perder datos", correcto);
            comprobar("al terminar: estaVacia() debe ser true", cola.estaVacia());
        });

        // Comprueba que la cola pueda almacenar una cantidad elevada de elementos
        protegido("crecimiento (3000 elementos)", () -> {
            for (int i = 0; i < 3000; i++) {
                cola.encolar(i);
            }
            comprobar("3000 elementos: tamanio() debe ser 3000", cola.tamanio() == 3000);

            // Comprueba que los elementos salgan en el mismo orden en que ingresaron
            boolean ordenCorrecto = true;
            for (int i = 0; i < 3000; i++) {
                if (cola.desencolar() != i) {
                    ordenCorrecto = false;
                    break;
                }
            }
            comprobar("3000 elementos salen en orden de llegada", ordenCorrecto);
        });

        // Comprueba que la cola pueda vaciarse y volver a utilizarse
        protegido("vaciar", () -> {
            cola.encolar(5);
            cola.encolar(6);
            cola.vaciar();
            comprobar("tras vaciar: estaVacia() debe ser true", cola.estaVacia());
            comprobar("tras vaciar: tamanio() debe ser 0", cola.tamanio() == 0);
            cola.encolar(9);
            comprobar("tras vaciar y encolar: frente() debe ser 9", cola.frente() == 9);
            cola.vaciar();
        });

        // Comprueba el funcionamiento de la cola mediante operaciones aleatorias comparándola con un modelo
        protegido("prueba aleatoria contra un modelo", () -> {
            Random azar = new Random(11);
            int[] modelo = new int[6000]; // Arreglo que representa el contenido esperado de la cola
            int inicio = 0;               // Posición del primer elemento del modelo
            int fin = 0;                  // Posición donde se agrega el próximo elemento
            boolean coincide = true;
            String detalle = "";

            // Realiza hasta 5000 operaciones aleatorias mientras los resultados coincidan
            for (int paso = 0; paso < 5000 && coincide; paso++) {
                int n = fin - inicio;

                // Decide aleatoriamente si agrega o elimina un elemento
                if (n == 0 || azar.nextInt(10) < 6) {
                    int valor = azar.nextInt(1000);
                    cola.encolar(valor);
                    modelo[fin++] = valor;
                } else {
                    int devuelto = cola.desencolar();
                    int esperado = modelo[inicio++];

                    // Comprueba que se haya eliminado el primer elemento que ingresó
                    if (devuelto != esperado) {
                        coincide = false;
                        detalle = "paso " + paso + ": desencolar() devolvió " + devuelto + " y debía devolver " + esperado;
                    }
                }

                // Comprueba que la cantidad de elementos coincida con el modelo
                if (coincide && cola.tamanio() != fin - inicio) {
                    coincide = false;
                    detalle = "paso " + paso + ": tamanio() es " + cola.tamanio() + " y debía ser " + (fin - inicio);
                }

                // Comprueba que el primer elemento de la cola sea el esperado
                if (coincide && fin - inicio > 0 && cola.frente() != modelo[inicio]) {
                    coincide = false;
                    detalle = "paso " + paso + ": frente() es " + cola.frente() + " y debía ser " + modelo[inicio];
                }
            }

            comprobar("5000 operaciones al azar coinciden con el modelo" + (coincide ? "" : " (" + detalle + ")"), coincide);
            cola.vaciar();
        });
    }

    // Contenido de una lista como texto

    // Recorre la lista y devuelve sus elementos separados por comas en un String
    private static String contenido(Lista<Integer> lista) {
        StringBuilder texto = new StringBuilder();

        for (int i = 0; i < lista.tamanio(); i++) {
            if (i > 0) {
                texto.append(',');
            }
            texto.append(lista.obtener(i));
        }
        return texto.toString();
    }

    // Registro de resultados

    // Registra una verificación y aumenta el contador de fallas si el resultado es incorrecto
    private void comprobar(String descripcion, boolean correcto) {
        total++;
        if (!correcto) {
            fallas++;
            System.out.println("  [FALLA] " + descripcion);
        }
    }

    // Comprueba que una operación lance el tipo de excepción esperado
    private void esperar(String descripcion, Class<? extends Throwable> tipo, Runnable accion) {
        total++;
        try {
            accion.run();
            fallas++;
            System.out.println("  [FALLA] " + descripcion + ": debía lanzar " + tipo.getSimpleName());
        } catch (Throwable t) {

            // Comprueba que la excepción lanzada sea del tipo esperado
            if (!tipo.isInstance(t)) {
                fallas++;
                System.out.println("  [FALLA] " + descripcion + ": lanzó " + t.getClass().getSimpleName()
                        + " y debía lanzar " + tipo.getSimpleName());
            }
        }
    }

    // Ejecuta un grupo de pruebas y captura cualquier excepción inesperada
    private void protegido(String grupo, Runnable bloque) {
        try {
            bloque.run();
        } catch (Throwable t) {
            total++;
            fallas++;
            System.out.println("  [FALLA] " + grupo + ": excepción inesperada " + t);
        }
    }

    // Muestra el resultado final de la verificación y devuelve si todas las pruebas fueron correctas
    private boolean terminar() {
        int correctas = total - fallas;
        System.out.println(nombre + ": " + correctas + " de " + total + " verificaciones correctas"
                + (fallas == 0 ? "  ->  OK" : "  ->  HAY FALLAS"));
        return fallas == 0;
    }
}