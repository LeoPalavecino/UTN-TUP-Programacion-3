
package act4;



import java.util.HashMap;
import java.util.Map;
import act4.Curso;

public class Main {
    public static void main(String[] args) {
        // 2. Usar HashMap<String, Curso> donde la clave es el código del curso
        Map<String, Curso> cursos = new HashMap<>();

        // 3. Agregar varios cursos
        cursos.put("PROG3", new Curso("Programación III", "Lic. González"));
        cursos.put("BD2", new Curso("Bases de Datos II", "Ing. Martínez"));
        cursos.put("METSIS", new Curso("Metodología de Sistemas", "Lic. Romero"));
        cursos.put("INGLES1", new Curso("Inglés Técnico I", "Prof. Silva"));

        // 4. Recuperar cursos por clave
        System.out.println("--- BÚSQUEDA POR CLAVE ---");
        String codigoBuscado = "PROG3";
        Curso cursoRecuperado = cursos.get(codigoBuscado);

        if (cursoRecuperado != null) {
            System.out.println("Curso encontrado con código [" + codigoBuscado + "]: " + cursoRecuperado);
        } else {
            System.out.println("No se encontró ningún curso con el código [" + codigoBuscado + "].");
        }

        System.out.println();

        // 5. Recorrer el map con entrySet() e imprimir las entradas
        System.out.println("--- LISTADO COMPLETO (entrySet) ---");
        for (Map.Entry<String, Curso> entrada : cursos.entrySet()) {
            String codigo = entrada.getKey();
            Curso curso = entrada.getValue();
            System.out.println("Código: " + codigo + " -> " + curso);
        }
    }
}