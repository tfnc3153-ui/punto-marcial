package com.academia.puntomarcial.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.academia.puntomarcial.model.Cliente;
import com.academia.puntomarcial.model.DetalleVenta;
import com.academia.puntomarcial.model.Venta;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * HU-12: genera la boleta de una venta pagada en PDF (OpenPDF).
 */
@Service
public class BoletaPdfService {

    private static final Color ROJO = new Color(0xB4, 0x23, 0x18);
    private static final Color GRIS = new Color(0x47, 0x54, 0x67);
    private static final Color GRIS_CLARO = new Color(0xF5, 0xF6, 0xF8);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Font titulo = new Font(Font.HELVETICA, 18, Font.BOLD, ROJO);
    private final Font subtitulo = new Font(Font.HELVETICA, 10, Font.NORMAL, GRIS);
    private final Font negrita = new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK);
    private final Font normal = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);
    private final Font cabecera = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
    private final Font total = new Font(Font.HELVETICA, 13, Font.BOLD, Color.BLACK);

    public byte[] generar(Venta venta) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.A4, 40, 40, 40, 40);

        try {
            PdfWriter.getInstance(documento, salida);
            documento.addTitle("Boleta " + ReglasVenta.numeroBoleta(venta.getIdVenta()));
            documento.addAuthor("Punto Marcial");
            documento.open();

            documento.add(encabezado(venta));
            documento.add(datosCliente(venta));
            documento.add(detalle(venta));
            documento.add(totales(venta));

            Paragraph gracias = new Paragraph("Gracias por tu compra. ¡Nos vemos en el entrenamiento!", subtitulo);
            gracias.setAlignment(Element.ALIGN_CENTER);
            gracias.setSpacingBefore(30);
            documento.add(gracias);

            documento.close();
        } catch (DocumentException e) {
            throw new IllegalStateException("No se pudo generar la boleta en PDF", e);
        }

        return salida.toByteArray();
    }

    // Logo, nombre de la academia y número de boleta
    private PdfPTable encabezado(Venta venta) throws DocumentException {
        PdfPTable tabla = new PdfPTable(new float[] { 1.2f, 4f, 3f });
        tabla.setWidthPercentage(100);

        PdfPCell celdaLogo = sinBorde(new PdfPCell());
        Image logo = cargarLogo();
        if (logo != null) {
            logo.scaleToFit(60, 60);
            celdaLogo.addElement(logo);
        }
        tabla.addCell(celdaLogo);

        PdfPCell celdaNombre = sinBorde(new PdfPCell());
        celdaNombre.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celdaNombre.addElement(new Paragraph("PUNTO MARCIAL", titulo));
        celdaNombre.addElement(new Paragraph("Academia de artes marciales · MMA · Muay Thai · Box", subtitulo));
        tabla.addCell(celdaNombre);

        PdfPCell celdaNumero = new PdfPCell();
        celdaNumero.setBorderColor(ROJO);
        celdaNumero.setBorderWidth(1.5f);
        celdaNumero.setPadding(8);
        Paragraph tipo = new Paragraph("BOLETA DE VENTA", negrita);
        tipo.setAlignment(Element.ALIGN_CENTER);
        Paragraph numero = new Paragraph(ReglasVenta.numeroBoleta(venta.getIdVenta()), titulo);
        numero.setAlignment(Element.ALIGN_CENTER);
        celdaNumero.addElement(tipo);
        celdaNumero.addElement(numero);
        tabla.addCell(celdaNumero);

        tabla.setSpacingAfter(18);
        return tabla;
    }

    private PdfPTable datosCliente(Venta venta) {
        Cliente cliente = venta.getCliente();

        PdfPTable tabla = new PdfPTable(new float[] { 1.3f, 3f, 1.3f, 2.4f });
        tabla.setWidthPercentage(100);

        tabla.addCell(etiqueta("Cliente:"));
        tabla.addCell(valor(cliente.getNombre() + " " + cliente.getApellido()));
        tabla.addCell(etiqueta("Fecha y hora:"));
        tabla.addCell(valor(venta.getFecha().format(FORMATO_FECHA)));

        tabla.addCell(etiqueta(cliente.getTipoDoc() + ":"));
        tabla.addCell(valor(cliente.getNumDoc()));
        tabla.addCell(etiqueta("Pago:"));
        tabla.addCell(valor("Tarjeta **** " + venta.getUltimos4()));

        tabla.setSpacingAfter(16);
        return tabla;
    }

    private PdfPTable detalle(Venta venta) throws DocumentException {
        PdfPTable tabla = new PdfPTable(new float[] { 4f, 1.4f, 1f, 1.6f, 1.6f });
        tabla.setWidthPercentage(100);
        tabla.setHeaderRows(1);

        for (String titulo : new String[] { "Producto", "Talla", "Cant.", "P. unitario", "Subtotal" }) {
            PdfPCell celda = new PdfPCell(new Phrase(titulo, cabecera));
            celda.setBackgroundColor(ROJO);
            celda.setPadding(6);
            celda.setBorderColor(ROJO);
            tabla.addCell(celda);
        }

        boolean alterna = false;
        for (DetalleVenta detalle : venta.getDetalles()) {
            Color fondo = alterna ? GRIS_CLARO : Color.WHITE;
            tabla.addCell(celdaDetalle(detalle.getProducto().getNombre(), Element.ALIGN_LEFT, fondo));
            tabla.addCell(celdaDetalle(detalle.getProducto().getTalla(), Element.ALIGN_LEFT, fondo));
            tabla.addCell(celdaDetalle(String.valueOf(detalle.getCantidad()), Element.ALIGN_RIGHT, fondo));
            tabla.addCell(celdaDetalle(soles(detalle.getPrecioUnitario()), Element.ALIGN_RIGHT, fondo));
            tabla.addCell(celdaDetalle(soles(detalle.getSubtotal()), Element.ALIGN_RIGHT, fondo));
            alterna = !alterna;
        }

        return tabla;
    }

    private PdfPTable totales(Venta venta) {
        PdfPTable tabla = new PdfPTable(new float[] { 6.4f, 1.6f, 1.6f });
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(10);

        tabla.addCell(sinBorde(new PdfPCell(new Phrase(""))));
        PdfPCell etiquetaTotal = sinBorde(new PdfPCell(new Phrase("TOTAL", total)));
        etiquetaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tabla.addCell(etiquetaTotal);
        PdfPCell montoTotal = sinBorde(new PdfPCell(new Phrase(soles(venta.getTotal()), total)));
        montoTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tabla.addCell(montoTotal);

        return tabla;
    }

    private PdfPCell etiqueta(String texto) {
        PdfPCell celda = sinBorde(new PdfPCell(new Phrase(texto, negrita)));
        celda.setPaddingBottom(5);
        return celda;
    }

    private PdfPCell valor(String texto) {
        PdfPCell celda = sinBorde(new PdfPCell(new Phrase(texto, normal)));
        celda.setPaddingBottom(5);
        return celda;
    }

    private PdfPCell celdaDetalle(String texto, int alineacion, Color fondo) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, normal));
        celda.setHorizontalAlignment(alineacion);
        celda.setBackgroundColor(fondo);
        celda.setBorderColor(new Color(0xD0, 0xD5, 0xDD));
        celda.setPadding(6);
        return celda;
    }

    private PdfPCell sinBorde(PdfPCell celda) {
        celda.setBorder(Rectangle.NO_BORDER);
        return celda;
    }

    private String soles(BigDecimal monto) {
        return "S/ " + String.format(Locale.US, "%,.2f", monto);
    }

    // Si el logo no está, la boleta se genera igual (solo sin imagen)
    private Image cargarLogo() {
        try (InputStream entrada = new ClassPathResource("boleta/logo_puntomarcial.jpg").getInputStream()) {
            return Image.getInstance(entrada.readAllBytes());
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }
}
