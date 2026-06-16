package modelo;
import java.util.ArrayList;
import java.util.List;

public class Veterinaria {
    private String nombreNegocio;
    private final ArrayList<Veterinario> listaVeterinarios;
    private final ArrayList<Responsable> listaClientes;
    private final ArrayList<Turno> listaTurnos;
    private final ArrayList<Medicamento> catalogoMedicamentos;
    private final ArrayList<Animal> pacientesRegistrados;

    public Veterinaria(String nombreNegocio) {
        this(nombreNegocio, false);
    }

    public Veterinaria(String nombreNegocio, boolean autoSembrar) {
        this.nombreNegocio = nombreNegocio;
        this.listaVeterinarios = new ArrayList<>();
        this.listaClientes = new ArrayList<>();
        this.listaTurnos = new ArrayList<>();
        this.catalogoMedicamentos = new ArrayList<>();
        this.pacientesRegistrados = new ArrayList<>();
        if (autoSembrar) {
            cargarCatalogoDemo();
        }
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
    }

    public void registrarVeterinario(Veterinario v) { listaVeterinarios.add(v); }
    public void registrarCliente(Responsable d) { listaClientes.add(d); }
    // ponytail: nadie consulta Veterinario.turnos — registrarEnVeterinario() es ruido muerto
    public void registrarTurno(Turno t) { listaTurnos.add(t); }
    public void agregarMedicamentoAlCatalogo(Medicamento m) { catalogoMedicamentos.add(m); }

    public ArrayList<Veterinario> getListaVeterinarios() { return listaVeterinarios; }
    public ArrayList<Responsable> getListaClientes() { return listaClientes; }
    public ArrayList<Turno> getListaTurnos() { return listaTurnos; }
    public ArrayList<Medicamento> getCatalogoMedicamentos() { return catalogoMedicamentos; }
    public ArrayList<Animal> getPacientesRegistrados() { return pacientesRegistrados; }

    public Veterinario buscarVeterinarioPorMatricula(String matricula) {
        if (matricula == null) return null;
        for (Veterinario v : listaVeterinarios) {
            if (matricula.equalsIgnoreCase(v.getMatricula())) return v;
        }
        return null;
    }

    public Veterinario buscarVeterinarioPorDni(String dni) {
        if (dni == null) return null;
        for (Veterinario v : listaVeterinarios) {
            if (dni.equals(v.getDNI())) return v;
        }
        return null;
    }

    public Responsable buscarClientePorDni(String dni) {
        if (dni == null) return null;
        for (Responsable c : listaClientes) {
            if (dni.equals(c.getDNI())) return c;
        }
        return null;
    }

    public void registrarPaciente(Animal a) {
        if (a != null && !pacientesRegistrados.contains(a)) {
            pacientesRegistrados.add(a);
        }
    }

    public ArrayList<Animal> buscarMascotasPorNombre(String nombre) {
        ArrayList<Animal> resultado = new ArrayList<>();
        if (nombre == null || nombre.isEmpty()) return resultado;
        String n = nombre.toLowerCase();
        for (Responsable c : listaClientes) {
            for (Animal a : c.getMascotas()) {
                if (a.getNombre().toLowerCase().contains(n)) {
                    resultado.add(a);
                }
            }
        }
        return resultado;
    }

    public ArrayList<Turno> obtenerTurnosPorFecha(String fecha) {
        ArrayList<Turno> resultado = new ArrayList<>();
        if (fecha == null) return resultado;
        for (Turno t : listaTurnos) {
            if (fecha.equals(t.getFecha())) resultado.add(t);
        }
        return resultado;
    }

    public ArrayList<Turno> obtenerTurnosDeVeterinario(Veterinario v) {
        ArrayList<Turno> resultado = new ArrayList<>();
        if (v == null) return resultado;
        for (Turno t : listaTurnos) {
            if (v.equals(t.getVeterinario())) resultado.add(t);
        }
        return resultado;
    }

    public ArrayList<Turno> obtenerTurnosPendientes() {
        ArrayList<Turno> resultado = new ArrayList<>();
        for (Turno t : listaTurnos) {
            if (t.esPendiente()) resultado.add(t);
        }
        return resultado;
    }

    public ArrayList<Turno> obtenerTurnosCompletados() {
        ArrayList<Turno> resultado = new ArrayList<>();
        for (Turno t : listaTurnos) {
            if (t.estaCompletado()) resultado.add(t);
        }
        return resultado;
    }

    public List<Animal> obtenerTodosLosAnimales() {
        List<Animal> todos = new ArrayList<>();
        for (Responsable c : listaClientes) {
            todos.addAll(c.getMascotas());
        }
        return todos;
    }

    public ArrayList<Animal> obtenerAnimalesEnAdopcion() {
        ArrayList<Animal> resultado = new ArrayList<>();
        for (Animal a : obtenerTodosLosAnimales()) {
            if (a.isEnAdopcion()) resultado.add(a);
        }
        return resultado;
    }

    public int contarTurnosDelDia(String fecha) {
        return obtenerTurnosPorFecha(fecha).size();
    }

    private void cargarCatalogoDemo() {
        agregarMedicamentoAlCatalogo(new Medicamento("SEN-001", "Amoxicilina 500mg", "Antibiótico"));
        agregarMedicamentoAlCatalogo(new Medicamento("SEN-002", "Meloxicam 5mg", "Analgésico / Antiinflamatorio"));
        agregarMedicamentoAlCatalogo(new Medicamento("SEN-003", "Dipirona", "Analgésico / Antipirético"));
        agregarMedicamentoAlCatalogo(new Medicamento("SEN-004", "Ivermectina", "Antiparasitario"));
        agregarMedicamentoAlCatalogo(new Medicamento("SEN-005", "Cefalexina 500mg", "Antibiótico"));
        agregarMedicamentoAlCatalogo(new Medicamento("SEN-006", "Metronidazol 250mg", "Antiparasitario / Antibacteriano"));
        agregarMedicamentoAlCatalogo(new Medicamento("SEN-007", "Enalapril 10mg", "Cardiovascular / Antihipertensivo"));
        agregarMedicamentoAlCatalogo(new Medicamento("SEN-008", "Omeprazol 20mg", "Protector Gástrico"));
    }
}
