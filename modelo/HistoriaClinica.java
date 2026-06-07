package modelo;
import java.util.ArrayList;

public class HistoriaClinica {
    private final ArrayList<Medicamento> medicamentosRecetados; // Agregación

    public HistoriaClinica() {
        this.medicamentosRecetados = new ArrayList<>();
    }

    public void recetarMedicamento(Medicamento med) {
        this.medicamentosRecetados.add(med);
    }

    public ArrayList<Medicamento> getMedicamentosRecetados() {
        return medicamentosRecetados;
    }
}