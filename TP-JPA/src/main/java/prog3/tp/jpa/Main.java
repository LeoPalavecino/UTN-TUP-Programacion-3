/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package prog3.tp.jpa;

import prog3.tp.jpa.dominio.*;

/**
 *
 * @author leopala
 */
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        // Inicialización de la fábrica y el gestor de entidades JPA
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("FacturacionPU");
        EntityManager em = emf.createEntityManager();

        try {
            // Iniciar la transacción
            em.getTransaction().begin();

            Date ahora = new Date();

            // 1. Instanciar y persistir el Usuario de auditoría
            Usuario admin = new Usuario();
            admin.setUsuario("admin");
            admin.setClave("123456");
            admin.setNombre("Carlos");
            admin.setApellido("Perez");
            em.persist(admin);

            // 2. Instanciar y persistir PuntoVenta
            PuntoVenta pv = new PuntoVenta();
            pv.setNumero(1);
            pv.setDescripcion("Sucursal Central");
            pv.setTipoEmision("Electronica");
            pv.setDomicilioComercial("Av. San Martín 1050");
            pv.setFechaAlta(ahora);
            pv.setFechaModificacion(ahora);
            pv.setUsuarioCarga(admin);
            pv.setUsuarioModificacion(admin);
            em.persist(pv);

            // 3. Instanciar y persistir Rubro y Marca
            Rubro computacion = new Rubro();
            computacion.setCodigo(10);
            computacion.setDenominacion("Computación");
            computacion.setFechaAlta(ahora);
            computacion.setFechaModificacion(ahora);
            computacion.setUsuarioCarga(admin);
            computacion.setUsuarioModificacion(admin);
            em.persist(computacion);

            Marca dell = new Marca();
            dell.setCodigo(501);
            dell.setDenominacion("Dell");
            dell.setFechaAlta(ahora);
            dell.setFechaModificacion(ahora);
            dell.setUsuarioCarga(admin);
            dell.setUsuarioModificacion(admin);
            em.persist(dell);

            // 4. Instanciar y persistir Artículo
            Articulo notebook = new Articulo();
            notebook.setCodigo("ART-001");
            notebook.setDenominacion("Notebook Dell XPS 13");
            notebook.setRubro(computacion);
            notebook.setMarca(dell);
            notebook.setFechaAlta(ahora);
            notebook.setFechaModificacion(ahora);
            notebook.setUsuarioCarga(admin);
            notebook.setUsuarioModificacion(admin);
            em.persist(notebook);

            // 5. Instanciar y persistir ListaPrecio y ListaPrecioArticulo
            ListaPrecio listaGeneral = new ListaPrecio();
            listaGeneral.setCodigo("LST-01");
            listaGeneral.setDenominacion("Precios Minoristas");
            listaGeneral.setFechaAlta(ahora);
            listaGeneral.setFechaModificacion(ahora);
            listaGeneral.setUsuarioCarga(admin);
            listaGeneral.setUsuarioModificacion(admin);
            em.persist(listaGeneral);

            ListaPrecioArticulo lpa = new ListaPrecioArticulo();
            lpa.setArticulo(notebook);
            lpa.setListaPrecio(listaGeneral);
            lpa.setPrecioVenta(1500000.0);
            lpa.setFechaAlta(ahora);
            lpa.setFechaModificacion(ahora);
            lpa.setUsuarioCarga(admin);
            lpa.setUsuarioModificacion(admin);
            em.persist(lpa);

            // 6. Instanciar FacturaVenta (Cabecera)
            FacturaVenta factura = new FacturaVenta();
            factura.setNumero(1001L);
            factura.setFechaEmision(ahora);
            factura.setPuntoVenta(pv);
            factura.setEstado("EMITIDA");
            factura.setFechaAlta(ahora);
            factura.setFechaModificacion(ahora);
            factura.setUsuarioCarga(admin);
            factura.setUsuarioModificacion(admin);

            // 7. Instanciar los ítems (FacturaVentaDetalle)
            FacturaVentaDetalle det1 = new FacturaVentaDetalle();
            det1.setListaPrecioArticulo(lpa);
            det1.setDescripcion("Notebook Dell XPS 13 i7");
            det1.setCantidad(2.0);
            det1.setPrecioUnitario(1500000.0);
            det1.setPorcentajeBonificacion(0.0);
            det1.setImporteNeto(3000000.0);
            det1.setImporteIva(630000.0);
            det1.setImporteSubtotal(3630000.0);

            // Asociación bidireccional mediante el método de conveniencia
            factura.agregarDetalle(det1);
            factura.setImporteTotal(det1.getImporteSubtotal());

            // 8. Requisito Clave: Persistir ÚNICAMENTE la cabecera FacturaVenta
            // Gracias a cascade = CascadeType.ALL, 'det1' se insertará automáticamente en PostgreSQL
            em.persist(factura);

            // Confirmar transacción
            em.getTransaction().commit();

            System.out.println("--------------------------------------------------");
            System.out.println("Factura persistida con éxito. ID: " + factura.getId());
            System.out.println("Detalle persistido por cascada. ID: " + det1.getId());
            System.out.println("--------------------------------------------------");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            // Cierre de recursos
            em.close();
            emf.close();
        }
    }
}