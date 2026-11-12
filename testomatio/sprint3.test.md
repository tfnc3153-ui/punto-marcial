<!-- suite
tags: sprint3
-->

# Sprint 3 - Cancelación de reservas y control de aforo (pruebas unitarias)

<!-- test
priority: high
tags: sprint3, unitaria, HU-06
-->

## CP29 - Cancelar con menos de 2 horas de anticipación

**Historia de usuario:** HU-06 Cancelación de reservas  
**Criterio de aceptación:** CA6: Si faltan menos de 2 horas, la cancelación es rechazada.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `CancelacionReservaTest.cancelar_conMenosDe2Horas_debeLanzarExcepcion`

### Precondiciones

- La clase empieza dentro de 1 hora.

### Datos de prueba

- Reserva: 1001
- Cliente: 1001

### Pasos

1. Llamar a ReservaService.cancelarReserva.

### Resultado esperado

Mensaje "Solo se puede cancelar con al menos 2 horas de anticipación."

<!-- test
priority: high
tags: sprint3, unitaria, HU-06
-->

## CP30 - Cancelar con más de 2 horas de anticipación

**Historia de usuario:** HU-06 Cancelación de reservas  
**Criterio de aceptación:** CA3, CA4 y CA5: Cancela y el aforo aumenta en uno.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `CancelacionReservaTest.cancelar_conMasDe2Horas_debeCancelarYDevolverElCupo`

### Precondiciones

- La clase empieza dentro de 3 horas y tiene 9 cupos libres.

### Datos de prueba

- Reserva: 1003
- Cliente: 1001

### Pasos

1. Llamar a ReservaService.cancelarReserva.

### Resultado esperado

La reserva queda CANCELADA y los cupos suben de 9 a 10.

<!-- test
priority: normal
tags: sprint3, unitaria, HU-06
-->

## CP31 - Cancelar la reserva de otro cliente

**Historia de usuario:** HU-06 Cancelación de reservas  
**Criterio de aceptación:** CA1: El cliente selecciona una reserva propia.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `CancelacionReservaTest.cancelar_reservaDeOtroCliente_debeLanzarExcepcion`

### Precondiciones

- La reserva 1003 es de María (1001).

### Datos de prueba

- Reserva: 1003
- Cliente que cancela: 1002

### Pasos

1. Llamar a ReservaService.cancelarReserva.

### Resultado esperado

Mensaje "Solo el cliente dueño de la reserva puede cancelarla."

<!-- test
priority: normal
tags: sprint3, unitaria, HU-06
-->

## CP32 - Cancelar una reserva ya cancelada

**Historia de usuario:** HU-06 Cancelación de reservas  
**Criterio de aceptación:** CA1: Solo se cancelan reservas activas.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `CancelacionReservaTest.cancelar_reservaYaCancelada_debeLanzarExcepcion`

### Precondiciones

- La reserva 1003 está CANCELADA.

### Datos de prueba

- Reserva: 1003

### Pasos

1. Llamar a ReservaService.cancelarReserva.

### Resultado esperado

Mensaje "La reserva ya estaba cancelada."

<!-- test
priority: low
tags: sprint3, unitaria, HU-06
-->

## CP33 - Cancelar una reserva que no existe

**Historia de usuario:** HU-06 Cancelación de reservas  
**Criterio de aceptación:** CA1: El cliente selecciona una reserva existente.  
**Prioridad:** Baja  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `CancelacionReservaTest.cancelar_reservaQueNoExiste_debeLanzarExcepcion`

### Precondiciones

- No existe la reserva 9999.

### Datos de prueba

- Reserva: 9999

### Pasos

1. Llamar a ReservaService.cancelarReserva.

### Resultado esperado

Mensaje "La reserva no existe."

<!-- test
priority: high
tags: sprint3, unitaria, HU-07
-->

## CP34 - Bajar el aforo por debajo de las reservas

**Historia de usuario:** HU-07 Supervisión de aforo  
**Criterio de aceptación:** CA5: Nunca permite superar el aforo máximo.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `AforoClaseTest.actualizar_aforoMenorQueLasReservas_debeLanzarExcepcion`

### Precondiciones

- La clase 1010 tiene aforo 10 y 6 reservas.

### Datos de prueba

- Aforo nuevo: 5

### Pasos

1. Llamar a ClaseService.actualizarClase.

### Resultado esperado

Mensaje "El aforo máximo no puede ser menor que las reservas existentes".

<!-- test
priority: normal
tags: sprint3, unitaria, HU-07
-->

## CP35 - Subir el aforo recalcula los cupos

**Historia de usuario:** HU-07 Supervisión de aforo  
**Criterio de aceptación:** CA2: Muestra los cupos disponibles correctos.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `AforoClaseTest.actualizar_aforoMayor_debeRecalcularLosCuposDisponibles`

### Precondiciones

- La clase 1010 tiene aforo 10 y 6 reservas.

### Datos de prueba

- Aforo nuevo: 15

### Pasos

1. Llamar a ClaseService.actualizarClase.

### Resultado esperado

Aforo 15 y 9 cupos disponibles (15 − 6).

<!-- suite
tags: sprint3
-->

# Sprint 3 - Cancelación de reservas y control de aforo (escenarios BDD)

<!-- test
priority: high
tags: sprint3, bdd, HU-06
-->

## E08 - El cliente cancela una reserva con anticipación

**Historia de usuario:** HU-06 Cancelación de reservas  
**Criterio de aceptación:** CA3 y CA4: Cancela con más de 2 horas y la reserva desaparece.  
**Prioridad:** Alta  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU06_HU07_cancelacion_aforo.feature`

### Precondiciones

- Cliente nuevo con una reserva en la clase 1009 (dentro de 4 días).

### Datos de prueba

- Clase: 1009

### Pasos

1. Iniciar sesión.
2. Abrir /mis-reservas.
3. Presionar CANCELAR RESERVA y aceptar la confirmación.

### Resultado esperado

Se muestra "La reserva #N fue cancelada correctamente." y ya no hay reservas para cancelar.

<!-- test
priority: normal
tags: sprint3, bdd, HU-07
-->

## E09 - El administrador supervisa el aforo

**Historia de usuario:** HU-07 Supervisión de aforo  
**Criterio de aceptación:** CA1 y CA2: Muestra el aforo y los cupos de cada clase.  
**Prioridad:** Media  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU06_HU07_cancelacion_aforo.feature`

### Precondiciones

- Sesión iniciada como administrador.

### Datos de prueba

- —

### Pasos

1. Abrir /supervisar-aforo.

### Resultado esperado

Se muestra "CLASES PROGRAMADAS" y una barra de ocupación por cada clase.

