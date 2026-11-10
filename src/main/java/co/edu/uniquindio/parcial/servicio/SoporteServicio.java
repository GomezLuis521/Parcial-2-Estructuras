package co.edu.uniquindio.parcial.servicio;

import co.edu.uniquindio.parcial.estructuras.Cola;
import co.edu.uniquindio.parcial.model.Prioridad;
import co.edu.uniquindio.parcial.model.Solicitud;

/** Punto 3: selección de solicitudes para una jornada. */
public class SoporteServicio {

    /**
     * Selecciona primero las solicitudes ALTA y luego las NORMAL (cada grupo en orden
     * de llegada) mientras quepan en los minutos disponibles. Las seleccionadas se
     * retiran de la cola de pendientes; las demás conservan su orden relativo.
     */
    public static Cola<Solicitud> seleccionarSolicitudes(Cola<Solicitud> pendientes, int minutosDisponibles) {
        Cola<Solicitud> seleccionadas = new Cola<>();
        if (minutosDisponibles <= 0) {
            return seleccionadas;
        }
        int restante = seleccionarPorPrioridad(pendientes, seleccionadas, Prioridad.ALTA, minutosDisponibles);
        seleccionarPorPrioridad(pendientes, seleccionadas, Prioridad.NORMAL, restante);
        return seleccionadas;
    }

    /**
     * Da una vuelta completa a la cola: cada solicitud se saca y, si es de la prioridad
     * buscada y cabe, se selecciona; si no, se vuelve a encolar (así se conserva el orden).
     * Retorna los minutos que quedan.
     */
    private static int seleccionarPorPrioridad(Cola<Solicitud> pendientes, Cola<Solicitud> seleccionadas,
                                               Prioridad prioridad, int restante) {
        int cantidad = pendientes.getTamanio();
        for (int i = 0; i < cantidad; i++) {
            Solicitud solicitud = pendientes.desencolar();
            if (solicitud.getPrioridad() == prioridad && solicitud.getDuracion() <= restante) {
                seleccionadas.encolar(solicitud);
                restante -= solicitud.getDuracion();
            } else {
                pendientes.encolar(solicitud);
            }
        }
        return restante;
    }
}
