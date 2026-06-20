package vista.paneles;

import controlador.ControladorVeterinaria;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import modelo.Persona;
import modelo.Responsable;
import modelo.Veterinario;
import vista.dialogos.DialogoEditarPersona;

/**
 * Gestor de personas embebido en la sección "Registros": lista veterinarios o
 * dueños (toggle), permite buscar, dar de alta y editar.
 */
public final class PanelGestionPersonas extends JPanel {

    private final ControladorVeterinaria controlador;
    private boolean mostrandoVeterinarios = true;
    private final JTextField txtBuscar;
    private final JPanel panelLista;
    private final JLabel lblFooter;
    private JButton btnTabVets;
    private JButton btnTabDuenos;
    private JButton btnAgregar;

    private static final String PLACEHOLDER = "Buscar...";

    public PanelGestionPersonas(ControladorVeterinaria controlador) {
        this.controlador = controlador;

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(recursos.Color.BORDER, 1, true),
                new EmptyBorder(18, 22, 18, 22)));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Fila título + botón Agregar ---
        JPanel filaTitulo = new JPanel(new BorderLayout());
        filaTitulo.setOpaque(false);
        filaTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        JLabel lblTitulo = new JLabel("Gestión de personas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(recursos.Color.INK);
        filaTitulo.add(lblTitulo, BorderLayout.WEST);

        btnAgregar = crearBotonAgregar();
        JPanel wrapAgregar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        wrapAgregar.setOpaque(false);
        wrapAgregar.add(btnAgregar);
        filaTitulo.add(wrapAgregar, BorderLayout.EAST);
        add(filaTitulo);
        add(Box.createVerticalStrut(12));

        // --- Fila toggle + buscador ---
        JPanel filaControles = new JPanel(new BorderLayout(12, 0));
        filaControles.setOpaque(false);
        filaControles.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaControles.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JPanel toggle = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(recursos.Color.BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
                g2.dispose();
            }
        };
        toggle.setOpaque(false);
        toggle.setBorder(new EmptyBorder(3, 4, 3, 4));
        btnTabVets = crearBotonToggle("Veterinarios", true);
        btnTabDuenos = crearBotonToggle("Dueños", false);
        toggle.add(btnTabVets);
        toggle.add(btnTabDuenos);
        filaControles.add(toggle, BorderLayout.WEST);

        txtBuscar = crearBuscador();
        filaControles.add(txtBuscar, BorderLayout.CENTER);
        add(filaControles);
        add(Box.createVerticalStrut(14));

        // --- Lista ---
        panelLista = new JPanel();
        panelLista.setOpaque(false);
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(panelLista);
        add(Box.createVerticalStrut(10));

        // --- Footer ---
        lblFooter = new JLabel();
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooter.setForeground(recursos.Color.MUTED);
        lblFooter.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(lblFooter);

        refrescar();
    }

    @Override
    public Dimension getMaximumSize() {
        // Ocupa todo el ancho disponible (como las demás tarjetas) sin estirarse en alto.
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }

    public void refrescar() {
        actualizarTextosToggle();
        panelLista.removeAll();

        String busqueda = textoBuscado();
        int total = 0;
        int activosTotales;

        if (mostrandoVeterinarios) {
            int act = 0;
            for (Veterinario v : controlador.getVeterinarios()) if (v.isActivo()) act++;
            activosTotales = act;
            for (Veterinario v : controlador.getVeterinarios()) {
                if (!coincide(busqueda, v.getNombre(), v.getApellido(), v.getDNI(), v.getMatricula())) continue;
                panelLista.add(crearCardVeterinario(v));
                panelLista.add(Box.createVerticalStrut(10));
                total++;
            }
        } else {
            activosTotales = controlador.getResponsables().size();
            for (Responsable r : controlador.getResponsables()) {
                if (!coincide(busqueda, r.getNombre(), r.getApellido(), r.getDNI(), r.getEmail())) continue;
                panelLista.add(crearCardDueno(r));
                panelLista.add(Box.createVerticalStrut(10));
                total++;
            }
        }

        lblFooter.setText(total + " resultado" + (total == 1 ? "" : "s") + " · " + activosTotales + " activos");
        panelLista.revalidate();
        panelLista.repaint();
    }

    private void actualizarTextosToggle() {
        btnTabVets.setText("Veterinarios  " + controlador.getVeterinarios().size());
        btnTabDuenos.setText("Dueños  " + controlador.getResponsables().size());
        btnTabVets.repaint();
        btnTabDuenos.repaint();
    }

    private String textoBuscado() {
        String t = txtBuscar.getText().trim().toLowerCase();
        return t.equals(PLACEHOLDER.toLowerCase()) ? "" : t;
    }

    private boolean coincide(String busqueda, String... campos) {
        if (busqueda.isEmpty()) return true;
        for (String c : campos) {
            if (c != null && c.toLowerCase().contains(busqueda)) return true;
        }
        return false;
    }

    // ---------- Cards ----------

    private JPanel crearCardVeterinario(Veterinario v) {
        String subtitulo = (v.getEspecialidad() != null ? v.getEspecialidad() : "General") + " · MP " + v.getMatricula();
        JPanel card = baseCard(iniciales(v), recursos.Color.PRIMARY, v.getNombre() + " " + v.getApellido(), subtitulo);

        JPanel este = new JPanel();
        este.setOpaque(false);
        este.setLayout(new BoxLayout(este, BoxLayout.Y_AXIS));
        JComponent badge = v.isActivo()
                ? badge("Activo", recursos.Color.GREEN_LIGHT, recursos.Color.SUCCESS)
                : badge("Inactivo", recursos.Color.BG, recursos.Color.MUTED);
        badge.setAlignmentX(Component.RIGHT_ALIGNMENT);
        este.add(badge);
        if (v.getTurnoTrabajo() != null && !v.getTurnoTrabajo().isBlank()) {
            este.add(Box.createVerticalStrut(4));
            JLabel lblHorario = new JLabel(v.getTurnoTrabajo());
            lblHorario.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblHorario.setForeground(recursos.Color.CAT_INACTIVO);
            lblHorario.setAlignmentX(Component.RIGHT_ALIGNMENT);
            este.add(lblHorario);
        }

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        derecha.setOpaque(false);
        derecha.add(este);
        derecha.add(chevron());
        card.add(derecha, BorderLayout.EAST);
        hacerClickeable(card, () -> abrirEdicion(v));
        return card;
    }

    private JPanel crearCardDueno(Responsable r) {
        String contacto = (r.getCelular() != null && !r.getCelular().isBlank() ? r.getCelular() : "Sin teléfono");
        if (r.getEmail() != null && !r.getEmail().isBlank()) contacto += " · " + r.getEmail();
        JPanel card = baseCard(iniciales(r), recursos.Color.CAT_CONSULTA, r.getNombre() + " " + r.getApellido(), contacto);

        int cant = r.getMascotas().size();
        String txt = cant + " mascota" + (cant == 1 ? "" : "s");
        JComponent badge = badge(txt, recursos.Color.YELLOW_LIGHT, recursos.Color.YELLOW_DARK);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        derecha.setOpaque(false);
        derecha.add(badge);
        derecha.add(chevron());
        card.add(derecha, BorderLayout.EAST);
        hacerClickeable(card, () -> abrirEdicion(r));
        return card;
    }

    private JPanel baseCard(String iniciales, Color colorAvatar, String titulo, String subtitulo) {
        JPanel card = new JPanel(new BorderLayout(14, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(recursos.Color.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 14, 12, 14));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(crearAvatar(iniciales, colorAvatar), BorderLayout.WEST);

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(recursos.Color.INK);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(recursos.Color.MUTED);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(Box.createVerticalGlue());
        centro.add(lblTitulo);
        centro.add(Box.createVerticalStrut(2));
        centro.add(lblSub);
        centro.add(Box.createVerticalGlue());
        card.add(centro, BorderLayout.CENTER);
        return card;
    }

    private JPanel crearAvatar(String iniciales, Color color) {
        JPanel avatar = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(46, 46));
        JLabel lbl = new JLabel(iniciales);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(Color.WHITE);
        avatar.add(lbl);
        return avatar;
    }

    private JLabel badge(String texto, Color fondo, Color textoColor) {
        JLabel lbl = new JLabel(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fondo);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(textoColor);
        lbl.setBorder(new EmptyBorder(3, 12, 3, 12));
        return lbl;
    }

    private String iniciales(Persona p) {
        String n = (p.getNombre() != null && !p.getNombre().isBlank()) ? p.getNombre().substring(0, 1) : "";
        String a = (p.getApellido() != null && !p.getApellido().isBlank()) ? p.getApellido().substring(0, 1) : "";
        String r = (n + a).toUpperCase();
        return r.isEmpty() ? "?" : r;
    }

    // ---------- Acciones ----------

    private void abrirAlta() {
        Frame padre = (Frame) SwingUtilities.getWindowAncestor(this);
        new DialogoEditarPersona(padre, mostrandoVeterinarios, null).setVisible(true);
        refrescar();
    }

    private void abrirEdicion(Persona p) {
        Frame padre = (Frame) SwingUtilities.getWindowAncestor(this);
        new DialogoEditarPersona(padre, p instanceof Veterinario, p).setVisible(true);
        refrescar();
    }

    // ---------- Botones ----------

    private JButton crearBotonAgregar() {
        JButton btn = new JButton("Agregar") {
            private boolean hover = false;
            {
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setBorder(new EmptyBorder(0, 20, 0, 20));
                setPreferredSize(new Dimension(150, 38));
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override public void mouseEntered(java.awt.event.MouseEvent e) { hover = true; repaint(); }
                    @Override public void mouseExited(java.awt.event.MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? recursos.Color.PRIMARY_DEEP : recursos.Color.PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
                setForeground(Color.WHITE);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.addActionListener(e -> abrirAlta());
        return btn;
    }

    private JButton crearBotonToggle(String texto, boolean paraVets) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean activo = (mostrandoVeterinarios == paraVets);
                if (activo) {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    g2.setColor(recursos.Color.BORDER);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                    setForeground(recursos.Color.PRIMARY);
                } else {
                    setForeground(recursos.Color.MUTED);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            mostrandoVeterinarios = paraVets;
            refrescar();
        });
        return btn;
    }

    private JLabel chevron() {
        JLabel lbl = new JLabel("›");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(recursos.Color.CAT_INACTIVO);
        lbl.setBorder(new EmptyBorder(0, 4, 0, 4));
        return lbl;
    }

    /** Hace clickeable toda la card (y sus hijos) para abrir la edición, como en el mockup. */
    private void hacerClickeable(Component c, Runnable onClick) {
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { onClick.run(); }
        });
        if (c instanceof Container) {
            for (Component hijo : ((Container) c).getComponents()) {
                hacerClickeable(hijo, onClick);
            }
        }
    }

    private JTextField crearBuscador() {
        JTextField tf = new JTextField(PLACEHOLDER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(recursos.Color.BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
                g2.setColor(recursos.Color.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 36, 36);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setForeground(recursos.Color.CAT_INACTIVO);
        tf.setOpaque(false);
        tf.setBorder(new EmptyBorder(0, 16, 0, 15));
        tf.addFocusListener(new java.awt.event.FocusListener() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if (tf.getText().equals(PLACEHOLDER)) {
                    tf.setText("");
                    tf.setForeground(recursos.Color.INK);
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (tf.getText().trim().isEmpty()) {
                    tf.setText(PLACEHOLDER);
                    tf.setForeground(recursos.Color.CAT_INACTIVO);
                }
            }
        });
        tf.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { refrescar(); }
        });
        return tf;
    }
}
