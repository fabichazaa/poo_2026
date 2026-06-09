package modelo;

import java.time.LocalDate;

public class RegistroVacunacion {

    private final Vacuna vacunaAplicada;
    private final LocalDate fechaAplicacion;
    private final LocalDate fechaVencimiento;

    public RegistroVacunacion(Vacuna vacunaAplicada, LocalDate fechaAplicacion) {
        this.vacunaAplicada = vacunaAplicada;
        this.fechaAplicacion = fechaAplicacion;
        this.fechaVencimiento = fechaAplicacion.plusDays(vacunaAplicada.getVigenciaDias());
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
}
