package modelo;

public class Turno {

    public static final String ESTADO_PENDIENTE = "Pendiente";
    public static final String ESTADO_COMPLETADO = "Completado";
    public static final String ESTADO_CANCELADO = "Cancelado";

    private int idTurno;
    private String fecha;
    private String hora;
    private Veterinario veterinario;
    private Animal animal;
    private String estado;
    private TipoTurno tipo;
    private String observaciones;

    public Turno(int idTurno, String fecha, String hora, Veterinario veterinario, Animal animal, TipoTurno tipo, String observaciones) {
        this.idTurno = idTurno;
        this.fecha = fecha;
        this.hora = hora;
        this.veterinario = veterinario;
        this.animal = animal;
        this.estado = ESTADO_PENDIENTE;
        this.tipo = tipo;
        this.observaciones = observaciones;
        // Not registering 'this' with Veterinario here to avoid leaking
        // the partially-constructed object from the constructor.
    }

    public Turno(String fecha, String hora, Veterinario veterinario, Animal animal, TipoTurno tipo) {
        this(0, fecha, hora, veterinario, animal, tipo, "");
    }

    public int getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    /**
     * Registra este Turno en el Veterinario asociado.
     * Llamar después de construir el objeto para evitar "this" escape en el constructor.
     */
    public void registrarEnVeterinario() {
        if (this.veterinario != null) {
            this.veterinario.agregarTurno(this);
        }
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TipoTurno getTipo() {
        return tipo;
    }

    public void setTipo(TipoTurno tipo) {
        this.tipo = tipo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void completarTurno() {
        this.estado = ESTADO_COMPLETADO;
    }

    public void cancelarTurno() {
        this.estado = ESTADO_CANCELADO;
    }

    public boolean esPendiente() {
        return ESTADO_PENDIENTE.equals(this.estado);
    }

    public boolean estaCompletado() {
        return ESTADO_COMPLETADO.equals(this.estado);
    }
}
