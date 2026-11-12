package com.academia.puntomarcial.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academia.puntomarcial.model.Clase;

public interface ClaseRepository extends JpaRepository<Clase, Integer> {

    @Query(
        value = "SELECT * FROM clase WHERE id_clase = :id FOR UPDATE",
        nativeQuery = true
    )
    Optional<Clase> buscarParaReserva(@Param("id") Integer id);

    // Clases que comparten sala o instructor; el traslape se revisa con ReglasReserva.hayTraslape
    @Query("""
        SELECT c
        FROM Clase c
        WHERE c.idClase <> :idExcluir
        AND (c.sala = :sala OR c.instructor.idUsuario = :idInstructor)
    """)
    List<Clase> buscarPorSalaOInstructor(
        @Param("sala") String sala,
        @Param("idInstructor") Integer idInstructor,
        @Param("idExcluir") Integer idExcluir
    );

    // Clases que el cliente puede reservar: las que todavía no empiezan
    List<Clase> findByHorarioInicioAfterOrderByHorarioInicioAsc(LocalDateTime ahora);

    // Clases del instructor: próximas (aún no terminan) y pasadas (ya terminaron)
    List<Clase> findByInstructorIdUsuarioAndHorarioFinAfterOrderByHorarioInicioAsc(Integer idInstructor, LocalDateTime ahora);

    List<Clase> findByInstructorIdUsuarioAndHorarioFinLessThanEqualOrderByHorarioInicioDesc(Integer idInstructor, LocalDateTime ahora);

    long countByInstructorIdUsuarioAndHorarioInicioAfter(Integer idInstructor, LocalDateTime ahora);
}
