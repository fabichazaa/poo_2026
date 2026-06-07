package controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import modelo.*;

public class ControladorVeterinaria {

    private static ControladorVeterinaria instancia;

    private final Veterinaria veterinaria;
    private Veterinario veterinarioLogueado;
    private final List<String> notas;

    private ControladorVeterinaria() {
        this.veterinaria = new Veterinaria("Happy Paws", true);
        this.notas = new ArrayList<>();
        cargarDatosDemo();
    }

    public static ControladorVeterinaria getInstancia() {
        if (instancia == null) {
            instancia = new ControladorVeterinaria();
        }
        return instancia;
    }

    public static void reiniciar() {
        instancia = null;
    }

    public Veterinaria getVeterinaria() {
        return veterinaria;
    }

    public Veterinario getVeterinarioLogueado() {
        return veterinarioLogueado;
    }

    public List<String> getNotas() {
        return notas;
    }

    public boolean loginPorMatricula(String matricula) {
        if (matricula == null || matricula.isBlank()) return false;
        Veterinario v = veterinaria.buscarVeterinarioPorMatricula(matricula.trim());
        if (v != null) {
            this.veterinarioLogueado = v;
            return true;
        }
        return false;
    }

    public void cerrarSesion() {
        this.veterinarioLogueado = null;
    }

    public boolean haySesionActiva() {
        return veterinarioLogueado != null;
    }

    public Turno registrarTurno(String fecha, String hora, Veterinario vet, Animal animal, TipoTurno tipo) {
        int nuevoId = veterinaria.getListaTurnos().size() + 1;
        Turno t = new Turno(nuevoId, fecha, hora, vet, animal, tipo);
        veterinaria.registrarTurno(t);
        return t;
    }

    public void recetarMedicamento(Animal animal, Medicamento med) {
        if (animal != null && med != null) {
            animal.getHistorial().recetarMedicamento(med);
        }
    }

    public void marcarEnAdopcion(Animal animal, boolean enAdopcion) {
        if (animal != null) {
            animal.setEnAdopcion(enAdopcion);
        }
    }

    public void agregarNota(String texto) {
        if (texto == null || texto.isBlank()) return;
        String autor = veterinarioLogueado != null
            ? veterinarioLogueado.getNombre() + " " + veterinarioLogueado.getApellido()
            : "Sistema";
        String nota = "[" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            + " — " + autor + "] " + texto.trim();
        notas.add(0, nota);
    }

    public void eliminarNota(int indice) {
        if (indice >= 0 && indice < notas.size()) {
            notas.remove(indice);
        }
    }

    public List<Animal> obtenerTodosLosAnimales() {
        return veterinaria.obtenerTodosLosAnimales();
    }

    public List<Animal> obtenerAnimalesEnAdopcion() {
        return veterinaria.obtenerAnimalesEnAdopcion();
    }

    public int contarTurnosDelVeterinario(Veterinario v, String estado) {
        if (v == null) return 0;
        int n = 0;
        for (Turno t : veterinaria.getListaTurnos()) {
            if (!v.equals(t.getVeterinario())) continue;
            if (estado == null) { n++; continue; }
            if (estado.equals(t.getEstado())) n++;
        }
        return n;
    }

    private void cargarDatosDemo() {
        cargarEquipo();
        cargarClientesYPacientes();
        cargarMedicamentosYVacunas();
        cargarTurnos();
        cargarNotas();
    }

    private void cargarEquipo() {
        Veterinario vet1 = new Veterinario("22333444", "Carlos", "Páez", "MP-9854");
        vet1.setEspecialidad("Cirugía");
        vet1.setTurnoTrabajo("Mañana");
        veterinaria.registrarVeterinario(vet1);

        Veterinario vet2 = new Veterinario("55555555", "Laura", "Gómez", "MP-1024");
        vet2.setEspecialidad("Clínica médica");
        vet2.setTurnoTrabajo("Tarde");
        veterinaria.registrarVeterinario(vet2);

        Veterinario vet3 = new Veterinario("38765432", "Ana", "Ruiz", "MP-2050");
        vet3.setEspecialidad("Dermatología");
        vet3.setTurnoTrabajo("Mañana");
        veterinaria.registrarVeterinario(vet3);

        Veterinario vet4 = new Veterinario("42987654", "Miguel", "Torres", "MP-3080");
        vet4.setEspecialidad("Cardiología");
        vet4.setTurnoTrabajo("Tarde");
        veterinaria.registrarVeterinario(vet4);
    }

