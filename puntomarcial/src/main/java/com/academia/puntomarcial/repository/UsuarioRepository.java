package com.academia.puntomarcial.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.academia.puntomarcial.model.Usuario;


public interface  UsuarioRepository extends JpaRepository<Usuario,Integer>{
    Optional<Usuario> findByCorreo(String correo);

    boolean existsByNumDoc(String numDoc);

    boolean existsByCorreo(String correo);

    boolean existsByCorreoAndIdUsuarioNot(String correo, Integer idUsuario);

    List<Usuario> findAllByOrderByApellidoAscNombreAsc();

    @Query("SELECT COUNT(a) FROM Administrador a WHERE a.estado = 'ACTIVO'")
    long contarAdministradoresActivos();
}
