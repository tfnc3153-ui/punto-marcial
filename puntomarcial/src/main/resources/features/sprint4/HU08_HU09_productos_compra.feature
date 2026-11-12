# language: es
@selenium
Característica: HU-08 Gestión de productos y HU-09 Compra de equipamiento
  Como administrador quiero ver qué productos llegaron al stock mínimo,
  y como cliente quiero armar mi carrito de compras

  Escenario: HU-08 CA4 - El administrador ve la alerta de stock mínimo
    Dado que inicié sesión con el correo "admin@puntomarcial.com" y la contraseña "Admin123"
    Cuando abro la pantalla "/gestion-productos"
    Entonces veo la alerta "producto(s) con stock bajo (en el mínimo o por debajo)."
    Y al marcar "solo stock bajo" todos los productos de la lista tienen alerta

  Escenario: HU-09 CA1, CA3 y CA4 - El carrito calcula el total de la compra
    Dado que inicié sesión con el correo "maria.flores@example.com" y la contraseña "Cliente123"
    Cuando abro la pantalla "/comprar-productos"
    Y agrego 2 unidades de "Vendas de algodón" al carrito
    Entonces el total del carrito es "S/ 39.80"

  Escenario: HU-09 CA5 - Un producto agotado no se puede agregar al carrito
    Dado que inicié sesión con el correo "maria.flores@example.com" y la contraseña "Cliente123"
    Cuando abro la pantalla "/comprar-productos"
    Entonces el producto "Cuerda para saltar" muestra "SIN STOCK" y no se puede agregar