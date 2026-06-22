package modelo;

import java.time.LocalDate;

public class Conejo extends Animal {

    private boolean esEsterilizado;

    public Conejo(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso,
            Responsable responsable, String raza) {
        super(nombre, fechaNacimiento, sexo, peso, responsable, raza);
        this.esEsterilizado = false;
    }

    public Conejo(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso, String raza) {
        super(nombre, fechaNacimiento, sexo, peso, raza);
        this.esEsterilizado = false;
    }

    public boolean isEsEsterilizado() {
        return esEsterilizado;
    }

    public void setEsEsterilizado(boolean esEsterilizado) {
        this.esEsterilizado = esEsterilizado;
    }

    @Override
    public TipoAlimentacion getTipoAlimentacion() {
        return TipoAlimentacion.HERBIBORO;
    }

    @Override
    public String getEspecie() {
        return "Conejo";
    }

    @Override
    public String getImagen() {
        return "imagenes/emojis/conejo.png";
    }

    @Override
    public String getColorInicioHexActivo() {
        return "#F472B6";
    }

    @Override
    public String getColorFinHexActivo() {
        return "#DB2777";
    }

    @Override
    public String getCategoriaFiltro() {
        return "Otro";
    }
}
