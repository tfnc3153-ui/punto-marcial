# language: es
@selenium
Característica: HU-13 Módulo del instructor y HU-04 Eliminación de clases
  Como instructor quiero registrar la asistencia de mis alumnos,
  y como administrador quiero eliminar solo las clases que no tienen reservas

  Escenario: HU-13 CA2, CA3 y CA4 - El instructor registra la asistencia en una clase en curso
    Dado que inicié sesión con el correo "diego.ramirez@puntomarcial.com" y la contraseña "Instructor123"
    Cuando abro la pantalla "/instructor/clases/1015"
    Y marco la asistencia del primer alumno que no la tenga marcada
    Entonces veo el mensaje de asistencia guardada

  Escenario: HU-04 CA5 - Una clase con reservas no se puede eliminar
    Dado que inicié sesión con el correo "admin@puntomarcial.com" y la contraseña "Admin123"
    Cuando abro la pantalla "/gestion-clases"
    Entonces la clase 1004 muestra el aviso "Tiene 3 reserva(s): no se puede eliminar."
    Y su botón ELIMINAR está deshabilitado

  Escenario: HU-04 CA5 - El administrador elimina una clase sin reservas
    Dado que existe una clase de Box sin reservas
    Y que inicié sesión con el correo "admin@puntomarcial.com" y la contraseña "Admin123"
    Cuando abro la pantalla "/gestion-clases"
    Y elimino esa clase y acepto la confirmación
    Entonces veo el mensaje de que la clase se eliminó