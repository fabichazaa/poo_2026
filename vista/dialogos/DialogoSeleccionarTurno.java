package vista.dialogos;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import modelo.Turno;
import recursos.CargadorFuentes;
import vista.componentes.Placeholders;

public class DialogoSeleccionarTurno extends JDialog {

    private final List<Turno> turnos;
    private Turno turnoSeleccionado = null;
    private JComboBox<Turno> comboTurno;
    private JTextField txtBuscar;

    public DialogoSeleccionarTurno(Window owner, List<Turno> turnos) {
        super(owner, "Seleccionar Turno", Dialog.ModalityType.APPLICATION_MODAL);
        this.turnos = turnos;
        try {
            ImageIcon iconoApp = new ImageIcon("imagenes/logo.png");
            setIconImage(iconoApp.getImage());
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono del diálogo: " + e.getMessage());
        }
        construir();
    }

    public Turno getTurnoSeleccionado() {
        return turnoSeleccionado;
    }

    private void construir() {
        setSize(480, 240);
        setResizable(false);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        // Panel principal de fondo
        JPanel panelFondo = new JPanel(new BorderLayout());
        panelFondo.setBackground(recursos.Color.SURFACE);
        panelFondo.setOpaque(true);
        panelFondo.setBorder(new EmptyBorder(1, 1, 1, 1));

        // ==========================================
        //  BODY PANEL (Buscador y Combo selector)
        // ==========================================
        JPanel bodyPanel = new JPanel();
        bodyPanel.setOpaque(false);
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBorder(new EmptyBorder(15, 24, 15, 24));

        JLabel lblCombo = new JLabel("SELECCIONÁ UN TURNO");
        lblCombo.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblCombo.setForeground(recursos.Color.MUTED);
        lblCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPanel.add(lblCombo);
        bodyPanel.add(Box.createVerticalStrut(6));

        // Input buscador con placeholder reactivo
        txtBuscar = new Placeholders.TextField("🔍 Buscar por mascota, fecha, tipo o veterinario...");
        txtBuscar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtBuscar.setPreferredSize(new Dimension(0, 38));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(recursos.Color.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBuscar.setForeground(recursos.Color.INK);
        txtBuscar.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPanel.add(txtBuscar);
        bodyPanel.add(Box.createVerticalStrut(8));

        comboTurno = new JComboBox<>();
        comboTurno.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        comboTurno.setPreferredSize(new Dimension(0, 48));
        comboTurno.setFont(CargadorFuentes.cargar(12f));
        comboTurno.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboTurno.setBackground(Color.WHITE);
        comboTurno.setBorder(new LineBorder(recursos.Color.BORDER, 1, true));

        // Llenar combo inicialmente
        refrescarCombo(turnos);

        // Renderizado personalizado de cada fila del ComboBox
        comboTurno.setRenderer(new DefaultListCellRenderer() {
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

                if (value instanceof Turno t) {
                    JLabel lblIcon = new JLabel("📅");
                    lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

                    JPanel textPanel = new JPanel();
                    textPanel.setOpaque(false);
                    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

                    String animalName = t.getAnimal() != null ? t.getAnimal().getNombre() : "Sin mascota";
                    String tipoStr = t.getTipo() != null ? t.getTipo().getDescripcion() : "Sin tipo";
                    JLabel lblNombre = new JLabel("Turno #" + t.getIdTurno() + " — " + animalName + " (" + tipoStr + ")");
                    lblNombre.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
                    lblNombre.setForeground(isItemSelected ? Color.WHITE : recursos.Color.INK);

                    JLabel lblSubText = new JLabel("Fecha: " + t.getFecha() + " " + t.getHora() + " · Estado: " + t.getEstado());
                    lblSubText.setFont(CargadorFuentes.cargar(10f));
                    lblSubText.setForeground(isItemSelected ? recursos.Color.PRIMARY_LIGHT : recursos.Color.MUTED);

                    textPanel.add(lblNombre);
                    textPanel.add(lblSubText);

                    panel.add(lblIcon, BorderLayout.WEST);
                    panel.add(textPanel, BorderLayout.CENTER);
                } else {
                    // Texto auxiliar cuando no hay coincidencias
                    JLabel lblVacio = new JLabel(value != null ? value.toString() : "");
                    lblVacio.setFont(CargadorFuentes.cargar(12f));
                    lblVacio.setForeground(recursos.Color.MUTED);
                    panel.add(lblVacio, BorderLayout.CENTER);
                }
                return panel;
            }
        });

        bodyPanel.add(comboTurno);
        panelFondo.add(bodyPanel, BorderLayout.CENTER);

        // ==========================================
        //  Buscador Reactivo y Filtrado
        // ==========================================
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
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
                String query = txtBuscar.getText().trim().toLowerCase();
                String placeholderText = "🔍 buscar por mascota, fecha, tipo o veterinario...";
                if (query.equals(placeholderText)) {
                    query = "";
                }

                java.util.List<Turno> filtrado = new java.util.ArrayList<>();
                for (Turno t : turnos) {
                    String animalName = t.getAnimal() != null ? t.getAnimal().getNombre() : "";
                    String tipoStr = t.getTipo() != null ? t.getTipo().getDescripcion() : "";
                    String vetName = t.getVeterinario() != null ? (t.getVeterinario().getNombre() + " " + t.getVeterinario().getApellido()) : "";
                    if (animalName.toLowerCase().contains(query)
                            || t.getFecha().toLowerCase().contains(query)
                            || tipoStr.toLowerCase().contains(query)
                            || vetName.toLowerCase().contains(query)
                            || String.valueOf(t.getIdTurno()).contains(query)
                            || t.getEstado().toLowerCase().contains(query)) {
                        filtrado.add(t);
                    }
                }
                refrescarCombo(filtrado);
            }
        });

        // ==========================================
        //  FOOTER PANEL (Acciones)
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
                new EmptyBorder(8, 16, 8, 16)
        ));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnConfirmar = new JButton("Confirmar") {
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
        btnConfirmar.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        btnConfirmar.setBackground(recursos.Color.PRIMARY);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setContentAreaFilled(false);
        btnConfirmar.setOpaque(false);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setBorder(new EmptyBorder(8, 16, 8, 16));
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
        btnConfirmar.addActionListener(e -> {
            Object selected = comboTurno.getSelectedItem();
            if (selected instanceof Turno t) {
                turnoSeleccionado = t;
            }
            dispose();
        });

        footerPanel.add(btnCancelar);
        footerPanel.add(btnConfirmar);

        panelFondo.add(footerPanel, BorderLayout.SOUTH);
        add(panelFondo, BorderLayout.CENTER);
    }

    private void refrescarCombo(List<Turno> items) {
        comboTurno.removeAllItems();
        if (items.isEmpty()) {
            // Si no hay resultados, mostramos un mensaje indicativo
            comboTurno.addItem(null);
        } else {
            for (Turno t : items) {
                comboTurno.addItem(t);
            }
        }
    }
}
