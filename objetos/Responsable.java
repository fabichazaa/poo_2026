package objetos;
import java.util.ArrayList;

public class Responsable extends Persona {
    private final ArrayList<Animal> mascotas; // Asociación: Un dueño tiene muchos animales

    public Responsable(String DNI, String nombre, String apellido, String direccion) {
        super(DNI, nombre, apellido, direccion);
        this.mascotas = new ArrayList<>();
    }

    public void agregarMascota(Animal animal) {
        this.mascotas.add(animal);
    }

    public ArrayList<Animal> getMascotas() { return mascotas; }
}