package recursos;

/**
 * Paleta de colores centralizada del sistema de diseño de Happy Paws. Todos los
 * colores provienen del archivo DESIGN.md.
 */
public final class Color extends java.awt.Color {

    public Color(int r, int g, int b) {
        super(r, g, b);
    }

    // ===== IDENTIDAD PRINCIPAL (TEAL) =====
    /**
     * Verde teal vibrante — Botones principales, barra de filtros activos y
     * acentos de marca. (#0D9488)
     */
    public static final Color PRIMARY = new Color(13, 148, 136);

    /**
     * Teal profundo — Hover de botones y badge de sesión activa. (#0B6C63)
     */
    public static final Color PRIMARY_DEEP = new Color(11, 108, 99);

    /**
     * Teal claro / pastel — Fondos de badges de estado (ej: "Activo").
     * (#E2F0EE)
     */
    public static final Color PRIMARY_LIGHT = new Color(226, 240, 238);

    // ===== ACENTOS (ACCENT) =====
    /**
     * Azul citas — Color funcional de la sección de turnos y estado pendiente.
     * (#3B82F6)
     */
    public static final Color ACCENT_BLUE = new Color(59, 130, 246);

    /**
     * Azul claro — Fondo de tarjetas de turno. (#E8F0FE)
     */
    public static final Color ACCENT_LIGHT = new Color(232, 240, 254);

    // ===== ESTADOS Y ALERTAS =====
    /**
     * Verde éxito — Turnos completados, badges positivos y texto de
     * confirmación. (#16A34A)
     */
    public static final Color SUCCESS = new Color(22, 163, 74);

    /**
     * Ámbar pendiente — Turnos en estado pendiente, alertas suaves de
     * vencimiento. (#D97706)
     */
    public static final Color PENDING = new Color(217, 119, 6);

    /**
     * Rojo error — Turnos cancelados y acciones destructivas. (#DC2626)
     */
    public static final Color ERROR = new Color(220, 38, 38);

    /**
     * Rojo alerta — Círculos de notificación/atención urgente. (#EF4444)
     */
    public static final Color BADGE_ALERT = new Color(239, 68, 68);

    // ===== SISTEMA ANTI-FATIGA (CANVAS & SUPERFICIES) =====
    /**
     * Slate claro grisáceo — Fondo general de la aplicación (Canvas). (#F1F5F9)
     */
    public static final Color BG = new Color(241, 245, 249);

    /**
     * Canvas General — Fondo de la ventana principal. (#F8FAFC)
     */
    public static final Color CANVAS_GENERAL = new Color(248, 250, 252);

    /**
     * Blanco puro — Tarjetas contenedoras, modales y formularios. (#FFFFFF)
     */
    public static final Color SURFACE = new Color(255, 255, 255);

    /**
     * Borde sutil — Líneas divisorias, contornos de tarjeta y campos. (#E2E8F0)
     */
    public static final Color BORDER = new Color(226, 232, 240);

    // ===== TIPOGRAFÍA Y CONTRASTE =====
    /**
     * Slate oscuro — Títulos principales, nombres y texto de alto contraste.
     * (#1E293B)
     */
    public static final Color INK = new Color(30, 41, 59);

    /**
     * Gris medio — Subtítulos, descripciones secundarias y placeholders.
     * (#64748B)
     */
    public static final Color MUTED = new Color(100, 116, 139);

    // ===== PALETA DE CATEGORÍAS CLÍNICAS =====
    /**
     * Naranja — Cirugía. (#FF6B00)
     */
    public static final Color CAT_CIRUGIA = new Color(255, 107, 0);

    /**
     * Azul — Consulta General. (#3B82F6)
     */
    public static final Color CAT_CONSULTA = new Color(59, 130, 246);

    /**
     * Morado — Análisis Clínicos. (#A855F7)
     */
    public static final Color CAT_ANALISIS = new Color(168, 85, 247);

    /**
     * Rosa/Fucsia — Vacunación. (#EC4899)
     */
    public static final Color CAT_VACUNA = new Color(236, 72, 153);

    /**
     * Amarillo/Dorado — Seguimiento Clínico. (#EAB308)
     */
    public static final Color CAT_SEGUIMIENTO = new Color(234, 179, 8);

    /**
     * Naranja Alerta — Controles o advertencias. (#F97316)
     */
    public static final Color CAT_CONTROL = new Color(249, 115, 22);

    /**
     * Gris — Pacientes o estados inactivos. (#94A3B8)
     */
    public static final Color CAT_INACTIVO = new Color(148, 163, 184);
}
