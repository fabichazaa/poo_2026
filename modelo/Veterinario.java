package modelo;

import java.util.*;

public class Veterinario extends Persona {

    private String matricula;
    private List<Turno> turnos;
    private String especialidad;
    private String turnoTrabajo;
    private boolean activo = true;

    public Veterinario(String DNI, String nombre, String apellido, String celular,
            Direccion direccion, String matricula) {
        super(DNI, nombre, apellido,celular, direccion);
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getImagen() {
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
