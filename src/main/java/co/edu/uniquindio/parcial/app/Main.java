package co.edu.uniquindio.parcial.app;

import co.edu.uniquindio.parcial.estructuras.ArbolAVL;
import co.edu.uniquindio.parcial.estructuras.ArbolBinarioBusqueda;
import co.edu.uniquindio.parcial.estructuras.Cola;
import co.edu.uniquindio.parcial.estructuras.Pila;
import co.edu.uniquindio.parcial.model.EstadoOperacion;
import co.edu.uniquindio.parcial.model.Operacion;
import co.edu.uniquindio.parcial.model.Paso;
import co.edu.uniquindio.parcial.model.Prioridad;
import co.edu.uniquindio.parcial.model.Solicitud;
import co.edu.uniquindio.parcial.servicio.HistorialServicio;
import co.edu.uniquindio.parcial.servicio.LaboratorioServicio;
import co.edu.uniquindio.parcial.servicio.SoporteServicio;

import java.util.Scanner;

/**
 * Levanta el dashboard en http://localhost:8080 y muestra un menú en consola
 * para ver el ejemplo de cada punto (ejecutar desde la carpeta raíz del proyecto).
 */
public class Main {

    private static final int[] LISTA_PUNTO5 =
            {45, 33, 29, 28, 22, 32, 34, 39, 36, 43, 60, 45, 46, 50, 48, 55, 70, 69, 65, 86, 79, 88};

