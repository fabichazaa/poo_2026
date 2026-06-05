package objetos;

public class Turno {
    private int idTurno;
    private String fecha;
    private String hora;
    private Veterinario veterinario; // Asociado a un Veterinario
    private Animal animal;           // Asociado a un Animal
    private String estado;           // "Pendiente" o "Completado"1private String estado;           // "Pendiente" o "Completado"
    private TipoTurno tipo;          // <-- NUEVO ATRIBUTO ENUM

    public Turno(int idTurno, String fecha, String hora, Veterinario veterinario, Animal animal, TipoTurno tipo) {
        this.idTurno = idTurno;
        this.fecha = fecha;
        this.hora = hora;
        this.veterinario = veterinario;
        this.animal = animal;
        this.estado = "Pendiente";
        this.tipo = tipo;
    }

    public Veterinario getVeterinario() { return veterinario; }
    public Animal getAnimal() { return animal; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getEstado() { return estado; }
    public TipoTurno getTipo() { return tipo; } // <-- Geter del Enum
    public void completarTurno() { this.estado = "Completado"; }
}   