import controlador.ControladorVeterinaria;
import modelo.*;
import vista.PortalVeterinario;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("--consola")) {
            ejecutarDemoConsola();
        } else {
            ControladorVeterinaria controlador = ControladorVeterinaria.getInstancia();
            
            // TEMP: Auto-login for development (skip login dialog)
            controlador.loginPorMatricula("MP-9854");
            
            if (controlador.haySesionActiva()) {
                PortalVeterinario.main(new String[]{});
            } else {
                System.out.println("Login automático falló. Saliendo.");
                System.exit(0);
            }
        }
    }

    private static void ejecutarDemoConsola() {
        Veterinaria miVeterinaria = new Veterinaria("Patitas Felices", true);

        Direccion dir1 = new Direccion("Las Glicinas", 925, "Del Viso");
        Direccion dir2 = new Direccion("Av. Libertador", 5579, "Belgrano");
        Direccion dir3 = new Direccion("Las Azucenas", 356, "Del Viso");
        Direccion dir4 = new Direccion("Los Lagos", 81, "Tigre");

        Veterinario vet1 = new Veterinario("7895457", "Paola", "Lopez", "+54 11 1111-2222", dir1, "MAT-001");
        Veterinario vet2 = new Veterinario("6343431", "Carlos", "Gomez", "+54 11 3333-4444", dir2, "MAT-002");
        miVeterinaria.registrarVeterinario(vet1);
        miVeterinaria.registrarVeterinario(vet2);

        Responsable cliente1 = new Responsable("1464564", "Gladys", "Decima", "+54 11 5555-6666", dir3);
        Responsable cliente2 = new Responsable("9876543", "Juan", "García", "+54 11 7777-8888", dir4);
        miVeterinaria.registrarCliente(cliente1);
        miVeterinaria.registrarCliente(cliente2);

        Animal mascota1 = new Perro("Perla", java.time.LocalDate.of(2009, 2, 20), false, 8.5f, cliente1, "Salchicha");
        Animal mascota2 = new Gato("Honey", java.time.LocalDate.of(2019, 11, 5), false, 4.3f, cliente2, "Británico de Pelo Corto");
        cliente1.agregarMascota(mascota1);
        cliente2.agregarMascota(mascota2);

        mascota1.getHistorial().recetarMedicamento(miVeterinaria.getCatalogoMedicamentos().get(0));

        Turno t1 = new Turno(1, "06/06/2026", "10:00", vet1, mascota1, TipoTurno.CONSULTA_GENERAL,"");
        Turno t2 = new Turno(2, "06/06/2026", "10:30", vet2, mascota2, TipoTurno.VACUNACION,"");
        miVeterinaria.registrarTurno(t1);
        miVeterinaria.registrarTurno(t2);

        System.out.println(miVeterinaria.getNombreNegocio());
        System.out.println("Veterinarios cargados: " + miVeterinaria.getListaVeterinarios().size());
        System.out.println("Clientes cargados: " + miVeterinaria.getListaClientes().size());
        System.out.println("Turnos cargados: " + miVeterinaria.getListaTurnos().size());
        System.out.println("Medicamentos en catálogo: " + miVeterinaria.getCatalogoMedicamentos().size());
        System.out.println();
        System.out.println(mascota1);
        System.out.println(mascota2);
        System.out.println();

        ComprobanteTurno comp = new ComprobanteTurno(t1, miVeterinaria.getNombreNegocio());
        System.out.println(comp.generarTextoCompleto());
    }
}