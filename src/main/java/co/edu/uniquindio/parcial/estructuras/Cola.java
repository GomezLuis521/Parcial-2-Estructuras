package co.edu.uniquindio.parcial.estructuras;

/** Cola propia implementada con nodos enlazados. */
public class Cola<T> {
    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamanio;

    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (estaVacia()) {
            frente = nuevo;
        } else {
            fin.siguiente = nuevo;
        }
        fin = nuevo;
        tamanio++;
    }

    public T desencolar() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía");
        }
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        tamanio--;
        return dato;
    }

    public T verFrente() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía");
        }
        return frente.dato;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    /** Elementos desde el frente hasta el final (solo para mostrar). */
    public Object[] aArreglo() {
        Object[] arreglo = new Object[tamanio];
        Nodo<T> actual = frente;
        for (int i = 0; i < tamanio; i++) {
            arreglo[i] = actual.dato;
            actual = actual.siguiente;
        }
        return arreglo;
    }

    @Override
    public String toString() {
        return String.join(" -> ", Pila.textos(aArreglo()));
    }
}
