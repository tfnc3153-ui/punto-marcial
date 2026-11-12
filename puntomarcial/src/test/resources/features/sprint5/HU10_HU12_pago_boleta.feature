# language: es
@selenium
Característica: HU-10 Pago simulado, HU-11 Actualización de stock y HU-12 Comprobante
  Como cliente quiero pagar mi compra con tarjeta
  para confirmarla y obtener mi boleta

  Antecedentes:
    Dado que inicié sesión como un cliente nuevo
    Y agregué 1 unidad de "Vendas de algodón" al carrito
    Y continué al pago

  Escenario: HU-10 CA3, HU-11 CA1 y HU-12 - Con una tarjeta válida la compra se realiza
    Cuando pago con la tarjeta "4111111111111111", vencimiento "1229" y CVV "123"
    Entonces veo el mensaje del pago "Pago aprobado. Tu compra fue registrada."
    Y veo el botón "DESCARGAR BOLETA (PDF)"

  Escenario: HU-10 CA2 y CA4 - Con una tarjeta de 15 dígitos no se realiza la compra
    Cuando pago con la tarjeta "411111111111111", vencimiento "1229" y CVV "123"
    Entonces veo el error de la tarjeta "El número de tarjeta debe tener 16 dígitos."