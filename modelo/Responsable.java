package modelo;
import java.util.ArrayList;

public class Responsable extends Persona {
    private final ArrayList<Animal> mascotas;

    public Responsable(String DNI, String nombre, String apellido, Direccion direccion) {
        super(DNI, nombre, apellido, direccion);
        this.mascotas = new ArrayList<>();
    }

    public Responsable(String DNI, String nombre, String apellido) {
        super(DNI, nombre, apellido);
        this.mascotas = new ArrayList<>();
    }

    public void agregarMascota(Animal animal) {
        this.mascotas.add(animal);
    }

    public void removerMascota(Animal animal) {
        this.mascotas.remove(animal);
    }

    public ArrayList<Animal> getMascotas() {
        return mascotas;
    }
}
