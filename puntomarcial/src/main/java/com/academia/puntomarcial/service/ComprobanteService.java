package com.academia.puntomarcial.service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.academia.puntomarcial.model.DetalleVenta;
import com.academia.puntomarcial.model.Venta;

import jakarta.mail.internet.MimeMessage;

/**
 * HU-12: envía el comprobante al correo del cliente, con la boleta PDF adjunta.
 * Si el envío falla, solo se informa: la venta ya quedó registrada.
 */
@Service
public class ComprobanteService {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final JavaMailSender mailSender;
    private final BoletaPdfService boletaPdfService;
    private final String remitente;

    public ComprobanteService(
            JavaMailSender mailSender,
            BoletaPdfService boletaPdfService,
            @Value("${MAIL_FROM:${spring.mail.username:}}") String remitente) {
        this.mailSender = mailSender;
        this.boletaPdfService = boletaPdfService;
        this.remitente = remitente;
    }

    /** Resultado del envío, para informar al cliente. */
    public record ResultadoCorreo(boolean enviado, String mensaje) {
    }

    public ResultadoCorreo enviarComprobante(Venta venta) {
        String destino = venta.getCliente().getCorreo();

        if (remitente == null || remitente.isBlank()) {
            return new ResultadoCorreo(false,
                    "No se pudo enviar el comprobante: el correo de la academia no está configurado.");
        }
        if (destino == null || destino.isBlank()) {
            return new ResultadoCorreo(false, "No se pudo enviar el comprobante: el cliente no tiene correo.");
        }

        String numero = ReglasVenta.numeroBoleta(venta.getIdVenta());

        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom(remitente, "Punto Marcial");
            helper.setTo(destino);
            helper.setSubject("Tu comprobante de compra " + numero + " - Punto Marcial");
            helper.setText(cuerpoHtml(venta, numero), true);
            helper.addAttachment("boleta-" + numero + ".pdf",
                    new ByteArrayResource(boletaPdfService.generar(venta)), "application/pdf");

            mailSender.send(mensaje);
            return new ResultadoCorreo(true, "Te enviamos el comprobante a " + destino + ".");
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
            return new ResultadoCorreo(false,
                    "No se pudo enviar el comprobante a " + destino + ". Puedes descargar la boleta en PDF.");
        }
    }

    private String cuerpoHtml(Venta venta, String numero) {
        StringBuilder filas = new StringBuilder();
        for (DetalleVenta detalle : venta.getDetalles()) {
            filas.append("<tr>")
                    .append(td(detalle.getProducto().getNombre() + " (" + detalle.getProducto().getTalla() + ")", "left"))
                    .append(td(String.valueOf(detalle.getCantidad()), "right"))
                    .append(td("S/ " + String.format(Locale.US, "%.2f", detalle.getSubtotal()), "right"))
                    .append("</tr>");
        }

        String nombre = HtmlUtils.htmlEscape(venta.getCliente().getNombre());

        return """
            <div style="font-family:Arial,sans-serif;color:#1A1D23;max-width:560px">
              <h1 style="color:#B42318;font-size:22px;margin:0 0 4px">PUNTO MARCIAL</h1>
              <p style="color:#475467;margin:0 0 20px">Comprobante de compra</p>
              <p>Hola %s, gracias por tu compra. Este es el resumen:</p>
              <p><strong>Boleta:</strong> %s<br>
                 <strong>Fecha:</strong> %s<br>
                 <strong>Tarjeta:</strong> **** %s</p>
              <table style="width:100%%;border-collapse:collapse" cellpadding="6">
                <tr style="background:#B42318;color:#FFFFFF">
                  <th align="left">Producto</th><th align="right">Cant.</th><th align="right">Subtotal</th>
                </tr>
                %s
              </table>
              <p style="font-size:18px;text-align:right"><strong>Total: S/ %s</strong></p>
              <p style="color:#475467">Adjuntamos la boleta en PDF.</p>
            </div>
            """.formatted(nombre, numero, venta.getFecha().format(FORMATO_FECHA),
                venta.getUltimos4(), filas, String.format(Locale.US, "%.2f", venta.getTotal()));
    }

    private String td(String texto, String alineacion) {
        return "<td align=\"" + alineacion + "\" style=\"border-bottom:1px solid #D0D5DD\">"
                + HtmlUtils.htmlEscape(texto) + "</td>";
    }
}
