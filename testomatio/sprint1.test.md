<!-- suite
tags: sprint1
-->

# Sprint 1 - Gestión de usuarios y acceso (pruebas unitarias)

<!-- test
priority: high
tags: sprint1, unitaria, HU-01
-->

## CP01 - Login con un correo que no está registrado

**Historia de usuario:** HU-01 Inicio de sesión  
**Criterio de aceptación:** CA3: Con credenciales incorrectas muestra un mensaje de error.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `AuthServiceTest.login_correoNoRegistrado_debeLanzarExcepcion`

### Precondiciones

- El correo no existe en la base de datos (repositorio simulado).

### Datos de prueba

- Correo: noexiste@example.com
- Contraseña: Cliente123

### Pasos

1. Llamar a AuthService.login con el correo y la contraseña.

### Resultado esperado

Se rechaza el ingreso con el mensaje "Credenciales incorrectas".

<!-- test
priority: high
tags: sprint1, unitaria, HU-01
-->

## CP02 - Login con una cuenta inactiva

**Historia de usuario:** HU-01 Inicio de sesión  
**Criterio de aceptación:** CA6: Un usuario desactivado no puede iniciar sesión.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `AuthServiceTest.login_cuentaInactiva_debeLanzarExcepcion`

### Precondiciones

- Existe un cliente con estado INACTIVO.

### Datos de prueba

- Correo: camila.chavez@example.com
- Estado: INACTIVO

### Pasos

1. Llamar a AuthService.login con ese correo.

### Resultado esperado

Se rechaza el ingreso con el mensaje "La cuenta está inactiva".

<!-- test
priority: high
tags: sprint1, unitaria, HU-01
-->

## CP03 - Login con contraseña incorrecta

**Historia de usuario:** HU-01 Inicio de sesión  
**Criterio de aceptación:** CA3: Con credenciales incorrectas muestra un mensaje de error.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `AuthServiceTest.login_contrasenaIncorrecta_debeLanzarExcepcion`

### Precondiciones

- Existe el cliente activo maria.flores@example.com.

### Datos de prueba

- Correo: maria.flores@example.com
- Contraseña: ClaveEquivocada1

### Pasos

1. Llamar a AuthService.login con la contraseña equivocada.

### Resultado esperado

Se rechaza el ingreso con el mensaje "Credenciales incorrectas".

<!-- test
priority: high
tags: sprint1, unitaria, HU-01
-->

## CP04 - Login correcto devuelve el rol del usuario

**Historia de usuario:** HU-01 Inicio de sesión  
**Criterio de aceptación:** CA2 y CA5: Con credenciales correctas permite el acceso según su rol.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `AuthServiceTest.login_credencialesCorrectas_debeDevolverRolCliente`

### Precondiciones

- Existe el cliente activo con id 1001.

### Datos de prueba

- Correo: maria.flores@example.com
- Contraseña: Cliente123

### Pasos

1. Llamar a AuthService.login con credenciales correctas.

### Resultado esperado

Devuelve el id 1001 y el rol CLIENTE.

<!-- test
priority: normal
tags: sprint1, unitaria, HU-02
-->

## CP05 - Registro con un rol que no existe

**Historia de usuario:** HU-02 Registro de usuarios  
**Criterio de aceptación:** CA1: Permite ingresar los datos y seleccionar el rol.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `UsuarioServiceTest.registrar_rolInvalido_debeLanzarExcepcion`

### Datos de prueba

- Rol: SECRETARIA
- Resto de datos válidos

### Pasos

1. Llamar a UsuarioService.registrarUsuario.

### Resultado esperado

Mensaje "El rol debe ser Administrador, Instructor o Cliente".

<!-- test
priority: normal
tags: sprint1, unitaria, HU-02
-->

## CP06 - Registro con el nombre vacío

**Historia de usuario:** HU-02 Registro de usuarios  
**Criterio de aceptación:** CA1: Permite ingresar los datos obligatorios.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `UsuarioServiceTest.registrar_nombreVacio_debeLanzarExcepcion`

### Datos de prueba

- Nombre: (vacío)
- Resto de datos válidos

### Pasos

1. Llamar a UsuarioService.registrarUsuario.

### Resultado esperado

Mensaje "Complete todos los campos obligatorios".

<!-- test
priority: high
tags: sprint1, unitaria, HU-02
-->

## CP07 - Registro con DNI de 7 dígitos

**Historia de usuario:** HU-02 Registro de usuarios  
**Criterio de aceptación:** CA2: Valida el DNI.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `UsuarioServiceTest.registrar_dniDe7Digitos_debeLanzarExcepcion`

### Datos de prueba

- DNI: 4578123
- Resto de datos válidos

### Pasos

1. Llamar a UsuarioService.registrarUsuario.

### Resultado esperado

Mensaje "El DNI debe tener exactamente 8 dígitos".

<!-- test
priority: normal
tags: sprint1, unitaria, HU-02
-->

## CP08 - Registro con un correo sin @

