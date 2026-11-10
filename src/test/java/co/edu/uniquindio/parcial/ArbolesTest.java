package co.edu.uniquindio.parcial;

import co.edu.uniquindio.parcial.estructuras.ArbolAVL;
import co.edu.uniquindio.parcial.estructuras.ArbolBinarioBusqueda;
import co.edu.uniquindio.parcial.servicio.LaboratorioServicio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de los puntos 4 y 5. */
class ArbolesTest {

    private static final int[] LISTA_PUNTO5 =
            {45, 33, 29, 28, 22, 32, 34, 39, 36, 43, 60, 45, 46, 50, 48, 55, 70, 69, 65, 86, 79, 88};

    private ArbolBinarioBusqueda arbolPunto4() {
        ArbolBinarioBusqueda arbol = new ArbolBinarioBusqueda();
        for (int v : new int[]{50, 30, 70, 20, 40, 60, 80, 55, 65}) {
            arbol.insertar(v);
        }
        return arbol;
    }

    // ---------- Punto 4a ----------

    @Test
    void cuentaEquiposEnRangoIncluyendoExtremos() {
        ArbolBinarioBusqueda arbol = arbolPunto4();
        assertEquals(5, LaboratorioServicio.contarEquiposEnRango(arbol.getRaiz(), 40, 65)); // 40 50 55 60 65
        assertEquals(9, LaboratorioServicio.contarEquiposEnRango(arbol.getRaiz(), 0, 100));
        assertEquals(0, LaboratorioServicio.contarEquiposEnRango(arbol.getRaiz(), 81, 99));
    }

    @Test
    void arbolVacioOLimitesInvertidosRetornaCero() {
        assertEquals(0, LaboratorioServicio.contarEquiposEnRango(null, 1, 10));
        assertEquals(0, LaboratorioServicio.contarEquiposEnRango(arbolPunto4().getRaiz(), 70, 20));
    }

    // ---------- Punto 4b ----------

    @Test
    void eliminar50UsaElMayorDeLaIzquierda() {
        ArbolBinarioBusqueda arbol = arbolPunto4();
        assertTrue(arbol.eliminar(50));
        assertEquals(40, arbol.getRaiz().getValor());
        assertEquals("20 -> 30 -> 40 -> 55 -> 60 -> 65 -> 70 -> 80", arbol.inorden().toString());
    }

    // ---------- Punto 5 ----------

    @Test
    void abbIgnoraElRepetidoYCalculaRecorridos() {
        ArbolBinarioBusqueda abb = new ArbolBinarioBusqueda();
        int repetidos = 0;
        for (int v : LISTA_PUNTO5) {
            if (!abb.insertar(v)) {
                repetidos++;
            }
        }
        assertEquals(1, repetidos); // el 45 aparece dos veces
        assertEquals(21, abb.inorden().getTamanio());
        assertEquals(45, abb.getRaiz().getValor());
        assertEquals("45 -> 33 -> 60 -> 29 -> 34 -> 46 -> 70", primeros(abb.amplitud().toString(), 7));
    }

    @Test
    void avlConstruidoConPreordenQuedaBalanceadoYOrdenado() {
        ArbolAVL avl = avlPunto5();
        assertTrue(avl.estaBalanceado());
        assertEquals(21, avl.inorden().getTamanio());
        assertEquals(inordenAbb(), avl.inorden().toString());
        assertFalse(avl.getPasos().isEmpty());
    }

    @Test
    void eliminar45EnElAvlUsaElMenorDeLaDerechaYQuedaBalanceado() {
        ArbolAVL avl = avlPunto5();
        avl.limpiarPasos();
        assertTrue(avl.eliminar(45));
        assertFalse(avl.buscar(45));
        assertTrue(avl.estaBalanceado());
        assertEquals(20, avl.inorden().getTamanio());
        assertTrue(avl.getPasos().get(1).getDescripcion().contains("menor valor: 46"));
    }

    @Test
    void avlHaceRotacionSimpleConTresValoresAscendentes() {
        ArbolAVL avl = new ArbolAVL();
        avl.insertar(1);
        avl.insertar(2);
        avl.insertar(3);
        assertEquals(2, avl.getRaiz().getValor());
        assertTrue(avl.estaBalanceado());
    }

    private ArbolAVL avlPunto5() {
        ArbolBinarioBusqueda abb = new ArbolBinarioBusqueda();
        for (int v : LISTA_PUNTO5) {
            abb.insertar(v);
        }
        ArbolAVL avl = new ArbolAVL();
        for (Object v : abb.preorden().aArreglo()) {
            avl.insertar((Integer) v);
        }
        return avl;
    }

    private String inordenAbb() {
        ArbolBinarioBusqueda abb = new ArbolBinarioBusqueda();
        for (int v : LISTA_PUNTO5) {
            abb.insertar(v);
        }
        return abb.inorden().toString();
    }

    private String primeros(String recorrido, int cantidad) {
        String[] partes = recorrido.split(" -> ");
        return String.join(" -> ", java.util.Arrays.copyOf(partes, cantidad));
    }
}
