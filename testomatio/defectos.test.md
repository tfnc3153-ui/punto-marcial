<!-- suite
id: @Sefce7883
tags: defectos
-->

# Registro de defectos

<!-- test
id: @T254fd4ba
priority: high
tags: defecto, abierto, severidad-alta
-->

## DEF-01 - La API no exige autenticación

**Severidad:** Alta  
**Requisito / historia afectada:** RNF-04 Seguridad  
**Cómo se detectó:** Revisión de seguridad  
**Estado:** Abierto  
**Pruebas que lo verifican:** —

### Pasos para reproducir (antes de la corrección)

1. Sin iniciar sesión, abrir en el navegador http://localhost:8081/usuarios

### Resultado obtenido

La API responde con la lista de usuarios sin pedir credenciales.

### Resultado esperado

La API debe pedir una sesión válida (token) antes de responder.

### Corrección aplicada

Pendiente: agregar autenticación con token (JWT) a los endpoints. Las pantallas sí están protegidas por rol.

### Verificación (re-test)

1. Sin iniciar sesión, abrir http://localhost:8081/usuarios
2. Observar que la lista de usuarios se muestra (el defecto sigue presente).

<!-- test
id: @Teb394690
priority: high
tags: defecto, cerrado, severidad-alta
-->

## DEF-02 - Las respuestas del servidor incluían el hash de la contraseña

**Severidad:** Alta  
**Requisito / historia afectada:** RNF-04 Seguridad  
**Cómo se detectó:** Revisión de las respuestas de la API  
**Estado:** Cerrado  
**Pruebas que lo verifican:** —

### Pasos para reproducir (antes de la corrección)

1. Consultar http://localhost:8081/reservas o el login
2. Revisar los datos del usuario en la respuesta

### Resultado obtenido

La respuesta mostraba el campo password con el hash BCrypt.

### Resultado esperado

La contraseña nunca debe salir del servidor.

### Corrección aplicada

El campo password se marcó como solo escritura (@JsonProperty WRITE_ONLY) y la API de usuarios responde con un DTO sin contraseña.

### Verificación (re-test)

1. Abrir http://localhost:8081/usuarios y http://localhost:8081/reservas
2. Buscar el texto "password" (Ctrl + F): no debe aparecer.

<!-- test
id: @T1182319b
priority: high
tags: defecto, cerrado, severidad-alta
-->

## DEF-03 - Ruta duplicada y pantallas accesibles sin el rol correspondiente

**Severidad:** Alta  
**Requisito / historia afectada:** RF-01 Autenticación  
**Cómo se detectó:** Revisión de las rutas del frontend  
**Estado:** Cerrado  
**Pruebas que lo verifican:** E01–E03 (cada rol entra solo a su pantalla)

### Pasos para reproducir (antes de la corrección)

1. Sin iniciar sesión, escribir en el navegador http://localhost:4200/gestion-clases

### Resultado obtenido

La pantalla se abría sin sesión o con otro rol.

### Resultado esperado

Solo el rol correspondiente puede abrir cada pantalla.

### Corrección aplicada

Guard por rol (roleGuard) en todas las rutas: administrador, instructor y cliente.

### Verificación (re-test)

1. Sin iniciar sesión, abrir http://localhost:4200/gestion-clases → vuelve al login.
2. Iniciar sesión como cliente y abrir http://localhost:4200/gestion-clases → vuelve al login.

<!-- test
id: @Tbee1097a
priority: normal
tags: defecto, cerrado, severidad-media
-->

## DEF-04 - El navegador autocompletaba las credenciales del administrador en el registro

**Severidad:** Media  
**Requisito / historia afectada:** HU-02 Registro de usuarios  
**Cómo se detectó:** Prueba manual  
**Estado:** Cerrado  
**Pruebas que lo verifican:** E05 (registro de un cliente)

### Pasos para reproducir (antes de la corrección)

1. Iniciar sesión como administrador y guardar la contraseña en el navegador
2. Abrir Registrar usuario

### Resultado obtenido

Los campos de correo y contraseña aparecían llenos con los datos del administrador.

### Resultado esperado

El formulario de registro debe aparecer vacío.

