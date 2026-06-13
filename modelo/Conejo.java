package modelo;

import java.time.LocalDate;

public class Conejo extends Animal {
    private String raza;
    private boolean esEsterilizado;

    public Conejo(String nombre, LocalDate fechaNacimiento, boolean sexo,
        Responsable responsable, String raza) {
        super(nombre, fechaNacimiento, sexo, responsable);
        this.raza = raza;
        this.esEsterilizado = false;
    }

    public Conejo(String nombre, LocalDate fechaNacimiento, boolean sexo, String raza) {
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
        return "Conejo";
    }

    @Override
    public String getImagen() {
        return "imagenes/emojis/conejo.png";
    }

    public String getColorInicioHex() {
        return "#F472B6";
    }
        
    public String getColorFinHex() {
        return "#DB2777";
    }

    public String getCategoriaFiltro() {
        return "Conejo";
    }
}
