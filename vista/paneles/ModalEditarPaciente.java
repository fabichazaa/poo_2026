package vista.paneles;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelo.Animal;

public class ModalEditarPaciente extends JDialog {

    private final Animal animal;
    private JTextField txtNombre;
    private JTextField txtRaza;
    private JTextField txtPeso;
    private JTextField txtFechaNac;
    private JRadioButton rbMacho;
    private JRadioButton rbHembra;
    private JCheckBox chkActivo;
    private JButton btnGuardar;

    public ModalEditarPaciente(Frame padre, Animal animal) {
        super(padre, "Editar Ficha del Paciente", true);
        this.animal = animal;

        // 🌟 EL TRUCO DEL BACKDROP: El modal ahora copia el tamaño exacto de la ventana padre
        setSize(padre.getWidth(), padre.getHeight());
        setLocation(padre.getLocation()); 
        setUndecorated(true);
        setLayout(new GridBagLayout()); // GridBagLayout centra al hijo (el formulario) automáticamente
        setBackground(new Color(0, 0, 0, 0)); // Diálogo transparente para soportar el velo personalizado

        initComponentes();
    }

    private void initComponentes() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Color colorInicioAnimal = Color.decode(animal.getColorInicioHex());
        Color colorFinAnimal = Color.decode(animal.getColorFinHex());

