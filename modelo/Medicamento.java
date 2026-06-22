package modelo;

public class Medicamento {
    private String codigoSenasa;
    private String nombreMedicamento;
    private String categoria;

    public Medicamento(String codigoSenasa, String nombreMedicamento) {
        this(codigoSenasa, nombreMedicamento, "");
    }

    public Medicamento(String codigoSenasa, String nombreMedicamento, String categoria) {
        this.codigoSenasa = codigoSenasa;
        this.nombreMedicamento = nombreMedicamento;
        this.categoria = categoria;
    }

    public String getCodigoSenasa() {
        return codigoSenasa;
    }
    
    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}