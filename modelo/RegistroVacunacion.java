package modelo;

import java.time.LocalDate;

public class RegistroVacunacion {

    private final Vacuna vacunaAplicada;
    private final LocalDate fechaAplicacion;
    private final LocalDate fechaVencimiento;
    private String tipoDosis;
    private String via;
    private String lote;
    private String observaciones;

    public RegistroVacunacion(Vacuna vacunaAplicada, LocalDate fechaAplicacion) {
        this(vacunaAplicada, fechaAplicacion, "Dosis anual", "Subcutánea", "", "");
    }

    public RegistroVacunacion(Vacuna vacunaAplicada, LocalDate fechaAplicacion, 
                              String tipoDosis, String via, String lote, String observaciones) {
        this.vacunaAplicada = vacunaAplicada;
        this.fechaAplicacion = fechaAplicacion;
        this.fechaVencimiento = fechaAplicacion.plusDays(vacunaAplicada.getVigenciaDias());
        this.tipoDosis = tipoDosis;
        this.via = via;
        this.lote = lote;
        this.observaciones = observaciones;
    }

    public Vacuna getVacunaAplicada() {
        return vacunaAplicada;
    }

    public LocalDate getFechaAplicacion() {
        return fechaAplicacion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public boolean estaVencida() {
        return LocalDate.now().isAfter(this.fechaVencimiento);
    }

    public String getTipoDosis() {
        return tipoDosis;
    }

    public String getVia() {
        return via;
    }

    public String getLote() {
        return lote;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
