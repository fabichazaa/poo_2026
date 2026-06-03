package objetos;
import java.util.ArrayList;

public class Veterinaria {
    private String nombreNegocio;
    private final ArrayList<Veterinario> listaVeterinarios; // Composición
    private final ArrayList<Responsable> listaClientes;          // Composición
    private final ArrayList<Turno> listaTurnos;             // Composición
    private final ArrayList<Medicamento> catalogoMedicamentos;

    public Veterinaria(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
        this.listaVeterinarios = new ArrayList<>();
        this.listaClientes = new ArrayList<>();
        this.listaTurnos = new ArrayList<>();
        this.catalogoMedicamentos = new ArrayList<>();
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
    }

    public void registrarVeterinario(Veterinario v) { listaVeterinarios.add(v); }
    public void registrarCliente(Responsable d) { listaClientes.add(d); }
    public void registrarTurno(Turno t) { listaTurnos.add(t); }
    public void agregarMedicamentoAlCatalogo(Medicamento m) { catalogoMedicamentos.add(m); }

    // Getters para que los JTables de tus ventanas puedan leer los datos
    public ArrayList<Veterinario> getListaVeterinarios() { return listaVeterinarios; }
    public ArrayList<Responsable> getListaClientes() { return listaClientes; }
    public ArrayList<Turno> getListaTurnos() { return listaTurnos; }
    public ArrayList<Medicamento> getCatalogoMedicamentos() { return catalogoMedicamentos; }
}