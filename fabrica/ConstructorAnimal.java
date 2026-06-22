package fabrica;

import java.time.LocalDate;
import modelo.Animal;
import modelo.Responsable;

public class ConstructorAnimal {
    private final String tipo;
    private String nombre;
    private LocalDate fechaNacimiento;
    private boolean sexo;
    private float peso;
    private Responsable responsable;
    private String raza;

    public ConstructorAnimal(String tipo) {
        this.tipo = tipo;
    }

    public ConstructorAnimal conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ConstructorAnimal nacidoEl(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public ConstructorAnimal esMacho(boolean sexo) {
        this.sexo = sexo;
        return this;
    }

    public ConstructorAnimal conPeso(float peso) {
        this.peso = peso;
        return this;
    }

    public ConstructorAnimal conDueño(Responsable responsable) {
        this.responsable = responsable;
        return this;
    }

    public ConstructorAnimal deRaza(String raza) {
        this.raza = raza;
        return this;
    }

    public Animal construir() {
        return FabricaAnimalMap.crear(tipo, nombre, fechaNacimiento, sexo, peso, responsable, raza);
    }
}