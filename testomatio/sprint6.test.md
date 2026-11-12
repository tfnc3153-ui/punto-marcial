<!-- suite
tags: sprint6
-->

# Sprint 6 - Roles, módulo del instructor y mejoras (pruebas unitarias)

<!-- test
priority: normal
tags: sprint6, unitaria, HU-13
-->

## CP56 - Marcar asistencia con un valor inválido

**Historia de usuario:** HU-13 Módulo del instructor  
**Criterio de aceptación:** CA4: Solo se guarda Asistió o No asistió.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `AsistenciaInstructorTest.marcarAsistencia_valorInvalido_debeLanzarExcepcion`

### Datos de prueba

- Asistencia: TARDE

### Pasos

1. Llamar a InstructorService.marcarAsistencia.

### Resultado esperado

Mensaje "La asistencia debe ser ASISTIO o NO_ASISTIO".

<!-- test
priority: high
tags: sprint6, unitaria, HU-13
-->

## CP57 - Marcar asistencia antes de que empiece la clase

**Historia de usuario:** HU-13 Módulo del instructor  
**Criterio de aceptación:** CA3: Solo permite registrar la asistencia cuando la clase ya inició.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `AsistenciaInstructorTest.marcarAsistencia_claseQueAunNoEmpieza_debeLanzarExcepcion`

### Precondiciones

- La clase empieza dentro de 1 hora.

### Datos de prueba

- Instructor: 1101
- Reserva: 2001
- Asistencia: ASISTIO

### Pasos

1. Llamar a InstructorService.marcarAsistencia.

### Resultado esperado

Mensaje "La asistencia se toma cuando la clase ya empezó".

<!-- test
priority: high
tags: sprint6, unitaria, HU-13
-->

## CP58 - Marcar asistencia en la clase de otro instructor

**Historia de usuario:** HU-13 Módulo del instructor  
**Criterio de aceptación:** CA1: El instructor ve solo sus clases asignadas.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `AsistenciaInstructorTest.marcarAsistencia_claseDeOtroInstructor_debeLanzarExcepcion`

### Precondiciones

- La clase 1015 es de Diego (1101).

### Datos de prueba

- Instructor: 1102 (Carla)
- Reserva: 1017

### Pasos

1. Llamar a InstructorService.marcarAsistencia.

### Resultado esperado

Mensaje "Esta clase no está asignada a usted".

<!-- test
priority: normal
tags: sprint6, unitaria, HU-13
-->

## CP59 - Marcar asistencia de una reserva cancelada

**Historia de usuario:** HU-13 Módulo del instructor  
**Criterio de aceptación:** CA2: Solo alumnos con reserva confirmada.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `AsistenciaInstructorTest.marcarAsistencia_reservaCancelada_debeLanzarExcepcion`

### Precondiciones

- La reserva 1018 está CANCELADA y la clase ya empezó.

### Datos de prueba

- Instructor: 1101
- Reserva: 1018

### Pasos

1. Llamar a InstructorService.marcarAsistencia.

### Resultado esperado

Mensaje "No se puede tomar asistencia de una reserva cancelada".

<!-- test
priority: high
tags: sprint6, unitaria, HU-13
-->

## CP60 - Marcar asistencia en una clase en curso

**Historia de usuario:** HU-13 Módulo del instructor  
**Criterio de aceptación:** CA4: La asistencia queda guardada.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `AsistenciaInstructorTest.marcarAsistencia_claseEnCurso_debeGuardarLaAsistencia`

### Precondiciones

- La clase 1015 de Diego empezó hace 30 minutos.

### Datos de prueba

- Instructor: 1101
- Reserva: 1017
- Asistencia: ASISTIO

### Pasos

1. Llamar a InstructorService.marcarAsistencia.

### Resultado esperado

La reserva queda con asistencia ASISTIO y se guarda en la base de datos.

<!-- test
priority: high
tags: sprint6, unitaria, HU-04
-->

## CP61 - Eliminar una clase con reservas

**Historia de usuario:** HU-04 Gestión de clases  
**Criterio de aceptación:** CA5: Solo se elimina una clase sin reservas.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `EliminarClaseTest.eliminar_claseConReservas_debeLanzarExcepcion`

### Precondiciones

- La clase 1004 tiene 3 reservas.

### Datos de prueba

- Clase: 1004

### Pasos

1. Llamar a ClaseService.eliminarClase.

### Resultado esperado

Mensaje "No se puede eliminar la clase porque tiene 3 reserva(s) registrada(s)" y no se borra.

<!-- test
priority: normal
tags: sprint6, unitaria, HU-04
-->

## CP62 - Eliminar una clase sin reservas

