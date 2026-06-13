package modelo;

import java.time.LocalDate;

public class Perro extends Animal {
    private String raza;
    private boolean necesitaPaseo;

    public Perro(String nombre, LocalDate fechaNacimiento, boolean sexo,
        Responsable responsable, String raza) {
        super(nombre, fechaNacimiento, sexo, responsable);
        this.raza = raza;
        this.necesitaPaseo = true;
    }

    public Perro(String nombre, LocalDate fechaNacimiento, boolean sexo, String raza) {
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
        return "Perro";
    }

    public String getImagen() {
        return "imagenes/emojis/perro.png";
    }

    public String getColorInicioHex() {
        return "#FFB200";
    }

    public String getColorFinHex() {
        return "#FF7300";
    }

    public String getCategoriaFiltro() {
        return "Perro";
    }
}
