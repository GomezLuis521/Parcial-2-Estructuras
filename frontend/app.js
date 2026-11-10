// Dashboard: envía cada formulario a la API Java y dibuja el resultado.

// Si la página se abre como archivo o desde IntelliJ, igual se consulta al servidor Java (Main).
const SERVIDOR = location.port === "8080" ? "" : "http://localhost:8080";

document.querySelectorAll("form[data-punto]").forEach((form) => {
    form.addEventListener("submit", (evento) => {
        evento.preventDefault();
        calcular(form);
    });
    calcular(form); // mostrar el ejemplo del enunciado al abrir
});

async function calcular(form) {
    const punto = form.dataset.punto;
    const salida = form.nextElementSibling;
    const parametros = new URLSearchParams(new FormData(form));
    try {
        const respuesta = await fetch(`${SERVIDOR}/api/punto${punto}?${parametros}`);
        const datos = await respuesta.json();
        if (datos.error) {
            salida.innerHTML = `<p class="error">${escapar(datos.error)}</p>`;
            return;
        }
        salida.innerHTML = RENDER[punto](datos);
    } catch (e) {
        salida.innerHTML = `<p class="error">No se pudo conectar con el servidor Java.
            Ejecuta la clase Main y abre http://localhost:8080</p>`;
    }
}

const RENDER = {
    1: (d) => fila("Original", d.original, "tope") + fila("Resultado", d.resultado, "tope"),
    2: (d) => fila("Original", d.original, "tope") + fila("Segmento", d.resultado, "tope"),
    3: (d) => fila("Inicial", d.inicial, "frente") + fila("Seleccionadas", d.seleccionadas, "frente")
        + fila("Pendientes", d.pendientes, "frente"),
    4: (d) => `
        <h3>a) Equipos dentro del rango</h3>
        <div class="dato-grande">${d.cantidad}</div>
        <h3>b) Árbol inicial</h3>${arbol(d.antes)}
        <h3>Árbol después de eliminar (${d.eliminado ? "reemplazo: mayor de la izquierda" : "el valor no existía"})</h3>
        ${arbol(d.despues)}
        ${fila("Inorden", d.inorden)}`,
    5: (d) => `
        <h3>a) Árbol ABB con FE</h3>
        ${d.repetidos.length ? `<p class="vacio">Repetidos ignorados: ${d.repetidos.join(", ")}</p>` : ""}
        ${arbol(d.abb)}
        <h3>b) Recorridos</h3>
        ${fila("Preorden", d.preorden)}${fila("Inorden", d.inorden)}
        ${fila("Postorden", d.postorden)}${fila("Amplitud", d.amplitud)}
        <h3>c) AVL construido con el preorden (paso a paso)</h3>
        ${d.pasosAvl.map(paso).join("")}
        <h3>AVL final</h3>${arbol(d.avl)}
        <h3>d) Eliminación en el AVL</h3>
        ${d.pasosEliminacion.map(paso).join("")}
        <p>¿Quedó balanceado? <strong>${d.balanceado ? "Sí" : "No"}</strong></p>`,
};

// ---------- Piezas de interfaz ----------

function fila(nombre, elementos, extremo) {
    const contenido = elementos.length
        ? elementos.map((e) => `<span class="chip">${escapar(e)}</span>`).join('<span class="flecha">→</span>')
        : '<span class="vacio">(vacía)</span>';
    const nota = extremo && elementos.length ? `<span class="vacio">← ${extremo}</span>` : "";
    return `<div class="fila"><span class="nombre">${nombre}</span>${nota}${contenido}</div>`;
}

function paso(p) {
    const alerta = p.descripcion.startsWith("Desbalance") ? " alerta" : "";
    return `<div class="paso${alerta}"><p>${escapar(p.descripcion)}</p>${p.arbol ? arbol(p.arbol) : ""}</div>`;
}

/** Dibuja el árbol en SVG: x según el orden inorden, y según el nivel. Muestra el FE de cada nodo. */
function arbol(raiz) {
    if (!raiz) return '<p class="vacio">(árbol vacío)</p>';
    const nodos = [];
    let indice = 0;
    (function ubicar(n, nivel) {
        if (!n) return;
        ubicar(n.i, nivel + 1);
        n.x = 30 + indice++ * 46;
        n.y = 30 + nivel * 70;
        nodos.push(n);
        ubicar(n.d, nivel + 1);
    })(raiz, 0);

    const ancho = 60 + (indice - 1) * 46;
    const alto = Math.max(...nodos.map((n) => n.y)) + 40;
    let lineas = "";
    let circulos = "";
    for (const n of nodos) {
        for (const hijo of [n.i, n.d]) {
            if (hijo) lineas += `<line x1="${n.x}" y1="${n.y}" x2="${hijo.x}" y2="${hijo.y}" stroke="#9aa8a0"/>`;
        }
        const malo = Math.abs(n.fe) > 1;
        circulos += `
            <circle cx="${n.x}" cy="${n.y}" r="17" fill="${malo ? "#fdecea" : "#e3f3e8"}" stroke="${malo ? "#c0392b" : "#1a7f3c"}" stroke-width="1.5"/>
            <text x="${n.x}" y="${n.y + 4}" text-anchor="middle" font-size="12" font-weight="600" fill="#1f2a24">${n.v}</text>
            <text x="${n.x}" y="${n.y - 22}" text-anchor="middle" font-size="10" fill="${malo ? "#c0392b" : "#5d6b63"}">FE=${n.fe}</text>`;
    }
    return `<div class="arbol"><svg width="${ancho}" height="${alto}" role="img" aria-label="Árbol binario">${lineas}${circulos}</svg></div>`;
}

function escapar(texto) {
    return String(texto).replace(/[&<>"]/g, (c) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;" })[c]);
}
