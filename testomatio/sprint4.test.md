<!-- suite
tags: sprint4
-->

# Sprint 4 - Gestión de productos y compra (pruebas unitarias)

<!-- test
priority: normal
tags: sprint4, unitaria, HU-08
-->

## CP36 - Registrar producto sin nombre

**Historia de usuario:** HU-08 Gestión de productos  
**Criterio de aceptación:** CA1: Permite registrar productos con sus datos.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ProductoServiceTest.registrar_nombreVacio_debeLanzarExcepcion`

### Datos de prueba

- Nombre: (espacios)

### Pasos

1. Llamar a ProductoService.registrarProducto.

### Resultado esperado

Mensaje "El nombre del producto es obligatorio".

<!-- test
priority: high
tags: sprint4, unitaria, HU-08
-->

## CP37 - Registrar producto con precio 0

**Historia de usuario:** HU-08 Gestión de productos  
**Criterio de aceptación:** CA1: El precio debe ser válido.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ProductoServiceTest.registrar_precioCero_debeLanzarExcepcion`

### Datos de prueba

- Producto: Vendas de algodón
- Precio: 0

### Pasos

1. Llamar a ProductoService.registrarProducto.

### Resultado esperado

Mensaje "El precio debe ser mayor a 0".

<!-- test
priority: high
tags: sprint4, unitaria, HU-08
-->

## CP38 - Registrar producto con stock negativo

**Historia de usuario:** HU-08 Gestión de productos  
**Criterio de aceptación:** CA1: El stock no puede ser negativo.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ProductoServiceTest.registrar_stockNegativo_debeLanzarExcepcion`

### Datos de prueba

- Stock: -1

### Pasos

1. Llamar a ProductoService.registrarProducto.

### Resultado esperado

Mensaje "El stock no puede ser negativo".

<!-- test
priority: low
tags: sprint4, unitaria, HU-08
-->

## CP39 - Registrar producto con categoría inexistente

**Historia de usuario:** HU-08 Gestión de productos  
**Criterio de aceptación:** CA1: La categoría debe ser válida.  
**Prioridad:** Baja  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ProductoServiceTest.registrar_categoriaQueNoExiste_debeLanzarExcepcion`

### Datos de prueba

- Categoría: JUGUETES

### Pasos

1. Llamar a ProductoService.registrarProducto.

### Resultado esperado

Mensaje "La categoría no es válida".

<!-- test
priority: normal
tags: sprint4, unitaria, HU-08
-->

## CP40 - Registrar producto repetido (mismo nombre y talla)

**Historia de usuario:** HU-08 Gestión de productos  
**Criterio de aceptación:** CA1: No se repiten productos.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ProductoServiceTest.registrar_productoRepetido_debeLanzarExcepcion`

### Precondiciones

- Ya existe "Vendas de algodón" talla 4.5 m.

### Datos de prueba

- Nombre: Vendas de algodón
- Talla: 4.5 m

### Pasos

1. Llamar a ProductoService.registrarProducto.

### Resultado esperado

Mensaje "Ya existe un producto con ese nombre y talla".

<!-- test
priority: low
tags: sprint4, unitaria, HU-08
-->

## CP41 - Registrar producto sin talla

**Historia de usuario:** HU-08 Gestión de productos  
**Criterio de aceptación:** CA1: Permite registrar productos.  
**Prioridad:** Baja  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ProductoServiceTest.registrar_sinTalla_debeGuardarConTallaUnicaYActivo`

### Datos de prueba

- Producto: Protector bucal
- Talla: (vacía)

### Pasos

1. Llamar a ProductoService.registrarProducto.

### Resultado esperado

Se guarda con talla "Única" y estado ACTIVO.

<!-- test
priority: normal
tags: sprint4, unitaria, HU-09
-->

## CP42 - Verificar un carrito vacío

**Historia de usuario:** HU-09 Compra de equipamiento  
**Criterio de aceptación:** CA1: El cliente selecciona productos.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `CarritoServiceTest.verificarCarrito_vacio_debeLanzarExcepcion`

### Datos de prueba

- Carrito: sin productos

### Pasos

1. Llamar a VentaService.verificarCarrito.

### Resultado esperado

Mensaje "El carrito está vacío."

