package com.gpadilla.mycar.pdf;

import com.gpadilla.mycar.dtos.alquiler.AlquilerDto;
import com.gpadilla.mycar.dtos.auto.AutoSummaryDto;
import com.gpadilla.mycar.dtos.cliente.ClienteSummaryDto;
import com.gpadilla.mycar.dtos.empresa.EmpresaDetailDto;
import com.gpadilla.mycar.dtos.factura.DetalleFacturaDto;
import com.gpadilla.mycar.dtos.factura.FacturaDto;
import com.gpadilla.mycar.dtos.promocion.PromocionViewDto;
import com.gpadilla.mycar.dtos.reportes.ReporteRecaudacionDto;
import com.gpadilla.mycar.dtos.reportes.ReporteVehiculosDto;
import com.gpadilla.mycar.entity.Empresa;
import com.gpadilla.mycar.service.EmpresaService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
@Component
public class PdfGenerator {

    @Autowired
    EmpresaService empresaService;

    public byte[] generarFacturaPdf(FacturaDto factura) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, out);

            //trae los datos de la entidad empresa
            EmpresaDetailDto empresa = empresaService.findSingletonDto();
            String calle = empresa.getDireccion().getCalle();
            String numeracion = empresa.getDireccion().getNumeracion();
            //String provincia = empresa.getDireccion().getProvinciaNombre(); el repositorio de direccion no trae la provincia
            String provincia = "Mendoza";
            String direccion = calle + " " + numeracion + ", " + provincia;
            //
            document.open();

            // ========================
            // 🔹 Encabezado principal
            // ========================
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 11);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

            Paragraph titulo = new Paragraph("AUTOMOTORA MYCAR", titleFont);
            titulo.setAlignment(Element.ALIGN_LEFT);
            document.add(titulo);

            document.add(new Paragraph("CUIT: 30-12345678-9", normalFont));
            document.add(new Paragraph("Direccion: " + direccion, normalFont));
            //document.add(new Paragraph("Dirección: Av. Las Heras 450, Mendoza", normalFont));
            document.add(new Paragraph("Teléfono: +54 261 555-1234", normalFont));
            document.add(Chunk.NEWLINE);

            // ========================
            // 🔹 Cabecera factura
            // ========================
            PdfPTable cabecera = new PdfPTable(2);
            cabecera.setWidthPercentage(100);
            cabecera.addCell(getCell("Factura Nº: " + factura.getNumeroFactura(), PdfPCell.ALIGN_LEFT, boldFont));
            cabecera.addCell(getCell("Fecha: " + factura.getFechaFactura().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), PdfPCell.ALIGN_RIGHT, normalFont));

            String formaPago = (factura.getFormaDePago() != null && factura.getFormaDePago().getTipoDePago() != null)
                    ? factura.getFormaDePago().getTipoDePago()
                    : "N/A";

            cabecera.addCell(getCell("Forma de Pago: " + formaPago, PdfPCell.ALIGN_LEFT, normalFont));
            cabecera.addCell(getCell("Estado: " + factura.getEstado(), PdfPCell.ALIGN_RIGHT, normalFont));
            document.add(cabecera);
            document.add(Chunk.NEWLINE);

            // ========================
            // 🔹 Datos del cliente
            // ========================
            if (factura.getDetalles() != null && !factura.getDetalles().isEmpty()) {
                DetalleFacturaDto detalle = factura.getDetalles().get(0);
                AlquilerDto alquiler = detalle.getAlquiler();
                ClienteSummaryDto cliente = alquiler.getCliente();

                Paragraph clienteTitulo = new Paragraph("Datos del Cliente", boldFont);
                clienteTitulo.setSpacingBefore(10);
                document.add(clienteTitulo);

                PdfPTable clienteTable = new PdfPTable(2);
                clienteTable.setWidthPercentage(100);
                clienteTable.addCell(getCell("Nombre: " + cliente.getNombre() + " " + cliente.getApellido(), PdfPCell.ALIGN_LEFT, normalFont));
                clienteTable.addCell(getCell("Documento: " + cliente.getTipoDocumento() + " " + cliente.getNumeroDocumento(), PdfPCell.ALIGN_LEFT, normalFont));
                clienteTable.addCell(getCell("Email: " + cliente.getUsuarioEmail(), PdfPCell.ALIGN_LEFT, normalFont));
                clienteTable.addCell(getCell("Nacionalidad: " + cliente.getNacionalidadNombre(), PdfPCell.ALIGN_LEFT, normalFont));
                document.add(clienteTable);
                document.add(Chunk.NEWLINE);
            }

            // ========================
            // 🔹 Tabla de detalles
            // ========================
            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{3, 2, 2, 2, 2});

            tabla.addCell(getHeaderCell("Auto"));
            tabla.addCell(getHeaderCell("Periodo"));
            tabla.addCell(getHeaderCell("Subtotal"));
            tabla.addCell(getHeaderCell("Promoción"));
            tabla.addCell(getHeaderCell("Total"));

            for (DetalleFacturaDto det : factura.getDetalles()) {
                AlquilerDto alquiler = det.getAlquiler();
                AutoSummaryDto auto = alquiler.getAuto();
                PromocionViewDto promo = det.getPromocion();

                String descripcion = auto.getMarca() + " " + auto.getModelo() + " (" + auto.getPatente() + ")";
                String periodo = alquiler.getFechaDesde().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                        " a " + alquiler.getFechaHasta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                double subtotal = det.getSubtotal() != null ? det.getSubtotal() : 0.0;
                String promoDesc = (promo != null)
                        ? promo.getCodigoDescuento() + " (" + promo.getPorcentajeDescuento() + "%)"
                        : "-";

                tabla.addCell(getCell(descripcion, PdfPCell.ALIGN_LEFT, normalFont));
                tabla.addCell(getCell(periodo, PdfPCell.ALIGN_CENTER, normalFont));
                tabla.addCell(getCell(String.format("$ %.2f", subtotal), PdfPCell.ALIGN_RIGHT, normalFont));
                tabla.addCell(getCell(promoDesc, PdfPCell.ALIGN_CENTER, normalFont));

                double totalConDesc = subtotal;
                if (promo != null && promo.getPorcentajeDescuento() != null) {
                    totalConDesc = subtotal * (1 - promo.getPorcentajeDescuento() / 100.0);
                }
                tabla.addCell(getCell(String.format("$ %.2f", totalConDesc), PdfPCell.ALIGN_RIGHT, boldFont));
            }

            document.add(tabla);
            document.add(Chunk.NEWLINE);

            // ========================
            // 🔹 Total final
            // ========================
            Paragraph total = new Paragraph(
                    "Total Pagado: $" + String.format("%.2f", factura.getTotalPagado()),
                    new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD)
            );
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("¡Gracias por elegir MYCAR!", new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC)));

            document.close();
            return out.toByteArray(); // ✅ retorna PDF en memoria

        } catch (Exception e) {
            log.error("Error generando factura PDF", e);
            throw new RuntimeException("Error generando PDF de factura", e);
        }
    }



    public byte[] generarReporteVehiculosAlquilados(LocalDate fechaInicio, LocalDate fechaFin, List<ReporteVehiculosDto> vehiculos) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate()); // horizontal
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 10);

            // 🔹 Título
            document.add(new Paragraph(
                    String.format("Reporte de Vehículos Alquilados (%s a %s)", fechaInicio, fechaFin),
                    titleFont
            ));
            document.add(Chunk.NEWLINE);

            // 🔹 Tabla
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            Stream.of("Modelo", "Patente", "Cliente", "Desde", "Hasta", "Días", "Monto Total")
                    .forEach(header -> {
                        PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        table.addCell(cell);
                    });

            for (ReporteVehiculosDto dto : vehiculos) {
                table.addCell(new Phrase(dto.getModelo(), textFont));
                table.addCell(new Phrase(dto.getPatente(), textFont));
                table.addCell(new Phrase(dto.getClienteNombre(), textFont));
                table.addCell(new Phrase(dto.getFechaDesde().toString(), textFont));
                table.addCell(new Phrase(dto.getFechaHasta().toString(), textFont));
                table.addCell(new Phrase(String.valueOf(dto.getDiasAlquilado()), textFont));
                table.addCell(new Phrase(String.format("$ %.2f", dto.getMonto()), textFont));            }

            document.add(table);

            // 🔹 Total general
            double total = vehiculos.stream().mapToDouble(v -> v.getMonto() != null ? v.getMonto() : 0).sum();
            Paragraph totalParagraph = new Paragraph(
                    String.format("\nTotal Recaudado: $ %.2f", total),
                    new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)
            );
            document.add(totalParagraph);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return out.toByteArray();
    }


    public byte[] generarReporteRecaudacionConDetalle(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            List<ReporteRecaudacionDto> cerrados,
            List<ReporteRecaudacionDto> abiertos,
            double totalCerrados,
            double totalAbiertos
    ) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font cellFont = new Font(Font.FontFamily.HELVETICA, 10);

            Paragraph title = new Paragraph(
                    String.format("Reporte de Recaudación (%s a %s)", fechaInicio, fechaFin),
                    titleFont
            );
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Sección CERRADOS
            document.add(new Paragraph("Alquileres Cerrados", headerFont));
            PdfPTable tableCerrados = buildRecaudacionTable(cerrados, cellFont);
            document.add(tableCerrados);
            document.add(new Paragraph(String.format("Total: $ %.2f", totalCerrados), headerFont));
            document.add(Chunk.NEWLINE);

            // Sección ABIERTOS
            document.add(new Paragraph("Alquileres Abiertos (parciales en el rango)", headerFont));
            PdfPTable tableAbiertos = buildRecaudacionTable(abiertos, cellFont);
            document.add(tableAbiertos);
            document.add(new Paragraph(String.format("Total: $ %.2f", totalAbiertos), headerFont));

            // === TOTAL GENERAL ===
            Paragraph totalGeneralTitle = new Paragraph("\nTotal General", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
            totalGeneralTitle.setSpacingBefore(10f);
            document.add(totalGeneralTitle);

            double totalGeneral = totalCerrados + totalAbiertos;

            Paragraph totalGeneralValue = new Paragraph(
                    String.format("Recaudación Total (cerrados + abiertos): $ %.2f", totalGeneral),
                    new Font(Font.FontFamily.HELVETICA, 12)
            );
            totalGeneralValue.setSpacingBefore(5f);
            document.add(totalGeneralValue);

            document.close();
            return out.toByteArray();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    private PdfPTable buildRecaudacionTable(List<ReporteRecaudacionDto> data, Font font) {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        Stream.of("Modelo", "Cant. Cerrados", "Total Cerrados", "Total Abiertos", "Total General").forEach(header -> {
            PdfPCell cell = new PdfPCell(new Phrase(header, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        });

        for (ReporteRecaudacionDto dto : data) {
            table.addCell(new Phrase(dto.getModelo(), font));
            table.addCell(new Phrase(String.valueOf(dto.getCantidadCerrados()), font));
            table.addCell(new Phrase(String.format("$ %.2f", dto.getTotalCerrados()), font));
            table.addCell(new Phrase(String.format("$ %.2f", dto.getTotalAbiertos()), font));
            table.addCell(new Phrase(String.format("$ %.2f", dto.getTotalGeneral()), font));
        }

        return table;
    }



    public <T> byte[] generarPdfEnMemoria(
            String title,
            List<String> headers,
            List<T> data,
            List<Function<T, String>> valueExtractors
    ) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, out);

            doc.open();
            Paragraph titulo = new Paragraph(title, new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);
            doc.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(100);
            addHeader(table, headers);
            addRows(table, data, valueExtractors);
            doc.add(table);

            doc.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando el PDF: " + e.getMessage(), e);
        }
    }

    private void addHeader(PdfPTable table, List<String> headers) {
        Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        for (String header : headers) {
            PdfPCell hcell = new PdfPCell(new Phrase(header, headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);
        }
    }

    private PdfPCell getHeaderCell(String text) {
        Font font = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    private PdfPCell getCell(String text, int alignment, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "-", font));
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private <T> void addRows(PdfPTable table, List<T> data, List<Function<T, String>> valueExtractors) {
        Font rowFont = new Font(Font.FontFamily.HELVETICA, 11);
        for (T item : data) {
            for (Function<T, String> extractor : valueExtractors) {
                PdfPCell cell = new PdfPCell(new Phrase(extractor.apply(item), rowFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
        }
    }
}

