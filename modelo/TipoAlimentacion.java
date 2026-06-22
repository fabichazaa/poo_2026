package modelo;

public enum TipoAlimentacion {
    OMNIVORO("Omnívoro"),
    CARNIVORO_ESTRICTO("Carnívoro estricto"),
    HERBIBORO("Herbívoro");

    private final String descripcion;

    TipoAlimentacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
