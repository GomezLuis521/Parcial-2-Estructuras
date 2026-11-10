package co.edu.uniquindio.parcial.servicio;

import co.edu.uniquindio.parcial.estructuras.Pila;
import co.edu.uniquindio.parcial.model.Operacion;

/** Puntos 1 y 2: operaciones sobre el historial guardado en una pila. */
public class HistorialServicio {

    /**
     * Punto 1. Retorna una pila con las n operaciones confirmadas más recientes
     * (la más reciente en el tope). La pila original queda igual.
     */
    public static Pila<Operacion> obtenerUltimasConfirmadas(Pila<Operacion> historial, int n) {
        Pila<Operacion> resultado = new Pila<>();
        if (n <= 0) {
            return resultado;
        }
        Pila<Operacion> auxiliar = new Pila<>();      // para restaurar el historial
        Pila<Operacion> seleccionadas = new Pila<>(); // quedan en orden invertido
        int contador = 0;

        while (!historial.estaVacia()) {
            Operacion operacion = historial.desapilar();
            auxiliar.apilar(operacion);
            if (contador < n && operacion.estaConfirmada()) {
                seleccionadas.apilar(operacion);
                contador++;
            }
        }
        // Restaurar el historial original
        while (!auxiliar.estaVacia()) {
            historial.apilar(auxiliar.desapilar());
        }
        // Invertir para que la más reciente quede en el tope
        while (!seleccionadas.estaVacia()) {
            resultado.apilar(seleccionadas.desapilar());
        }
        return resultado;
    }

    /**
     * Punto 2. Retorna una pila con los elementos entre las posiciones inicio y fin
     * (contadas desde el tope empezando en 1, ambos extremos incluidos).
     * La acción en la posición inicio queda en el tope. La pila original queda igual.
     */
    public static <T> Pila<T> obtenerSegmento(Pila<T> pila, int inicio, int fin) {
        Pila<T> resultado = new Pila<>();
        if (inicio < 1 || inicio > fin || fin > pila.getTamanio()) {
            return resultado;
        }
        Pila<T> auxiliar = new Pila<>();
        Pila<T> segmento = new Pila<>(); // queda en orden invertido
        int posicion = 1;

        while (!pila.estaVacia()) {
            T elemento = pila.desapilar();
            auxiliar.apilar(elemento);
            if (posicion >= inicio && posicion <= fin) {
                segmento.apilar(elemento);
            }
            posicion++;
        }
        while (!auxiliar.estaVacia()) {
            pila.apilar(auxiliar.desapilar());
        }
        while (!segmento.estaVacia()) {
            resultado.apilar(segmento.desapilar());
        }
        return resultado;
    }
}