### Corrección aplicada

Se desactivó el autocompletado (autocomplete="off" y "new-password") en los campos del formulario.

### Verificación (re-test)

1. Iniciar sesión como administrador y abrir Registrar usuario.
2. Comprobar que correo y contraseña están vacíos.

<!-- test
id: @T2dbbbc19
priority: normal
tags: defecto, cerrado, severidad-media
-->

## DEF-05 - Contraste de colores insuficiente (no cumplía WCAG 2.1 AA)

**Severidad:** Media  
**Requisito / historia afectada:** RNF-02 Usabilidad y accesibilidad  
**Cómo se detectó:** axe DevTools / Lighthouse  
**Estado:** Cerrado  
**Pruebas que lo verifican:** —

### Pasos para reproducir (antes de la corrección)

1. Ejecutar Lighthouse (Accesibilidad) en el login y en el menú

### Resultado obtenido

Lighthouse y axe reportaban textos con contraste bajo.

### Resultado esperado

Contraste mínimo de 4.5:1 en textos (WCAG 2.1 AA).

### Corrección aplicada

Se ajustó la paleta de colores (variables CSS) para cumplir el contraste mínimo.

### Verificación (re-test)

1. Abrir http://localhost:4200/login
2. F12 → Lighthouse → marcar Accessibility → Analyze.
3. Comprobar que no hay errores de contraste.

<!-- test
id: @T9d4aedfe
priority: high
tags: defecto, cerrado, severidad-alta
-->

## DEF-06 - Eliminar una clase con reservas producía un error 500

**Severidad:** Alta  
**Requisito / historia afectada:** HU-04 Gestión de clases  
**Cómo se detectó:** Prueba manual  
**Estado:** Cerrado  
**Pruebas que lo verifican:** CP61, CP62, E16, E17

### Pasos para reproducir (antes de la corrección)

1. Como administrador, intentar eliminar una clase que tiene reservas

### Resultado obtenido

El servidor respondía error 500 y no se mostraba un mensaje claro.

### Resultado esperado

No se debe poder eliminar una clase con reservas y se debe informar el motivo.

### Corrección aplicada

Regla puedeEliminarClase en el servicio, respuesta 409 con mensaje y botón ELIMINAR deshabilitado en la pantalla.

### Verificación (re-test)

1. Como administrador, abrir Gestión de clases.
2. La clase 1004 muestra "Tiene 3 reserva(s): no se puede eliminar." y el botón ELIMINAR está deshabilitado.

<!-- test
id: @T9421736b
priority: normal
tags: defecto, cerrado, severidad-media
-->

## DEF-07 - Algunas pantallas se quedaban en "Cargando…"

**Severidad:** Media  
**Requisito / historia afectada:** RNF-02 Usabilidad  
**Cómo se detectó:** Prueba manual  
**Estado:** Cerrado  
**Pruebas que lo verifican:** E06, E09, E11 (las pantallas cargan sus datos)

### Pasos para reproducir (antes de la corrección)

1. Abrir Clases o Actualizar usuario

### Resultado obtenido

La pantalla se quedaba en "Cargando…" aunque el servidor ya había respondido.

### Resultado esperado

La pantalla debe mostrar los datos al recibir la respuesta.

### Corrección aplicada

Se actualiza la vista después de cada respuesta del servidor (detectChanges).

### Verificación (re-test)

1. Abrir Clases, Mis reservas, Comprar productos y Actualizar usuario.
2. Comprobar que todas cargan sus datos.

<!-- test
id: @T8cfc4a78
priority: normal
tags: defecto, cerrado, severidad-media
-->

## DEF-08 - Los mensajes de error no aparecían en pantalla (incluido el login)

**Severidad:** Media  
**Requisito / historia afectada:** HU-01 Inicio de sesión (CA3)  
**Cómo se detectó:** Prueba manual  
**Estado:** Cerrado  
**Pruebas que lo verifican:** E04

### Pasos para reproducir (antes de la corrección)

1. Iniciar sesión con una contraseña incorrecta

### Resultado obtenido

No se mostraba ningún mensaje.

### Resultado esperado

