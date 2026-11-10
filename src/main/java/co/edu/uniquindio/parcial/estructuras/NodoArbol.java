package co.edu.uniquindio.parcial.estructuras;

/** Nodo de árbol binario. El valor es el código entero (clave). */
public class NodoArbol {
    int valor;
    NodoArbol izquierdo;
    NodoArbol derecho;

    public NodoArbol(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public NodoArbol getIzquierdo() {
        return izquierdo;
    }

    public NodoArbol getDerecho() {
        return derecho;
    }
}
