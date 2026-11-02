@echo off
if "%~1"=="" (
    java -cp out juego.pantallas.Main
) else (
    java -cp out %1
)