
package tostring_act3;

public class Principal {
    public static void main(String[] args) {
        Curso curso1 = new Curso("Programación III");
        curso1.agregarEstudiante(new Estudiante("Juan", 20, "Tecnicatura en Programación"));
        curso1.agregarEstudiante(new Estudiante("María", 22, "Tecnicatura en Programación"));

        Curso curso2 = new Curso("Estructuras de Datos");
        curso2.agregarEstudiante(new Estudiante("Pedro", 21, "Ingeniería en Sistemas"));
        curso2.agregarEstudiante(new Estudiante("Lucía", 23, "Ingeniería en Sistemas"));

        System.out.println(curso1);
        System.out.println(curso2);
    }
}