package vista.paneles;

import controlador.ControladorVeterinaria;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import recursos.CargadorFuentes;

public class PanelNotas extends JPanel {

    private final ControladorVeterinaria controlador;
    private JTextArea campoNuevaNota;
    private JPanel panelLista;

    private static final Color COLOR_TOPE = recursos.Color.PENDING;

    public PanelNotas(ControladorVeterinaria controlador) {
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
            new LineBorder(recursos.Color.BORDER, 1, true),
            new EmptyBorder(16, 22, 16, 22)
        ));
        JLabel lblTitulo = new JLabel("Notas y Recordatorios");
        lblTitulo.setFont(CargadorFuentes.cargar(16f));
        lblTitulo.setForeground(recursos.Color.INK);
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        JLabel lblSub = new JLabel("Anotaciones internas del equipo veterinario");
        lblSub.setFont(CargadorFuentes.cargar(11f));
        lblSub.setForeground(new Color(100, 116, 139));
        panelHeader.add(lblSub, BorderLayout.SOUTH);
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(0, 12));
        panelCentral.setOpaque(false);

        JPanel panelEditor = new JPanel(new BorderLayout(0, 8));
        panelEditor.setBackground(Color.WHITE);
        panelEditor.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(recursos.Color.BORDER, 1, true),
            new EmptyBorder(14, 18, 14, 18)
        ));

        JLabel lblEditor = new JLabel("Nueva nota");
        lblEditor.setFont(CargadorFuentes.cargar(12f));
        lblEditor.setForeground(new Color(71, 85, 105));
        panelEditor.add(lblEditor, BorderLayout.NORTH);

        campoNuevaNota = new JTextArea(3, 1);
        campoNuevaNota.setFont(CargadorFuentes.cargar(13f));
        campoNuevaNota.setLineWrap(true);
        campoNuevaNota.setWrapStyleWord(true);
        campoNuevaNota.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(203, 213, 225), 1, true),
            new EmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane scrollEditor = new JScrollPane(campoNuevaNota);
        scrollEditor.setBorder(null);
        panelEditor.add(scrollEditor, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Agregar nota");
        btnGuardar.setFont(CargadorFuentes.cargar(12f));
        btnGuardar.setBackground(COLOR_TOPE);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setOpaque(true);
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> agregarNota());
        panelEditor.add(btnGuardar, BorderLayout.SOUTH);

        panelCentral.add(panelEditor, BorderLayout.NORTH);

        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(Color.WHITE);
        panelLista.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(recursos.Color.BORDER, 1, true),
            new EmptyBorder(12, 16, 12, 16)
        ));
        JScrollPane scrollLista = new JScrollPane(panelLista);
        scrollLista.setBorder(null);
        scrollLista.getViewport().setBackground(Color.WHITE);
        panelCentral.add(scrollLista, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);
    }

    public void actualizar() {
        panelLista.removeAll();
        if (controlador.getNotas().isEmpty()) {
            JLabel lblVacio = new JLabel("No hay notas registradas.");
            lblVacio.setFont(CargadorFuentes.cargar(12f));
            lblVacio.setForeground(recursos.Color.CAT_INACTIVO);
            lblVacio.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblVacio.setBorder(new EmptyBorder(30, 0, 30, 0));
            panelLista.add(lblVacio);
        } else {
            for (int i = 0; i < controlador.getNotas().size(); i++) {
                final int idx = i;
                panelLista.add(crearItemNota(controlador.getNotas().get(i), idx));
                panelLista.add(Box.createVerticalStrut(8));
            }
        }
        panelLista.revalidate();
        panelLista.repaint();
    }

    private JPanel crearItemNota(String texto, int indice) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setBackground(new Color(255, 251, 235));
        item.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(254, 243, 199), 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lblTexto = new JLabel("<html><div style='width: 100%;'>" + texto + "</div></html>");
        lblTexto.setFont(CargadorFuentes.cargar(12f));
        lblTexto.setForeground(recursos.Color.INK);
        item.add(lblTexto, BorderLayout.CENTER);

        JButton btnEliminar = new JButton("✕");
        btnEliminar.setFont(CargadorFuentes.cargar(11f));
        btnEliminar.setBackground(new Color(255, 251, 235));
        btnEliminar.setForeground(new Color(180, 83, 9));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setBorder(null);
        btnEliminar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEliminar.addActionListener(e -> {
            controlador.eliminarNota(indice);
            actualizar();
        });
        item.add(btnEliminar, BorderLayout.EAST);
        return item;
    }

    private void agregarNota() {
        String texto = campoNuevaNota.getText();
        if (texto == null || texto.isBlank()) {
            JOptionPane.showMessageDialog(this, "Escribí una nota antes de agregar.");
            return;
        }
        controlador.agregarNota(texto);
        campoNuevaNota.setText("");
        actualizar();
    }
}
