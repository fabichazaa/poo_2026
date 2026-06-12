package vista.dialogos;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import modelo.*;
import recursos.CargadorFuentes;

public class DialogoAtenderTurno extends JDialog {

    private final ControladorVeterinaria controlador;
    private final Turno turno;
    private final Animal animal;

    private Point initialClick;
    private int timerSeconds = 38 * 60 + 14; // Inicia en 38:14 como en la imagen
    private Timer swingTimer;

    private JLabel lblHeaderTimer;
    private JLabel lblTurnoDuration;

    private JTextField txtProximoControl;
    private JTextField txtFiltrarAplicados;
    private JPanel panelMedicamentos;
    private JTextArea areaObservaciones;
    private JLabel lblCharCount;

    public DialogoAtenderTurno(Frame owner, ControladorVeterinaria controlador, Turno turno) {
        super(owner, "Atender turno — " + turno.getAnimal().getNombre(), true);
        this.controlador = controlador;
        this.turno = turno;
        this.animal = turno.getAnimal();
        construir();
        iniciarTimer();
    }

    private void iniciarTimer() {
        swingTimer = new Timer(1000, e -> {
            timerSeconds++;
            String formatted = formatTime(timerSeconds);
            if (lblHeaderTimer != null) {
                lblHeaderTimer.setText(formatted);
            }
            if (lblTurnoDuration != null) {
                lblTurnoDuration.setText(formatted);
            }
        });
        swingTimer.start();
    }

    private String formatTime(int totalSeconds) {
        int m = totalSeconds / 60;
        int s = totalSeconds % 60;
        return String.format("%02d:%02d", m, s);
    }

    @Override
    public void dispose() {
        if (swingTimer != null) {
            swingTimer.stop();
        }
        super.dispose();
    }

    private void construir() {
        setSize(580, 780);
        setUndecorated(false);
        setResizable(false);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        // Panel principal de fondo con esquinas redondeadas simuladas
        JPanel panelFondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(recursos.Color.BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
            }
        };
        panelFondo.setOpaque(false);