        // --- 1. CONTENEDOR MAESTRO DEL FORMULARIO (Caja Blanca Fija) ---
        JPanel panelCuerpo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.dispose();
            }
        };
        panelCuerpo.setOpaque(false);
        // Forzamos el tamaño del mockup para la tarjeta central
        panelCuerpo.setPreferredSize(new Dimension(460, 580));
        panelCuerpo.setMinimumSize(new Dimension(460, 580));
        panelCuerpo.setMaximumSize(new Dimension(460, 580));

        // --- 2. CABECERA NARANJA ---
        JPanel panelHeaderNaranja = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color naranjaInicio = new Color(251, 146, 60);
                Color naranjaFin = new Color(234, 88, 12);
                g2.setPaint(new GradientPaint(0, 0, colorInicioAnimal, getWidth(), 0, colorFinAnimal));
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight() + 20, 28, 28));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panelHeaderNaranja.setOpaque(false);
        panelHeaderNaranja.setPreferredSize(new Dimension(0, 75));
        panelHeaderNaranja.setBorder(new EmptyBorder(12, 20, 12, 20));

        JPanel panelTextosHeader = new JPanel(new GridLayout(2, 1, 0, 2));
        panelTextosHeader.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("Editar Ficha del Paciente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        
        JLabel lblSubtitulo = new JLabel(animal.getNombre() + " · " + animal.getEspecie());
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(254, 215, 170));
        
        panelTextosHeader.add(lblTitulo);
        panelTextosHeader.add(lblSubtitulo);

        JButton btnCerrar = new JButton() {
            {
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        };
        ImageIcon iconoCerrar = cargarIconoHD("imagenes/emojis/cruz.png", 16, 16);
        if (iconoCerrar != null) {
            btnCerrar.setIcon(iconoCerrar);
        } else {
            btnCerrar.setText("✕");
            btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnCerrar.setForeground(Color.WHITE);
        }
        btnCerrar.addActionListener(e -> dispose());

        panelHeaderNaranja.add(panelTextosHeader, BorderLayout.WEST);
        panelHeaderNaranja.add(btnCerrar, BorderLayout.EAST);

        // --- 3. FORMULARIO INTERNO CENTRAL ---
        JPanel panelFormulario = new JPanel();
        panelFormulario.setOpaque(false);
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBorder(new EmptyBorder(20, 24, 20, 24));

        // 🌟 FIJAR ALINEACIÓN IZQUIERDA EN COMPONENTES DEL FORMULARIO
        // Campo NOMBRE
        panelFormulario.add(crearLabelFormulario("NOMBRE *"));
        txtNombre = crearTextFieldFormulario(animal.getNombre());
        txtNombre.setAlignmentX(Component.LEFT_ALIGNMENT); // 🔥 Clave para alinear a la izquierda
        panelFormulario.add(txtNombre);
        panelFormulario.add(Box.createVerticalStrut(12));

        // Campo RAZA
        panelFormulario.add(crearLabelFormulario("RAZA"));
        txtRaza = crearTextFieldFormulario(animal.getRaza());
        txtRaza.setAlignmentX(Component.LEFT_ALIGNMENT); // 🔥 Clave para alinear a la izquierda
        panelFormulario.add(txtRaza);
        panelFormulario.add(Box.createVerticalStrut(12));

        // Columnas Divididas (Sexo y Peso)
        JPanel panelFilaDividida = new JPanel(new GridLayout(1, 2, 16, 0));
        panelFilaDividida.setOpaque(false);
        panelFilaDividida.setMaximumSize(new Dimension(Short.MAX_VALUE, 65));
        panelFilaDividida.setAlignmentX(Component.LEFT_ALIGNMENT); // 🔥 Clave para alinear a la izquierda

        // Sub-bloque SEXO
        JPanel colSexo = new JPanel();
        colSexo.setOpaque(false);
        colSexo.setLayout(new BoxLayout(colSexo, BoxLayout.Y_AXIS));
        colSexo.add(crearLabelFormulario("SEXO"));
        
        JPanel panelBotonesSexo = new JPanel(new GridLayout(1, 2, 8, 0));
        panelBotonesSexo.setOpaque(false);
        panelBotonesSexo.setMaximumSize(new Dimension(Short.MAX_VALUE, 38));

        rbMacho = new JRadioButton("Macho", animal.getSexo()) {
            {
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected()) {
                    setForeground(colorFinAnimal);
                    g2.setColor(new Color(255, 247, 237)); 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(colorInicioAnimal); 
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
                } else {
                    setForeground(recursos.Color.INK);
                    g2.setColor(recursos.Color.BG); 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(recursos.Color.BORDER);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };

        rbHembra = new JRadioButton("Hembra", !animal.getSexo()) {
            {
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected()) {
                    setForeground(colorFinAnimal);
                    g2.setColor(new Color(255, 247, 237)); 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(colorInicioAnimal);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
                } else {
                    setForeground(recursos.Color.INK);
                    g2.setColor(recursos.Color.BG);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(recursos.Color.BORDER);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };

        ButtonGroup grupoSexo = new ButtonGroup();
        grupoSexo.add(rbMacho);
        grupoSexo.add(rbHembra);
        
        rbMacho.addActionListener(e -> { rbMacho.repaint(); rbHembra.repaint(); });
        rbHembra.addActionListener(e -> { rbMacho.repaint(); rbHembra.repaint(); });

        panelBotonesSexo.add(rbMacho);
        panelBotonesSexo.add(rbHembra);
        colSexo.add(panelBotonesSexo);
        panelFilaDividida.add(colSexo);

        // Sub-bloque PESO
        JPanel colPeso = new JPanel();
        colPeso.setOpaque(false);
        colPeso.setLayout(new BoxLayout(colPeso, BoxLayout.Y_AXIS));
        colPeso.add(crearLabelFormulario("PESO (KG) *"));
        txtPeso = crearTextFieldFormulario(String.valueOf(animal.getPeso()));
        txtPeso.setAlignmentX(Component.LEFT_ALIGNMENT);
        colPeso.add(txtPeso);
        panelFilaDividida.add(colPeso);

        panelFormulario.add(panelFilaDividida);
        panelFormulario.add(Box.createVerticalStrut(12));

        // Campo FECHA DE NACIMIENTO
        panelFormulario.add(crearLabelFormulario("FECHA DE NAC. (DD/MM/AAAA)"));
        String fechaStr = (animal.getFechaNacimiento() != null) ? animal.getFechaNacimiento().format(formato) : "";
        txtFechaNac = crearTextFieldFormulario(fechaStr);
        txtFechaNac.setAlignmentX(Component.LEFT_ALIGNMENT); // 🔥 Clave para alinear a la izquierda
        panelFormulario.add(txtFechaNac);
        panelFormulario.add(Box.createVerticalStrut(12));

        // Campo ESTADO ACTIVO
        JPanel panelCheck = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelCheck.setOpaque(false);
        panelCheck.setAlignmentX(Component.LEFT_ALIGNMENT); // 🔥 Clave para alinear a la izquierda
        chkActivo = new JCheckBox("Paciente Activo en la Veterinaria", animal.isActivo());
        chkActivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkActivo.setOpaque(false);
        chkActivo.setForeground(recursos.Color.INK);
        panelCheck.add(chkActivo);
        panelFormulario.add(panelCheck);

        // --- 4. BOTONERA INFERIOR CON REACCIÓN HOVER ---
        JPanel panelBotonesBottom = new JPanel(new GridLayout(1, 2, 12, 0));
        panelBotonesBottom.setOpaque(false);
        panelBotonesBottom.setBorder(new EmptyBorder(10, 24, 24, 24));

        JButton btnCancelar = new JButton("Cancelar") {
            private boolean hover = false;
            {
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(java.awt.event.MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? new Color(226, 232, 240) : new Color(241, 245, 249));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                setForeground(new Color(71, 85, 105));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnCancelar.addActionListener(e -> dispose());

        btnGuardar = new JButton("Guardar Cambios →") {
            private boolean hover = false;
            {
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(java.awt.event.MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? colorInicioAnimal : colorFinAnimal);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                setForeground(Color.WHITE);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btnGuardar.addActionListener(e -> {
            try {
                animal.setNombre(txtNombre.getText().trim());
                animal.setRaza(txtRaza.getText().trim());
                animal.setPeso(Float.parseFloat(txtPeso.getText().trim()));
                animal.setActivo(chkActivo.isSelected());
                
                if (!txtFechaNac.getText().isBlank()) {
                    animal.setFechaNacimiento(LocalDate.parse(txtFechaNac.getText().trim(), formato));
                }
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Por favor, verifica los campos obligatorios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.setPreferredSize(new Dimension(0, 42));
        btnGuardar.setPreferredSize(new Dimension(0, 42));
        panelBotonesBottom.add(btnCancelar);
        panelBotonesBottom.add(btnGuardar);

        // Ensamblamos la tarjeta blanca central
        panelCuerpo.add(panelHeaderNaranja, BorderLayout.NORTH);
        panelCuerpo.add(panelFormulario, BorderLayout.CENTER);
        panelCuerpo.add(panelBotonesBottom, BorderLayout.SOUTH);

        // --- 🌟 ADICIÓN FINAL: AGREGAMOS LA TARJETA AL MODAL ---
        add(panelCuerpo);
    }

    // 🌟 SECCIÓN CLAVE: Sobreescribimos el paint del JDialog entero para dibujar el velo semitransparente
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        // Dibujamos un rectángulo del tamaño de toda la pantalla con color negro y 45% de opacidad (115)
        g2.setColor(new Color(15, 23, 42, 115)); 
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paint(g); // Dibuja los componentes hijos (la tarjeta blanca) arriba del velo
    }

    private JLabel crearLabelFormulario(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(148, 163, 184));
        lbl.setBorder(new EmptyBorder(0, 2, 4, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT); // 🔥 Clave de alineación
        return lbl;
    }

    private JTextField crearTextFieldFormulario(String valorInicial) {
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
        tf.setMaximumSize(new Dimension(Short.MAX_VALUE, 38));
        tf.setPreferredSize(new Dimension(Short.MAX_VALUE, 38));
        tf.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return tf;
    }

    private ImageIcon cargarIconoHD(String ruta, int ancho, int alto) {
        try {
            java.awt.image.BufferedImage imgBuffer = javax.imageio.ImageIO.read(new java.io.File(ruta));
            java.awt.image.BufferedImage resizedImg = new java.awt.image.BufferedImage(ancho, alto, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(imgBuffer, 0, 0, ancho, alto, null);
            g2.dispose();
            return new ImageIcon(resizedImg);
        } catch (Exception e) {
            return null;
        }
    }
}