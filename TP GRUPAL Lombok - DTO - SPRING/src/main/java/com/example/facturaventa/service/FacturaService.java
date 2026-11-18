package com.example.facturaventa.service;

import com.example.facturaventa.dto.FacturaReporteDTO;
import com.example.facturaventa.exception.ReglaNegocioException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/** Servicio de lectura de facturas: cumple el TP Spring (JPQL, DTO y filtros). */
@Service
@RequiredArgsConstructor
public class FacturaService {
    @PersistenceContext private EntityManager em;

    @Transactional(readOnly = true)
    public List<FacturaReporteDTO> buscarFacturasFiltradas(LocalDate desde, LocalDate hasta,
                                                           String estado, Double montoMinimo) {
        if (desde != null && hasta != null && desde.isAfter(hasta))
            throw new ReglaNegocioException("fechaDesde debe ser anterior o igual a fechaHasta");
        if (montoMinimo != null && (!Double.isFinite(montoMinimo) || montoMinimo < 0))
            throw new ReglaNegocioException("montoMinimo no puede ser negativo");

        StringBuilder jpql = new StringBuilder(
            "SELECT new com.example.facturaventa.dto.FacturaReporteDTO(" +
            "f.numero, f.fechaEmision, COALESCE(c.denominacion, 'Consumidor Final'), " +
            "ci.denominacion, pv.descripcion, f.importeTotal, COUNT(d)) " +
            "FROM FacturaVenta f " +
            "LEFT JOIN f.cliente c " +
            "JOIN f.condicionIva ci " +
            "JOIN f.puntoVenta pv " +
            "LEFT JOIN f.detalles d WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();
        if (desde != null) {
            jpql.append("AND f.fechaEmision >= :desde ");
            params.put("desde", Date.valueOf(desde));
        }
        if (hasta != null) {
            jpql.append("AND f.fechaEmision <= :hasta ");
            params.put("hasta", Date.valueOf(hasta));
        }
        if (estado != null && !estado.isBlank()) {
            String normal = estado.trim().toUpperCase(Locale.ROOT);
            if (!Set.of("EMITIDA", "ANULADA").contains(normal))
                throw new ReglaNegocioException("Estado inválido; usar EMITIDA o ANULADA");
            jpql.append("AND f.estado = :estado ");
            params.put("estado", normal);
        }
        if (montoMinimo != null) {
            jpql.append("AND f.importeTotal >= :monto ");
            params.put("monto", montoMinimo);
        }
        jpql.append("GROUP BY f.id, f.estado, f.numero, f.fechaEmision, " +
                    "c.denominacion, ci.denominacion, pv.descripcion, f.importeTotal " +
                    "ORDER BY f.fechaEmision DESC, f.id DESC");
        TypedQuery<FacturaReporteDTO> query = em.createQuery(jpql.toString(), FacturaReporteDTO.class);
        params.forEach(query::setParameter);
        return query.getResultList();
    }
}
