package modelo;

import java.time.LocalDate;

public class Prescripcion {
    private final Medicamento medicamento;
    private final String dosis;
    private final String viaAdministracion;
    private final String frecuencia;
    private final LocalDate fechaInicio;
    private final int vigenciaDias;
    private final String indicaciones;
    private final boolean esMagistral;

    public Prescripcion(Medicamento medicamento, String dosis, String viaAdministracion, 
                        String frecuencia, LocalDate fechaInicio, int vigenciaDias, 
                        String indicaciones, boolean esMagistral) {
        this.medicamento = medicamento;
        this.dosis = dosis;
        this.viaAdministracion = viaAdministracion;
        this.frecuencia = frecuencia;
        this.fechaInicio = fechaInicio;
        this.vigenciaDias = vigenciaDias;
        this.indicaciones = indicaciones;
        this.esMagistral = esMagistral;
    }

    // Constructor de conveniencia/compatibilidad
    public Prescripcion(Medicamento medicamento) {
        this(medicamento, "1 dosis", "Oral", "Cada 24hs", LocalDate.now(), 7, "Uso general", false);
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public String getDosis() {
        return dosis;
    }

    public String getViaAdministracion() {
        return viaAdministracion;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public int getVigenciaDias() {
        return vigenciaDias;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public boolean isEsMagistral() {
        return esMagistral;
    }

    // Delegados para facilidad de uso
    public String getNombreMedicamento() {
        return medicamento != null ? medicamento.getNombreMedicamento() : "";
    }

    public String getCodigoSenasa() {
        return medicamento != null ? medicamento.getCodigoSenasa() : "";
    }
}
