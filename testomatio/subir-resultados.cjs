/**
 * Semana 10 – Sube a Testomat.io los resultados REALES de las pruebas como una ejecución (Run).
 *
 * Lee:
 *   - los casos de prueba de esta carpeta (sprint1.test.md … sprint6.test.md), con su id @T...
 *   - los resultados que genera Maven en ../puntomarcial/target/surefire-reports/*.xml
 * y marca cada caso como aprobado (passed) o fallido (failed) según lo que pasó al ejecutarlo.
 *
 * Uso (PowerShell, dentro de la carpeta testomatio):
 *   $env:TESTOMATIO="tstmt_..."
 *   node subir-resultados.cjs
 */
const fs = require('fs');
const path = require('path');

const CARPETA_XML = path.join(__dirname, '..', 'puntomarcial', 'target', 'surefire-reports');

// Escenarios BDD: caso -> [runner, nombre del escenario en el reporte de Maven]
const ESCENARIOS = {
  E01: ['Sprint1BddTest', 'Example #1.1'],
  E02: ['Sprint1BddTest', 'Example #1.2'],
  E03: ['Sprint1BddTest', 'Example #1.3'],
  E04: ['Sprint1BddTest', 'HU-01 CA3 - Con una contraseña incorrecta se muestra un error'],
  E05: ['Sprint1BddTest', 'HU-02 CA1, CA4 y CA5 - El administrador registra un cliente'],
  E06: ['Sprint2BddTest', 'HU-05 CA1, CA3 y CA4 - El cliente reserva una clase con cupos'],
  E07: ['Sprint2BddTest', 'HU-05 CA5 - Una clase llena no se puede reservar'],
  E08: ['Sprint3BddTest', 'HU-06 CA3 y CA4 - El cliente cancela una reserva con más de 2 horas de anticipación'],
  E09: ['Sprint3BddTest', 'HU-07 CA1 y CA2 - El administrador supervisa el aforo de las clases'],
  E10: ['Sprint4BddTest', 'HU-08 CA4 - El administrador ve la alerta de stock mínimo'],
  E11: ['Sprint4BddTest', 'HU-09 CA1, CA3 y CA4 - El carrito calcula el total de la compra'],
  E12: ['Sprint4BddTest', 'HU-09 CA5 - Un producto agotado no se puede agregar al carrito'],
  E13: ['Sprint5BddTest', 'HU-10 CA3, HU-11 CA1 y HU-12 - Con una tarjeta válida la compra se realiza'],
  E14: ['Sprint5BddTest', 'HU-10 CA2 y CA4 - Con una tarjeta de 15 dígitos no se realiza la compra'],
  E15: ['Sprint6BddTest', 'HU-13 CA2, CA3 y CA4 - El instructor registra la asistencia en una clase en curso'],
  E16: ['Sprint6BddTest', 'HU-04 CA5 - Una clase con reservas no se puede eliminar'],
  E17: ['Sprint6BddTest', 'HU-04 CA5 - El administrador elimina una clase sin reservas'],
};

function decodificar(texto) {
  return texto.replace(/&quot;/g, '"').replace(/&apos;/g, "'").replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&amp;/g, '&');
}

