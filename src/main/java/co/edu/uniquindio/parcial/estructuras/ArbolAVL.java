package co.edu.uniquindio.parcial.estructuras;

import co.edu.uniquindio.parcial.model.Paso;

import java.util.ArrayList;
import java.util.List;

/**
 * Árbol AVL propio. Guarda cada paso (inserciones, desbalances y rotaciones)
 * con una foto del árbol para poder explicarlo.
 * FE = altura(derecho) - altura(izquierdo). Balanceado si FE está entre -1 y 1.
 */
public class ArbolAVL extends ArbolBinarioBusqueda {
    // La lista solo guarda la bitácora de pasos; los datos viven en el árbol propio.
    private final List<Paso> pasos = new ArrayList<>();

    public List<Paso> getPasos() {
        return pasos;
    }

    public void limpiarPasos() {
        pasos.clear();
    }

    @Override
    public boolean insertar(int valor) {
        if (buscar(valor)) {
            anotar("El " + valor + " ya existe en el árbol, se ignora.");
            return false;
        }
        raiz = insertar(raiz, valor);
        registrar("Se inserta el " + valor + ". Árbol con FE después de la inserción:");
        return true;
    }

    private NodoArbol insertar(NodoArbol nodo, int valor) {
        if (nodo == null) {
            return new NodoArbol(valor);
        }
        if (valor < nodo.valor) {
            nodo.izquierdo = insertar(nodo.izquierdo, valor);
        } else {
            nodo.derecho = insertar(nodo.derecho, valor);
        }
        return balancear(nodo);
    }

    /**
     * Elimina el valor. Si tiene dos hijos se reemplaza por el MENOR del
     * subárbol derecho (regla del punto 5d) y luego se rebalancea.
     */
    @Override
    public boolean eliminar(int valor) {
        if (!buscar(valor)) {
            anotar("El " + valor + " no existe en el árbol.");
            return false;
        }
        registrar("Árbol antes de eliminar el " + valor + ":");
        raiz = eliminar(raiz, valor);
        registrar("Árbol final después de eliminar el " + valor + ":");
        return true;
    }

    private NodoArbol eliminar(NodoArbol nodo, int valor) {
        if (valor < nodo.valor) {
            nodo.izquierdo = eliminar(nodo.izquierdo, valor);
        } else if (valor > nodo.valor) {
            nodo.derecho = eliminar(nodo.derecho, valor);
        } else {
            if (nodo.izquierdo == null) {
                anotar("El " + valor + " no tiene hijo izquierdo: su lugar lo ocupa su hijo derecho.");
                return nodo.derecho;
            }
            if (nodo.derecho == null) {
                anotar("El " + valor + " solo tiene hijo izquierdo: su lugar lo ocupa ese hijo.");
                return nodo.izquierdo;
            }
            NodoArbol menor = nodo.derecho;
            while (menor.izquierdo != null) {
                menor = menor.izquierdo;
            }
            anotar("El " + valor + " tiene dos hijos. Se busca en el subárbol derecho el menor valor: "
                    + menor.valor + ". Ese valor ocupa el lugar del " + valor
                    + " y se elimina el " + menor.valor + " de su posición original.");
            nodo.valor = menor.valor;
            nodo.derecho = eliminar(nodo.derecho, menor.valor);
        }
        return balancear(nodo);
    }

    /** Revisa el FE del nodo y hace la rotación que corresponda. */
    private NodoArbol balancear(NodoArbol nodo) {
        int fe = factorEquilibrio(nodo);
        if (fe < -1) { // cargado a la izquierda
            registrar("Desbalance en el nodo " + nodo.valor + " (FE=" + fe + "). Árbol antes de rotar:");
            if (factorEquilibrio(nodo.izquierdo) <= 0) {
                anotar("Caso izquierda-izquierda: rotación simple a la derecha sobre " + nodo.valor + ".");
                return rotarDerecha(nodo);
            }
            anotar("Caso izquierda-derecha: rotación doble. Primero rotación a la izquierda sobre "
                    + nodo.izquierdo.valor + " y luego a la derecha sobre " + nodo.valor + ".");
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }
        if (fe > 1) { // cargado a la derecha
            registrar("Desbalance en el nodo " + nodo.valor + " (FE=" + fe + "). Árbol antes de rotar:");
            if (factorEquilibrio(nodo.derecho) >= 0) {
                anotar("Caso derecha-derecha: rotación simple a la izquierda sobre " + nodo.valor + ".");
                return rotarIzquierda(nodo);
            }
            anotar("Caso derecha-izquierda: rotación doble. Primero rotación a la derecha sobre "
                    + nodo.derecho.valor + " y luego a la izquierda sobre " + nodo.valor + ".");
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }
        return nodo;
    }

    private NodoArbol rotarDerecha(NodoArbol nodo) {
        NodoArbol nuevaRaiz = nodo.izquierdo;
        nodo.izquierdo = nuevaRaiz.derecho;
        nuevaRaiz.derecho = nodo;
        return nuevaRaiz;
    }

    private NodoArbol rotarIzquierda(NodoArbol nodo) {
        NodoArbol nuevaRaiz = nodo.derecho;
        nodo.derecho = nuevaRaiz.izquierdo;
        nuevaRaiz.izquierdo = nodo;
        return nuevaRaiz;
    }

    /** true si todos los nodos tienen FE entre -1 y 1. */
    public boolean estaBalanceado() {
        return estaBalanceado(raiz);
    }

    private boolean estaBalanceado(NodoArbol nodo) {
        if (nodo == null) {
            return true;
        }
        int fe = factorEquilibrio(nodo);
        return fe >= -1 && fe <= 1 && estaBalanceado(nodo.izquierdo) && estaBalanceado(nodo.derecho);
    }

    private void registrar(String descripcion) {
        pasos.add(new Paso(descripcion, aJson()));
    }

    private void anotar(String descripcion) {
        pasos.add(new Paso(descripcion, null));
    }
}
