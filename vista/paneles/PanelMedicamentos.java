package vista.paneles;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.border.*;
import modelo.Medicamento;
import modelo.Vacuna;
import recursos.CargadorFuentes;
import recursos.Color;
import vista.dialogos.DialogoAgregarMedicamento;

public class PanelMedicamentos extends JPanel {

    // Radios de la "tabla visual" (encabezado + listado) para ambos modos.
    private static final int RADIO_LISTADO = 16;
    private static final int RADIO_HEADER = 14;

    private final ControladorVeterinaria controlador;
    private final Map<String, Boolean> estadosCatalogo;

    private JPanel panelTabla;
    private JTextField campoBusqueda;
    private JButton btnTodos;
    private JButton btnActivos;
    private JButton btnInactivos;
    private JButton btnVistaMedicamentos;
    private JButton btnVistaVacunas;
    private JButton btnAgregar;

    private JLabel lblHeaderMedicamentos;
    private JLabel lblHeaderVacunas;
    private JLabel lblSidebarMedicamentos;
    private JLabel lblSidebarVacunas;
    private JLabel lblStatTotal;
    private JLabel lblStatActivos;
    private JLabel lblStatSinStock;

    private boolean vistaVacunas = false;
    private String filtroEstado = "Todos";

    public PanelMedicamentos(ControladorVeterinaria controlador) {
        this.controlador = controlador;
        this.estadosCatalogo = new HashMap<>();
        construir();
        actualizar();
    }

    private void construir() {
        setLayout(new BorderLayout(0, 14));
        setOpaque(true);
        setBackground(Color.BG);
        setBorder(new EmptyBorder(8, 18, 14, 18));

        add(crearHeader(), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);
    }

