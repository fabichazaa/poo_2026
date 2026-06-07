package modelo;

import java.time.LocalDate;

public class Gato extends Animal {
    private String raza;
    private boolean esEsterilizado;

    public Gato(String nombre, LocalDate fechaNacimiento, boolean sexo,
        Responsable responsable, String raza) {
        super(nombre, fechaNacimiento, sexo, responsable);
        this.raza = raza;
        this.esEsterilizado = false;
    }

    public Gato(String nombre, LocalDate fechaNacimiento, boolean sexo, String raza) {
        super(nombre, fechaNacimiento, sexo);
        this.raza = raza;
        this.esEsterilizado = false;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public boolean isEsEsterilizado() {
        return esEsterilizado;
    }

    public void setEsEsterilizado(boolean esEsterilizado) {
        this.esEsterilizado = esEsterilizado;
    }

    @Override
    public TipoAlimentacion getTipoAlimentacion() {
        return TipoAlimentacion.CARNIVORO_ESTRICTO;
    }

    @Override
    public String getEspecie() {
        return "Gato";
    }

    @Override
    public String getRutaFoto() {
        return "imagenes/gato.png";
    }
}
