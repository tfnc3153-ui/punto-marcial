# language: es
@selenium
Característica: HU-05 Reserva de clases por los clientes
  Como cliente de la academia
  quiero reservar una clase
  para asegurar mi cupo

  Escenario: HU-05 CA1, CA3 y CA4 - El cliente reserva una clase con cupos
    Dado que inicié sesión como un cliente nuevo
    Cuando abro la pantalla "/clases"
    Y selecciono la primera clase con cupos disponibles
    Y confirmo la reserva
    Entonces veo el mensaje de reserva "¡Reserva realizada correctamente!"

  Escenario: HU-05 CA5 - Una clase llena no se puede reservar
    Dado que inicié sesión con el correo "maria.flores@example.com" y la contraseña "Cliente123"
    Cuando abro la pantalla "/clases"
    Entonces las clases llenas muestran "SIN CUPOS" y no se pueden elegir