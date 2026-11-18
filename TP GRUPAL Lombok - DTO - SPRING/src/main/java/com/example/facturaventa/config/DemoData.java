package com.example.facturaventa.config;

import com.example.facturaventa.model.*;
import com.example.facturaventa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

/** Dos facturas de ejemplo cargadas sin requerir endpoints de escritura. */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name="app.demo-data.enabled", havingValue="true")
public class DemoData implements CommandLineRunner {
    private final ClienteRepository clientes;
    private final PuntoVentaRepository puntos;
    private final CondicionIvaRepository condiciones;
    private final TipoMonedaRepository monedas;
    private final ArticuloRepository articulos;
    private final ListaPrecioArticuloRepository precios;
    private final FacturaVentaRepository facturas;

    @Override @Transactional
    public void run(String... args) {
        if (puntos.count() != 0) return;
        PuntoVenta pv = puntos.save(new PuntoVenta("Sucursal Central"));
        CondicionIva iva = condiciones.save(new CondicionIva("IVA Responsable Inscripto"));
        CondicionIva cf = condiciones.save(new CondicionIva("Consumidor Final"));
        TipoMoneda ars = monedas.save(new TipoMoneda("ARS", "Pesos argentinos"));
        Cliente empresa = clientes.save(new Cliente("Empresa Demo S.A.", "30-12345678-9"));
        Articulo agua = articulos.save(new Articulo("Bidón de agua", new BigDecimal("21.00")));
        Articulo soda = articulos.save(new Articulo("Soda", new BigDecimal("21.00")));
        Articulo vasos = articulos.save(new Articulo("Vasos descartables", new BigDecimal("10.50")));
        ListaPrecioArticulo pa = precios.save(new ListaPrecioArticulo(agua, new BigDecimal("1000.00")));
        ListaPrecioArticulo ps = precios.save(new ListaPrecioArticulo(soda, new BigDecimal("600.00")));
        ListaPrecioArticulo pvVasos = precios.save(new ListaPrecioArticulo(vasos, new BigDecimal("125.00")));

        FacturaVenta f1 = base(LocalDate.now(), pv, cf, ars, null);
        agregar(f1, agua, pa, "2", "2000", "420", "2420");
        agregar(f1, soda, ps, "1", "600", "126", "726");
        f1.setImporteNeto(2600d);
        f1.setImporteIva(546d);
        f1.setImporteTotal(3146d);
        f1.setImporteCobrado(0d);
        f1.setImporteSaldo(3146d);
        facturas.saveAndFlush(f1);
        f1.setNumero(1000L + f1.getId());

        FacturaVenta f2 = base(LocalDate.now().minusDays(1), pv, iva, ars, empresa);
        agregar(f2, vasos, pvVasos, "4", "500", "52.50", "552.50");
        f2.setImporteNeto(500d);
        f2.setImporteIva(52.5d);
        f2.setImporteTotal(552.5d);
        f2.setImporteCobrado(250d);
        f2.setImporteSaldo(302.5d);
        facturas.saveAndFlush(f2);
        f2.setNumero(1000L + f2.getId());
    }

    private FacturaVenta base(LocalDate fecha, PuntoVenta pv, CondicionIva ci,
                              TipoMoneda moneda, Cliente cliente) {
        FacturaVenta f = new FacturaVenta();
        f.setFechaEmision(Date.valueOf(fecha));
        f.setPuntoVenta(pv);
        f.setCondicionIva(ci);
        f.setTipoMoneda(moneda);
        f.setCliente(cliente);
        f.setEstado("EMITIDA");
        return f;
    }

    private void agregar(FacturaVenta f, Articulo art, ListaPrecioArticulo precio,
                         String cantidad, String neto, String impuesto, String total) {
        FacturaVentaDetalle d = new FacturaVentaDetalle();
        d.setArticulo(art);
        d.setListaPrecioArticulo(precio);
        d.setCantidad(new BigDecimal(cantidad));
        d.setPrecioUnitarioNeto(precio.getPrecioNeto());
        d.setAlicuotaIva(art.getAlicuotaIva());
        d.setImporteNeto(new BigDecimal(neto));
        d.setImporteIva(new BigDecimal(impuesto));
        d.setImporteTotal(new BigDecimal(total));
        f.agregarDetalle(d);
    }
}
