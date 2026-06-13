package vista;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import modelo.*;
import recursos.CargadorFuentes;

public class PortalVeterinario extends JFrame {

    private final ControladorVeterinaria controlador;
    private final Veterinaria miVeterinaria;
    private final Veterinario veterinarioLogueado;

    // Componentes para la navegación por capas
    private final JPanel panelContenedorSecciones;
    private final CardLayout navegadorCapas;

    // Componentes de la sección Citas
    private final vista.paneles.PanelCitas panelCitas;

    // Variables globales de fuentes
    private Font fuenteTitulo;
    private Font fuenteSubtitulos;
    private Font fuenteNormal;

    public PortalVeterinario() {
        this.controlador = ControladorVeterinaria.getInstancia();
        this.miVeterinaria = controlador.getVeterinaria();
        this.veterinarioLogueado = controlador.getVeterinarioLogueado();

        fuenteTitulo = CargadorFuentes.cargar(22f);
        fuenteSubtitulos = CargadorFuentes.cargar(14f);
        fuenteNormal = CargadorFuentes.cargar(12f);

        setTitle("Happy Paws");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        try {
            // Buscamos la imagen en la carpeta de recursos/imágenes
            ImageIcon iconoApp = new ImageIcon("imagenes/logo.png");
            setIconImage(iconoApp.getImage());
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono de la aplicación: " + e.getMessage());
        }
        // ===============================================
        getContentPane().setBackground(new Color(241, 245, 249));

        // --- PANEL SUPERIOR: Encabezado con bienvenida y contadores ---
        JPanel panelSuperiorAgrupado = new JPanel(new BorderLayout(0, 10));
        panelSuperiorAgrupado.setOpaque(false);
        panelSuperiorAgrupado.setBorder(new EmptyBorder(15, 25, 5, 25));

        // Línea 1: Saludo y Fecha
        JPanel panelHeaderLinea1 = new JPanel(new BorderLayout());
        panelHeaderLinea1.setOpaque(false);

        int turnosHoy = controlador.contarTurnosDelVeterinario(veterinarioLogueado, null);
        JLabel lblSaludo = new JLabel("<html><font color='#1E293B'><b>¡Hola, Dr. " + veterinarioLogueado.getApellido() + "! 👋</b></font><br><font color='#64748B' size='4'>Tenés <font color='#0D9488'><b>" + turnosHoy + " turnos</b></font> asignados. Que tengas un gran día.</font></html>");
        lblSaludo.setFont(fuenteTitulo);
        lblSaludo.setSize(450, 100);
        panelHeaderLinea1.add(lblSaludo, BorderLayout.WEST);

        JLabel lblFechaActual = new JLabel("<html><div style='text-align: right;'><font color='#94A3B8'>Hoy</font><br><font color='#1E293B'><b>Viernes, 06 Jun 2026</b></font></div></html>");
        lblFechaActual.setFont(fuenteNormal);
        panelHeaderLinea1.add(lblFechaActual, BorderLayout.EAST);
        panelSuperiorAgrupado.add(panelHeaderLinea1, BorderLayout.NORTH);

        add(panelSuperiorAgrupado, BorderLayout.NORTH);

        // --- CUERPO PRINCIPAL CON CARDLAYOUT ---
        navegadorCapas = new CardLayout();
        panelContenedorSecciones = new JPanel(navegadorCapas);
        panelContenedorSecciones.setOpaque(false);

        panelContenedorSecciones.add(crearVistaInicio(), "PANTALLA_INICIO");
        panelCitas = new vista.paneles.PanelCitas(controlador);
        panelContenedorSecciones.add(panelCitas, "PANTALLA_CITAS");

        panelContenedorSecciones.add(new vista.paneles.PanelRegistros(controlador), "PANTALLA_REGISTROS");
        panelContenedorSecciones.add(new vista.paneles.PanelAdopcion(controlador), "PANTALLA_ADOPCION");
        panelContenedorSecciones.add(new vista.paneles.PanelMedicamentos(controlador), "PANTALLA_MEDICAMENTOS");
        panelContenedorSecciones.add(new vista.paneles.PanelNotas(controlador), "PANTALLA_NOTAS");
        panelContenedorSecciones.add(new vista.paneles.PanelMas(controlador), "PANTALLA_MAS");

        add(panelContenedorSecciones, BorderLayout.CENTER);

        // --- BARRA DE NAVEGACIÓN INFERIOR BLANCA Y ESTILIZADA ---
        // --- BARRA DE NAVEGACIÓN INFERIOR BLANCA Y ESTILIZADA ---
        JPanel panelMenuInferior = new JPanel(new GridLayout(1, 6, 5, 0));
        panelMenuInferior.setBackground(Color.WHITE);
        panelMenuInferior.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)),
                new EmptyBorder(8, 10, 8, 10)
        ));

        // Pasamos: Texto, Ruta del Icono y Clave de la Pantalla asociada
        JButton btnInicio = crearBotonMenuNav("Inicio", "imagenes/emojis/casa.png", "🏠", "PANTALLA_INICIO");
        JButton btnRegistros = crearBotonMenuNav("Registros", null, "📋", "PANTALLA_REGISTROS");
        JButton btnAdopcion = crearBotonMenuNav("Adopción", "imagenes/emojis/patitas.png", "🐾", "PANTALLA_ADOPCION");
        JButton btnMedicamentos = crearBotonMenuNav("Medicamentos","", "💊", "PANTALLA_MEDICAMENTOS");
        JButton btnNotas = crearBotonMenuNav("Notas", null, "📝", "PANTALLA_NOTAS");
        JButton btnCitas = crearBotonMenuNav("Turnos", "imagenes/emojis/calendario.png", "📅", "PANTALLA_CITAS");
        JButton btnMas = crearBotonMenuNav("Más", null, "➕", "PANTALLA_MAS");

        // Al iniciar la app, la sección activa es Inicio, por lo tanto lo marcamos
        ((BotonMenuNav) btnInicio).setActivo(true);

        panelMenuInferior.add(btnInicio);
        panelMenuInferior.add(btnRegistros);
        panelMenuInferior.add(btnAdopcion);
        panelMenuInferior.add(btnMedicamentos);
        panelMenuInferior.add(btnNotas);
        panelMenuInferior.add(btnCitas);
        panelMenuInferior.add(btnMas);

        add(panelMenuInferior, BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------------
    // VISTA 1: SECCIÓN DE INICIO (Diseño exacto de la captura con Emojis)
    // ---------------------------------------------------------------------
    // ---------------------------------------------------------------------
    // VISTA 1: SECCIÓN DE INICIO (Diseño exacto de la captura con Emojis)
    // ---------------------------------------------------------------------
    private JPanel crearVistaInicio() {
        JPanel panelDashboard = new JPanel(new GridBagLayout());
        panelDashboard.setOpaque(false);
        panelDashboard.setBorder(new EmptyBorder(10, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 10, 0, 10);

        // Alto de la barra decorativa superior y radio de redondeo consistente
        int altoBarra = 9;
        int radioEsquina = 16;

        // --- COLUMNA 1: Tarjeta Profesional (Borde superior Verde Esmeralda con Degradado) ---
        JPanel cardUsuario = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Degradado Turquesa claro a Esmeralda oscuro
                GradientPaint degradado = new GradientPaint(0, 0, new Color(45, 212, 191), getWidth(), 0, new Color(13, 148, 136));
                g2.setPaint(degradado);

                // Recorte redondeado para que calce con las esquinas del borde de la card
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radioEsquina, radioEsquina));
                g2.fillRect(0, 0, getWidth(), altoBarra);
                g2.dispose();
            }
        };
        cardUsuario.setBackground(Color.WHITE);
        cardUsuario.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true), new EmptyBorder(25, 20, 20, 20)
        ));

        // Contenedor del avatar del doctor
        JPanel panelAvatarContenedor = new JPanel(new BorderLayout(0, 10));
        panelAvatarContenedor.setOpaque(false);

        // Contenedor del avatar del doctor con Degradado y Antialiasing
        JPanel panelFotoPerfil = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();

                // ACTIVAR SUAVIZADO: Esencial para evitar pixeles duros en los bordes curvos
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // CONFIGURAR DEGRADADO: Desde un turquesa brillante (arriba izq) a un esmeralda (abajo der)
                Color colorInicio = new Color(45, 212, 191); // Teal brillante #2DD4BF
                Color colorFin = new Color(13, 148, 136);    // Esmeralda #0D9488
                GradientPaint degradadoDiagonal = new GradientPaint(0, 0, colorInicio, getWidth(), getHeight(), colorFin);
                g2.setPaint(degradadoDiagonal);

                // Dibujar el fondo redondeado contenedor
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
            }
        };
        panelFotoPerfil.setPreferredSize(new Dimension(110, 110));
        panelFotoPerfil.setLayout(new GridBagLayout());

        // --- SOLUCIÓN ULTRA HD PARA EL VETERINARIO ---
        try {
            String rutaImagen = "imagenes/emojis/vet.png";
            // Leemos el archivo directo a un java.io.File para evitar compresión nativa corrupta de ImageIcon
            java.io.File archivoImagen = new java.io.File(rutaImagen);
            java.awt.image.BufferedImage imgBuffer = javax.imageio.ImageIO.read(archivoImagen);

            int targetWidth = 95;  // Un toque de aire para que no toque los bordes del contenedor
            int targetHeight = 95;

            // Forzamos el remuestreo bilinear premium desde la matriz de bytes pura
            ImageIcon iconoEscalado = escalarImagenAltaCalidad(imgBuffer, targetWidth, targetHeight);

            JLabel lblIconoUser = new JLabel(iconoEscalado);
            panelFotoPerfil.add(lblIconoUser);
        } catch (IOException e) {
            System.out.println("Error al renderizar en alta definición: " + e.getMessage());
            JLabel lblIconoUser = new JLabel("👨‍⚕️");
            lblIconoUser.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
            panelFotoPerfil.add(lblIconoUser);
        }

        // Badge pequeño "Activo"
        JLabel lblActivo = new JLabel("Activo", SwingConstants.CENTER);
        lblActivo.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblActivo.setForeground(Color.WHITE);
        lblActivo.setOpaque(true);
        lblActivo.setBackground(new Color(34, 197, 94));
        lblActivo.setBorder(new EmptyBorder(2, 8, 2, 8));

        JPanel panelAgrupadorFoto = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelAgrupadorFoto.setOpaque(false);
        panelAgrupadorFoto.add(panelFotoPerfil);

        panelAvatarContenedor.add(panelAgrupadorFoto, BorderLayout.CENTER);

        JPanel panelBadgeCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, -10));
        panelBadgeCenter.setOpaque(false);
        panelBadgeCenter.add(lblActivo);
        panelAvatarContenedor.add(panelBadgeCenter, BorderLayout.SOUTH);

        // Bloque de datos del Profesional
        JPanel panelDatosUser = new JPanel();
        panelDatosUser.setOpaque(false);
        panelDatosUser.setLayout(new BoxLayout(panelDatosUser, BoxLayout.Y_AXIS));

        JLabel lblNombreUser = new JLabel(veterinarioLogueado.getNombre() + " " + veterinarioLogueado.getApellido(), SwingConstants.CENTER);
        lblNombreUser.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombreUser.setForeground(new Color(30, 41, 59));
        lblNombreUser.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRolUser = new JLabel("Veterinario Activo", SwingConstants.CENTER);
        lblRolUser.setFont(fuenteNormal);
        lblRolUser.setForeground(new Color(13, 148, 136));
        lblRolUser.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tarjeta gris inferior interna para la Matrícula
        JPanel cardMatricula = new JPanel(new GridLayout(2, 1, 0, 2));
        cardMatricula.setBackground(new Color(248, 250, 252));
        cardMatricula.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(241, 245, 249), 1, true), new EmptyBorder(10, 15, 10, 15)
        ));
        JLabel lblMatTxt = new JLabel("Matrícula", SwingConstants.CENTER);
        lblMatTxt.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMatTxt.setForeground(new Color(148, 163, 184));
        JLabel lblMatNum = new JLabel(veterinarioLogueado.getMatricula(), SwingConstants.CENTER);
        lblMatNum.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblMatNum.setForeground(new Color(30, 41, 59));
        cardMatricula.add(lblMatTxt);
        cardMatricula.add(lblMatNum);

        // Detalles inferiores corregidos: Especialidad Y el Turno faltante
        JPanel panelFilaDetalles = new JPanel(new GridLayout(1, 2, 10, 0));
        panelFilaDetalles.setOpaque(false);
        panelFilaDetalles.add(crearMiniBadgeInformación("Especialidad", "General"));

        panelDatosUser.add(lblNombreUser);
        panelDatosUser.add(Box.createVerticalStrut(4));
        panelDatosUser.add(lblRolUser);
        panelDatosUser.add(Box.createVerticalStrut(15));
        panelDatosUser.add(cardMatricula);
        panelDatosUser.add(Box.createVerticalStrut(12));
        panelDatosUser.add(panelFilaDetalles);

        cardUsuario.add(panelAvatarContenedor, BorderLayout.NORTH);
        cardUsuario.add(panelDatosUser, BorderLayout.CENTER);
        cardUsuario.setPreferredSize(new Dimension(280, 400));

        gbc.gridx = 0;
        gbc.weightx = 0;
        panelDashboard.add(cardUsuario, gbc);

        // --- COLUMNA 2: Mis Próximos Turnos (Borde superior Violeta con Degradado) ---
        JPanel cardTurnos = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint degradado = new GradientPaint(0, 0, new Color(168, 85, 247), getWidth(), 0, new Color(109, 40, 217));
                g2.setPaint(degradado);

                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radioEsquina, radioEsquina));
                g2.fillRect(0, 0, getWidth(), altoBarra);
                g2.dispose();
            }
        };
        cardTurnos.setBackground(Color.WHITE);
        cardTurnos.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true), new EmptyBorder(20, 15, 15, 15)
        ));

        JPanel headerTurnosInterno = new JPanel(new BorderLayout());
        headerTurnosInterno.setOpaque(false);
        JLabel lblTituloTurnos = new JLabel("Mis Próximos Turnos");
        lblTituloTurnos.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTituloTurnos.setForeground(new Color(30, 41, 59));

        JLabel lblTagHoy = new BadgeRedondeado("Hoy", new Color(243, 232, 255), new Color(124, 58, 237));
        lblTagHoy.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTagHoy.setForeground(new Color(124, 58, 237));
        lblTagHoy.setBorder(new EmptyBorder(6, 14, 6, 16));

        headerTurnosInterno.add(lblTituloTurnos, BorderLayout.WEST);
        headerTurnosInterno.add(lblTagHoy, BorderLayout.EAST);
        cardTurnos.add(headerTurnosInterno, BorderLayout.NORTH);

        JPanel panelListaTurnos = new JPanel();
        panelListaTurnos.setLayout(new BoxLayout(panelListaTurnos, BoxLayout.Y_AXIS));
        panelListaTurnos.setBackground(Color.WHITE);

        ArrayList<Turno> turnosGlobales = miVeterinaria.getListaTurnos();
        for (Turno t : turnosGlobales) {
            if (t.getVeterinario().equals(veterinarioLogueado)) {
                panelListaTurnos.add(crearTarjetaTurnoVisual(t));
                panelListaTurnos.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollTurnos = new JScrollPane(panelListaTurnos);
        scrollTurnos.setBorder(null);
        scrollTurnos.setOpaque(false);
        scrollTurnos.getViewport().setOpaque(false);

        // --- CONFIGURACIÓN MODERNA Y FLUIDA DEL SCROLL ---
        JScrollBar barraVertical = scrollTurnos.getVerticalScrollBar();
        barraVertical.setUI(new ModernScrollBarUI()); // Aplicamos tu UI personalizada
        barraVertical.setPreferredSize(new Dimension(8, 0)); // Barra delgada de 8px
        barraVertical.setOpaque(false); // Forzamos la transparencia de fondo de forma segura
        barraVertical.setUnitIncrement(12); // Scroll rápido y fluido con la ruedita
        // ------

        cardTurnos.add(scrollTurnos, BorderLayout.CENTER);
        cardTurnos.setPreferredSize(new Dimension(300, 400));

        gbc.gridx = 1;
        gbc.weightx = 1;
        panelDashboard.add(cardTurnos, gbc);

        // --- COLUMNA 3: Acciones Rápidas (Borde superior Naranja a Fucsia con Degradado Ancho) ---
        JPanel cardAcciones = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint degradadoHorizontal = new GradientPaint(0, 0, new Color(249, 115, 22), getWidth(), 0, new Color(236, 72, 153));
                g2.setPaint(degradadoHorizontal);

                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radioEsquina, radioEsquina));
                g2.fillRect(0, 0, getWidth(), altoBarra);
                g2.dispose();
            }
        };
        cardAcciones.setBackground(Color.WHITE);
        cardAcciones.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true), new EmptyBorder(20, 15, 15, 15)
        ));

        JLabel lblTituloAcciones = new JLabel("Acciones Rápidas");
        lblTituloAcciones.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTituloAcciones.setForeground(new Color(30, 41, 59));
        cardAcciones.add(lblTituloAcciones, BorderLayout.NORTH);

        JPanel panelBotonesAccion = new JPanel();
        panelBotonesAccion.setLayout(new BoxLayout(panelBotonesAccion, BoxLayout.Y_AXIS));
        panelBotonesAccion.setOpaque(false);

        panelBotonesAccion.add(crearFilaAccionEstilizada("Nueva Consulta", new Color(115, 236, 255), "imagenes/emojis/estetoscopio.png"));
        panelBotonesAccion.add(Box.createVerticalStrut(12));
        panelBotonesAccion.add(crearFilaAccionEstilizada("Registrar Paciente", new Color(99, 102, 241), "imagenes/emojis/perro_cara.png"));
        panelBotonesAccion.add(Box.createVerticalStrut(12));

        cardAcciones.add(panelBotonesAccion, BorderLayout.CENTER);

        // --- CONTADORES INFERIORES EN ACCIONES RÁPIDAS ---
        JPanel panelStatusDia = new JPanel(new GridLayout(1, 2, 10, 0));
        panelStatusDia.setOpaque(false);

        int totalAtendidos = controlador.contarTurnosDelVeterinario(veterinarioLogueado, Turno.ESTADO_COMPLETADO);
        int totalPendientes = controlador.contarTurnosDelVeterinario(veterinarioLogueado, Turno.ESTADO_PENDIENTE);

        // 1. Atendidos con su símbolo de éxito
        panelStatusDia.add(crearMiniContadorInferior(
                String.valueOf(totalAtendidos),
                "Atendidos",
                new Color(240, 253, 250),
                new Color(13, 148, 136),
                "imagenes/emojis/exito.png" // <-- Ruta del ícono de éxito
        ));

        // 2. Pendientes con su reloj de arena
        panelStatusDia.add(crearMiniContadorInferior(
                String.valueOf(totalPendientes),
                "Pendientes",
                new Color(254, 243, 199),
                new Color(217, 119, 6),
                "imagenes/emojis/reloj_arena.png" // <-- Ruta del reloj de arena
        ));

        cardAcciones.add(panelStatusDia, BorderLayout.SOUTH);

        gbc.gridx = 2;
        gbc.weightx = 0;
        panelDashboard.add(cardAcciones, gbc);

        return panelDashboard;
    }

    private JPanel crearTarjetaTurnoVisual(Turno t) {
        JPanel itemTurno = new JPanel(new BorderLayout(15, 0));
        itemTurno.setBackground(new Color(248, 250, 252));
        itemTurno.setMaximumSize(new Dimension(385, 62));
        itemTurno.setPreferredSize(new Dimension(385, 62));
        itemTurno.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(241, 245, 249), 1, true), new EmptyBorder(6, 12, 6, 12)
        ));

        // --- CAJA DE LA HORA OPTIMIZADA (COMPACTA) ---
        JLabel lblHora = new JLabel(t.getHora(), SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sombra sutil
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(1, 2, getWidth() - 2, getHeight() - 3, 8, 8);

                // Fondo blanco limpio
                g2.setColor(new Color(255, 255, 255));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                // Borde gris claro delgado
                g2.setColor(new Color(226, 232, 240));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblHora.setFont(fuenteNormal);
        lblHora.setForeground(new Color(30, 41, 59));
        lblHora.setPreferredSize(new Dimension(65, 28));

        // Contenedor para centrar la tarjeta de la hora verticalmente
        JPanel panelHoraWrapper = new JPanel(new GridBagLayout());
        panelHoraWrapper.setOpaque(false);
        panelHoraWrapper.add(lblHora);
        itemTurno.add(panelHoraWrapper, BorderLayout.WEST);

        // --- BLOQUE CENTRAL: ÍCONO Y TEXTO ALINEADOS A LA IZQUIERDA ---
        // FlowLayout.LEFT garantiza que los componentes se posicionen de izquierda a derecha inmediatamente después de la hora
        JPanel panelContenidoCentral = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelContenidoCentral.setOpaque(false);

        // Cargar imagen del animal (Perro o Gato)
        String rutaImagen = (t.getAnimal() instanceof Perro) ? "imagenes/emojis/perro.png" : "imagenes/emojis/gato.png";
        JLabel lblEmoji = new JLabel();
        try {
            ImageIcon iconoAnimal = new ImageIcon(rutaImagen);
            ImageIcon iconoEscalado = escalarImagenAltaCalidad(iconoAnimal.getImage(), 22, 22);
            lblEmoji.setIcon(iconoEscalado);
        } catch (Exception e) {
            String emojiMascota = (t.getAnimal() instanceof Perro) ? "🐕" : "🐈";
            lblEmoji.setText(emojiMascota);
            lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        }
        panelContenidoCentral.add(lblEmoji);

        // Panel para los textos (Nombre arriba, tipo de turno abajo)
        JPanel panelLabelsInternos = new JPanel(new GridLayout(2, 1, 0, 0));
        panelLabelsInternos.setOpaque(false);

        JLabel lblPaciente = new JLabel(t.getAnimal().getNombre());
        lblPaciente.setFont(fuenteSubtitulos);
        lblPaciente.setForeground(new Color(15, 23, 42));

        JLabel lblSubDescripcion = new JLabel(t.getTipo().getDescripcion());
        lblSubDescripcion.setFont(fuenteNormal);
        lblSubDescripcion.setForeground(new Color(148, 163, 184));

        panelLabelsInternos.add(lblPaciente);
        panelLabelsInternos.add(lblSubDescripcion);
        panelContenidoCentral.add(panelLabelsInternos);

        // Envolvemos en un GridBagLayout externo únicamente para que el bloque mantenga el centrado vertical con respecto a la tarjeta,
        // pero obligando a que su contenido interno se empuje hacia el extremo izquierdo (WEST)
        JPanel panelAlineacionWrapper = new JPanel(new GridBagLayout());
        panelAlineacionWrapper.setOpaque(false);
        GridBagConstraints gbcCentro = new GridBagConstraints();
        gbcCentro.anchor = GridBagConstraints.WEST; // <-- Fuerza la alineación hacia la izquierda
        gbcCentro.weightx = 1.0;                    // <-- Toma el espacio restante para empujar el badge a la derecha
        panelAlineacionWrapper.add(panelContenidoCentral, gbcCentro);

        itemTurno.add(panelAlineacionWrapper, BorderLayout.CENTER);

        // --- TAG DINÁMICO A LA DERECHA (PÍLDORA) ---
        PildoraBadge lblBadgePildora = new PildoraBadge(t.getTipo().getDescripcion());
        lblBadgePildora.setFont(fuenteNormal);
        lblBadgePildora.setBorder(new EmptyBorder(4, 12, 4, 12));

        switch (t.getTipo()) {
            case CIRUGIA -> {
                lblBadgePildora.setCustomBackground(new Color(254, 226, 226));
                lblBadgePildora.setForeground(new Color(220, 38, 38));
            }
            case CONSULTA_GENERAL -> {
                lblBadgePildora.setCustomBackground(new Color(219, 234, 254));
                lblBadgePildora.setForeground(new Color(37, 99, 235));
            }
            case ANALISIS -> {
                lblBadgePildora.setCustomBackground(new Color(243, 232, 255));
                lblBadgePildora.setForeground(new Color(147, 51, 234));
            }
            default -> {
                lblBadgePildora.setCustomBackground(new Color(220, 252, 231));
                lblBadgePildora.setForeground(new Color(22, 163, 74));
                lblBadgePildora.setText("Vacunación");
            }
        }

        JPanel panelBadgeWrapper = new JPanel(new GridBagLayout());
        panelBadgeWrapper.setOpaque(false);
        panelBadgeWrapper.add(lblBadgePildora);
        itemTurno.add(panelBadgeWrapper, BorderLayout.EAST);

        return itemTurno;
    }

    private JPanel crearFilaAccionEstilizada(String titulo, Color colorFondoIcono, String emojiIcono) {
        // Reducimos la altura a 52 para que se adapte de forma estilizada a una sola línea
        JPanel panelFila = new JPanel(new BorderLayout(15, 0));
        panelFila.setBackground(new Color(248, 250, 252));
        panelFila.setMaximumSize(new Dimension(320, 52));
        panelFila.setPreferredSize(new Dimension(320, 52));
        panelFila.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(241, 245, 249), 1, true), new EmptyBorder(6, 12, 6, 12)
        ));

        // --- ICONO DE LA ACCIÓN ---
        JPanel panelCuadroIcono = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(colorFondoIcono);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }
        };
        panelCuadroIcono.setPreferredSize(new Dimension(40, 40));
        panelCuadroIcono.setLayout(new GridBagLayout());

        JLabel lblEmoji = new JLabel();
        if (emojiIcono.endsWith(".png")) {
            try {
                ImageIcon icono = new ImageIcon(emojiIcono);
                // Usando tu método de alta calidad o el escalado clásico suave
                Image imagenEscalada = icono.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                lblEmoji.setIcon(new ImageIcon(imagenEscalada));
            } catch (Exception e) {
                lblEmoji.setText("?");
                lblEmoji.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            }
        } else {
            lblEmoji.setText(emojiIcono);
            lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        }
        panelCuadroIcono.add(lblEmoji);
        panelFila.add(panelCuadroIcono, BorderLayout.WEST);

        // --- TEXTO DE LA ACCIÓN (SÓLO TÍTULO Y CENTRADO VERTICAL) ---
        // Usamos GridBagLayout en el contenedor del texto para lograr un centrado vertical absoluto y limpio
        JPanel panelContenedorTexto = new JPanel(new GridBagLayout());
        panelContenedorTexto.setOpaque(false);

        JLabel lblT = new JLabel(titulo);
        lblT.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Subido a 14 un toque para destacar más al estar solo
        lblT.setForeground(new Color(15, 23, 42));

        GridBagConstraints gbcTexto = new GridBagConstraints();
        gbcTexto.anchor = GridBagConstraints.WEST; // Alineado firmemente a la izquierda (pegado al ícono)
        gbcTexto.weightx = 1.0;                    // Empuja lo que esté a la derecha
        panelContenedorTexto.add(lblT, gbcTexto);

        panelFila.add(panelContenedorTexto, BorderLayout.CENTER);

        // La envolvemos en un GridBagLayout para que también mantenga el centro vertical perfecto
        JPanel panelFlechaWrapper = new JPanel(new GridBagLayout());
        panelFlechaWrapper.setOpaque(false);
        panelFila.add(panelFlechaWrapper, BorderLayout.EAST);

        return panelFila;
    }

    private JPanel crearMiniBadgeInformación(String titulo, String valor) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 1));
        panel.setBackground(new Color(240, 253, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 251, 241), 1, true), new EmptyBorder(6, 10, 6, 10)
        ));
        JLabel lblT = new JLabel(titulo, SwingConstants.CENTER);
        lblT.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblT.setForeground(new Color(148, 163, 184));
        JLabel lblV = new JLabel(valor, SwingConstants.CENTER);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblV.setForeground(new Color(15, 23, 42));
        panel.add(lblT);
        panel.add(lblV);
        return panel;
    }

    private JPanel crearMiniContadorInferior(String valor, String etiqueta, Color fondo, Color colorTexto, String rutaIcono) {
        // Modificamos el dibujo para remover por completo las esquinas redondeadas
        JPanel panel = new JPanel(new BorderLayout(12, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // 1. Fondo completamente recto (sin border-radius)
                g2.setColor(fondo);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // 2. Contorno sutil y fino de 1px recto estilo "Especialidad"
                Color colorBordeSuave = new Color(
                        Math.max(0, fondo.getRed() - 25),
                        Math.max(0, fondo.getGreen() - 20),
                        Math.max(0, fondo.getBlue() - 15)
                );
                g2.setColor(colorBordeSuave);
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

                g2.dispose();
            }
        };
        panel.setOpaque(false);

        // Mantenemos el padding interno y las dimensiones de 56px de alto para que no se achate
        panel.setBorder(new EmptyBorder(8, 14, 8, 14));
        panel.setPreferredSize(new Dimension(135, 56));

        // --- ÍCONO EN ALTA RESOLUCIÓN ---
        JLabel lblIcono = new JLabel();
        try {
            java.io.File archivoImagen = new java.io.File(rutaIcono);
            java.awt.image.BufferedImage imgBuffer = javax.imageio.ImageIO.read(archivoImagen);
            ImageIcon iconoEscalado = escalarImagenAltaCalidad(imgBuffer, 22, 22);
            lblIcono.setIcon(iconoEscalado);
        } catch (IOException e) {
            lblIcono.setText("•");
            lblIcono.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblIcono.setForeground(colorTexto);
        }

        JPanel panelIconoWrapper = new JPanel(new GridBagLayout());
        panelIconoWrapper.setOpaque(false);
        panelIconoWrapper.add(lblIcono);
        panel.add(panelIconoWrapper, BorderLayout.WEST);

        // --- PANEL DE TEXTOS ---
        JPanel panelTxt = new JPanel(new GridLayout(2, 1, 0, 1));
        panelTxt.setOpaque(false);

        JLabel lblVal = new JLabel(valor);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblVal.setForeground(colorTexto);

        JLabel lblE = new JLabel(etiqueta);
        lblE.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblE.setForeground(new Color(100, 116, 139));

        panelTxt.add(lblVal);
        panelTxt.add(lblE);

        panel.add(panelTxt, BorderLayout.CENTER);

        return panel;
    }

    private JButton crearBotonMenuNav(String titulo, String icono, String unicodeIcon, String claveCapa) {
        BotonMenuNav btn = new BotonMenuNav(titulo, icono, unicodeIcon, claveCapa);

        // Vinculamos la acción de cambiar de sección
        btn.addActionListener(e -> {
            // 1. Cambiamos la sección en el CardLayout
            switch (claveCapa) {
                case "PANTALLA_REGISTROS" ->
                    ((vista.paneles.PanelRegistros) panelContenedorSecciones.getComponent(2)).actualizar();
                case "PANTALLA_ADOPCION" ->
                    ((vista.paneles.PanelAdopcion) panelContenedorSecciones.getComponent(3)).actualizar();
                case "PANTALLA_MEDICAMENTOS" ->
                        ((vista.paneles.PanelMedicamentos) panelContenedorSecciones.getComponent(4)).actualizar();
                case "PANTALLA_NOTAS" ->
                    ((vista.paneles.PanelNotas) panelContenedorSecciones.getComponent(5)).actualizar();
                case "PANTALLA_MAS" ->
                    ((vista.paneles.PanelMas) panelContenedorSecciones.getComponent(6)).actualizar();
                case "PANTALLA_CITAS" ->
                    panelCitas.actualizar();
                default -> {
                }
            }
            navegadorCapas.show(panelContenedorSecciones, claveCapa);

            // 2. RECORRER TODOS LOS BOTONES para avisarles cuál es el activo ahora
            JPanel panelMenu = (JPanel) btn.getParent();
            if (panelMenu != null) {
                for (Component comp : panelMenu.getComponents()) {
                    if (comp instanceof BotonMenuNav b) {
                        b.setActivo(b == btn); // True solo para el botón cliqueado
                    }
                }
            }
        });

        return btn;
    }

    private static class BadgeRedondeado extends JLabel {

        private final Color bgColor;
        private final Color borderColor;

        public BadgeRedondeado(String text, Color background, Color border) {
            super(text, SwingConstants.CENTER);
            this.bgColor = background;
            this.borderColor = border;
            setOpaque(false);
            setVerticalAlignment(SwingConstants.CENTER);
            setVerticalTextPosition(SwingConstants.CENTER);
            setHorizontalTextPosition(SwingConstants.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(0.8f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);

            super.paintComponent(g);
        }
    }

    private static class PildoraBadge extends JLabel {

        private Color bgColor = Color.WHITE;

        public PildoraBadge(String text) {
            super(text, SwingConstants.CENTER);
            setOpaque(false);
        }

        public void setCustomBackground(Color bg) {
            bgColor = bg;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

            Color borderColor = new Color(
                    Math.max(0, bgColor.getRed() - 40),
                    Math.max(0, bgColor.getGreen() - 40),
                    Math.max(0, bgColor.getBlue() - 40)
            );
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(0.8f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);

            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Font fuenteBaseUI = CargadorFuentes.obtenerFuenteBase().deriveFont(12f);

            UIManager.put("Label.font", fuenteBaseUI);
            UIManager.put("Button.font", fuenteBaseUI);
            UIManager.put("ComboBox.font", fuenteBaseUI);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            System.out.println("No se pudo cargar la fuente del sistema UI.");
        }

        SwingUtilities.invokeLater(() -> new PortalVeterinario().setVisible(true));
    }

    // Método auxiliar para escalar PNGs sin perder calidad (evita el pixelado)
    private ImageIcon escalarImagenAltaCalidad(Image srcImg, int w, int h) {
        java.awt.image.BufferedImage resizedImg = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        // Configuración de renderizado premium
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar la imagen en el lienzo limpio de alta calidad
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return new ImageIcon(resizedImg);
    }

    // =========================================================================
    // CLASE INTERNA: Debe ir al final del archivo, FUERA de cualquier método
    // =========================================================================
    // =========================================================================
    // CLASE INTERNA: Modificada con soporte para efecto Hover (Rollover)
    // =========================================================================
    private static class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {

        // Diseña el "track" (el fondo por donde se desliza la barra)
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            // Lo dejamos vacío para eliminar el fondo gris antiguo de Windows 2000
        }

        // Diseña el "thumb" (la barrita redondeada que arrastramos)
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // --- LÓGICA DE HOVER / DRAG DINÁMICO ---
            Color colorFinal;

            if (isDragging) {
                // Si el usuario la está arrastrando: Gris Slate Oscuro (#64748B)
                colorFinal = new Color(100, 116, 139);
            } else if (isThumbRollover()) {
                // Si solo tiene el mouse encima (Hover): Gris Slate Intermedio (#94A3B8)
                colorFinal = new Color(148, 163, 184);
            } else {
                // Estado base pasivo: Tu gris suave original (#CBD5E1)
                colorFinal = new Color(203, 213, 225);
            }

            g2.setColor(colorFinal);
            // ----------------------------------------

            // Dibujamos la barra con un margen de 2px a los costados y esquinas redondeadas
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                    thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
            g2.dispose();
        }

        // Removemos la flecha superior clásica de Windows
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return crearBotonInvisible();
        }

        // Removemos la flecha inferior clásica de Windows
        @Override
        protected JButton createIncreaseButton(int orientation) {
            return crearBotonInvisible();
        }

        // Método auxiliar para generar un botón sin dimensiones (oculto)
        private JButton crearBotonInvisible() {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            btn.setMinimumSize(new Dimension(0, 0));
            btn.setMaximumSize(new Dimension(0, 0));
            return btn;
        }
    }

    // =========================================================================
    // CLASE INTERNA: Botón de Navegación Profesional (Área de Hover Corregida)
    // =========================================================================
    private class BotonMenuNav extends JButton {

        private boolean mouseEncima = false;
        private boolean activo = false;

        // Colores de la captura de pantalla
        private final Color colorFondoVerdeSuave = new Color(240, 253, 250); // Menta clarito #F0FDFA
        private final Color colorTextoVerdeOscuro = new Color(13, 148, 136); // Teal / Esmeralda #0D9488
        private final Color colorTextoGrisBase = new Color(100, 116, 139);   // Gris Slate #64748B

        public BotonMenuNav(String titulo, String icono, String unicodeIcon, String claveCapa) {
            // Configuración base estética de Swing
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // REDISEÑO DEL TAMAÑO: Reducimos el ancho preferido para que la celda invisible no sea gigante
            // Le damos un padding interno generoso a los lados (24px) para que el fondo verde abrace al texto cómodamente
            setPreferredSize(new Dimension(85, 70));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(8, 20, 8, 20)); // <-- Mantiene el área de clic e ícono perfectamente contenida

            // 1. Inicializar y centrar el ícono (PNG o Emoji)
            JLabel lblIcon = new JLabel();
            lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (icono != null && new java.io.File(icono).exists()) {
                try {
                    // ImageIcon icon = new ImageIcon(icono);
                    // Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    // lblIcon.setIcon(new ImageIcon(scaled));
                    ImageIcon icon = new ImageIcon(icono);
                    ImageIcon iconoEscalado = escalarImagenAltaCalidad(icon.getImage(), 32, 32);
                    lblIcon.setIcon(iconoEscalado);
                } catch (Exception e) {
                    lblIcon.setText(unicodeIcon);
                    lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
                }
            } else {
                lblIcon.setText(unicodeIcon);
                lblIcon.setBorder(new EmptyBorder(2,1,1,1));
                lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            }

            // 2. Inicializar y centrar el texto inferior
            JLabel lblTitulo = new JLabel(titulo);
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Añadimos los componentes al botón con un separador vertical
            add(Box.createVerticalGlue());
            add(lblIcon);
            add(Box.createVerticalStrut(5));
            add(lblTitulo);
            add(Box.createVerticalGlue());

            // 3. Escuchador de Eventos para detectar el paso del mouse preciso
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    mouseEncima = true;
                    repaint(); // Fuerza a Swing a volver a pintar con el color de Hover
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    mouseEncima = false;
                    repaint(); // Vuelve a pintar el estado pasivo
                }
            });
        }

        // Setter para actualizar el estado del botón desde afuera al hacer clic
        public void setActivo(boolean estado) {
            this.activo = estado;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Obtenemos los JLabels internos para cambiarles el color de texto dinámicamente
            JLabel lblIcon = (JLabel) getComponent(1);
            JLabel lblTitulo = (JLabel) getComponent(3);

            // --- LÓGICA DE RENDERIZADO DINÁMICO (HOVER Y ACTIVE MATCH REAL) ---
            if (activo || mouseEncima) {
                g2.setColor(colorFondoVerdeSuave);

                // CAMBIO CLAVE: Ahora la píldora toma el ancho exacto del botón visible en vez de un número fijo
                // Dejamos un margen sutil de 4px a los lados para que se vea redondeado y armónico
                int x = 6;
                int y = 5;
                int anchoPildora = getWidth() - 12;
                int altoPildora = getHeight() - 10;

                g2.fillRoundRect(x, y, anchoPildora, altoPildora, 18, 18);

                // Cambiamos las tipografías al verde oscuro corporativo
                lblTitulo.setForeground(colorTextoVerdeOscuro);
                if (lblIcon.getIcon() == null) {
                    lblIcon.setForeground(colorTextoVerdeOscuro);
                }
            } else {
                // Estado pasivo: Sin fondo y texto gris suave
                lblTitulo.setForeground(colorTextoGrisBase);
                if (lblIcon.getIcon() == null) {
                    lblIcon.setForeground(colorTextoGrisBase);
                }
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
