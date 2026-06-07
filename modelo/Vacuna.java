package modelo;

import java.time.LocalDate;

public class Vacuna extends Medicamento {
    private LocalDate fechaAplicacion;
    private LocalDate fechaVencimiento;

    public Vacuna(String codigoSenasa, String nombreMedicamento, LocalDate fechaAplicacion, LocalDate fechaVencimiento) {
        // Llama al constructor de Medicamento
        super(codigoSenasa, nombreMedicamento); 
        this.fechaAplicacion = fechaAplicacion;
        this.fechaVencimiento = fechaVencimiento;
    }

    public LocalDate getFechaAplicacion() { return fechaAplicacion; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }

    // Método clave: nos dice si la vacuna ya venció con respecto a la fecha actual
    public boolean estaVencida() {
        return LocalDate.now().isAfter(this.fechaVencimiento);
    }
}