<!-- test
priority: normal
tags: sprint4, unitaria, HU-09
-->

## CP43 - Carrito con cantidad 0

**Historia de usuario:** HU-09 Compra de equipamiento  
**Criterio de aceptación:** CA1: El cliente selecciona productos y cantidades.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `CarritoServiceTest.verificarCarrito_cantidadCero_debeLanzarExcepcion`

### Datos de prueba

- Producto 1012, cantidad 0

### Pasos

1. Llamar a VentaService.verificarCarrito.

### Resultado esperado

Mensaje "La cantidad de cada producto debe ser 1 o más."

<!-- test
priority: high
tags: sprint4, unitaria, HU-09
-->

## CP44 - Calcular subtotal y total del carrito

**Historia de usuario:** HU-09 Compra de equipamiento  
**Criterio de aceptación:** CA3 y CA4: Calcula correctamente el subtotal y el total.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `CarritoServiceTest.verificarCarrito_conStock_debeCalcularElTotal`

### Precondiciones

- Vendas de algodón S/ 19.90 y Guantes de MMA S/ 129.90 con stock.

### Datos de prueba

- 2 × Vendas de algodón
- 1 × Guantes de MMA

### Pasos

1. Llamar a VentaService.verificarCarrito.

### Resultado esperado

Subtotal S/ 39.80, total S/ 169.70 y se puede continuar.

<!-- test
priority: high
tags: sprint4, unitaria, HU-09
-->

## CP45 - Carrito sin stock suficiente

**Historia de usuario:** HU-09 Compra de equipamiento  
**Criterio de aceptación:** CA2 y CA5: Si el stock es insuficiente, no permite completar la compra.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `CarritoServiceTest.verificarCarrito_sinStockSuficiente_noDebePermitirContinuar`

### Precondiciones

- Guantes de box 12 oz tienen stock 2.

### Datos de prueba

- Cantidad pedida: 5

### Pasos

1. Llamar a VentaService.verificarCarrito.

### Resultado esperado

No permite continuar: "Guantes de box (talla 12 oz): pediste 5, hay 2".

<!-- suite
tags: sprint4
-->

# Sprint 4 - Gestión de productos y compra (escenarios BDD)

<!-- test
priority: normal
tags: sprint4, bdd, HU-08
-->

## E10 - El administrador ve la alerta de stock mínimo

**Historia de usuario:** HU-08 Gestión de productos  
**Criterio de aceptación:** CA4: Identifica cuando el stock llega al mínimo.  
**Prioridad:** Media  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU08_HU09_productos_compra.feature`

### Precondiciones

- Hay productos con stock igual o menor al mínimo.

### Datos de prueba

- Filtro: solo stock bajo

### Pasos

1. Abrir /gestion-productos.
2. Marcar "solo stock bajo".

### Resultado esperado

Se muestra la alerta "producto(s) con stock bajo" y todos los productos listados tienen etiqueta de alerta.

<!-- test
priority: high
tags: sprint4, bdd, HU-09
-->

## E11 - El carrito calcula el total de la compra

**Historia de usuario:** HU-09 Compra de equipamiento  
**Criterio de aceptación:** CA1, CA3 y CA4: Calcula subtotal y total.  
**Prioridad:** Alta  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU08_HU09_productos_compra.feature`

### Precondiciones

- Sesión iniciada como cliente.

### Datos de prueba

- Producto: Vendas de algodón (S/ 19.90)
- Cantidad: 2

### Pasos

1. Abrir /comprar-productos.
2. Filtrar por General.
3. Agregar 2 unidades al carrito.

### Resultado esperado

El total del carrito es "S/ 39.80".

<!-- test
priority: normal
tags: sprint4, bdd, HU-09
-->

## E12 - Un producto agotado no se puede agregar

**Historia de usuario:** HU-09 Compra de equipamiento  
**Criterio de aceptación:** CA5: Sin stock no permite comprar.  
**Prioridad:** Media  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU08_HU09_productos_compra.feature`

### Precondiciones

- "Cuerda para saltar" tiene stock 0.

### Datos de prueba

- Producto: Cuerda para saltar

### Pasos

1. Abrir /comprar-productos.
2. Filtrar por General.

### Resultado esperado

El botón muestra "SIN STOCK" y está deshabilitado.

