package com.academia.puntomarcial.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.academia.puntomarcial.model.Administrador;
import com.academia.puntomarcial.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner crearAdministrador(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String correo = "admin@puntomarcial.com";

            if (usuarioRepository.findByCorreo(correo).isEmpty()) {

                Administrador admin = new Administrador();

                admin.setNombre("Administrador");
                admin.setApellido("Sistema");
                admin.setTipoDoc("DNI");
                admin.setNumDoc("ADMIN001");
                admin.setCorreo(correo);
                admin.setTelefono("999999999");
                admin.setPassword(passwordEncoder.encode("Admin123"));
                admin.setEstado("ACTIVO");
                admin.setCodigoEmpleado("ADM001");

                usuarioRepository.save(admin);

                System.out.println("Administrador creado: " + correo);

            } else {
                System.out.println("El administrador ya existe.");
            }
        };
    }
}
