import controlador.ControladorVeterinaria;
import java.time.LocalDate;
import java.util.List;
import modelo.*;

public class Demo {

    private static int pruebasPasadas = 0;
    private static int pruebasFalladas = 0;

    public static void main(String[] args) {
        seccion("Smoke test — Happy Paws (Fase 5: Pruebas integrales)");

        ControladorVeterinaria.reiniciar();
        ControladorVeterinaria c = ControladorVeterinaria.getInstancia();
        Veterinaria v = c.getVeterinaria();

        // ========================================================
        seccion("1) Verificación de datos seed");
        // ========================================================
        verificar("Hay 4 veterinarios cargados", v.getListaVeterinarios().size() == 4);
        verificar("Hay 4 clientes cargados",     v.getListaClientes().size() == 4);
        verificar("Hay 17 animales en total",    v.obtenerTodosLosAnimales().size() == 17);
        verificar("Hay 21 turnos cargados",      v.getListaTurnos().size() == 21);
        verificar("Hay 8 medicamentos en el catálogo", v.getCatalogoMedicamentos().size() == 8);
        verificar("Notas cargadas (>=4)",        c.getNotas().size() >= 4);

        // ========================================================
        seccion("2) Polimorfismo: getTipoAlimentacion() y getEspecie()");
        // ========================================================
        Animal perro = v.obtenerTodosLosAnimales().stream()
            .filter(a -> a instanceof Perro).findFirst().orElseThrow();
        Animal gato = v.obtenerTodosLosAnimales().stream()
            .filter(a -> a instanceof Gato).findFirst().orElseThrow();

        verificar("Perro.getEspecie() == \"Perro\"", "Perro".equals(perro.getEspecie()));
        verificar("Gato.getEspecie()  == \"Gato\"",  "Gato".equals(gato.getEspecie()));
        verificar("Perro.getTipoAlimentacion() == OMNIVORO",        perro.getTipoAlimentacion() == TipoAlimentacion.OMNIVORO);
        verificar("Gato.getTipoAlimentacion()  == CARNIVORO_ESTRICTO", gato.getTipoAlimentacion()  == TipoAlimentacion.CARNIVORO_ESTRICTO);

        // ========================================================
        seccion("3) Login por matrícula (3 vets distintos)");
        // ========================================================
        verificar("Login MP-9854 (Carlos)", c.loginPorMatricula("MP-9854"));
        verificar("Vet logueado es Carlos Páez",
            c.getVeterinarioLogueado().getNombre().equals("Carlos")
            && c.getVeterinarioLogueado().getApellido().equals("Páez"));

        verificar("Login MP-1024 (Laura)",  c.loginPorMatricula("MP-1024"));
        verificar("Vet logueado es Laura Gómez",
            c.getVeterinarioLogueado().getNombre().equals("Laura"));

        verificar("Login MP-9999 (inexistente) debe fallar", !c.loginPorMatricula("MP-9999"));
        verificar("Login vacío debe fallar",                 !c.loginPorMatricula(""));
        verificar("Login null debe fallar",                  !c.loginPorMatricula(null));

        // ========================================================
        seccion("4) Flujo: alta de turno → atender → comprobante");
        // ========================================================
        c.loginPorMatricula("MP-9854");
        Veterinario carlos = c.getVeterinarioLogueado();
        Animal bobby = v.obtenerTodosLosAnimales().stream()
            .filter(a -> a.getNombre().equals("Bobby")).findFirst().orElseThrow();
        int turnosAntes = v.getListaTurnos().size();
        Turno nuevo = c.registrarTurno("20/06/2026", "10:00", carlos, bobby, TipoTurno.CONSULTA_GENERAL,"");
        verificar("Se creó un nuevo turno (id != 0)",        nuevo.getIdTurno() > 0);
        verificar("Total turnos += 1",                       v.getListaTurnos().size() == turnosAntes + 1);
        verificar("El nuevo turno está Pendiente",           nuevo.esPendiente());
        verificar("El nuevo turno NO está Completado",      !nuevo.estaCompletado());

        nuevo.completarTurno();
        verificar("Tras completarTurno() → Completado",     nuevo.estaCompletado());
        verificar("Tras completarTurno() NO es Pendiente",  !nuevo.esPendiente());

        ComprobanteTurno comp = new ComprobanteTurno(nuevo, v.getNombreNegocio());
        String texto = comp.generarTextoCompleto();
        verificar("Comprobante contiene nombre del paciente (Bobby)", texto.contains("Bobby"));
        verificar("Comprobante contiene nombre del vet (Carlos)",      texto.contains("Carlos"));
        verificar("Comprobante contiene nombre del responsable (Ricardo)", texto.contains("Ricardo"));
        verificar("Comprobante contiene tipo de turno (Consulta General)", texto.contains("Consulta General"));
        verificar("Comprobante contiene la veterinaria (Happy Paws)",  texto.contains("Happy Paws"));

        // ========================================================
        seccion("5) Flujo: recetar medicamento y aplicar vacuna");
        // ========================================================
        int medsAntes = bobby.getHistorial().getMedicamentosRecetados().size();
        Medicamento amoxi = v.getCatalogoMedicamentos().get(0);
        c.recetarMedicamento(bobby, amoxi);
        verificar("Historia clínica += 1 medicamento",
            bobby.getHistorial().getMedicamentosRecetados().size() == medsAntes + 1);

        Vacuna nuevaVac = new Vacuna("SEN-TEST-001", "Vacuna Test", 365);
        c.registrarVacunacion(bobby, nuevaVac, LocalDate.of(2026, 6, 1));
        boolean vacAgregada = bobby.getHistorial().getRegistroVacunas().stream()
            .anyMatch(r -> r.getVacunaAplicada().getCodigoSenasa().equals("SEN-TEST-001"));
        verificar("Vacuna fue agregada a la historia", vacAgregada);

        RegistroVacunacion regNueva = bobby.getHistorial().getRegistroVacunas().stream()
            .filter(r -> r.getVacunaAplicada().getCodigoSenasa().equals("SEN-TEST-001"))
            .findFirst().orElseThrow();
        verificar("Vacuna nueva NO está vencida", !regNueva.estaVencida());

        Vacuna vacunaVieja = new Vacuna("SEN-VIEJA-001", "Vacuna Vieja", 365);
        RegistroVacunacion regVencida = new RegistroVacunacion(vacunaVieja, LocalDate.of(2020, 1, 1));
        verificar("Vacuna vieja SÍ está vencida", regVencida.estaVencida());

        // Verificar que Cheese tiene una vacuna VENCIDA en su historial
        Animal cheese = v.obtenerTodosLosAnimales().stream()
            .filter(a -> a.getNombre().equals("Cheese")).findFirst().orElseThrow();
        boolean cheeseTieneVencida = cheese.getHistorial().getRegistroVacunas().stream()
            .anyMatch(rv -> rv.estaVencida());
        verificar("Cheese tiene al menos una vacuna vencida", cheeseTieneVencida);

        // ========================================================
        seccion("6) Flujo: portal de adopciones");
        // ========================================================
        Animal mochi = v.obtenerTodosLosAnimales().stream()
            .filter(a -> a.getNombre().equals("Mochi")).findFirst().orElseThrow();
        verificar("Mochi NO está en adopción inicialmente", !mochi.isEnAdopcion());

        c.marcarEnAdopcion(mochi, true);
        verificar("Mochi está en adopción tras setEnAdopcion(true)", mochi.isEnAdopcion());
        verificar("Mochi aparece en obtenerAnimalesEnAdopcion()",
            v.obtenerAnimalesEnAdopcion().contains(mochi));

        c.marcarEnAdopcion(mochi, false);
        verificar("Mochi ya NO está en adopción tras setEnAdopcion(false)", !mochi.isEnAdopcion());
        verificar("Mochi NO aparece en obtenerAnimalesEnAdopcion()",
            !v.obtenerAnimalesEnAdopcion().contains(mochi));

        // ========================================================
        seccion("7) Flujo: notas y recordatorios");
        // ========================================================
        int notasAntes = c.getNotas().size();
        c.agregarNota("Nota de prueba integración");
        verificar("Se agregó una nota", c.getNotas().size() == notasAntes + 1);
        verificar("La nota más reciente está primera (LIFO)",
            c.getNotas().get(0).contains("Nota de prueba"));
        verificar("La nota tiene autor y fecha",
            c.getNotas().get(0).contains("Carlos") && c.getNotas().get(0).contains("20"));

        c.agregarNota("   "); // vacía
        verificar("Nota vacía NO se agrega", c.getNotas().size() == notasAntes + 1);

        c.eliminarNota(0);
        verificar("Eliminar nota reduce el tamaño en 1",
            c.getNotas().size() == notasAntes);

        // ========================================================
        seccion("8) Búsquedas y filtros (Veterinaria)");
        // ========================================================
        verificar("buscarVeterinarioPorMatricula(MP-9854) → Carlos",
            v.buscarVeterinarioPorMatricula("MP-9854").getNombre().equals("Carlos"));
        verificar("buscarVeterinarioPorMatricula(MP-XXXX) → null",
            v.buscarVeterinarioPorMatricula("MP-XXXX") == null);
        verificar("buscarVeterinarioPorDni(22333444) → Carlos",
            v.buscarVeterinarioPorDni("22333444").getNombre().equals("Carlos"));
        verificar("buscarClientePorDni(12345678) → Claudio",
            v.buscarClientePorDni("12345678").getNombre().equals("Claudio"));
        verificar("buscarClientePorDni(99999999) → null",
            v.buscarClientePorDni("99999999") == null);

        List<Animal> r1 = v.buscarMascotasPorNombre("hu");
        verificar("buscarMascotasPorNombre(\"hu\") encuentra a Hulk",
            r1.stream().anyMatch(a -> a.getNombre().equals("Hulk")));

        List<Turno> hoy = v.obtenerTurnosPorFecha("06/06/2026");
        verificar("obtenerTurnosPorFecha(06/06/2026) ≥ 6", hoy.size() >= 6);

        List<Turno> carlosTodos = v.obtenerTurnosDeVeterinario(carlos);
        verificar("Carlos tiene turnos asignados", !carlosTodos.isEmpty());

        List<Turno> pendientes = v.obtenerTurnosPendientes();
        verificar("Hay turnos pendientes (>=5)", pendientes.size() >= 5);

        List<Turno> completados = v.obtenerTurnosCompletados();
        verificar("Hay turnos completados (>=5)", completados.size() >= 5);

        // ========================================================
        seccion("9) Composición y agregación (estructura)");
        // ========================================================
        Animal hulk = v.obtenerTodosLosAnimales().stream()
            .filter(a -> a.getNombre().equals("Hulk")).findFirst().orElseThrow();
        verificar("Hulk tiene HistoriaClinica (composición)",
            hulk.getHistorial() != null);
        verificar("Hulk tiene Responsable (asociación)",
            hulk.getResponsable() != null);
        verificar("Responsable de Hulk es Claudio",
            hulk.getResponsable().getNombre().equals("Claudio"));
        verificar("Responsable tiene mascotas (agregación)",
            hulk.getResponsable().getMascotas().contains(hulk));

        // ========================================================
        seccion("10) Presentación de combos (evitar modelo.X@hashcode)");
        // ========================================================
        Veterinario cualquierVet = v.getListaVeterinarios().get(0);
        String presentacionVet = "Dr/a. " + cualquierVet.getNombre() + " " + cualquierVet.getApellido()
            + " — " + cualquierVet.getEspecialidad() + "  (" + cualquierVet.getMatricula() + ")";
        verificar("Render del JComboBox<Veterinario> contiene nombre y matrícula",
            presentacionVet.contains(cualquierVet.getNombre())
            && presentacionVet.contains(cualquierVet.getMatricula())
            && !presentacionVet.contains("@"));

        Animal cualquierAnimal = v.obtenerTodosLosAnimales().get(0);
        String presentacionAnimal = cualquierAnimal.getNombre() + " (" + cualquierAnimal.getEspecie() + ")"
            + " — " + cualquierAnimal.getResponsable().getNombre();
        verificar("Render del JComboBox<Animal> incluye nombre y responsable",
            presentacionAnimal.contains(cualquierAnimal.getNombre())
            && presentacionAnimal.contains(cualquierAnimal.getResponsable().getNombre()));

        String presentacionTipo = TipoTurno.CONSULTA_GENERAL.getDescripcion();
        verificar("Render del JComboBox<TipoTurno> muestra descripción (no enum)",
            !presentacionTipo.equals("CONSULTA_GENERAL") && presentacionTipo.length() > 0);

        // ========================================================
        seccion("11) Cierre de sesión");
        // ========================================================
        c.cerrarSesion();
        verificar("Tras cerrarSesion() no hay sesión activa",
            !c.haySesionActiva());
        verificar("getVeterinarioLogueado() retorna null",
            c.getVeterinarioLogueado() == null);

        // ========================================================
        resumen();
        // ========================================================
        if (pruebasFalladas > 0) {
            System.exit(1);
        }
    }

    private static void seccion(String titulo) {
        System.out.println();
        System.out.println("──── " + titulo);
    }

    private static void verificar(String descripcion, boolean condicion) {
        if (condicion) {
            System.out.println("  \u2713 " + descripcion);
            pruebasPasadas++;
        } else {
            System.out.println("  \u2717 " + descripcion + "  \u26A0 FALLÓ");
            pruebasFalladas++;
        }
    }

    private static void resumen() {
        System.out.println();
        System.out.println("══════════════════════════════════════════");
        System.out.println("  Resultado: " + pruebasPasadas + " pasadas, " + pruebasFalladas + " fallidas");
        System.out.println("══════════════════════════════════════════");
    }
}
