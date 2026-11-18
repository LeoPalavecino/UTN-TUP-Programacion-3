package com.example.facturaventa.controller;

import com.example.facturaventa.dto.FacturaReporteDTO;
import com.example.facturaventa.service.FacturaService;
import com.example.facturaventa.service.ReporteExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/** GET JSON filtrado + exportaciones PDF y Excel del TP de Spring. */
@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaRestController {
    private final FacturaService facturas;
    private final ReporteExportService exportaciones;

    @GetMapping
    public List<FacturaReporteDTO> listar(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
        @RequestParam(required = false) String estado,
        @RequestParam(required = false) Double montoMinimo) {
        return facturas.buscarFacturasFiltradas(fechaDesde, fechaHasta, estado, montoMinimo);
    }

    @GetMapping(value = "/reporte.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> descargarPdf(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
        @RequestParam(required = false) String estado,
        @RequestParam(required = false) Double montoMinimo) {
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte-facturas.pdf")
            .body(exportaciones.pdf(fechaDesde, fechaHasta, estado, montoMinimo));
    }

    @GetMapping(value = "/reporte.xlsx", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> descargarExcel(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
        @RequestParam(required = false) String estado,
        @RequestParam(required = false) Double montoMinimo) {
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte-facturas.xlsx")
            .body(exportaciones.excel(fechaDesde, fechaHasta, estado, montoMinimo));
    }
}
