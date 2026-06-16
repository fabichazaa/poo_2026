package vista.dialogos;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import modelo.*;
import recursos.CargadorFuentes;
import vista.componentes.Placeholders;

public class DialogoRegistrarVacunacion extends JDialog {

    private final ControladorVeterinaria controlador;
    private final Animal animal;
    private boolean registrado = false;

    private JTextField txtBuscarVacuna;
    private JComboBox<Vacuna> comboVacuna;

    private JTextField campoTipoDosis;
    private JTextField campoVia;
    private JTextField campoLote;

    private JTextField campoFechaAplicacion;
    private JTextField campoVigenciaDias;
    private JTextField campoProximaDosis;

    private JTextArea areaObservaciones;
    private JLabel lblError;

    private final List<Vacuna> listaVacunasSemilla = new ArrayList<>();

    public DialogoRegistrarVacunacion(Window owner, ControladorVeterinaria controlador, Animal animal) {
        super(owner, "Registrar vacunación — " + animal.getNombre(), Dialog.ModalityType.APPLICATION_MODAL);
        this.controlador = controlador;
        this.animal = animal;
        inicializarSemillaVacunas();
        construir();
    }

    public boolean isRegistrado() {
        return registrado;
    }

    private void inicializarSemillaVacunas() {
        // Semilla de vacunas estándar para mostrar en el catálogo con descripciones atractivas
        Vacuna v1 = new Vacuna("VAC-001", "Antirrábica", 365);
        v1.setCategoria("Nobivac Rabies");
        Vacuna v2 = new Vacuna("VAC-002", "Triple Felina", 365);
        v2.setCategoria("Nobivac Tricat Trio");
        Vacuna v3 = new Vacuna("VAC-003", "Parvovirus Canino", 365);
        v3.setCategoria("Nobivac Parvo");
        Vacuna v4 = new Vacuna("VAC-004", "Quíntuple Canina", 365);
        v4.setCategoria("Defensor 5");
        Vacuna v5 = new Vacuna("VAC-005", "Leucemia Felina", 365);
        v5.setCategoria("Nobivac FeLV");

        listaVacunasSemilla.add(v1);
        listaVacunasSemilla.add(v2);
        listaVacunasSemilla.add(v3);
        listaVacunasSemilla.add(v4);
        listaVacunasSemilla.add(v5);
    }

