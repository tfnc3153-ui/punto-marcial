package com.academia.puntomarcial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academia.puntomarcial.model.Reserva;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    @Query("""
        SELECT COUNT(r)
        FROM Reserva r
        WHERE r.cliente.idUsuario = :idCliente
        AND r.clase.idClase = :idClase
        AND r.estado = 'ACTIVA'
    """)
    long contarReserva(
            @Param("idCliente") Integer idCliente,
            @Param("idClase") Integer idClase
    );
    // Solo las reservas que el cliente ve: las canceladas siguen en la BD pero no se muestran
    @Query("""
    SELECT r FROM Reserva r
    WHERE r.cliente.idUsuario = :idCliente
    AND r.estado <> 'CANCELADA'
    ORDER BY r.idReserva DESC
""")
List<Reserva> buscarPorCliente(
    @Param("idCliente") Integer idCliente
);

    long countByClaseIdClase(Integer idClase);

    // Alumnos de una clase (reservas activas), ordenados por apellido
    List<Reserva> findByClaseIdClaseAndEstadoOrderByClienteApellidoAsc(Integer idClase, String estado);

    // [idClase, cantidad] de todas las clases que tienen al menos una reserva
    @Query("SELECT r.clase.idClase, COUNT(r) FROM Reserva r GROUP BY r.clase.idClase")
    List<Object[]> contarReservasPorClase();
}