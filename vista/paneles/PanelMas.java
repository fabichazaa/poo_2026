package vista.paneles;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.*;
import modelo.ComprobanteTurno;
import modelo.Turno;
import recursos.CargadorFuentes;
import vista.dialogos.DialogoComprobante;
import vista.dialogos.DialogoSeleccionarTurno;

public class PanelMas extends JPanel {

    private final ControladorVeterinaria controlador;
    private JLabel lblNombre;
    private JLabel lblVets;
    private JLabel lblClientes;
    private JLabel lblTurnos;
    private JLabel lblEnAdopcion;
    private JLabel lblNotas;
    private PanelGestionPersonas panelGestion;

    private JPanel panelEstadisticas;
    private JPanel panelExportar;
    private JPanel panelTextoExportar;
    private JPanel panelBotonesExportar;
    private JPanel panelAcercaDe;
    private JPanel panelCuerpo;
    private JScrollPane scroll;
    private JLabel lblAcercaTitulo;
    private JTextPane txtAcercaDesc;

    private static final Color COLOR_TOPE = recursos.Color.SLATE_600;

    public PanelMas(ControladorVeterinaria controlador) {
        this.controlador = controlador;
        construir();

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                reajustarLayout();
            }
        });
    }

    private void construir() {
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);
        setBorder(new EmptyBorder(10, 25, 15, 25));

        JPanel panelHeader = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(COLOR_TOPE);
                g.fillRect(0, 0, getWidth(), 4);
            }
        };
        panelHeader.setBackground(Color.WHITE);
        panelHeader.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(recursos.Color.BORDER, 1, true),
            new EmptyBorder(16, 22, 16, 22)
        ));
        JLabel lblTitulo = new JLabel("Configuración y Más");
        lblTitulo.setFont(CargadorFuentes.cargar(16f));
        lblTitulo.setForeground(recursos.Color.INK);
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        add(panelHeader, BorderLayout.NORTH);

        panelCuerpo = new ScrollablePanel();
        panelCuerpo.setOpaque(false);
        panelCuerpo.setLayout(new BoxLayout(panelCuerpo, BoxLayout.Y_AXIS));

        panelGestion = new PanelGestionPersonas(controlador);
        panelCuerpo.add(panelGestion);
        panelCuerpo.add(Box.createVerticalStrut(15));
        panelCuerpo.add(crearPanelEstadisticas());
        panelCuerpo.add(Box.createVerticalStrut(15));
        panelCuerpo.add(crearPanelExportar());
        panelCuerpo.add(Box.createVerticalStrut(15));
        panelCuerpo.add(crearPanelAcercaDe());

        scroll = new JScrollPane(panelCuerpo);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(recursos.Color.BG);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearPanelEstadisticas() {
        panelEstadisticas = new JPanel(new GridLayout(2, 3, 12, 12));
        panelEstadisticas.setBackground(Color.WHITE);
        panelEstadisticas.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(recursos.Color.BORDER, 1, true),
            new EmptyBorder(20, 22, 20, 22)
        ));
        panelEstadisticas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        lblNombre = new JLabel();
        lblVets = new JLabel();
        lblClientes = new JLabel();
        lblTurnos = new JLabel();
        lblEnAdopcion = new JLabel();
        lblNotas = new JLabel();

        panelEstadisticas.add(crearStat("Veterinaria", lblNombre, recursos.Color.PRIMARY));
        panelEstadisticas.add(crearStat("Veterinarios", lblVets, new Color(99, 102, 241)));
        panelEstadisticas.add(crearStat("Clientes", lblClientes, new Color(124, 58, 237)));
        panelEstadisticas.add(crearStat("Turnos", lblTurnos, recursos.Color.PENDING));
        panelEstadisticas.add(crearStat("En adopción", lblEnAdopcion, new Color(236, 72, 153)));
        panelEstadisticas.add(crearStat("Notas activas", lblNotas, recursos.Color.SUCCESS));

        return panelEstadisticas;
    }

    private JPanel crearStat(String titulo, JLabel lblValor, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(recursos.Color.CANVAS_GENERAL);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 1, true),
            new EmptyBorder(12, 14, 12, 14)
        ));
        lblValor.setFont(CargadorFuentes.cargar(22f));
        lblValor.setForeground(color);
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(CargadorFuentes.cargar(11f));
        lblTit.setForeground(new Color(100, 116, 139));
        lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblValor);
        panel.add(Box.createVerticalStrut(2));
        panel.add(lblTit);
        return panel;
    }

    private JPanel crearPanelExportar() {
        panelExportar = new JPanel(new BorderLayout(15, 10));
        panelExportar.setBackground(Color.WHITE);
        panelExportar.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(recursos.Color.BORDER, 1, true),
            new EmptyBorder(18, 22, 18, 22)
        ));
        panelExportar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        panelTextoExportar = new JPanel();
        panelTextoExportar.setOpaque(false);
        panelTextoExportar.setLayout(new BoxLayout(panelTextoExportar, BoxLayout.Y_AXIS));
        JLabel lblTit = new JLabel("Exportar comprobante de atención");
        lblTit.setFont(CargadorFuentes.cargar(13f));
        lblTit.setForeground(recursos.Color.INK);
        lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblDesc = new JLabel("Visualizá o guardá un comprobante a partir de un turno existente.");
        lblDesc.setFont(CargadorFuentes.cargar(11f));
        lblDesc.setForeground(new Color(100, 116, 139));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTextoExportar.add(lblTit);
        panelTextoExportar.add(Box.createVerticalStrut(2));
        panelTextoExportar.add(lblDesc);

        panelBotonesExportar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotonesExportar.setOpaque(false);
        JButton btnVer = new JButton("Ver comprobante") {
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
        btnVer.setBackground(recursos.Color.PRIMARY);
        btnVer.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        btnVer.setForeground(Color.WHITE);
        btnVer.setContentAreaFilled(false);
        btnVer.setBorderPainted(false);
        btnVer.setFocusPainted(false);
        btnVer.setPreferredSize(new Dimension(150, 36));
        btnVer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnVer.setBackground(recursos.Color.PRIMARY_DEEP);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnVer.setBackground(recursos.Color.PRIMARY);
            }
        });
        btnVer.addActionListener(e -> verComprobante());

        JButton btnGuardar = new JButton("Guardar en archivo…") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(getForeground());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnGuardar.setBackground(Color.WHITE);
        btnGuardar.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        btnGuardar.setForeground(recursos.Color.PRIMARY);
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(170, 36));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnGuardar.setBackground(recursos.Color.PRIMARY_LIGHT);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnGuardar.setBackground(Color.WHITE);
            }
        });
        btnGuardar.addActionListener(e -> guardarComprobante());

        panelBotonesExportar.add(btnVer);
        panelBotonesExportar.add(btnGuardar);

        panelExportar.add(panelTextoExportar, BorderLayout.CENTER);
        panelExportar.add(panelBotonesExportar, BorderLayout.EAST);
        return panelExportar;
    }

    private JPanel crearPanelAcercaDe() {
        panelAcercaDe = new JPanel();
        panelAcercaDe.setBackground(Color.WHITE);
        panelAcercaDe.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(recursos.Color.BORDER, 1, true),
            new EmptyBorder(18, 22, 18, 22)
        ));
        panelAcercaDe.setLayout(new BoxLayout(panelAcercaDe, BoxLayout.Y_AXIS));
        panelAcercaDe.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        lblAcercaTitulo = new JLabel("Acerca de Happy Paws");
        lblAcercaTitulo.setFont(CargadorFuentes.cargar(14f));
        lblAcercaTitulo.setForeground(recursos.Color.INK);
        lblAcercaTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelAcercaDe.add(lblAcercaTitulo);

        panelAcercaDe.add(Box.createVerticalStrut(8));

        txtAcercaDesc = new JTextPane();
        txtAcercaDesc.setContentType("text/html");
        txtAcercaDesc.setText("<html><body style='font-family:sans-serif; font-size:12px; color:#475569; margin:0;'>"
            + "<b>Happy Paws</b> — Sistema de Gestión Veterinaria.<br>"
            + "Trabajo Integrador de Programación Orientada a Objetos.<br>"
            + "Tecnicatura Universitaria en Desarrollo de Software.<br><br>"
            + "Implementa una jerarquía de herencia con clases abstractas, polimorfismo, "
            + "composición, agregación, colecciones y una clase de reporte (ComprobanteTurno) "
            + "que delega en los objetos del modelo."
            + "</body></html>");
        txtAcercaDesc.setEditable(false);
        txtAcercaDesc.setOpaque(false);
        txtAcercaDesc.setFocusable(false);
        txtAcercaDesc.setBorder(null);
        txtAcercaDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelAcercaDe.add(txtAcercaDesc);
        return panelAcercaDe;
    }

    private void reajustarLayout() {
        if (scroll == null) return;
        int width = scroll.getViewport().getWidth();
        if (width <= 0) {
            width = getWidth() - 50;
        }
        if (width <= 0) {
            width = 1000; // Default wide mode
        }

        // 1. Estadísticas
        if (width < 650) {
            panelEstadisticas.setLayout(new GridLayout(3, 2, 10, 10));
            panelEstadisticas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        } else {
            panelEstadisticas.setLayout(new GridLayout(2, 3, 12, 12));
            panelEstadisticas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        }

        // 2. Exportación
        panelExportar.remove(panelTextoExportar);
        panelExportar.remove(panelBotonesExportar);
        if (width < 650) {
            panelExportar.add(panelTextoExportar, BorderLayout.CENTER);
            panelExportar.add(panelBotonesExportar, BorderLayout.SOUTH);
            panelExportar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 145));
        } else {
            panelExportar.add(panelTextoExportar, BorderLayout.CENTER);
            panelExportar.add(panelBotonesExportar, BorderLayout.EAST);
            panelExportar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        }

        // 3. Acerca De
        if (txtAcercaDesc != null && lblAcercaTitulo != null) {
            int paddingX = 22 * 2;
            int borderX = 1 * 2;
            int txtWidth = width - paddingX - borderX - 10;
            if (txtWidth > 0) {
                txtAcercaDesc.setSize(new Dimension(txtWidth, Integer.MAX_VALUE));
            }
            int prefHeight = txtAcercaDesc.getPreferredSize().height;
            int titleHeight = lblAcercaTitulo.getPreferredSize().height;
            int strutHeight = 8;
            int paddingY = 18 * 2;
            int totalHeight = titleHeight + strutHeight + prefHeight + paddingY + 12; // 12px extra margin for safety
            panelAcercaDe.setPreferredSize(new Dimension(Integer.MAX_VALUE, totalHeight));
            panelAcercaDe.setMaximumSize(new Dimension(Integer.MAX_VALUE, totalHeight));
        } else {
            panelAcercaDe.setMaximumSize(new Dimension(Integer.MAX_VALUE, width < 650 ? 320 : 240));
        }

        panelEstadisticas.revalidate();
        panelExportar.revalidate();
        panelAcercaDe.revalidate();
        panelCuerpo.revalidate();
        panelCuerpo.repaint();
    }

    public void actualizar() {
        lblNombre.setText(controlador.getVeterinaria().getNombreNegocio());
        lblVets.setText(String.valueOf(controlador.getVeterinaria().getListaVeterinarios().size()));
        lblClientes.setText(String.valueOf(controlador.getVeterinaria().getListaClientes().size()));
        lblTurnos.setText(String.valueOf(controlador.getVeterinaria().getListaTurnos().size()));
        lblEnAdopcion.setText(String.valueOf(controlador.obtenerAnimalesEnAdopcion().size()));
        lblNotas.setText(String.valueOf(controlador.getNotas().size()));
        if (panelGestion != null) panelGestion.refrescar();
        reajustarLayout();
    }

    private Turno seleccionarTurno() {
        if (controlador.getVeterinaria().getListaTurnos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay turnos registrados.");
            return null;
        }
        java.util.List<Turno> list = controlador.getVeterinaria().getListaTurnos();
        Window owner = SwingUtilities.getWindowAncestor(this);
        DialogoSeleccionarTurno dialogo = new DialogoSeleccionarTurno(owner, list);
        dialogo.setVisible(true);
        return dialogo.getTurnoSeleccionado();
    }

    private void verComprobante() {
        Turno t = seleccionarTurno();
        if (t == null) return;
        ComprobanteTurno c = new ComprobanteTurno(t, controlador.getVeterinaria().getNombreNegocio());
        Window owner = SwingUtilities.getWindowAncestor(this);
        Frame frame = owner instanceof Frame ? (Frame) owner : null;
        new DialogoComprobante(frame, c).setVisible(true);
    }

    private void guardarComprobante() {
        Turno t = seleccionarTurno();
        if (t == null) return;
        ComprobanteTurno c = new ComprobanteTurno(t, controlador.getVeterinaria().getNombreNegocio());

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("comprobante_turno_" + t.getIdTurno() + ".txt"));
        int opcion = chooser.showSaveDialog(SwingUtilities.getWindowAncestor(this));
        if (opcion == JFileChooser.APPROVE_OPTION) {
            File destino = chooser.getSelectedFile();
            try (PrintWriter pw = new PrintWriter(destino, "UTF-8")) {
                pw.print(c.generarTextoCompleto());
                JOptionPane.showMessageDialog(this, "Comprobante guardado en:\n" + destino.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
            }
        }
    }

    private static class ScrollablePanel extends JPanel implements Scrollable {
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
