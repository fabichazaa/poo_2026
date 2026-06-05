package objetos;

import java.util.*;

public class Veterinario extends Persona {
    private String matricula;
    private List<Turno> turnos; // Asociación: Un veterinario tiene muchos turnos

    public Veterinario(String DNI, String nombre, String apellido,
        String direccion, String matricula) {
        super(DNI, nombre, apellido, direccion);
        this.matricula = matricula;
        this.turnos = new ArrayList<>();
    }

    public String getMatricula() {
        return matricula;
    }

    public String getRutaFoto() {
        return "imagenes/vet.jpg";
    }

    public List<Turno> getTurnos() {
        return turnos;
    }
}