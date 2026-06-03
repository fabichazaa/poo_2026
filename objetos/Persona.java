package objetos;

public abstract class Persona {
    private final String DNI;
    private String nombre;
    private String apellido;
    private Direccion direccion; // Asociacion: Persona tiene una dirección

    public Persona(String DNI, String nombre, String apellido, Direccion direccion) {
        this.DNI = DNI;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
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