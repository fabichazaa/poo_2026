package modelo;

public class Vacuna extends Medicamento {

    private final int vigenciaDias; // Guardamos la vigencia en cantidad de días

    public Vacuna(String codigoSenasa, String nombreMedicamento, int vigenciaDias) {
        super(codigoSenasa, nombreMedicamento);
        this.vigenciaDias = vigenciaDias;
    }

    public int getVigenciaDias() {
        return vigenciaDias;
    }
}
