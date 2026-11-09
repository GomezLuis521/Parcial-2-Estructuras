package co.edu.uniquindio.parcial.model;

/** Operación del historial (punto 1). */
public class Operacion {
    private final String codigo;
    private final EstadoOperacion estado;

    public Operacion(String codigo, EstadoOperacion estado) {
        this.codigo = codigo;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public EstadoOperacion getEstado() {
        return estado;
    }

    public boolean estaConfirmada() {
        return estado == EstadoOperacion.CONFIRMADA;
    }

    @Override
    public String toString() {
        return codigo + " (" + estado + ")";
    }
}
