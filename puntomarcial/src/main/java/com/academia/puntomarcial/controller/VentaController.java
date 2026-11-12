package com.academia.puntomarcial.controller;

import java.util.List;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.academia.puntomarcial.dto.ItemCarritoRequest;
import com.academia.puntomarcial.dto.VentaRequest;
import com.academia.puntomarcial.dto.VentaResponse;
import com.academia.puntomarcial.model.Venta;
import com.academia.puntomarcial.service.BoletaPdfService;
import com.academia.puntomarcial.service.ComprobanteService;
import com.academia.puntomarcial.service.ReglasVenta;
import com.academia.puntomarcial.service.VentaService;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final BoletaPdfService boletaPdfService;
    private final ComprobanteService comprobanteService;

    public VentaController(
            VentaService ventaService,
            BoletaPdfService boletaPdfService,
            ComprobanteService comprobanteService) {
        this.ventaService = ventaService;
        this.boletaPdfService = boletaPdfService;
        this.comprobanteService = comprobanteService;
    }

    // 201 si el pago se aprobó; 400 con la venta RECHAZADA si el pago no es válido
    @PostMapping
    public ResponseEntity<?> registrarVenta(@RequestBody VentaRequest request) {
        try {
            VentaResponse respuesta = ventaService.registrarVenta(request);

            // HU-12: el correo se envía cuando la venta ya está confirmada;
            // si falla, la venta no se revierte y solo se informa
            if (respuesta.isPagada()) {
                Venta venta = ventaService.buscarConDetalle(respuesta.getVenta().getIdVenta());
                ComprobanteService.ResultadoCorreo correo = comprobanteService.enviarComprobante(venta);
                respuesta.informarCorreo(correo.enviado(), correo.mensaje());
            }

            return ResponseEntity
                    .status(respuesta.isPagada() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST)
                    .body(respuesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // HU-12: boleta en PDF de una venta pagada, solo para el cliente dueño
    @GetMapping("/{id}/boleta")
    public ResponseEntity<?> descargarBoleta(
            @PathVariable Integer id,
            @RequestParam Integer idCliente) {
        try {
            Venta venta = ventaService.buscarVentaPagadaDelCliente(id, idCliente);
            byte[] pdf = boletaPdfService.generar(venta);
            String archivo = "boleta-" + ReglasVenta.numeroBoleta(venta.getIdVenta()) + ".pdf";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.attachment().filename(archivo).build().toString())
                    .body(pdf);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
        }
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> listarPorCliente(@PathVariable Integer idCliente) {
        try {
            return ResponseEntity.ok(ventaService.listarPorCliente(idCliente));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verificar")
    public ResponseEntity<?> verificarCarrito(@RequestBody List<ItemCarritoRequest> carrito) {
        try {
            return ResponseEntity.ok(ventaService.verificarCarrito(carrito));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
