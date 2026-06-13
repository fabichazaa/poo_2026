package modelo;

import java.time.LocalDate;

public class Gato extends Animal {
    private String raza;
    private boolean esEsterilizado;

    public Gato(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso,
        Responsable responsable, String raza) {
        super(nombre, fechaNacimiento, sexo, peso, responsable);
        this.raza = raza;
        this.esEsterilizado = false;
    }

    public Gato(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso, String raza) {
        super(nombre, fechaNacimiento, sexo, peso);
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

    public String getImagen() {
        return "imagenes/emojis/gato.png";
    }

    public String getColorInicioHex() {
        return "#22D3EE";
    }

    public String getColorFinHex() {
        return "#2563EB";
    }

    public String getCategoriaFiltro() {
        return "Gato";
    }
}
