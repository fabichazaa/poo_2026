package modelo;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Animal {
    private final String idAnimal;
    private String nombre;
    private LocalDate fechaNacimiento;
    private final boolean sexo; // True: macho, False: hembra
    private Responsable responsable;
    private final HistoriaClinica historiaClinica;
    private boolean enAdopcion;
    private boolean activo;
    private float peso;
    private String raza;

    public Animal(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso, Responsable responsable, String raza) {
        this.idAnimal = generarIdAnimal();
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.responsable = responsable;
        this.historiaClinica = new HistoriaClinica();
        this.enAdopcion = false;
        this.peso = peso;
        this.activo = true;
        this.raza = raza;
    }

    public Animal(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso, String raza) {
        this(nombre, fechaNacimiento, sexo, peso, null, raza);
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

    public boolean getSexo() {
        return sexo;
    }

    public boolean isActivo() { 
        return activo;
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

    public float getPeso() {
        return peso;
    }

    public String getRaza() {
        return raza;
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

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setPeso(float peso) { 
        this.peso = peso;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    private String generarIdAnimal() {
        return UUID.randomUUID().toString();
    }

    public int calcularEdad() {
        if (fechaNacimiento == null) {
            return -1;
        }
        return java.time.LocalDate.now().getYear() - fechaNacimiento.getYear();
    }

    abstract public String getImagen();

    abstract public String getColorInicioHex();

    abstract public String getColorFinHex();

    abstract public String getCategoriaFiltro();
    
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
