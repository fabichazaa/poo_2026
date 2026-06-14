package modelo;

import java.time.LocalDate;

public class Loro extends Animal {

    private boolean necesitaPaseo;

    public Loro(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso,
            Responsable responsable, String raza) {
        super(nombre, fechaNacimiento, sexo, peso, responsable, raza);
        this.necesitaPaseo = true;
    }

    public Loro(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso, String raza) {
        super(nombre, fechaNacimiento, sexo, peso, raza);
        this.necesitaPaseo = true;
    }

    public boolean isNecesitaPaseo() {
        return necesitaPaseo;
    }

    public void setNecesitaPaseo(boolean necesitaPaseo) {
        this.necesitaPaseo = necesitaPaseo;
    }

    @Override
    public TipoAlimentacion getTipoAlimentacion() {
        return TipoAlimentacion.OMNIVORO;
    }

    @Override
    public String getEspecie() {
        return "Loro";
    }

    @Override
    public String getImagen() {
        return "imagenes/emojis/loro.png";
    }

    public String getColorInicioHexActivo() {
        return "#34D399";
    }

    public String getColorFinHexActivo() {
        return "#059669";
    }

    @Override
    public String getCategoriaFiltro() {
        return "Otro";
    }
}
