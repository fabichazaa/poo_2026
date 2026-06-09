package modelo;

import java.util.ArrayList;

public class HistoriaClinica {

    private final ArrayList<Medicamento> medicamentosRecetados;
    private final ArrayList<RegistroVacunacion> registroVacunas; // Nueva lista dedicada

    public HistoriaClinica() {
        this.medicamentosRecetados = new ArrayList<>();
        this.registroVacunas = new ArrayList<>();
    }

    public void recetarMedicamento(Medicamento med) {
        this.medicamentosRecetados.add(med);
    }

    public void registrarVacuna(RegistroVacunacion reg) {
        this.registroVacunas.add(reg);
    }

    public ArrayList<Medicamento> getMedicamentosRecetados() {
        return medicamentosRecetados;
    }

    public ArrayList<RegistroVacunacion> getRegistroVacunas() {
        return registroVacunas;
    }
}
