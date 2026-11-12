package com.academia.puntomarcial.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.academia.puntomarcial.dto.DatosPagoRequest;
import com.academia.puntomarcial.dto.ItemCarritoRequest;
import com.academia.puntomarcial.dto.ItemCarritoResponse;
import com.academia.puntomarcial.dto.VentaRequest;
import com.academia.puntomarcial.dto.VentaResponse;
import com.academia.puntomarcial.dto.VerificacionCarritoResponse;
import com.academia.puntomarcial.model.Cliente;
import com.academia.puntomarcial.model.DetalleVenta;
import com.academia.puntomarcial.model.Producto;
import com.academia.puntomarcial.model.Venta;
import com.academia.puntomarcial.repository.ClienteRepository;
import com.academia.puntomarcial.repository.ProductoRepository;
import com.academia.puntomarcial.repository.VentaRepository;

@Service
public class VentaService {

    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;

    public VentaService(
            ProductoRepository productoRepository,
            VentaRepository ventaRepository,
            ClienteRepository clienteRepository) {
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
    }

    /**
     * HU-09: revisa el carrito contra el stock actual y calcula subtotales y total.
     * No modifica nada; el descuento del stock se hace al pagar (HU-10 / HU-11).
     */
    public VerificacionCarritoResponse verificarCarrito(List<ItemCarritoRequest> carrito) {
        Map<Integer, Integer> cantidades = agruparCantidades(carrito);

        List<ItemCarritoResponse> items = new ArrayList<>();
        List<BigDecimal> subtotales = new ArrayList<>();
        List<String> productosSinStock = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entrada : cantidades.entrySet()) {
            Producto producto = productoRepository.findById(entrada.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Uno de los productos ya no existe."));

            if (!"ACTIVO".equals(producto.getEstado())) {
                throw new IllegalArgumentException(
                        "El producto " + producto.getNombre() + " ya no está a la venta.");
            }

            int cantidad = entrada.getValue();
            BigDecimal subtotal = ReglasVenta.calcularSubtotal(producto.getPrecio(), cantidad);
            boolean suficiente = ReglasVenta.stockSuficiente(producto.getStock(), cantidad);

            if (!suficiente) {
                productosSinStock.add(producto.getNombre() + " (talla " + producto.getTalla()
                        + "): pediste " + cantidad + ", hay " + producto.getStock());
            }

            subtotales.add(subtotal);
            items.add(new ItemCarritoResponse(
                    producto.getIdProducto(), producto.getNombre(), producto.getTalla(),
                    producto.getPrecio(), cantidad, subtotal, producto.getStock(), suficiente));
        }

        return new VerificacionCarritoResponse(
                items, ReglasVenta.calcularTotal(subtotales), productosSinStock.isEmpty(), productosSinStock);
    }

    /**
     * HU-10 y HU-11: registra la compra en una sola transacción.
     * - Bloquea los productos (FOR UPDATE) y vuelve a revisar el stock.
     * - Si el pago es válido: venta PAGADA y se descuenta el stock.
     * - Si el pago no es válido: venta RECHAZADA y el stock no cambia.
     */
    @Transactional
    public VentaResponse registrarVenta(VentaRequest request) {
        if (request == null || request.getIdCliente() == null) {
            throw new IllegalArgumentException("Debe indicar el cliente.");
        }
        if (request.getPago() == null) {
            throw new IllegalArgumentException("Debe ingresar los datos de pago.");
        }

        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe."));

        Map<Integer, Integer> cantidades = agruparCantidades(request.getItems());

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());

        List<BigDecimal> subtotales = new ArrayList<>();
        List<String> productosSinStock = new ArrayList<>();

        // Se bloquean en orden de id para que dos compras simultáneas no se esperen en cruz
        List<Integer> ids = new ArrayList<>(cantidades.keySet());
        ids.sort(null);

        for (Integer idProducto : ids) {
            Producto producto = productoRepository.buscarParaVenta(idProducto)
                    .orElseThrow(() -> new IllegalArgumentException("Uno de los productos ya no existe."));

            if (!"ACTIVO".equals(producto.getEstado())) {
                throw new IllegalArgumentException(
                        "El producto " + producto.getNombre() + " ya no está a la venta.");
            }

            int cantidad = cantidades.get(idProducto);

            if (!ReglasVenta.stockSuficiente(producto.getStock(), cantidad)) {
                productosSinStock.add(producto.getNombre() + " (talla " + producto.getTalla()
                        + "): pediste " + cantidad + ", hay " + producto.getStock());
                continue;
            }

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(ReglasVenta.calcularSubtotal(producto.getPrecio(), cantidad));

            venta.agregarDetalle(detalle);
            subtotales.add(detalle.getSubtotal());
        }

