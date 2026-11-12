package com.academia.puntomarcial.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academia.puntomarcial.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

    @Query("""
        SELECT DISTINCT v FROM Venta v
        LEFT JOIN FETCH v.detalles
        WHERE v.cliente.idUsuario = :idCliente
        ORDER BY v.idVenta DESC
    """)
    List<Venta> buscarPorCliente(@Param("idCliente") Integer idCliente);

    @Query("""
        SELECT v FROM Venta v
        LEFT JOIN FETCH v.detalles
        WHERE v.idVenta = :idVenta
    """)
    Optional<Venta> buscarConDetalle(@Param("idVenta") Integer idVenta);
}
