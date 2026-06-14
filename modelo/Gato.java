package modelo;

import java.time.LocalDate;

public class Gato extends Animal {

    private boolean esEsterilizado;

    public Gato(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso,
            Responsable responsable, String raza) {
        super(nombre, fechaNacimiento, sexo, peso, responsable, raza);
        this.esEsterilizado = false;
    }

    public Gato(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso, String raza) {
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
        return TipoAlimentacion.CARNIVORO_ESTRICTO;
    }

    @Override
    public String getEspecie() {
        return "Gato";
    }

    @Override
    public String getImagen() {
        return "imagenes/emojis/gato.png";
    }

    public String getColorInicioHexActivo() {
        return "#22D3EE";
    }

    public String getColorFinHexActivo() {
        return "#2563EB";
    }

    @Override
    public String getCategoriaFiltro() {
        return "Gato";
    }
}
