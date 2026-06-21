package vista.dialogos;

import controlador.ControladorVeterinaria;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelo.Direccion;
import modelo.Persona;
import modelo.Responsable;
import modelo.Veterinario;
import recursos.CargadorFuentes;
import recursos.ImageLoader;

public class DialogoEditarPersona extends JDialog {

    private final ControladorVeterinaria controlador;
    private final boolean esVeterinario;
    private final boolean esModoEdicion;
    private final Persona persona;

    private JTextField txtDni;
    private JTextField txtCelular;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtCalle;
    private JTextField txtAltura;
    private JTextField txtLocalidad;
    private JTextField txtMatricula; // solo veterinario

    public DialogoEditarPersona(Frame padre, boolean esVeterinario, Persona personaExistente) {
        super(padre, titulo(esVeterinario, personaExistente), true);
        this.controlador = ControladorVeterinaria.getInstancia();
        this.esVeterinario = esVeterinario;
        this.persona = personaExistente;
        this.esModoEdicion = (personaExistente != null);

        setSize(500, esVeterinario ? 540 : 480);
        setResizable(false);
        setLocationRelativeTo(padre);
        ImageIcon icono = ImageLoader.loadScaled("imagenes/logo.png", 32, 32);
        if (icono != null && icono.getImage() != null && icono.getIconWidth() > 0) {
            setIconImage(icono.getImage());
        }
        initComponentes();
    }

    private static String titulo(boolean esVet, Persona p) {
        String tipo = esVet ? "veterinario" : "dueño";
        if (p != null) return "Editar " + tipo;
        return "Nuevo " + tipo;
    }

