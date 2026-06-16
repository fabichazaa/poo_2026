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
import vista.dialogos.DialogoEditarPaciente;
import vista.dialogos.DialogoNuevoTurno;

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
            ImageIcon iconoApp = new ImageIcon("imagenes/logo.png");
            setIconImage(iconoApp.getImage());
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono de la aplicación: " + e.getMessage());
        }
        
        getContentPane().setBackground(recursos.Color.BG);

        // --- PANEL SUPERIOR: Encabezado con bienvenida y contadores ---
        JPanel panelSuperiorAgrupado = new JPanel(new BorderLayout(0, 10));
        panelSuperiorAgrupado.setOpaque(false);
        panelSuperiorAgrupado.setBorder(new EmptyBorder(15, 25, 5, 25));

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

        panelContenedorSecciones.add(new vista.paneles.PanelMascotas(controlador), "PANTALLA_MASCOTAS");
        panelContenedorSecciones.add(new vista.paneles.PanelMedicamentos(controlador), "PANTALLA_MEDICAMENTOS");
        panelContenedorSecciones.add(new vista.paneles.PanelMas(controlador), "PANTALLA_MAS");

        add(panelContenedorSecciones, BorderLayout.CENTER);

        // --- BARRA DE NAVEGACIÓN INFERIOR OPTIMIZADA (SOLO 5 SECCIONES ACTIVAS) ---
        // 🌟 CAMBIO: Ajustamos a GridLayout(1, 5) para que los botones remanentes queden distribuidos de forma perfecta
        JPanel panelMenuInferior = new JPanel(new GridLayout(1, 5, 5, 0));
        panelMenuInferior.setBackground(Color.WHITE);
        panelMenuInferior.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, recursos.Color.BORDER),
                new EmptyBorder(8, 10, 8, 10)
        ));

        JButton btnInicio = crearBotonMenuNav("Inicio", "imagenes/emojis/casa.png", "🏠", "PANTALLA_INICIO");
        JButton btnRegistros = crearBotonMenuNav("Mascotas", "imagenes/emojis/patitas.png", "📋", "PANTALLA_MASCOTAS");
        JButton btnMedicamentos = crearBotonMenuNav("Medicamentos", "imagenes/emojis/pastilla.png", "💊", "PANTALLA_MEDICAMENTOS");
        JButton btnCitas = crearBotonMenuNav("Turnos", "imagenes/emojis/calendario.png", "📅", "PANTALLA_CITAS");
        JButton btnMas = crearBotonMenuNav("Registros", "imagenes/emojis/usuario.png", "👤", "PANTALLA_MAS");

        ((BotonMenuNav) btnInicio).setActivo(true);

        panelMenuInferior.add(btnInicio);
        panelMenuInferior.add(btnRegistros);
        panelMenuInferior.add(btnMedicamentos);
        panelMenuInferior.add(btnCitas);
        panelMenuInferior.add(btnMas);

        add(panelMenuInferior, BorderLayout.SOUTH);
    }

    private JPanel crearVistaInicio() {
        JPanel panelDashboard = new JPanel(new GridBagLayout());
        panelDashboard.setOpaque(false);
        panelDashboard.setBorder(new EmptyBorder(10, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 10, 0, 10);

        int altoBarra = 9;
        int radioEsquina = 16;

        // --- COLUMNA 1: Tarjeta Profesional ---
        JPanel cardUsuario = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint degradado = new GradientPaint(0, 0, new Color(45, 212, 191), getWidth(), 0, recursos.Color.PRIMARY);
                g2.setPaint(degradado);
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radioEsquina, radioEsquina));
                g2.fillRect(0, 0, getWidth(), altoBarra);
                g2.dispose();
            }
        };
        cardUsuario.setBackground(Color.WHITE);
        cardUsuario.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(recursos.Color.BORDER, 1, true), new EmptyBorder(25, 20, 20, 20)
        ));

        JPanel panelAvatarContenedor = new JPanel(new BorderLayout(0, 10));
        panelAvatarContenedor.setOpaque(false);

        JPanel panelFotoPerfil = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint degradadoDiagonal = new GradientPaint(0, 0, new Color(45, 212, 191), getWidth(), getHeight(), recursos.Color.PRIMARY);
                g2.setPaint(degradadoDiagonal);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
            }
        };
        panelFotoPerfil.setPreferredSize(new Dimension(110, 110));
        panelFotoPerfil.setLayout(new GridBagLayout());

        try {
            java.awt.image.BufferedImage imgBuffer = javax.imageio.ImageIO.read(new java.io.File("imagenes/emojis/vet.png"));
            panelFotoPerfil.add(new JLabel(escalarImagenAltaCalidad(imgBuffer, 95, 95)));
        } catch (IOException e) {
            JLabel lblIconoUser = new JLabel("👨‍⚕️");
            lblIconoUser.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
            panelFotoPerfil.add(lblIconoUser);
        }

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

        JPanel panelDatosUser = new JPanel();
        panelDatosUser.setOpaque(false);
        panelDatosUser.setLayout(new BoxLayout(panelDatosUser, BoxLayout.Y_AXIS));

        JLabel lblNombreUser = new JLabel(veterinarioLogueado.getNombre() + " " + veterinarioLogueado.getApellido(), SwingConstants.CENTER);
        lblNombreUser.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombreUser.setForeground(recursos.Color.INK);
        lblNombreUser.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRolUser = new JLabel("Veterinario Activo", SwingConstants.CENTER);
        lblRolUser.setFont(fuenteNormal);
        lblRolUser.setForeground(recursos.Color.PRIMARY);
        lblRolUser.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel cardMatricula = new JPanel(new GridLayout(2, 1, 0, 2));
        cardMatricula.setBackground(recursos.Color.CANVAS_GENERAL);
        cardMatricula.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(recursos.Color.BG, 1, true), new EmptyBorder(10, 15, 10, 15)
        ));
        JLabel lblMatTxt = new JLabel("Matrícula", SwingConstants.CENTER);
        lblMatTxt.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMatTxt.setForeground(recursos.Color.CAT_INACTIVO);
        JLabel lblMatNum = new JLabel(veterinarioLogueado.getMatricula(), SwingConstants.CENTER);
        lblMatNum.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblMatNum.setForeground(recursos.Color.INK);
        cardMatricula.add(lblMatTxt);
        cardMatricula.add(lblMatNum);

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

        gbc.gridx = 0; gbc.weightx = 0;
        panelDashboard.add(cardUsuario, gbc);

        // --- COLUMNA 2: Mis Próximos Turnos ---
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
                new LineBorder(recursos.Color.BORDER, 1, true), new EmptyBorder(20, 15, 15, 15)
        ));

        JPanel headerTurnosInterno = new JPanel(new BorderLayout());
        headerTurnosInterno.setOpaque(false);
        JLabel lblTituloTurnos = new JLabel("Mis Próximos Turnos");
        lblTituloTurnos.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTituloTurnos.setForeground(recursos.Color.INK);

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

        JScrollBar barraVertical = scrollTurnos.getVerticalScrollBar();
        barraVertical.setUI(new vista.componentes.ModernScrollBarUI(recursos.Color.WHITE)); 
        barraVertical.setPreferredSize(new Dimension(8, 0)); 
        barraVertical.setOpaque(false); 
        barraVertical.setUnitIncrement(12); 

        cardTurnos.add(scrollTurnos, BorderLayout.CENTER);
        cardTurnos.setPreferredSize(new Dimension(300, 400));

        gbc.gridx = 1; gbc.weightx = 1;
        panelDashboard.add(cardTurnos, gbc);

        // --- COLUMNA 3: Acciones Rápidas ---
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
                new LineBorder(recursos.Color.BORDER, 1, true), new EmptyBorder(20, 15, 15, 15)
        ));

        JLabel lblTituloAcciones = new JLabel("Acciones Rápidas");
        lblTituloAcciones.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTituloAcciones.setForeground(recursos.Color.INK);
        cardAcciones.add(lblTituloAcciones, BorderLayout.NORTH);

        JPanel panelBotonesAccion = new JPanel();
        panelBotonesAccion.setLayout(new BoxLayout(panelBotonesAccion, BoxLayout.Y_AXIS));
        panelBotonesAccion.setOpaque(false);

        JPanel btnRegistrarConsulta = crearFilaAccionEstilizada("Nueva Consulta", new Color(115, 236, 255), "imagenes/emojis/estetoscopio.png");
        btnRegistrarConsulta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegistrarConsulta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                DialogoNuevoTurno modalAlta = new DialogoNuevoTurno(PortalVeterinario.this, controlador);
                modalAlta.setVisible(true);
                panelCitas.actualizar();
            }
        });
        panelBotonesAccion.add(btnRegistrarConsulta);
        panelBotonesAccion.add(Box.createVerticalStrut(12));
        
        JPanel btnRegistrarPaciente = crearFilaAccionEstilizada("Registrar Paciente", new Color(99, 102, 241), "imagenes/emojis/perro_cara.png");
        btnRegistrarPaciente.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegistrarPaciente.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                DialogoEditarPaciente modalAlta = new DialogoEditarPaciente(PortalVeterinario.this, null);
                modalAlta.setVisible(true);
                
                // 🌟 CORREGIDO: Buscamos el PanelRegistros en el índice 2 de tus pestañas limpias
                if (panelContenedorSecciones.getComponent(2) instanceof vista.paneles.PanelMascotas pReg) {
                    pReg.actualizar();
                }
            }
        });
        panelBotonesAccion.add(btnRegistrarPaciente);        
        panelBotonesAccion.add(Box.createVerticalStrut(12));

        cardAcciones.add(panelBotonesAccion, BorderLayout.CENTER);

        JPanel panelStatusDia = new JPanel(new GridLayout(1, 2, 10, 0));
        panelStatusDia.setOpaque(false);

        int totalAtendidos = controlador.contarTurnosDelVeterinario(veterinarioLogueado, Turno.ESTADO_COMPLETADO);
        int totalPendientes = controlador.contarTurnosDelVeterinario(veterinarioLogueado, Turno.ESTADO_PENDIENTE);

        panelStatusDia.add(crearMiniContadorInferior(String.valueOf(totalAtendidos), "Atendidos", new Color(240, 253, 250), recursos.Color.PRIMARY, "imagenes/emojis/exito.png"));
        panelStatusDia.add(crearMiniContadorInferior(String.valueOf(totalPendientes), "Pendientes", new Color(254, 243, 199), recursos.Color.PENDING, "imagenes/emojis/reloj_arena.png"));

        cardAcciones.add(panelStatusDia, BorderLayout.SOUTH);

        gbc.gridx = 2; gbc.weightx = 0;
        panelDashboard.add(cardAcciones, gbc);

        return panelDashboard;
    }

    private JPanel crearTarjetaTurnoVisual(Turno t) {
        JPanel itemTurno = new JPanel(new BorderLayout(15, 0));
        itemTurno.setBackground(recursos.Color.CANVAS_GENERAL);
        itemTurno.setMaximumSize(new Dimension(385, 62));
        itemTurno.setPreferredSize(new Dimension(385, 62));
        itemTurno.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(recursos.Color.BG, 1, true), new EmptyBorder(6, 12, 6, 12)
        ));

        JLabel lblHora = new JLabel(t.getHora(), SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(1, 2, getWidth() - 2, getHeight() - 3, 8, 8);
                g2.setColor(recursos.Color.SURFACE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(recursos.Color.BORDER);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblHora.setFont(fuenteNormal);
        lblHora.setForeground(recursos.Color.INK);
        lblHora.setPreferredSize(new Dimension(65, 28));

        JPanel panelHoraWrapper = new JPanel(new GridBagLayout());
        panelHoraWrapper.setOpaque(false);
        panelHoraWrapper.add(lblHora);
        itemTurno.add(panelHoraWrapper, BorderLayout.WEST);

        JPanel panelContenidoCentral = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelContenidoCentral.setOpaque(false);

        String rutaImagen = t.getAnimal().getImagen();
        JLabel lblEmoji = new JLabel();
        ImageIcon iconoAnimal = new ImageIcon(rutaImagen);
        if (iconoAnimal.getImage() != null) {
            ImageIcon iconoEscalado = escalarImagenAltaCalidad(iconoAnimal.getImage(), 22, 22);
            lblEmoji.setIcon(iconoEscalado);
        }
        panelContenidoCentral.add(lblEmoji);

        JPanel panelLabelsInternos = new JPanel(new GridLayout(2, 1, 0, 0));
        panelLabelsInternos.setOpaque(false);

        JLabel lblPaciente = new JLabel(t.getAnimal().getNombre());
        lblPaciente.setFont(fuenteSubtitulos);
        lblPaciente.setForeground(new Color(15, 23, 42));

        JLabel lblSubDescripcion = new JLabel(t.getTipo().getDescripcion());
        lblSubDescripcion.setFont(fuenteNormal);
        lblSubDescripcion.setForeground(recursos.Color.CAT_INACTIVO);

        panelLabelsInternos.add(lblPaciente);
        panelLabelsInternos.add(lblSubDescripcion);
        panelContenidoCentral.add(panelLabelsInternos);

        JPanel panelAlineacionWrapper = new JPanel(new GridBagLayout());
        panelAlineacionWrapper.setOpaque(false);
        GridBagConstraints gbcCentro = new GridBagConstraints();
        gbcCentro.anchor = GridBagConstraints.WEST; 
        gbcCentro.weightx = 1.0;                    
        panelAlineacionWrapper.add(panelContenidoCentral, gbcCentro);

        itemTurno.add(panelAlineacionWrapper, BorderLayout.CENTER);

        PildoraBadge lblBadgePildora = new PildoraBadge(t.getTipo().getDescripcion());
        lblBadgePildora.setFont(fuenteNormal);
        lblBadgePildora.setBorder(new EmptyBorder(4, 12, 4, 12));
        lblBadgePildora.setCustomBackground(t.getTipo().getBadgeBgColor());
        lblBadgePildora.setForeground(t.getTipo().getBadgeFgColor());

        JPanel panelBadgeWrapper = new JPanel(new GridBagLayout());
        panelBadgeWrapper.setOpaque(false);
        panelBadgeWrapper.add(lblBadgePildora);
        itemTurno.add(panelBadgeWrapper, BorderLayout.EAST);

        return itemTurno;
    }

    private JPanel crearFilaAccionEstilizada(String titulo, Color colorFondoIcono, String emojiIcono) {
        JPanel panelFila = new JPanel(new BorderLayout(15, 0)) {
            private boolean mouseEncima = false; 
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) { mouseEncima = true; repaint(); }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) { mouseEncima = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mouseEncima ? new Color(241, 245, 249) : recursos.Color.CANVAS_GENERAL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(recursos.Color.BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        
        panelFila.setOpaque(false);
        panelFila.setBorder(new EmptyBorder(6, 12, 6, 12));
        panelFila.setMaximumSize(new Dimension(320, 52));
        panelFila.setPreferredSize(new Dimension(320, 52));

        JPanel panelCuadroIcono = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(colorFondoIcono);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
            }
        };
        panelCuadroIcono.setOpaque(false);
        panelCuadroIcono.setPreferredSize(new Dimension(40, 40));
        panelCuadroIcono.setLayout(new GridBagLayout());

        JLabel lblEmoji = new JLabel();
        if (emojiIcono.endsWith(".png")) {
            try {
                java.awt.image.BufferedImage imgBuffer = javax.imageio.ImageIO.read(new java.io.File(emojiIcono));
                lblEmoji.setIcon(escalarImagenAltaCalidad(imgBuffer, 24, 24));
            } catch (IOException e) {
                lblEmoji.setText("?");
                lblEmoji.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            }
        } else {
            lblEmoji.setText(emojiIcono);
            lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        }
        panelCuadroIcono.add(lblEmoji);
        panelFila.add(panelCuadroIcono, BorderLayout.WEST);

        JPanel panelContenedorTexto = new JPanel(new GridBagLayout());
        panelContenedorTexto.setOpaque(false);

        JLabel lblT = new JLabel(titulo);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        lblT.setForeground(new Color(15, 23, 42));

        GridBagConstraints gbcTexto = new GridBagConstraints();
        gbcTexto.anchor = GridBagConstraints.WEST; 
        gbcTexto.weightx = 1.0;                    
        panelContenedorTexto.add(lblT, gbcTexto);

        panelFila.add(panelContenedorTexto, BorderLayout.CENTER);

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
        lblT.setForeground(recursos.Color.CAT_INACTIVO);
        JLabel lblV = new JLabel(valor, SwingConstants.CENTER);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblV.setForeground(new Color(15, 23, 42));
        panel.add(lblT);
        panel.add(lblV);
        return panel;
    }

    private JPanel crearMiniContadorInferior(String valor, String etiqueta, Color fondo, Color colorTexto, String rutaIcono) {
        JPanel panel = new JPanel(new BorderLayout(12, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(fondo);
                g2.fillRect(0, 0, getWidth(), getHeight());

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
        panel.setBorder(new EmptyBorder(8, 14, 8, 14));
        panel.setPreferredSize(new Dimension(135, 56));

        JLabel lblIcono = new JLabel();
        try {
            java.awt.image.BufferedImage imgBuffer = javax.imageio.ImageIO.read(new java.io.File(rutaIcono));
            lblIcono.setIcon(escalarImagenAltaCalidad(imgBuffer, 22, 22));
        } catch (IOException e) {
            lblIcono.setText("•");
            lblIcono.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblIcono.setForeground(colorTexto);
        }

        JPanel panelIconoWrapper = new JPanel(new GridBagLayout());
        panelIconoWrapper.setOpaque(false);
        panelIconoWrapper.add(lblIcono);
        panel.add(panelIconoWrapper, BorderLayout.WEST);

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

        btn.addActionListener(e -> {
            switch (claveCapa) {
                case "PANTALLA_MASCOTAS" ->
                    ((vista.paneles.PanelMascotas) panelContenedorSecciones.getComponent(2)).actualizar();
                case "PANTALLA_MEDICAMENTOS" ->
                    ((vista.paneles.PanelMedicamentos) panelContenedorSecciones.getComponent(3)).actualizar();
                case "PANTALLA_MAS" ->
                    ((vista.paneles.PanelMas) panelContenedorSecciones.getComponent(4)).actualizar();
                case "PANTALLA_CITAS" ->
                    panelCitas.actualizar();
                default -> {
                }
            }
            navegadorCapas.show(panelContenedorSecciones, claveCapa);

            JPanel panelMenu = (JPanel) btn.getParent();
            if (panelMenu != null) {
                for (Component comp : panelMenu.getComponents()) {
                    if (comp instanceof BotonMenuNav b) {
                        b.setActivo(b == btn); 
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
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | javax.swing.UnsupportedLookAndFeelException e) {
            System.out.println("No se pudo cargar la fuente del sistema UI.");
        }
        SwingUtilities.invokeLater(() -> new PortalVeterinario().setVisible(true));
    }

    private ImageIcon escalarImagenAltaCalidad(Image srcImg, int w, int h) {
        java.awt.image.BufferedImage resizedImg = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return new ImageIcon(resizedImg);
    }

    private class BotonMenuNav extends JButton {
        private boolean mouseEncima = false;
        private boolean activo = false;
        private final Color colorFondoVerdeSuave = new Color(240, 253, 250); 
        private final Color colorTextoVerdeOscuro = recursos.Color.PRIMARY; 
        private final Color colorTextoGrisBase = new Color(100, 116, 139);   

        public BotonMenuNav(String titulo, String icono, String unicodeIcon, String claveCapa) {
            setFocusPainted(false); setContentAreaFilled(false); setBorderPainted(false); setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(85, 70));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(8, 20, 8, 20));

            JLabel lblIcon = new JLabel();
            lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (icono != null && new java.io.File(icono).exists()) {
                try {
                    java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(new java.io.File(icono));
                    lblIcon.setIcon(escalarImagenAltaCalidad(img, 32, 32));
                } catch (IOException e) {
                    lblIcon.setText(unicodeIcon);
                    lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
                }
            } else {
                lblIcon.setText(unicodeIcon);
                lblIcon.setBorder(new EmptyBorder(2,1,1,1));
                lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            }

            JLabel lblTitulo = new JLabel(titulo);
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

            add(Box.createVerticalGlue());
            add(lblIcon);
            add(Box.createVerticalStrut(5));
            add(lblTitulo);
            add(Box.createVerticalGlue());

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseEntered(java.awt.event.MouseEvent e) { mouseEncima = true; repaint(); }
                @Override public void mouseExited(java.awt.event.MouseEvent e) { mouseEncima = false; repaint(); }
            });
        }

        public void setActivo(boolean estado) {
            this.activo = estado;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            JLabel lblIcon = (JLabel) getComponent(1);
            JLabel lblTitulo = (JLabel) getComponent(3);

            if (activo || mouseEncima) {
                g2.setColor(colorFondoVerdeSuave);
                int x = 6; int y = 5;
                int anchoPildora = getWidth() - 12;
                int altoPildora = getHeight() - 10;
                g2.fillRoundRect(x, y, anchoPildora, altoPildora, 18, 18);

                lblTitulo.setForeground(colorTextoVerdeOscuro);
                if (lblIcon.getIcon() == null) {
                    lblIcon.setForeground(colorTextoVerdeOscuro);
                }
            } else {
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