package modelo;

import java.time.LocalDate;

public class Perro extends Animal {
    private String raza;
    private boolean necesitaPaseo;

    public Perro(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso,
        Responsable responsable, String raza) {
        super(nombre, fechaNacimiento, sexo, peso, responsable);
        this.raza = raza;
        this.necesitaPaseo = true;
    }

    public Perro(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso, String raza) {
        super(nombre, fechaNacimiento, sexo, peso);
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

    public String getColorInicioHexActivo() {
        return "#FFB200";
    }

    public String getColorFinHexActivo() {
        return "#FF7300";
    }

    public String getCategoriaFiltro() {
        return "Perro";
    }
}
