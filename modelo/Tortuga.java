package modelo;

import java.time.LocalDate;

public class Tortuga extends Animal {

    private boolean necesitaPaseo;

    public Tortuga(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso,
            Responsable responsable, String raza) {
        super(nombre, fechaNacimiento, sexo, peso, responsable, raza);
        this.necesitaPaseo = true;
    }

    public Tortuga(String nombre, LocalDate fechaNacimiento, boolean sexo, float peso, String raza) {
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
        return "Tortuga";
    }

    @Override
    public String getImagen() {
        return "imagenes/emojis/tortuga.png";
    }

    public String getColorInicioHexActivo() {
        return "#A3E635";
    }
        
    public String getColorFinHexActivo() {
        return "#4D7C0F";
    }

    @Override
    public String getCategoriaFiltro() {
        return "Otro";
    }
}
