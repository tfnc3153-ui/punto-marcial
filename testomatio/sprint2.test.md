<!-- suite
tags: sprint2
-->

# Sprint 2 - Gestión de clases y reservas (pruebas unitarias)

<!-- test
priority: high
tags: sprint2, unitaria, HU-04
-->

## CP17 - Registrar clase con aforo 0

**Historia de usuario:** HU-04 Gestión de clases  
**Criterio de aceptación:** CA2: Valida que el aforo máximo esté entre 1 y 30.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ClaseServiceTest.registrar_aforoCero_debeLanzarExcepcion`

### Datos de prueba

- Aforo: 0
- MMA, sala A, 18:00–19:00

### Pasos

1. Llamar a ClaseService.registrarClase.

### Resultado esperado

Mensaje "El aforo máximo debe estar entre 1 y 30".

<!-- test
priority: high
tags: sprint2, unitaria, HU-04
-->

## CP18 - Registrar clase con aforo 31

**Historia de usuario:** HU-04 Gestión de clases  
**Criterio de aceptación:** CA2: Valida que el aforo máximo esté entre 1 y 30.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ClaseServiceTest.registrar_aforoMayorA30_debeLanzarExcepcion`

### Datos de prueba

- Aforo: 31

### Pasos

1. Llamar a ClaseService.registrarClase.

### Resultado esperado

Mensaje "El aforo máximo debe estar entre 1 y 30".

<!-- test
priority: normal
tags: sprint2, unitaria, HU-04
-->

## CP19 - Registrar clase que termina antes de empezar

**Historia de usuario:** HU-04 Gestión de clases  
**Criterio de aceptación:** CA1: Permite registrar una clase con horario válido.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ClaseServiceTest.registrar_finAntesDelInicio_debeLanzarExcepcion`

### Datos de prueba

- Inicio: 19:00
- Fin: 18:00

### Pasos

1. Llamar a ClaseService.registrarClase.

### Resultado esperado

Mensaje "El fin de la clase debe ser posterior al inicio".

<!-- test
priority: normal
tags: sprint2, unitaria, HU-04
-->

## CP20 - Registrar clase en una sala que no existe

**Historia de usuario:** HU-04 Gestión de clases  
**Criterio de aceptación:** CA1: La sala debe ser A, B, C o D.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ClaseServiceTest.registrar_salaQueNoExiste_debeLanzarExcepcion`

### Datos de prueba

- Sala: E

### Pasos

1. Llamar a ClaseService.registrarClase.

### Resultado esperado

Mensaje "La sala debe ser A, B, C o D".

<!-- test
priority: normal
tags: sprint2, unitaria, HU-04
-->

## CP21 - Registrar clase de una disciplina que no se dicta

**Historia de usuario:** HU-04 Gestión de clases  
**Criterio de aceptación:** CA1: La disciplina debe ser MMA, Muay Thai o Box.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ClaseServiceTest.registrar_disciplinaQueNoSeDicta_debeLanzarExcepcion`

### Datos de prueba

- Disciplina: KARATE

### Pasos

1. Llamar a ClaseService.registrarClase.

### Resultado esperado

Mensaje "La disciplina debe ser MMA, Muay Thai o Box".

<!-- test
priority: high
tags: sprint2, unitaria, HU-04
-->

## CP22 - Registrar clase con instructor de otra disciplina

**Historia de usuario:** HU-04 Gestión de clases  
**Criterio de aceptación:** CA1: El instructor debe ser de la disciplina de la clase.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ClaseServiceTest.registrar_instructorDeOtraDisciplina_debeLanzarExcepcion`

### Precondiciones

- Luis Paredes (1103) dicta Muay Thai.

### Datos de prueba

- Disciplina: MMA
- Instructor: 1103

### Pasos

1. Llamar a ClaseService.registrarClase.

### Resultado esperado

Mensaje "El instructor Luis Paredes no dicta esa disciplina".

<!-- test
priority: high
tags: sprint2, unitaria, HU-04
-->

## CP23 - Registrar clase con horario cruzado

**Historia de usuario:** HU-04 Gestión de clases  
**Criterio de aceptación:** CA3: No permite traslape de horario en la misma sala/instructor.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ClaseServiceTest.registrar_horarioCruzadoEnLaMismaSala_debeLanzarExcepcion`

### Precondiciones

- Existe una clase en la sala A de 18:00 a 19:00.

### Datos de prueba

- Sala: A
- Horario nuevo: 18:30–19:30

### Pasos

1. Llamar a ClaseService.registrarClase.

### Resultado esperado

Mensaje "Ya existe una clase en esa sala o con ese instructor en el horario indicado".

<!-- test
priority: high
tags: sprint2, unitaria, HU-04
-->

## CP24 - Registrar clase con datos correctos

**Historia de usuario:** HU-04 Gestión de clases  
**Criterio de aceptación:** CA1: Permite registrar una clase.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ClaseServiceTest.registrar_datosCorrectos_debeGuardarConTodosLosCupos`

