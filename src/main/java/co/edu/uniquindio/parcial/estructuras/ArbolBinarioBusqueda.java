package co.edu.uniquindio.parcial.estructuras;

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



    public static int altura(NodoArbol nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
    }

    public static int factorEquilibrio(NodoArbol nodo) {
        return altura(nodo.derecho) - altura(nodo.izquierdo);
    }




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