    private void initComponentes() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, recursos.Color.PRIMARY, getWidth(), getHeight(), recursos.Color.ACCENT_BLUE));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        root.setOpaque(false);
        root.setBorder(new EmptyBorder(3, 3, 3, 3));

        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 19, 19);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);

        card.add(crearHeader(), BorderLayout.NORTH);
        card.add(crearFormulario(), BorderLayout.CENTER);
        card.add(crearBotonera(), BorderLayout.SOUTH);

        root.add(card, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(18, 22, 8, 22));

        JPanel logo = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(recursos.Color.PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        logo.setPreferredSize(new Dimension(40, 40));
        logo.setOpaque(false);
        JLabel lblIcono = new JLabel("🐾");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        ImageIcon icon = ImageLoader.loadScaled("imagenes/logo.png", 28, 28);
        if (icon != null && icon.getImage() != null && icon.getIconWidth() > 0) {
            lblIcono.setIcon(icon);
            lblIcono.setText("");
        }
        logo.add(lblIcono);
        header.add(logo);

        JPanel titulos = new JPanel();
        titulos.setOpaque(false);
        titulos.setLayout(new BoxLayout(titulos, BoxLayout.Y_AXIS));
        JLabel lblTitulo = new JLabel(getTitle());
        lblTitulo.setFont(CargadorFuentes.cargar(17f).deriveFont(Font.BOLD));
        lblTitulo.setForeground(recursos.Color.INK);
        JLabel lblSub = new JLabel(esModoEdicion ? "Editá los datos y guardá" : "Completá los datos (* obligatorios)");
        lblSub.setFont(CargadorFuentes.cargar(11f));
        lblSub.setForeground(recursos.Color.MUTED);
        titulos.add(lblTitulo);
        titulos.add(lblSub);
        header.add(titulos);

        return header;
    }

    private JPanel crearFormulario() {
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(8, 22, 8, 22));

        Direccion dir = (persona != null) ? persona.getDireccion() : null;

        form.add(filaDoble(
                "DNI / DOCUMENTO *", txtDni = crearTextField(persona != null ? persona.getDNI() : ""),
                "TELÉFONO / CELULAR *", txtCelular = crearTextField(persona != null ? persona.getCelular() : "")));
        if (esModoEdicion) txtDni.setEditable(false);
        form.add(Box.createVerticalStrut(12));

        form.add(filaDoble(
                "NOMBRE *", txtNombre = crearTextField(persona != null ? persona.getNombre() : ""),
                "APELLIDO *", txtApellido = crearTextField(persona != null ? persona.getApellido() : "")));
        form.add(Box.createVerticalStrut(12));

        form.add(crearLabel("DIRECCIÓN *"));
        JPanel filaDir = new JPanel(new GridBagLayout());
        filaDir.setOpaque(false);
        filaDir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        filaDir.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1.0;
        txtCalle = crearTextField(dir != null ? dir.getCalle() : "");
        txtAltura = crearTextField(dir != null && dir.getNumero() > 0 ? String.valueOf(dir.getNumero()) : "");
        txtLocalidad = crearTextField(dir != null ? dir.getLocalidad() : "");
        gbc.gridx = 0; gbc.weightx = 0.55; gbc.insets = new Insets(0, 0, 0, 8);
        filaDir.add(txtCalle, gbc);
        gbc.gridx = 1; gbc.weightx = 0.15; gbc.insets = new Insets(0, 0, 0, 8);
        filaDir.add(txtAltura, gbc);
        gbc.gridx = 2; gbc.weightx = 0.30; gbc.insets = new Insets(0, 0, 0, 0);
        filaDir.add(txtLocalidad, gbc);
        form.add(filaDir);

        if (esVeterinario) {
            form.add(Box.createVerticalStrut(12));
            form.add(crearLabel("MATRÍCULA *"));
            txtMatricula = crearTextField(persona != null ? ((Veterinario) persona).getMatricula() : "");
            txtMatricula.setAlignmentX(Component.LEFT_ALIGNMENT);
            form.add(txtMatricula);
        }

        form.add(Box.createVerticalGlue());
        return form;
    }

    private JPanel crearBotonera() {
        JPanel botonera = new JPanel(new GridLayout(1, esModoEdicion ? 3 : 2, 12, 0));
        botonera.setOpaque(false);
        botonera.setBorder(new EmptyBorder(8, 22, 18, 22));
        if (esModoEdicion) botonera.add(crearBotonEliminar());
        botonera.add(crearBoton("Cancelar", false, e -> dispose()));
        botonera.add(crearBoton(esModoEdicion ? "Guardar cambios" : "Crear registro", true, e -> guardar()));
        return botonera;
    }

    private void guardar() {
        try {
            String calle = txtCalle.getText().trim();
            int altura = txtAltura.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtAltura.getText().trim());
            String localidad = txtLocalidad.getText().trim().isEmpty() ? "Pilar" : txtLocalidad.getText().trim();
            Direccion direccion = new Direccion(calle, altura, localidad);

            if (!esModoEdicion) {
                if (esVeterinario) {
                    controlador.registrarVeterinario(
                            txtDni.getText().trim(), txtNombre.getText().trim(), txtApellido.getText().trim(),
                            txtCelular.getText().trim(), direccion, txtMatricula.getText().trim());
                } else {
                    controlador.registrarResponsable(
                            txtDni.getText().trim(), txtNombre.getText().trim(), txtApellido.getText().trim(),
                            txtCelular.getText().trim(), direccion);
                }
            } else {
                if (txtNombre.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException();
                }
                persona.setNombre(txtNombre.getText().trim());
                persona.setApellido(txtApellido.getText().trim());
                persona.setCelular(txtCelular.getText().trim());
                persona.setDireccion(direccion);
                if (esVeterinario) {
                    if (txtMatricula.getText().trim().isEmpty()) throw new IllegalArgumentException();
                    ((Veterinario) persona).setMatricula(txtMatricula.getText().trim());
                }
            }

            JOptionPane.showMessageDialog(this,
                    esModoEdicion ? "Los cambios se guardaron correctamente." : "¡Registro creado con éxito!",
                    "Operación exitosa", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El N° de la dirección debe ser numérico.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Completá los campos obligatorios (*).",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo registrar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (esVeterinario && persona == controlador.getVeterinarioLogueado()) {
            JOptionPane.showMessageDialog(this,
                    "No podés eliminar al veterinario con el que iniciaste sesión.",
                    "Acción no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = persona.getNombre() + " " + persona.getApellido();
        String detalle = "";
        if (esVeterinario) {
            int turnos = controlador.contarTurnosDelVeterinario((Veterinario) persona, null);
            if (turnos > 0) detalle = "\nTiene " + turnos + " turno(s) asociados.";
        } else {
            int m = ((Responsable) persona).getMascotas().size();
            if (m > 0) detalle = "\nSe quitarán también sus " + m + " mascota(s) del sistema.";
        }
        int op = JOptionPane.showConfirmDialog(this,
                "¿Eliminar a " + nombre + "?" + detalle + "\nEsta acción no se puede deshacer.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (op != JOptionPane.YES_OPTION) return;

        if (esVeterinario) controlador.eliminarVeterinario((Veterinario) persona);
        else controlador.eliminarResponsable((Responsable) persona);
        dispose();
    }

    private JPanel filaDoble(String label1, JComponent campo1, String label2, JComponent campo2) {
        JPanel fila = new JPanel(new GridLayout(1, 2, 12, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.add(colCampo(label1, campo1));
        fila.add(colCampo(label2, campo2));
        return fila;
    }

    private JPanel colCampo(String label, JComponent campo) {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(crearLabel(label));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        col.add(campo);
        return col;
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(recursos.Color.CAT_INACTIVO);
        lbl.setBorder(new EmptyBorder(0, 2, 4, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField crearTextField(String valorInicial) {
        JTextField tf = new JTextField(valorInicial) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(recursos.Color.BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(recursos.Color.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setForeground(recursos.Color.INK);
        tf.setOpaque(false);
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        tf.setPreferredSize(new Dimension(150, 38));
        tf.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return tf;
    }

    private JButton crearBoton(String texto, boolean primario, java.awt.event.ActionListener accion) {
        JButton btn = new JButton(texto) {
            private boolean hover = false;
            {
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override public void mouseEntered(java.awt.event.MouseEvent e) { hover = true; repaint(); }
                    @Override public void mouseExited(java.awt.event.MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (primario) {
                    g2.setColor(hover ? recursos.Color.PRIMARY_DEEP : recursos.Color.PRIMARY);
                    setForeground(Color.WHITE);
                } else {
                    g2.setColor(hover ? new Color(226, 232, 240) : new Color(241, 245, 249));
                    setForeground(recursos.Color.SLATE_600);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.addActionListener(accion);
        return btn;
    }

    private JButton crearBotonEliminar() {
        JButton btn = new JButton("🗑 Eliminar") {
            private boolean hover = false;
            {
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override public void mouseEntered(java.awt.event.MouseEvent e) { hover = true; repaint(); }
                    @Override public void mouseExited(java.awt.event.MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? new Color(254, 202, 202) : recursos.Color.RED_LIGHT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                setForeground(recursos.Color.ERROR);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.addActionListener(e -> eliminar());
        return btn;
    }
}