### Precondiciones

- Diego Ramírez (1101) dicta MMA y está activo.

### Datos de prueba

- MMA, Diego, sala A, 18:00–19:00, aforo 12

### Pasos

1. Llamar a ClaseService.registrarClase.

### Resultado esperado

La clase se guarda con 12 cupos disponibles.

<!-- test
priority: high
tags: sprint2, unitaria, HU-05
-->

## CP25 - Reservar una clase sin cupos

**Historia de usuario:** HU-05 Reserva de clases  
**Criterio de aceptación:** CA5: Si no hay cupos, rechaza la reserva.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ReservaServiceTest.reservar_claseSinCupos_debeLanzarExcepcion`

### Precondiciones

- La clase 1004 tiene 0 cupos.

### Datos de prueba

- Cliente: 1001
- Clase: 1004

### Pasos

1. Llamar a ReservaService.registrarReserva.

### Resultado esperado

Mensaje "La clase no tiene cupos disponibles."

<!-- test
priority: high
tags: sprint2, unitaria, HU-05
-->

## CP26 - Reservar la misma clase dos veces

**Historia de usuario:** HU-05 Reserva de clases  
**Criterio de aceptación:** CA2: Verifica la reserva (sin duplicados).  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ReservaServiceTest.reservar_mismaClaseDosVeces_debeLanzarExcepcion`

### Precondiciones

- El cliente 1001 ya reservó la clase 1002.

### Datos de prueba

- Cliente: 1001
- Clase: 1002

### Pasos

1. Llamar a ReservaService.registrarReserva.

### Resultado esperado

Mensaje "El cliente ya tiene una reserva para esta clase."

<!-- test
priority: high
tags: sprint2, unitaria, HU-05
-->

## CP27 - Reservar una clase que ya empezó

**Historia de usuario:** HU-05 Reserva de clases  
**Criterio de aceptación:** CA6: No permite reservar una clase que ya inició.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ReservaServiceTest.reservar_claseQueYaEmpezo_debeLanzarExcepcion`

### Precondiciones

- La clase 1015 empezó hace 30 minutos.

### Datos de prueba

- Cliente: 1001
- Clase: 1015

### Pasos

1. Llamar a ReservaService.registrarReserva.

### Resultado esperado

Mensaje "La clase ya empezó: no se puede reservar."

<!-- test
priority: high
tags: sprint2, unitaria, HU-05
-->

## CP28 - Reservar una clase con cupo

**Historia de usuario:** HU-05 Reserva de clases  
**Criterio de aceptación:** CA3 y CA4: Registra la reserva y el aforo disminuye en uno.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `ReservaServiceTest.reservar_conCupo_debeRegistrarYDisminuirElAforo`

### Precondiciones

- La clase 1009 tiene 10 cupos libres.

### Datos de prueba

- Cliente: 1001
- Clase: 1009

### Pasos

1. Llamar a ReservaService.registrarReserva.

### Resultado esperado

Reserva ACTIVA con fecha de hoy; los cupos bajan de 10 a 9.

<!-- suite
tags: sprint2
-->

# Sprint 2 - Gestión de clases y reservas (escenarios BDD)

<!-- test
priority: high
tags: sprint2, bdd, HU-05
-->

## E06 - El cliente reserva una clase con cupos

**Historia de usuario:** HU-05 Reserva de clases  
**Criterio de aceptación:** CA1, CA3 y CA4: Reserva una clase con cupo.  
**Prioridad:** Alta  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU04_HU05_clases_reservas.feature`

### Precondiciones

- Cliente nuevo creado para la prueba.

### Datos de prueba

- Primera clase con cupos disponibles

### Pasos

1. Iniciar sesión.
2. Abrir /clases.
3. Presionar SELECCIONAR.
4. Presionar CONFIRMAR RESERVA.

### Resultado esperado

Se muestra "¡Reserva realizada correctamente!"

<!-- test
priority: high
tags: sprint2, bdd, HU-05
-->

## E07 - Una clase llena no se puede reservar

**Historia de usuario:** HU-05 Reserva de clases  
**Criterio de aceptación:** CA5: Si no hay cupos, rechaza la reserva.  
**Prioridad:** Alta  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU04_HU05_clases_reservas.feature`

### Precondiciones

- La clase 1004 está llena.

### Datos de prueba

- Cliente: maria.flores@example.com

### Pasos

1. Iniciar sesión.
2. Abrir /clases.

### Resultado esperado

Las clases llenas muestran "SIN CUPOS" con el botón deshabilitado.

