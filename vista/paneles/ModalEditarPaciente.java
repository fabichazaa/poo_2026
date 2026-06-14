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
        // Usamos la barra nativa del sistema operativo con el título dinámico estándar
        super(padre, "Editar paciente: " + animal.getNombre(), true); 
        this.animal = animal;

        // Tamaño compacto ideal ya que la barra nativa ocupa su propio espacio arriba
        setSize(460, 520);
        setLocationRelativeTo(padre); // Centra el modal perfecto sobre tu ventana de la veterinaria
        setLayout(new BorderLayout());

        initComponentes();
    }

    private void initComponentes() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Color colorInicioAnimal = Color.decode(animal.getColorInicioHex());
        Color colorFinAnimal = Color.decode(animal.getColorFinHex());

        // Contenedor principal de la tarjeta
        JPanel panelCuerpo = new JPanel(new BorderLayout());
        panelCuerpo.setBackground(Color.WHITE);

        // --- 1. FORMULARIO INTERNO CENTRAL ---
        JPanel panelFormulario = new JPanel();
        panelFormulario.setOpaque(false);
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        // EmptyBorder le da aire en los cuatro márgenes internos para que respiren los inputs
        panelFormulario.setBorder(new EmptyBorder(25, 24, 20, 24));

        // Campo NOMBRE
        panelFormulario.add(crearLabelFormulario("NOMBRE *"));
        txtNombre = crearTextFieldFormulario(animal.getNombre());
        txtNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFormulario.add(txtNombre);
        panelFormulario.add(Box.createVerticalStrut(12));

        // Campo RAZA (Campo editable libre)
        panelFormulario.add(crearLabelFormulario("RAZA"));
        txtRaza = crearTextFieldFormulario(animal.getRaza());
        txtRaza.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFormulario.add(txtRaza);
        panelFormulario.add(Box.createVerticalStrut(12));

        // Columnas partidas en paralelo (Sexo a la izquierda, Peso a la derecha)
        JPanel panelFilaDividida = new JPanel(new GridLayout(1, 2, 16, 0));
        panelFilaDividida.setOpaque(false);
        panelFilaDividida.setMaximumSize(new Dimension(Short.MAX_VALUE, 65));
        panelFilaDividida.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Sub-bloque SEXO (Píldoras redondeadas con imágenes)
        JPanel colSexo = new JPanel();
        colSexo.setOpaque(false);
        colSexo.setLayout(new BoxLayout(colSexo, BoxLayout.Y_AXIS));
        colSexo.add(crearLabelFormulario("SEXO"));
        
        JPanel panelBotonesSexo = new JPanel(new GridLayout(1, 2, 8, 0));
        panelBotonesSexo.setOpaque(false);
        panelBotonesSexo.setMaximumSize(new Dimension(Short.MAX_VALUE, 38));

        ImageIcon iconoMacho = cargarIconoHD("imagenes/emojis/macho.png", 14, 14);
        ImageIcon iconoHembra = cargarIconoHD("imagenes/emojis/hembra.png", 14, 14);

        // --- SECCIÓN SEXO (PÍLDORAS MINIMALISTAS CENTRADAS) ---
        // ❌ Eliminamos las líneas de cargarIconoHD de macho y hembra para no consumir memoria con imágenes

        // Botón Macho personalizado (Texto Centrado)
        rbMacho = new JRadioButton("Macho", animal.getSexo()) { // 🌟 Mantenemos el texto limpio
            {
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setHorizontalAlignment(SwingConstants.CENTER); // 🌟 Fuerza el centrado horizontal del texto
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                // ❌ Eliminamos el EmptyBorder izquierdo para que el texto ocupe el centro real
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Pintamos el fondo y los bordes según la selección usando los colores de la mascota
                if (isSelected()) {
                    setForeground(colorFinAnimal);
                    g2.setColor(new Color(255, 247, 237)); // Fondo sutil de selección
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(colorInicioAnimal); // Contorno con el color de la mascota
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
                } else {
                    setForeground(recursos.Color.INK);
                    g2.setColor(recursos.Color.BG); // Fondo gris sutil de tu paleta
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(recursos.Color.BORDER);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                }
                
                g2.dispose();
                super.paintComponent(g); // Swing dibuja el texto "Macho" centrado automáticamente acá
            }
        };

        // Botón Hembra personalizado (Texto Centrado)
        rbHembra = new JRadioButton("Hembra", !animal.getSexo()) {
            {
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setHorizontalAlignment(SwingConstants.CENTER); // 🌟 Fuerza el centrado horizontal del texto
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                // ❌ Eliminamos el EmptyBorder izquierdo para que el texto ocupe el centro real
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
                super.paintComponent(g); // Swing dibuja el texto "Hembra" centrado automáticamente acá
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
        txtFechaNac.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFormulario.add(txtFechaNac);
        panelFormulario.add(Box.createVerticalStrut(12));

        // Campo ESTADO ACTIVO
        JPanel panelCheck = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelCheck.setOpaque(false);
        panelCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkActivo = new JCheckBox("Paciente Activo en la Veterinaria", animal.isActivo());
        chkActivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkActivo.setOpaque(false);
        chkActivo.setForeground(recursos.Color.INK);
        panelCheck.add(chkActivo);
        panelFormulario.add(panelCheck);

        // --- 2. BOTONERA INFERIOR CON HOVER ---
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

        btnGuardar = new JButton("Guardar Cambios") {
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
                // Si hace hover adopta el color fin, si no, el inicio dinámico de la mascota
                g2.setColor(hover ? colorFinAnimal : colorInicioAnimal);
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
                
                // Mapeamos el radio button al sexo mutable del objeto
                // animal.setSexo(rbMacho.isSelected()); 

                if (!txtFechaNac.getText().isBlank()) {
                    animal.setFechaNacimiento(LocalDate.parse(txtFechaNac.getText().trim(), formato));
                }
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Por favor, verifica que los campos obligatorios y numéricos sean correctos.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.setPreferredSize(new Dimension(0, 42));
        btnGuardar.setPreferredSize(new Dimension(0, 42));
        panelBotonesBottom.add(btnCancelar);
        panelBotonesBottom.add(btnGuardar);

        // Ensamblado en la tarjeta blanca principal
        panelCuerpo.add(panelFormulario, BorderLayout.CENTER);
        panelCuerpo.add(panelBotonesBottom, BorderLayout.SOUTH);

        add(panelCuerpo, BorderLayout.CENTER);
    }

    // --- MÈTODOS AUXILIARES DEFINIDOS CORRECTAMENTE ---
    private JLabel crearLabelFormulario(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(148, 163, 184));
        lbl.setBorder(new EmptyBorder(0, 2, 4, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
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