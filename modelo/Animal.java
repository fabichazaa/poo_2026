package modelo;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Animal {
    private final String idAnimal;
    private String nombre;
    private LocalDate fechaNacimiento;
    private final boolean sexo;
    private Responsable responsable;
    private final HistoriaClinica historiaClinica;
    private boolean enAdopcion;

    public Animal(String nombre, LocalDate fechaNacimiento, boolean sexo, Responsable responsable) {
        this.idAnimal = generarIdAnimal();
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.responsable = responsable;
        this.historiaClinica = new HistoriaClinica();
        this.enAdopcion = false;
    }

    public Animal(String nombre, LocalDate fechaNacimiento, boolean sexo) {
        this(nombre, fechaNacimiento, sexo, null);
    }

    public String getIdAnimal() {
        return idAnimal;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public boolean isSexo() {
        return sexo;
    }

    public Responsable getResponsable() {
        return responsable;
    }

    public HistoriaClinica getHistorial() {
        return historiaClinica;
    }

    public boolean isEnAdopcion() {
        return enAdopcion;
    }

    public void setEnAdopcion(boolean enAdopcion) {
        this.enAdopcion = enAdopcion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setResponsable(Responsable responsable) {
        this.responsable = responsable;
    }

    private String generarIdAnimal() {
        return UUID.randomUUID().toString();
    }

    public int calcularEdad() {
        if (fechaNacimiento == null) {
            return -1;
        }
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }

    public String getRutaFoto() {
        return "imagenes/patitas.png";
    }

    public abstract TipoAlimentacion getTipoAlimentacion();

    public abstract String getEspecie();

    @Override
    public String toString() {
        String resp = responsable != null
            ? responsable.getNombre() + " " + responsable.getApellido()
            : "Sin responsable";
        return getEspecie() + " - Nombre: " + nombre
            + ", Edad: " + calcularEdad() + " años"
            + ", Alimentación: " + getTipoAlimentacion().getDescripcion()
            + ", Responsable: " + resp;
    }
}
