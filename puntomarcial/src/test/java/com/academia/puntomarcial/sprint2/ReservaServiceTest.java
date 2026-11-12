package com.academia.puntomarcial.sprint2;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.academia.puntomarcial.model.Clase;
import com.academia.puntomarcial.model.Cliente;
import com.academia.puntomarcial.model.Reserva;
import com.academia.puntomarcial.repository.ClaseRepository;
import com.academia.puntomarcial.repository.ClienteRepository;
import com.academia.puntomarcial.repository.ReservaRepository;
import com.academia.puntomarcial.service.ReservaService;

/**
 * Sprint 2 – HU-05: Reserva de clases por los clientes.
 */
public class ReservaServiceTest {

    //CP25
    @Test
    void reservar_claseSinCupos_debeLanzarExcepcion()
    {
        //Preparar datos
        ReservaRepository reservas = mock(ReservaRepository.class);
        ClaseRepository clases = mock(ClaseRepository.class);
        ClienteRepository clientes = mock(ClienteRepository.class);
        ReservaService servicio = new ReservaService(reservas, clases, clientes);

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Clase claseLlena = new Clase();
        claseLlena.setIdClase(1004);
        claseLlena.setHorarioInicio(LocalDateTime.now().plusDays(2));
        claseLlena.setAforoDisponible(0);

        Reserva reserva = new Reserva();
        reserva.setCliente(maria);
        reserva.setClase(claseLlena);

        when(clases.buscarParaReserva(1004)).thenReturn(Optional.of(claseLlena));
        when(clientes.findById(1001)).thenReturn(Optional.of(maria));
        when(reservas.contarReserva(1001, 1004)).thenReturn(0L);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarReserva(reserva));

        //Verificar
        assertEquals("La clase no tiene cupos disponibles.", error.getMessage());
    }

    //CP26
    @Test
    void reservar_mismaClaseDosVeces_debeLanzarExcepcion()
    {
        //Preparar datos
        ReservaRepository reservas = mock(ReservaRepository.class);
        ClaseRepository clases = mock(ClaseRepository.class);
        ClienteRepository clientes = mock(ClienteRepository.class);
        ReservaService servicio = new ReservaService(reservas, clases, clientes);

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Clase clase = new Clase();
        clase.setIdClase(1002);
        clase.setHorarioInicio(LocalDateTime.now().plusDays(1));
        clase.setAforoDisponible(9);

        Reserva reserva = new Reserva();
        reserva.setCliente(maria);
        reserva.setClase(clase);

        when(clases.buscarParaReserva(1002)).thenReturn(Optional.of(clase));
        when(clientes.findById(1001)).thenReturn(Optional.of(maria));
        // María ya tiene una reserva activa en esa clase
        when(reservas.contarReserva(1001, 1002)).thenReturn(1L);

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarReserva(reserva));

        //Verificar
        assertEquals("El cliente ya tiene una reserva para esta clase.", error.getMessage());
    }

    //CP27
    @Test
    void reservar_claseQueYaEmpezo_debeLanzarExcepcion()
    {
        //Preparar datos
        ReservaRepository reservas = mock(ReservaRepository.class);
        ClaseRepository clases = mock(ClaseRepository.class);
        ClienteRepository clientes = mock(ClienteRepository.class);
        ReservaService servicio = new ReservaService(reservas, clases, clientes);

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Clase claseEnCurso = new Clase();
        claseEnCurso.setIdClase(1015);
        claseEnCurso.setHorarioInicio(LocalDateTime.now().minusMinutes(30));   // empezó hace 30 minutos
        claseEnCurso.setAforoDisponible(9);

        Reserva reserva = new Reserva();
        reserva.setCliente(maria);
        reserva.setClase(claseEnCurso);

        when(clases.buscarParaReserva(1015)).thenReturn(Optional.of(claseEnCurso));
        when(clientes.findById(1001)).thenReturn(Optional.of(maria));

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.registrarReserva(reserva));

        //Verificar
        assertEquals("La clase ya empezó: no se puede reservar.", error.getMessage());
    }

    //CP28
    @Test
    void reservar_conCupo_debeRegistrarYDisminuirElAforo()
    {
        //Preparar datos
        ReservaRepository reservas = mock(ReservaRepository.class);
        ClaseRepository clases = mock(ClaseRepository.class);
        ClienteRepository clientes = mock(ClienteRepository.class);
        ReservaService servicio = new ReservaService(reservas, clases, clientes);

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Clase clase = new Clase();
        clase.setIdClase(1009);
        clase.setHorarioInicio(LocalDateTime.now().plusDays(4));
        clase.setAforoMaximo(15);
        clase.setAforoDisponible(10);

        Reserva reserva = new Reserva();
        reserva.setCliente(maria);
        reserva.setClase(clase);

        when(clases.buscarParaReserva(1009)).thenReturn(Optional.of(clase));
        when(clientes.findById(1001)).thenReturn(Optional.of(maria));
        when(reservas.contarReserva(1001, 1009)).thenReturn(0L);
        when(reservas.save(any(Reserva.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        //Ejecutar
        Reserva guardada = servicio.registrarReserva(reserva);

        //Verificar
        assertEquals(9, clase.getAforoDisponible());                        // el aforo bajó de 10 a 9
        assertEquals("ACTIVA", guardada.getEstado());
        assertEquals(LocalDate.now().toString(), guardada.getFechaReserva());
        verify(clases).save(clase);
    }
}
