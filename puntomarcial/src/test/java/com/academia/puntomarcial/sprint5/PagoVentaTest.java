package com.academia.puntomarcial.sprint5;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.academia.puntomarcial.dto.DatosPagoRequest;
import com.academia.puntomarcial.dto.ItemCarritoRequest;
import com.academia.puntomarcial.dto.VentaRequest;
import com.academia.puntomarcial.dto.VentaResponse;
import com.academia.puntomarcial.model.Cliente;
import com.academia.puntomarcial.model.Producto;
import com.academia.puntomarcial.repository.ClienteRepository;
import com.academia.puntomarcial.repository.ProductoRepository;
import com.academia.puntomarcial.repository.VentaRepository;
import com.academia.puntomarcial.service.VentaService;

/**
 * Sprint 5 – HU-10: Pago simulado y HU-11: Actualización automática del stock.
 */
public class PagoVentaTest {

    //CP46
    @Test
    void pagar_tarjetaDe15Digitos_debeRechazarLaVenta()
    {
        //Preparar datos
        ProductoRepository productos = mock(ProductoRepository.class);
        ClienteRepository clientes = mock(ClienteRepository.class);
        VentaService servicio = new VentaService(productos, mock(VentaRepository.class), clientes);

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Producto vendas = new Producto();
        vendas.setIdProducto(1012);
        vendas.setNombre("Vendas de algodón");
        vendas.setPrecio(new BigDecimal("19.90"));
        vendas.setStock(40);
        vendas.setStockMinimo(10);
        vendas.setEstado("ACTIVO");

        VentaRequest compra = new VentaRequest();
        compra.setIdCliente(1001);
        compra.setItems(List.of(new ItemCarritoRequest(1012, 2)));
        compra.setPago(new DatosPagoRequest("Maria Flores", "411111111111111", "12/99", "123"));

        when(clientes.findById(1001)).thenReturn(Optional.of(maria));
        when(productos.buscarParaVenta(1012)).thenReturn(Optional.of(vendas));

        //Ejecutar
        VentaResponse respuesta = servicio.registrarVenta(compra);

        //Verificar
        assertEquals("RECHAZADA", respuesta.getVenta().getEstado());
        assertEquals("Pago rechazado: el número de tarjeta debe tener 16 dígitos.", respuesta.getMensaje());
        assertEquals(40, vendas.getStock());   // el stock no cambia
    }

    //CP47
    @Test
    void pagar_tarjetaVencida_debeRechazarLaVenta()
    {
        //Preparar datos
        ProductoRepository productos = mock(ProductoRepository.class);
        ClienteRepository clientes = mock(ClienteRepository.class);
        VentaService servicio = new VentaService(productos, mock(VentaRepository.class), clientes);

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Producto vendas = new Producto();
        vendas.setIdProducto(1012);
        vendas.setNombre("Vendas de algodón");
        vendas.setPrecio(new BigDecimal("19.90"));
        vendas.setStock(40);
        vendas.setStockMinimo(10);
        vendas.setEstado("ACTIVO");

        VentaRequest compra = new VentaRequest();
        compra.setIdCliente(1001);
        compra.setItems(List.of(new ItemCarritoRequest(1012, 1)));
        compra.setPago(new DatosPagoRequest("Maria Flores", "4111111111111111", "01/20", "123"));   // venció en enero de 2020

        when(clientes.findById(1001)).thenReturn(Optional.of(maria));
        when(productos.buscarParaVenta(1012)).thenReturn(Optional.of(vendas));

        //Ejecutar
        VentaResponse respuesta = servicio.registrarVenta(compra);

        //Verificar
        assertEquals("RECHAZADA", respuesta.getVenta().getEstado());
        assertEquals("Pago rechazado: la tarjeta está vencida o la fecha no tiene el formato MM/AA.", respuesta.getMensaje());
    }

    //CP48
    @Test
    void pagar_cvvDe2Digitos_debeRechazarLaVenta()
    {
        //Preparar datos
        ProductoRepository productos = mock(ProductoRepository.class);
        ClienteRepository clientes = mock(ClienteRepository.class);
        VentaService servicio = new VentaService(productos, mock(VentaRepository.class), clientes);

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Producto vendas = new Producto();
        vendas.setIdProducto(1012);
        vendas.setNombre("Vendas de algodón");
        vendas.setPrecio(new BigDecimal("19.90"));
        vendas.setStock(40);
        vendas.setStockMinimo(10);
        vendas.setEstado("ACTIVO");

        VentaRequest compra = new VentaRequest();
        compra.setIdCliente(1001);
        compra.setItems(List.of(new ItemCarritoRequest(1012, 1)));
        compra.setPago(new DatosPagoRequest("Maria Flores", "4111111111111111", "12/99", "12"));

        when(clientes.findById(1001)).thenReturn(Optional.of(maria));
        when(productos.buscarParaVenta(1012)).thenReturn(Optional.of(vendas));

        //Ejecutar
        VentaResponse respuesta = servicio.registrarVenta(compra);

        //Verificar
        assertEquals("RECHAZADA", respuesta.getVenta().getEstado());
        assertEquals("Pago rechazado: el CVV debe tener 3 dígitos.", respuesta.getMensaje());
    }

