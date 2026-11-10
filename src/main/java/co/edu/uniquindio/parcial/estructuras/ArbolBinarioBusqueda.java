package co.edu.uniquindio.parcial.estructuras;

/**
 * Árbol binario de búsqueda propio. No admite claves repetidas.
 * FE (factor de equilibrio) = altura(subárbol derecho) - altura(subárbol izquierdo).
 */
public class ArbolBinarioBusqueda {
    protected NodoArbol raiz;

    public NodoArbol getRaiz() {
        return raiz;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public boolean buscar(int valor) {
        NodoArbol actual = raiz;
        while (actual != null) {
            if (valor == actual.valor) {
                return true;
            }
            actual = valor < actual.valor ? actual.izquierdo : actual.derecho;
        }
        return false;
    }

    /** Inserta el valor. Retorna false si ya existía (se ignora el repetido). */
    public boolean insertar(int valor) {
        if (buscar(valor)) {
            return false;
        }
        raiz = insertar(raiz, valor);
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
        return nodo;
    }

    /**
     * Elimina el valor. Si el nodo tiene dos hijos se reemplaza por el
     * mayor del subárbol izquierdo (predecesor), como pide el punto 4b.
     */
    public boolean eliminar(int valor) {
        if (!buscar(valor)) {
            return false;
        }
        raiz = eliminar(raiz, valor);
        return true;
    }

    private NodoArbol eliminar(NodoArbol nodo, int valor) {
        if (valor < nodo.valor) {
            nodo.izquierdo = eliminar(nodo.izquierdo, valor);
        } else if (valor > nodo.valor) {
            nodo.derecho = eliminar(nodo.derecho, valor);
        } else {
            if (nodo.izquierdo == null) {
                return nodo.derecho;
            }
            if (nodo.derecho == null) {
                return nodo.izquierdo;
            }
            NodoArbol mayor = nodo.izquierdo;
            while (mayor.derecho != null) {
                mayor = mayor.derecho;
            }
            nodo.valor = mayor.valor;
            nodo.izquierdo = eliminar(nodo.izquierdo, mayor.valor);
        }
        return nodo;
    }

    // ---------------- Recorridos ----------------

    public Cola<Integer> preorden() {
        Cola<Integer> resultado = new Cola<>();
        preorden(raiz, resultado);
        return resultado;
    }

    private void preorden(NodoArbol nodo, Cola<Integer> resultado) {
        if (nodo != null) {
            resultado.encolar(nodo.valor);
            preorden(nodo.izquierdo, resultado);
            preorden(nodo.derecho, resultado);
        }
    }

    public Cola<Integer> inorden() {
        Cola<Integer> resultado = new Cola<>();
        inorden(raiz, resultado);
        return resultado;
    }

    private void inorden(NodoArbol nodo, Cola<Integer> resultado) {
        if (nodo != null) {
            inorden(nodo.izquierdo, resultado);
            resultado.encolar(nodo.valor);
            inorden(nodo.derecho, resultado);
        }
    }

    public Cola<Integer> postorden() {
        Cola<Integer> resultado = new Cola<>();
        postorden(raiz, resultado);
        return resultado;
    }

    private void postorden(NodoArbol nodo, Cola<Integer> resultado) {
        if (nodo != null) {
            postorden(nodo.izquierdo, resultado);
            postorden(nodo.derecho, resultado);
            resultado.encolar(nodo.valor);
        }
    }

    /** Recorrido en amplitud (por niveles) usando la cola propia. */
    public Cola<Integer> amplitud() {
        Cola<Integer> resultado = new Cola<>();
        if (raiz == null) {
            return resultado;
        }
        Cola<NodoArbol> pendientes = new Cola<>();
        pendientes.encolar(raiz);
        while (!pendientes.estaVacia()) {
            NodoArbol actual = pendientes.desencolar();
            resultado.encolar(actual.valor);
            if (actual.izquierdo != null) {
                pendientes.encolar(actual.izquierdo);
            }
            if (actual.derecho != null) {
                pendientes.encolar(actual.derecho);
            }
        }
        return resultado;
    }

    // ---------------- Altura y FE ----------------

    public static int altura(NodoArbol nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
    }

    public static int factorEquilibrio(NodoArbol nodo) {
        return altura(nodo.derecho) - altura(nodo.izquierdo);
    }

    // ---------------- Para mostrar ----------------

    /** Árbol en JSON: {"v":valor,"fe":FE,"i":izq,"d":der}. Lo usa el dashboard. */
    public String aJson() {
        return aJson(raiz);
    }

    private static String aJson(NodoArbol nodo) {
        if (nodo == null) {
            return "null";
        }
        return "{\"v\":" + nodo.valor + ",\"fe\":" + factorEquilibrio(nodo)
                + ",\"i\":" + aJson(nodo.izquierdo) + ",\"d\":" + aJson(nodo.derecho) + "}";
    }

    /** Dibujo de lado para consola: la raíz a la izquierda, el subárbol derecho arriba. */
    public String dibujar() {
        if (raiz == null) {
            return "(árbol vacío)\n";
        }
        StringBuilder sb = new StringBuilder();
        dibujar(raiz, "", sb);
        return sb.toString();
    }

    private static void dibujar(NodoArbol nodo, String sangria, StringBuilder sb) {
        if (nodo == null) {
            return;
        }
        dibujar(nodo.derecho, sangria + "        ", sb);
        sb.append(sangria).append(nodo.valor).append(" [FE=").append(factorEquilibrio(nodo)).append("]\n");
        dibujar(nodo.izquierdo, sangria + "        ", sb);
    }
}
