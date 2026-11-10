package co.edu.uniquindio.parcial;

import co.edu.uniquindio.parcial.estructuras.Pila;
import co.edu.uniquindio.parcial.model.EstadoOperacion;
import co.edu.uniquindio.parcial.model.Operacion;
import co.edu.uniquindio.parcial.servicio.HistorialServicio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de los puntos 1 y 2. */
class HistorialServicioTest {

    private Pila<Operacion> historialEjemplo() {
        Pila<Operacion> historial = new Pila<>();
        historial.apilar(new Operacion("O1", EstadoOperacion.ANULADA));
        historial.apilar(new Operacion("O2", EstadoOperacion.CONFIRMADA));
        historial.apilar(new Operacion("O3", EstadoOperacion.CONFIRMADA));
        historial.apilar(new Operacion("O4", EstadoOperacion.ANULADA));
        historial.apilar(new Operacion("O5", EstadoOperacion.CONFIRMADA));
        return historial;
    }

    private Pila<String> accionesEjemplo() {
        Pila<String> pila = new Pila<>();
        for (int i = 1; i <= 6; i++) {
            pila.apilar("A" + i);
        }
        return pila; // tope: A6
    }

    // ---------- Punto 1 ----------

    @Test
    void ejemploDelEnunciadoConNIgualADos() {
        Pila<Operacion> historial = historialEjemplo();
        Pila<Operacion> resultado = HistorialServicio.obtenerUltimasConfirmadas(historial, 2);
        assertEquals("O5 (CONFIRMADA) -> O3 (CONFIRMADA)", resultado.toString());
    }

    @Test
    void siHayMenosConfirmadasQueNSeIncluyenTodas() {
        Pila<Operacion> resultado = HistorialServicio.obtenerUltimasConfirmadas(historialEjemplo(), 10);
        assertEquals("O5 (CONFIRMADA) -> O3 (CONFIRMADA) -> O2 (CONFIRMADA)", resultado.toString());
    }

    @Test
    void nMenorOIgualACeroRetornaPilaVacia() {
        assertTrue(HistorialServicio.obtenerUltimasConfirmadas(historialEjemplo(), 0).estaVacia());
        assertTrue(HistorialServicio.obtenerUltimasConfirmadas(historialEjemplo(), -3).estaVacia());
    }

    @Test
    void elHistorialOriginalNoCambia() {
        Pila<Operacion> historial = historialEjemplo();
        String antes = historial.toString();
        HistorialServicio.obtenerUltimasConfirmadas(historial, 2);
        assertEquals(antes, historial.toString());
        assertEquals(5, historial.getTamanio());
    }

    // ---------- Punto 2 ----------

    @Test
    void segmentoDelEjemploDelEnunciado() {
        Pila<String> pila = accionesEjemplo();
        assertEquals("A5 -> A4 -> A3", HistorialServicio.obtenerSegmento(pila, 2, 4).toString());
        assertEquals("A6 -> A5 -> A4 -> A3 -> A2 -> A1", pila.toString());
    }

    @Test
    void inicioIgualAFinRetornaUnSoloElemento() {
        assertEquals("A4", HistorialServicio.obtenerSegmento(accionesEjemplo(), 3, 3).toString());
    }

    @Test
    void segmentoCompleto() {
        assertEquals(6, HistorialServicio.obtenerSegmento(accionesEjemplo(), 1, 6).getTamanio());
    }

    @Test
    void posicionesInvalidasRetornanPilaVacia() {
        assertTrue(HistorialServicio.obtenerSegmento(accionesEjemplo(), 0, 3).estaVacia());
        assertTrue(HistorialServicio.obtenerSegmento(accionesEjemplo(), 4, 2).estaVacia());
        assertTrue(HistorialServicio.obtenerSegmento(accionesEjemplo(), 2, 7).estaVacia());
    }
}