    //CP49
    @Test
    void pagar_datosCorrectos_debeRegistrarVentaPagadaYDescontarStock()
    {
        //Preparar datos
        ProductoRepository productos = mock(ProductoRepository.class);
        ClienteRepository clientes = mock(ClienteRepository.class);
        VentaService servicio = new VentaService(productos, mock(VentaRepository.class), clientes);

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Producto vendas = new Producto();
        vendas.setIdProducto(1012);
        vendas.setNombre("Vendas de algodón");
        vendas.setPrecio(new BigDecimal("19.90"));
        vendas.setStock(40);
        vendas.setStockMinimo(10);
        vendas.setEstado("ACTIVO");

        VentaRequest compra = new VentaRequest();
        compra.setIdCliente(1001);
        compra.setItems(List.of(new ItemCarritoRequest(1012, 2)));
        compra.setPago(new DatosPagoRequest("Maria Flores", "4111 1111 1111 1111", "12/99", "123"));

        when(clientes.findById(1001)).thenReturn(Optional.of(maria));
        when(productos.buscarParaVenta(1012)).thenReturn(Optional.of(vendas));

        //Ejecutar
        VentaResponse respuesta = servicio.registrarVenta(compra);

        //Verificar
        assertEquals("PAGADA", respuesta.getVenta().getEstado());
        assertEquals("Pago aprobado. Tu compra fue registrada.", respuesta.getMensaje());
        assertEquals(new BigDecimal("39.80"), respuesta.getVenta().getTotal());
        assertEquals("1111", respuesta.getVenta().getUltimos4());   // solo se guardan los últimos 4 dígitos
        assertEquals(38, vendas.getStock());                         // el stock bajó de 40 a 38
    }

    //CP50
    @Test
    void pagar_stockLlegaAlMinimo_debeGenerarAlerta()
    {
        //Preparar datos
        ProductoRepository productos = mock(ProductoRepository.class);
        ClienteRepository clientes = mock(ClienteRepository.class);
        VentaService servicio = new VentaService(productos, mock(VentaRepository.class), clientes);

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Producto guantes = new Producto();
        guantes.setIdProducto(1010);
        guantes.setNombre("Guantes de box");
        guantes.setTalla("16 oz");
        guantes.setPrecio(new BigDecimal("189.90"));
        guantes.setStock(5);
        guantes.setStockMinimo(4);
        guantes.setEstado("ACTIVO");

        VentaRequest compra = new VentaRequest();
        compra.setIdCliente(1001);
        compra.setItems(List.of(new ItemCarritoRequest(1010, 1)));
        compra.setPago(new DatosPagoRequest("Maria Flores", "4111111111111111", "12/99", "123"));

        when(clientes.findById(1001)).thenReturn(Optional.of(maria));
        when(productos.buscarParaVenta(1010)).thenReturn(Optional.of(guantes));

        //Ejecutar
        VentaResponse respuesta = servicio.registrarVenta(compra);

        //Verificar: quedan 4, igual al mínimo, así que se avisa
        assertEquals(4, guantes.getStock());
        assertEquals("Guantes de box (talla 16 oz): quedan 4", respuesta.getProductosStockMinimo().get(0));
    }

    //CP51
    @Test
    void pagar_stockInsuficiente_debeLanzarExcepcion()
    {
        //Preparar datos
        ProductoRepository productos = mock(ProductoRepository.class);
        ClienteRepository clientes = mock(ClienteRepository.class);
        VentaService servicio = new VentaService(productos, mock(VentaRepository.class), clientes);

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Producto guantes = new Producto();
        guantes.setIdProducto(1009);
        guantes.setNombre("Guantes de box");
        guantes.setTalla("12 oz");
        guantes.setPrecio(new BigDecimal("179.90"));
        guantes.setStock(2);
        guantes.setStockMinimo(3);
        guantes.setEstado("ACTIVO");

        VentaRequest compra = new VentaRequest();
        compra.setIdCliente(1001);
        compra.setItems(List.of(new ItemCarritoRequest(1009, 5)));
        compra.setPago(new DatosPagoRequest("Maria Flores", "4111111111111111", "12/99", "123"));

        when(clientes.findById(1001)).thenReturn(Optional.of(maria));
        when(productos.buscarParaVenta(1009)).thenReturn(Optional.of(guantes));

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarVenta(compra));

        //Verificar
        assertEquals("No hay stock suficiente para: Guantes de box (talla 12 oz): pediste 5, hay 2", error.getMessage());
        assertEquals(2, guantes.getStock());   // el stock nunca queda negativo
    }

    //CP52
    @Test
    void pagar_sinDatosDeTarjeta_debeLanzarExcepcion()
    {
        //Preparar datos
        VentaService servicio = new VentaService(mock(ProductoRepository.class),
                                                 mock(VentaRepository.class),
                                                 mock(ClienteRepository.class));

        VentaRequest compra = new VentaRequest();
        compra.setIdCliente(1001);
        compra.setItems(List.of(new ItemCarritoRequest(1012, 1)));
        compra.setPago(null);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarVenta(compra));

        //Verificar
        assertEquals("Debe ingresar los datos de pago.", error.getMessage());
    }
}