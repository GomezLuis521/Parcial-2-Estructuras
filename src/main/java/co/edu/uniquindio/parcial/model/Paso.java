package co.edu.uniquindio.parcial.model;

/** Un paso explicado de la construcción o eliminación en un árbol (punto 5). */
public class Paso {
    private final String descripcion;
    private final String arbolJson; // foto del árbol en ese momento, puede ser null

    public Paso(String descripcion, String arbolJson) {
        this.descripcion = descripcion;
        this.arbolJson = arbolJson;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getArbolJson() {
        return arbolJson;
    }
}
