package co.edu.uniquindio.parcial.model;

/** Solicitud del centro de soporte (punto 3). */
public class Solicitud {
    private final String codigo;
    private final Prioridad prioridad;
    private final int duracion; // minutos

    public Solicitud(String codigo, Prioridad prioridad, int duracion) {
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración debe ser un entero positivo: " + codigo);
        }
        this.codigo = codigo;
        this.prioridad = prioridad;
        this.duracion = duracion;
    }

    public String getCodigo() {
        return codigo;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public int getDuracion() {
        return duracion;
    }

    @Override
    public String toString() {
        return codigo + " (" + prioridad + ", " + duracion + " min)";
    }
}
