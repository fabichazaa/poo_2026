package modelo;

import java.time.LocalDate;

public class Perro extends Animal {

    private boolean necesitaPaseo;

    public Perro(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso,
            Responsable responsable, String raza) {
        super(nombre, fechaNacimiento, sexo, peso, responsable, raza);
        this.necesitaPaseo = true;
    }

    public Perro(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso, String raza) {
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
        return "Perro";
    }

    @Override
    public String getImagen() {
        return "imagenes/emojis/perro.png";
    }

    public String getColorInicioHexActivo() {
        return "#FFB200";
    }

    public String getColorFinHexActivo() {
        return "#FF7300";
    }

    @Override
    public String getCategoriaFiltro() {
        return "Perro";
    }
}
