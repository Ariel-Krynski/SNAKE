# Snake: Deshacer — Game Jam Java

Implementación sobre el kit entregado por el docente. Requiere Java 11 o superior y no usa bibliotecas externas.

## Compilar y ejecutar (Windows)
1. Descomprimir la carpeta.
2. Ejecutar `compilar.bat`.
3. Ejecutar `ejecutar.bat juego.pantallas.Main`.

En Linux/macOS:
```sh
./compilar.sh
./ejecutar.sh juego.pantallas.Main
```

## Cómo jugar
- `ENTER` o click: iniciar desde el menú.
- Flechas: mover la serpiente.
- `Z`: deshacer el último movimiento (cuesta 5 puntos).
- `P`: pausar/reanudar.
- `ESC`: volver al menú.
- Comer manzana roja suma 10 y hace crecer la serpiente.
- Comer manzana dorada suma 30 y hace crecer dos segmentos.
- Comer manzana podrida resta 15 puntos y encoge la serpiente.
- Chocar contra una pared o contra el cuerpo termina la partida.
- En la pantalla final, `ENTER` vuelve a jugar y `M` o `ESC` vuelve al menú.

## Diseño
- `MenuEscena`, `JuegoEscena` y `FinEscena`: tres escenas conectadas.
- `Comida` es una clase abstracta. `Manzana`, `ManzanaDorada` y `ManzanaPodrida` implementan efectos diferentes mediante sobrescritura.
- `EfectoComida` es una interfaz propia.
- `ListaDoble`: guarda los segmentos del cuerpo.
- `ColaEnlazada`: conserva los giros de dirección pendientes.
- `PilaEnlazada`: guarda estados anteriores para deshacer movimientos.
- `PruebaMisEstructuras`: ejecuta el verificador del kit para los tres TADs.

## Nota sobre el kit recibido
El ZIP original no compilaba de cero: `ListaBase.java` tenía métodos con `@Override` que no existen en la interfaz `Lista`, y `Mundo.java` invocaba `iterator()`/`for-each` sobre una interfaz que no declara ese método. Se hicieron dos ajustes mínimos de compatibilidad en esos archivos, sin modificar las interfaces entregadas. El resto de los paquetes originales se conserva.
