package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ComprobanteTurno {

    private final Turno turno;
    private final LocalDateTime fechaEmision;
    private String nombreVeterinaria;

    public ComprobanteTurno(Turno turno) {
        this(turno, "Veterinaria");
    }

    public ComprobanteTurno(Turno turno, String nombreVeterinaria) {
        if (turno == null) {
            throw new IllegalArgumentException("El comprobante requiere un turno asociado.");
        }
        this.turno = turno;
        this.nombreVeterinaria = nombreVeterinaria;
        this.fechaEmision = LocalDateTime.now();
    }

    public Turno getTurno() {
        return turno;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public String generarEncabezado() {

        return """
               =========================================
                  COMPROBANTE DE ATENCI\u00d3N VETERINARIA   
               =========================================
               Veterinaria: """ + nombreVeterinaria() + "\n"
                + "Comprobante N°: " + turno.getIdTurno() + "\n"
                + "Emitido: " + fechaEmision + "\n"
                + "-----------------------------------------\n";
    }

    public String generarDetallePaciente() {
        Animal a = turno.getAnimal();
        Responsable r = a.getResponsable();
        StringBuilder sb = new StringBuilder();
        sb.append("PACIENTE\n");
        sb.append("  Nombre: ").append(a.getNombre()).append("\n");
        sb.append("  Especie: ").append(a.getEspecie()).append("\n");
        sb.append("  Edad: ").append(a.calcularEdad()).append(" años\n");
        sb.append("  Alimentación: ").append(a.getTipoAlimentacion().getDescripcion()).append("\n");
        sb.append("  Raza: ").append(a.getRaza()).append("\n");
        sb.append("  ID: ").append(a.getIdAnimal()).append("\n");
        if (r != null) {
            sb.append("\nRESPONSABLE\n");
            sb.append("  Nombre: ").append(r.getNombre()).append(" ").append(r.getApellido()).append("\n");
            sb.append("  DNI: ").append(r.getDNI()).append("\n");
            sb.append("  Dirección: ").append(r.getDireccionCompleta()).append("\n");
        } else {
            sb.append("\nRESPONSABLE: Sin responsable asignado\n");
        }
        return sb.toString();
    }

    public String generarDetalleAtencion() {
        Veterinario v = turno.getVeterinario();
        StringBuilder sb = new StringBuilder();
        sb.append("\nATENCIÓN\n");
        sb.append("  Fecha: ").append(turno.getFecha()).append("\n");
        sb.append("  Hora: ").append(turno.getHora()).append("\n");
        sb.append("  Tipo: ").append(turno.getTipo().getDescripcion())
                .append(" (").append(turno.getTipo().getDuracionMinutos()).append(" min)\n");
        sb.append("  Estado: ").append(turno.getEstado()).append("\n");
        if (v != null) {
            sb.append("  Veterinario: Dr/a. ").append(v.getNombre()).append(" ").append(v.getApellido()).append("\n");
            sb.append("  Matrícula: ").append(v.getMatricula()).append("\n");
            sb.append("  Especialidad: ").append(v.getEspecialidad()).append("\n");
        }
        if (turno.getObservaciones() != null && !turno.getObservaciones().isEmpty()) {
            sb.append("\nOBSERVACIONES\n  ").append(turno.getObservaciones()).append("\n");
        }
        return sb.toString();
    }

    public String generarDetalleTratamiento() {
        ArrayList<Prescripcion> meds = turno.getAnimal().getHistorial().getMedicamentosRecetados();
        ArrayList<RegistroVacunacion> vacs = turno.getAnimal().getHistorial().getRegistroVacunas();
        StringBuilder sb = new StringBuilder();
        sb.append("\nTRATAMIENTO\n");

        boolean tieneMeds = meds != null && !meds.isEmpty();
        boolean tieneVacs = vacs != null && !vacs.isEmpty();

        if (!tieneMeds && !tieneVacs) {
            sb.append("  Sin medicamentos ni vacunas registradas.\n");
        } else {
            int i = 1;
            if (meds != null && !meds.isEmpty()) {
                for (Prescripcion p : meds) {
                    sb.append("  ").append(i++).append(". ")
                            .append(p.getMedicamento().getNombreMedicamento())
                            .append(" (SENASA: ").append(p.getMedicamento().getCodigoSenasa()).append(")\n");
                    sb.append("     Dosis: ").append(p.getDosis())
                            .append(" | Vía: ").append(p.getViaAdministracion())
                            .append(" | Frec: ").append(p.getFrecuencia())
                            .append(" | Días: ").append(p.getVigenciaDias())
                            .append("\n");
                    if (p.getIndicaciones() != null && !p.getIndicaciones().isEmpty()) {
                        sb.append("     Indicaciones: ").append(p.getIndicaciones()).append("\n");
                    }
                }
            }
            if (vacs != null && !vacs.isEmpty()) {
                for (RegistroVacunacion rv : vacs) {
                    sb.append("  ").append(i++).append(". ")
                            .append(rv.getVacunaAplicada().getNombreMedicamento())
                            .append(" (SENASA: ").append(rv.getVacunaAplicada().getCodigoSenasa()).append(")\n");
                    sb.append("     Tipo: Vacuna | Aplicada: ").append(rv.getFechaAplicacion())
                            .append(" | Vence: ").append(rv.getFechaVencimiento())
                            .append(" (Vigencia: ").append(rv.getVacunaAplicada().getVigenciaDias()).append(" días)")
                            .append(rv.estaVencida() ? " (VENCIDA)" : " (VIGENTE)")
                            .append("\n");
                }
            }
        }
        return sb.toString();
    }

    public String generarTextoCompleto() {
        return generarEncabezado()
                + generarDetallePaciente()
                + generarDetalleAtencion()
                + generarDetalleTratamiento()
                + "=========================================\n";
    }

    public void marcarComoCompletado() {
        turno.completarTurno();
    }

    private String nombreVeterinaria() {
        return nombreVeterinaria != null ? nombreVeterinaria : "Veterinaria";
    }
}
