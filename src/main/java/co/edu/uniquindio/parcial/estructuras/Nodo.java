package co.edu.uniquindio.parcial.estructuras;

/** Nodo simple usado por la Pila y la Cola. */
public class Nodo<T> {
    T dato;
    Nodo<T> siguiente;

    public Nodo(T dato) {
        this.dato = dato;
    }
}