    private void construir() {
        setSize(540, 640);
        setUndecorated(true);
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 24, 24));
        setResizable(false);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        // ==========================================
        //  HEADER BANNER (Purple/Violet)
        // ==========================================
        JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(16, 24, 16, 24));

        JPanel textHeader = new JPanel();
        textHeader.setOpaque(false);
        textHeader.setLayout(new BoxLayout(textHeader, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Registrar Vacunación");
        lblTitulo.setFont(CargadorFuentes.cargar(16f).deriveFont(Font.BOLD));
        lblTitulo.setForeground(Color.WHITE);

        String raza = animal.getRaza();
        JLabel lblSub = new JLabel("Para: " + animal.getNombre() + " (" + raza + " · " + animal.getEspecie() + ")");
        lblSub.setFont(CargadorFuentes.cargar(12f));
        lblSub.setForeground(recursos.Color.PURPLE_LIGHT);

        textHeader.add(lblTitulo);
        textHeader.add(Box.createVerticalStrut(2));
        textHeader.add(lblSub);

        JButton btnClose = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 40));
                    g2.fillOval(0, 0, getWidth(), getHeight());
                }
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int size = 12;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                g2.drawLine(x, y, x + size, y + size);
                g2.drawLine(x + size, y, x, y + size);
                g2.dispose();
            }
        };
        btnClose.setPreferredSize(new Dimension(28, 28));
        btnClose.setOpaque(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setBorderPainted(false);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());

        headerPanel.add(textHeader, BorderLayout.CENTER);
        headerPanel.add(btnClose, BorderLayout.EAST);

        // Borde redondeado simulado para toda la ventana usando un panel principal con borde
        JPanel panelFondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                int headerHeight = headerPanel.getHeight() > 0 ? headerPanel.getHeight() : 70;
                g2.setColor(recursos.Color.PURPLE_DARK); // Purple Dark
                g2.fillRect(0, 0, getWidth(), headerHeight + 1);
                g2.dispose();
                
                Graphics2D gBorder = (Graphics2D) g.create();
                gBorder.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gBorder.setColor(recursos.Color.BORDER);
                gBorder.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                gBorder.dispose();
            }
        };
        panelFondo.setOpaque(true);
        panelFondo.setBorder(new EmptyBorder(1, 1, 1, 1));

        panelFondo.add(headerPanel, BorderLayout.NORTH);

        // ==========================================
        //  FORM BODY (White)
        // ==========================================
        JPanel bodyPanel = new JPanel();
        bodyPanel.setOpaque(false);
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBorder(new EmptyBorder(20, 24, 20, 24));

        // VACUNA DEL CATÁLOGO
        JLabel lblVac = new JLabel("VACUNA DEL CATÁLOGO");
        lblVac.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblVac.setForeground(recursos.Color.MUTED);
        lblVac.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPanel.add(lblVac);
        bodyPanel.add(Box.createVerticalStrut(6));

        // Buscador reactivo
        txtBuscarVacuna = new Placeholders.TextField("🔍 Escribí para buscar vacuna...");
        txtBuscarVacuna.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtBuscarVacuna.setPreferredSize(new Dimension(0, 38));
        txtBuscarVacuna.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(recursos.Color.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        txtBuscarVacuna.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        txtBuscarVacuna.setForeground(recursos.Color.INK);
        txtBuscarVacuna.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPanel.add(txtBuscarVacuna);
        bodyPanel.add(Box.createVerticalStrut(8));

        // JComboBox de vacunas
        comboVacuna = new JComboBox<>();
        comboVacuna.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        comboVacuna.setPreferredSize(new Dimension(0, 52));
        comboVacuna.setFont(CargadorFuentes.cargar(12f));
        comboVacuna.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboVacuna.setBackground(Color.WHITE);
        comboVacuna.setBorder(new LineBorder(recursos.Color.BORDER, 1, true));

        // Renderer personalizado con jeringa
        comboVacuna.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JPanel panel = new JPanel(new BorderLayout(10, 0));
                panel.setBorder(new EmptyBorder(6, 10, 6, 10));
                panel.setOpaque(true);

                boolean isItemSelected = isSelected && index != -1;

                if (isItemSelected) {
                    panel.setBackground(recursos.Color.PURPLE_DARK);
                } else {
                    panel.setBackground(Color.WHITE);
                }

                if (value instanceof Vacuna v) {
                    JLabel lblIcon = new JLabel("💉");
                    lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));

                    JPanel textPanel = new JPanel();
                    textPanel.setOpaque(false);
                    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

                    JLabel lblNombre = new JLabel(v.getCodigoSenasa() + " — " + v.getNombreMedicamento());
                    lblNombre.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
                    lblNombre.setForeground(isItemSelected ? Color.WHITE : recursos.Color.INK);

                    JLabel lblSub = new JLabel(v.getCategoria().isEmpty() ? "Vacuna" : v.getCategoria());
                    lblSub.setFont(CargadorFuentes.cargar(10f));
                    lblSub.setForeground(isItemSelected ? recursos.Color.PURPLE_LIGHT : recursos.Color.MUTED);

                    textPanel.add(lblNombre);
                    textPanel.add(lblSub);

                    panel.add(lblIcon, BorderLayout.WEST);
                    panel.add(textPanel, BorderLayout.CENTER);
                }
                return panel;
            }
        });
        bodyPanel.add(comboVacuna);
        bodyPanel.add(Box.createVerticalStrut(14));

        // TIPO DE DOSIS & VÍA
        JPanel gridDosisVia = new JPanel(new GridLayout(1, 2, 16, 0));
        gridDosisVia.setOpaque(false);
        gridDosisVia.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridDosisVia.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JPanel pDosis = new JPanel();
        pDosis.setOpaque(false);
        pDosis.setLayout(new BoxLayout(pDosis, BoxLayout.Y_AXIS));
        JLabel lblDosis = new JLabel("TIPO DE DOSIS");
        lblDosis.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblDosis.setForeground(recursos.Color.MUTED);
        campoTipoDosis = new JTextField("Dosis anual");
        campoTipoDosis.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(recursos.Color.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campoTipoDosis.setFont(CargadorFuentes.cargar(12f));
        campoTipoDosis.setForeground(recursos.Color.INK);
        pDosis.add(lblDosis);
        pDosis.add(Box.createVerticalStrut(6));
        pDosis.add(campoTipoDosis);

        JPanel pVia = new JPanel();
        pVia.setOpaque(false);
        pVia.setLayout(new BoxLayout(pVia, BoxLayout.Y_AXIS));
        JLabel lblVia = new JLabel("VÍA");
        lblVia.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblVia.setForeground(recursos.Color.MUTED);
        campoVia = new JTextField("Subcutánea");
        campoVia.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(recursos.Color.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campoVia.setFont(CargadorFuentes.cargar(12f));
        campoVia.setForeground(recursos.Color.INK);
        pVia.add(lblVia);
        pVia.add(Box.createVerticalStrut(6));
        pVia.add(campoVia);

        gridDosisVia.add(pDosis);
        gridDosisVia.add(pVia);
        bodyPanel.add(gridDosisVia);
        bodyPanel.add(Box.createVerticalStrut(14));

        // NÚMERO DE LOTE
        JLabel lblLote = new JLabel("NÚMERO DE LOTE (recomendado)");
        lblLote.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblLote.setForeground(recursos.Color.MUTED);
        lblLote.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPanel.add(lblLote);
        bodyPanel.add(Box.createVerticalStrut(6));

        campoLote = new Placeholders.TextField("Ej: LOT2026-A4892");
        campoLote.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        campoLote.setPreferredSize(new Dimension(0, 38));
        campoLote.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(recursos.Color.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campoLote.setFont(CargadorFuentes.cargar(12f));
        campoLote.setForeground(recursos.Color.INK);
        campoLote.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPanel.add(campoLote);
        bodyPanel.add(Box.createVerticalStrut(14));

        // FECHA DE APLICACIÓN, VIGENCIA, PRÓXIMA DOSIS
        JPanel gridTres = new JPanel(new GridLayout(1, 3, 12, 0));
        gridTres.setOpaque(false);
        gridTres.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridTres.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JPanel pAplic = new JPanel();
        pAplic.setOpaque(false);
        pAplic.setLayout(new BoxLayout(pAplic, BoxLayout.Y_AXIS));
        JLabel lblAplic = new JLabel("FECHA APLICACIÓN");
        lblAplic.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblAplic.setForeground(recursos.Color.MUTED);

        JPanel dateWrapper = new JPanel(new BorderLayout(6, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(recursos.Color.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
            }
        };
        dateWrapper.setOpaque(false);
        dateWrapper.setBorder(new EmptyBorder(8, 10, 8, 10));

        campoFechaAplicacion = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        campoFechaAplicacion.setBorder(null);
        campoFechaAplicacion.setOpaque(false);
        campoFechaAplicacion.setFont(CargadorFuentes.cargar(12f));
        campoFechaAplicacion.setForeground(recursos.Color.INK);
        JLabel lblCalIcon = new JLabel("📅");
        lblCalIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        dateWrapper.add(campoFechaAplicacion, BorderLayout.CENTER);
        dateWrapper.add(lblCalIcon, BorderLayout.EAST);

        pAplic.add(lblAplic);
        pAplic.add(Box.createVerticalStrut(6));
        pAplic.add(dateWrapper);

        JPanel pVig = new JPanel();
        pVig.setOpaque(false);
        pVig.setLayout(new BoxLayout(pVig, BoxLayout.Y_AXIS));
        JLabel lblVig = new JLabel("VIGENCIA (DÍAS)");
        lblVig.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblVig.setForeground(recursos.Color.MUTED);
        campoVigenciaDias = new JTextField("365");
        campoVigenciaDias.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(recursos.Color.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campoVigenciaDias.setFont(CargadorFuentes.cargar(12f));
        campoVigenciaDias.setForeground(recursos.Color.INK);
        pVig.add(lblVig);
        pVig.add(Box.createVerticalStrut(6));
        pVig.add(campoVigenciaDias);

        JPanel pProx = new JPanel();
        pProx.setOpaque(false);
        pProx.setLayout(new BoxLayout(pProx, BoxLayout.Y_AXIS));
        JLabel lblProx = new JLabel("PRÓXIMA DOSIS");
        lblProx.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblProx.setForeground(recursos.Color.MUTED);

        campoProximaDosis = new JTextField();
        campoProximaDosis.setEditable(false);
        campoProximaDosis.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(recursos.Color.PURPLE_LIGHT, 1, true), // purple border
                new EmptyBorder(8, 12, 8, 12)
        ));
        campoProximaDosis.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        campoProximaDosis.setBackground(recursos.Color.PURPLE_LIGHT); // Light purple background
        campoProximaDosis.setForeground(recursos.Color.PURPLE_DARK); // Purple text

        pProx.add(lblProx);
        pProx.add(Box.createVerticalStrut(6));
        pProx.add(campoProximaDosis);

        gridTres.add(pAplic);
        gridTres.add(pVig);
        gridTres.add(pProx);
        bodyPanel.add(gridTres);
        bodyPanel.add(Box.createVerticalStrut(14));

        // OBSERVACIONES
        JLabel lblObs = new JLabel("OBSERVACIONES (opcional)");
        lblObs.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblObs.setForeground(recursos.Color.MUTED);
        lblObs.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPanel.add(lblObs);
        bodyPanel.add(Box.createVerticalStrut(6));

        areaObservaciones = new Placeholders.TextArea("Reacciones post-vacunación, condiciones especiales del animal...", 3, 20);
        areaObservaciones.setFont(CargadorFuentes.cargar(12f));
        areaObservaciones.setForeground(recursos.Color.INK);
        areaObservaciones.setLineWrap(true);
        areaObservaciones.setWrapStyleWord(true);

        JScrollPane scrollObs = new JScrollPane(areaObservaciones);
        scrollObs.setBorder(new LineBorder(recursos.Color.BORDER, 1, true));
        scrollObs.getViewport().setBackground(Color.WHITE);
        scrollObs.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollObs.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        scrollObs.setPreferredSize(new Dimension(0, 90));
        bodyPanel.add(scrollObs);

        bodyPanel.add(Box.createVerticalStrut(8));

        lblError = new JLabel(" ");
        lblError.setFont(CargadorFuentes.cargar(11f));
        lblError.setForeground(recursos.Color.ERROR);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPanel.add(lblError);

        panelFondo.add(bodyPanel, BorderLayout.CENTER);

        // ==========================================
        //  FOOTER ACTIONS (Cancelar & Confirmar)
        // ==========================================
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 24, 20, 24));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(recursos.Color.INK);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(recursos.Color.BORDER, 1, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnConfirmar = new JButton("💉 Registrar vacunación") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnConfirmar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));

        btnConfirmar.setBackground(recursos.Color.PURPLE_DARK);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setContentAreaFilled(false);
        btnConfirmar.setOpaque(false);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnConfirmar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConfirmar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnConfirmar.setBackground(recursos.Color.PURPLE_HOVER); // darker purple
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnConfirmar.setBackground(recursos.Color.PURPLE_DARK);
            }
        });
        btnConfirmar.addActionListener(e -> intentarConfirmar());

        footerPanel.add(btnCancelar);
        footerPanel.add(btnConfirmar);

        panelFondo.add(footerPanel, BorderLayout.SOUTH);

        add(panelFondo, BorderLayout.CENTER);

        // Listeners reactivos para el cálculo de fecha
        inicializarListeners();
    }

    private void inicializarListeners() {
        // Cálculo reactivo
        javax.swing.event.DocumentListener calculador = new javax.swing.event.DocumentListener() {

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calcular();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calcular();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                calcular();
            }

            private void calcular() {
                try {
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate aplic = LocalDate.parse(campoFechaAplicacion.getText().trim(), fmt);
                    int vigencia = Integer.parseInt(campoVigenciaDias.getText().trim());
                    if (vigencia >= 0) {
                        LocalDate prox = aplic.plusDays(vigencia);
                        campoProximaDosis.setText(prox.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    } else {
                        campoProximaDosis.setText("—");
                    }
                } catch (NumberFormatException ex) {
                    campoProximaDosis.setText("—");
                }
            }
        };

        campoFechaAplicacion.getDocument().addDocumentListener(calculador);
        campoVigenciaDias.getDocument().addDocumentListener(calculador);

        // Forzar cálculo inicial
        try {
            LocalDate aplic = LocalDate.parse(campoFechaAplicacion.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            int vig = Integer.parseInt(campoVigenciaDias.getText().trim());
            campoProximaDosis.setText(aplic.plusDays(vig).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (NumberFormatException ex) {
            campoProximaDosis.setText("—");
        }

        // Buscador reactivo para vacunas
        txtBuscarVacuna.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            private void filtrar() {
                String query = txtBuscarVacuna.getText().trim().toLowerCase();
                if (query.equals("escribí para buscar vacuna...")) {
                    query = "";
                }
                ArrayList<Vacuna> filtrado = new ArrayList<>();
                for (Vacuna v : listaVacunasSemilla) {
                    if (v.getNombreMedicamento().toLowerCase().contains(query)
                            || v.getCodigoSenasa().toLowerCase().contains(query)
                            || v.getCategoria().toLowerCase().contains(query)) {
                        filtrado.add(v);
                    }
                }
                refrescarCombo(filtrado);
            }
        });

        refrescarCombo(listaVacunasSemilla);
    }

    private void refrescarCombo(List<Vacuna> items) {
        comboVacuna.removeAllItems();
        for (Vacuna v : items) {
            comboVacuna.addItem(v);
        }
    }

    private void intentarConfirmar() {
        lblError.setText(" ");
        Vacuna sel = (Vacuna) comboVacuna.getSelectedItem();
        if (sel == null) {
            lblError.setText("Por favor seleccione una vacuna.");
            return;
        }

        String tipoDosis = campoTipoDosis.getText().trim();
        if (tipoDosis.isEmpty()) {
            lblError.setText("Por favor ingrese el tipo de dosis.");
            return;
        }

        String via = campoVia.getText().trim();
        if (via.isEmpty()) {
            lblError.setText("Por favor ingrese la vía.");
            return;
        }

        String lote = campoLote.getText().trim();
        if (lote.equals("Ej: LOT2026-A4892")) {
            lote = "";
        }

        LocalDate fechaAplic;
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fechaAplic = LocalDate.parse(campoFechaAplicacion.getText().trim(), fmt);
        } catch (DateTimeParseException ex) {
            lblError.setText("Fecha de aplicación inválida. Use dd/MM/yyyy.");
            return;
        }

        int vigencia;
        try {
            vigencia = Integer.parseInt(campoVigenciaDias.getText().trim());
            if (vigencia <= 0) {
                lblError.setText("La vigencia debe ser mayor a 0 días.");
                return;
            }
        } catch (NumberFormatException ex) {
            lblError.setText("Vigencia inválida. Ingrese un número entero.");
            return;
        }

        String obs = areaObservaciones.getText().trim();
        if (obs.equals("Reacciones post-vacunación, condiciones especiales del animal...")) {
            obs = "";
        }

        // Crear vacuna final con la vigencia ingresada
        Vacuna v = new Vacuna(sel.getCodigoSenasa(), sel.getNombreMedicamento(), vigencia);
        v.setCategoria(sel.getCategoria());

        // Crear registro completo de vacunación
        RegistroVacunacion registro = new RegistroVacunacion(
                v, fechaAplic, tipoDosis, via, lote, obs
        );

        // Registrar en el controlador
        controlador.registrarVacunacion(animal, registro);
        registrado = true;

        JOptionPane.showMessageDialog(this, "Vacunación registrada con éxito.");
        dispose();
    }
}
