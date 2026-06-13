package modelo;

import java.time.LocalDate;

public class Loro extends Animal {
    private String raza;
    private boolean necesitaPaseo;

    public Loro(String nombre, LocalDate fechaNacimiento, boolean sexo,
        Responsable responsable, String raza) {
        super(nombre, fechaNacimiento, sexo, responsable);
        this.raza = raza;
        this.necesitaPaseo = true;
    }

    public Loro(String nombre, LocalDate fechaNacimiento, boolean sexo, String raza) {
        super(nombre, fechaNacimiento, sexo);
        this.raza = raza;
        this.necesitaPaseo = true;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
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

    public String getColorInicioHex() {
        return "#34D399";
    }

    public String getColorFinHex() {
        return "#059669";
    } 

    public String getCategoriaFiltro() {
        return "Otro";
    }
}
