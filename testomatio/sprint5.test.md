<!-- suite
tags: sprint5
-->

# Sprint 5 - Pago simulado, stock y comprobante (pruebas unitarias)

<!-- test
priority: high
tags: sprint5, unitaria, HU-10
-->

## CP46 - Pagar con tarjeta de 15 dígitos

**Historia de usuario:** HU-10 Pago simulado  
**Criterio de aceptación:** CA2 y CA4: Valida la tarjeta; si el pago falla, no se confirma.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `PagoVentaTest.pagar_tarjetaDe15Digitos_debeRechazarLaVenta`

### Precondiciones

- Vendas de algodón con stock 40.

### Datos de prueba

- Tarjeta: 411111111111111
- Vencimiento: 12/99
- CVV: 123

### Pasos

1. Llamar a VentaService.registrarVenta.

### Resultado esperado

Venta RECHAZADA, mensaje "Pago rechazado: el número de tarjeta debe tener 16 dígitos." y el stock sigue en 40.

<!-- test
priority: high
tags: sprint5, unitaria, HU-10
-->

## CP47 - Pagar con tarjeta vencida

**Historia de usuario:** HU-10 Pago simulado  
**Criterio de aceptación:** CA2 y CA4: Valida el vencimiento de la tarjeta.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `PagoVentaTest.pagar_tarjetaVencida_debeRechazarLaVenta`

### Datos de prueba

- Tarjeta: 4111111111111111
- Vencimiento: 01/20
- CVV: 123

### Pasos

1. Llamar a VentaService.registrarVenta.

### Resultado esperado

Venta RECHAZADA: "Pago rechazado: la tarjeta está vencida o la fecha no tiene el formato MM/AA."

<!-- test
priority: normal
tags: sprint5, unitaria, HU-10
-->

## CP48 - Pagar con CVV de 2 dígitos

**Historia de usuario:** HU-10 Pago simulado  
**Criterio de aceptación:** CA2 y CA4: Valida el CVV.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `PagoVentaTest.pagar_cvvDe2Digitos_debeRechazarLaVenta`

### Datos de prueba

- Tarjeta: 4111111111111111
- Vencimiento: 12/99
- CVV: 12

### Pasos

1. Llamar a VentaService.registrarVenta.

### Resultado esperado

Venta RECHAZADA: "Pago rechazado: el CVV debe tener 3 dígitos."

<!-- test
priority: high
tags: sprint5, unitaria, HU-10, HU-11
-->

## CP49 - Pago correcto registra la venta pagada

**Historia de usuario:** HU-10 Pago simulado, HU-11 Actualización de stock  
**Criterio de aceptación:** HU-10 CA3 y HU-11 CA1: Pago válido registra la venta y descuenta el stock.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `PagoVentaTest.pagar_datosCorrectos_debeRegistrarVentaPagadaYDescontarStock`

### Precondiciones

- Vendas de algodón (S/ 19.90) con stock 40.

### Datos de prueba

- 2 unidades
- Tarjeta: 4111 1111 1111 1111
- Vencimiento: 12/99
- CVV: 123

### Pasos

1. Llamar a VentaService.registrarVenta.

### Resultado esperado

Venta PAGADA por S/ 39.80, guarda solo "1111" y el stock baja de 40 a 38.

<!-- test
priority: high
tags: sprint5, unitaria, HU-11
-->

## CP50 - Venta que deja el stock en el mínimo

**Historia de usuario:** HU-11 Actualización de stock  
**Criterio de aceptación:** CA3: Si el stock alcanza el mínimo, genera una alerta.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `PagoVentaTest.pagar_stockLlegaAlMinimo_debeGenerarAlerta`

### Precondiciones

- Guantes de box 16 oz: stock 5, mínimo 4.

### Datos de prueba

- 1 unidad, tarjeta válida

### Pasos

1. Llamar a VentaService.registrarVenta.

### Resultado esperado

Stock queda en 4 y se avisa "Guantes de box (talla 16 oz): quedan 4".

<!-- test
priority: high
tags: sprint5, unitaria, HU-11
-->

## CP51 - Venta con stock insuficiente

**Historia de usuario:** HU-11 Actualización de stock  
**Criterio de aceptación:** CA2: El stock nunca puede quedar negativo.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `PagoVentaTest.pagar_stockInsuficiente_debeLanzarExcepcion`

### Precondiciones

- Guantes de box 12 oz: stock 2.

### Datos de prueba

- Cantidad: 5, tarjeta válida

### Pasos

1. Llamar a VentaService.registrarVenta.

### Resultado esperado

