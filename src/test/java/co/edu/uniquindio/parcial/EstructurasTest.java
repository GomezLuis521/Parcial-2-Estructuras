package co.edu.uniquindio.parcial;

import co.edu.uniquindio.parcial.estructuras.Cola;
import co.edu.uniquindio.parcial.estructuras.Pila;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstructurasTest {

    @Test
    void pilaEsLifo() {
        Pila<String> pila = new Pila<>();
        pila.apilar("A");
        pila.apilar("B");
        assertEquals("B", pila.verTope());
        assertEquals("B", pila.desapilar());
        assertEquals("A", pila.desapilar());
        assertTrue(pila.estaVacia());
        assertThrows(IllegalStateException.class, pila::desapilar);
    }

    @Test
    void colaEsFifo() {
        Cola<String> cola = new Cola<>();
        cola.encolar("A");
        cola.encolar("B");
        assertEquals(2, cola.getTamanio());
        assertEquals("A", cola.desencolar());
        assertEquals("B", cola.desencolar());
        assertTrue(cola.estaVacia());
        assertThrows(IllegalStateException.class, cola::desencolar);
    }
}
