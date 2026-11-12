# language: es
@selenium
Característica: HU-06 Cancelación de reservas y HU-07 Supervisión del aforo
  Como cliente quiero cancelar una reserva para liberar mi cupo,
  y como administrador quiero supervisar el aforo de las clases

  Escenario: HU-06 CA3 y CA4 - El cliente cancela una reserva con más de 2 horas de anticipación
    Dado que soy un cliente nuevo con una reserva en la clase 1009
    Y que inicié sesión con ese cliente
    Cuando abro la pantalla "/mis-reservas"
    Y cancelo la reserva y acepto la confirmación
    Entonces veo el mensaje de que esa reserva fue cancelada
    Y ya no veo reservas para cancelar

  Escenario: HU-07 CA1 y CA2 - El administrador supervisa el aforo de las clases
    Dado que inicié sesión con el correo "admin@puntomarcial.com" y la contraseña "Admin123"
    Cuando abro la pantalla "/supervisar-aforo"
    Entonces veo el título "CLASES PROGRAMADAS"
    Y cada clase muestra su barra de ocupación