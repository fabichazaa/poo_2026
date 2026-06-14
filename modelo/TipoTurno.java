package modelo;

public enum TipoTurno {
    CONSULTA_GENERAL("Consulta General", 15),
    SEGUIMIENTO("Seguimiento", 15),
    VACUNACION("Vacunación", 10),
    CIRUGIA("Cirugía", 60),
    BANIO("Baño", 30),
    ANALISIS("Análisis", 20);

    private final String descripcion;
    private final int duracionMinutos;

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

    public java.awt.Color getAccentColor() {
        return switch (this) {
            case CIRUGIA ->
                recursos.Color.CAT_CIRUGIA;
            case CONSULTA_GENERAL ->
                recursos.Color.CAT_CONSULTA;
            case ANALISIS ->
                recursos.Color.CAT_ANALISIS;
            case VACUNACION ->
                recursos.Color.CAT_VACUNA;
            case SEGUIMIENTO ->
                recursos.Color.CAT_CONTROL;
            default ->
                recursos.Color.CAT_CONTROL;
        };
    }

    public java.awt.Color getBadgeBgColor() {
        return switch (this) {
            case CIRUGIA ->
                recursos.Color.RED_LIGHT;
            case CONSULTA_GENERAL ->
                recursos.Color.BLUE_LIGHT;
            case ANALISIS ->
                recursos.Color.PURPLE_LIGHT;
            case SEGUIMIENTO ->
                recursos.Color.ORANGE_LIGHT;
            default ->
                recursos.Color.SUCCESS_LIGHT;
        };
    }

    public java.awt.Color getBadgeFgColor() {
        return switch (this) {
            case CIRUGIA ->
                recursos.Color.ERROR;
            case CONSULTA_GENERAL ->
                recursos.Color.BLUE_DARK;
            case ANALISIS ->
                recursos.Color.PURPLE_DARK;
            case SEGUIMIENTO ->
                recursos.Color.ORANGE_DARK;
            default ->
                recursos.Color.SUCCESS;
        };
    }
}