**Historia de usuario:** HU-02 Registro de usuarios  
**Criterio de aceptación:** CA1: Permite ingresar los datos obligatorios.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `UsuarioServiceTest.registrar_correoSinArroba_debeLanzarExcepcion`

### Datos de prueba

- Correo: maria.example.com
- Resto de datos válidos

### Pasos

1. Llamar a UsuarioService.registrarUsuario.

### Resultado esperado

Mensaje "El correo no es válido".

<!-- test
priority: normal
tags: sprint1, unitaria, HU-02
-->

## CP09 - Registro con teléfono de 8 dígitos

**Historia de usuario:** HU-02 Registro de usuarios  
**Criterio de aceptación:** CA1: Permite ingresar los datos obligatorios.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `UsuarioServiceTest.registrar_telefonoDe8Digitos_debeLanzarExcepcion`

### Datos de prueba

- Teléfono: 91058296
- Resto de datos válidos

### Pasos

1. Llamar a UsuarioService.registrarUsuario.

### Resultado esperado

Mensaje "El teléfono debe tener 9 dígitos".

<!-- test
priority: normal
tags: sprint1, unitaria, HU-02
-->

## CP10 - Registro con contraseña sin números

**Historia de usuario:** HU-02 Registro de usuarios  
**Criterio de aceptación:** CA4: Guarda al usuario con la contraseña encriptada.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `UsuarioServiceTest.registrar_contrasenaSinNumeros_debeLanzarExcepcion`

### Datos de prueba

- Contraseña: ClaveSegura
- Resto de datos válidos

### Pasos

1. Llamar a UsuarioService.registrarUsuario.

### Resultado esperado

Mensaje "La contraseña debe tener al menos 8 caracteres, con letras y números".

<!-- test
priority: high
tags: sprint1, unitaria, HU-02
-->

## CP11 - Registro con un DNI ya registrado

**Historia de usuario:** HU-02 Registro de usuarios  
**Criterio de aceptación:** CA2: Valida que el DNI no esté registrado previamente.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `UsuarioServiceTest.registrar_dniYaRegistrado_debeLanzarExcepcion`

### Precondiciones

- El DNI 45781236 ya existe (repositorio simulado).

### Datos de prueba

- DNI: 45781236
- Resto de datos válidos

### Pasos

1. Llamar a UsuarioService.registrarUsuario.

### Resultado esperado

Mensaje "El número de documento ya está registrado".

<!-- test
priority: high
tags: sprint1, unitaria, HU-02
-->

## CP12 - Registro con un correo ya registrado

**Historia de usuario:** HU-02 Registro de usuarios  
**Criterio de aceptación:** CA2: Valida que el correo no esté registrado previamente.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `UsuarioServiceTest.registrar_correoYaRegistrado_debeLanzarExcepcion`

### Precondiciones

- El correo maria.flores@example.com ya existe.

### Datos de prueba

- DNI: 45781236 (nuevo)
- Correo: maria.flores@example.com

### Pasos

1. Llamar a UsuarioService.registrarUsuario.

### Resultado esperado

Mensaje "El correo ya está registrado".

<!-- test
priority: normal
tags: sprint1, unitaria, HU-02
-->

## CP13 - Registro de instructor sin disciplina

**Historia de usuario:** HU-02 Registro de usuarios  
**Criterio de aceptación:** CA3: Solicita el dato propio del rol (disciplina del instructor).  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `UsuarioServiceTest.registrar_instructorSinDisciplina_debeLanzarExcepcion`

### Datos de prueba

- Rol: INSTRUCTOR
- Disciplina: (vacía)

### Pasos

1. Llamar a UsuarioService.registrarUsuario.

### Resultado esperado

Mensaje "El instructor debe tener una disciplina: MMA, Muay Thai o Box".

<!-- test
priority: high
tags: sprint1, unitaria, HU-02
-->

## CP14 - Registro correcto de un cliente

**Historia de usuario:** HU-02 Registro de usuarios  
**Criterio de aceptación:** CA4 y CA5: Guarda correctamente al usuario con la contraseña encriptada.  
**Prioridad:** Alta  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `UsuarioServiceTest.registrar_datosCorrectos_debeGuardarConContrasenaEncriptada`

### Datos de prueba

- Rol: CLIENTE
- Nombre: Maria Flores
- DNI: 45781236
- Correo: maria@example.com
- Teléfono: 910582963
- Contraseña: Cliente123

### Pasos

1. Llamar a UsuarioService.registrarUsuario.

### Resultado esperado

Se guarda una vez, con rol CLIENTE, estado ACTIVO y la contraseña encriptada.

<!-- test
priority: normal
tags: sprint1, unitaria, HU-03
-->

## CP15 - Actualizar un usuario que no existe

**Historia de usuario:** HU-03 Actualización de usuarios  
**Criterio de aceptación:** CA1: Permite buscar un usuario registrado.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `UsuarioServiceTest.actualizar_usuarioQueNoExiste_debeLanzarExcepcion`

### Precondiciones

- No existe el usuario 9999.

### Datos de prueba

- Id: 9999

### Pasos

1. Llamar a UsuarioService.actualizarUsuario.