**Historia de usuario:** HU-04 Gestión de clases  
**Criterio de aceptación:** CA5: Permite eliminar una clase sin reservas.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `EliminarClaseTest.eliminar_claseSinReservas_debeEliminarla`

### Precondiciones

- La clase 1013 no tiene reservas.

### Datos de prueba

- Clase: 1013

### Pasos

1. Llamar a ClaseService.eliminarClase.

### Resultado esperado

La clase se elimina.

<!-- test
priority: high
tags: sprint6, unitaria, HU-03
-->

## CP63 - Desactivar instructor con clases futuras

**Historia de usuario:** HU-03 Actualización de usuarios  
**Criterio de aceptación:** CA5: No desactiva a un instructor con clases futuras.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `EstadoUsuarioTest.desactivar_instructorConClasesFuturas_debeLanzarExcepcion`

### Precondiciones

- Diego (1101) tiene 3 clases futuras.

### Datos de prueba

- Usuario: 1101
- Estado nuevo: INACTIVO

### Pasos

1. Llamar a UsuarioService.cambiarEstado.

### Resultado esperado

Mensaje "No se puede desactivar: el instructor tiene clases futuras asignadas".

<!-- test
priority: high
tags: sprint6, unitaria, HU-03
-->

## CP64 - Desactivar al último administrador

**Historia de usuario:** HU-03 Actualización de usuarios  
**Criterio de aceptación:** CA5: No desactiva al último administrador activo.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `EstadoUsuarioTest.desactivar_ultimoAdministrador_debeLanzarExcepcion`

### Precondiciones

- Solo hay 1 administrador activo.

### Datos de prueba

- Usuario: 1
- Estado nuevo: INACTIVO

### Pasos

1. Llamar a UsuarioService.cambiarEstado.

### Resultado esperado

Mensaje "Debe quedar al menos un administrador activo".

<!-- test
priority: low
tags: sprint6, unitaria, HU-03
-->

## CP65 - Cambiar a un estado inválido

**Historia de usuario:** HU-03 Actualización de usuarios  
**Criterio de aceptación:** CA5: El estado solo puede ser ACTIVO o INACTIVO.  
**Prioridad:** Baja  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `EstadoUsuarioTest.cambiarEstado_valorInvalido_debeLanzarExcepcion`

### Datos de prueba

- Estado nuevo: BLOQUEADO

### Pasos

1. Llamar a UsuarioService.cambiarEstado.

### Resultado esperado

Mensaje "El estado debe ser ACTIVO o INACTIVO".

<!-- suite
tags: sprint6
-->

# Sprint 6 - Roles, módulo del instructor y mejoras (escenarios BDD)

<!-- test
priority: high
tags: sprint6, bdd, HU-13
-->

## E15 - El instructor registra la asistencia

**Historia de usuario:** HU-13 Módulo del instructor  
**Criterio de aceptación:** CA2, CA3 y CA4: Registra la asistencia en una clase en curso.  
**Prioridad:** Alta  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU13_HU04_instructor_clases.feature`

### Precondiciones

- La clase 1015 de Diego está en curso.

### Datos de prueba

- Instructor: diego.ramirez@puntomarcial.com

### Pasos

1. Iniciar sesión.
2. Abrir /instructor/clases/1015.
3. Marcar la asistencia del primer alumno.

### Resultado esperado

Se muestra el mensaje de asistencia guardada (por ejemplo "María Flores: asistió.").

<!-- test
priority: high
tags: sprint6, bdd, HU-04
-->

## E16 - Una clase con reservas no se puede eliminar

**Historia de usuario:** HU-04 Gestión de clases  
**Criterio de aceptación:** CA5: Una clase con reservas no se puede eliminar.  
**Prioridad:** Alta  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU13_HU04_instructor_clases.feature`

### Precondiciones

- La clase 1004 tiene 3 reservas.

### Datos de prueba

- Clase: 1004

### Pasos

1. Iniciar sesión como administrador.
2. Abrir /gestion-clases.

### Resultado esperado

Se muestra "Tiene 3 reserva(s): no se puede eliminar." y el botón ELIMINAR está deshabilitado.

<!-- test
priority: normal
tags: sprint6, bdd, HU-04
-->

## E17 - El administrador elimina una clase sin reservas

**Historia de usuario:** HU-04 Gestión de clases  
**Criterio de aceptación:** CA5: Permite eliminar una clase sin reservas.  
**Prioridad:** Media  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU13_HU04_instructor_clases.feature`

### Precondiciones

- Existe una clase de Box sin reservas (creada para la prueba).

### Datos de prueba

- Clase de Box en fecha lejana

### Pasos

1. Iniciar sesión como administrador.
2. Abrir /gestion-clases.
3. Presionar ELIMINAR y aceptar la confirmación.

### Resultado esperado

Se muestra "Se eliminó la clase de Box del ... en la sala ..."

