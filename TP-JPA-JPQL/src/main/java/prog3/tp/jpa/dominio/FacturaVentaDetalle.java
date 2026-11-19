
package prog3.tp.jpa.dominio;

/**
 * 
 * @author leopala
 * 
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "facturas_venta_detalles", schema = "contable")
public class FacturaVentaDetalle extends EntityId {

    @ManyToOne
    @JoinColumn(name = "id_factura", nullable = false)
    private FacturaVenta factura;

    @ManyToOne
    @JoinColumn(name = "id_lista_precio_articulo", nullable = false)
    private ListaPrecioArticulo listaPrecioArticulo;

    private String descripcion;

    @Column(nullable = false)
    private double cantidad;

    @Column(nullable = false)
    private double precioUnitario;

    private double porcentajeBonificacion;
    private double importeNeto;
    private double importeIva;

    @Column(nullable = false)
    private double importeSubtotal;

    public FacturaVentaDetalle() {}

    // Getters y Setters
    public FacturaVenta getFactura() { return factura; }
    public void setFactura(FacturaVenta factura) { this.factura = factura; }

    public ListaPrecioArticulo getListaPrecioArticulo() { return listaPrecioArticulo; }
    public void setListaPrecioArticulo(ListaPrecioArticulo listaPrecioArticulo) { this.listaPrecioArticulo = listaPrecioArticulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getPorcentajeBonificacion() { return porcentajeBonificacion; }
    public void setPorcentajeBonificacion(double porcentajeBonificacion) { this.porcentajeBonificacion = porcentajeBonificacion; }

    public double getImporteNeto() { return importeNeto; }
    public void setImporteNeto(double importeNeto) { this.importeNeto = importeNeto; }

    public double getImporteIva() { return importeIva; }
    public void setImporteIva(double importeIva) { this.importeIva = importeIva; }

    public double getImporteSubtotal() { return importeSubtotal; }
    public void setImporteSubtotal(double importeSubtotal) { this.importeSubtotal = importeSubtotal; }
}
