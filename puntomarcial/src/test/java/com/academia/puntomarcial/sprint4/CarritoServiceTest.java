package com.academia.puntomarcial.sprint4;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.academia.puntomarcial.dto.ItemCarritoRequest;
import com.academia.puntomarcial.dto.VerificacionCarritoResponse;
import com.academia.puntomarcial.model.Producto;
import com.academia.puntomarcial.repository.ClienteRepository;
import com.academia.puntomarcial.repository.ProductoRepository;
import com.academia.puntomarcial.repository.VentaRepository;
import com.academia.puntomarcial.service.VentaService;

/**
 * Sprint 4 – HU-09: Compra de equipamiento (carrito, subtotal, total y stock suficiente).
 */
public class CarritoServiceTest {

    //CP42
    @Test
    void verificarCarrito_vacio_debeLanzarExcepcion()
    {
        //Preparar datos
        VentaService servicio = new VentaService(mock(ProductoRepository.class),
                                                 mock(VentaRepository.class),
                                                 mock(ClienteRepository.class));

        List<ItemCarritoRequest> carrito = List.of();

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.verificarCarrito(carrito));

        //Verificar
        assertEquals("El carrito está vacío.", error.getMessage());
    }

    //CP43
    @Test
    void verificarCarrito_cantidadCero_debeLanzarExcepcion()
    {
        //Preparar datos
        VentaService servicio = new VentaService(mock(ProductoRepository.class),
                                                 mock(VentaRepository.class),
                                                 mock(ClienteRepository.class));

        List<ItemCarritoRequest> carrito = List.of(new ItemCarritoRequest(1012, 0));

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.verificarCarrito(carrito));

        //Verificar
        assertEquals("La cantidad de cada producto debe ser 1 o más.", error.getMessage());
    }

    //CP44
    @Test
    void verificarCarrito_conStock_debeCalcularElTotal()
    {
        //Preparar datos
        ProductoRepository productos = mock(ProductoRepository.class);
        VentaService servicio = new VentaService(productos, mock(VentaRepository.class), mock(ClienteRepository.class));

        Producto vendas = new Producto();
        vendas.setIdProducto(1012);
        vendas.setNombre("Vendas de algodón");
        vendas.setPrecio(new BigDecimal("19.90"));
        vendas.setStock(40);
        vendas.setEstado("ACTIVO");

        Producto guantes = new Producto();
        guantes.setIdProducto(1001);
        guantes.setNombre("Guantes de MMA");
        guantes.setPrecio(new BigDecimal("129.90"));
        guantes.setStock(15);
        guantes.setEstado("ACTIVO");

        when(productos.findById(1012)).thenReturn(Optional.of(vendas));
        when(productos.findById(1001)).thenReturn(Optional.of(guantes));

        // 2 vendas (2 × 19.90 = 39.80) + 1 guantes (129.90)
        List<ItemCarritoRequest> carrito = List.of(new ItemCarritoRequest(1012, 2),
                                                   new ItemCarritoRequest(1001, 1));

        //Ejecutar
        VerificacionCarritoResponse respuesta = servicio.verificarCarrito(carrito);

        //Verificar
        assertEquals(new BigDecimal("39.80"), respuesta.getItems().get(0).getSubtotal());
        assertEquals(new BigDecimal("169.70"), respuesta.getTotal());
        assertTrue(respuesta.isPuedeContinuar());
    }

    //CP45
    @Test
    void verificarCarrito_sinStockSuficiente_noDebePermitirContinuar()
    {
        //Preparar datos
        ProductoRepository productos = mock(ProductoRepository.class);
        VentaService servicio = new VentaService(productos, mock(VentaRepository.class), mock(ClienteRepository.class));

        Producto guantes = new Producto();
        guantes.setIdProducto(1009);
        guantes.setNombre("Guantes de box");
        guantes.setTalla("12 oz");
        guantes.setPrecio(new BigDecimal("179.90"));
        guantes.setStock(2);
        guantes.setEstado("ACTIVO");

        when(productos.findById(1009)).thenReturn(Optional.of(guantes));

        // Pide 5 pero solo hay 2
        List<ItemCarritoRequest> carrito = List.of(new ItemCarritoRequest(1009, 5));

        //Ejecutar
        VerificacionCarritoResponse respuesta = servicio.verificarCarrito(carrito);

        //Verificar
        assertFalse(respuesta.isPuedeContinuar());
        assertEquals("Guantes de box (talla 12 oz): pediste 5, hay 2", respuesta.getProductosSinStock().get(0));
    }
}