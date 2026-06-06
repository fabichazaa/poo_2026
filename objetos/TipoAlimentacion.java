package objetos;

public enum TipoAlimentacion {
    OMNIVORO("Omnívoro"),
    CARNIVORO_ESTRICTO("Carnívoro estricto"),
    HERVIBORO("Herbívoro");

    private final String descripcion;

    TipoAlimentacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