        // Sin stock suficiente no se registra nada (la transacción se revierte)
        if (!productosSinStock.isEmpty()) {
            throw new IllegalArgumentException(
                    "No hay stock suficiente para: " + String.join("; ", productosSinStock));
        }

        venta.setTotal(ReglasVenta.calcularTotal(subtotales));

        DatosPagoRequest pago = request.getPago();
        String numeroTarjeta = soloDigitos(pago.getNumeroTarjeta());
        String vencimiento = pago.getVencimiento() == null ? null : pago.getVencimiento().trim();
        String cvv = pago.getCvv() == null ? null : pago.getCvv().trim();

        if (!ReglasVenta.pagoValido(numeroTarjeta, vencimiento, cvv, LocalDate.now())) {
            venta.setEstado("RECHAZADA");
            ventaRepository.save(venta);

            return new VentaResponse(
                    venta, "Pago rechazado: " + motivoRechazo(numeroTarjeta, vencimiento, cvv), List.of());
        }

        // Pago válido: se descuenta el stock y se revisa si algún producto llegó al mínimo
        List<String> productosStockMinimo = new ArrayList<>();

        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(ReglasVenta.descontarStock(producto.getStock(), detalle.getCantidad()));
            productoRepository.save(producto);

            if (ReglasVenta.stockBajo(producto.getStock(), producto.getStockMinimo())) {
                productosStockMinimo.add(producto.getNombre() + " (talla " + producto.getTalla()
                        + "): quedan " + producto.getStock());
            }
        }

        venta.setEstado("PAGADA");
        venta.setUltimos4(numeroTarjeta.substring(12));
        ventaRepository.save(venta);

        return new VentaResponse(venta, "Pago aprobado. Tu compra fue registrada.", productosStockMinimo);
    }

    public Venta buscarConDetalle(Integer idVenta) {
        return ventaRepository.buscarConDetalle(idVenta)
                .orElseThrow(() -> new IllegalArgumentException("La venta no existe."));
    }

    /** HU-12: la boleta solo existe para ventas PAGADAS y solo la descarga el cliente dueño. */
    public Venta buscarVentaPagadaDelCliente(Integer idVenta, Integer idCliente) {
        Venta venta = buscarConDetalle(idVenta);

        if (idCliente == null || !idCliente.equals(venta.getCliente().getIdUsuario())) {
            throw new IllegalArgumentException("Solo el cliente dueño de la compra puede descargar la boleta.");
        }
        if (!"PAGADA".equals(venta.getEstado())) {
            throw new IllegalArgumentException("Solo las compras pagadas tienen boleta.");
        }

        return venta;
    }

    public List<Venta> listarPorCliente(Integer idCliente) {
        if (!clienteRepository.existsById(idCliente)) {
            throw new IllegalArgumentException("El cliente no existe.");
        }
        return ventaRepository.buscarPorCliente(idCliente);
    }

    private String soloDigitos(String texto) {
        return texto == null ? null : texto.replaceAll("[\\s-]", "");
    }

    private String motivoRechazo(String numeroTarjeta, String vencimiento, String cvv) {
        if (!ReglasVenta.tarjetaValida(numeroTarjeta)) {
            return "el número de tarjeta debe tener 16 dígitos.";
        }
        if (!ReglasVenta.vencimientoValido(vencimiento, LocalDate.now())) {
            return "la tarjeta está vencida o la fecha no tiene el formato MM/AA.";
        }
        return "el CVV debe tener 3 dígitos.";
    }

    // Valida el carrito y suma las cantidades si el mismo producto viene dos veces
    private Map<Integer, Integer> agruparCantidades(List<ItemCarritoRequest> carrito) {
        if (carrito == null || carrito.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío.");
        }

        Map<Integer, Integer> cantidades = new LinkedHashMap<>();

        for (ItemCarritoRequest item : carrito) {
            if (item.getIdProducto() == null) {
                throw new IllegalArgumentException("Debe indicar el producto.");
            }
            if (!ReglasVenta.cantidadValida(item.getCantidad())) {
                throw new IllegalArgumentException("La cantidad de cada producto debe ser 1 o más.");
            }
            cantidades.merge(item.getIdProducto(), item.getCantidad(), Integer::sum);
        }

        return cantidades;
    }
}