    private void cargarClientesYPacientes() {
        Direccion dir1 = new Direccion("Av. Siempre Viva", 742, "Springfield");
        Direccion dir2 = new Direccion("Calle de los Gatos", 123, "Palermo");
        Direccion dir3 = new Direccion("Boulevard de los Perros", 456, "Recoleta");
        Direccion dir4 = new Direccion("Pasaje San Roque", 89, "San Telmo");

        Responsable c1 = new Responsable("12345678", "Claudio", "Chiqui", dir1);
        Responsable c2 = new Responsable("24678901", "Marta", "Sánchez", dir2);
        Responsable c3 = new Responsable("31456789", "Ricardo", "López", dir3);
        Responsable c4 = new Responsable("40876543", "Patricia", "Fernández", dir4);
        veterinaria.registrarCliente(c1);
        veterinaria.registrarCliente(c2);
        veterinaria.registrarCliente(c3);
        veterinaria.registrarCliente(c4);

        c1.agregarMascota(new Perro("Hulk",   LocalDate.of(2023, 4, 15), true,  c1, "Dogo de Burdeos"));
        c1.agregarMascota(new Gato ("Luna",   LocalDate.of(2025, 8, 20), false, c1, "Siamés"));
        c1.agregarMascota(new Perro("Cheese", LocalDate.of(2018, 1, 10), true,  c1, "Beagle"));
        c1.agregarMascota(new Gato ("Mochi",  LocalDate.of(2024, 2, 14), false, c1, "Siamés"));

        c2.agregarMascota(new Gato ("Mishi",     LocalDate.of(2022, 11, 5), false, c2, "Persa"));
        c2.agregarMascota(new Gato ("Bigotes",   LocalDate.of(2020, 6, 18), true,  c2, "Maine Coon"));
        c2.agregarMascota(new Perro("Toby",      LocalDate.of(2019, 9, 22), true,  c2, "Caniche"));

        c3.agregarMascota(new Perro("Bobby", LocalDate.of(2021, 3, 11), true,  c3, "Golden Retriever"));
        c3.agregarMascota(new Perro("Rocky", LocalDate.of(2017, 7, 4),  true,  c3, "Bulldog Inglés"));

        c4.agregarMascota(new Gato ("Pelusa",   LocalDate.of(2024, 5, 30), false, c4, "Angora Turco"));
    }

    private void cargarMedicamentosYVacunas() {
        Veterinario vet1 = veterinaria.getListaVeterinarios().get(0);
        Veterinario vet2 = veterinaria.getListaVeterinarios().get(1);

        Animal hulk   = buscarAnimal("Hulk");
        Animal luna   = buscarAnimal("Luna");
        Animal mishi  = buscarAnimal("Mishi");
        Animal bobby  = buscarAnimal("Bobby");
        Animal cheese = buscarAnimal("Cheese");

        if (hulk != null) {
            hulk.getHistorial().recetarMedicamento(veterinaria.getCatalogoMedicamentos().get(0));
            hulk.getHistorial().recetarMedicamento(
                new Vacuna("SEN-VAC-001", "Antirrábica", LocalDate.of(2025, 6, 1), LocalDate.of(2026, 6, 1))
            );
            hulk.setEnAdopcion(false);
        }
        if (luna != null) {
            luna.getHistorial().recetarMedicamento(
                new Vacuna("SEN-VAC-002", "Triple Felina", LocalDate.of(2025, 2, 10), LocalDate.of(2026, 2, 10))
            );
        }
        if (cheese != null) {
            cheese.getHistorial().recetarMedicamento(
                new Vacuna("SEN-VAC-003", "Parvovirus (vencida)", LocalDate.of(2023, 1, 15), LocalDate.of(2024, 1, 15))
            );
        }
        if (mishi != null) {
            mishi.getHistorial().recetarMedicamento(veterinaria.getCatalogoMedicamentos().get(1));
        }
        if (bobby != null) {
            bobby.setEnAdopcion(true);
        }
    }

