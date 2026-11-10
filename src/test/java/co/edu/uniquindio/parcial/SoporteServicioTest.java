package co.edu.uniquindio.parcial;

import co.edu.uniquindio.parcial.estructuras.Cola;
import co.edu.uniquindio.parcial.model.Prioridad;
import co.edu.uniquindio.parcial.model.Solicitud;
import co.edu.uniquindio.parcial.servicio.SoporteServicio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas del punto 3. */
class SoporteServicioTest {

    private Cola<Solicitud> pendientesEjemplo() {
        Cola<Solicitud> cola = new Cola<>();
        cola.encolar(new Solicitud("S1", Prioridad.NORMAL, 10));
        cola.encolar(new Solicitud("S2", Prioridad.ALTA, 30));
        cola.encolar(new Solicitud("S3", Prioridad.ALTA, 15));
        cola.encolar(new Solicitud("S4", Prioridad.NORMAL, 5));
        return cola;
    }

    private String codigos(Cola<Solicitud> cola) {
        StringBuilder sb = new StringBuilder();
        for (Object o : cola.aArreglo()) {
            sb.append(((Solicitud) o).getCodigo()).append(' ');
        }
        return sb.toString().trim();
    }

    @Test
    void ejemploDelEnunciadoCon25Minutos() {
        Cola<Solicitud> pendientes = pendientesEjemplo();
        Cola<Solicitud> seleccionadas = SoporteServicio.seleccionarSolicitudes(pendientes, 25);
        assertEquals("S3 S1", codigos(seleccionadas));
        assertEquals("S2 S4", codigos(pendientes));
    }

    @Test
    void sinTiempoRetornaColaVaciaYConservaPendientes() {
        Cola<Solicitud> pendientes = pendientesEjemplo();
        assertTrue(SoporteServicio.seleccionarSolicitudes(pendientes, 0).estaVacia());
        assertEquals("S1 S2 S3 S4", codigos(pendientes));
    }

    @Test
    void conTiempoSuficienteSeSeleccionanTodasAltasPrimero() {
        Cola<Solicitud> pendientes = pendientesEjemplo();
        assertEquals("S2 S3 S1 S4", codigos(SoporteServicio.seleccionarSolicitudes(pendientes, 100)));
        assertTrue(pendientes.estaVacia());
    }

    @Test
    void duracionNoPositivaNoSePermite() {
        assertThrows(IllegalArgumentException.class, () -> new Solicitud("S9", Prioridad.ALTA, 0));
    }
}
