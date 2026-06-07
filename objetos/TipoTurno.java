package objetos;

public enum TipoTurno {
    CONSULTA_GENERAL("Consulta General", 15),
    CIRUGIA("Cirugía", 60),
    BANIO("Baño", 30),
    ANALISIS("Análisis", 20);

    private final String descripcion;
    private final int duracionMinutos; // <-- Atributo para la duración

    // El constructor del enum asigna ambos valores
    TipoTurno(String descripcion, int duracionMinutos) {
        this.descripcion = descripcion;
        this.duracionMinutos = duracionMinutos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }
}