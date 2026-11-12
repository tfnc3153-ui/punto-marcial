package com.academia.puntomarcial.sprint5;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.academia.puntomarcial.model.Cliente;
import com.academia.puntomarcial.model.Venta;
import com.academia.puntomarcial.repository.ClienteRepository;
import com.academia.puntomarcial.repository.ProductoRepository;
import com.academia.puntomarcial.repository.VentaRepository;
import com.academia.puntomarcial.service.VentaService;

/**
 * Sprint 5 – HU-12: Comprobante (boleta) de la compra.
 */
public class BoletaTest {

    //CP53
    @Test
    void boleta_deCompraRechazada_debeLanzarExcepcion()
    {
        //Preparar datos
        VentaRepository ventas = mock(VentaRepository.class);
        VentaService servicio = new VentaService(mock(ProductoRepository.class), ventas, mock(ClienteRepository.class));

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Venta venta = new Venta();
        venta.setIdVenta(1005);
        venta.setCliente(maria);
        venta.setEstado("RECHAZADA");

        when(ventas.buscarConDetalle(1005)).thenReturn(Optional.of(venta));

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.buscarVentaPagadaDelCliente(1005, 1001));

        //Verificar
        assertEquals("Solo las compras pagadas tienen boleta.", error.getMessage());
    }

    //CP54
    @Test
    void boleta_deOtroCliente_debeLanzarExcepcion()
    {
        //Preparar datos
        VentaRepository ventas = mock(VentaRepository.class);
        VentaService servicio = new VentaService(mock(ProductoRepository.class), ventas, mock(ClienteRepository.class));

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Venta ventaDeMaria = new Venta();
        ventaDeMaria.setIdVenta(1001);
        ventaDeMaria.setCliente(maria);
        ventaDeMaria.setEstado("PAGADA");

        when(ventas.buscarConDetalle(1001)).thenReturn(Optional.of(ventaDeMaria));

        //Ejecutar: Carlos (1002) intenta descargar la boleta de María
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.buscarVentaPagadaDelCliente(1001, 1002));

        //Verificar
        assertEquals("Solo el cliente dueño de la compra puede descargar la boleta.", error.getMessage());
    }

    //CP55
    @Test
    void boleta_compraPagadaDelMismoCliente_debeDevolverLaVenta()
    {
        //Preparar datos
        VentaRepository ventas = mock(VentaRepository.class);
        VentaService servicio = new VentaService(mock(ProductoRepository.class), ventas, mock(ClienteRepository.class));

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Venta venta = new Venta();
        venta.setIdVenta(1001);
        venta.setCliente(maria);
        venta.setEstado("PAGADA");

        when(ventas.buscarConDetalle(1001)).thenReturn(Optional.of(venta));

        //Ejecutar
        Venta resultado = servicio.buscarVentaPagadaDelCliente(1001, 1001);

        //Verificar
        assertSame(venta, resultado);
    }
}