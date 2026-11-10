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
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servidor HTTP sencillo (incluido en el JDK) que entrega el dashboard
 * de la carpeta "frontend" y responde la API en JSON con los resultados de cada punto.
 */
public class ServidorDashboard {

    public static void iniciar(int puerto) throws IOException {
        HttpServer servidor = HttpServer.create(new InetSocketAddress(puerto), 0);
        servidor.createContext("/", ServidorDashboard::archivoEstatico);
        servidor.createContext("/api/punto1", e -> responder(e, () -> punto1(parametros(e))));
        servidor.createContext("/api/punto2", e -> responder(e, () -> punto2(parametros(e))));
        servidor.createContext("/api/punto3", e -> responder(e, () -> punto3(parametros(e))));
        servidor.createContext("/api/punto4", e -> responder(e, () -> punto4(parametros(e))));
        servidor.createContext("/api/punto5", e -> responder(e, () -> punto5(parametros(e))));
        servidor.start();
        System.out.println("Dashboard disponible en http://localhost:" + puerto);
    }

    // ---------------- Puntos ----------------

    /** ops: "O5 CONFIRMADA, O4 ANULADA, ..." leído desde el tope. */
    static String punto1(Map<String, String> p) {
        String[] items = lista(p.get("ops"));
        Pila<Operacion> historial = new Pila<>();
        for (int i = items.length - 1; i >= 0; i--) { // se apila desde el fondo
            String[] partes = items[i].split("\\s+");
            if (partes.length != 2) {
                throw new IllegalArgumentException("Formato de operación inválido: " + items[i]);
            }
            historial.apilar(new Operacion(partes[0], EstadoOperacion.valueOf(partes[1].toUpperCase())));
        }
        Pila<Operacion> resultado = HistorialServicio.obtenerUltimasConfirmadas(historial, entero(p, "n"));
        return "{\"original\":" + arreglo(historial.aArreglo()) + ",\"resultado\":" + arreglo(resultado.aArreglo()) + "}";
    }

    /** acciones: "A6, A5, ..." leído desde el tope. */
    static String punto2(Map<String, String> p) {
        String[] items = lista(p.get("acciones"));
        Pila<String> pila = new Pila<>();
        for (int i = items.length - 1; i >= 0; i--) {
            pila.apilar(items[i]);
        }
        Pila<String> resultado = HistorialServicio.obtenerSegmento(pila, entero(p, "inicio"), entero(p, "fin"));
        return "{\"original\":" + arreglo(pila.aArreglo()) + ",\"resultado\":" + arreglo(resultado.aArreglo()) + "}";
    }

    /** solicitudes: "S1 NORMAL 10, S2 ALTA 30, ..." en orden de llegada. */
    static String punto3(Map<String, String> p) {
        Cola<Solicitud> pendientes = new Cola<>();
        for (String item : lista(p.get("solicitudes"))) {
            String[] partes = item.split("\\s+");
            if (partes.length != 3) {
                throw new IllegalArgumentException("Formato de solicitud inválido: " + item);
            }
            pendientes.encolar(new Solicitud(partes[0], Prioridad.valueOf(partes[1].toUpperCase()),
                    Integer.parseInt(partes[2])));
        }
        String inicial = arreglo(pendientes.aArreglo());
        Cola<Solicitud> seleccionadas = SoporteServicio.seleccionarSolicitudes(pendientes, entero(p, "minutos"));
        return "{\"inicial\":" + inicial + ",\"seleccionadas\":" + arreglo(seleccionadas.aArreglo())
                + ",\"pendientes\":" + arreglo(pendientes.aArreglo()) + "}";
    }

    /** valores: códigos a insertar; inf, sup: rango; eliminar: nodo a eliminar (predecesor). */
    static String punto4(Map<String, String> p) {
        ArbolBinarioBusqueda arbol = new ArbolBinarioBusqueda();
        for (int valor : enteros(p.get("valores"))) {
            arbol.insertar(valor);
        }
        int cantidad = LaboratorioServicio.contarEquiposEnRango(arbol.getRaiz(), entero(p, "inf"), entero(p, "sup"));
        String antes = arbol.aJson();
        boolean eliminado = arbol.eliminar(entero(p, "eliminar"));
        return "{\"cantidad\":" + cantidad + ",\"antes\":" + antes + ",\"eliminado\":" + eliminado
                + ",\"despues\":" + arbol.aJson() + ",\"inorden\":" + arreglo(arbol.inorden().aArreglo()) + "}";
    }

