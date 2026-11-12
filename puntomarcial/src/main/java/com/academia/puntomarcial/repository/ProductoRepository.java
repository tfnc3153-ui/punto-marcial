package com.academia.puntomarcial.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academia.puntomarcial.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Bloquea la fila hasta terminar la venta, para que dos compras no vendan el mismo stock
    @Query(
        value = "SELECT * FROM producto WHERE id_producto = :id FOR UPDATE",
        nativeQuery = true
    )
    Optional<Producto> buscarParaVenta(@Param("id") Integer id);

    boolean existsByNombreIgnoreCaseAndTallaIgnoreCase(String nombre, String talla);

    boolean existsByNombreIgnoreCaseAndTallaIgnoreCaseAndIdProductoNot(
            String nombre, String talla, Integer idProducto);
}
