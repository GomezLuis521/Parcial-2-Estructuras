package co.edu.uniquindio.parcial.estructuras;

/** Pila propia implementada con nodos enlazados. */
public class Pila<T> {
    private Nodo<T> tope;
    private int tamanio;

    public void apilar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = tope;
        tope = nuevo;
        tamanio++;
    }

    public T desapilar() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila está vacía");
        }
        T dato = tope.dato;
        tope = tope.siguiente;
        tamanio--;
        return dato;
    }

    public T verTope() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila está vacía");
        }
        return tope.dato;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    /** Elementos desde el tope hasta el fondo (solo para mostrar). */
    public Object[] aArreglo() {
        Object[] arreglo = new Object[tamanio];
        Nodo<T> actual = tope;
        for (int i = 0; i < tamanio; i++) {
            arreglo[i] = actual.dato;
            actual = actual.siguiente;
        }
        return arreglo;
    }

    @Override
    public String toString() {
        return String.join(" -> ", textos(aArreglo()));
    }

    static String[] textos(Object[] arreglo) {
        String[] textos = new String[arreglo.length];
        for (int i = 0; i < arreglo.length; i++) {
            textos[i] = String.valueOf(arreglo[i]);
        }
        return textos;
    }
}
