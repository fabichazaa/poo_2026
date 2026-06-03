package objetos;

public class Veterinario extends Persona {
    private String matricula;

    public Veterinario(String DNI, String nombre, String apellido,
        Direccion direccion, String matricula) {
        super(DNI, nombre, apellido, direccion);
        this.matricula = matricula;
    }

    public String getMatricula() {
        return matricula;
    }
}