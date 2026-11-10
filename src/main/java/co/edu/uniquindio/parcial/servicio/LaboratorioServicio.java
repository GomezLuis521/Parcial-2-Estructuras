package co.edu.uniquindio.parcial.servicio;

import co.edu.uniquindio.parcial.estructuras.NodoArbol;

/** Punto 4a: consulta de equipos por rango en un árbol binario de búsqueda. */
public class LaboratorioServicio {

    /**
     * Cuenta (recursivamente) los códigos dentro de [inferior, superior].
     * Aprovecha el orden del ABB: si el nodo es menor que el rango no se visita
     * su izquierda, y si es mayor no se visita su derecha.
     */
    public static int contarEquiposEnRango(NodoArbol raiz, int inferior, int superior) {
        if (raiz == null || inferior > superior) {
            return 0;
        }
        if (raiz.getValor() < inferior) {
            return contarEquiposEnRango(raiz.getDerecho(), inferior, superior);
        }
        if (raiz.getValor() > superior) {
            return contarEquiposEnRango(raiz.getIzquierdo(), inferior, superior);
        }
        return 1 + contarEquiposEnRango(raiz.getIzquierdo(), inferior, superior)
                + contarEquiposEnRango(raiz.getDerecho(), inferior, superior);
    }
}
