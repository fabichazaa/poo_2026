
import java.time.LocalDate;
import objetos.*;

public class Main {
    public static void main(String[] args) {
        Veterinaria miVeterinaria = new Veterinaria("Patitas Felices");

        Direccion dir1 = new Direccion("Las Glicinas", 925, "Del Viso");
        Direccion dir2 = new Direccion("Av. Libertador", 5579, "Belgrano");
        Direccion dir3 = new Direccion("Las Azucenas", 356, "Del Viso");
        Direccion dir4 = new Direccion("Los Lagos", 81, "Tigre");


        // Crear algunos veterinarios
        Veterinario vet1 = new Veterinario("7895457", "Paola", "Lopez", dir1, "MAT-001");
        Veterinario vet2 = new Veterinario("6343431", "Carlos", "Gomez", dir2, "MAT-002");

        miVeterinaria.registrarVeterinario(vet1);
        miVeterinaria.registrarVeterinario(vet2);

        Responsable cliente1 = new Responsable("1464564", "Gladys", "Decima", dir3);
        Responsable cliente2 = new Responsable("9876543", "Juan", "García", dir4);

        miVeterinaria.registrarCliente(cliente1);
        miVeterinaria.registrarCliente(cliente2);

        LocalDate fechaNac1 = LocalDate.of(2009, 2, 20);
        Animal mascota1 = new Perro("Perla", fechaNac1, false,  cliente1 , "Salchiha");
        LocalDate fechaNac2 = LocalDate.of(2019, 11, 5);
        Animal mascota2 = new Gato("Honey", fechaNac2, false, cliente2, "Britanico de Pelo Corto");
        cliente1.agregarMascota(mascota1);
        cliente2.agregarMascota(mascota2);

        
        
    }
}   