    private void cargarTurnos() {
        Veterinario vet1 = veterinaria.getListaVeterinarios().get(0);
        Veterinario vet2 = veterinaria.getListaVeterinarios().get(1);
        Veterinario vet3 = veterinaria.getListaVeterinarios().get(2);
        Veterinario vet4 = veterinaria.getListaVeterinarios().get(3);

        Animal hulk   = buscarAnimal("Hulk");
        Animal luna   = buscarAnimal("Luna");
        Animal cheese = buscarAnimal("Cheese");
        Animal mochi  = buscarAnimal("Mochi");
        Animal mishi  = buscarAnimal("Mishi");
        Animal bigotes= buscarAnimal("Bigotes");
        Animal bobby  = buscarAnimal("Bobby");
        Animal rocky  = buscarAnimal("Rocky");
        Animal toby   = buscarAnimal("Toby");
        Animal pelusa = buscarAnimal("Pelusa");

        Turno tPasado1 = new Turno(1,  "02/06/2026", "09:00", vet1, hulk,   TipoTurno.CIRUGIA);
        Turno tPasado2 = new Turno(2,  "02/06/2026", "11:00", vet2, mishi,  TipoTurno.CONSULTA_GENERAL);
        Turno tPasado3 = new Turno(3,  "04/06/2026", "10:30", vet1, cheese, TipoTurno.ANALISIS);
        Turno tPasado4 = new Turno(4,  "04/06/2026", "16:00", vet3, bobby,  TipoTurno.CONSULTA_GENERAL);
        Turno tPasado1c = new Turno(5, "05/06/2026", "08:30", vet1, luna,   TipoTurno.VACUNACION);
        tPasado1.completarTurno();
        tPasado2.completarTurno();
        tPasado3.completarTurno();
        tPasado4.completarTurno();
        tPasado1c.completarTurno();

        Turno tHoy1 = new Turno(6,  "06/06/2026", "09:30", vet1, hulk,   TipoTurno.CIRUGIA);
        Turno tHoy2 = new Turno(7,  "06/06/2026", "10:15", vet1, luna,   TipoTurno.CONSULTA_GENERAL);
        Turno tHoy3 = new Turno(8,  "06/06/2026", "11:45", vet1, hulk,   TipoTurno.ANALISIS);
        Turno tHoy4 = new Turno(9,  "06/06/2026", "14:00", vet1, mochi,  TipoTurno.VACUNACION);
        Turno tHoy5 = new Turno(10, "06/06/2026", "15:30", vet2, mishi,  TipoTurno.BANIO);
        Turno tHoy6 = new Turno(11, "06/06/2026", "16:00", vet4, rocky,  TipoTurno.CONSULTA_GENERAL);

        Turno tFut1 = new Turno(12, "08/06/2026", "10:00", vet1, bigotes, TipoTurno.CIRUGIA);
        Turno tFut2 = new Turno(13, "10/06/2026", "11:00", vet1, cheese,  TipoTurno.VACUNACION);
        Turno tFut3 = new Turno(14, "12/06/2026", "09:00", vet2, toby,    TipoTurno.CONSULTA_GENERAL);
        Turno tFut4 = new Turno(15, "15/06/2026", "17:00", vet3, pelusa,  TipoTurno.ANALISIS);

        Turno tCancel = new Turno(16, "06/06/2026", "18:00", vet1, luna, TipoTurno.CONSULTA_GENERAL);
        tCancel.cancelarTurno();

        veterinaria.registrarTurno(tPasado1);
        veterinaria.registrarTurno(tPasado2);
        veterinaria.registrarTurno(tPasado3);
        veterinaria.registrarTurno(tPasado4);
        veterinaria.registrarTurno(tPasado1c);
        veterinaria.registrarTurno(tHoy1);
        veterinaria.registrarTurno(tHoy2);
        veterinaria.registrarTurno(tHoy3);
        veterinaria.registrarTurno(tHoy4);
        veterinaria.registrarTurno(tHoy5);
        veterinaria.registrarTurno(tHoy6);
        veterinaria.registrarTurno(tFut1);
        veterinaria.registrarTurno(tFut2);
        veterinaria.registrarTurno(tFut3);
        veterinaria.registrarTurno(tFut4);
        veterinaria.registrarTurno(tCancel);
    }

    private void cargarNotas() {
        agregarNota("Recordar pedir resultados de análisis de Hulk antes del viernes.");
        agregarNota("Reponer stock de Amoxicilina 500mg en mostrador.");
        agregarNota("Llamar al laboratorio por la muestra de Cheese (vencida la parvovirus).");
        agregarNota("Mishi está respondiendo bien al tratamiento dermatológico.");
    }

    private Animal buscarAnimal(String nombre) {
        for (Responsable c : veterinaria.getListaClientes()) {
            for (Animal a : c.getMascotas()) {
                if (nombre.equals(a.getNombre())) return a;
            }
        }
        return null;
    }
}