### Resultado esperado

Mensaje "El usuario no existe".

<!-- test
priority: normal
tags: sprint1, unitaria, HU-03
-->

## CP16 - Actualizar con el correo de otro usuario

**Historia de usuario:** HU-03 Actualización de usuarios  
**Criterio de aceptación:** CA3: Valida los campos al actualizar.  
**Prioridad:** Media  
**Tipo:** Prueba unitaria automatizada (JUnit 5 + Mockito)  
**Prueba automatizada:** `UsuarioServiceTest.actualizar_correoDeOtroUsuario_debeLanzarExcepcion`

### Precondiciones

- El correo carlos.mendoza@example.com pertenece a otro usuario.

### Datos de prueba

- Id: 1001
- Correo nuevo: carlos.mendoza@example.com

### Pasos

1. Llamar a UsuarioService.actualizarUsuario.

### Resultado esperado

Mensaje "El correo ya está en uso por otro usuario".

<!-- suite
tags: sprint1
-->

# Sprint 1 - Gestión de usuarios y acceso (escenarios BDD)

<!-- test
priority: high
tags: sprint1, bdd, HU-01
-->

## E01 - El administrador entra a su panel

**Historia de usuario:** HU-01 Inicio de sesión  
**Criterio de aceptación:** CA2 y CA5: Con credenciales correctas entra a su pantalla.  
**Prioridad:** Alta  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU01_HU02_acceso_usuarios.feature`

### Precondiciones

- Backend, frontend y MySQL encendidos.

### Datos de prueba

- Correo: admin@puntomarcial.com
- Contraseña: Admin123

### Pasos

1. Ir a la pantalla de inicio de sesión.
2. Ingresar correo y contraseña.
3. Presionar INGRESAR.

### Resultado esperado

El sistema lleva a /administrador.

<!-- test
priority: high
tags: sprint1, bdd, HU-01
-->

## E02 - El instructor entra a su panel

**Historia de usuario:** HU-01 Inicio de sesión  
**Criterio de aceptación:** CA2 y CA5: Con credenciales correctas entra a su pantalla.  
**Prioridad:** Alta  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU01_HU02_acceso_usuarios.feature`

### Precondiciones

- Backend, frontend y MySQL encendidos.

### Datos de prueba

- Correo: diego.ramirez@puntomarcial.com
- Contraseña: Instructor123

### Pasos

1. Ir a la pantalla de inicio de sesión.
2. Ingresar correo y contraseña.
3. Presionar INGRESAR.

### Resultado esperado

El sistema lleva a /instructor.

<!-- test
priority: high
tags: sprint1, bdd, HU-01
-->

## E03 - El cliente entra a su menú

**Historia de usuario:** HU-01 Inicio de sesión  
**Criterio de aceptación:** CA2 y CA5: Con credenciales correctas entra a su pantalla.  
**Prioridad:** Alta  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU01_HU02_acceso_usuarios.feature`

### Precondiciones

- Backend, frontend y MySQL encendidos.

### Datos de prueba

- Correo: maria.flores@example.com
- Contraseña: Cliente123

### Pasos

1. Ir a la pantalla de inicio de sesión.
2. Ingresar correo y contraseña.
3. Presionar INGRESAR.

### Resultado esperado

El sistema lleva a /menu.

<!-- test
priority: high
tags: sprint1, bdd, HU-01
-->

## E04 - Contraseña incorrecta muestra un error

**Historia de usuario:** HU-01 Inicio de sesión  
**Criterio de aceptación:** CA3: Con credenciales incorrectas muestra un mensaje de error.  
**Prioridad:** Alta  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU01_HU02_acceso_usuarios.feature`

### Precondiciones

- Backend, frontend y MySQL encendidos.

### Datos de prueba

- Correo: admin@puntomarcial.com
- Contraseña: ClaveEquivocada1

### Pasos

1. Ir a la pantalla de inicio de sesión.
2. Ingresar correo y contraseña equivocada.
3. Presionar INGRESAR.

### Resultado esperado

Se muestra "Credenciales incorrectas" y sigue en el login.

<!-- test
priority: high
tags: sprint1, bdd, HU-02
-->

## E05 - El administrador registra un cliente

**Historia de usuario:** HU-02 Registro de usuarios  
**Criterio de aceptación:** CA1, CA4 y CA5: Registra al cliente y muestra confirmación.  
**Prioridad:** Alta  
**Tipo:** Prueba de aceptación BDD automatizada (Gherkin + Cucumber + Selenium WebDriver)  
**Archivo .feature:** `HU01_HU02_acceso_usuarios.feature`

### Precondiciones

- Sesión iniciada como administrador.

### Datos de prueba

- Nombre: Prueba Selenium
- DNI, correo y teléfono únicos
- Contraseña: Prueba123

### Pasos

1. Abrir /registrar-usuario.
2. Llenar el formulario (rol Cliente).
3. Presionar REGISTRAR USUARIO.

### Resultado esperado

Se muestra "Cliente registrado correctamente: Prueba Selenium."

