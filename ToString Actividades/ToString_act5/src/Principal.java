
import java.util.HashSet;
import java.util.Set;

public class Principal {
    public static void main(String[] args) {
        Set<Producto> conjuntoProductos = new HashSet<>();

        Producto p1 = new Producto("PROD01", "Café Molido 500g", 4500.0);
        Producto p2 = new Producto("PROD02", "Leche Entera 1L", 1200.0);
        // Producto con el mismo código que p1 (mismo hashCode y equals true)
        Producto p3 = new Producto("PROD01", "Café en Grano 500g", 5200.0);
        Producto p4 = new Producto("PROD03", "Medialuna de Manteca", 800.0);
        // Producto con el mismo código que p2
        Producto p5 = new Producto("PROD02", "Leche Descremada 1L", 1250.0);

        Producto[] productos = { p1, p2, p3, p4, p5 };

        for (Producto prod : productos) {
            boolean agregado = conjuntoProductos.add(prod);
            if (agregado) {
                System.out.println("Insertado: " + prod.getCodigo() + " - " + prod.getNombre());
            } else {
                System.out.println("Rechazado por duplicado: " + prod.getCodigo() + " - " + prod.getNombre());
            }
        }

        System.out.println("\n--- Contenido Final del HashSet (sin duplicados) ---");
        for (Producto prod : conjuntoProductos) {
            System.out.println(prod);
        }
    }
}