    public static void main(String[] args) throws Exception {
        ServidorDashboard.iniciar(8080);
        Scanner teclado = new Scanner(System.in);
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\n========== MENÚ PARCIAL II ==========");
            System.out.println("1. Punto 1 - Últimas operaciones confirmadas");
            System.out.println("2. Punto 2 - Segmento del historial");
            System.out.println("3. Punto 3 - Selección de solicitudes");
            System.out.println("4. Punto 4 - ABB: equipos por rango y eliminar 50");
            System.out.println("5. Punto 5a/b - ABB con FE y recorridos");
            System.out.println("6. Punto 5c - AVL con el preorden (paso a paso)");
            System.out.println("7. Punto 5d - Eliminar 45 del AVL");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");
            opcion = teclado.hasNextLine() ? teclado.nextLine().trim() : "0";
            switch (opcion) {
                case "1" -> punto1();
                case "2" -> punto2();
                case "3" -> punto3();
                case "4" -> punto4();
                case "5" -> punto5Abb();
                case "6" -> punto5Avl();
                case "7" -> punto5Eliminar();
                case "0" -> System.out.println("Hasta luego.");
                default -> System.out.println("Opción no válida, escribe un número del 0 al 7.");
            }
        }
        System.exit(0); // también apaga el servidor del dashboard
    }

    private static void titulo(String texto) {
        System.out.println("\n==================== " + texto + " ====================");
    }

    private static void punto1() {
        titulo("PUNTO 1 - obtenerUltimasConfirmadas");
        Pila<Operacion> historial = new Pila<>();
        historial.apilar(new Operacion("O1", EstadoOperacion.ANULADA));
        historial.apilar(new Operacion("O2", EstadoOperacion.CONFIRMADA));
        historial.apilar(new Operacion("O3", EstadoOperacion.CONFIRMADA));
        historial.apilar(new Operacion("O4", EstadoOperacion.ANULADA));
        historial.apilar(new Operacion("O5", EstadoOperacion.CONFIRMADA));
        System.out.println("Historial (tope primero): " + historial);
        System.out.println("Resultado n=2:            " + HistorialServicio.obtenerUltimasConfirmadas(historial, 2));
        System.out.println("Historial sin cambios:    " + historial);
    }

    private static void punto2() {
        titulo("PUNTO 2 - obtenerSegmento");
        Pila<String> pila = new Pila<>();
        for (int i = 1; i <= 6; i++) {
            pila.apilar("A" + i);
        }
        System.out.println("Original:               " + pila);
        System.out.println("Segmento inicio=2 fin=4: " + HistorialServicio.obtenerSegmento(pila, 2, 4));
        System.out.println("Original sin cambios:   " + pila);
    }

    private static void punto3() {
        titulo("PUNTO 3 - seleccionarSolicitudes");
        Cola<Solicitud> pendientes = new Cola<>();
        pendientes.encolar(new Solicitud("S1", Prioridad.NORMAL, 10));
        pendientes.encolar(new Solicitud("S2", Prioridad.ALTA, 30));
        pendientes.encolar(new Solicitud("S3", Prioridad.ALTA, 15));
        pendientes.encolar(new Solicitud("S4", Prioridad.NORMAL, 5));
        System.out.println("Pendientes:    " + pendientes);
        Cola<Solicitud> seleccionadas = SoporteServicio.seleccionarSolicitudes(pendientes, 25);
        System.out.println("Con 25 minutos");
        System.out.println("Seleccionadas: " + seleccionadas);
        System.out.println("Pendientes:    " + pendientes);
    }

    private static void punto4() {
        titulo("PUNTO 4 - ABB de equipos");
        ArbolBinarioBusqueda arbol = new ArbolBinarioBusqueda();
        for (int codigo : new int[]{50, 30, 70, 20, 40, 60, 80, 55, 65}) {
            arbol.insertar(codigo);
        }
        System.out.println("a) Equipos en rango [35, 65]: "
                + LaboratorioServicio.contarEquiposEnRango(arbol.getRaiz(), 35, 65));
        System.out.println("b) Árbol inicial (la raíz a la izquierda, derecha arriba):");
        System.out.print(arbol.dibujar());
        arbol.eliminar(50);
        System.out.println("Árbol después de eliminar 50 (se reemplaza por el mayor de la izquierda, 40):");
        System.out.print(arbol.dibujar());
        System.out.println("Inorden: " + arbol.inorden());
    }

    private static ArbolBinarioBusqueda construirAbb(boolean avisarRepetidos) {
        ArbolBinarioBusqueda abb = new ArbolBinarioBusqueda();
        for (int valor : LISTA_PUNTO5) {
            if (!abb.insertar(valor) && avisarRepetidos) {
                System.out.println("El " + valor + " está repetido, el ABB no admite repetidos: se ignora.");
            }
        }
        return abb;
    }

    private static ArbolAVL construirAvl() {
        ArbolAVL avl = new ArbolAVL();
        for (Object valor : construirAbb(false).preorden().aArreglo()) {
            avl.insertar((Integer) valor);
        }
        return avl;
    }

    private static void punto5Abb() {
        titulo("PUNTO 5a/b - ABB con FE y recorridos");
        ArbolBinarioBusqueda abb = construirAbb(true);
        System.out.println("a) ABB con FE (la raíz a la izquierda, derecha arriba):");
        System.out.print(abb.dibujar());
        System.out.println("b) Preorden:  " + abb.preorden());
        System.out.println("   Inorden:   " + abb.inorden());
        System.out.println("   Postorden: " + abb.postorden());
        System.out.println("   Amplitud:  " + abb.amplitud());
    }

    private static void punto5Avl() {
        titulo("PUNTO 5c - AVL construido con el preorden");
        imprimirPasos(construirAvl());
    }

    private static void punto5Eliminar() {
        titulo("PUNTO 5d - Eliminación del 45 en el AVL");
        ArbolAVL avl = construirAvl();
        avl.limpiarPasos();
        avl.eliminar(45);
        imprimirPasos(avl);
        System.out.println("¿AVL balanceado? " + avl.estaBalanceado());
    }

    /** En consola se listan los pasos y el árbol final; el dashboard dibuja cada paso. */
    private static void imprimirPasos(ArbolAVL avl) {
        for (Paso paso : avl.getPasos()) {
            System.out.println("- " + paso.getDescripcion());
        }
        System.out.print(avl.dibujar());
    }
}
