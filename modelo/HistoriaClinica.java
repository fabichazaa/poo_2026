package modelo;

import java.util.ArrayList;

public class HistoriaClinica {

    private final ArrayList<Prescripcion> medicamentosRecetados;
    private final ArrayList<RegistroVacunacion> registroVacunas;

    public HistoriaClinica() {
        this.medicamentosRecetados = new ArrayList<>();
        this.registroVacunas = new ArrayList<>();
    }

    public void recetarMedicamento(Medicamento med) {
        this.medicamentosRecetados.add(new Prescripcion(med));
    }

    public void recetarMedicamento(Prescripcion pres) {
        this.medicamentosRecetados.add(pres);
    }

    public void registrarVacuna(RegistroVacunacion reg) {
        this.registroVacunas.add(reg);
    }

    public ArrayList<Prescripcion> getMedicamentosRecetados() {
        return medicamentosRecetados;
    }

    public ArrayList<RegistroVacunacion> getRegistroVacunas() {
        return registroVacunas;
    }
}