Mensaje "No hay stock suficiente para: Guantes de box (talla 12 oz): pediste 5, hay 2" y el stock sigue en 2.

<!-- test
priority: normal
tags: sprint5, unitaria, HU-10
-->

## CP52 - Pagar sin datos de tarjeta

**Historia de usuario:** HU-10 Pago simulado  
**Criterio de aceptación:** CA1: Solicita los datos necesarios para el pago.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `PagoVentaTest.pagar_sinDatosDeTarjeta_debeLanzarExcepcion`

### Datos de prueba

- Pago: (sin datos)

### Pasos

1. Llamar a VentaService.registrarVenta.

### Resultado esperado

Mensaje "Debe ingresar los datos de pago."

<!-- test
priority: normal
tags: sprint5, unitaria, HU-12
-->

## CP53 - Boleta de una compra rechazada

**Historia de usuario:** HU-12 Comprobante de compra  
**Criterio de aceptación:** CA5: Solo las compras pagadas tienen boleta.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `BoletaTest.boleta_deCompraRechazada_debeLanzarExcepcion`

### Precondiciones

- La venta 1005 está RECHAZADA.

### Datos de prueba

- Venta: 1005
- Cliente: 1001

### Pasos

1. Llamar a VentaService.buscarVentaPagadaDelCliente.

### Resultado esperado

Mensaje "Solo las compras pagadas tienen boleta."

<!-- test
priority: high
tags: sprint5, unitaria, HU-12
-->

## CP54 - Boleta de otro cliente

**Historia de usuario:** HU-12 Comprobante de compra  
**Criterio de aceptación:** CA5: Solo el cliente dueño descarga su boleta.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `BoletaTest.boleta_deOtroCliente_debeLanzarExcepcion`

### Precondiciones

- La venta 1001 es de María (1001).

### Datos de prueba

- Venta: 1001
- Cliente que descarga: 1002

### Pasos

1. Llamar a VentaService.buscarVentaPagadaDelCliente.

### Resultado esperado

Mensaje "Solo el cliente dueño de la compra puede descargar la boleta."

<!-- test
priority: normal
tags: sprint5, unitaria, HU-12
-->

## CP55 - Boleta de una compra pagada propia

**Historia de usuario:** HU-12 Comprobante de compra  
**Criterio de aceptación:** CA5: El cliente puede descargar la boleta de su compra.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `BoletaTest.boleta_compraPagadaDelMismoCliente_debeDevolverLaVenta`

### Precondiciones

- La venta 1001 de María está PAGADA.

### Datos de prueba

- Venta: 1001
- Cliente: 1001

### Pasos

1. Llamar a VentaService.buscarVentaPagadaDelCliente.

### Resultado esperado

Devuelve la venta para generar la boleta.

<!-- suite
tags: sprint5
-->

# Sprint 5 - Pago simulado, stock y comprobante (escenarios BDD)

<!-- test
priority: high
tags: sprint5, bdd, HU-10, HU-11, HU-12
-->

## E13 - Con una tarjeta válida la compra se realiza

**Historia de usuario:** HU-10 Pago simulado, HU-11 Actualización de stock, HU-12 Comprobante de compra  
**Criterio de aceptación:** HU-10 CA3, HU-11 CA1 y HU-12: Compra pagada con boleta.  
**Prioridad:** Alta  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU10_HU12_pago_boleta.feature`

### Precondiciones

- Cliente nuevo con 1 Vendas de algodón en el carrito.

### Datos de prueba

- Tarjeta: 4111111111111111
- Vencimiento: 12/29
- CVV: 123

### Pasos

1. Continuar al pago.
2. Llenar los datos de la tarjeta.
3. Presionar PAGAR.

### Resultado esperado

Se muestra "Pago aprobado. Tu compra fue registrada." y el botón "DESCARGAR BOLETA (PDF)".

<!-- test
priority: high
tags: sprint5, bdd, HU-10
-->

## E14 - Con una tarjeta de 15 dígitos no se compra

**Historia de usuario:** HU-10 Pago simulado  
**Criterio de aceptación:** CA2 y CA4: Valida la tarjeta y no confirma la compra.  
**Prioridad:** Alta  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU10_HU12_pago_boleta.feature`

### Precondiciones

- Cliente nuevo con 1 Vendas de algodón en el carrito.

### Datos de prueba

- Tarjeta: 411111111111111
- Vencimiento: 12/29
- CVV: 123

### Pasos

1. Continuar al pago.
2. Llenar los datos de la tarjeta.
3. Presionar PAGAR.

### Resultado esperado

Se muestra "El número de tarjeta debe tener 16 dígitos." y no se registra la compra.