    /** valores: lista para el ABB; eliminar: nodo a eliminar del AVL. */
    static String punto5(Map<String, String> p) {
        ArbolBinarioBusqueda abb = new ArbolBinarioBusqueda();
        Cola<Integer> repetidos = new Cola<>();
        for (int valor : enteros(p.get("valores"))) {
            if (!abb.insertar(valor)) {
                repetidos.encolar(valor);
            }
        }
        Object[] preorden = abb.preorden().aArreglo();

        ArbolAVL avl = new ArbolAVL();
        for (Object valor : preorden) {
            avl.insertar((Integer) valor);
        }
        String pasosConstruccion = pasos(avl.getPasos());
        String avlFinal = avl.aJson();
        avl.limpiarPasos();
        avl.eliminar(entero(p, "eliminar"));

        return "{\"abb\":" + abb.aJson()
                + ",\"repetidos\":" + arreglo(repetidos.aArreglo())
                + ",\"preorden\":" + arreglo(preorden)
                + ",\"inorden\":" + arreglo(abb.inorden().aArreglo())
                + ",\"postorden\":" + arreglo(abb.postorden().aArreglo())
                + ",\"amplitud\":" + arreglo(abb.amplitud().aArreglo())
                + ",\"pasosAvl\":" + pasosConstruccion
                + ",\"avl\":" + avlFinal
                + ",\"pasosEliminacion\":" + pasos(avl.getPasos())
                + ",\"avlFinal\":" + avl.aJson()
                + ",\"balanceado\":" + avl.estaBalanceado() + "}";
    }

    // ---------------- Utilidades HTTP / JSON ----------------

    private static void archivoEstatico(HttpExchange e) throws IOException {
        String ruta = e.getRequestURI().getPath();
        // Solo se sirven estos tres archivos (evita leer otros archivos del disco).
        String nombre = switch (ruta) {
            case "/", "/index.html" -> "index.html";
            case "/styles.css" -> "styles.css";
            case "/app.js" -> "app.js";
            default -> null;
        };
        if (nombre == null) {
            enviar(e, 404, "text/plain", "No encontrado".getBytes(StandardCharsets.UTF_8));
            return;
        }
        String tipo = nombre.endsWith(".css") ? "text/css" : nombre.endsWith(".js") ? "text/javascript" : "text/html";
        enviar(e, 200, tipo + "; charset=utf-8", Files.readAllBytes(Path.of("frontend", nombre)));
    }

    private interface Calculo {
        String calcular();
    }

    /** Ejecuta el cálculo; si los datos de entrada son inválidos responde 400 con el mensaje. */
    private static void responder(HttpExchange e, Calculo calculo) throws IOException {
        int codigo = 200;
        String json;
        try {
            json = calculo.calcular();
        } catch (RuntimeException error) {
            codigo = 400;
            json = "{\"error\":" + texto("Datos inválidos: " + error.getMessage()) + "}";
        }
        enviar(e, codigo, "application/json; charset=utf-8", json.getBytes(StandardCharsets.UTF_8));
    }

    private static void enviar(HttpExchange e, int codigo, String tipo, byte[] cuerpo) throws IOException {
        e.getResponseHeaders().set("Content-Type", tipo);
        // Permite usar el dashboard abierto como archivo o desde el navegador de IntelliJ.
        e.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        e.sendResponseHeaders(codigo, cuerpo.length);
        try (OutputStream salida = e.getResponseBody()) {
            salida.write(cuerpo);
        }
    }

    private static Map<String, String> parametros(HttpExchange e) {
        Map<String, String> mapa = new HashMap<>();
        String consulta = e.getRequestURI().getRawQuery();
        if (consulta != null) {
            for (String par : consulta.split("&")) {
                String[] kv = par.split("=", 2);
                mapa.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                        kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "");
            }
        }
        return mapa;
    }

    static String[] lista(String texto) {
        if (texto == null || texto.isBlank()) {
            return new String[0];
        }
        String[] partes = texto.trim().split("\\s*[,\\-]\\s*");
        return partes;
    }

    static int[] enteros(String texto) {
        String[] partes = lista(texto);
        int[] numeros = new int[partes.length];
        for (int i = 0; i < partes.length; i++) {
            numeros[i] = Integer.parseInt(partes[i]);
        }
        return numeros;
    }

    static int entero(Map<String, String> p, String nombre) {
        String valor = p.get(nombre);
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Falta el parámetro " + nombre);
        }
        return Integer.parseInt(valor.trim());
    }

    static String arreglo(Object[] elementos) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < elementos.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(texto(String.valueOf(elementos[i])));
        }
        return sb.append(']').toString();
    }

    static String texto(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    static String pasos(List<Paso> pasos) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < pasos.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            Paso paso = pasos.get(i);
            sb.append("{\"descripcion\":").append(texto(paso.getDescripcion()))
                    .append(",\"arbol\":").append(paso.getArbolJson() == null ? "null" : paso.getArbolJson())
                    .append('}');
        }
        return sb.append(']').toString();
    }
}
