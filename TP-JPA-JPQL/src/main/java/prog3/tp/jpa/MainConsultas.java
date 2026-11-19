
package prog3.tp.jpa;

import prog3.tp.jpa.dominio.*;

/**
 * 
 * @author leopala
 * 
 */

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainConsultas {

    public static void main(String[] args) {
        EntityManagerFactory emf = 
            Persistence.createEntityManagerFactory("FacturacionPU");
        EntityManager em = emf.createEntityManager();

        try {
            System.out.println("==========================================");
            System.out.println("EJECUCIÓN DE CONSULTAS JPQL");
            System.out.println("==========================================\n");

            // ==========================================
            // NIVEL 1
            // ==========================================
            System.out.println("--- 1. Lista completa de facturas ---");
            List<FacturaVenta> c1 = em.createQuery(
                "SELECT f FROM FacturaVenta f", FacturaVenta.class)
                .getResultList();
            c1.forEach(f -> System.out.println("Factura N°: " + f.getNumero() 
                + " | Total: $" + f.getImporteTotal()));

            System.out.println("\n--- 2. Proyección de atributos (número, fecha, total) ---");
            List<Object[]> c2 = em.createQuery(
                "SELECT f.numero, f.fechaEmision, f.importeTotal FROM FacturaVenta f", 
                Object[].class)
                .getResultList();
            for (Object[] fila : c2) {
                System.out.println("N°: " + fila[0] 
                    + " | Fecha: " + fila[1] 
                    + " | Total: $" + fila[2]);
            }

            System.out.println("\n--- 3. Artículos de rubro 'Computación' ---");
            List<Articulo> c3 = em.createQuery(
                "SELECT a FROM Articulo a WHERE a.rubro.denominacion = :denominacion", 
                Articulo.class)
                .setParameter("denominacion", "Computación")
                .getResultList();
            c3.forEach(a -> System.out.println("Articulo: " + a.getDenominacion() 
                + " (" + a.getCodigo() + ")"));

            System.out.println("\n--- 4. Facturas en rango de fechas ---");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date fechaInicio = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 2);
            Date fechaFin = cal.getTime();

            List<FacturaVenta> c4 = em.createQuery(
                "SELECT f FROM FacturaVenta f "
                + "WHERE f.fechaEmision BETWEEN :inicio AND :fin", 
                FacturaVenta.class)
                .setParameter("inicio", fechaInicio)
                .setParameter("fin", fechaFin)
                .getResultList();
            c4.forEach(f -> System.out.println("Factura N°: " + f.getNumero()));

            // ==========================================
            // NIVEL 2
            // ==========================================
            System.out.println("\n--- 5. Facturas EMITIDAS > $10.000 y no anuladas ---");
            List<FacturaVenta> c5 = em.createQuery(
                "SELECT f FROM FacturaVenta f "
                + "WHERE f.estado = :estado "
                + "AND f.importeTotal > :importe "
                + "AND f.fechaAnulacion IS NULL", FacturaVenta.class)
                .setParameter("estado", "EMITIDA")
                .setParameter("importe", 10000.0)
                .getResultList();
            c5.forEach(f -> System.out.println("Factura N°: " + f.getNumero() 
                + " | Total: $" + f.getImporteTotal()));

            System.out.println("\n--- 6. Búsqueda Clientes (denominación o CUIT '20-') ---");
            List<Cliente> c6 = em.createQuery(
                "SELECT c FROM Cliente c "
                + "WHERE LOWER(c.denominacion) LIKE :texto "
                + "OR c.cuitCuil LIKE :prefijo", Cliente.class)
                .setParameter("texto", "%a%")
                .setParameter("prefijo", "20-%")
                .getResultList();
            c6.forEach(c -> System.out.println("Cliente: " + c.getDenominacion()));

            System.out.println("\n--- 7. Estados distintos sin duplicados ordenados ---");
            List<String> c7 = em.createQuery(
                "SELECT DISTINCT f.estado FROM FacturaVenta f ORDER BY f.estado ASC", 
                String.class)
                .getResultList();
            c7.forEach(e -> System.out.println("Estado: " + e));

            System.out.println("\n--- 8. Agregaciones: COUNT, SUM, AVG ---");
            Object[] c8 = em.createQuery(
                "SELECT COUNT(f), SUM(f.importeTotal), AVG(f.importeTotal) "
                + "FROM FacturaVenta f WHERE f.estado = :estado", 
                Object[].class)
                .setParameter("estado", "EMITIDA")
                .getSingleResult();
            System.out.println("Cant: " + c8[0] 
                + " | Suma: $" + c8[1] 
                + " | Promedio: $" + c8[2]);

            System.out.println("\n--- 9. Puntos de venta con operador IN ---");
            List<PuntoVenta> c9 = em.createQuery(
                "SELECT pv FROM PuntoVenta pv WHERE pv.numero IN :numeros", 
                PuntoVenta.class)
                .setParameter("numeros", List.of(1, 2, 5))
                .getResultList();
            c9.forEach(pv -> System.out.println("PV: " + pv.getDescripcion() 
                + " (N°: " + pv.getNumero() + ")"));

            // ==========================================
            // NIVEL 3
            // ==========================================
            System.out.println("\n--- 10. Facturas por usuario de carga ---");
            List<FacturaVenta> c10 = em.createQuery(
                "SELECT f FROM FacturaVenta f "
                + "WHERE f.usuarioCarga.usuario = :usr", FacturaVenta.class)
                .setParameter("usr", "admin")
                .getResultList();
            c10.forEach(f -> System.out.println("Factura N°: " + f.getNumero() 
                + " cargada por admin"));

            System.out.println("\n--- 11. Detalles de facturas del PV 1 (INNER JOIN) ---");
            List<FacturaVentaDetalle> c11 = em.createQuery(
                "SELECT d FROM FacturaVentaDetalle d "
                + "JOIN d.factura f "
                + "WHERE f.puntoVenta.numero = :numPv", 
                FacturaVentaDetalle.class)
                .setParameter("numPv", 1)
                .getResultList();
            c11.forEach(d -> System.out.println("Detalle: " + d.getDescripcion() 
                + " | Cant: " + d.getCantidad()));

            System.out.println("\n--- 12. Artículos y Marcas (LEFT JOIN) ---");
            List<Object[]> c12 = em.createQuery(
                "SELECT a.denominacion, m.denominacion "
                + "FROM Articulo a LEFT JOIN a.marca m", Object[].class)
                .getResultList();
            for (Object[] fila : c12) {
                System.out.println("Art: " + fila[0] + " | Marca: " + fila[1]);
            }

            System.out.println("\n--- 13. Facturas con artículos marca 'Dell' ---");
            List<FacturaVenta> c13 = em.createQuery(
                "SELECT DISTINCT f FROM FacturaVenta f "
                + "JOIN f.detalles d "
                + "JOIN d.listaPrecioArticulo lpa "
                + "JOIN lpa.articulo a "
                + "JOIN a.marca m "
                + "WHERE m.denominacion = :marca", FacturaVenta.class)
                .setParameter("marca", "Dell")
                .getResultList();
            c13.forEach(f -> System.out.println("Factura N°: " + f.getNumero()));

            System.out.println("\n--- 14. Facturas con importe superior al promedio ---");
            List<FacturaVenta> c14 = em.createQuery(
                "SELECT f FROM FacturaVenta f "
                + "WHERE f.importeTotal > "
                + "(SELECT AVG(f2.importeTotal) FROM FacturaVenta f2)", 
                FacturaVenta.class)
                .getResultList();
            c14.forEach(f -> System.out.println("Factura N°: " + f.getNumero() 
                + " ($" + f.getImporteTotal() + ")"));

            // ==========================================
            // NIVEL 4
            // ==========================================
            System.out.println("\n--- 16. Facturación por Punto de Venta (GROUP BY) ---");
            List<Object[]> c16 = em.createQuery(
                "SELECT pv.descripcion, COUNT(f), SUM(f.importeTotal) "
                + "FROM FacturaVenta f JOIN f.puntoVenta pv "
                + "GROUP BY pv.descripcion", Object[].class)
                .getResultList();
            for (Object[] f : c16) {
                System.out.println("PV: " + f[0] 
                    + " | Facturas: " + f[1] 
                    + " | Total: $" + f[2]);
            }

            System.out.println("\n--- 17. Usuarios con más de 5 facturas (HAVING) ---");
            List<Object[]> c17 = em.createQuery(
                "SELECT u.nombre, COUNT(f) "
                + "FROM FacturaVenta f JOIN f.usuarioCarga u "
                + "GROUP BY u.nombre "
                + "HAVING COUNT(f) > 5", Object[].class)
                .getResultList();
            if (c17.isEmpty()) {
                System.out.println("Ningún usuario supera las 5 facturas aún.");
            } else {
                for (Object[] f : c17) {
                    System.out.println("Usuario: " + f[0] + " | Total: " + f[1]);
                }
            }

            System.out.println("\n--- 18. Ventas acumuladas por Marca ---");
            List<Object[]> c18 = em.createQuery(
                "SELECT m.denominacion, SUM(d.cantidad), SUM(d.importeSubtotal) "
                + "FROM FacturaVentaDetalle d "
                + "JOIN d.listaPrecioArticulo lpa "
                + "JOIN lpa.articulo a "
                + "JOIN a.marca m "
                + "GROUP BY m.denominacion", Object[].class)
                .getResultList();
            for (Object[] f : c18) {
                System.out.println("Marca: " + f[0] 
                    + " | Unidades: " + f[1] 
                    + " | Subtotal: $" + f[2]);
            }

            // ==========================================
            // NIVEL 5
            // ==========================================
            System.out.println("\n--- 20. Marcas con artículos facturados (EXISTS) ---");
            List<Marca> c20 = em.createQuery(
                "SELECT m FROM Marca m "
                + "WHERE EXISTS ("
                + "  SELECT d FROM FacturaVentaDetalle d "
                + "  WHERE d.listaPrecioArticulo.articulo.marca = m"
                + ")", Marca.class)
                .getResultList();
            c20.forEach(m -> System.out.println("Marca facturada: " + m.getDenominacion()));

            System.out.println("\n--- 21. Artículos sin ventas (NOT EXISTS) ---");
            List<Articulo> c21 = em.createQuery(
                "SELECT a FROM Articulo a "
                + "WHERE NOT EXISTS ("
                + "  SELECT d FROM FacturaVentaDetalle d "
                + "  WHERE d.listaPrecioArticulo.articulo = a"
                + ")", Articulo.class)
                .getResultList();
            if (c21.isEmpty()) {
                System.out.println("Todos los artículos existentes tienen ventas asociadas.");
            } else {
                c21.forEach(a -> System.out.println("Sin ventas: " + a.getDenominacion()));
            }

            System.out.println("\n--- 22. Clasificación CASE WHEN de facturas ---");
            List<Object[]> c22 = em.createQuery(
                "SELECT f.numero, f.importeTotal, "
                + "CASE "
                + "  WHEN f.importeTotal > 50000 THEN 'ALTO VALOR' "
                + "  WHEN f.importeTotal BETWEEN 10000 AND 50000 THEN 'MEDIO VALOR' "
                + "  ELSE 'BAJO VALOR' "
                + "END "
                + "FROM FacturaVenta f "
                + "ORDER BY f.importeTotal DESC", Object[].class)
                .getResultList();
            for (Object[] f : c22) {
                System.out.println("Factura N°: " + f[0] 
                    + " | Total: $" + f[1] 
                    + " | Categoria: " + f[2]);
            }

            System.out.println("\n==========================================");
            System.out.println("FIN DE CONSULTAS EXITOSO");
            System.out.println("==========================================");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
