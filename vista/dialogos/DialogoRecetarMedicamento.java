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

public class DialogoRecetarMedicamento extends JDialog {

    private final ControladorVeterinaria controlador;
    private final Animal animal;
    private boolean recetado = false;

    private JRadioButton radioCatalogo;
    private JRadioButton radioMagistral;

    private JPanel panelDelCatalogo;
    private JPanel panelMagistral;

    private JTextField txtBuscarMed;
    private JComboBox<Medicamento> comboMedicamento;
    private JTextField txtMedMagistral;

    private JTextField campoDosis;
    private JTextField campoVia;

    private JTextField campoFrecuencia;
    private JTextField campoFechaInicio;
    private JTextField campoVigenciaDias;

    private JTextArea areaIndicaciones;
    private JLabel lblError;

    public DialogoRecetarMedicamento(Window owner, ControladorVeterinaria controlador, Animal animal) {
        super(owner, "Recetar medicamento — " + animal.getNombre(), Dialog.ModalityType.APPLICATION_MODAL);
        this.controlador = controlador;
        this.animal = animal;
        construir();
    }

    public boolean isRecetado() {
        return recetado;
    }

    private void construir() {
        setSize(540, 640);
        setUndecorated(true);
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 24, 24));
        setResizable(false);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        // ==========================================
        //  HEADER BANNER (Green/Teal)
        // ==========================================
        JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(16, 24, 16, 24));

        JPanel textHeader = new JPanel();
        textHeader.setOpaque(false);
        textHeader.setLayout(new BoxLayout(textHeader, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Recetar Medicamento");
        lblTitulo.setFont(CargadorFuentes.cargar(16f).deriveFont(Font.BOLD));
        lblTitulo.setForeground(Color.WHITE);

        String raza = "Mixto";
        switch (animal) {
            case Perro perro ->
                raza = perro.getRaza();
            case Gato gato ->
                raza = gato.getRaza();
            default -> {
            }
        }
        JLabel lblSub = new JLabel("Para: " + animal.getNombre() + " (" + raza + " · " + animal.getEspecie() + ")");
        lblSub.setFont(CargadorFuentes.cargar(12f));
        lblSub.setForeground(new Color(226, 240, 238));

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
                g2.setColor(recursos.Color.PRIMARY);
                g2.fillRect(0, 0, getWidth(), headerHeight + 1);
                g2.dispose();

                Graphics2D gBorder = (Graphics2D) g.create();
                gBorder.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gBorder.setColor(new Color(226, 232, 240));
                gBorder.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                gBorder.dispose();
            }
        };
        panelFondo.setOpaque(true);
        panelFondo.setBorder(new EmptyBorder(1, 1, 1, 1));

        panelFondo.add(headerPanel, BorderLayout.NORTH);

        // ==========================================
        //  FORM BODY (White background)
        // ==========================================
        JPanel bodyPanel = new JPanel();
        bodyPanel.setOpaque(false);
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBorder(new EmptyBorder(20, 24, 20, 24));

        // TIPO DE MEDICAMENTO (Selector de radios)
        JLabel lblTipo = new JLabel("TIPO DE MEDICAMENTO");
        lblTipo.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblTipo.setForeground(recursos.Color.MUTED);
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPanel.add(lblTipo);
        bodyPanel.add(Box.createVerticalStrut(6));

        JPanel panelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        panelRadios.setOpaque(false);
        panelRadios.setAlignmentX(Component.LEFT_ALIGNMENT);
        radioCatalogo = new JRadioButton("Del catálogo", true);
        radioMagistral = new JRadioButton("Manual", false);
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(radioCatalogo);
        grupo.add(radioMagistral);
        radioCatalogo.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        radioMagistral.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        radioCatalogo.setOpaque(false);
        radioMagistral.setOpaque(false);
        radioCatalogo.setForeground(recursos.Color.INK);
        radioMagistral.setForeground(recursos.Color.INK);

        radioCatalogo.addActionListener(e -> actualizarVisibilidad());
        radioMagistral.addActionListener(e -> actualizarVisibilidad());

        panelRadios.add(radioCatalogo);
        panelRadios.add(radioMagistral);
        bodyPanel.add(panelRadios);
        bodyPanel.add(Box.createVerticalStrut(14));

        // PANEL SELECTOR DE MEDICAMENTO (INTERMITENTE)
        JPanel panelMedSelector = new JPanel(new CardLayout());
        panelMedSelector.setOpaque(false);
        panelMedSelector.setAlignmentX(Component.LEFT_ALIGNMENT);

        // CARD DEL CATÁLOGO
        panelDelCatalogo = new JPanel();
        panelDelCatalogo.setOpaque(false);
        panelDelCatalogo.setLayout(new BoxLayout(panelDelCatalogo, BoxLayout.Y_AXIS));

        JLabel lblCat = new JLabel("BUSCAR PRODUCTO DEL CATÁLOGO");
        lblCat.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblCat.setForeground(recursos.Color.MUTED);
        lblCat.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDelCatalogo.add(lblCat);
        panelDelCatalogo.add(Box.createVerticalStrut(6));

        // Input buscador
        txtBuscarMed = new PlaceHolderTextField("🔍 Escribí para buscar medicamento...");
        txtBuscarMed.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtBuscarMed.setPreferredSize(new Dimension(0, 38));
        txtBuscarMed.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        txtBuscarMed.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        txtBuscarMed.setForeground(recursos.Color.INK);
        txtBuscarMed.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDelCatalogo.add(txtBuscarMed);
        panelDelCatalogo.add(Box.createVerticalStrut(8));

        // ComboBox de medicamentos
        comboMedicamento = new JComboBox<>();
        comboMedicamento.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        comboMedicamento.setPreferredSize(new Dimension(0, 52));
        comboMedicamento.setFont(CargadorFuentes.cargar(12f));
        comboMedicamento.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboMedicamento.setBackground(Color.WHITE);
        comboMedicamento.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));

        // Renderer personalizado
        comboMedicamento.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JPanel panel = new JPanel(new BorderLayout(10, 0));
                panel.setBorder(new EmptyBorder(6, 10, 6, 10));
                panel.setOpaque(true);

                boolean isItemSelected = isSelected && index != -1;

                if (isItemSelected) {
                    panel.setBackground(recursos.Color.PRIMARY);
                } else {
                    panel.setBackground(Color.WHITE);
                }

                if (value instanceof Medicamento m) {
                    JLabel lblIcon = new JLabel("💊");
                    lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));

                    JPanel textPanel = new JPanel();
                    textPanel.setOpaque(false);
                    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

                    JLabel lblNombre = new JLabel(m.getCodigoSenasa() + " — " + m.getNombreMedicamento());
                    lblNombre.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
                    lblNombre.setForeground(isItemSelected ? Color.WHITE : recursos.Color.INK);

                    JLabel lblSub = new JLabel(m.getSubtitulo().isEmpty() ? "Medicamento" : m.getSubtitulo());
                    lblSub.setFont(CargadorFuentes.cargar(10f));
                    lblSub.setForeground(isItemSelected ? new Color(226, 240, 238) : recursos.Color.MUTED);

                    textPanel.add(lblNombre);
                    textPanel.add(lblSub);

                    panel.add(lblIcon, BorderLayout.WEST);
                    panel.add(textPanel, BorderLayout.CENTER);
                }
                return panel;
            }
        });

        panelDelCatalogo.add(comboMedicamento);

        // CARD MAGISTRAL
        panelMagistral = new JPanel();
        panelMagistral.setOpaque(false);
        panelMagistral.setLayout(new BoxLayout(panelMagistral, BoxLayout.Y_AXIS));

        JLabel lblMag = new JLabel("NOMBRE DEL MEDICAMENTO MAGISTRAL / MANUAL");
        lblMag.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblMag.setForeground(recursos.Color.MUTED);
        lblMag.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelMagistral.add(lblMag);
        panelMagistral.add(Box.createVerticalStrut(6));

        txtMedMagistral = new PlaceHolderTextField("Ej: Amoxicilina suspensión especial");
        txtMedMagistral.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtMedMagistral.setPreferredSize(new Dimension(0, 38));
        txtMedMagistral.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        txtMedMagistral.setFont(CargadorFuentes.cargar(12f));
        txtMedMagistral.setForeground(recursos.Color.INK);
        txtMedMagistral.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelMagistral.add(txtMedMagistral);
        panelMagistral.add(Box.createVerticalStrut(8));

        panelMedSelector.add(panelDelCatalogo, "catalogo");
        panelMedSelector.add(panelMagistral, "magistral");

        bodyPanel.add(panelMedSelector);
        bodyPanel.add(Box.createVerticalStrut(14));

        // DOSIS & VÍA (2 columnas)
        JPanel gridDosisVia = new JPanel(new GridLayout(1, 2, 16, 0));
        gridDosisVia.setOpaque(false);
        gridDosisVia.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridDosisVia.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JPanel pDosis = new JPanel();
        pDosis.setOpaque(false);
        pDosis.setLayout(new BoxLayout(pDosis, BoxLayout.Y_AXIS));
        JLabel lblDosis = new JLabel("DOSIS");
        lblDosis.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblDosis.setForeground(recursos.Color.MUTED);
        campoDosis = new PlaceHolderTextField("Ej: 1 comprimido");
        campoDosis.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campoDosis.setFont(CargadorFuentes.cargar(12f));
        campoDosis.setForeground(recursos.Color.INK);
        pDosis.add(lblDosis);
        pDosis.add(Box.createVerticalStrut(6));
        pDosis.add(campoDosis);

        JPanel pVia = new JPanel();
        pVia.setOpaque(false);
        pVia.setLayout(new BoxLayout(pVia, BoxLayout.Y_AXIS));
        JLabel lblVia = new JLabel("VÍA DE ADMINISTRACIÓN");
        lblVia.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblVia.setForeground(recursos.Color.MUTED);
        campoVia = new JTextField("Oral");
        campoVia.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
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

        // FRECUENCIA, FECHA INICIO, VIGENCIA (3 columnas)
        JPanel gridTres = new JPanel(new GridLayout(1, 3, 12, 0));
        gridTres.setOpaque(false);
        gridTres.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridTres.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JPanel pFrec = new JPanel();
        pFrec.setOpaque(false);
        pFrec.setLayout(new BoxLayout(pFrec, BoxLayout.Y_AXIS));
        JLabel lblFrec = new JLabel("FRECUENCIA");
        lblFrec.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblFrec.setForeground(recursos.Color.MUTED);
        campoFrecuencia = new JTextField("Cada 12hs");
        campoFrecuencia.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campoFrecuencia.setFont(CargadorFuentes.cargar(12f));
        campoFrecuencia.setForeground(recursos.Color.INK);
        pFrec.add(lblFrec);
        pFrec.add(Box.createVerticalStrut(6));
        pFrec.add(campoFrecuencia);

        JPanel pInicio = new JPanel();
        pInicio.setOpaque(false);
        pInicio.setLayout(new BoxLayout(pInicio, BoxLayout.Y_AXIS));
        JLabel lblInicio = new JLabel("FECHA DE INICIO");
        lblInicio.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblInicio.setForeground(recursos.Color.MUTED);

        JPanel dateWrapper = new JPanel(new BorderLayout(6, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(new Color(226, 232, 240));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
            }
        };
        dateWrapper.setOpaque(false);
        dateWrapper.setBorder(new EmptyBorder(8, 10, 8, 10));

        campoFechaInicio = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        campoFechaInicio.setBorder(null);
        campoFechaInicio.setOpaque(false);
        campoFechaInicio.setFont(CargadorFuentes.cargar(12f));
        campoFechaInicio.setForeground(recursos.Color.INK);
        JLabel lblCalIcon = new JLabel("📅");
        lblCalIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        dateWrapper.add(campoFechaInicio, BorderLayout.CENTER);
        dateWrapper.add(lblCalIcon, BorderLayout.EAST);

        pInicio.add(lblInicio);
        pInicio.add(Box.createVerticalStrut(6));
        pInicio.add(dateWrapper);

        JPanel pVig = new JPanel();
        pVig.setOpaque(false);
        pVig.setLayout(new BoxLayout(pVig, BoxLayout.Y_AXIS));
        JLabel lblVig = new JLabel("VIGENCIA (DÍAS)");
        lblVig.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblVig.setForeground(recursos.Color.MUTED);
        campoVigenciaDias = new JTextField("7");
        campoVigenciaDias.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campoVigenciaDias.setFont(CargadorFuentes.cargar(12f));
        campoVigenciaDias.setForeground(recursos.Color.INK);
        pVig.add(lblVig);
        pVig.add(Box.createVerticalStrut(6));
        pVig.add(campoVigenciaDias);

        gridTres.add(pFrec);
        gridTres.add(pInicio);
        gridTres.add(pVig);
        bodyPanel.add(gridTres);
        bodyPanel.add(Box.createVerticalStrut(14));

        // INDICACIONES PARA EL DUEÑO
        JLabel lblInd = new JLabel("INDICACIONES PARA EL DUEÑO (opcional)");
        lblInd.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblInd.setForeground(recursos.Color.MUTED);
        lblInd.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPanel.add(lblInd);
        bodyPanel.add(Box.createVerticalStrut(6));

        areaIndicaciones = new PlaceHolderTextArea("Ej: Administrar con alimento para evitar malestar gástrico...", 4, 20);
        areaIndicaciones.setFont(CargadorFuentes.cargar(12f));
        areaIndicaciones.setForeground(recursos.Color.INK);
        areaIndicaciones.setLineWrap(true);
        areaIndicaciones.setWrapStyleWord(true);

        JScrollPane scrollInd = new JScrollPane(areaIndicaciones);
        scrollInd.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
        scrollInd.getViewport().setBackground(Color.WHITE);
        scrollInd.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollInd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        scrollInd.setPreferredSize(new Dimension(0, 100));
        bodyPanel.add(scrollInd);

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
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnConfirmar = new JButton("💊 Confirmar receta") {
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

        btnConfirmar.setBackground(recursos.Color.PRIMARY);
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
                btnConfirmar.setBackground(recursos.Color.PRIMARY_DEEP);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnConfirmar.setBackground(recursos.Color.PRIMARY);
            }
        });
        btnConfirmar.addActionListener(e -> intentarConfirmar());

        footerPanel.add(btnCancelar);
        footerPanel.add(btnConfirmar);

        panelFondo.add(footerPanel, BorderLayout.SOUTH);

        add(panelFondo, BorderLayout.CENTER);

        // Cargar catálogo e inicializar el buscador reactivo
        inicializarCatalogo();
    }

    private void inicializarCatalogo() {
        ArrayList<Medicamento> catalogo = controlador.getVeterinaria().getCatalogoMedicamentos();

        // Llenar combo inicialmente
        refrescarCombo(catalogo);

        // Buscador reactivo
        txtBuscarMed.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
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
                String query = txtBuscarMed.getText().trim().toLowerCase();
                if (query.equals("escribí para buscar medicamento...")) {
                    query = "";
                }
                ArrayList<Medicamento> filtrado = new ArrayList<>();
                for (Medicamento m : catalogo) {
                    if (m.getNombreMedicamento().toLowerCase().contains(query)
                            || m.getCodigoSenasa().toLowerCase().contains(query)
                            || m.getSubtitulo().toLowerCase().contains(query)) {
                        filtrado.add(m);
                    }
                }
                refrescarCombo(filtrado);
            }
        });

        actualizarVisibilidad();
    }

    private void refrescarCombo(List<Medicamento> items) {
        comboMedicamento.removeAllItems();
        for (Medicamento m : items) {
            comboMedicamento.addItem(m);
        }
    }

    private void actualizarVisibilidad() {
        CardLayout cl = (CardLayout) panelDelCatalogo.getParent().getLayout();
        if (radioCatalogo.isSelected()) {
            cl.show(panelDelCatalogo.getParent(), "catalogo");
        } else {
            cl.show(panelDelCatalogo.getParent(), "magistral");
        }
    }

    private void intentarConfirmar() {
        lblError.setText(" ");
        String nombreMed;
        String codigoSenasa;
        boolean esMagistral = radioMagistral.isSelected();

        if (esMagistral) {
            nombreMed = txtMedMagistral.getText().trim();
            if (nombreMed.isEmpty() || nombreMed.equals("Ej: Amoxicilina suspensión especial")) {
                lblError.setText("Por favor ingrese el nombre del medicamento magistral.");
                return;
            }
            // Generar código interno
            codigoSenasa = "MAG-" + String.format("%04d", (int) (Math.random() * 10000));
        } else {
            Medicamento sel = (Medicamento) comboMedicamento.getSelectedItem();
            if (sel == null) {
                lblError.setText("Por favor seleccione un medicamento del catálogo.");
                return;
            }
            nombreMed = sel.getNombreMedicamento();
            codigoSenasa = sel.getCodigoSenasa();
        }

        String dosis = campoDosis.getText().trim();
        if (dosis.isEmpty() || dosis.equals("Ej: 1 comprimido")) {
            lblError.setText("Por favor ingrese la dosis.");
            return;
        }

        String via = campoVia.getText().trim();
        if (via.isEmpty()) {
            lblError.setText("Por favor ingrese la vía de administración.");
            return;
        }

        String frecuencia = campoFrecuencia.getText().trim();
        if (frecuencia.isEmpty()) {
            lblError.setText("Por favor ingrese la frecuencia.");
            return;
        }

        LocalDate fechaInicio;
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fechaInicio = LocalDate.parse(campoFechaInicio.getText().trim(), fmt);
        } catch (DateTimeParseException ex) {
            lblError.setText("Fecha de inicio inválida. Use el formato dd/MM/yyyy.");
            return;
        }

        int vigencia;
        try {
            vigencia = Integer.parseInt(campoVigenciaDias.getText().trim());
            if (vigencia <= 0) {
                lblError.setText("La vigencia en días debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException ex) {
            lblError.setText("Vigencia inválida. Ingrese un número de días entero.");
            return;
        }

        String indicaciones = areaIndicaciones.getText().trim();
        if (indicaciones.equals("Ej: Administrar con alimento para evitar malestar gástrico...")) {
            indicaciones = "";
        }

        // Guardar receta
        Medicamento med;
        if (esMagistral) {
            med = new Medicamento(codigoSenasa, nombreMed, "Magistral");
        } else {
            Medicamento sel = (Medicamento) comboMedicamento.getSelectedItem();
            med = sel;
        }

        Prescripcion receta = new Prescripcion(
                med, dosis, via, frecuencia, fechaInicio, vigencia, indicaciones, esMagistral
        );

        controlador.recetarMedicamento(animal, receta);
        recetado = true;

        JOptionPane.showMessageDialog(this, "Medicamento recetado con éxito.");
        dispose();
    }

    // ==========================================================
    //  PLACEHOLDER CUSTOM COMPONENTS HELPERS
    // ==========================================================
    private static class PlaceHolderTextField extends JTextField {

        private final String placeholder;

        public PlaceHolderTextField(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(148, 163, 184));
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
                Insets insets = getInsets();
                FontMetrics fm = g2.getFontMetrics();
                int y = (getHeight() - insets.top - insets.bottom - fm.getHeight()) / 2 + fm.getAscent() + insets.top;
                g2.drawString(placeholder, insets.left, y);
                g2.dispose();
            }
        }
    }

    private static class PlaceHolderTextArea extends JTextArea {

        private final String placeholder;

        public PlaceHolderTextArea(String placeholder, int rows, int columns) {
            super(rows, columns);
            this.placeholder = placeholder;
            setBorder(new EmptyBorder(8, 10, 8, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(148, 163, 184));
                g2.setFont(getFont().deriveFont(Font.ITALIC));
                Insets insets = getInsets();
                g2.drawString(placeholder, insets.left + 2, insets.top + g2.getFontMetrics().getAscent());
                g2.dispose();
            }
        }
    }
}
