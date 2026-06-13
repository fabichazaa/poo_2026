package vista.dialogos;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import modelo.*;
import recursos.CargadorFuentes;
import recursos.ImageLoader;
import vista.componentes.CardPanel;
import vista.componentes.ModernScrollBarUI;

public class DialogoNuevoTurno extends JDialog {

    private final ControladorVeterinaria controlador;
    private Turno turnoCreado;

    private JComboBox<Animal> comboAnimales;
    private TipoTurno tipoSeleccionado = null;
    private TimeSlotButton slotSeleccionado = null;
    @SuppressWarnings("unused")
    private String prioridadSeleccionada = "Normal";
    private JTextField campoFecha;
    private JTextArea campoNotas;
    private JLabel lblError;
    // Etiquetas del Resumen
    private JLabel lblResumenPacienteVal;
    private JLabel lblResumenTipoVal;
    private JLabel lblResumenFechaVal;
    private JLabel lblResumenHoraVal;

    private final List<TipoTurnoButton> botonesTipo = new ArrayList<>();
    private final List<PrioridadButton> botonesPrioridad = new ArrayList<>();
    private final List<TimeSlotButton> botonesHora = new ArrayList<>();

    public DialogoNuevoTurno(Frame owner, ControladorVeterinaria controlador) {
        super(owner, "Registrar nuevo turno", true);
        this.controlador = controlador;
        this.turnoCreado = null;
        construir();
    }

    public Turno getTurnoCreado() {
        return turnoCreado;
    }

    private void construir() {
        setSize(980, 640);
        setResizable(false);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(241, 245, 249)); // #F1F5F9

        // ----------------- HEADER PANEL -----------------
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.setBorder(new EmptyBorder(16, 24, 8, 24));

        // Header Izquierdo (Botón Volver + Logo + Títulos)
        JPanel panelHeaderIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelHeaderIzq.setOpaque(false);

