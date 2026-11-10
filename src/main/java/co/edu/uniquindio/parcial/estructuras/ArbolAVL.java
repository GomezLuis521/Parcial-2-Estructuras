package co.edu.uniquindio.parcial.estructuras;

import co.edu.uniquindio.parcial.model.Paso;
import java.util.ArrayList;
import java.util.List;

public class ArbolAVL extends ArbolBinarioBusqueda {
    private final List<Paso> pasos = new ArrayList();

    public ArbolAVL() {
    }

    public List<Paso> getPasos() {
        return this.pasos;
    }

    public void limpiarPasos() {
        this.pasos.clear();
    }

    public boolean insertar(int var1) {
        if (this.buscar(var1)) {
            this.anotar("El " + var1 + " ya existe en el árbol, se ignora.");
            return false;
        } else {
            this.raiz = this.insertar(this.raiz, var1);
            this.registrar("Se inserta el " + var1 + ". Árbol con FE después de la inserción:");
            return true;
        }
    }

    private NodoArbol insertar(NodoArbol var1, int var2) {
        if (var1 == null) {
            return new NodoArbol(var2);
        } else {
            if (var2 < var1.valor) {
                var1.izquierdo = this.insertar(var1.izquierdo, var2);
            } else {
                var1.derecho = this.insertar(var1.derecho, var2);
            }

            return this.balancear(var1);
        }
    }

    public boolean eliminar(int var1) {
        if (!this.buscar(var1)) {
            this.anotar("El " + var1 + " no existe en el árbol.");
            return false;
        } else {
            this.registrar("Árbol antes de eliminar el " + var1 + ":");
            this.raiz = this.eliminar(this.raiz, var1);
            this.registrar("Árbol final después de eliminar el " + var1 + ":");
            return true;
        }
    }

    private NodoArbol eliminar(NodoArbol var1, int var2) {
        if (var2 < var1.valor) {
            var1.izquierdo = this.eliminar(var1.izquierdo, var2);
        } else if (var2 > var1.valor) {
            var1.derecho = this.eliminar(var1.derecho, var2);
        } else {
            if (var1.izquierdo == null) {
                this.anotar("El " + var2 + " no tiene hijo izquierdo: su lugar lo ocupa su hijo derecho.");
                return var1.derecho;
            }

            if (var1.derecho == null) {
                this.anotar("El " + var2 + " solo tiene hijo izquierdo: su lugar lo ocupa ese hijo.");
                return var1.izquierdo;
            }

            NodoArbol var3;
            for(var3 = var1.derecho; var3.izquierdo != null; var3 = var3.izquierdo) {
            }

            this.anotar("El " + var2 + " tiene dos hijos. Se busca en el subárbol derecho el menor valor: " + var3.valor + ". Ese valor ocupa el lugar del " + var2 + " y se elimina el " + var3.valor + " de su posición original.");
            var1.valor = var3.valor;
            var1.derecho = this.eliminar(var1.derecho, var3.valor);
        }

        return this.balancear(var1);
    }

    private NodoArbol balancear(NodoArbol var1) {
        int var2 = factorEquilibrio(var1);
        if (var2 < -1) {
            this.registrar("Desbalance en el nodo " + var1.valor + " (FE=" + var2 + "). Árbol antes de rotar:");
            if (factorEquilibrio(var1.izquierdo) <= 0) {
                this.anotar("Caso izquierda-izquierda: rotación simple a la derecha sobre " + var1.valor + ".");
                return this.rotarDerecha(var1);
            } else {
                this.anotar("Caso izquierda-derecha: rotación doble. Primero rotación a la izquierda sobre " + var1.izquierdo.valor + " y luego a la derecha sobre " + var1.valor + ".");
                var1.izquierdo = this.rotarIzquierda(var1.izquierdo);
                return this.rotarDerecha(var1);
            }
        } else if (var2 > 1) {
            this.registrar("Desbalance en el nodo " + var1.valor + " (FE=" + var2 + "). Árbol antes de rotar:");
            if (factorEquilibrio(var1.derecho) >= 0) {
                this.anotar("Caso derecha-derecha: rotación simple a la izquierda sobre " + var1.valor + ".");
                return this.rotarIzquierda(var1);
            } else {
                this.anotar("Caso derecha-izquierda: rotación doble. Primero rotación a la derecha sobre " + var1.derecho.valor + " y luego a la izquierda sobre " + var1.valor + ".");
                var1.derecho = this.rotarDerecha(var1.derecho);
                return this.rotarIzquierda(var1);
            }
        } else {
            return var1;
        }
    }

    private NodoArbol rotarDerecha(NodoArbol var1) {
        NodoArbol var2 = var1.izquierdo;
        var1.izquierdo = var2.derecho;
        var2.derecho = var1;
        return var2;
    }

    private NodoArbol rotarIzquierda(NodoArbol var1) {
        NodoArbol var2 = var1.derecho;
        var1.derecho = var2.izquierdo;
        var2.izquierdo = var1;
        return var2;
    }

    public boolean estaBalanceado() {
        return this.estaBalanceado(this.raiz);
    }

    private boolean estaBalanceado(NodoArbol var1) {
        if (var1 == null) {
            return true;
        } else {
            int var2 = factorEquilibrio(var1);
            return var2 >= -1 && var2 <= 1 && this.estaBalanceado(var1.izquierdo) && this.estaBalanceado(var1.derecho);
        }
    }

    private void registrar(String var1) {
        this.pasos.add(new Paso(var1, this.aJson()));
    }

    private void anotar(String var1) {
        this.pasos.add(new Paso(var1, (String)null));
    }
}
