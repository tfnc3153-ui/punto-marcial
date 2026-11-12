# language: es
@selenium
Característica: HU-01 Inicio de sesión y HU-02 Registro de usuarios
  Como usuario de la Academia Punto Marcial
  quiero iniciar sesión con mi rol
  para acceder a las funciones que me corresponden

  Esquema del escenario: HU-01 CA2 y CA5 - Cada rol entra a su propia pantalla
    Dado que estoy en la pantalla de inicio de sesión
    Cuando ingreso el correo "<correo>" y la contraseña "<clave>"
    Entonces el sistema me lleva a la pantalla "<pantalla>"

    Ejemplos:
      | rol           | correo                         | clave         | pantalla       |
      | Administrador | admin@puntomarcial.com         | Admin123      | /administrador |
      | Instructor    | diego.ramirez@puntomarcial.com | Instructor123 | /instructor    |
      | Cliente       | maria.flores@example.com       | Cliente123    | /menu          |

  Escenario: HU-01 CA3 - Con una contraseña incorrecta se muestra un error
    Dado que estoy en la pantalla de inicio de sesión
    Cuando ingreso el correo "admin@puntomarcial.com" y la contraseña "ClaveEquivocada1"
    Entonces veo el mensaje de error "Credenciales incorrectas"

  Escenario: HU-02 CA1, CA4 y CA5 - El administrador registra un cliente
    Dado que inicié sesión con el correo "admin@puntomarcial.com" y la contraseña "Admin123"
    Cuando registro un cliente nuevo llamado "Prueba" "Selenium"
    Entonces veo el mensaje de registro "Cliente registrado correctamente: Prueba Selenium."