        // ==========================================
        //  HEADER BANNER (Teal)
        // ==========================================
        JPanel headerPanel = new JPanel(new BorderLayout(12, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(recursos.Color.PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 20, 24, 24);
                g2.dispose();
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(16, 24, 16, 24));

        JPanel headerTextPanel = new JPanel();
        headerTextPanel.setOpaque(false);
        headerTextPanel.setLayout(new BoxLayout(headerTextPanel, BoxLayout.Y_AXIS));

        JLabel lblHeaderTitle = new JLabel("Finalizar Turno");
        lblHeaderTitle.setFont(CargadorFuentes.cargar(18f).deriveFont(Font.BOLD));
        lblHeaderTitle.setForeground(Color.WHITE);

        JLabel lblHeaderSub = new JLabel("Complete el registro médico para cerrar la atención.");
        lblHeaderSub.setFont(CargadorFuentes.cargar(12f));
        lblHeaderSub.setForeground(new Color(226, 240, 238));

        headerTextPanel.add(lblHeaderTitle);
        headerTextPanel.add(Box.createVerticalStrut(2));
        headerTextPanel.add(lblHeaderSub);

        // Timer de cabecera
        JPanel headerRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        headerRightPanel.setOpaque(false);

        lblHeaderTimer = new JLabel("⏱ " + formatTime(timerSeconds));
        lblHeaderTimer.setFont(CargadorFuentes.cargar(13f).deriveFont(Font.BOLD));
        lblHeaderTimer.setForeground(Color.WHITE);
        lblHeaderTimer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 255, 255, 80), 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));

        headerRightPanel.add(lblHeaderTimer);

        headerPanel.add(headerTextPanel, BorderLayout.CENTER);
        headerPanel.add(headerRightPanel, BorderLayout.EAST);
        panelFondo.add(headerPanel, BorderLayout.NORTH);

        // ==========================================
        //  SCROLLABLE BODY (Cards in Single Column)
        // ==========================================
        JPanel scrollBodyPanel = new JPanel();
        scrollBodyPanel.setOpaque(false);
        scrollBodyPanel.setLayout(new BoxLayout(scrollBodyPanel, BoxLayout.Y_AXIS));
        scrollBodyPanel.setBorder(new EmptyBorder(16, 24, 16, 24));

        // -- Card 1: Paciente --
        CardPanel cardPaciente = new CardPanel(recursos.Color.PRIMARY);
        cardPaciente.setLayout(new BorderLayout(16, 0));
        cardPaciente.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Avatar circular
        JPanel panelAvatar = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(animal instanceof Perro ? recursos.Color.AVATAR_DOG : recursos.Color.AVATAR_CAT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panelAvatar.setPreferredSize(new Dimension(56, 56));
        panelAvatar.setMinimumSize(new Dimension(56, 56));
        panelAvatar.setMaximumSize(new Dimension(56, 56));

        ImageIcon iconPerro = new ImageIcon("imagenes/emojis/perro.png");
        Image iconPerroResized = iconPerro.getImage().getScaledInstance(56, 56, Image.SCALE_SMOOTH);
        ImageIcon iconGato = new ImageIcon("imagenes/emojis/gato.png");
        Image iconGatoResized = iconGato.getImage().getScaledInstance(56, 56, Image.SCALE_SMOOTH);

        JLabel lblAvatarEmoji = new JLabel(animal instanceof Perro ? new ImageIcon(iconPerroResized) : new ImageIcon(iconGatoResized));
        lblAvatarEmoji.setHorizontalAlignment(SwingConstants.CENTER);
        lblAvatarEmoji.setBounds(0, 0, 56, 56);
        panelAvatar.add(lblAvatarEmoji);

        // Info paciente
        JPanel panelInfoPac = new JPanel();
        panelInfoPac.setOpaque(false);
        panelInfoPac.setLayout(new BoxLayout(panelInfoPac, BoxLayout.Y_AXIS));

        JPanel panelNombreTag = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelNombreTag.setOpaque(false);
        panelNombreTag.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNombrePac = new JLabel(animal.getNombre());
        lblNombrePac.setFont(CargadorFuentes.cargar(16f).deriveFont(Font.BOLD));
        lblNombrePac.setForeground(recursos.Color.INK);

        panelNombreTag.add(lblNombrePac);

        String raza = "Mixto";
        if (animal instanceof Perro) {
            raza = ((Perro) animal).getRaza();
        } else if (animal instanceof Gato) {
            raza = ((Gato) animal).getRaza();
        }
        JLabel lblRazaEdad = new JLabel(raza + " · " + animal.calcularEdad() + " años · 28 kg");
        lblRazaEdad.setFont(CargadorFuentes.cargar(12f));
        lblRazaEdad.setForeground(recursos.Color.MUTED);
        lblRazaEdad.setBorder(new EmptyBorder(4, 0, 0, 0));
        lblRazaEdad.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblConstantes = new JLabel("• Constantes normales");
        lblConstantes.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblConstantes.setForeground(recursos.Color.SUCCESS);
        lblConstantes.setBorder(new EmptyBorder(2, 0, 0, 0));
        lblConstantes.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelInfoPac.add(panelNombreTag);
        panelInfoPac.add(lblRazaEdad);
        panelInfoPac.add(lblConstantes);

        cardPaciente.add(panelAvatar, BorderLayout.WEST);
        cardPaciente.add(panelInfoPac, BorderLayout.CENTER);

        // -- Card 2: Turno --
        Color catColor = colorCategoria(turno.getTipo());
        CardPanel cardTurno = new CardPanel(catColor);
        cardTurno.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 6, 3, 6);

        // Usar estetoscopio PNG local para el título
        ImageIcon iconEstetoscopio = new ImageIcon("imagenes/emojis/estetoscopio.png");
        Image imgEstetoscopio = iconEstetoscopio.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        JLabel lblTurnoTitulo = new JLabel(turno.getTipo().getDescripcion().toUpperCase(), new ImageIcon(imgEstetoscopio), SwingConstants.LEFT);
        lblTurnoTitulo.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        lblTurnoTitulo.setForeground(catColor);
        lblTurnoTitulo.setIconTextGap(6);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        cardTurno.add(lblTurnoTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.weightx = 0.5;

        JLabel lblF1 = new JLabel("Fecha");
        lblF1.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblF1.setForeground(recursos.Color.MUTED);
        JLabel lblV1 = new JLabel(turno.getFecha());
        lblV1.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblV1.setForeground(recursos.Color.INK);

        gbc.gridx = 0;
        gbc.gridy = 1;
        cardTurno.add(lblF1, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        cardTurno.add(lblV1, gbc);

        JLabel lblF2 = new JLabel("Hora");
        lblF2.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblF2.setForeground(recursos.Color.MUTED);
        JLabel lblV2 = new JLabel(turno.getHora());
        lblV2.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblV2.setForeground(recursos.Color.INK);

        gbc.gridx = 0;
        gbc.gridy = 2;
        cardTurno.add(lblF2, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        cardTurno.add(lblV2, gbc);

        JLabel lblF3 = new JLabel("Duración");
        lblF3.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblF3.setForeground(recursos.Color.MUTED);
        lblTurnoDuration = new JLabel(formatTime(timerSeconds));
        lblTurnoDuration.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblTurnoDuration.setForeground(recursos.Color.PRIMARY);

        gbc.gridx = 0;
        gbc.gridy = 3;
        cardTurno.add(lblF3, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        cardTurno.add(lblTurnoDuration, gbc);

        // -- Card 3: Próximo Control --
        CardPanel cardControl = new CardPanel(recursos.Color.CAT_SEGUIMIENTO);
        cardControl.setLayout(new BoxLayout(cardControl, BoxLayout.Y_AXIS));

        // Usar calendario PNG local para el título
        ImageIcon iconCal = new ImageIcon("imagenes/emojis/calendario.png");
        Image imgCal = iconCal.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        JLabel lblControlTitle = new JLabel("PRÓXIMO CONTROL", new ImageIcon(imgCal), SwingConstants.LEFT);
        lblControlTitle.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        lblControlTitle.setForeground(recursos.Color.CAT_SEGUIMIENTO);
        lblControlTitle.setIconTextGap(6);
        lblControlTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardControl.add(lblControlTitle);
        cardControl.add(Box.createVerticalStrut(4));

        JPanel pControlWrapper = new JPanel(new BorderLayout(8, 0)) {
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
        pControlWrapper.setOpaque(false);
        pControlWrapper.setBorder(new EmptyBorder(8, 12, 8, 12));
        pControlWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        pControlWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtProximoControl = new JTextField("dd/mm/aaaa");
        txtProximoControl.setBorder(null);
        txtProximoControl.setOpaque(false);
        txtProximoControl.setFont(CargadorFuentes.cargar(12f));
        txtProximoControl.setForeground(recursos.Color.MUTED);
        txtProximoControl.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtProximoControl.getText().equals("dd/mm/aaaa")) {
                    txtProximoControl.setText("");
                    txtProximoControl.setForeground(recursos.Color.INK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtProximoControl.getText().isEmpty()) {
                    txtProximoControl.setText("dd/mm/aaaa");
                    txtProximoControl.setForeground(recursos.Color.MUTED);
                }
            }
        });

        JLabel lblCalIcon2 = new JLabel("📅");
        lblCalIcon2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));

        pControlWrapper.add(txtProximoControl, BorderLayout.CENTER);
        pControlWrapper.add(lblCalIcon2, BorderLayout.EAST);

        cardControl.add(pControlWrapper);
        cardControl.add(Box.createVerticalStrut(4));

        JLabel lblControlSub = new JLabel("<html>Opcional — agenda automáticamente el próximo turno.</html>");
        lblControlSub.setFont(CargadorFuentes.cargar(10f));
        lblControlSub.setForeground(recursos.Color.MUTED);
        lblControlSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardControl.add(lblControlSub);

        // ====================================================================================
        //  GRID ROW: Turno y Próximo Control en una sola fila
        // ====================================================================================
        JPanel panelTurnoControlRow = new JPanel(new GridLayout(1, 2, 14, 0));
        panelTurnoControlRow.setOpaque(false);
        panelTurnoControlRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTurnoControlRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        panelTurnoControlRow.add(cardTurno);
        panelTurnoControlRow.add(cardControl);

        // -- Card 4: Acciones Clínicas (Borde superior en Azul) --
        CardPanel cardAcciones = new CardPanel(recursos.Color.ACCENT_BLUE);
        cardAcciones.setLayout(new BoxLayout(cardAcciones, BoxLayout.Y_AXIS));
        cardAcciones.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Icono estetoscopio PNG local para el título
        JLabel lblAccionesTitle = new JLabel("Acciones Clínicas", new ImageIcon(imgEstetoscopio), SwingConstants.LEFT);
        lblAccionesTitle.setFont(CargadorFuentes.cargar(13f).deriveFont(Font.BOLD));
        lblAccionesTitle.setForeground(recursos.Color.ACCENT_BLUE);
        lblAccionesTitle.setIconTextGap(6);
        lblAccionesTitle.setBorder(new EmptyBorder(8, 12, 0, 0));
        lblAccionesTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardAcciones.add(lblAccionesTitle);
        cardAcciones.add(Box.createVerticalStrut(12));

        // 2 Columnas de botones premium
        JPanel panelBotonesAccion = new JPanel(new GridLayout(1, 2, 16, 0));
        panelBotonesAccion.setOpaque(false);
        panelBotonesAccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        panelBotonesAccion.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnRecetar = crearBotonAccionClinica("💊", "Recetar Medicamento");
        btnRecetar.addActionListener(e -> abrirRecetar());
        JButton btnVacunar = crearBotonAccionClinica("💉", "Registrar Vacunación");
        btnVacunar.addActionListener(e -> abrirVacuna());

        panelBotonesAccion.add(btnRecetar);
        panelBotonesAccion.add(btnVacunar);
        cardAcciones.add(panelBotonesAccion);
        cardAcciones.add(Box.createVerticalStrut(16));

        // Buscador de aplicados (para filtrar el historial de la sesión)
        txtFiltrarAplicados = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(148, 163, 184));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    Insets insets = getInsets();
                    g2.drawString("🔍 Filtrar aplicados en esta sesión...", insets.left, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
                    g2.dispose();
                }
            }
        };
        txtFiltrarAplicados.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        txtFiltrarAplicados.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        txtFiltrarAplicados.setFont(CargadorFuentes.cargar(11f));
        txtFiltrarAplicados.setForeground(recursos.Color.INK);
        txtFiltrarAplicados.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtFiltrarAplicados.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                refrescarMedicamentos();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                refrescarMedicamentos();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                refrescarMedicamentos();
            }
        });
        cardAcciones.add(txtFiltrarAplicados);
        cardAcciones.add(Box.createVerticalStrut(10));

        // Listado de medicamentos/vacunas agregados
        panelMedicamentos = new JPanel();
        panelMedicamentos.setOpaque(false);
        panelMedicamentos.setLayout(new BoxLayout(panelMedicamentos, BoxLayout.Y_AXIS));
        panelMedicamentos.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardAcciones.add(panelMedicamentos);

        // -- Card 5: Observaciones Médicas --
        CardPanel cardObs = new CardPanel(recursos.Color.SUCCESS);
        cardObs.setLayout(new BoxLayout(cardObs, BoxLayout.Y_AXIS));
        cardObs.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panelObsHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelObsHeader.setOpaque(false);
        panelObsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblObsEmoji = new JLabel("📝");
        lblObsEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        JLabel lblObsTitle = new JLabel("Observaciones Médicas");
        lblObsTitle.setFont(CargadorFuentes.cargar(13f).deriveFont(Font.BOLD));
        lblObsTitle.setForeground(recursos.Color.SUCCESS);

        panelObsHeader.add(lblObsEmoji);
        panelObsHeader.add(lblObsTitle);
        cardObs.add(panelObsHeader);
        cardObs.add(Box.createVerticalStrut(10));

        // Pills/Tags de autocompletado rápido
        JPanel panelPills = new JPanel(new GridLayout(0, 3, 6, 6));
        panelPills.setOpaque(false);
        panelPills.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        panelPills.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] tags = {
            "Evolución favorable", "Sin complicaciones", "Alta con seguimiento",
            "Requiere reposo", "Dieta especial", "Control en 7 días",};
        for (String tag : tags) {
            JButton btnTag = crearPill(tag);
            btnTag.addActionListener(e -> {
                String cur = areaObservaciones.getText().trim();
                if (cur.equals("Describa el diagnóstico, hallazgos, recomendaciones y próximos pasos...") || cur.isEmpty()) {
                    areaObservaciones.setText(tag);
                } else {
                    String concat = cur + ", " + tag;
                    if (concat.length() > 1000) {
                        concat = concat.substring(0, 1000);
                    }
                    areaObservaciones.setText(concat);
                }
                areaObservaciones.setForeground(recursos.Color.INK);
                areaObservaciones.requestFocus();
            });
            panelPills.add(btnTag);
        }
        cardObs.add(panelPills);
        cardObs.add(Box.createVerticalStrut(10));

        // Campo de Texto de Observación
        areaObservaciones = new JTextArea(4, 22) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(148, 163, 184));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    Insets insets = getInsets();
                    g2.drawString("Describa el diagnóstico, hallazgos, recomendaciones y próximos pasos...", insets.left + 2, insets.top + g2.getFontMetrics().getAscent());
                    g2.dispose();
                }
            }
        };
        areaObservaciones.setFont(CargadorFuentes.cargar(12f));
        areaObservaciones.setForeground(recursos.Color.INK);
        areaObservaciones.setLineWrap(true);
        areaObservaciones.setWrapStyleWord(true);
        areaObservaciones.setText(turno.getObservaciones() != null ? turno.getObservaciones() : "");

        // Limitar caracteres
        areaObservaciones.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (areaObservaciones.getText().length() >= 300) {
                    e.consume();
                }
            }
        });

        JScrollPane scrollObs = new JScrollPane(areaObservaciones);
        scrollObs.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
        scrollObs.getViewport().setBackground(Color.WHITE);
        scrollObs.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        scrollObs.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardObs.add(scrollObs);

        cardObs.add(Box.createVerticalStrut(10));

        lblCharCount = new JLabel("0 / 1000");
        lblCharCount.setFont(CargadorFuentes.cargar(10f));
        lblCharCount.setForeground(recursos.Color.MUTED);

        JPanel panelCharCount = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelCharCount.setOpaque(false);
        panelCharCount.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCharCount.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        panelCharCount.add(lblCharCount);

        // Listener de conteo
        areaObservaciones.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            private void update() {
                int len = areaObservaciones.getText().length();
                lblCharCount.setText(len + " / 1000");
                if (len > 1000) {
                    lblCharCount.setForeground(recursos.Color.ERROR);
                } else {
                    lblCharCount.setForeground(recursos.Color.MUTED);
                }
            }
        });
        // Forzar conteo inicial
        lblCharCount.setText(areaObservaciones.getText().length() + " / 1000");

        cardObs.add(panelCharCount);

        // Añadir todas las tarjetas al contenedor (con Fila Unificada para Turno y Control)
        cardPaciente.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTurnoControlRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardAcciones.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardObs.setAlignmentX(Component.LEFT_ALIGNMENT);

        scrollBodyPanel.add(cardPaciente);
        scrollBodyPanel.add(Box.createVerticalStrut(14));
        scrollBodyPanel.add(panelTurnoControlRow);
        scrollBodyPanel.add(Box.createVerticalStrut(14));
        scrollBodyPanel.add(cardAcciones);
        scrollBodyPanel.add(Box.createVerticalStrut(14));
        scrollBodyPanel.add(cardObs);

        // -- Scroll Pane con barra moderna --
        JPanel wrapper = new ScrollablePanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(scrollBodyPanel, BorderLayout.NORTH);

        JScrollPane mainScroll = new JScrollPane(wrapper);
        mainScroll.setBorder(null);
        mainScroll.setViewportBorder(null);
        mainScroll.setOpaque(false);
        mainScroll.getViewport().setOpaque(false);
        mainScroll.getVerticalScrollBar().setUnitIncrement(14);
        mainScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Estilo de barra de scroll
        mainScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        mainScroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        panelFondo.add(mainScroll, BorderLayout.CENTER);

        // ==========================================
        //  FOOTER ACTIONS (Save & Complete)
        // ==========================================
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 24, 20, 24));

        JButton btnGuardarBorrador = new JButton("Guardar borrador ");
        btnGuardarBorrador.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        btnGuardarBorrador.setBackground(Color.WHITE);
        btnGuardarBorrador.setForeground(recursos.Color.INK);
        btnGuardarBorrador.setFocusPainted(false);
        btnGuardarBorrador.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(10, 16, 10, 16)
        ));
        btnGuardarBorrador.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardarBorrador.addActionListener(e -> guardarBorrador(true));

        JButton btnCompletar = new JButton("Completar Turno ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(recursos.Color.PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnCompletar.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        btnCompletar.setForeground(recursos.Color.WHITE);
        btnCompletar.setFocusPainted(false);
        btnCompletar.setContentAreaFilled(false);
        btnCompletar.setOpaque(false);
        btnCompletar.setBorderPainted(false);
        btnCompletar.setBorder(new EmptyBorder(10, 24, 10, 24));
        btnCompletar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCompletar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCompletar.setBackground(recursos.Color.PRIMARY_DEEP);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnCompletar.setBackground(recursos.Color.PRIMARY);
            }
        });
        btnCompletar.addActionListener(e -> completarAtencion());

        footerPanel.add(btnGuardarBorrador, BorderLayout.WEST);
        footerPanel.add(btnCompletar, BorderLayout.EAST);

        panelFondo.add(footerPanel, BorderLayout.SOUTH);
        add(panelFondo, BorderLayout.CENTER);

        // Cargar listado inicial de medicamentos de la sesión
        refrescarMedicamentos();
    }

    private JButton crearBotonAccionClinica(String emoji, String text) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(10, 0));
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(14, 14, 14, 14)
        ));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        lblEmoji.setForeground(recursos.Color.INK);

        JLabel lblText = new JLabel(text);
        lblText.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        lblText.setForeground(recursos.Color.INK);

        JLabel lblPlus = new JLabel("+");
        lblPlus.setFont(CargadorFuentes.cargar(14f).deriveFont(Font.BOLD));
        lblPlus.setForeground(recursos.Color.MUTED);

        btn.add(lblEmoji, BorderLayout.WEST);
        btn.add(lblText, BorderLayout.CENTER);
        btn.add(lblPlus, BorderLayout.EAST);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(248, 250, 252));
                btn.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(recursos.Color.ACCENT_BLUE, 1, true),
                        new EmptyBorder(10, 14, 10, 14)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(226, 232, 240), 1, true),
                        new EmptyBorder(10, 14, 10, 14)
                ));
            }
        });
        return btn;
    }

    private JButton crearPill(String tagText) {
        JButton btn = new JButton(tagText);
        btn.setFont(CargadorFuentes.cargar(11f));
        btn.setBackground(new Color(248, 250, 252));
        btn.setForeground(recursos.Color.MUTED);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(4, 10, 4, 10)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(241, 245, 249));
                btn.setForeground(recursos.Color.INK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(248, 250, 252));
                btn.setForeground(recursos.Color.MUTED);
            }
        });
        return btn;
    }

    private void abrirRecetar() {
        try {
            DialogoRecetarMedicamento d = new DialogoRecetarMedicamento(this, controlador, animal);
            d.setVisible(true);
            if (d.isRecetado()) {
                refrescarMedicamentos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir recetar: " + ex.getMessage());
        }
    }

    private void abrirVacuna() {
        try {
            DialogoRegistrarVacunacion d = new DialogoRegistrarVacunacion(this, controlador, animal);
            d.setVisible(true);
            if (d.isRegistrado()) {
                refrescarMedicamentos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir vacuna: " + ex.getMessage());
        }
    }

    private void refrescarMedicamentos() {
        panelMedicamentos.removeAll();

        String query = txtFiltrarAplicados.getText().trim().toLowerCase();

        ArrayList<Prescripcion> recetados = animal.getHistorial().getMedicamentosRecetados();
        ArrayList<RegistroVacunacion> vacunas = animal.getHistorial().getRegistroVacunas();

        int count = 0;

        for (Prescripcion p : recetados) {
            String details = p.getNombreMedicamento() + " (" + p.getCodigoSenasa() + ")";
            details += " · Dosis: " + p.getDosis() + " · Vía: " + p.getViaAdministracion() + " · Frec: " + p.getFrecuencia();

            if (!query.isEmpty() && !details.toLowerCase().contains(query)) {
                continue;
            }

            JPanel item = crearFilaAplicado("💊", details, () -> {
                animal.getHistorial().getMedicamentosRecetados().remove(p);
                refrescarMedicamentos();
            });
            panelMedicamentos.add(item);
            panelMedicamentos.add(Box.createVerticalStrut(6));
            count++;
        }

        for (RegistroVacunacion rv : vacunas) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String details = rv.getVacunaAplicada().getNombreMedicamento() + " (" + rv.getVacunaAplicada().getCodigoSenasa() + ")"
                    + " · Lote: " + (rv.getLote().isEmpty() ? "S/L" : rv.getLote())
                    + " · Vence: " + rv.getFechaVencimiento().format(fmt);

            if (!query.isEmpty() && !details.toLowerCase().contains(query)) {
                continue;
            }

            JPanel item = crearFilaAplicado("💉", details, () -> {
                animal.getHistorial().getRegistroVacunas().remove(rv);
                refrescarMedicamentos();
            });
            panelMedicamentos.add(item);
            panelMedicamentos.add(Box.createVerticalStrut(6));
            count++;
        }

        if (count == 0) {
            JLabel empty = new JLabel(query.isEmpty() ? "(Sin medicación ni vacunas registradas)" : "(No se encontraron coincidencias)");
            empty.setFont(CargadorFuentes.cargar(12f));
            empty.setForeground(recursos.Color.MUTED);
            empty.setBorder(new EmptyBorder(8, 8, 8, 8));
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelMedicamentos.add(empty);
        }

        panelMedicamentos.revalidate();
        panelMedicamentos.repaint();
    }

    private JPanel crearFilaAplicado(String icon, String text, Runnable onDelete) {
        JPanel p = new JPanel(new BorderLayout(8, 0)) {
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
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(6, 10, 6, 10));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));

        JLabel lblText = new JLabel(text);
        lblText.setFont(CargadorFuentes.cargar(12f));
        lblText.setForeground(recursos.Color.INK);

        JButton btnDel = new JButton("X");
        btnDel.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        btnDel.setForeground(recursos.Color.ERROR);
        btnDel.setOpaque(false);
        btnDel.setContentAreaFilled(false);
        btnDel.setBorderPainted(false);
        btnDel.setFocusPainted(false);
        btnDel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDel.addActionListener(e -> onDelete.run());

        p.add(lblIcon, BorderLayout.WEST);
        p.add(lblText, BorderLayout.CENTER);
        p.add(btnDel, BorderLayout.EAST);
        return p;
    }

    private void guardarBorrador(boolean mostrarMensaje) {
        String obs = areaObservaciones.getText().trim();
        turno.setObservaciones(obs);
        if (mostrarMensaje) {
            JOptionPane.showMessageDialog(this, "Borrador guardado con éxito.");
        }
    }

    private void completarAtencion() {
        guardarBorrador(false);

        // Opcional: Generar próximo turno si se especificó fecha de control
        String controlFecha = txtProximoControl.getText().trim();
        if (!controlFecha.isEmpty() && !controlFecha.equals("dd/mm/aaaa")) {
            try {
                // Validar formato básico dd/mm/aaaa
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate.parse(controlFecha, fmt);

                // Agendar próximo turno automático para el mismo veterinario y animal
                // como un control clínico general en un horario por defecto (ej: 10:00)
                controlador.registrarTurno(
                        controlFecha, "10:00", turno.getVeterinario(), animal, TipoTurno.CONSULTA_GENERAL, "Turno agendado automáticamente por control médico."
                );
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "La fecha de próximo control es inválida. Use dd/MM/yyyy o déjelo vacío.");
                return;
            }
        }

        if (!turno.estaCompletado()) {
            turno.completarTurno();
        }

        // Abrir Comprobante
        Veterinaria v = controlador.getVeterinaria();
        ComprobanteTurno comp = new ComprobanteTurno(turno, v.getNombreNegocio());
        DialogoComprobante dc = new DialogoComprobante((Frame) getOwner(), comp);
        dc.setVisible(true);

        dispose();
    }

    private static Color colorCategoria(TipoTurno tipo) {
        if (tipo == null) {
            return recursos.Color.PRIMARY;
        }
        return switch (tipo) {
            case CIRUGIA ->
                recursos.Color.CAT_CIRUGIA;
            case CONSULTA_GENERAL ->
                recursos.Color.CAT_CONSULTA;
            case ANALISIS ->
                recursos.Color.CAT_ANALISIS;
            case VACUNACION ->
                recursos.Color.CAT_VACUNA;
            default ->
                recursos.Color.CAT_CONTROL;
        };
    }

    // ==========================================
    //  HELPER INNER CLASSES (Modern UI elements)
    // ==========================================
    private static class CardPanel extends JPanel {

        private final Color topColor;
        private final int radius = 16;
        private final int topBarHeight = 6;

        public CardPanel(Color topColor) {
            this.topColor = topColor;
            setOpaque(false);
            setBackground(Color.WHITE);
            setBorder(new EmptyBorder(16, 20, 16, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw white card body
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            // Paint colored top bar
            g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.setColor(topColor);
            g2.fillRect(0, 0, getWidth(), topBarHeight);

            // Draw subtle card border
            g2.setClip(null);
            g2.setColor(new Color(226, 232, 240));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.dispose();
        }
    }

    private static class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color colorFinal = isDragging ? new Color(100, 116, 139)
                    : (isThumbRollover() ? new Color(148, 163, 184) : new Color(203, 213, 225));

            g2.setColor(colorFinal);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                    thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
            g2.dispose();
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return crearBotonInvisible();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return crearBotonInvisible();
        }

        private JButton crearBotonInvisible() {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            btn.setMinimumSize(new Dimension(0, 0));
            btn.setMaximumSize(new Dimension(0, 0));
            return btn;
        }
    }

    private static class ScrollablePanel extends JPanel implements Scrollable {

        public ScrollablePanel(LayoutManager layout) {
            super(layout);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 64;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
}
