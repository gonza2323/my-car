package com.gpadilla.mycar.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Function;

@Component
public class PdfGenerator {

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

