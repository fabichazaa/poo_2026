package vista.paneles;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.*;
import modelo.ComprobanteTurno;
import modelo.Turno;
import recursos.CargadorFuentes;
import vista.dialogos.DialogoComprobante;

public class PanelMas extends JPanel {

    private final ControladorVeterinaria controlador;
    private JLabel lblNombre;
    private JLabel lblVets;
    private JLabel lblClientes;
    private JLabel lblTurnos;
    private JLabel lblEnAdopcion;
    private JLabel lblNotas;

    private static final Color COLOR_TOPE = new Color(71, 85, 105);

    public PanelMas(ControladorVeterinaria controlador) {
        this.controlador = controlador;
        construir();
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
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(16, 22, 16, 22)
        ));
        JLabel lblTitulo = new JLabel("Configuración y Más");
        lblTitulo.setFont(CargadorFuentes.cargar(16f));
        lblTitulo.setForeground(new Color(30, 41, 59));
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelCuerpo = new JPanel();
        panelCuerpo.setOpaque(false);
        panelCuerpo.setLayout(new BoxLayout(panelCuerpo, BoxLayout.Y_AXIS));

        panelCuerpo.add(crearPanelEstadisticas());
        panelCuerpo.add(Box.createVerticalStrut(15));
        panelCuerpo.add(crearPanelExportar());
        panelCuerpo.add(Box.createVerticalStrut(15));
        panelCuerpo.add(crearPanelAcercaDe());

        JScrollPane scroll = new JScrollPane(panelCuerpo);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(241, 245, 249));
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearPanelEstadisticas() {
        JPanel card = new JPanel(new GridLayout(2, 3, 12, 12));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(20, 22, 20, 22)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        lblNombre = new JLabel();
        lblVets = new JLabel();
        lblClientes = new JLabel();
        lblTurnos = new JLabel();
        lblEnAdopcion = new JLabel();
        lblNotas = new JLabel();

        card.add(crearStat("Veterinaria", lblNombre, new Color(13, 148, 136)));
        card.add(crearStat("Veterinarios", lblVets, new Color(99, 102, 241)));
        card.add(crearStat("Clientes", lblClientes, new Color(124, 58, 237)));
        card.add(crearStat("Turnos", lblTurnos, new Color(217, 119, 6)));
        card.add(crearStat("En adopción", lblEnAdopcion, new Color(236, 72, 153)));
        card.add(crearStat("Notas activas", lblNotas, new Color(22, 163, 74)));

        return card;
    }

    private JPanel crearStat(String titulo, JLabel lblValor, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(248, 250, 252));
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
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(18, 22, 18, 22)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JPanel panelTexto = new JPanel();
        panelTexto.setOpaque(false);
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        JLabel lblTit = new JLabel("Exportar comprobante de atención");
        lblTit.setFont(CargadorFuentes.cargar(13f));
        lblTit.setForeground(new Color(30, 41, 59));
        lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblDesc = new JLabel("Visualizá o guardá un comprobante a partir de un turno existente.");
        lblDesc.setFont(CargadorFuentes.cargar(11f));
        lblDesc.setForeground(new Color(100, 116, 139));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTexto.add(lblTit);
        panelTexto.add(Box.createVerticalStrut(2));
        panelTexto.add(lblDesc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotones.setOpaque(false);
        JButton btnVer = new JButton("Ver comprobante");
        btnVer.setFont(CargadorFuentes.cargar(12f));
        btnVer.setBackground(new Color(13, 148, 136));
        btnVer.setForeground(Color.WHITE);
        btnVer.setFocusPainted(false);
        btnVer.setOpaque(true);
        btnVer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVer.addActionListener(e -> verComprobante());
        JButton btnGuardar = new JButton("Guardar en archivo…");
        btnGuardar.setFont(CargadorFuentes.cargar(12f));
        btnGuardar.setBackground(Color.WHITE);
        btnGuardar.setForeground(new Color(13, 148, 136));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(new LineBorder(new Color(13, 148, 136), 1, true));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardarComprobante());
        panelBotones.add(btnVer);
        panelBotones.add(btnGuardar);

        card.add(panelTexto, BorderLayout.CENTER);
        card.add(panelBotones, BorderLayout.EAST);
        return card;
    }

    private JPanel crearPanelAcercaDe() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(18, 22, 18, 22)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel lblTit = new JLabel("Acerca de Happy Paws");
        lblTit.setFont(CargadorFuentes.cargar(14f));
        lblTit.setForeground(new Color(30, 41, 59));
        lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTit);

        card.add(Box.createVerticalStrut(8));

        JLabel lblDesc = new JLabel("<html><div style='width: 600px; color:#475569;'>"
            + "<b>Happy Paws</b> — Sistema de Gestión Veterinaria.<br>"
            + "Trabajo Integrador de Programación Orientada a Objetos.<br>"
            + "Tecnicatura Universitaria en Desarrollo de Software.<br><br>"
            + "Implementa una jerarquía de herencia con clases abstractas, polimorfismo, "
            + "composición, agregación, colecciones y una clase de reporte (ComprobanteTurno) "
            + "que delega en los objetos del modelo."
            + "</div></html>");
        lblDesc.setFont(CargadorFuentes.cargar(12f));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblDesc);
        return card;
    }

    public void actualizar() {
        lblNombre.setText(controlador.getVeterinaria().getNombreNegocio());
        lblVets.setText(String.valueOf(controlador.getVeterinaria().getListaVeterinarios().size()));
        lblClientes.setText(String.valueOf(controlador.getVeterinaria().getListaClientes().size()));
        lblTurnos.setText(String.valueOf(controlador.getVeterinaria().getListaTurnos().size()));
        lblEnAdopcion.setText(String.valueOf(controlador.obtenerAnimalesEnAdopcion().size()));
        lblNotas.setText(String.valueOf(controlador.getNotas().size()));
    }

    private Turno seleccionarTurno() {
        if (controlador.getVeterinaria().getListaTurnos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay turnos registrados.");
            return null;
        }
        Turno[] arr = controlador.getVeterinaria().getListaTurnos().toArray(new Turno[0]);
        Turno sel = (Turno) JOptionPane.showInputDialog(
            SwingUtilities.getWindowAncestor(this),
            "Seleccioná un turno para generar el comprobante:",
            "Seleccionar turno",
            JOptionPane.PLAIN_MESSAGE,
            null,
            arr,
            arr[0]
        );
        return sel;
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
}
