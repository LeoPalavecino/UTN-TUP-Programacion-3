
package tostring_act3;

import java.util.ArrayList;
import java.util.List;

public class Curso {
    private String nombre;
    private List<Estudiante> listaDeEstudiantes;

    public Curso(String nombre) {
        this.nombre = nombre;
        this.listaDeEstudiantes = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Estudiante> getListaDeEstudiantes() {
        return listaDeEstudiantes;
    }

    public void agregarEstudiante(Estudiante estudiante) {
        this.listaDeEstudiantes.add(estudiante);
    }

    @Override
    public String toString() {
        String resultado = "Curso: " + nombre + "\n"
                         + "Cantidad de alumnos: " + listaDeEstudiantes.size() + "\n"
                         + "Lista de estudiantes:\n";

        for (Estudiante est : listaDeEstudiantes) {
            resultado += "  - " + est + "\n";
        }

        return resultado;
    }
}