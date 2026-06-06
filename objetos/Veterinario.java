package objetos;

import java.util.*;

public class Veterinario extends Persona {
    private String matricula;
    private List<Turno> turnos;
    private String especialidad;
    private String turnoTrabajo;

    public Veterinario(String DNI, String nombre, String apellido,
        Direccion direccion, String matricula) {
        super(DNI, nombre, apellido, direccion);
        this.matricula = matricula;
        this.turnos = new ArrayList<>();
        this.especialidad = "General";
        this.turnoTrabajo = "Mañana";
    }

    public Veterinario(String DNI, String nombre, String apellido, String matricula) {
        super(DNI, nombre, apellido);
        this.matricula = matricula;
        this.turnos = new ArrayList<>();
        this.especialidad = "General";
        this.turnoTrabajo = "Mañana";
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTurnoTrabajo() {
        return turnoTrabajo;
    }

    public void setTurnoTrabajo(String turnoTrabajo) {
        this.turnoTrabajo = turnoTrabajo;
    }

    public String getRutaFoto() {
        return "imagenes/vet.jpg";
    }

    public List<Turno> getTurnos() {
        return turnos;
    }

    public void agregarTurno(Turno t) {
        if (t != null && !this.turnos.contains(t)) {
            this.turnos.add(t);
        }
    }
}
