package modelo;

public enum TipoTurno {
    CONSULTA_GENERAL("Consulta General", 15, "imagenes/emojis/estetoscopio.png", "🩺"),
    VACUNACION("Vacunación", 10, "imagenes/emojis/jeringa.png", "💉"),
    CIRUGIA("Cirugía", 60, "imagenes/emojis/salud.png", "⚕️"),
    BANIO("Baño", 30, "imagenes/emojis/jabon.png", "🧼"),
    ANALISIS("Análisis", 20, "imagenes/emojis/laboratorio.png", "🔬"),
    SEGUIMIENTO("Seguimiento", 20, "imagenes/emojis/anotar.png", "📝");

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
}
