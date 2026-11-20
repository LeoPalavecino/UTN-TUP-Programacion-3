
import java.util.ArrayList;
import java.util.List;

public class Principal {
    public static void main(String[] args) {
        List<Producto> productos = new ArrayList<>();

        Producto p1 = new Producto("A01", "Café Molido", 4500.0);
        Producto p2 = new Producto("A02", "Azúcar Mascabo", 2200.0);
        Producto p3 = new Producto("A01", "Café Tostado en Grano", 5100.0); // Mismo código que p1

        // Intento de inserción de p1
        if (!productos.contains(p1)) {
            productos.add(p1);
            System.out.println("Agregado: " + p1.getNombre());
        } else {
            System.out.println("Duplicado ignorado: " + p1.getNombre() + " (Código: " + p1.getCodigo() + ")");
        }

        // Intento de inserción de p2
        if (!productos.contains(p2)) {
            productos.add(p2);
            System.out.println("Agregado: " + p2.getNombre());
        } else {
            System.out.println("Duplicado ignorado: " + p2.getNombre() + " (Código: " + p2.getCodigo() + ")");
        }

        // Intento de inserción de p3 (mismo código "A01")
        if (!productos.contains(p3)) {
            productos.add(p3);
            System.out.println("Agregado: " + p3.getNombre());
        } else {
            System.out.println("Duplicado ignorado: " + p3.getNombre() + " (Código: " + p3.getCodigo() + ")");
        }

        System.out.println("\nLista final de productos (" + productos.size() + " elementos):");
        for (Producto prod : productos) {
            System.out.println("  - " + prod);
        }
    }
}