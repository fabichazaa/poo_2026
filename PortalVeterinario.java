
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import objetos.*;

public class PortalVeterinario extends JFrame {

    private Veterinaria miVeterinaria;
    private Veterinario veterinarioLogueado;

    // Componentes para la navegación por capas
    private JPanel panelContenedorSecciones;
    private CardLayout navegadorCapas;

    // Componentes de la sección Citas
    private JPanel panelListaCitasDinamica;
    private JComboBox<String> comboFiltroAgenda;

    // Variables globales de fuentes
    private Font fuenteTitulo;
    private Font fuenteSubtitulos;
    private Font fuenteNormal;

    public PortalVeterinario() {
        inicializarDatosVeterinaria();

        fuenteTitulo = cargarFuentePersonalizada(22f);
        fuenteSubtitulos = cargarFuentePersonalizada(14f);
        fuenteNormal = cargarFuentePersonalizada(12f);

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

        int turnos = veterinarioLogueado.getTurnos().size(); // Contar turnos del veterinario logueado
        JLabel lblSaludo = new JLabel("<html><font color='#1E293B'><b>¡Hola, Dr. " + veterinarioLogueado.getApellido() + "! 👋</b></font><br><font color='#64748B' size='4'>Tenés <font color='#0D9488'><b>" + turnos + " turnos</b></font> pendientes hoy. Que tengas un gran día.</font></html>");
        lblSaludo.setFont(fuenteTitulo);
        lblSaludo.setSize(450, 100);
        panelHeaderLinea1.add(lblSaludo, BorderLayout.WEST);

        JLabel lblFechaActual = new JLabel("<html><div style='text-align: right;'><font color='#94A3B8'>Hoy</font><br><font color='#1E293B'><b>Viernes, 06 Jun 2026</b></font></div></html>");
        lblFechaActual.setFont(fuenteNormal);
        panelHeaderLinea1.add(lblFechaActual, BorderLayout.EAST);
        panelSuperiorAgrupado.add(panelHeaderLinea1, BorderLayout.NORTH);

        // Línea 2: Tarjetas de estadísticas superiores
        JPanel panelStatsSuperiores = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 15));
        panelStatsSuperiores.setOpaque(false);
        panelStatsSuperiores.add(crearCardEstadisticaSuperior("3", "Atendidos", new Color(22f / 255f, 163f / 255f, 74f / 255f, 0.1f), new Color(22, 163, 74), "imagenes/emojis/exito.png"));
        panelStatsSuperiores.add(crearCardEstadisticaSuperior("4", "Mis Turnos", new Color(14f / 255f, 116f / 255f, 144f / 255f, 0.1f), new Color(14, 116, 144), "imagenes/emojis/calendario.png"));
        panelStatsSuperiores.add(crearCardEstadisticaSuperior("1", "Pendientes", new Color(217f / 255f, 119f / 255f, 6f / 255f, 0.1f), new Color(217, 119, 6), "imagenes/emojis/reloj_arena.png"));
        panelSuperiorAgrupado.add(panelStatsSuperiores, BorderLayout.SOUTH);

        add(panelSuperiorAgrupado, BorderLayout.NORTH);

        // --- CUERPO PRINCIPAL CON CARDLAYOUT ---
        navegadorCapas = new CardLayout();
        panelContenedorSecciones = new JPanel(navegadorCapas);
        panelContenedorSecciones.setOpaque(false);

        panelContenedorSecciones.add(crearVistaInicio(), "PANTALLA_INICIO");
        panelContenedorSecciones.add(crearVistaCitas(), "PANTALLA_CITAS");

        panelContenedorSecciones.add(crearPanelPlaceholder("Registros Clínicos"), "PANTALLA_REGISTROS");
        panelContenedorSecciones.add(crearPanelPlaceholder("Portal de Adopciones"), "PANTALLA_ADOPCION");
        panelContenedorSecciones.add(crearPanelPlaceholder("Notas y Recordatorios"), "PANTALLA_NOTAS");
        panelContenedorSecciones.add(crearPanelPlaceholder("Configuración y Más"), "PANTALLA_MAS");

        add(panelContenedorSecciones, BorderLayout.CENTER);

        // --- BARRA DE NAVEGACIÓN INFERIOR BLANCA Y ESTILIZADA ---
        JPanel panelMenuInferior = new JPanel(new GridLayout(1, 6, 5, 0));
        panelMenuInferior.setBackground(Color.WHITE);
        panelMenuInferior.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)),
                new EmptyBorder(8, 10, 8, 10)
        ));

        JButton btnInicio = crearBotonMenuNav(" Inicio ", "🏠");
        JButton btnRegistros = crearBotonMenuNav(" Registros ", "📂");
        JButton btnAdopcion = crearBotonMenuNav(" Adopción ", "🐾");
        JButton btnNotas = crearBotonMenuNav(" Notas ", "📝");
        JButton btnCitas = crearBotonMenuNav(" Citas ", "📅");
        JButton btnMas = crearBotonMenuNav(" Más ", "💬");

        btnInicio.addActionListener(e -> navegadorCapas.show(panelContenedorSecciones, "PANTALLA_INICIO"));
        btnCitas.addActionListener(e -> {
            actualizarListaCitasSeccion();
            navegadorCapas.show(panelContenedorSecciones, "PANTALLA_CITAS");
        });
        btnRegistros.addActionListener(e -> navegadorCapas.show(panelContenedorSecciones, "PANTALLA_REGISTROS"));
        btnAdopcion.addActionListener(e -> navegadorCapas.show(panelContenedorSecciones, "PANTALLA_ADOPCION"));
        btnNotas.addActionListener(e -> navegadorCapas.show(panelContenedorSecciones, "PANTALLA_NOTAS"));
        btnMas.addActionListener(e -> navegadorCapas.show(panelContenedorSecciones, "PANTALLA_MAS"));

        panelMenuInferior.add(btnInicio);
        panelMenuInferior.add(btnRegistros);
        panelMenuInferior.add(btnAdopcion);
        panelMenuInferior.add(btnNotas);
        panelMenuInferior.add(btnCitas);
        panelMenuInferior.add(btnMas);

        add(panelMenuInferior, BorderLayout.SOUTH);
    }

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

        // --- COLUMNA 1: Tarjeta Profesional (Borde superior Verde Esmeralda) ---
        JPanel cardUsuario = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(13, 148, 136));
                g.fillRect(0, 0, getWidth(), 5);
            }
        };
        cardUsuario.setBackground(Color.WHITE);
        cardUsuario.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true), new EmptyBorder(25, 20, 20, 20)
        ));

        // Contenedor del avatar del doctor (Emoji nativo estilizado)
        JPanel panelAvatarContenedor = new JPanel(new BorderLayout(0, 10));
        panelAvatarContenedor.setOpaque(false);

        JPanel panelFotoPerfil = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 168, 150)); // Fondo turquesa/esmeralda idéntico
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        panelFotoPerfil.setPreferredSize(new Dimension(110, 110));
        panelFotoPerfil.setLayout(new GridBagLayout());

        // Cargar imagen de veterinario desde archivo
        try {
            String rutaImagen = "imagenes/emojis/vet.png";
            ImageIcon iconoVet = new ImageIcon(rutaImagen);
            Image imagenEscalada = iconoVet.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel lblIconoUser = new JLabel(new ImageIcon(imagenEscalada));
            panelFotoPerfil.add(lblIconoUser);
        } catch (Exception e) {
            // Si la imagen no se carga, usar el emoji como fallback
            JLabel lblIconoUser = new JLabel("👨‍⚕️");
            lblIconoUser.setFont(new Font("Segoe UI", Font.PLAIN, 50));
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

        // Detalles inferiores (Especialidad y Turno)
        JPanel panelFilaDetalles = new JPanel(new GridLayout(1, 2, 10, 0));
        panelFilaDetalles.setOpaque(false);
        panelFilaDetalles.add(crearMiniBadgeInformación("Especialidad", "General"));
        panelFilaDetalles.add(crearMiniBadgeInformación("Turno", "Mañana"));

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

        // --- COLUMNA 2: Mis Próximos Turnos (Borde superior Violeta) ---
        JPanel cardTurnos = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(124, 58, 237));
                g.fillRect(0, 0, getWidth(), 5);
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
        cardTurnos.add(scrollTurnos, BorderLayout.CENTER);
        cardTurnos.setPreferredSize(new Dimension(300, 400));

        gbc.gridx = 1;
        gbc.weightx = 1;
        panelDashboard.add(cardTurnos, gbc);

        // --- COLUMNA 3: Acciones Rápidas (Borde superior Fucsia) ---
        JPanel cardAcciones = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(236, 72, 153));
                g.fillRect(0, 0, getWidth(), 5);
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

        // Se configuran los emojis nativos de los botones como se observa en la captura
        panelBotonesAccion.add(crearFilaAccionEstilizada("Nueva Consulta", "Iniciar atención a un paciente", new Color(13, 148, 136), "imagenes/emojis/estetoscopio.png"));
        panelBotonesAccion.add(Box.createVerticalStrut(12));
        panelBotonesAccion.add(crearFilaAccionEstilizada("Registrar Paciente", "Agregar nuevo animal al sistema", new Color(99, 102, 241), "🐶"));
        panelBotonesAccion.add(Box.createVerticalStrut(12));
        panelBotonesAccion.add(crearFilaAccionEstilizada("Ver Historiales", "Consultar historial clínico", new Color(245, 158, 11), "📁"));

        cardAcciones.add(panelBotonesAccion, BorderLayout.CENTER);

        JPanel panelStatusDia = new JPanel(new GridLayout(1, 2, 10, 0));
        panelStatusDia.setOpaque(false);
        panelStatusDia.add(crearMiniContadorInferior("3", "Atendidos "));
        panelStatusDia.add(crearMiniContadorInferior("4", "Mis Turnos"));
        cardAcciones.add(panelStatusDia, BorderLayout.SOUTH);
        cardAcciones.setPreferredSize(new Dimension(280, 400));

        gbc.gridx = 2;
        gbc.weightx = 0;
        panelDashboard.add(cardAcciones, gbc);

        return panelDashboard;
    }

    // ---------------------------------------------------------------------
    // VISTA 2: SECCIÓN SELECCIONABLE DE CITAS
    // ---------------------------------------------------------------------
    private JPanel crearVistaCitas() {
        JPanel panelCitasPrincipal = new JPanel(new BorderLayout(0, 15));
        panelCitasPrincipal.setOpaque(false);
        panelCitasPrincipal.setBorder(new EmptyBorder(10, 25, 15, 25));

        JPanel panelFiltrosCita = new JPanel(new BorderLayout());
        panelFiltrosCita.setBackground(Color.WHITE);
        panelFiltrosCita.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true), new EmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblSeleccion = new JLabel("Visualización de la Agenda: ");
        lblSeleccion.setFont(fuenteSubtitulos);

        comboFiltroAgenda = new JComboBox<>(new String[]{"Mis Turnos Asignados 👤", "Agenda General de la Veterinaria 🏢"});
        comboFiltroAgenda.setFont(fuenteNormal);
        comboFiltroAgenda.addActionListener(e -> actualizarListaCitasSeccion());

        panelFiltrosCita.add(lblSeleccion, BorderLayout.WEST);
        panelFiltrosCita.add(comboFiltroAgenda, BorderLayout.CENTER);
        panelCitasPrincipal.add(panelFiltrosCita, BorderLayout.NORTH);

        panelListaCitasDinamica = new JPanel();
        panelListaCitasDinamica.setLayout(new BoxLayout(panelListaCitasDinamica, BoxLayout.Y_AXIS));
        panelListaCitasDinamica.setBackground(Color.WHITE);

        JScrollPane scrollCitas = new JScrollPane(panelListaCitasDinamica);
        scrollCitas.setBorder(new LineBorder(Color.WHITE, 1, true));
        panelCitasPrincipal.add(scrollCitas, BorderLayout.CENTER);

        return panelCitasPrincipal;
    }

    private void actualizarListaCitasSeccion() {
        panelListaCitasDinamica.removeAll();
        panelListaCitasDinamica.add(Box.createVerticalStrut(10));

        ArrayList<Turno> todosLosTurnos = miVeterinaria.getListaTurnos();
        int seleccion = comboFiltroAgenda.getSelectedIndex();

        for (Turno t : todosLosTurnos) {
            if (seleccion == 0) {
                if (t.getVeterinario().equals(veterinarioLogueado)) {
                    panelListaCitasDinamica.add(crearTarjetaTurnoCompleta(t, false));
                    panelListaCitasDinamica.add(Box.createVerticalStrut(10));
                }
            } else {
                panelListaCitasDinamica.add(crearTarjetaTurnoCompleta(t, true));
                panelListaCitasDinamica.add(Box.createVerticalStrut(10));
            }
        }

        panelListaCitasDinamica.revalidate();
        panelListaCitasDinamica.repaint();
    }

    // --- RENDERIZADO CON EMOJIS COMPATIBLES SEÚN LA ESPECIE ---
    private JPanel crearTarjetaTurnoVisual(Turno t) {
        JPanel itemTurno = new JPanel(new BorderLayout(15, 0));
        itemTurno.setBackground(new Color(248, 250, 252));
        itemTurno.setMaximumSize(new Dimension(385, 70));
        itemTurno.setPreferredSize(new Dimension(385, 70));
        itemTurno.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(241, 245, 249), 1, true), new EmptyBorder(12, 12, 12, 12)
        ));

        // Caja de la hora
        JLabel lblHora = new JLabel(t.getHora(), SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw shadow
                g2.setColor(new Color(0, 0, 0, 25));
                g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 4, 12, 12);

                // Draw white background
                g2.setColor(new Color(255, 255, 255));
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 12, 12);

                // Draw border
                g2.setColor(new Color(220, 225, 230));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 12, 12);

                super.paintComponent(g);
            }
        };
        lblHora.setFont(fuenteNormal); // Usa tu Google Sans sin romper nada
        lblHora.setForeground(new Color(30, 41, 59));
        lblHora.setPreferredSize(new Dimension(75, 35));
        itemTurno.add(lblHora, BorderLayout.WEST);

        // Bloque central estructurado: Separamos el Emoji del Texto
        JPanel panelTextosTurno = new JPanel(new BorderLayout(8, 0));
        panelTextosTurno.setOpaque(false);

        // Cargar imagen de animal según tipo
        String rutaImagen = (t.getAnimal() instanceof Perro) ? "imagenes/emojis/perro.png" : "imagenes/emojis/gato.png";
        JLabel lblEmoji = new JLabel();
        try {
            ImageIcon iconoAnimal = new ImageIcon(rutaImagen);
            Image imagenEscalada = iconoAnimal.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            lblEmoji.setIcon(new ImageIcon(imagenEscalada));
        } catch (Exception e) {
            // Si la imagen no se carga, usar el emoji como fallback
            String emojiMascota = (t.getAnimal() instanceof Perro) ? "🐕" : "🐈";
            lblEmoji.setText(emojiMascota);
            lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        }
        panelTextosTurno.add(lblEmoji, BorderLayout.WEST);

        // Los textos de información siguen usando Google Sans mediante tus fuentes globales
        JPanel panelLabelsInternos = new JPanel(new GridLayout(2, 1, 0, 1));
        panelLabelsInternos.setOpaque(false);

        JLabel lblPaciente = new JLabel(t.getAnimal().getNombre());
        lblPaciente.setFont(fuenteSubtitulos);
        lblPaciente.setForeground(new Color(15, 23, 42));

        panelLabelsInternos.add(lblPaciente);
        panelTextosTurno.add(panelLabelsInternos, BorderLayout.CENTER);

        itemTurno.add(panelTextosTurno, BorderLayout.CENTER);

        // Tag dinámico a la derecha (Píldora)
        PildoraBadge lblBadgePildora = new PildoraBadge(t.getTipo().getDescripcion());
        lblBadgePildora.setFont(fuenteNormal);
        lblBadgePildora.setBorder(new EmptyBorder(4, 12, 4, 12));

        switch (t.getTipo()) {
            case CIRUGIA:
                lblBadgePildora.setCustomBackground(new Color(254, 226, 226));
                lblBadgePildora.setForeground(new Color(220, 38, 38));
                break;
            case CONSULTA_GENERAL:
                lblBadgePildora.setCustomBackground(new Color(219, 234, 254));
                lblBadgePildora.setForeground(new Color(37, 99, 235));
                break;
            case ANALISIS:
                lblBadgePildora.setCustomBackground(new Color(243, 232, 255));
                lblBadgePildora.setForeground(new Color(147, 51, 234));
                break;
            default:
                lblBadgePildora.setCustomBackground(new Color(220, 252, 231));
                lblBadgePildora.setForeground(new Color(22, 163, 74));
                lblBadgePildora.setText("Vacunación");
                break;
        }

        JPanel panelBadgeWrapper = new JPanel(new GridBagLayout());
        panelBadgeWrapper.setOpaque(false);
        panelBadgeWrapper.add(lblBadgePildora);
        itemTurno.add(panelBadgeWrapper, BorderLayout.EAST);

        return itemTurno;
    }

    // Fila de acciones usando emojis o imágenes en cajas de colores
    private JPanel crearFilaAccionEstilizada(String titulo, String sub, Color colorFondoIcono, String emojiIcono) {
        JPanel panelFila = new JPanel(new BorderLayout(15, 0));
        panelFila.setBackground(new Color(248, 250, 252));
        panelFila.setMaximumSize(new Dimension(320, 58));
        panelFila.setPreferredSize(new Dimension(320, 58));
        panelFila.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(241, 245, 249), 1, true), new EmptyBorder(8, 12, 8, 12)
        ));

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

        // Cargar imagen si es ruta PNG, sino usar como emoji
        JLabel lblEmoji = new JLabel();
        if (emojiIcono.endsWith(".png")) {
            try {
                ImageIcon icono = new ImageIcon(emojiIcono);
                Image imagenEscalada = icono.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                lblEmoji.setIcon(new ImageIcon(imagenEscalada));
            } catch (Exception e) {
                lblEmoji.setText("?");
                lblEmoji.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            }
        } else {
            lblEmoji.setText(emojiIcono);
            lblEmoji.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        }
        panelCuadroIcono.add(lblEmoji);
        panelFila.add(panelCuadroIcono, BorderLayout.WEST);

        JPanel panelTextos = new JPanel(new GridLayout(2, 1, 0, 1));
        panelTextos.setOpaque(false);
        JLabel lblT = new JLabel(titulo);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblT.setForeground(new Color(15, 23, 42));
        JLabel lblS = new JLabel(sub);
        lblS.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblS.setForeground(new Color(148, 163, 184));
        panelTextos.add(lblT);
        panelTextos.add(lblS);
        panelFila.add(panelTextos, BorderLayout.CENTER);

        JLabel lblFlechita = new JLabel(">");
        lblFlechita.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFlechita.setForeground(new Color(203, 213, 225));
        panelFila.add(lblFlechita, BorderLayout.EAST);

        return panelFila;
    }

    private JPanel crearTarjetaTurnoCompleta(Turno t, boolean mostrarVeterinario) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(new Color(245, 249, 252));
        card.setMaximumSize(new Dimension(750, 65));
        card.setPreferredSize(new Dimension(750, 65));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 230, 240), 1, true), new EmptyBorder(8, 15, 8, 15)
        ));

        JLabel lblHora = new JLabel(t.getHora(), SwingConstants.CENTER);
        lblHora.setFont(fuenteSubtitulos);
        lblHora.setForeground(Color.WHITE);
        lblHora.setBackground(new Color(13, 148, 136));
        lblHora.setOpaque(true);
        lblHora.setPreferredSize(new Dimension(75, 35));
        card.add(lblHora, BorderLayout.WEST);

        JPanel panelTextos = new JPanel(new GridLayout(2, 1, 0, 2));
        panelTextos.setOpaque(false);

        String infoPrincipal = "Paciente: " + t.getAnimal().getNombre() + " (" + t.getTipo().getDescripcion() + ") - Duración: " + t.getTipo().getDuracionMinutos() + " min.";
        JLabel lblInfo = new JLabel(infoPrincipal);
        lblInfo.setFont(fuenteSubtitulos);

        String infoSecundaria = "Fecha: " + t.getFecha() + " | Responsable: " + t.getAnimal().getResponsable().getNombre() + " " + t.getAnimal().getResponsable().getApellido();
        if (mostrarVeterinario) {
            infoSecundaria += " | Atendido por: Dr. " + t.getVeterinario().getApellido();
        }
        JLabel lblDetalle = new JLabel(infoSecundaria);
        lblDetalle.setFont(fuenteNormal);
        lblDetalle.setForeground(Color.GRAY);

        panelTextos.add(lblInfo);
        panelTextos.add(lblDetalle);
        card.add(panelTextos, BorderLayout.CENTER);

        return card;
    }

    private JPanel crearCardEstadisticaSuperior(String valor, String etiqueta, Color fondo, Color colorTexto, String iconStr) {
        JPanel card = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 1)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                g2.setColor(fondo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createLineBorder(colorTexto, 1, true));
        card.setPreferredSize(new Dimension(110, 42));

        JLabel lblIcono = new JLabel();
        // Verificar si es una ruta de imagen o un emoji
        if (iconStr.endsWith(".png")) {
            try {
                ImageIcon icono = new ImageIcon(iconStr);
                Image imagenEscalada = icono.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                lblIcono.setIcon(new ImageIcon(imagenEscalada));
            } catch (Exception e) {
                lblIcono.setText("?");
                lblIcono.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                lblIcono.setForeground(colorTexto);
            }
        } else {
            lblIcono.setText(iconStr);
            lblIcono.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblIcono.setForeground(colorTexto);
        }

        JLabel lblVal = new JLabel(valor);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblVal.setForeground(colorTexto);

        JLabel lblEt = new JLabel(etiqueta);
        lblEt.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblEt.setForeground(new Color(100, 116, 139));

        JPanel panelTextos = new JPanel(new GridLayout(2, 1, 0, -2));
        panelTextos.setOpaque(false);
        panelTextos.add(lblVal);
        panelTextos.add(lblEt);

        card.add(lblIcono);
        card.add(panelTextos);
        return card;
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

    private JPanel crearMiniContadorInferior(String valor, String etiqueta) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(240, 253, 250));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createLineBorder(new Color(204, 251, 241), 1, true));

        JLabel lblV = new JLabel(valor);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblV.setForeground(new Color(13, 148, 136));

        JLabel lblE = new JLabel(etiqueta);
        lblE.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblE.setForeground(new Color(100, 116, 139));

        JPanel panelTxt = new JPanel(new GridLayout(2, 1, 0, -2));
        panelTxt.setOpaque(false);
        panelTxt.add(lblV);
        panelTxt.add(lblE);

        panel.add(panelTxt);
        return panel;
    }

    private JPanel crearPanelPlaceholder(String tituloSeccion) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel lbl = new JLabel("Sección: " + tituloSeccion + " (Próximamente)");
        lbl.setFont(fuenteSubtitulos);
        panel.add(lbl);
        return panel;
    }

    private JButton crearBotonMenuNav(String titulo, String icono) {
        JButton btn = new JButton("<html><center><font size='5'>" + icono + "</font><br/><b style='white-space: nowrap;'>" + titulo + "</b></center></html>");
        btn.setFont(fuenteNormal);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 12, 15, 12));
        btn.setForeground(new Color(100, 116, 139));
        btn.setBackground(Color.WHITE);
        btn.setPreferredSize(new Dimension(120, 80));
        return btn;
    }

    private void inicializarDatosVeterinaria() {
        miVeterinaria = new Veterinaria("San Roque", true);

        veterinarioLogueado = new Veterinario("22333444", "Carlos", "Páez", "MP-9854");
        Veterinario otroVet = new Veterinario("55555555", "Laura", "Gómez", "MP-1024");

        miVeterinaria.registrarVeterinario(veterinarioLogueado);
        miVeterinaria.registrarVeterinario(otroVet);

        Responsable cliente = new Responsable("12345678", "Claudio", "Chiqui");
        miVeterinaria.registrarCliente(cliente);

        Perro hulk = new Perro("Hulk", LocalDate.of(2023, 4, 15), true, cliente, "Dogo de Burdeos");
        Gato luna = new Gato("Luna", LocalDate.of(2025, 8, 20), false, cliente, "Siamés");
        Perro cheese = new Perro("Cheese", LocalDate.of(2018, 1, 10), true, cliente, "Beagle");
        Gato mochi = new Gato("Mochi", LocalDate.of(2024, 2, 14), false, cliente, "Siamés");

        cliente.agregarMascota(hulk);
        cliente.agregarMascota(luna);
        cliente.agregarMascota(cheese);
        cliente.agregarMascota(mochi);

        miVeterinaria.registrarTurno(new Turno(1, "05/06/2026", "09:30 AM", veterinarioLogueado, hulk, TipoTurno.CIRUGIA));
        miVeterinaria.registrarTurno(new Turno(2, "05/06/2026", "10:15 AM", veterinarioLogueado, luna, TipoTurno.CONSULTA_GENERAL));
        miVeterinaria.registrarTurno(new Turno(3, "05/06/2026", "11:45 AM", veterinarioLogueado, hulk, TipoTurno.ANALISIS));
        miVeterinaria.registrarTurno(new Turno(4, "05/06/2026", "02:00 PM", veterinarioLogueado, mochi, TipoTurno.VACUNACION));
    }

    // Badge redondeado con control de colores
    private static class BadgeRedondeado extends JLabel {

        private Color bgColor;
        private Color borderColor;

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

            // Draw background with rounded corners
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

            // Draw border
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(0.8f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);

            super.paintComponent(g);
        }
    }

    // Inner class for pill-shaped badge
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

            // Draw background with rounded corners
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

            // Draw border (darker than background)
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
            Font fuenteBaseUI = cargarFuenteBase();

            UIManager.put("Label.font", fuenteBaseUI);
            UIManager.put("Button.font", fuenteBaseUI);
            UIManager.put("ComboBox.font", fuenteBaseUI);
        } catch (Exception e) {
            System.out.println("No se pudo cargar la fuente del sistema UI.");
        }

        SwingUtilities.invokeLater(() -> new PortalVeterinario().setVisible(true));
    }

    private static Font cargarFuenteBase() {
        try {
            String[] rutasCandidatas = {
                "recursos/GoogleSans.ttf",
                "recursos\\GoogleSans.ttf",
                "./recursos/GoogleSans.ttf"
            };
            for (String ruta : rutasCandidatas) {
                java.io.File f = new java.io.File(ruta);
                if (f.exists()) {
                    return Font.createFont(Font.TRUETYPE_FONT, f).deriveFont(12f);
                }
            }
        } catch (Exception e) {
            // ignorar, se usa la fuente por defecto del sistema
        }
        return new Font("Segoe UI", Font.PLAIN, 12);
    }

    private Font cargarFuentePersonalizada(float tamano) {
        try {
            String[] rutasCandidatas = {
                "recursos/GoogleSans.ttf",
                "recursos\\GoogleSans.ttf",
                "./recursos/GoogleSans.ttf"
            };
            for (String ruta : rutasCandidatas) {
                java.io.File archivoFuente = new java.io.File(ruta);
                if (archivoFuente.exists()) {
                    Font fuenteBase = Font.createFont(Font.TRUETYPE_FONT, archivoFuente);
                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(fuenteBase);
                    return fuenteBase.deriveFont(tamano);
                }
            }
        } catch (Exception e) {
            // ignorar, se usa la fuente por defecto del sistema
        }
        return new Font("Segoe UI", Font.PLAIN, (int) tamano);
    }
}
