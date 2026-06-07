package modelo;

public abstract class Persona {
    private final String DNI;
    private String nombre;
    private String apellido;
    private Direccion direccion;

    public Persona(String DNI, String nombre, String apellido, Direccion direccion) {
        this.DNI = DNI;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
    }

    public Persona(String DNI, String nombre, String apellido) {
        this(DNI, nombre, apellido, null);
    }

    public String getDNI() {
        return this.DNI;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public String getDireccionCompleta() {
        return direccion != null ? direccion.getDireccionCompleta() : "Sin dirección";
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
}
