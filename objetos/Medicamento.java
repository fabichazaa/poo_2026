package objetos;

public class Medicamento {
    private String codigoSenasa;
    private String nombreMedicamento;

    public Medicamento(String codigoSenasa, String nombreMedicamento) {
        this.codigoSenasa = codigoSenasa;
        this.nombreMedicamento = nombreMedicamento;
    }

    public String getCodigoSenasa() {
        return codigoSenasa;
    }
    
    public String getNombreMedicamento() {
        return nombreMedicamento;
    }
}