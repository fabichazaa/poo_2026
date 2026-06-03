package objetos;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Animal {
    private final String idAnimal;
    private String nombre;
    private LocalDate fechaNacimiento;
    private final boolean sexo;
    private Responsable responsable;
    private final HistoriaClinica historiaClinica; // Composición: El animal nace con su historial

    public Animal(String nombre, LocalDate fechaNacimiento, boolean sexo, Responsable responsable) {
        this.idAnimal = generarIdAnimal();
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.responsable = responsable;
        this.historiaClinica = new HistoriaClinica();
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
        return "imagenes/" + idAnimal + ".jpg";
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Edad: " + calcularEdad() + " años, Responsable: " + responsable.getNombre() + " " + responsable.getApellido();
    }
}