package modelo;

public class Medicamento {
    private String codigoSenasa;
    private String nombreMedicamento;
    private String subtitulo;

    public Medicamento(String codigoSenasa, String nombreMedicamento) {
        this(codigoSenasa, nombreMedicamento, "");
    }

    public Medicamento(String codigoSenasa, String nombreMedicamento, String subtitulo) {
        this.codigoSenasa = codigoSenasa;
        this.nombreMedicamento = nombreMedicamento;
        this.subtitulo = subtitulo;
    }

    public String getCodigoSenasa() {
        return codigoSenasa;
    }
    
    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }
}