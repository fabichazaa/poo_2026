package objetos;

import java.time.LocalDate;

public class Gato extends Animal {
    private String raza;

    public Gato(String nombre, LocalDate fechaNacimiento, boolean sexo,
        Responsable responsable, String raza) {
        super(nombre, fechaNacimiento, sexo, responsable);
        this.raza = raza;
    }

    public String getRaza() {
        return raza;
    }

    @Override
    public String getRutaFoto() {
        return "imagenes/perro.png";
    }
}