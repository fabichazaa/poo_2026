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
        if (matricula == null || matricula.isBlank()) {
            return false;
        }
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

    public Turno registrarTurno(String fecha, String hora, Veterinario vet, Animal animal, TipoTurno tipo, String observaciones) {
        int nuevoId = veterinaria.getListaTurnos().size() + 1;
        Turno t = new Turno(nuevoId, fecha, hora, vet, animal, tipo, observaciones);
        veterinaria.registrarTurno(t);
        return t;
    }

    public void recetarMedicamento(Animal animal, Medicamento med) {
        if (animal != null && med != null) {
            animal.getHistorial().recetarMedicamento(med);
        }
    }

    public void recetarMedicamento(Animal animal, Prescripcion pres) {
        if (animal != null && pres != null) {
            animal.getHistorial().recetarMedicamento(pres);
        }
    }

    public void registrarVacunacion(Animal animal, Vacuna vacuna, LocalDate fechaAplicacion) {
        if (animal != null && vacuna != null && fechaAplicacion != null) {
            RegistroVacunacion registro = new RegistroVacunacion(vacuna, fechaAplicacion);
            animal.getHistorial().registrarVacuna(registro);
        }
    }

    public void registrarVacunacion(Animal animal, RegistroVacunacion registro) {
        if (animal != null && registro != null) {
            animal.getHistorial().registrarVacuna(registro);
        }
    }

    public void marcarEnAdopcion(Animal animal, boolean enAdopcion) {
        if (animal != null) {
            animal.setEnAdopcion(enAdopcion);
        }
    }

    public void agregarNota(String texto) {
        if (texto == null || texto.isBlank()) {
            return;
        }
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
        if (v == null) {
            return 0;
        }
        int n = 0;
        for (Turno t : veterinaria.getListaTurnos()) {
            if (!v.equals(t.getVeterinario())) {
                continue;
            }
            if (estado == null) {
                n++;
                continue;
            }
            if (estado.equals(t.getEstado())) {
                n++;
            }
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
        // 🔥 MODIFICADO: Agregamos el parámetro celular intermedio que hereda de Persona
        Veterinario vet1 = new Veterinario("22333444", "Carlos", "Páez", "+54 11 9999-8888", null, "MP-9854");
        vet1.setEspecialidad("Cirugía");
        vet1.setTurnoTrabajo("Mañana");
        veterinaria.registrarVeterinario(vet1);

        Veterinario vet2 = new Veterinario("55555555", "Laura", "Gómez", "+54 11 9999-7777", null, "MP-1024");
        vet2.setEspecialidad("Clínica médica");
        vet2.setTurnoTrabajo("Tarde");
        veterinaria.registrarVeterinario(vet2);

        Veterinario vet3 = new Veterinario("38765432", "Ana", "Ruiz", "+54 11 9999-6666", null, "MP-2050");
        vet3.setEspecialidad("Dermatología");
        vet3.setTurnoTrabajo("Mañana");
        veterinaria.registrarVeterinario(vet3);

        Veterinario vet4 = new Veterinario("42987654", "Miguel", "Torres", "+54 11 9999-5555", null, "MP-3080");
        vet4.setEspecialidad("Cardiología");
        vet4.setTurnoTrabajo("Tarde");
        veterinaria.registrarVeterinario(vet4);
    }

    private void cargarClientesYPacientes() {
        Direccion dir1 = new Direccion("Av. Siempre Viva", 742, "Springfield");
        Direccion dir2 = new Direccion("Calle de los Gatos", 123, "Palermo");
        Direccion dir3 = new Direccion("Boulevard de los Perros", 456, "Recoleta");
        Direccion dir4 = new Direccion("Pasaje San Roque", 89, "San Telmo");

        Responsable c1 = new Responsable("12345678", "Claudio", "Chiqui", "+54 11 3476 3465", dir1);
        Responsable c2 = new Responsable("24678901", "Marta", "Sánchez","+54 11 3476 3465", dir2);
        Responsable c3 = new Responsable("31456789", "Ricardo", "López","+54 11 3476 3465", dir3);
        Responsable c4 = new Responsable("40876543", "Patricia", "Fernández", "+54 11 3476 3465",dir4);
        veterinaria.registrarCliente(c1);
        veterinaria.registrarCliente(c2);
        veterinaria.registrarCliente(c3);
        veterinaria.registrarCliente(c4);

        // 🔥 MODIFICADO: Inyectamos el parámetro flotante del peso (ej: 14.5f, 4.2f) según requiere el constructor
        // Claudio (c1)
        c1.agregarMascota(new Perro("Hulk",     LocalDate.of(2023, 4, 15), true,  28.0f, c1, "Dogo de Burdeos"));
        c1.agregarMascota(new Gato ("Luna",     LocalDate.of(2025, 8, 20), false, 4.2f,  c1, "Siamés"));
        c1.agregarMascota(new Perro("Cheese",   LocalDate.of(2018, 1, 10), true,  11.5f, c1, "Beagle"));
        c1.agregarMascota(new Gato ("Mochi",    LocalDate.of(2024, 2, 14), false, 3.8f,  c1, "Siamés"));
        c1.agregarMascota(new Loro ("Pepe",     LocalDate.of(2021, 5, 10), true,  0.4f,  c1, "Amazonas"));
        c1.agregarMascota(new Conejo("Copito",  LocalDate.of(2025, 1, 12), false, 1.8f,  c1, "Cabeza de León"));

        // Marta (c2)
        c2.agregarMascota(new Gato ("Mishi",     LocalDate.of(2022, 11, 5), false, 4.0f,  c2, "Persa"));
        c2.agregarMascota(new Gato ("Bigotes",   LocalDate.of(2020, 6, 18), true,  6.5f,  c2, "Maine Coon"));
        c2.agregarMascota(new Perro("Toby",      LocalDate.of(2019, 9, 22), true,  5.2f,  c2, "Caniche"));
        c2.agregarMascota(new Tortuga("Manuelita", LocalDate.of(1996, 1, 1), true, 2.1f,  c2, "Terrestre"));
        c2.agregarMascota(new Loro ("Paquito",   LocalDate.of(2015, 3, 22), false, 0.5f,  c2, "Gris Africano"));

        // Ricardo (c3)
        c3.agregarMascota(new Perro("Bobby", LocalDate.of(2021, 3, 11), true,  24.3f, c3, "Golden Retriever"));
        c3.agregarMascota(new Perro("Rocky", LocalDate.of(2017, 7, 4),  true,  13.1f, c3, "Bulldog Francés"));
        c3.agregarMascota(new Conejo("Tambor", LocalDate.of(2025, 3, 15), false, 2.0f,  c3, "Mini Lop"));
        c3.agregarMascota(new Tortuga("Rafaela", LocalDate.of(2005, 10, 8), false, 1.9f,  c3, "Caja"));

        // Patricia (c4)
        c4.agregarMascota(new Gato ("Pelusa",    LocalDate.of(2024, 5, 30), false, 3.5f,  c4, "Angora Turco"));
        c4.agregarMascota(new Conejo("Orejas",   LocalDate.of(2024, 8, 4),  true,  2.2f,  c4, "Angora"));

        // Bucle automatizado de registro
        for (Animal a : c1.getMascotas()) veterinaria.registrarPaciente(a);
        for (Animal a : c2.getMascotas()) veterinaria.registrarPaciente(a);
        for (Animal a : c3.getMascotas()) veterinaria.registrarPaciente(a);
        for (Animal a : c4.getMascotas()) veterinaria.registrarPaciente(a);

        // --- Configuración de Animales Inactivos ---
        Animal animalInactivo1 = buscarAnimal("Cheese");
        if (animalInactivo1 != null) {
            animalInactivo1.setActivo(false);
        }

        Animal animalInactivo2 = buscarAnimal("Manuelita");
        if (animalInactivo2 != null) {
            animalInactivo2.setActivo(false);
        }

        Animal animalInactivo3 = buscarAnimal("Orejas");
        if (animalInactivo3 != null) {
            animalInactivo3.setActivo(false);
        }
    }
    
    private void cargarMedicamentosYVacunas() {
        Animal hulk   = buscarAnimal("Hulk");
        Animal luna   = buscarAnimal("Luna");
        Animal mishi  = buscarAnimal("Mishi");
        Animal bobby  = buscarAnimal("Bobby");
        Animal cheese = buscarAnimal("Cheese");

        if (hulk != null) {
            hulk.getHistorial().recetarMedicamento(veterinaria.getCatalogoMedicamentos().get(0));
            Vacuna v1 = new Vacuna("SEN-VAC-001", "Antirrábica", 365);
            hulk.getHistorial().registrarVacuna(new RegistroVacunacion(v1, LocalDate.of(2025, 6, 1)));
            hulk.setEnAdopcion(false);
        }
        if (luna != null) {
            Vacuna v2 = new Vacuna("SEN-VAC-002", "Triple Felina", 365);
            luna.getHistorial().registrarVacuna(new RegistroVacunacion(v2, LocalDate.of(2025, 2, 10)));
        }
        if (cheese != null) {
            Vacuna v3 = new Vacuna("SEN-VAC-003", "Parvovirus (vencida)", 365);
            cheese.getHistorial().registrarVacuna(new RegistroVacunacion(v3, LocalDate.of(2023, 1, 15)));
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

        Animal hulk = buscarAnimal("Hulk");
        Animal luna = buscarAnimal("Luna");
        Animal cheese = buscarAnimal("Cheese");
        Animal mochi = buscarAnimal("Mochi");
        Animal mishi = buscarAnimal("Mishi");
        Animal bigotes = buscarAnimal("Bigotes");
        Animal bobby = buscarAnimal("Bobby");
        Animal rocky = buscarAnimal("Rocky");
        Animal toby = buscarAnimal("Toby");
        Animal pelusa = buscarAnimal("Pelusa");
        
        // Variables recuperadas de pacientes especiales
        Animal pepe = buscarAnimal("Pepe");
        Animal tambor = buscarAnimal("Tambor");
        Animal manuelita = buscarAnimal("Manuelita");
        Animal copito = buscarAnimal("Copito");
        Animal orejas = buscarAnimal("Orejas");

        String b1 = "02/06/2026";
        String b2 = "04/06/2026";
        String b3 = "06/06/2026";
        String b4 = "08/06/2026";
        String b5 = "12/06/2026";

        // --- Turnos Pasados ---
        Turno tPasado1 = new Turno(1, b1, "09:00", vet1, hulk, TipoTurno.CIRUGIA, "Castración inminente :(");
        Turno tPasado2 = new Turno(2, b1, "11:00", vet2, mishi, TipoTurno.CONSULTA_GENERAL, "Observación dientes");
        Turno tPasado3 = new Turno(3, b2, "10:30", vet1, cheese, TipoTurno.ANALISIS, "Orina muy oscura");
        Turno tPasado4 = new Turno(4, b2, "16:00", vet3, bobby, TipoTurno.CONSULTA_GENERAL, "Pelaje se cae mucho");
        Turno tPasado1c = new Turno(5, b5, "08:30", vet1, luna, TipoTurno.VACUNACION, "Anti-Parasitaria");
        Turno tPasadoLoro = new Turno(17, b1, "14:30", vet2, pepe, TipoTurno.CONSULTA_GENERAL, "Revisión de plumas");
        
        tPasado1.completarTurno();
        tPasado2.completarTurno();
        tPasado3.completarTurno();
        tPasado4.completarTurno();
        tPasado1c.completarTurno();
        tPasadoLoro.completarTurno();

        // --- Turnos de Hoy ---
        Turno tHoy1 = new Turno(6, b3, "09:30", vet1, hulk, TipoTurno.CIRUGIA, "Castración inminente :(");
        Turno tHoy2 = new Turno(7, b3, "10:15", vet1, luna, TipoTurno.CONSULTA_GENERAL, "");
        Turno tHoy3 = new Turno(8, b3, "11:45", vet1, hulk, TipoTurno.ANALISIS, "");
        Turno tHoy4 = new Turno(9, b3, "14:00", vet1, mochi, TipoTurno.VACUNACION, "");
        Turno tHoy5 = new Turno(10, b3, "15:30", vet2, mishi, TipoTurno.BANIO, "");
        Turno tHoy6 = new Turno(11, b3, "16:00", vet4, rocky, TipoTurno.CONSULTA_GENERAL, "");
        
        // 🔥 RESTAURADO: Turnos perdidos de los pacientes de la rama de Fabi
        Turno tHoyConejo = new Turno(18, b3, "16:45", vet1, tambor, TipoTurno.VACUNACION, "Control anual");
        Turno tHoyTortuga = new Turno(19, b3, "17:30", vet2, manuelita, TipoTurno.CONSULTA_GENERAL, "Chequeo caparazón");

        // --- Turnos Futuros ---
        Turno tFut1 = new Turno(12, b4, "10:00", vet1, bigotes, TipoTurno.CIRUGIA, "");
        Turno tFut2 = new Turno(13, "10/06/2026", "11:00", vet1, cheese, TipoTurno.VACUNACION, "");
        Turno tFut3 = new Turno(14, "12/06/2026", "09:00", vet2, toby, TipoTurno.CONSULTA_GENERAL, "");
        Turno tFut4 = new Turno(15, "15/06/2026", "17:00", vet3, pelusa, TipoTurno.ANALISIS, "");
        
        // 🔥 RESTAURADO: Turnos futuros perdidos de la rama de Fabi
        Turno tFutConejo2 = new Turno(20, b4, "11:30", vet1, copito, TipoTurno.ANALISIS, "Control digestivo");
        Turno tFutConejo3 = new Turno(21, "11/06/2026", "15:00", vet3, orejas, TipoTurno.CONSULTA_GENERAL, "Revisión oreja izquierda");

        // --- Turnos Cancelados ---
        Turno tCancel = new Turno(16, b3, "18:00", vet1, luna, TipoTurno.CONSULTA_GENERAL, "");
        tCancel.cancelarTurno();

        // =========================================================================
        // 🔥 NUEVO: SET DE PRUEBAS DE TODOS LOS TIPOS DE TURNO ASOCIADOS A HULK
        // =========================================================================
        Turno tHulkConsulta  = new Turno(22, "10/06/2026", "08:30", vet2, hulk, TipoTurno.CONSULTA_GENERAL, "Chequeo de tos leve.");
        Turno tHulkVacuna    = new Turno(23, "11/06/2026", "09:00", vet1, hulk, TipoTurno.VACUNACION, "Refuerzo Quíntuple.");
        Turno tHulkCirugia   = new Turno(24, "12/06/2026", "11:15", vet1, hulk, TipoTurno.CIRUGIA, "Limpieza sarro dental.");
        Turno tHulkBanio     = new Turno(25, "13/06/2026", "15:00", vet2, hulk, TipoTurno.BANIO, "Baño sanitario medicado por pulgas.");
        Turno tHulkAnalisis  = new Turno(26, "14/06/2026", "16:30", vet3, hulk, TipoTurno.ANALISIS, "Muestra raspaje de piel.");
        Turno tHulkSeguimiento= new Turno(27, "15/06/2026", "10:00", vet1, hulk, TipoTurno.SEGUIMIENTO, "Control de evolución dermatológica.");

        // Simulamos que algunos ya pasaron y fueron completados para llenar el Historial
        tHulkConsulta.completarTurno();
        tHulkVacuna.completarTurno();
        tHulkAnalisis.completarTurno();

        // Registro unificado en el sistema (Sin colisiones ni superposiciones)
        veterinaria.registrarTurno(tPasado1);
        veterinaria.registrarTurno(tPasado2);
        veterinaria.registrarTurno(tPasado3);
        veterinaria.registrarTurno(tPasado4);
        veterinaria.registrarTurno(tPasado1c);
        veterinaria.registrarTurno(tPasadoLoro);
        veterinaria.registrarTurno(tHoy1);
        veterinaria.registrarTurno(tHoy2);
        veterinaria.registrarTurno(tHoy3);
        veterinaria.registrarTurno(tHoy4);
        veterinaria.registrarTurno(tHoy5);
        veterinaria.registrarTurno(tHoy6);
        veterinaria.registrarTurno(tHoyConejo);
        veterinaria.registrarTurno(tHoyTortuga);
        veterinaria.registrarTurno(tFut1);
        veterinaria.registrarTurno(tFut2);
        veterinaria.registrarTurno(tFut3);
        veterinaria.registrarTurno(tFut4);
        veterinaria.registrarTurno(tFutConejo2);
        veterinaria.registrarTurno(tFutConejo3);
        veterinaria.registrarTurno(tCancel); 

        // Registramos las nuevas variables de Hulk en el sistema
        veterinaria.registrarTurno(tHulkConsulta);
        veterinaria.registrarTurno(tHulkVacuna);
        veterinaria.registrarTurno(tHulkCirugia);
        veterinaria.registrarTurno(tHulkBanio);
        veterinaria.registrarTurno(tHulkAnalisis);
        veterinaria.registrarTurno(tHulkSeguimiento);
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
                if (nombre.equals(a.getNombre())) {
                    return a;
                }
            }
        }
        return null;
    }
}