        // Botón volver circular
        JButton btnVolver = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(226, 232, 240));
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);

                g2.setColor(new Color(30, 41, 59));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                String txt = "<";
                int x = (getWidth() - fm.stringWidth(txt)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(txt, x, y - 1);
                g2.dispose();
            }
        };
        btnVolver.setPreferredSize(new Dimension(36, 36));
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> dispose());
        panelHeaderIzq.add(btnVolver);

        // Logo
        JPanel panelLogo = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                g2.setColor(new Color(13, 148, 136)); // Teal-600
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        panelLogo.setPreferredSize(new Dimension(36, 36));
        panelLogo.setOpaque(false);
        JLabel lblLogoIcon = new JLabel("🐾");
        lblLogoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        ImageIcon icon = ImageLoader.loadScaled("imagenes/logo.png", 36, 36);
        if (icon.getImage() != null && icon.getIconWidth() > 0) {
            lblLogoIcon.setIcon(icon);
            lblLogoIcon.setText("");
        }

        panelLogo.add(lblLogoIcon);
        panelHeaderIzq.add(panelLogo);

        // Títulos
        JPanel panelTitulos = new JPanel();
        panelTitulos.setOpaque(false);
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Nuevo Turno");
        lblTitulo.setFont(CargadorFuentes.cargar(18f).deriveFont(Font.BOLD));
        lblTitulo.setForeground(new Color(30, 41, 59));

        Veterinario vetLogueado = controlador.getVeterinarioLogueado();
        String vetStr = (vetLogueado != null)
                ? "Dr/a. " + vetLogueado.getNombre() + " " + vetLogueado.getApellido() + " · " + vetLogueado.getMatricula()
                : "Veterinario no identificado";
        JLabel lblSubtitle = new JLabel(vetStr);
        lblSubtitle.setFont(CargadorFuentes.cargar(11f));
        lblSubtitle.setForeground(new Color(100, 116, 139));

        panelTitulos.add(lblTitulo);
        panelTitulos.add(lblSubtitle);
        panelHeaderIzq.add(panelTitulos);

        panelHeader.add(panelHeaderIzq, BorderLayout.WEST);

        // Header Derecho (* campos obligatorios)
        JLabel lblObligatorio = new JLabel("* campos obligatorios");
        lblObligatorio.setFont(CargadorFuentes.cargar(11f));
        lblObligatorio.setForeground(new Color(148, 163, 184));
        lblObligatorio.setBorder(new EmptyBorder(10, 0, 0, 0));
        panelHeader.add(lblObligatorio, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

        // ----------------- CENTRAL PANEL (TWO COLUMNS) -----------------
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(new EmptyBorder(8, 24, 16, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // --- COLUMNA IZQUIERDA ---
        JPanel panelColIzq = new JPanel();
        panelColIzq.setOpaque(false);
        panelColIzq.setLayout(new BoxLayout(panelColIzq, BoxLayout.Y_AXIS));
        CardPanel cardPaciente = new CardPanel(recursos.Color.PRIMARY); // Teal
        cardPaciente.setLayout(new BorderLayout(0, 8));

        JLabel lblPacienteTit = new JLabel("PACIENTE *");
        lblPacienteTit.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblPacienteTit.setForeground(recursos.Color.MUTED);
        cardPaciente.add(lblPacienteTit, BorderLayout.NORTH);

        final List<Animal> todosLosAnimalesBase = new ArrayList<>();
        for (Responsable c : controlador.getVeterinaria().getListaClientes()) {
            todosLosAnimalesBase.addAll(c.getMascotas());
        }

        ArrayList<Animal> todos = new ArrayList<>();
        todos.add(null); // placeholder
        todos.addAll(todosLosAnimalesBase);

        JPanel panelPacienteInputs = new JPanel();
        panelPacienteInputs.setOpaque(false);
        panelPacienteInputs.setLayout(new BoxLayout(panelPacienteInputs, BoxLayout.Y_AXIS));

        JTextField txtBuscarPaciente = new JTextField("Buscar por nombre o dueño...");
        txtBuscarPaciente.setFont(CargadorFuentes.cargar(12f));
        txtBuscarPaciente.setForeground(recursos.Color.MUTED);
        txtBuscarPaciente.setPreferredSize(new Dimension(180, 32));
        txtBuscarPaciente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        txtBuscarPaciente.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(recursos.Color.BORDER, 1, true),
                new EmptyBorder(0, 10, 0, 10)
        ));

        txtBuscarPaciente.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtBuscarPaciente.getText().equals("Buscar por nombre o dueño...")) {
                    txtBuscarPaciente.setText("");
                    txtBuscarPaciente.setForeground(recursos.Color.INK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtBuscarPaciente.getText().trim().isEmpty()) {
                    txtBuscarPaciente.setText("Buscar por nombre o dueño...");
                    txtBuscarPaciente.setForeground(recursos.Color.MUTED);
                }
            }
        });

        comboAnimales = new JComboBox<>(todos.toArray(Animal[]::new));
        comboAnimales.setBackground(recursos.Color.CANVAS_GENERAL);
        comboAnimales.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        comboAnimales.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        comboAnimales.setPreferredSize(new Dimension(180, 32));
        comboAnimales.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(recursos.Color.BORDER, 1, true),
                new EmptyBorder(2, 2, 2, 2)
        ));

        // Live Filter
        txtBuscarPaciente.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String query = txtBuscarPaciente.getText().toLowerCase().trim();
                if (query.equals("buscar por nombre o dueño...")) {
                    query = "";
                }

                List<Animal> filtrados = new ArrayList<>();
                filtrados.add(null); // placeholder
                for (Animal a : todosLosAnimalesBase) {
                    boolean coincide = query.isEmpty()
                            || a.getNombre().toLowerCase().contains(query)
                            || (a instanceof Perro && ((Perro) a).getRaza().toLowerCase().contains(query))
                            || (a instanceof Gato && ((Gato) a).getRaza().toLowerCase().contains(query))
                            || a.getResponsable().getNombre().toLowerCase().contains(query)
                            || a.getResponsable().getApellido().toLowerCase().contains(query);
                    if (coincide) {
                        filtrados.add(a);
                    }
                }

                comboAnimales.setModel(new DefaultComboBoxModel<>(filtrados.toArray(Animal[]::new)));
                if (filtrados.size() == 2) {
                    comboAnimales.setSelectedIndex(1);
                } else {
                    comboAnimales.setSelectedIndex(0);
                }
            }
        });

        // Custom Renderer
        comboAnimales.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel cellPanel = new JPanel(new BorderLayout(8, 0));
            cellPanel.setOpaque(true);
            cellPanel.setBorder(new EmptyBorder(6, 12, 6, 12));

            JLabel iconLabel = new JLabel();
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel textLabel = new JLabel();
            textLabel.setFont(CargadorFuentes.cargar(12f));

            if (value == null) {
                textLabel.setText("Seleccionar paciente...");
                textLabel.setForeground(recursos.Color.CAT_INACTIVO); // Slate-400
                ImageIcon patIcon = ImageLoader.loadScaled("imagenes/emojis/patitas.png", 16, 16);
                if (patIcon.getImage() != null && patIcon.getIconWidth() > 0) {
                    iconLabel.setIcon(patIcon);
                } else {
                    iconLabel.setText("🐾");
                    iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                }
            } else {
                String text = value.getNombre() + " (" + value.getEspecie() + ") — "
                        + value.getResponsable().getNombre() + " " + value.getResponsable().getApellido();
                textLabel.setText(text);
                textLabel.setForeground(recursos.Color.INK); // Slate-800

                String imgPath = (value instanceof Perro) ? "imagenes/emojis/perro.png" : "imagenes/emojis/gato.png";
                ImageIcon petIcon = ImageLoader.loadScaled(imgPath, 16, 16);
                if (petIcon.getImage() != null && petIcon.getIconWidth() > 0) {
                    iconLabel.setIcon(petIcon);
                } else {
                    iconLabel.setText(value instanceof Perro ? "🐕" : "🐈");
                    iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                }
            }

            cellPanel.add(iconLabel, BorderLayout.WEST);
            cellPanel.add(textLabel, BorderLayout.CENTER);

            if (isSelected) {
                cellPanel.setBackground(recursos.Color.PRIMARY); // Teal-600
                textLabel.setForeground(recursos.Color.SURFACE);
                iconLabel.setForeground(recursos.Color.SURFACE);
            } else {
                cellPanel.setBackground(recursos.Color.SURFACE);
            }

            return cellPanel;
        });

        panelPacienteInputs.add(txtBuscarPaciente);
        panelPacienteInputs.add(Box.createVerticalStrut(8));
        panelPacienteInputs.add(comboAnimales);

        cardPaciente.add(panelPacienteInputs, BorderLayout.CENTER);
        panelColIzq.add(cardPaciente);

        panelColIzq.add(Box.createVerticalStrut(12));

        // 2. Card Tipo de Turno
        CardPanel cardTipo = new CardPanel(new Color(139, 92, 246)); // Purple
        cardTipo.setLayout(new BorderLayout(0, 8));

        JLabel lblTipoTit = new JLabel("TIPO DE TURNO *");
        lblTipoTit.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblTipoTit.setForeground(new Color(100, 116, 139));
        cardTipo.add(lblTipoTit, BorderLayout.NORTH);

        JPanel panelTipoGrid = new JPanel(new GridLayout(3, 2, 2, 4));
        panelTipoGrid.setOpaque(false);

        // Creamos los 8 botones de tipo
        TipoTurnoButton btnConsulta = new TipoTurnoButton("Consulta", "imagenes/emojis/estetoscopio.png", "🩺", TipoTurno.CONSULTA_GENERAL);
        TipoTurnoButton btnAnalisis = new TipoTurnoButton("Análisis", "imagenes/emojis/laboratorio.png", "🔬", TipoTurno.ANALISIS);
        TipoTurnoButton btnCirugia = new TipoTurnoButton("Cirugía", "imagenes/emojis/salud.png", "⚕️", TipoTurno.CIRUGIA);
        TipoTurnoButton btnSeguimiento = new TipoTurnoButton("Seguimiento", "imagenes/emojis/carpeta.png", "📋", TipoTurno.CONSULTA_GENERAL);
        TipoTurnoButton btnVacunacion = new TipoTurnoButton("Vacunación", "imagenes/emojis/pastilla.png", "💊", TipoTurno.VACUNACION);
        TipoTurnoButton btnEstetica = new TipoTurnoButton("Estética", "imagenes/emojis/manito.png", "✂️", TipoTurno.BANIO);

        botonesTipo.add(btnConsulta);
        botonesTipo.add(btnAnalisis);
        botonesTipo.add(btnCirugia);
        botonesTipo.add(btnSeguimiento);
        botonesTipo.add(btnVacunacion);
        botonesTipo.add(btnEstetica);

        for (TipoTurnoButton b : botonesTipo) {
            panelTipoGrid.add(b);
            b.addActionListener(e -> seleccionarTipo(b));
        }

        cardTipo.add(panelTipoGrid, BorderLayout.CENTER);
        panelColIzq.add(cardTipo);

        panelColIzq.add(Box.createVerticalStrut(12));

        // 3. Card Prioridad
        CardPanel cardPrioridad = new CardPanel(new Color(249, 115, 22), orangeGlow());
        cardPrioridad.setLayout(new BorderLayout(0, 8));

        JLabel lblPrioridadTit = new JLabel("PRIORIDAD");
        lblPrioridadTit.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblPrioridadTit.setForeground(new Color(100, 116, 139));
        cardPrioridad.add(lblPrioridadTit, BorderLayout.NORTH);

        JPanel panelPills = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelPills.setOpaque(false);

        PrioridadButton pillNormal = new PrioridadButton("Normal", new Color(100, 116, 139));
        PrioridadButton pillMedia = new PrioridadButton("Media", new Color(234, 179, 8));
        PrioridadButton pillAlta = new PrioridadButton("Alta", new Color(239, 68, 68));

        botonesPrioridad.add(pillNormal);
        botonesPrioridad.add(pillMedia);
        botonesPrioridad.add(pillAlta);

        pillNormal.setSeleccionado(true);

        for (PrioridadButton p : botonesPrioridad) {
            panelPills.add(p);
            p.addActionListener(e -> seleccionarPrioridad(p));
        }

        cardPrioridad.add(panelPills, BorderLayout.CENTER);
        panelColIzq.add(cardPrioridad);

        gbc.gridx = 0;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 8);
        panelCentral.add(panelColIzq, gbc);

        // --- COLUMNA DERECHA ---
        JPanel panelColDer = new JPanel();
        panelColDer.setOpaque(false);
        panelColDer.setLayout(new BoxLayout(panelColDer, BoxLayout.Y_AXIS));

        // 1. Card Fecha y Hora
        CardPanel cardFechaHora = new CardPanel(new Color(59, 130, 246)); // Blue
        cardFechaHora.setLayout(new GridBagLayout());

        GridBagConstraints gbcFH = new GridBagConstraints();
        gbcFH.fill = GridBagConstraints.BOTH;
        gbcFH.weighty = 1.0;
        gbcFH.insets = new Insets(0, 4, 0, 4);

        // Subcolumna Fecha
        JPanel panelSubFecha = new JPanel();
        panelSubFecha.setOpaque(false);
        panelSubFecha.setLayout(new BoxLayout(panelSubFecha, BoxLayout.Y_AXIS));

        JLabel lblFechaTit = new JLabel("FECHA *");
        lblFechaTit.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblFechaTit.setForeground(new Color(100, 116, 139));
        lblFechaTit.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSubFecha.add(lblFechaTit);
        panelSubFecha.add(Box.createVerticalStrut(6));

        JPanel panelFechaWrapper = new JPanel(new BorderLayout(8, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(248, 250, 252));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(new Color(226, 232, 240));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
            }
        };
        panelFechaWrapper.setOpaque(false);
        panelFechaWrapper.setBorder(new EmptyBorder(8, 12, 8, 12));
        panelFechaWrapper.setMaximumSize(new Dimension(120, 38));
        panelFechaWrapper.setPreferredSize(new Dimension(120, 38));
        panelFechaWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        campoFecha = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        campoFecha.setBorder(null);
        campoFecha.setOpaque(false);
        campoFecha.setFont(CargadorFuentes.cargar(12f));
        campoFecha.setForeground(new Color(30, 41, 59));
        panelFechaWrapper.add(campoFecha, BorderLayout.CENTER);

        JLabel lblCalIcon = new JLabel("📅");
        ImageIcon calIcon = ImageLoader.loadScaled("imagenes/emojis/calendario.png", 16, 16);
        if (calIcon.getImage() != null && calIcon.getIconWidth() > 0) {
            lblCalIcon.setIcon(calIcon);
            lblCalIcon.setText("");
        }

        panelFechaWrapper.add(lblCalIcon, BorderLayout.EAST);
        panelSubFecha.add(panelFechaWrapper);
        panelSubFecha.add(Box.createVerticalStrut(40)); // spacing

        gbcFH.gridx = 0;
        gbcFH.weightx = 0.4;
        cardFechaHora.add(panelSubFecha, gbcFH);

        // Subcolumna Hora
        JPanel panelSubHora = new JPanel();
        panelSubHora.setOpaque(false);
        panelSubHora.setLayout(new BoxLayout(panelSubHora, BoxLayout.Y_AXIS));

        JLabel lblHoraTit = new JLabel("HORA *");
        lblHoraTit.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblHoraTit.setForeground(new Color(100, 116, 139));
        lblHoraTit.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSubHora.add(lblHoraTit);
        panelSubHora.add(Box.createVerticalStrut(6));

        JPanel panelHoraGrid = new JPanel(new GridLayout(0, 4, 8, 8));
        panelHoraGrid.setBackground(Color.WHITE);

        String[][] slots = {
            {"08:00 AM", "08:00"}, {"08:30 AM", "08:30"}, {"09:00 AM", "09:00"},
            {"09:30 AM", "09:30"}, {"10:00 AM", "10:00"}, {"10:30 AM", "10:30"},
            {"11:00 AM", "11:00"}, {"11:30 AM", "11:30"}, {"12:00 PM", "12:00"},
            {"12:30 PM", "12:30"}, {"02:00 PM", "14:00"}, {"02:30 PM", "14:30"},
            {"03:00 PM", "15:00"}, {"03:30 PM", "15:30"}, {"04:00 PM", "16:00"},
            {"04:30 PM", "16:30"}, {"05:00 PM", "17:00"}, {"05:30 PM", "17:30"}
        };

        for (String[] pair : slots) {
            TimeSlotButton tButton = new TimeSlotButton(pair[0], pair[1]);
            botonesHora.add(tButton);
            panelHoraGrid.add(tButton);
            tButton.addActionListener(e -> seleccionarHora(tButton));
        }

        JScrollPane scrollHora = new JScrollPane(panelHoraGrid);
        scrollHora.setBorder(null);
        scrollHora.setOpaque(false);
        scrollHora.getViewport().setOpaque(false);
        scrollHora.setPreferredSize(new Dimension(380, 160));
        scrollHora.setMaximumSize(new Dimension(380, 160));
        scrollHora.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Aplicamos el scrollbar personalizado
        JScrollBar barVertical = scrollHora.getVerticalScrollBar();
        barVertical.setUI(new ModernScrollBarUI());
        barVertical.setPreferredSize(new Dimension(8, 0));
        barVertical.setUnitIncrement(14);

        panelSubHora.add(scrollHora);

        gbcFH.gridx = 1;
        gbcFH.weightx = 0.6;
        cardFechaHora.add(panelSubHora, gbcFH);

        panelColDer.add(cardFechaHora);

        panelColDer.add(Box.createVerticalStrut(12));

        // 2. Card Notas / Motivo
        CardPanel cardNotas = new CardPanel(new Color(148, 163, 184)); // Slate-400
        cardNotas.setLayout(new BorderLayout(0, 8));

        JLabel lblNotasTit = new JLabel("NOTAS / MOTIVO DE CONSULTA");
        lblNotasTit.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblNotasTit.setForeground(new Color(100, 116, 139));
        cardNotas.add(lblNotasTit, BorderLayout.NORTH);

        campoNotas = new JTextArea(3, 20);
        campoNotas.setFont(CargadorFuentes.cargar(12f));
        campoNotas.setForeground(new Color(30, 41, 59));
        campoNotas.setLineWrap(true);
        campoNotas.setWrapStyleWord(true);

        // Custom padding & border for JTextArea
        campoNotas.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        // Character counter
        JLabel lblCounter = new JLabel("0 / 300");
        lblCounter.setFont(CargadorFuentes.cargar(10f));
        lblCounter.setForeground(new Color(148, 163, 184));
        lblCounter.setHorizontalAlignment(SwingConstants.RIGHT);

        campoNotas.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            private void update() {
                int len = campoNotas.getText().length();
                lblCounter.setText(len + " / 300");
                if (len > 300) {
                    lblCounter.setForeground(new Color(220, 38, 38));
                } else {
                    lblCounter.setForeground(new Color(148, 163, 184));
                }
            }
        });

        cardNotas.add(campoNotas, BorderLayout.CENTER);
        cardNotas.add(lblCounter, BorderLayout.SOUTH);
        panelColDer.add(cardNotas);

        panelColDer.add(Box.createVerticalStrut(12));

        // // 3. Card Recordatorio Automático
        // JPanel panelRecWrapper = new JPanel(new BorderLayout()) {
        //     @Override
        //     protected void paintComponent(Graphics g) {
        //         Graphics2D g2 = (Graphics2D) g.create();
        //         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //         g2.setColor(Color.WHITE);
        //         g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        //         g2.setColor(new Color(226, 232, 240));
        //         g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
        //         g2.dispose();
        //     }
        // };
        // panelRecWrapper.setOpaque(false);
        // panelRecWrapper.setBorder(new EmptyBorder(12, 16, 12, 16));
        // panelRecWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        // JPanel panelRecIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        // panelRecIzq.setOpaque(false);
        // // Bell icon panel
        // JPanel panelBell = new JPanel(new GridBagLayout()) {
        //     @Override
        //     protected void paintComponent(Graphics g) {
        //         Graphics2D g2 = (Graphics2D) g.create();
        //         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //         g2.setColor(new Color(254, 243, 199)); // Amber-100
        //         g2.fillOval(0, 0, getWidth(), getHeight());
        //         g2.dispose();
        //     }
        // };
        // panelBell.setPreferredSize(new Dimension(36, 36));
        // panelBell.setOpaque(false);
        // JLabel lblBell = new JLabel("🔔");
        // lblBell.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        // panelBell.add(lblBell);
        // panelRecIzq.add(panelBell);
        // JPanel panelRecText = new JPanel();
        // panelRecText.setOpaque(false);
        // panelRecText.setLayout(new BoxLayout(panelRecText, BoxLayout.Y_AXIS));
        // JLabel lblRecTit = new JLabel("Recordatorio automático");
        // lblRecTit.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        // lblRecTit.setForeground(new Color(30, 41, 59));
        // JLabel lblRecSub = new JLabel("Notificar al dueño 24 hs antes del turno");
        // lblRecSub.setFont(CargadorFuentes.cargar(10f));
        // lblRecSub.setForeground(new Color(148, 163, 184));
        // panelRecText.add(lblRecTit);
        // panelRecText.add(lblRecSub);
        // panelRecIzq.add(panelRecText);
        // panelRecWrapper.add(panelRecIzq, BorderLayout.WEST);
        // switchRecordatorio = new SwitchButton();
        // JPanel panelSwitchWrapper = new JPanel(new GridBagLayout());
        // panelSwitchWrapper.setOpaque(false);
        // panelSwitchWrapper.add(switchRecordatorio);
        // panelRecWrapper.add(panelSwitchWrapper, BorderLayout.EAST);
        // panelColDer.add(panelRecWrapper);
        // panelColDer.add(Box.createVerticalStrut(12));
        // 4. Card Resumen del Turno
        final JPanel panelResumen = new JPanel(new BorderLayout(15, 0)) {
            @Override
            protected void paintComponent(final Graphics g) {
                final Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(226, 232, 240));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        panelResumen.setOpaque(false);
        panelResumen.setBorder(new EmptyBorder(12, 16, 12, 16));
        panelResumen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        final JPanel panelResumenData = new JPanel();
        panelResumenData.setOpaque(false);
        panelResumenData.setLayout(new BoxLayout(panelResumenData, BoxLayout.Y_AXIS));

        final JLabel lblResumenTit = new JLabel("RESUMEN DEL TURNO");
        lblResumenTit.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblResumenTit.setForeground(new Color(148, 163, 184));
        panelResumenData.add(lblResumenTit);
        panelResumenData.add(Box.createVerticalStrut(6));

        // Rows mapping
        panelResumenData.add(crearFilaResumen("Paciente", lblResumenPacienteVal = new JLabel("Sin seleccionar")));
        panelResumenData.add(crearFilaResumen("Tipo", lblResumenTipoVal = new JLabel("Sin seleccionar")));
        lblResumenFechaVal = new JLabel(campoFecha.getText());
        lblResumenFechaVal.setForeground(new Color(30, 41, 59));
        panelResumenData.add(crearFilaResumen("Fecha", lblResumenFechaVal));
        panelResumenData.add(crearFilaResumen("Hora", lblResumenHoraVal = new JLabel("Sin seleccionar")));

        panelResumen.add(panelResumenData, BorderLayout.CENTER);

        // CTA button
        final JButton btnRegistrar = new JButton("Registrar turno") {
            @Override
            protected void paintComponent(final Graphics g) {
                final Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(13, 148, 136)); // Teal-600
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnRegistrar.setFont(CargadorFuentes.cargar(14f).deriveFont(Font.BOLD));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setContentAreaFilled(false);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(180, 36));
        btnRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(e -> intentarGuardar());

        final JPanel panelRegistrarWrapper = new JPanel(new GridBagLayout());
        panelRegistrarWrapper.setOpaque(false);
        panelRegistrarWrapper.add(btnRegistrar);

        panelResumen.add(panelRegistrarWrapper, BorderLayout.EAST);

        panelColDer.add(panelResumen);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 8, 0, 0);
        panelCentral.add(panelColDer, gbc);

        add(panelCentral, BorderLayout.CENTER);

        // Error message row at the very bottom
        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setOpaque(false);
        panelFooter.setBorder(new EmptyBorder(0, 24, 8, 24));
        lblError = new JLabel(" ");
        lblError.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblError.setForeground(new Color(220, 38, 38));
        panelFooter.add(lblError, BorderLayout.WEST);
        add(panelFooter, BorderLayout.SOUTH);

        // Wire up Listeners
        comboAnimales.addActionListener(e -> {
            Animal selected = (Animal) comboAnimales.getSelectedItem();
            if (selected == null) {
                lblResumenPacienteVal.setText("Sin seleccionar");
                lblResumenPacienteVal.setForeground(new Color(148, 163, 184));
            } else {
                lblResumenPacienteVal.setText(selected.getNombre());
                lblResumenPacienteVal.setForeground(new Color(30, 41, 59));
            }
        });

        campoFecha.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            private void update() {
                String txt = campoFecha.getText().trim();
                if (txt.isEmpty()) {
                    lblResumenFechaVal.setText("Sin seleccionar");
                    lblResumenFechaVal.setForeground(new Color(148, 163, 184));
                } else {
                    lblResumenFechaVal.setText(txt);
                    lblResumenFechaVal.setForeground(new Color(30, 41, 59));
                }
            }
        });

        // Set default selected time slot if any (e.g. 10:00 AM)
        for (TimeSlotButton b : botonesHora) {
            if (b.getModelTime().equals("10:00")) {
                seleccionarHora(b);
                break;
            }
        }
    }

    private Color orangeGlow() {
        return new Color(236, 72, 153);
    }

    private JPanel crearFilaResumen(String label, JLabel lblValue) {
        JPanel row = new JPanel(new BorderLayout(16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));

        JLabel lblName = new JLabel(label);
        lblName.setFont(CargadorFuentes.cargar(11f));
        lblName.setForeground(new Color(148, 163, 184));
        row.add(lblName, BorderLayout.WEST);

        lblValue.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblValue.setForeground(new Color(148, 163, 184)); // default is gray
        row.add(lblValue, BorderLayout.EAST);

        return row;
    }

    private void seleccionarTipo(TipoTurnoButton selected) {
        for (TipoTurnoButton b : botonesTipo) {
            b.setSeleccionado(b == selected);
        }
        tipoSeleccionado = selected.getTipo();
        lblResumenTipoVal.setText(selected.getTexto());
        lblResumenTipoVal.setForeground(new Color(30, 41, 59));
    }

    private void seleccionarPrioridad(PrioridadButton selected) {
        for (PrioridadButton p : botonesPrioridad) {
            p.setSeleccionado(p == selected);
        }
        prioridadSeleccionada = selected.getTexto();
    }

    private void seleccionarHora(TimeSlotButton selected) {
        for (TimeSlotButton b : botonesHora) {
            b.setSeleccionado(b == selected);
        }
        slotSeleccionado = selected;
        lblResumenHoraVal.setText(selected.getTimeText());
        lblResumenHoraVal.setForeground(new Color(30, 41, 59));
    }

    private void intentarGuardar() {
        String fechaTxt = campoFecha.getText().trim();
        String horaTxt = slotSeleccionado != null ? slotSeleccionado.getModelTime() : "";

        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(fechaTxt, fmt);
        } catch (DateTimeParseException ex) {
            lblError.setText("Formato de fecha inválido. Usá dd/MM/yyyy.");
            return;
        }

        if (horaTxt.isEmpty()) {
            lblError.setText("Seleccioná una hora.");
            return;
        }

        Veterinario vet = controlador.getVeterinarioLogueado();
        Animal animal = (Animal) comboAnimales.getSelectedItem();

        if (vet == null || animal == null) {
            lblError.setText("Seleccioná un paciente.");
            return;
        }

        if (tipoSeleccionado == null) {
            lblError.setText("Seleccioná un tipo de turno.");
            return;
        }

        // Character count validation
        String observaciones = campoNotas.getText().trim();
        if (observaciones.length() > 300) {
            lblError.setText("Las notas no pueden superar los 300 caracteres.");
            return;
        }

        turnoCreado = controlador.registrarTurno(fechaTxt, horaTxt, vet, animal, tipoSeleccionado, observaciones);
        if (turnoCreado != null) {
            if (!observaciones.isEmpty()) {
                turnoCreado.setObservaciones(observaciones);
            }
            JOptionPane.showMessageDialog(this, "Turno registrado con éxito.");
            dispose();
        } else {
            lblError.setText("Error al registrar el turno.");
        }
    }

    // Helper TipoTurnoButton class
    private static class TipoTurnoButton extends JButton {

        private final String texto;
        private final TipoTurno tipo;
        private boolean seleccionado = false;
        private final Color colorNormalBg = new Color(248, 250, 252);
        private final Color colorNormalBorder = new Color(226, 232, 240);
        private final Color colorSelectedBg = new Color(240, 253, 250);
        private final Color colorSelectedBorder = new Color(13, 148, 136);

        public TipoTurnoButton(String texto, String iconPath, String unicodeIcon, TipoTurno tipo) {
            this.texto = texto;
            this.tipo = tipo;

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(120, 48));
            setLayout(new BorderLayout(10, 20));

            JLabel lblIcon = new JLabel();
            lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
            ImageIcon icon = null;
            if (iconPath != null) {
                icon = ImageLoader.loadScaled(iconPath, 16, 16);
            }
            if (icon != null && icon.getImage() != null && icon.getIconWidth() > 0) {
                lblIcon.setIcon(icon);
            } else {
                lblIcon.setText(unicodeIcon);
                lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
            }

            JLabel lblText = new JLabel(texto);
            lblText.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
            lblText.setForeground(new Color(71, 85, 105)); // Slate-600

            JPanel innerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 16));
            innerPanel.setOpaque(false);
            innerPanel.add(lblIcon);
            innerPanel.add(lblText);

            add(innerPanel, BorderLayout.CENTER);

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (!seleccionado) {
                        setBackground(new Color(241, 245, 249));
                        repaint();
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (!seleccionado) {
                        setBackground(colorNormalBg);
                        repaint();
                    }
                }
            });
        }

        public String getTexto() {
            return texto;
        }

        public TipoTurno getTipo() {
            return tipo;
        }

        public void setSeleccionado(boolean s) {
            this.seleccionado = s;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            Color bg = seleccionado ? colorSelectedBg : getBackground();
            if (bg == null) {
                bg = colorNormalBg;
            }
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            Color border = seleccionado ? colorSelectedBorder : colorNormalBorder;
            g2.setColor(border);
            g2.setStroke(new BasicStroke(seleccionado ? 1.5f : 1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Helper PrioridadButton class
    private static class PrioridadButton extends JButton {

        private final String texto;
        private final Color dotColor;
        private boolean seleccionado = false;

        public PrioridadButton(String texto, Color dotColor) {
            this.texto = texto;
            this.dotColor = dotColor;

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(85, 28));

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (!seleccionado) {
                        repaint();
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (!seleccionado) {
                        repaint();
                    }
                }
            });
        }

        public String getTexto() {
            return texto;
        }

        public void setSeleccionado(boolean s) {
            this.seleccionado = s;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg = seleccionado ? new Color(241, 245, 249) : Color.WHITE;
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

            Color border = seleccionado ? new Color(30, 41, 59) : new Color(226, 232, 240);
            g2.setColor(border);
            g2.setStroke(new BasicStroke(seleccionado ? 1.5f : 1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());

            // Draw dot
            g2.setColor(dotColor);
            g2.fillOval(12, getHeight() / 2 - 4, 8, 8);

            // Draw text
            g2.setColor(new Color(71, 85, 105));
            g2.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
            FontMetrics fm = g2.getFontMetrics();
            int textX = 26;
            int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(texto, textX, textY);

            g2.dispose();
        }
    }

    // Helper TimeSlotButton class
    private static class TimeSlotButton extends JButton {

        private final String timeText;
        private final String modelTime;
        private boolean seleccionado = false;

        public TimeSlotButton(String timeText, String modelTime) {
            this.timeText = timeText;
            this.modelTime = modelTime;

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(85, 30));

            setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
            setForeground(new Color(71, 85, 105));
        }

        public void setSeleccionado(boolean s) {
            this.seleccionado = s;
            setForeground(seleccionado ? Color.WHITE : new Color(71, 85, 105));
            repaint();
        }

        public String getModelTime() {
            return modelTime;
        }

        public String getTimeText() {
            return timeText;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg = seleccionado ? new Color(2, 132, 199) : new Color(248, 250, 252);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

            Color border = seleccionado ? new Color(2, 132, 199) : new Color(226, 232, 240);
            g2.setColor(border);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

            FontMetrics fm = g2.getFontMetrics(getFont());
            int textX = (getWidth() - fm.stringWidth(timeText)) / 2;
            int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(getForeground());
            g2.drawString(timeText, textX, textY);

            g2.dispose();
        }
    }
}
