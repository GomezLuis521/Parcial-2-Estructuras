package co.edu.uniquindio.parcial.servicio;

import co.edu.uniquindio.parcial.estructuras.Cola;
import co.edu.uniquindio.parcial.model.Prioridad;
import co.edu.uniquindio.parcial.model.Solicitud;


public class SoporteServicio {


    public static Cola<Solicitud> seleccionarSolicitudes(Cola<Solicitud> pendientes, int minutosDisponibles) {
        Cola<Solicitud> seleccionadas = new Cola<>();
        if (minutosDisponibles <= 0) {
            return seleccionadas;
        }
        int restante = seleccionarPorPrioridad(pendientes, seleccionadas, Prioridad.ALTA, minutosDisponibles);
        seleccionarPorPrioridad(pendientes, seleccionadas, Prioridad.NORMAL, restante);
        return seleccionadas;
    }


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
