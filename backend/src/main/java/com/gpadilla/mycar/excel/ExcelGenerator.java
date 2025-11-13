package com.gpadilla.mycar.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Function;

@Component
public class ExcelGenerator {

    /**
     * Versión Excel equivalente a PdfGenerator.generarPdfEnMemoria(...)
     *
     * @param title           título del reporte (se escribe en la primera fila)
     * @param headers         encabezados de columnas
     * @param data            lista de datos
     * @param valueExtractors funciones para obtener cada columna desde T
     */
    public <T> byte[] generarExcelEnMemoria(
            String title,
            List<String> headers,
            List<T> data,
            List<Function<T, String>> valueExtractors
    ) {
        if (headers == null || headers.isEmpty()) {
            throw new IllegalArgumentException("La lista de encabezados no puede ser nula ni vacía");
        }
        if (valueExtractors == null || valueExtractors.isEmpty()) {
            throw new IllegalArgumentException("La lista de extractores de valores no puede ser nula ni vacía");
        }
        if (headers.size() != valueExtractors.size()) {
            throw new IllegalArgumentException("Debe haber la misma cantidad de encabezados que de extractores");
        }

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Nombre simple para la hoja
            String sheetName = (title != null && !title.isBlank())
                    ? (title.length() > 31 ? title.substring(0, 31) : title)
                    : "Datos";

            Sheet sheet = workbook.createSheet(sheetName);

            // Estilos simples
            CellStyle titleStyle = buildTitleStyle(workbook);
            CellStyle headerStyle = buildHeaderStyle(workbook);
            CellStyle cellStyle = buildCellStyle(workbook);

            int rowIndex = 0;

            // ===== Título (primera fila, celdas combinadas) =====
            Row titleRow = sheet.createRow(rowIndex++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(title != null ? title : "");
            titleCell.setCellStyle(titleStyle);

            // combinamos desde la col 0 hasta la última de headers
            // solo si hay 2 o más columnas
            if (headers.size() > 1) {
                sheet.addMergedRegion(new CellRangeAddress(
                        0, 0, 0, headers.size() - 1
                ));
            }

            // Fila vacía para separar
            rowIndex++;

            // ===== Encabezados =====
            Row headerRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }

            // ===== Filas de datos =====
            if (data != null) {
                for (T item : data) {
                    Row row = sheet.createRow(rowIndex++);
                    for (int col = 0; col < valueExtractors.size(); col++) {
                        String value = valueExtractors.get(col).apply(item);
                        Cell cell = row.createCell(col);
                        cell.setCellValue(value != null ? value : "");
                        cell.setCellStyle(cellStyle);
                    }
                }
            }

            // Ajustar ancho de columnas
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando el Excel: " + e.getMessage(), e);
        }
    }

    // ===== Estilos privados, simples =====

    private CellStyle buildTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle buildHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle buildCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}