Debe mostrarse "Credenciales incorrectas".

### Corrección aplicada

Cada pantalla muestra el mensaje que envía el servidor.

### Verificación (re-test)

1. Iniciar sesión con admin@puntomarcial.com y la contraseña ClaveEquivocada1.
2. Debe aparecer "Credenciales incorrectas".

<!-- test
id: @Tc5e91211
priority: high
tags: defecto, cerrado, severidad-critica
-->

## DEF-09 - MariaDB rechazaba el bloqueo FOR UPDATE OF y fallaban reservas y ventas

**Severidad:** Crítica  
**Requisito / historia afectada:** RNF-03 Fiabilidad  
**Cómo se detectó:** Prueba de integración  
**Estado:** Cerrado  
**Pruebas que lo verifican:** CP28, CP49, E06, E13

### Pasos para reproducir (antes de la corrección)

1. Reservar una clase o comprar un producto

### Resultado obtenido

Error de SQL: MariaDB no acepta "FOR UPDATE OF" y la operación fallaba.

### Resultado esperado

La reserva o la venta se registra y bloquea el registro para evitar sobrecupos.

### Corrección aplicada

Consulta nativa SELECT … FOR UPDATE compatible con MariaDB.

### Verificación (re-test)

1. Como cliente, reservar una clase con cupos y comprar un producto.
2. Ambas operaciones se registran sin error.

<!-- test
id: @Tdcf0b99e
priority: high
tags: defecto, cerrado, severidad-alta
-->

## DEF-10 - Se podía reservar una clase que ya había empezado

**Severidad:** Alta  
**Requisito / historia afectada:** HU-05 Reserva de clases (CA6)  
**Cómo se detectó:** Prueba manual  
**Estado:** Cerrado  
**Pruebas que lo verifican:** CP27

### Pasos para reproducir (antes de la corrección)

1. Como cliente, reservar una clase cuya hora de inicio ya pasó

### Resultado obtenido

La reserva se registraba.

### Resultado esperado

No se debe poder reservar una clase que ya empezó.

### Corrección aplicada

Regla claseYaInicio en el servicio de reservas y la lista de clases solo muestra las que no han empezado.

### Verificación (re-test)

1. Como cliente, abrir Clases: la clase 1015 (en curso) no aparece en la lista.

<!-- test
id: @T04ba05c7
priority: low
tags: defecto, cerrado, severidad-baja
-->

## DEF-11 - Los calendarios eran ilegibles en el tema oscuro

**Severidad:** Baja  
**Requisito / historia afectada:** RNF-02 Usabilidad y accesibilidad  
**Cómo se detectó:** Revisión de accesibilidad  
**Estado:** Cerrado  
**Pruebas que lo verifican:** —

### Pasos para reproducir (antes de la corrección)

1. Abrir el selector de fecha en Gestión de clases o Mis compras

### Resultado obtenido

El calendario se mostraba con colores que no se leían.

### Resultado esperado

El calendario debe leerse con claridad.

### Corrección aplicada

Se fijó el esquema de colores de los campos de fecha (color-scheme).

### Verificación (re-test)

1. Abrir Gestión de clases y desplegar el calendario de la fecha.
2. Comprobar que se lee con claridad.

<!-- test
id: @Tac318103
priority: normal
tags: defecto, cerrado, severidad-media
-->

## DEF-12 - Una reserva enviada sin fecha producía un error 500

**Severidad:** Media  
**Requisito / historia afectada:** HU-05 Reserva de clases  
**Cómo se detectó:** Prueba automatizada (Selenium, al preparar datos por la API)  
**Estado:** Cerrado  
**Pruebas que lo verifican:** CP28, E08

### Pasos para reproducir (antes de la corrección)

1. Enviar a POST /reservas una reserva sin el campo fechaReserva

### Resultado obtenido

El servidor respondía error 500.

### Resultado esperado

La reserva se registra con la fecha actual.

### Corrección aplicada

El servicio asigna la fecha de hoy si no se envía.

### Verificación (re-test)

1. Ejecutar la prueba unitaria CP28 (ReservaServiceTest): verifica que la reserva queda con la fecha de hoy.

