package com.academia.puntomarcial.sprint4;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.academia.puntomarcial.model.Producto;
import com.academia.puntomarcial.repository.ProductoRepository;
import com.academia.puntomarcial.service.ProductoService;

/**
 * Sprint 4 – HU-08: Gestión de productos deportivos e inventario.
 */
public class ProductoServiceTest {

    //CP36
    @Test
    void registrar_nombreVacio_debeLanzarExcepcion()
    {
        //Preparar datos
        Producto producto = new Producto();
        ProductoService servicio = new ProductoService(mock(ProductoRepository.class));

        producto.setNombre("   ");
        producto.setCategoria("ACCESORIOS");
        producto.setDisciplina("GENERAL");
        producto.setTalla("4.5 m");
        producto.setPrecio(new BigDecimal("19.90"));
        producto.setStock(40);
        producto.setStockMinimo(10);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarProducto(producto));

        //Verificar
        assertEquals("El nombre del producto es obligatorio", error.getMessage());
    }

    //CP37
    @Test
    void registrar_precioCero_debeLanzarExcepcion()
    {
        //Preparar datos
        Producto producto = new Producto();
        ProductoService servicio = new ProductoService(mock(ProductoRepository.class));

        producto.setNombre("Vendas de algodón");
        producto.setCategoria("ACCESORIOS");
        producto.setDisciplina("GENERAL");
        producto.setTalla("4.5 m");
        producto.setPrecio(BigDecimal.ZERO);
        producto.setStock(40);
        producto.setStockMinimo(10);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarProducto(producto));

        //Verificar
        assertEquals("El precio debe ser mayor a 0", error.getMessage());
    }

    //CP38
    @Test
    void registrar_stockNegativo_debeLanzarExcepcion()
    {
        //Preparar datos
        Producto producto = new Producto();
        ProductoService servicio = new ProductoService(mock(ProductoRepository.class));

        producto.setNombre("Vendas de algodón");
        producto.setCategoria("ACCESORIOS");
        producto.setDisciplina("GENERAL");
        producto.setTalla("4.5 m");
        producto.setPrecio(new BigDecimal("19.90"));
        producto.setStock(-1);
        producto.setStockMinimo(10);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarProducto(producto));

        //Verificar
        assertEquals("El stock no puede ser negativo", error.getMessage());
    }

    //CP39
    @Test
    void registrar_categoriaQueNoExiste_debeLanzarExcepcion()
    {
        //Preparar datos
        Producto producto = new Producto();
        ProductoService servicio = new ProductoService(mock(ProductoRepository.class));

        producto.setNombre("Pelota");
        producto.setCategoria("JUGUETES");
        producto.setDisciplina("GENERAL");
        producto.setTalla("Única");
        producto.setPrecio(new BigDecimal("30.00"));
        producto.setStock(5);
        producto.setStockMinimo(1);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarProducto(producto));

        //Verificar
        assertEquals("La categoría no es válida", error.getMessage());
    }

    //CP40
    @Test
    void registrar_productoRepetido_debeLanzarExcepcion()
    {
        //Preparar datos
        Producto producto = new Producto();
        ProductoRepository repositorio = mock(ProductoRepository.class);
        ProductoService servicio = new ProductoService(repositorio);

        producto.setNombre("Vendas de algodón");
        producto.setCategoria("ACCESORIOS");
        producto.setDisciplina("GENERAL");
        producto.setTalla("4.5 m");
        producto.setPrecio(new BigDecimal("19.90"));
        producto.setStock(40);
        producto.setStockMinimo(10);

        // Ya existe un producto con el mismo nombre y talla
        when(repositorio.existsByNombreIgnoreCaseAndTallaIgnoreCase("Vendas de algodón", "4.5 m")).thenReturn(true);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarProducto(producto));

        //Verificar
        assertEquals("Ya existe un producto con ese nombre y talla", error.getMessage());
    }

    //CP41
    @Test
    void registrar_sinTalla_debeGuardarConTallaUnicaYActivo()
    {
        //Preparar datos
        Producto producto = new Producto();
        ProductoRepository repositorio = mock(ProductoRepository.class);
        ProductoService servicio = new ProductoService(repositorio);

        producto.setNombre("Protector bucal");
        producto.setCategoria("PROTECCION");
        producto.setDisciplina("GENERAL");
        producto.setTalla("");
        producto.setPrecio(new BigDecimal("24.90"));
        producto.setStock(25);
        producto.setStockMinimo(5);

        when(repositorio.save(any(Producto.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        //Ejecutar
        Producto guardado = servicio.registrarProducto(producto);

        //Verificar
        assertEquals("Única", guardado.getTalla());
        assertEquals("ACTIVO", guardado.getEstado());
    }
}
