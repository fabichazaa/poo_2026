package objetos;

import java.time.LocalDate;

public class Perro extends Animal {
    private String raza;

    public Perro(String nombre, LocalDate fechaNacimiento, boolean sexo,
        Responsable responsable, String raza) {
        super(nombre, fechaNacimiento, sexo, responsable);
        this.raza = raza;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    @Override
    public String getRutaFoto() {
        return "imagenes/perro.png";
    }
}