// 1. Casos de prueba de los archivos .test.md: código (CP01/E01), id de Testomat.io, título y prueba asociada
function leerCasos() {
  const casos = [];
  for (const archivo of fs.readdirSync(__dirname).filter(f => f.endsWith('.test.md')).sort()) {
    const texto = fs.readFileSync(path.join(__dirname, archivo), 'utf8');
    const bloques = texto.split('<!-- test').slice(1);
    for (const bloque of bloques) {
      const id = (bloque.match(/id:\s*(@T[\w\d]{8})/) || [])[1];
      const titulo = (bloque.match(/^##\s+(.+)$/m) || [])[1];
      const codigo = titulo ? titulo.split(' - ')[0].trim() : null;
      const metodo = (bloque.match(/\*\*Prueba automatizada:\*\*\s*`([^`]+)`/) || [])[1];
      casos.push({ archivo, id, titulo, codigo, metodo });
    }
  }
  return casos;
}

// 2. Resultados de Maven: clase.método (o runner + escenario) -> { estado, tiempo, mensaje }
function leerResultados() {
  if (!fs.existsSync(CARPETA_XML)) {
    throw new Error('No existe ' + CARPETA_XML + '. Primero ejecuta las pruebas con Maven.');
  }
  const resultados = {};
  for (const archivo of fs.readdirSync(CARPETA_XML).filter(f => f.startsWith('TEST-') && f.endsWith('.xml'))) {
    const xml = fs.readFileSync(path.join(CARPETA_XML, archivo), 'utf8');
    const casos = xml.split('<testcase ').slice(1);
    for (const caso of casos) {
      const nombre = decodificar((caso.match(/name="([^"]*)"/) || [])[1] || '');
      const clase = ((caso.match(/classname="([^"]*)"/) || [])[1] || '').split('.').pop();
      const tiempo = parseFloat((caso.match(/time="([^"]*)"/) || [])[1] || '0');
      const cuerpo = caso.split('</testcase>')[0];
      let estado = 'passed';
      let mensaje = '';
      const falla = cuerpo.match(/<(failure|error)[^>]*message="([^"]*)"/) || cuerpo.match(/<(failure|error)/);
      if (falla) {
        estado = 'failed';
        mensaje = decodificar(falla[2] || '');
      } else if (/<skipped/.test(cuerpo)) {
        estado = 'skipped';
      }
      resultados[clase + '.' + nombre] = { estado, tiempo, mensaje };
    }
  }
  return resultados;
}

async function main() {
  const casos = leerCasos();
  const resultados = leerResultados();

  // Si los archivos no tienen el id @T de cada caso, se le pide a Testomat.io (por el título del caso)
  if (casos.some(c => !c.id) && process.env.TESTOMATIO) {
    const url = (process.env.TESTOMATIO_URL || 'https://app.testomat.io').trim();
    const respuesta = await fetch(url + '/api/test_data?api_key=' + process.env.TESTOMATIO.trim());
    if (!respuesta.ok) {
      throw new Error('Testomat.io respondió ' + respuesta.status + '. Revisa la clave (TESTOMATIO).');
    }
    const mapa = (await respuesta.json()).tests || {};
    for (const c of casos.filter(c => !c.id)) {
      const id = mapa[c.titulo];
      if (id) c.id = id.startsWith('@T') ? id : '@T' + id;
    }
  }
  const sinId = casos.filter(c => !c.id);
  if (sinId.length && process.env.TESTOMATIO) {
    throw new Error(sinId.length + ' casos no se encontraron en Testomat.io, por ejemplo: ' + sinId[0].titulo);
  }

  // Une cada caso con su resultado
  const reporte = [];
  const sinResultado = [];
  for (const c of casos) {
    const clave = c.metodo ? c.metodo : (ESCENARIOS[c.codigo] || []).join('.');
    const r = resultados[clave];
    if (!r) {
      sinResultado.push(c.codigo + ' (' + clave + ')');
      continue;
    }
    reporte.push({ ...c, ...r });
  }

  const aprobados = reporte.filter(r => r.estado === 'passed').length;
  const fallidos = reporte.filter(r => r.estado === 'failed').length;
  console.log(`Casos: ${casos.length} | con resultado: ${reporte.length} | aprobados: ${aprobados} | fallidos: ${fallidos}`);
  if (sinResultado.length) {
    console.log('Sin resultado (no se ejecutaron, se suben como omitidos):');
    sinResultado.forEach(s => console.log('  - ' + s));
  }

  if (!process.env.TESTOMATIO) {
    console.log('\nFalta la clave. Escribe: $env:TESTOMATIO="tstmt_..." y vuelve a ejecutar.');
    return;
  }

  // 3. Crea la ejecución en Testomat.io y sube cada resultado
  const { TestomatioClient, STATUS } = require('@testomatio/reporter');
  process.env.TESTOMATIO_TITLE = process.env.TESTOMATIO_TITLE || 'Ejecución final – Punto Marcial';
  const cliente = new TestomatioClient();
  await cliente.createRun();

  for (const r of reporte) {
    await cliente.addTestRun(r.estado, {
      test_id: r.id,
      title: r.titulo,
      time: Math.round(r.tiempo * 1000),
      message: r.estado === 'failed' ? r.mensaje : '',
    });
  }
  for (const c of casos.filter(c => sinResultado.some(s => s.startsWith(c.codigo + ' ')))) {
    await cliente.addTestRun(STATUS.SKIPPED, { test_id: c.id, title: c.titulo, message: 'No se ejecutó' });
  }

  await cliente.updateRunStatus(fallidos ? STATUS.FAILED : STATUS.PASSED);
  console.log('\nListo. Abre Testomat.io → Runs para ver la ejecución.');
}

main().catch(error => {
  console.error('Error: ' + error.message);
  process.exit(1);
});
