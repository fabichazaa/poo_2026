package modelo;

public enum TipoTurno {
    CONSULTA_GENERAL("Consulta General", 15, "imagenes/emojis/estetoscopio.png", "🩺"),
    VACUNACION("Vacunación", 10, "imagenes/emojis/jeringa.png", "💉"),
    CIRUGIA("Cirugía", 60, "imagenes/emojis/salud.png", "⚕️"),
    BANIO("Baño", 30, "imagenes/emojis/jabon.png", "🧼"),
    ANALISIS("Análisis", 20, "imagenes/emojis/laboratorio.png", "🔬"),
    SEGUIMIENTO("Seguimiento", 15, "imagenes/emojis/anotar.png", "📝");

    private final String descripcion;
    private final int duracionMinutos;
    
    private final String rutaEmoji;
    private final String emojiRespaldo;

    TipoTurno(String descripcion, int duracionMinutos, String rutaEmoji, String emojiRespaldo) {
        this.descripcion = descripcion;
        this.duracionMinutos = duracionMinutos;
        this.rutaEmoji = rutaEmoji;
        this.emojiRespaldo = emojiRespaldo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public String getRutaEmoji() {
        return rutaEmoji;
    }

    public String getEmojiRespaldo() {
        return emojiRespaldo;
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
