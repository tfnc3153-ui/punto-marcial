package com.academia.puntomarcial.sprint3;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
 * Sprint 3 – HU-06: Cancelación de reservas con restricción de tiempo (2 horas).
 */
public class CancelacionReservaTest {

    //CP29
    @Test
    void cancelar_conMenosDe2Horas_debeLanzarExcepcion()
    {
        //Preparar datos
        ReservaRepository reservas = mock(ReservaRepository.class);
        ClaseRepository clases = mock(ClaseRepository.class);
        ReservaService servicio = new ReservaService(reservas, clases, mock(ClienteRepository.class));

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Clase clase = new Clase();
        clase.setIdClase(1001);
        clase.setHorarioInicio(LocalDateTime.now().plusHours(1));   // falta solo 1 hora

        Reserva reserva = new Reserva();
        reserva.setIdReserva(1001);
        reserva.setCliente(maria);
        reserva.setClase(clase);
        reserva.setEstado("ACTIVA");

        when(reservas.findById(1001)).thenReturn(Optional.of(reserva));
        when(clases.buscarParaReserva(1001)).thenReturn(Optional.of(clase));

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.cancelarReserva(1001, 1001));

        //Verificar
        assertEquals("Solo se puede cancelar con al menos 2 horas de anticipación.", error.getMessage());
    }

    //CP30
    @Test
    void cancelar_conMasDe2Horas_debeCancelarYDevolverElCupo()
    {
        //Preparar datos
        ReservaRepository reservas = mock(ReservaRepository.class);
        ClaseRepository clases = mock(ClaseRepository.class);
        ReservaService servicio = new ReservaService(reservas, clases, mock(ClienteRepository.class));

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Clase clase = new Clase();
        clase.setIdClase(1002);
        clase.setHorarioInicio(LocalDateTime.now().plusHours(3));   // faltan 3 horas
        clase.setAforoMaximo(12);
        clase.setAforoDisponible(9);

        Reserva reserva = new Reserva();
        reserva.setIdReserva(1003);
        reserva.setCliente(maria);
        reserva.setClase(clase);
        reserva.setEstado("ACTIVA");

        when(reservas.findById(1003)).thenReturn(Optional.of(reserva));
        when(clases.buscarParaReserva(1002)).thenReturn(Optional.of(clase));
        when(reservas.save(any(Reserva.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        //Ejecutar
        Reserva cancelada = servicio.cancelarReserva(1003, 1001);

        //Verificar
        assertEquals("CANCELADA", cancelada.getEstado());   // queda en la BD como cancelada
        assertEquals(10, clase.getAforoDisponible());       // el cupo volvió: de 9 a 10
    }

    //CP31
    @Test
    void cancelar_reservaDeOtroCliente_debeLanzarExcepcion()
    {
        //Preparar datos
        ReservaRepository reservas = mock(ReservaRepository.class);
        ReservaService servicio = new ReservaService(reservas, mock(ClaseRepository.class), mock(ClienteRepository.class));

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Reserva reservaDeMaria = new Reserva();
        reservaDeMaria.setIdReserva(1003);
        reservaDeMaria.setCliente(maria);
        reservaDeMaria.setEstado("ACTIVA");

        when(reservas.findById(1003)).thenReturn(Optional.of(reservaDeMaria));

        //Ejecutar: Carlos (1002) intenta cancelar la reserva de María
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.cancelarReserva(1003, 1002));

        //Verificar
        assertEquals("Solo el cliente dueño de la reserva puede cancelarla.", error.getMessage());
    }

    //CP32
    @Test
    void cancelar_reservaYaCancelada_debeLanzarExcepcion()
    {
        //Preparar datos
        ReservaRepository reservas = mock(ReservaRepository.class);
        ReservaService servicio = new ReservaService(reservas, mock(ClaseRepository.class), mock(ClienteRepository.class));

        Cliente maria = new Cliente();
        maria.setIdUsuario(1001);

        Reserva reserva = new Reserva();
        reserva.setIdReserva(1003);
        reserva.setCliente(maria);
        reserva.setEstado("CANCELADA");

        when(reservas.findById(1003)).thenReturn(Optional.of(reserva));

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.cancelarReserva(1003, 1001));

        //Verificar
        assertEquals("La reserva ya estaba cancelada.", error.getMessage());
    }

    //CP33
    @Test
    void cancelar_reservaQueNoExiste_debeLanzarExcepcion()
    {
        //Preparar datos
        ReservaRepository reservas = mock(ReservaRepository.class);
        ReservaService servicio = new ReservaService(reservas, mock(ClaseRepository.class), mock(ClienteRepository.class));

        when(reservas.findById(9999)).thenReturn(Optional.empty());

        //Ejecutar
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                                      () -> servicio.cancelarReserva(9999, 1001));

        //Verificar
        assertEquals("La reserva no existe.", error.getMessage());
    }
}