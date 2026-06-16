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

    /**
     * Cian — Baño / Estética. (#06B6D4)
     */
    public static final Color CAT_BANIO = new Color(6, 182, 212);

    // ===== ADICIONES DEL SISTEMA DE DISEÑO HAPPY PAWS =====
    public static final Color DIVIDER = new Color(203, 213, 225);
    public static final Color INACTIVE_TEXT = new Color(188, 204, 220);

    // Colores para avatares
    public static final Color AVATAR_DOG = new Color(254, 186, 100);
    public static final Color AVATAR_CAT = new Color(125, 211, 252);

    // Colores claros/pastel y variantes para Badges y Estados
    public static final Color RED_LIGHT = new Color(254, 226, 226);
    public static final Color BLUE_LIGHT = new Color(219, 234, 254);
    public static final Color BLUE_DARK = new Color(37, 99, 235);
    public static final Color PURPLE_LIGHT = new Color(243, 232, 255);
    public static final Color PURPLE_DARK = new Color(147, 51, 234);
    public static final Color YELLOW_LIGHT = new Color(254, 243, 199);
    public static final Color YELLOW_DARK = new Color(180, 83, 9);
    public static final Color GREEN_LIGHT = new Color(220, 252, 231);
    public static final Color GREEN_DARK = new Color(4, 120, 87);
    public static final Color ORANGE_LIGHT = new Color(255, 221, 179);
    public static final Color ORANGE_DARK = new Color(229, 132, 0);
    public static final Color SUCCESS_LIGHT = new Color(220, 252, 231);

    public static final Color CYAN_LIGHT = new Color(207, 250, 254);
    public static final Color CYAN_DARK = new Color(14, 116, 144);

    public static final Color PINK_LIGHT = new Color(252, 231, 243);
    public static final Color PINK_DARK = new Color(219, 39, 119);

    public static final Color ORANGE_CAT_LIGHT = new Color(255, 237, 213);
    public static final Color ORANGE_CAT_DARK = new Color(234, 88, 12);

    public static final Color YELLOW_CAT_LIGHT = new Color(254, 249, 195);
    public static final Color YELLOW_CAT_DARK = new Color(161, 98, 7);

    // Colores específicos de las tarjetas de estadísticas
    public static final Color STAT_HOY_BG = new Color(240, 249, 255);
    public static final Color STAT_HOY_TXT = new Color(14, 116, 144);
    public static final Color STAT_PEND_BG = new Color(254, 243, 199);
    public static final Color STAT_PEND_TXT = new Color(180, 83, 9);
    public static final Color STAT_REAL_BG = new Color(236, 253, 245);
    public static final Color STAT_REAL_TXT = new Color(4, 120, 87);

    // ===== COLORES FUNCIONALES ADICIONALES =====
    /**
     * Slate-600 — Texto secundario de botones y acciones internas. (#475569)
     */
    public static final Color SLATE_600 = new Color(71, 85, 105);

    /**
     * Azul slot seleccionado — Fondo de time slots activos en el selector de
     * hora. (#0284C7)
     */
    public static final Color SLOT_BLUE = new Color(2, 132, 199);

    /**
     * Púrpura hover — Variante oscura para hover de botones púrpura. (#7E22CE)
     */
    public static final Color PURPLE_HOVER = new Color(126, 34, 206);
}