    private JPanel crearHeader() {
        JPanel panelHeader = new JPanel(new BorderLayout(16, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new java.awt.Color(6, 139, 132));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        panelHeader.setOpaque(false);
        panelHeader.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel panelTitulos = new JPanel();
        panelTitulos.setOpaque(false);
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Catálogo de Productos");
        lblTitulo.setFont(CargadorFuentes.cargar(18f).deriveFont(Font.BOLD));
        lblTitulo.setForeground(Color.SURFACE);

        JLabel lblSubtitulo = new JLabel(controlador.getVeterinaria().getNombreNegocio() + " · Portal Clínico — Administración");
        lblSubtitulo.setFont(CargadorFuentes.cargar(11f));
        lblSubtitulo.setForeground(new java.awt.Color(205, 246, 239));

        panelTitulos.add(lblTitulo);
        panelTitulos.add(Box.createVerticalStrut(2));
        panelTitulos.add(lblSubtitulo);
        panelHeader.add(panelTitulos, BorderLayout.WEST);

        JPanel panelBadges = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBadges.setOpaque(false);

        lblHeaderMedicamentos = crearBadgeHeader("0 medicamentos", new java.awt.Color(18, 154, 143));
        lblHeaderVacunas = crearBadgeHeader("0 vacunas", new java.awt.Color(21, 160, 149));

        panelBadges.add(lblHeaderMedicamentos);
        panelBadges.add(lblHeaderVacunas);
        panelHeader.add(panelBadges, BorderLayout.EAST);

        return panelHeader;
    }

    private JPanel crearContenido() {
        JPanel panelCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(Color.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
            }
        };
        panelCard.setOpaque(false);
        panelCard.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel panelBody = new JPanel(new BorderLayout());
        panelBody.setOpaque(false);
        panelBody.setBorder(new EmptyBorder(12, 12, 12, 12));

        panelBody.add(crearSidebar(), BorderLayout.WEST);
        panelBody.add(crearMain(), BorderLayout.CENTER);

        panelCard.add(panelBody, BorderLayout.CENTER);
        return panelCard;
    }

    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setOpaque(false);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(4, 2, 4, 12));
        sidebar.setPreferredSize(new Dimension(190, 0));

        JLabel lblCatalogo = new JLabel("CATÁLOGO");
        lblCatalogo.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblCatalogo.setForeground(new java.awt.Color(148, 163, 184));
        lblCatalogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblCatalogo);
        sidebar.add(Box.createVerticalStrut(12));

        btnVistaMedicamentos = crearBotonSidebar("Medicamentos", true);
        btnVistaMedicamentos.addActionListener(e -> {
            vistaVacunas = false;
            actualizar();
        });
        lblSidebarMedicamentos = crearContadorSidebar();
        JPanel filaMedicamentos = armarFilaSidebar(btnVistaMedicamentos, lblSidebarMedicamentos);
        filaMedicamentos.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(filaMedicamentos);
        sidebar.add(Box.createVerticalStrut(8));

        btnVistaVacunas = crearBotonSidebar("Vacunas", false);
        btnVistaVacunas.addActionListener(e -> {
            vistaVacunas = true;
            actualizar();
        });
        lblSidebarVacunas = crearContadorSidebar();
        JPanel filaVacunas = armarFilaSidebar(btnVistaVacunas, lblSidebarVacunas);
        filaVacunas.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(filaVacunas);

        sidebar.add(Box.createVerticalStrut(24));

        JSeparator sep = new JSeparator();
        sep.setForeground(Color.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(16));

        JLabel lblSistema = new JLabel("SISTEMA");
        lblSistema.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblSistema.setForeground(new java.awt.Color(148, 163, 184));
        lblSistema.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblSistema);
        sidebar.add(Box.createVerticalStrut(8));

        JLabel itemProveedores = crearItemSistema("➜  Proveedores");
        itemProveedores.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(itemProveedores);
        sidebar.add(Box.createVerticalStrut(8));
        JLabel itemImportar = crearItemSistema("➜  Importar catálogo");
        itemImportar.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(itemImportar);
        sidebar.add(Box.createVerticalStrut(8));
        JLabel itemHistorial = crearItemSistema("➜  Historial de cambios");
        itemHistorial.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(itemHistorial);
        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JPanel crearMain() {
        JPanel panelMain = new JPanel(new BorderLayout(0, 12));
        panelMain.setOpaque(false);
        panelMain.setBorder(new MatteBorder(0, 1, 0, 0, Color.BORDER));

        JPanel panelControles = new JPanel(new BorderLayout(8, 0));
        panelControles.setOpaque(false);
        panelControles.setBorder(new EmptyBorder(8, 12, 8, 12));

        campoBusqueda = crearCampoBusqueda();
        panelControles.add(campoBusqueda, BorderLayout.CENTER);

        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelDerecha.setOpaque(false);

        btnTodos = crearBotonFiltro("Todos", true);
        btnActivos = crearBotonFiltro("Activos", false);
        btnInactivos = crearBotonFiltro("Inactivos", false);

        btnTodos.addActionListener(e -> cambiarFiltroEstado("Todos"));
        btnActivos.addActionListener(e -> cambiarFiltroEstado("Activos"));
        btnInactivos.addActionListener(e -> cambiarFiltroEstado("Inactivos"));

        panelDerecha.add(btnTodos);
        panelDerecha.add(btnActivos);
        panelDerecha.add(btnInactivos);

        btnAgregar = crearBotonAgregar();
        panelDerecha.add(btnAgregar);

        panelControles.add(panelDerecha, BorderLayout.EAST);

        JPanel wrapperControles = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(Color.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
        wrapperControles.setOpaque(false);
        wrapperControles.setBorder(new EmptyBorder(0, 14, 0, 8));
        wrapperControles.add(panelControles, BorderLayout.CENTER);
        panelMain.add(wrapperControles, BorderLayout.NORTH);

        panelTabla = new PanelTablaScrollable();
        panelTabla.setOpaque(false);
        panelTabla.setLayout(new BoxLayout(panelTabla, BoxLayout.Y_AXIS));

        JScrollPane scrollTabla = new JScrollPane(panelTabla);
        scrollTabla.setBorder(new EmptyBorder(6, 6, 6, 6));
        scrollTabla.setOpaque(false);
        scrollTabla.getViewport().setOpaque(false);
        scrollTabla.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTabla.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        JScrollBar bar = scrollTabla.getVerticalScrollBar();
        bar.setUI(new vista.componentes.ModernScrollBarUI());
        bar.setPreferredSize(new Dimension(8, 0));
        bar.setUnitIncrement(16);

        JPanel wrapperTabla = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                java.awt.Color fondoTabla = new java.awt.Color(241, 245, 249);
                g2.setColor(fondoTabla);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new java.awt.Color(206, 217, 231));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };
        wrapperTabla.setOpaque(false);
        wrapperTabla.setBorder(new EmptyBorder(8, 14, 6, 8));
        wrapperTabla.add(scrollTabla, BorderLayout.CENTER);
        panelMain.add(wrapperTabla, BorderLayout.CENTER);

        JPanel panelStats = new JPanel(new GridLayout(1, 3, 12, 0));
        panelStats.setOpaque(false);
        panelStats.setBorder(new EmptyBorder(2, 14, 0, 0));

        lblStatTotal = new JLabel("0", SwingConstants.RIGHT);
        lblStatActivos = new JLabel("0", SwingConstants.RIGHT);
        lblStatSinStock = new JLabel("0", SwingConstants.RIGHT);

        panelStats.add(crearCardStat("Total en catálogo", lblStatTotal, new java.awt.Color(239, 246, 255), new java.awt.Color(29, 78, 216)));
        panelStats.add(crearCardStat("Activos", lblStatActivos, new java.awt.Color(236, 253, 245), new java.awt.Color(5, 150, 105)));
        panelStats.add(crearCardStat("Sin stock", lblStatSinStock, new java.awt.Color(254, 242, 242), new java.awt.Color(220, 38, 38)));
        panelMain.add(panelStats, BorderLayout.SOUTH);

        return panelMain;
    }

    public void actualizar() {
        ArrayList<Medicamento> catalogo = controlador.getVeterinaria().getCatalogoMedicamentos();
        ArrayList<Medicamento> medicamentos = new ArrayList<>();
        ArrayList<Medicamento> vacunas = new ArrayList<>();

        for (Medicamento m : catalogo) {
            if (m instanceof Vacuna) {
                vacunas.add(m);
            } else {
                medicamentos.add(m);
            }
            asegurarEstadoInicial(m);
        }

        int totalMedicamentos = medicamentos.size();
        int totalVacunas = vacunas.size();
        lblHeaderMedicamentos.setText(totalMedicamentos + " medicamentos");
        lblHeaderVacunas.setText(totalVacunas + " vacunas");
        lblSidebarMedicamentos.setText(String.valueOf(totalMedicamentos));
        lblSidebarVacunas.setText(String.valueOf(totalVacunas));

        refrescarBotonesSidebar();
        refrescarBotonesFiltro();
        btnAgregar.setText(vistaVacunas ? "+ Agregar vacuna" : "+ Agregar medicamento");
        campoBusqueda.setToolTipText(vistaVacunas ? "Buscar vacunas" : "Buscar medicamentos");

        ArrayList<Medicamento> base = vistaVacunas ? vacunas : medicamentos;
        ArrayList<Medicamento> visibles = filtrarCatalogo(base);

        renderTabla(visibles);
        actualizarStats(visibles);
    }

    private void renderTabla(ArrayList<Medicamento> visibles) {
        panelTabla.removeAll();

        String[] columnas = vistaVacunas
                ? new String[]{"CÓDIGO", "NOMBRE", "LABORATORIO", "ESPECIE", "VIGENCIA", "STOCK", "ESTADO"}
                : new String[]{"CÓDIGO", "NOMBRE", "CATEGORÍA", "PRESENTACIÓN", "STOCK", "ESTADO"};

        panelTabla.add(crearHeaderTabla(columnas));
        panelTabla.add(Box.createVerticalStrut(4));

        if (visibles.isEmpty()) {
            JLabel vacio = new JLabel("No hay resultados para los filtros seleccionados", SwingConstants.CENTER);
            vacio.setFont(CargadorFuentes.cargar(12f));
            vacio.setForeground(Color.MUTED);
            vacio.setBorder(new EmptyBorder(30, 0, 30, 0));
            panelTabla.add(vacio);
        } else {
            for (int i = 0; i < visibles.size(); i++) {
                Medicamento med = visibles.get(i);
                panelTabla.add(crearFilaMedicamento(med));
                if (i < visibles.size() - 1) {
                    panelTabla.add(new JSeparator(SwingConstants.HORIZONTAL));
                }
            }
        }

        panelTabla.revalidate();
        panelTabla.repaint();
    }

    private JPanel crearHeaderTabla(String[] columnas) {
        JPanel header = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                java.awt.Color bgHeader = new java.awt.Color(248, 250, 252);
                g2.setColor(bgHeader);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + RADIO_LISTADO, RADIO_HEADER, RADIO_HEADER);
                // Borde más marcado para separar visualmente el encabezado del fondo gris.
                g2.setColor(new java.awt.Color(188, 200, 216));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() + RADIO_LISTADO - 1, RADIO_HEADER, RADIO_HEADER);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(10, 10, 8, 10));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        double[] pesos = pesosColumnas();
        for (int i = 0; i < columnas.length; i++) {
            String col = columnas[i];
            JLabel lbl = new JLabel(col);
            lbl.setHorizontalAlignment(SwingConstants.LEFT);
            lbl.setFont(CargadorFuentes.cargar(10f).deriveFont(Font.BOLD));
            lbl.setForeground(new java.awt.Color(148, 163, 184));
            agregarCelda(header, lbl, i, pesos[i], GridBagConstraints.WEST);
        }
        return header;
    }

    private JPanel crearFilaMedicamento(Medicamento med) {
        boolean activo = esActivo(med);
        JPanel fila = new JPanel(new GridBagLayout());
        fila.setOpaque(false);
        fila.setBorder(new EmptyBorder(10, 10, 10, 10));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

        double[] pesos = pesosColumnas();

        JLabel lblCodigo = crearChipTexto(med.getCodigoSenasa(), new java.awt.Color(241, 245, 249), new java.awt.Color(71, 85, 105), true);
        lblCodigo.setHorizontalAlignment(SwingConstants.LEFT);
        JLabel lblNombre = new JLabel(med.getNombreMedicamento());
        lblNombre.setFont(CargadorFuentes.cargar(13f).deriveFont(Font.BOLD));
        lblNombre.setForeground(activo ? Color.INK : Color.CAT_INACTIVO);
        lblNombre.setToolTipText(med.getNombreMedicamento());
        lblNombre.setText(truncar(med.getNombreMedicamento(), vistaVacunas ? 24 : 26));

        agregarCelda(fila, lblCodigo, 0, pesos[0], GridBagConstraints.WEST);
        agregarCelda(fila, lblNombre, 1, pesos[1], GridBagConstraints.WEST);

        if (vistaVacunas) {
            JLabel lblLab = crearTextoSecundario(truncar(obtenerLaboratorio(med), 12));
            JLabel lblEspecie = crearChipTexto(obtenerEspecieVacuna(med), new java.awt.Color(220, 252, 231), new java.awt.Color(5, 150, 105), false);
            JLabel lblVigencia = crearTextoSecundario(((Vacuna) med).getVigenciaDias() + " días");
            JLabel lblStock = crearChipStock(med);
            JToggleButton toggle = crearToggleEstado(med, activo, new java.awt.Color(139, 92, 246));

            agregarCelda(fila, lblLab, 2, pesos[2], GridBagConstraints.WEST);
            agregarCelda(fila, lblEspecie, 3, pesos[3], GridBagConstraints.WEST);
            agregarCelda(fila, lblVigencia, 4, pesos[4], GridBagConstraints.WEST);
            agregarCelda(fila, lblStock, 5, pesos[5], GridBagConstraints.WEST);
            agregarCelda(fila, toggle, 6, pesos[6], GridBagConstraints.WEST);
        } else {
            JLabel lblCategoria = crearChipTexto(truncar(obtenerCategoria(med), 18), colorCategoriaFondo(med), colorCategoriaTexto(med), false);
            JLabel lblPresentacion = crearTextoSecundario(obtenerPresentacion(med));
            JLabel lblStock = crearChipStock(med);
            JToggleButton toggle = crearToggleEstado(med, activo, Color.PRIMARY);

            agregarCelda(fila, lblCategoria, 2, pesos[2], GridBagConstraints.WEST);
            agregarCelda(fila, lblPresentacion, 3, pesos[3], GridBagConstraints.WEST);
            agregarCelda(fila, lblStock, 4, pesos[4], GridBagConstraints.WEST);
            agregarCelda(fila, toggle, 5, pesos[5], GridBagConstraints.WEST);
        }
        return fila;
    }

    private JLabel crearTextoSecundario(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(CargadorFuentes.cargar(12f));
        lbl.setForeground(new java.awt.Color(100, 116, 139));
        return lbl;
    }

    private JLabel crearChipTexto(String texto, java.awt.Color bg, java.awt.Color fg, boolean bold) {
        JLabel chip = new JLabel(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        chip.setOpaque(false);
        chip.setHorizontalAlignment(SwingConstants.LEFT);
        chip.setFont(CargadorFuentes.cargar(bold ? 11f : 10f).deriveFont(bold ? Font.BOLD : Font.PLAIN));
        chip.setForeground(fg);
        chip.setBorder(new EmptyBorder(3, 8, 3, 8));
        return chip;
    }

    private JLabel crearChipStock(Medicamento med) {
        int stock = obtenerStock(med);
        boolean sinStock = stock <= 0;
        String unidad = vistaVacunas ? "dosis" : obtenerUnidadMedicamento(med);
        String texto = sinStock ? "Sin stock" : stock + " " + unidad;

        java.awt.Color fondo = sinStock ? new java.awt.Color(254, 226, 226) : new java.awt.Color(220, 252, 231);
        java.awt.Color textoColor = sinStock ? new java.awt.Color(185, 28, 28) : new java.awt.Color(5, 150, 105);
        return crearChipTexto(texto, fondo, textoColor, true);
    }

    private JToggleButton crearToggleEstado(Medicamento med, boolean activo, java.awt.Color colorOn) {
        JToggleButton toggle = new JToggleButton();
        toggle.setSelected(activo);
        toggle.setPreferredSize(new Dimension(44, 22));
        toggle.setOpaque(false);
        toggle.setContentAreaFilled(false);
        toggle.setBorderPainted(false);
        toggle.setFocusPainted(false);
        toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        toggle.setUI(new javax.swing.plaf.basic.BasicToggleButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JToggleButton b = (JToggleButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = b.getWidth();
                int h = b.getHeight();
                int knob = h - 6;
                java.awt.Color bg = b.isSelected() ? colorOn : new java.awt.Color(226, 232, 240);

                g2.setColor(bg);
                g2.fillRoundRect(0, 0, w, h, h, h);
                g2.setColor(java.awt.Color.WHITE);
                int x = b.isSelected() ? (w - knob - 3) : 3;
                g2.fillOval(x, 3, knob, knob);
                g2.dispose();
            }
        });

        toggle.addActionListener(e -> {
            estadosCatalogo.put(claveEstado(med), toggle.isSelected());
            actualizar();
        });
        return toggle;
    }

    private void actualizarStats(ArrayList<Medicamento> visibles) {
        int activos = 0;
        int sinStock = 0;

        for (Medicamento m : visibles) {
            if (esActivo(m)) {
                activos++;
            }
            if (obtenerStock(m) <= 0) {
                sinStock++;
            }
        }

        lblStatTotal.setText(String.valueOf(visibles.size()));
        lblStatActivos.setText(String.valueOf(activos));
        lblStatSinStock.setText(String.valueOf(sinStock));
    }

    private ArrayList<Medicamento> filtrarCatalogo(ArrayList<Medicamento> listaBase) {
        ArrayList<Medicamento> filtrados = new ArrayList<>();
        String q = campoBusqueda != null ? campoBusqueda.getText().trim().toLowerCase() : "";

        for (Medicamento med : listaBase) {
            boolean coincideTexto = q.isEmpty()
                    || med.getNombreMedicamento().toLowerCase().contains(q)
                    || med.getCodigoSenasa().toLowerCase().contains(q)
                    || obtenerCategoria(med).toLowerCase().contains(q);
            if (!coincideTexto) {
                continue;
            }

            boolean activo = esActivo(med);
            if ("Activos".equals(filtroEstado) && !activo) {
                continue;
            }
            if ("Inactivos".equals(filtroEstado) && activo) {
                continue;
            }
            filtrados.add(med);
        }

        return filtrados;
    }

    private JPanel crearCardStat(String titulo, JLabel lblValor, java.awt.Color bg, java.awt.Color colorValor) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new java.awt.Color(
                        Math.max(0, bg.getRed() - 16),
                        Math.max(0, bg.getGreen() - 16),
                        Math.max(0, bg.getBlue() - 16)
                ));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(CargadorFuentes.cargar(12f));
        lblTitulo.setForeground(new java.awt.Color(71, 85, 105));

        lblValor.setFont(CargadorFuentes.cargar(20f).deriveFont(Font.BOLD));
        lblValor.setForeground(colorValor);

        card.setPreferredSize(new Dimension(0, 62));
        card.add(lblTitulo, BorderLayout.WEST);
        card.add(lblValor, BorderLayout.EAST);
        return card;
    }

    private JButton crearBotonSidebar(String texto, boolean activo) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (Boolean.TRUE.equals(getClientProperty("activo"))) {
                    g2.setColor(new java.awt.Color(235, 252, 247));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                    g2.setColor(new java.awt.Color(153, 246, 228));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };

        boton.putClientProperty("activo", activo);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFont(CargadorFuentes.cargar(14f).deriveFont(Font.BOLD));
        boton.setForeground(new java.awt.Color(30, 41, 59));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(150, 42));
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setOpaque(false);
        return boton;
    }

    private JPanel armarFilaSidebar(JButton boton, JLabel contador) {
        JPanel fila = new JPanel(new BorderLayout(6, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        fila.add(boton, BorderLayout.CENTER);
        fila.add(contador, BorderLayout.EAST);
        return fila;
    }

    private JLabel crearContadorSidebar() {
        JLabel lbl = new JLabel("0", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new java.awt.Color(226, 232, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lbl.setOpaque(false);
        lbl.setFont(CargadorFuentes.cargar(10f).deriveFont(Font.BOLD));
        lbl.setForeground(new java.awt.Color(71, 85, 105));
        lbl.setPreferredSize(new Dimension(24, 20));
        return lbl;
    }

    private JLabel crearBadgeHeader(String texto, java.awt.Color bg) {
        JLabel lbl = new JLabel(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lbl.setOpaque(false);
        lbl.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        lbl.setForeground(Color.SURFACE);
        lbl.setBorder(new EmptyBorder(6, 12, 6, 12));
        return lbl;
    }

    private JLabel crearItemSistema(String texto) {
        JLabel item = new JLabel(texto);
        item.setFont(CargadorFuentes.cargar(13f));
        item.setForeground(new java.awt.Color(148, 163, 184));
        return item;
    }

    private JTextField crearCampoBusqueda() {
        JTextField campo = new JTextField();
        campo.setPreferredSize(new Dimension(290, 36));
        campo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campo.setFont(CargadorFuentes.cargar(12f));
        campo.setForeground(Color.INK);
        campo.setText("");
        campo.setBackground(Color.SURFACE);
        campo.setToolTipText("Buscar por código, nombre o categoría");

        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (KeyEvent.VK_ESCAPE == e.getKeyCode()) {
                    campo.setText("");
                    actualizar();
                }
            }
        });

        campo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizar();
            }
        });

        return campo;
    }

    private JButton crearBotonFiltro(String texto, boolean activo) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (Boolean.TRUE.equals(getClientProperty("activo"))) {
                    g2.setColor(java.awt.Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(Color.BORDER);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                } else {
                    g2.setColor(new java.awt.Color(241, 245, 249));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.putClientProperty("activo", activo);
        btn.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        btn.setForeground(new java.awt.Color(71, 85, 105));
        int ancho = 72;
        if ("Activos".equals(texto)) {
            ancho = 86;
        } else if ("Inactivos".equals(texto)) {
            ancho = 96;
        }
        btn.setPreferredSize(new Dimension(ancho, 30));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBotonAgregar() {
        JButton btn = new JButton("+ Agregar medicamento") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(java.awt.Color.WHITE);
        btn.setFont(CargadorFuentes.cargar(13f).deriveFont(Font.BOLD));
        btn.setPreferredSize(new Dimension(220, 36));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> abrirDialogoAgregar());
        return btn;
    }

    private void cambiarFiltroEstado(String nuevoFiltro) {
        filtroEstado = nuevoFiltro;
        actualizar();
    }

    private void refrescarBotonesFiltro() {
        btnTodos.putClientProperty("activo", "Todos".equals(filtroEstado));
        btnActivos.putClientProperty("activo", "Activos".equals(filtroEstado));
        btnInactivos.putClientProperty("activo", "Inactivos".equals(filtroEstado));
        btnTodos.repaint();
        btnActivos.repaint();
        btnInactivos.repaint();
    }

    private void refrescarBotonesSidebar() {
        btnVistaMedicamentos.putClientProperty("activo", !vistaVacunas);
        btnVistaVacunas.putClientProperty("activo", vistaVacunas);
        btnVistaMedicamentos.repaint();
        btnVistaVacunas.repaint();
    }

    private String obtenerCategoria(Medicamento med) {
        if (med.getCategoria() != null && !med.getCategoria().trim().isEmpty()) {
            return med.getCategoria();
        }
        String nombre = med.getNombreMedicamento().toLowerCase();
        if (nombre.contains("flox") || nombre.contains("cilina") || nombre.contains("azol")) {
            return "Antibiótico";
        }
        if (nombre.contains("anti") || nombre.contains("cort") || nombre.contains("pred")) {
            return "Antiinflamatorio";
        }
        if (nombre.contains("omep") || nombre.contains("gastro")) {
            return "Gastroprotector";
        }
        return "General";
    }

    private java.awt.Color colorCategoriaFondo(Medicamento med) {
        String categoria = obtenerCategoria(med).toLowerCase();
        if (categoria.contains("antib")) {
            return new java.awt.Color(219, 234, 254);
        }
        if (categoria.contains("infl")) {
            return new java.awt.Color(255, 237, 213);
        }
        if (categoria.contains("gastro")) {
            return new java.awt.Color(204, 251, 241);
        }
        return new java.awt.Color(243, 244, 246);
    }

    private java.awt.Color colorCategoriaTexto(Medicamento med) {
        String categoria = obtenerCategoria(med).toLowerCase();
        if (categoria.contains("antib")) {
            return new java.awt.Color(29, 78, 216);
        }
        if (categoria.contains("infl")) {
            return new java.awt.Color(194, 65, 12);
        }
        if (categoria.contains("gastro")) {
            return new java.awt.Color(13, 148, 136);
        }
        return new java.awt.Color(55, 65, 81);
    }

    private String obtenerPresentacion(Medicamento med) {
        int hash = Math.abs(claveEstado(med).hashCode()) % 4;
        if (hash == 0) {
            return "Comprimidos";
        }
        if (hash == 1) {
            return "Solución oral";
        }
        if (hash == 2) {
            return "Cápsulas";
        }
        return "Suspensión";
    }

    private String obtenerLaboratorio(Medicamento med) {
        int hash = Math.abs(claveEstado(med).hashCode()) % 4;
        if (hash == 0) {
            return "Nobivac";
        }
        if (hash == 1) {
            return "Zoetis";
        }
        if (hash == 2) {
            return "Virbac";
        }
        return "Purevax";
    }

    private String obtenerEspecieVacuna(Medicamento med) {
        String nombre = med.getNombreMedicamento().toLowerCase();
        if (nombre.contains("feli") && nombre.contains("cani")) {
            return "Canina / Felina";
        }
        if (nombre.contains("feli")) {
            return "Felina";
        }
        if (nombre.contains("cani") || nombre.contains("rab")) {
            return "Canina";
        }
        return "Canina";
    }

    private int obtenerStock(Medicamento med) {
        int hash = Math.abs(claveEstado(med).hashCode()) % 7;
        if (hash == 0) {
            return 0;
        }
        if (vistaVacunas) {
            return 12 + hash * 6;
        }
        return 60 + hash * 30;
    }

    private String obtenerUnidadMedicamento(Medicamento med) {
        String p = obtenerPresentacion(med);
        if ("Solución oral".equals(p) || "Suspensión".equals(p)) {
            return "ml";
        }
        if ("Cápsulas".equals(p)) {
            return "cáps";
        }
        return "comp";
    }

    private void asegurarEstadoInicial(Medicamento med) {
        String key = claveEstado(med);
        if (!estadosCatalogo.containsKey(key)) {
            boolean activo = (Math.abs(key.hashCode()) % 5) != 0;
            estadosCatalogo.put(key, activo);
        }
    }

    private boolean esActivo(Medicamento med) {
        return estadosCatalogo.getOrDefault(claveEstado(med), true);
    }

    private String claveEstado(Medicamento med) {
        return med.getCodigoSenasa() + "|" + med.getNombreMedicamento();
    }

    private double[] pesosColumnas() {
        if (vistaVacunas) {
            return new double[]{0.14, 0.26, 0.13, 0.13, 0.14, 0.12, 0.08};
        }
        return new double[]{0.13, 0.23, 0.20, 0.21, 0.12, 0.11};
    }

    private int[] anchosColumnasBase() {
        if (vistaVacunas) {
            return new int[]{108, 290, 90, 90, 96, 92, 74};
        }
        return new int[]{92, 308, 142, 134, 94, 74};
    }

    private void agregarCelda(JPanel fila, Component comp, int col, double peso, int anchor) {
        int[] anchos = anchosColumnasBase();
        int anchoColumna = (col >= 0 && col < anchos.length) ? anchos[col] : 120;
        int ultimaColumna = anchos.length - 1;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, col == ultimaColumna ? 0 : 8);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(0, anchor == GridBagConstraints.CENTER ? 0 : 2, 0, 0));
        Dimension dimCol = new Dimension(anchoColumna, 30);
        contenedor.setPreferredSize(dimCol);
        contenedor.setMinimumSize(dimCol);
        contenedor.setMaximumSize(new Dimension(anchoColumna, Integer.MAX_VALUE));

        if (anchor == GridBagConstraints.CENTER) {
            JPanel center = new JPanel(new GridBagLayout());
            center.setOpaque(false);
            center.add(comp);
            contenedor.add(center, BorderLayout.CENTER);
        } else {
            JPanel left = new JPanel(new BorderLayout());
            left.setOpaque(false);
            left.add(comp, BorderLayout.WEST);
            contenedor.add(left, BorderLayout.CENTER);
        }

        fila.add(contenedor, gbc);
    }

    private String truncar(String texto, int max) {
        if (texto == null || texto.length() <= max) {
            return texto;
        }
        if (max <= 3) {
            return texto.substring(0, Math.max(0, max));
        }
        return texto.substring(0, max - 3) + "...";
    }

    private static class PanelTablaScrollable extends JPanel implements Scrollable {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.SURFACE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIO_LISTADO, RADIO_LISTADO);
            g2.setColor(new java.awt.Color(226, 232, 240));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIO_LISTADO, RADIO_LISTADO);
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 20;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return Math.max(visibleRect.height - 20, 40);
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

    private void abrirDialogoAgregar() {
        DialogoAgregarMedicamento dialogo = new DialogoAgregarMedicamento(
                SwingUtilities.getWindowAncestor(this), controlador
        );
        dialogo.setVisible(true);
        if (dialogo.isAgregado()) {
            actualizar();
        }
    }
}
