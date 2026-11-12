package com.academia.puntomarcial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academia.puntomarcial.model.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, Integer> {

    List<Instructor> findByEstadoOrderByDisciplinaAscApellidoAsc(String estado);
}
