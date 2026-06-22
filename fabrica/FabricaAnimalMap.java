package fabrica;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import modelo.*;

public class FabricaAnimalMap {
    private static Map<String, Class<? extends Animal>> registro = new HashMap<>();

    static {
        registro.put("perro",   Perro.class);
        registro.put("gato",    Gato.class);
        registro.put("conejo", Conejo.class);
        registro.put("tortuga", Tortuga.class);
        registro.put("loro",  Loro.class);
    }

    public static Animal crear(String tipo, String nombre, LocalDate fechaNacimiento, boolean sexo, float peso, Responsable responsable, String raza) {
        Class<? extends Animal> clase = registro.get(tipo.toLowerCase().trim());
        
        if (clase == null) {
            throw new IllegalArgumentException("El tipo de animal '" + tipo + "' no está registrado en el sistema.");
        }
        
        try {
            return clase.getDeclaredConstructor(
                String.class, LocalDate.class, boolean.class, float.class, Responsable.class, String.class
            ).newInstance(nombre, fechaNacimiento, sexo, peso, responsable, raza);
            
        } catch (Exception e) {
            throw new RuntimeException("Error crítico al fabricar el animal tipo '" + tipo + "'. Verifique sus constructores hilos.", e);
        }
    }

    public static void registrar(String tipo, Class<? extends Animal> clase) {
        registro.put(tipo, clase);
    }
}
