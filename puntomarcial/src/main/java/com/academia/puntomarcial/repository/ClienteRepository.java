package com.academia.puntomarcial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academia.puntomarcial.model.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    boolean existsByNumDoc(String numDoc);
    boolean existsByCorreo(String correo);


    
}
