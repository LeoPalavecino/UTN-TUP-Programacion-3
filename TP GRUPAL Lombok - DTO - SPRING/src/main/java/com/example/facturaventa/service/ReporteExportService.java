package com.example.facturaventa.service;

import com.example.facturaventa.dto.FacturaReporteDTO;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Chunk;
import com.lowagie.text.Phrase;
import com.lowagie.text.FontFactory;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.*;
import java.text.*;
import java.util.*;

@Service @RequiredArgsConstructor
public class ReporteExportService {
    private final FacturaService facturas;
    private static final String[] COLUMNAS={"N.º factura","Fecha","Cliente","Condición IVA","Punto de venta","Total","Ítems"};

    public byte[] pdf(java.time.LocalDate desde, java.time.LocalDate hasta, String estado, Double minimo) {
        List<FacturaReporteDTO> filas=facturas.buscarFacturasFiltradas(desde,hasta,estado,minimo);
        try (ByteArrayOutputStream out=new ByteArrayOutputStream()) {
            Document documento=new Document(PageSize.A4.rotate(),32,32,30,30);
            PdfWriter.getInstance(documento,out);
            documento.open();
            documento.add(new Paragraph("Reporte ejecutivo de facturas de venta",FontFactory.getFont(FontFactory.HELVETICA_BOLD,17)));
            documento.add(new Paragraph("Fecha de generación: "+java.time.LocalDate.now()+"  |  Facturas: "+filas.size(),FontFactory.getFont(FontFactory.HELVETICA,10)));
            documento.add(Chunk.NEWLINE);
            PdfPTable tabla=new PdfPTable(new float[]{1.0f,1.05f,2.25f,1.9f,1.7f,1.15f,.6f});
            tabla.setWidthPercentage(100);
            for(String titulo:COLUMNAS) {
                PdfPCell cell=new PdfPCell(new Phrase(titulo,FontFactory.getFont(FontFactory.HELVETICA_BOLD,9)));
                cell.setBackgroundColor(new java.awt.Color(224,236,248)); cell.setPadding(7); tabla.addCell(cell);
            }
            for(FacturaReporteDTO f:filas) {
                String[] datos={String.valueOf(f.getNumeroFactura()),fecha(f.getFechaEmision()),f.getClienteDenominacion(),
                    f.getCondicionIva(),f.getPuntoVentaDescripcion(),dinero(f.getImporteTotal()),String.valueOf(f.getCantidadItems())};
                for(String dato:datos) {
                    PdfPCell cell=new PdfPCell(new Phrase(dato,FontFactory.getFont(FontFactory.HELVETICA,9)));
                    cell.setPadding(6); tabla.addCell(cell);
                }
            }
            documento.add(tabla);
            documento.add(Chunk.NEWLINE);
            documento.add(new Paragraph("TOTAL GENERAL: "+dinero(filas.stream().mapToDouble(FacturaReporteDTO::getImporteTotal).sum()),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD,11)));
            documento.close();
            return out.toByteArray();
        } catch (DocumentException | IOException ex) {throw new IllegalStateException("No se pudo generar el PDF",ex);}
    }

    public byte[] excel(java.time.LocalDate desde, java.time.LocalDate hasta, String estado, Double minimo) {
        List<FacturaReporteDTO> filas=facturas.buscarFacturasFiltradas(desde,hasta,estado,minimo);
        try (XSSFWorkbook libro=new XSSFWorkbook(); ByteArrayOutputStream out=new ByteArrayOutputStream()) {
            Sheet hoja=libro.createSheet("Ventas");
            CellStyle cabecera=libro.createCellStyle();
            Font negrita=libro.createFont();negrita.setBold(true);cabecera.setFont(negrita);
            cabecera.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            cabecera.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            CellStyle fecha=libro.createCellStyle();fecha.setDataFormat(libro.getCreationHelper().createDataFormat().getFormat("dd/mm/yyyy"));
            CellStyle moneda=libro.createCellStyle();moneda.setDataFormat(libro.getCreationHelper().createDataFormat().getFormat("#,##0.00"));
            Row titulo=hoja.createRow(0);
            for(int i=0;i<COLUMNAS.length;i++){Cell c=titulo.createCell(i);c.setCellValue(COLUMNAS[i]);c.setCellStyle(cabecera);}
            int n=1;
            for(FacturaReporteDTO f:filas) {
                Row fila=hoja.createRow(n++);
                fila.createCell(0).setCellValue(f.getNumeroFactura());
                Cell cFecha=fila.createCell(1);cFecha.setCellValue(f.getFechaEmision());cFecha.setCellStyle(fecha);
                fila.createCell(2).setCellValue(f.getClienteDenominacion());
                fila.createCell(3).setCellValue(f.getCondicionIva());
                fila.createCell(4).setCellValue(f.getPuntoVentaDescripcion());
                Cell cTotal=fila.createCell(5);cTotal.setCellValue(f.getImporteTotal());cTotal.setCellStyle(moneda);
                fila.createCell(6).setCellValue(f.getCantidadItems());
            }
            Row total=hoja.createRow(n);
            Cell texto=total.createCell(4);texto.setCellValue("TOTAL GENERAL");texto.setCellStyle(cabecera);
            Cell importe=total.createCell(5);
            if(!filas.isEmpty()) importe.setCellFormula("SUM(F2:F"+n+")");
            else importe.setCellValue(0d);
            importe.setCellStyle(moneda);
            int[] anchuras={15,16,34,31,27,18,12};
            for(int i=0;i<anchuras.length;i++)hoja.setColumnWidth(i,anchuras[i]*256);
            hoja.createFreezePane(0,1);
            hoja.setAutoFilter(new org.apache.poi.ss.util.CellRangeAddress(0,Math.max(1,n-1),0,6));
            libro.write(out);
            return out.toByteArray();
        } catch(IOException ex){throw new IllegalStateException("No se pudo generar Excel",ex);}
    }
    private String fecha(java.util.Date d){return new SimpleDateFormat("dd/MM/yyyy").format(d);}
    private String dinero(double v){return NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR")).format(v);}
}
