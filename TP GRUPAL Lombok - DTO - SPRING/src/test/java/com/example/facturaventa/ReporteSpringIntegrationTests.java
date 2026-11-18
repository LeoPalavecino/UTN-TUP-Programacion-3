package com.example.facturaventa;

import com.example.facturaventa.dto.FacturaReporteDTO;
import com.example.facturaventa.exception.ReglaNegocioException;
import com.example.facturaventa.service.FacturaService;
import com.example.facturaventa.service.ReporteExportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties={
    "spring.datasource.url=jdbc:h2:mem:reportes;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "app.demo-data.enabled=true"
})
class ReporteSpringIntegrationTests {
    @Autowired FacturaService facturas;
    @Autowired ReporteExportService export;

    @Test void consultaJPQLDTO() {
        List<FacturaReporteDTO> reporte=facturas.buscarFacturasFiltradas(null,null,null,null);
        assertEquals(2,reporte.size());
        assertTrue(reporte.stream().anyMatch(f->"Consumidor Final".equals(f.getClienteDenominacion())));
        assertTrue(reporte.stream().anyMatch(f->f.getCantidadItems()==2));
    }
    @Test void filtrosDeMontoFechaEstado() {
        assertEquals(1,facturas.buscarFacturasFiltradas(null,null,"EMITIDA",1000d).size());
        assertTrue(facturas.buscarFacturasFiltradas(LocalDate.now().plusDays(10),null,null,null).isEmpty());
        assertThrows(ReglaNegocioException.class,()->facturas.buscarFacturasFiltradas(
            LocalDate.now(), LocalDate.now().minusDays(1),null,null));
    }
    @Test void reportesPDFyExcel() {
        byte[] pdf=export.pdf(null,null,null,null);
        byte[] xlsx=export.excel(null,null,null,null);
        assertTrue(pdf.length>1000);
        assertEquals("%PDF",new String(pdf,0,4,StandardCharsets.US_ASCII));
        assertTrue(xlsx.length>1000);
        assertEquals('P',xlsx[0]); assertEquals('K',xlsx[1]);
    }
}
