package vista.dialogos;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controlador.ControladorVeterinaria;
import modelo.Animal;
import modelo.Direccion;
import modelo.Responsable;

public class DialogoEditarPaciente extends JDialog {

    private Animal animal; 
    private final controlador.ControladorVeterinaria controlador;
    private final boolean esModoEdicion; 

    // Componentes - Selección de Especie 
    private String especieSeleccionada = "Perro"; 
    private JPanel panelGrillaEspecies;

    // Componentes - Pestaña Animal
    private JTextField txtNombre;
    private JTextField txtRaza;
    private JTextField txtPeso;
    private JTextField txtFechaNac;
    private JRadioButton rbMacho;
    private JRadioButton rbHembra;
    private JCheckBox chkActivo;
    
    // Componentes - Pestaña Responsable
    private JComboBox<String> cmbModoResponsable;
    private JComboBox<Responsable> cmbResponsablesExistentes;
    private JPanel panelDropdownExistente;
    private JPanel panelCamposTextoResponsable;
    private JTextField txtDniDueno;
    private JTextField txtNombreDueno;
    private JTextField txtApellidoDueno;
    private JTextField txtCelularDueno;
    private JTextField txtCalleDueno;
    private JTextField txtAlturaDueno; 
    private JTextField txtLocalidadDueno; 

    // Atributos de respaldo para evitar el error de scope (effectively final)
    private String calleOriginal = "";
    private String alturaOriginal = "";
    private String localidadOriginal = "";

    private JButton btnGuardar;
    private JTabbedPane tabsFormulario;

    // PALETA DE COLORES CORPORATIVOS UNIFICADA (Estilo Figma)
    private final Color VERDE_PRIMARY = new Color(13, 148, 136);   
    private final Color VERDE_HOVER = new Color(15, 118, 110);     
    private final Color VERDE_SUAVE = new Color(204, 251, 241);    

    public DialogoEditarPaciente(Frame padre, Animal animalExistente) {
        super(padre, (animalExistente != null) ? "Editar paciente: " + animalExistente.getNombre() : "Registrar nuevo paciente", true); 
        
        this.animal = animalExistente;
        this.esModoEdicion = (animalExistente != null);
        this.controlador = ControladorVeterinaria.getInstancia();

        if (!esModoEdicion) {
            this.especieSeleccionada = "Perro"; 
        } else {
            this.especieSeleccionada = animal.getEspecie();
        }

        setSize(480, esModoEdicion ? 590 : 640);
        setLocationRelativeTo(padre); 
        setLayout(new BorderLayout());

        initComponentes();
    }

    private void initComponentes() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Contenedor de la tarjeta blanca
        JPanel panelCuerpo = new JPanel(new BorderLayout());
        panelCuerpo.setBackground(Color.WHITE);

        // --- CREACIÓN DEL TABBED PANE OPTIMIZADO (SIN RAYAS VERTICALES) ---
        tabsFormulario = new JTabbedPane();
        tabsFormulario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabsFormulario.setBackground(Color.WHITE); 
        tabsFormulario.setOpaque(true);
        
        tabsFormulario.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {}
            @Override protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {}
        });

        // ========================================================
        // 🐾 PESTAÑA 1: DATOS DEL ANIMAL
        // ========================================================
        JPanel panelTabAnimal = new JPanel();
        panelTabAnimal.setBackground(Color.WHITE);
        panelTabAnimal.setLayout(new BoxLayout(panelTabAnimal, BoxLayout.Y_AXIS));
        panelTabAnimal.setBorder(new EmptyBorder(25, 24, 20, 24));

        // 🌟 LA SOLUCIÓN AL CORRIMIENTO: Envolvemos la grilla adentro de un FlowLayout contenedor protector
        if (!esModoEdicion) {
            panelTabAnimal.add(crearLabelFormulario("SELECCIONAR ESPECIE *"));
            
            // Este panel protector absorbe la tensión horizontal del BoxLayout impidiendo deformaciones
            JPanel panelContenedorEspeciesHD = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            panelContenedorEspeciesHD.setOpaque(false);
            panelContenedorEspeciesHD.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));
            panelContenedorEspeciesHD.setAlignmentX(Component.LEFT_ALIGNMENT);

            panelGrillaEspecies = new JPanel(new GridLayout(1, 5, 6, 0)); 
            panelGrillaEspecies.setOpaque(false);
            // El ancho de 412px es la medida exacta de la tarjeta interna menos los paddings laterales
            panelGrillaEspecies.setPreferredSize(new Dimension(412, 38)); 
            
            String[] especiesDisponibles = {"Perro", "Gato", "Tortuga", "Loro", "Conejo"};
            
            for (int i = 0; i < especiesDisponibles.length; i++) {
                panelGrillaEspecies.add(crearBotonPildoraEspecie(especiesDisponibles[i]));
            }
            
            panelContenedorEspeciesHD.add(panelGrillaEspecies);
            panelTabAnimal.add(panelContenedorEspeciesHD);
            panelTabAnimal.add(Box.createVerticalStrut(14));
        }

        panelTabAnimal.add(crearLabelFormulario("NOMBRE *"));
        txtNombre = crearTextFieldFormulario(esModoEdicion ? animal.getNombre() : "");
        txtNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTabAnimal.add(txtNombre);
        panelTabAnimal.add(Box.createVerticalStrut(12));

        panelTabAnimal.add(crearLabelFormulario("RAZA"));
        txtRaza = crearTextFieldFormulario(esModoEdicion ? animal.getRaza() : "");
        txtRaza.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTabAnimal.add(txtRaza);
        panelTabAnimal.add(Box.createVerticalStrut(12));

        JPanel panelFilaDividida = new JPanel(new GridLayout(1, 2, 16, 0));
        panelFilaDividida.setOpaque(false);
        panelFilaDividida.setMaximumSize(new Dimension(Short.MAX_VALUE, 65));
        panelFilaDividida.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel colSexo = new JPanel();
        colSexo.setOpaque(false);
        colSexo.setLayout(new BoxLayout(colSexo, BoxLayout.Y_AXIS));
        colSexo.add(crearLabelFormulario("SEXO"));
        
        JPanel panelBotonesSexo = new JPanel(new GridLayout(1, 2, 8, 0));
        panelBotonesSexo.setOpaque(false);
        panelBotonesSexo.setMaximumSize(new Dimension(Short.MAX_VALUE, 38));

        boolean sexoInicial = esModoEdicion ? animal.getSexo() : true; 
        rbMacho = new JRadioButton("Macho", sexoInicial) {
            {
                setFocusPainted(false); setContentAreaFilled(false); setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setHorizontalAlignment(SwingConstants.CENTER); setFont(new Font("Segoe UI", Font.BOLD, 13));
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected()) {
                    setForeground(VERDE_PRIMARY); g2.setColor(VERDE_SUAVE); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(VERDE_PRIMARY); g2.setStroke(new BasicStroke(1.5f)); g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
                } else {
                    setForeground(recursos.Color.INK); g2.setColor(recursos.Color.BG); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(recursos.Color.BORDER); g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                }
                g2.dispose(); super.paintComponent(g);
            }
        };

        rbHembra = new JRadioButton("Hembra", !sexoInicial) {
            {
                setFocusPainted(false); setContentAreaFilled(false); setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setHorizontalAlignment(SwingConstants.CENTER); setFont(new Font("Segoe UI", Font.BOLD, 13));
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected()) {
                    setForeground(VERDE_PRIMARY); g2.setColor(VERDE_SUAVE); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(VERDE_PRIMARY); g2.setStroke(new BasicStroke(1.5f)); g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
                } else {
                    setForeground(recursos.Color.INK); g2.setColor(recursos.Color.BG); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(recursos.Color.BORDER); g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                }
                g2.dispose(); super.paintComponent(g);
            }
        };

        ButtonGroup grupoSexo = new ButtonGroup();
        grupoSexo.add(rbMacho); grupoSexo.add(rbHembra);
        
        rbMacho.addActionListener(e -> { rbMacho.repaint(); rbHembra.repaint(); });
        rbHembra.addActionListener(e -> { rbMacho.repaint(); rbHembra.repaint(); });

        panelBotonesSexo.add(rbMacho); panelBotonesSexo.add(rbHembra);
        colSexo.add(panelBotonesSexo); panelFilaDividida.add(colSexo);

        JPanel colPeso = new JPanel();
        colPeso.setOpaque(false); colPeso.setLayout(new BoxLayout(colPeso, BoxLayout.Y_AXIS));
        colPeso.add(crearLabelFormulario("PESO (KG) *"));
        
        txtPeso = crearTextFieldFormulario(esModoEdicion ? String.valueOf(animal.getPeso()) : "");
        txtPeso.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        colPeso.add(txtPeso); 
        panelFilaDividida.add(colPeso);

        panelTabAnimal.add(panelFilaDividida);
        panelTabAnimal.add(Box.createVerticalStrut(12));

        panelTabAnimal.add(crearLabelFormulario("FECHA DE NAC. (DD/MM/AAAA)"));
        String fechaStr = (esModoEdicion && animal.getFechaNacimiento() != null) ? animal.getFechaNacimiento().format(formato) : "";
        txtFechaNac = crearTextFieldFormulario(fechaStr);
        txtFechaNac.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTabAnimal.add(txtFechaNac);
        panelTabAnimal.add(Box.createVerticalStrut(12));

        JPanel panelCheck = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelCheck.setOpaque(false); panelCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkActivo = new JCheckBox("Paciente Activo en la Veterinaria", esModoEdicion ? animal.isActivo() : true);
        chkActivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkActivo.setOpaque(false); chkActivo.setForeground(recursos.Color.INK);
        panelCheck.add(chkActivo); panelTabAnimal.add(panelCheck);
        
        panelTabAnimal.add(Box.createVerticalGlue());


        // ========================================================
        // 👤 PESTAÑA 2: DATOS DEL RESPONSABLE
        // ========================================================
        JPanel panelTabResponsable = new JPanel();
        panelTabResponsable.setBackground(Color.WHITE);
        panelTabResponsable.setLayout(new BoxLayout(panelTabResponsable, BoxLayout.Y_AXIS));
        panelTabResponsable.setBorder(new EmptyBorder(25, 24, 20, 24));

        Responsable resp = esModoEdicion ? animal.getResponsable() : null;

        panelTabResponsable.add(crearLabelFormulario("ACCION SOBRE EL RESPONSABLE"));
        cmbModoResponsable = new JComboBox<>();
        
        if (!esModoEdicion) {
            cmbModoResponsable.setModel(new DefaultComboBoxModel<>(new String[]{
                "Asignar Dueño Existente", 
                "Registrar y Asignar Nuevo Dueño"
            }));
        } else {
            cmbModoResponsable.setModel(new DefaultComboBoxModel<>(new String[]{
                "Editar Responsable Actual", 
                "Asignar Dueño Existente", 
                "Registrar y Asignar Nuevo Dueño"
            }));
        }

        cmbModoResponsable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbModoResponsable.setMaximumSize(new Dimension(Short.MAX_VALUE, 38));
        cmbModoResponsable.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTabResponsable.add(cmbModoResponsable);
        panelTabResponsable.add(Box.createVerticalStrut(12));

        panelDropdownExistente = new JPanel();
        panelDropdownExistente.setOpaque(false);
        panelDropdownExistente.setLayout(new BoxLayout(panelDropdownExistente, BoxLayout.Y_AXIS));
        panelDropdownExistente.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDropdownExistente.setVisible(!esModoEdicion); 

        panelDropdownExistente.add(crearLabelFormulario("SELECCIONAR RESPONSABLE DE LA LISTA"));
        java.util.List<Responsable> listaVete = controlador.getVeterinaria().getListaClientes();
        cmbResponsablesExistentes = new JComboBox<>(new java.util.Vector<>(listaVete));
        cmbResponsablesExistentes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbResponsablesExistentes.setMaximumSize(new Dimension(Short.MAX_VALUE, 38));
        cmbResponsablesExistentes.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDropdownExistente.add(cmbResponsablesExistentes);
        
        panelTabResponsable.add(panelDropdownExistente);

        panelCamposTextoResponsable = new JPanel();
        panelCamposTextoResponsable.setOpaque(false);
        panelCamposTextoResponsable.setLayout(new BoxLayout(panelCamposTextoResponsable, BoxLayout.Y_AXIS));
        panelCamposTextoResponsable.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCamposTextoResponsable.setVisible(esModoEdicion); 

        JPanel panelFilaDocumento = new JPanel(new GridBagLayout());
        panelFilaDocumento.setOpaque(false);
        panelFilaDocumento.setMaximumSize(new Dimension(Short.MAX_VALUE, 65));
        panelFilaDocumento.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        GridBagConstraints gbcDoc = new GridBagConstraints();
        gbcDoc.fill = GridBagConstraints.HORIZONTAL; gbcDoc.weighty = 1.0;

        JPanel colDni = new JPanel(); colDni.setOpaque(false); colDni.setLayout(new BoxLayout(colDni, BoxLayout.Y_AXIS));
        colDni.add(crearLabelFormulario("DNI / DOCUMENTO *"));
        txtDniDueno = crearTextFieldFormulario(resp != null ? resp.getDNI() : "");
        colDni.add(txtDniDueno);
        gbcDoc.gridx = 0; gbcDoc.weightx = 0.45; gbcDoc.insets = new Insets(0, 0, 0, 12);
        panelFilaDocumento.add(colDni, gbcDoc);

        JPanel colCel = new JPanel(); colCel.setOpaque(false); colCel.setLayout(new BoxLayout(colCel, BoxLayout.Y_AXIS));
        colCel.add(crearLabelFormulario("TELÉFONO / CELULAR *"));
        txtCelularDueno = crearTextFieldFormulario(resp != null ? resp.getCelular() : "");
        colCel.add(txtCelularDueno);
        gbcDoc.gridx = 1; gbcDoc.weightx = 0.55; gbcDoc.insets = new Insets(0, 0, 0, 0);
        panelFilaDocumento.add(colCel, gbcDoc);

        panelCamposTextoResponsable.add(panelFilaDocumento);
        panelCamposTextoResponsable.add(Box.createVerticalStrut(10));

        JPanel panelFilaNombreCompuesto = new JPanel(new GridLayout(1, 2, 12, 0));
        panelFilaNombreCompuesto.setOpaque(false);
        panelFilaNombreCompuesto.setMaximumSize(new Dimension(Short.MAX_VALUE, 65));
        panelFilaNombreCompuesto.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel colNom = new JPanel(); colNom.setOpaque(false); colNom.setLayout(new BoxLayout(colNom, BoxLayout.Y_AXIS));
        colNom.add(crearLabelFormulario("NOMBRE *"));
        txtNombreDueno = crearTextFieldFormulario(resp != null ? resp.getNombre() : "");
        colNom.add(txtNombreDueno);

        JPanel colApe = new JPanel(); colApe.setOpaque(false); colApe.setLayout(new BoxLayout(colApe, BoxLayout.Y_AXIS));
        colApe.add(crearLabelFormulario("APELLIDO *"));
        txtApellidoDueno = crearTextFieldFormulario(resp != null ? resp.getApellido() : "");
        colApe.add(txtApellidoDueno);

        panelFilaNombreCompuesto.add(colNom);
        panelFilaNombreCompuesto.add(colApe);
        panelCamposTextoResponsable.add(panelFilaNombreCompuesto);
        panelCamposTextoResponsable.add(Box.createVerticalStrut(10));

        if (resp != null && resp.getDireccion() != null) {
            Direccion dir = resp.getDireccion();
            calleOriginal = dir.getCalle();
            alturaOriginal = dir.getNumero() > 0 ? String.valueOf(dir.getNumero()) : ""; 
            localidadOriginal = dir.getLocalidad();
        }

        JPanel panelFilaDireccionTriple = new JPanel(new GridBagLayout());
        panelFilaDireccionTriple.setOpaque(false);
        panelFilaDireccionTriple.setMaximumSize(new Dimension(Short.MAX_VALUE, 65));
        panelFilaDireccionTriple.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        GridBagConstraints gbcDir = new GridBagConstraints();
        gbcDir.fill = GridBagConstraints.HORIZONTAL; gbcDir.weighty = 1.0;

        JPanel colCalle = new JPanel(); colCalle.setOpaque(false); colCalle.setLayout(new BoxLayout(colCalle, BoxLayout.Y_AXIS));
        colCalle.add(crearLabelFormulario("CALLE *"));
        txtCalleDueno = crearTextFieldFormulario(calleOriginal);
        colCalle.add(txtCalleDueno);
        gbcDir.gridx = 0; gbcDir.weightx = 0.50; gbcDir.insets = new Insets(0, 0, 0, 10); 
        panelFilaDireccionTriple.add(colCalle, gbcDir);

        JPanel colAltura = new JPanel(); colAltura.setOpaque(false); colAltura.setLayout(new BoxLayout(colAltura, BoxLayout.Y_AXIS));
        colAltura.add(crearLabelFormulario("N° *"));
        txtAlturaDueno = crearTextFieldFormulario(alturaOriginal);
        colAltura.add(txtAlturaDueno);
        gbcDir.gridx = 1; gbcDir.weightx = 0.20; gbcDir.insets = new Insets(0, 0, 0, 10); 
        panelFilaDireccionTriple.add(colAltura, gbcDir);

        JPanel colLocalidad = new JPanel(); colLocalidad.setOpaque(false); colLocalidad.setLayout(new BoxLayout(colLocalidad, BoxLayout.Y_AXIS));
        colLocalidad.add(crearLabelFormulario("LOCALIDAD *"));
        txtLocalidadDueno = crearTextFieldFormulario(localidadOriginal);
        colLocalidad.add(txtLocalidadDueno);
        gbcDir.gridx = 2; gbcDir.weightx = 0.30; gbcDir.insets = new Insets(0, 0, 0, 0); 
        panelFilaDireccionTriple.add(colLocalidad, gbcDir);

        panelCamposTextoResponsable.add(panelFilaDireccionTriple);
        panelTabResponsable.add(panelCamposTextoResponsable);
        
        panelTabResponsable.add(Box.createVerticalGlue()); 

        cmbModoResponsable.addActionListener(e -> {
            String seleccion = cmbModoResponsable.getSelectedItem().toString();
            if (seleccion.equals("Editar Responsable Actual")) {
                panelDropdownExistente.setVisible(false);
                panelCamposTextoResponsable.setVisible(true);
                txtDniDueno.setEditable(false);
                txtDniDueno.setText(resp != null ? resp.getDNI() : "");
                txtNombreDueno.setText(resp != null ? resp.getNombre() : "");
                txtApellidoDueno.setText(resp != null ? resp.getApellido() : "");
                txtCelularDueno.setText(resp != null ? resp.getCelular() : "");
                txtCalleDueno.setText(calleOriginal);
                txtAlturaDueno.setText(alturaOriginal);
                txtLocalidadDueno.setText(localidadOriginal);
            } else if (seleccion.equals("Asignar Dueño Existente")) {
                panelDropdownExistente.setVisible(true);
                panelCamposTextoResponsable.setVisible(false);
            } else { 
                panelDropdownExistente.setVisible(false);
                panelCamposTextoResponsable.setVisible(true);
                txtDniDueno.setEditable(true);
                txtDniDueno.setText("");
                txtNombreDueno.setText("");
                txtApellidoDueno.setText("");
                txtCelularDueno.setText("");
                txtCalleDueno.setText("");
                txtAlturaDueno.setText("");
                txtLocalidadDueno.setText("");
            }
            panelTabResponsable.revalidate();
            panelTabResponsable.repaint();
        });

        if (esModoEdicion && resp != null) txtDniDueno.setEditable(false);

        tabsFormulario.addTab("Datos del animal", panelTabAnimal);
        tabsFormulario.addTab("Responsable", panelTabResponsable);
        
        actualizarEstiloPestanas(tabsFormulario);

        // --- 4. BOTONERA INFERIOR ---
        JPanel panelGridBotonesBottom = new JPanel(new GridLayout(1, 2, 12, 0));
        panelGridBotonesBottom.setOpaque(false);
        panelGridBotonesBottom.setBorder(new EmptyBorder(10, 24, 24, 24));

        JButton btnCancel = new JButton("Cancelar") {
            private boolean hover = false;
            {
                setFont(new Font("Segoe UI", Font.BOLD, 14)); setFocusPainted(false);
                setContentAreaFilled(false); setBorderPainted(false);
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
                setForeground(new Color(71, 85, 105)); g2.dispose(); super.paintComponent(g);
            }
        };
        btnCancel.addActionListener(e -> dispose());

        btnGuardar = new JButton(esModoEdicion ? "Guardar Cambios" : "Registrar Paciente") {
            private boolean hover = false;
            {
                setFont(new Font("Segoe UI", Font.BOLD, 14)); setFocusPainted(false);
                setContentAreaFilled(false); setBorderPainted(false);
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
                g2.setColor(hover ? VERDE_HOVER : VERDE_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                setForeground(Color.WHITE); g2.dispose(); super.paintComponent(g);
            }
        };
        
        btnGuardar.addActionListener(e -> {
            try {
                String nombreA = txtNombre.getText().trim();
                String razaA = txtRaza.getText().trim();
                float pesoA = Float.parseFloat(txtPeso.getText().trim());
                boolean activoA = chkActivo.isSelected();
                boolean sexoA = rbMacho.isSelected();
                LocalDate fechaNacA = txtFechaNac.getText().isBlank() ? null : LocalDate.parse(txtFechaNac.getText().trim(), formato);

                if (nombreA.isEmpty()) throw new IllegalArgumentException();

                if (!esModoEdicion) {
                    if (especieSeleccionada.equals("Perro")) {
                        this.animal = new modelo.Perro(nombreA, fechaNacA, sexoA, pesoA, razaA);
                    } else if (especieSeleccionada.equals("Gato")) {
                        this.animal = new modelo.Gato(nombreA, fechaNacA, sexoA, pesoA, razaA);
                    } else if (especieSeleccionada.equals("Tortuga")) {
                        this.animal = new modelo.Tortuga(nombreA, fechaNacA, sexoA, pesoA, razaA);
                    } else if (especieSeleccionada.equals("Loro")) {
                        this.animal = new modelo.Loro(nombreA, fechaNacA, sexoA, pesoA, razaA);
                    } else if (especieSeleccionada.equals("Conejo")) {
                        this.animal = new modelo.Conejo(nombreA, fechaNacA, sexoA, pesoA, razaA);
                    }
                } else {
                    animal.setNombre(nombreA);
                    animal.setRaza(razaA);
                    animal.setPeso(pesoA);
                    animal.setActivo(activoA);
                    animal.setSexo(sexoA);
                    if (fechaNacA != null) animal.setFechaNacimiento(fechaNacA);
                }

                String seleccionModo = cmbModoResponsable.getSelectedItem().toString();
                int alturaInt = txtAlturaDueno.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtAlturaDueno.getText().trim());
                String localidadTexto = txtLocalidadDueno.getText().trim().isEmpty() ? "Pilar" : txtLocalidadDueno.getText().trim();
                Direccion nueva_direccion = new Direccion(txtCalleDueno.getText().trim(), alturaInt, localidadTexto);

                if (seleccionModo.equals("Editar Responsable Actual")) {
                    if (animal.getResponsable() != null) {
                        Responsable r = animal.getResponsable();
                        r.setNombre(txtNombreDueno.getText().trim());
                        r.setApellido(txtApellidoDueno.getText().trim());
                        r.setCelular(txtCelularDueno.getText().trim());
                        r.setDireccion(nueva_direccion);
                    }
                } 
                else if (seleccionModo.equals("Asignar Dueño Existente")) {
                    Responsable seleccionado = (Responsable) cmbResponsablesExistentes.getSelectedItem();
                    if (seleccionado != null) animal.setResponsable(seleccionado);
                } 
                else if (seleccionModo.equals("Registrar y Asignar Nuevo Dueño")) {
                    Responsable nuevo = new Responsable(
                        txtDniDueno.getText().trim(),
                        txtNombreDueno.getText().trim(),
                        txtApellidoDueno.getText().trim(),
                        txtCelularDueno.getText().trim(),
                        nueva_direccion
                    );
                    controlador.getVeterinaria().getListaClientes().add(nuevo);
                    animal.setResponsable(nuevo);
                }

                if (!esModoEdicion) {
                    controlador.getVeterinaria().getPacientesRegistrados().add(animal);
                    
                    // CARTEL DE ÉXITO: Para un registro nuevo
                    JOptionPane.showMessageDialog(this, 
                        "¡Paciente registrado con éxito en el sistema!", 
                        "Operación Exitosa", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // CARTEL DE ÉXITO: Para cuando se edita un paciente existente
                    JOptionPane.showMessageDialog(this, 
                        "Los cambios se han guardado correctamente.", 
                        "Operación Exitosa", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
                
                dispose();
            } catch (NumberFormatException numEx) {
                JOptionPane.showMessageDialog(this, "Verifica que el Peso y el N° de calle sean numéricos correctos.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Por favor, completa los campos obligatorios (*).", "Error de validación", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelGridBotonesBottom.add(btnCancel);
        panelGridBotonesBottom.add(btnGuardar);

        panelCuerpo.add(tabsFormulario, BorderLayout.CENTER);
        panelCuerpo.add(panelGridBotonesBottom, BorderLayout.SOUTH);

        add(panelCuerpo, BorderLayout.CENTER);
    }

    private JButton crearBotonPildoraEspecie(String nombre) {
        JButton btn = new JButton(nombre) {
            {
                setFocusPainted(false); setContentAreaFilled(false); setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                setBorder(new EmptyBorder(6, 10, 6, 10));
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean activo = especieSeleccionada.equals(nombre);
                if (activo) {
                    setForeground(VERDE_PRIMARY); g2.setColor(VERDE_SUAVE); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    g2.setColor(VERDE_PRIMARY); g2.setStroke(new BasicStroke(1.5f)); g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 16, 16);
                } else {
                    setForeground(new Color(100, 116, 139)); g2.setColor(new Color(248, 250, 252)); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    g2.setColor(new Color(226, 232, 240)); g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                }
                g2.dispose(); super.paintComponent(g);
            }
        };
        
        btn.addActionListener(e -> {
            especieSeleccionada = nombre;
            JPanel parent = (JPanel) btn.getParent();
            if (parent != null) parent.repaint();
        });
        
        return btn;
    }

    private void actualizarEstiloPestanas(JTabbedPane tabs) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            String titulo = tabs.getTitleAt(i);
            final int indicePestana = i;
            
            JLabel lblTabCustom = new JLabel(titulo, SwingConstants.CENTER) {
                private boolean mouseEncima = false;
                {
                    setFont(new Font("Segoe UI", Font.BOLD, 13)); setPreferredSize(new Dimension(200, 40)); 
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseEntered(java.awt.event.MouseEvent e) { mouseEncima = true; repaint(); }
                        public void mouseExited(java.awt.event.MouseEvent e) { mouseEncima = false; repaint(); }
                        public void mousePressed(java.awt.event.MouseEvent e) { tabs.setSelectedIndex(indicePestana); }
                    });
                }
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    boolean estaSeleccionada = (tabs.getSelectedIndex() == indicePestana);
                    if (estaSeleccionada) {
                        setForeground(VERDE_PRIMARY); g2.setColor(VERDE_SUAVE); g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 14, 14);
                    } else if (mouseEncima) {
                        setForeground(VERDE_HOVER); g2.setColor(new Color(241, 245, 249)); g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 14, 14);
                    } else { setForeground(new Color(148, 163, 184)); }
                    g2.dispose(); super.paintComponent(g);
                }
            };
            tabs.setTabComponentAt(i, lblTabCustom);
        }
        tabs.addChangeListener(e -> tabs.repaint());
    }

    private JLabel crearLabelFormulario(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11)); lbl.setForeground(new Color(148, 163, 184));
        lbl.setBorder(new EmptyBorder(0, 2, 4, 0)); lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField crearTextFieldFormulario(String valorInicial) {
        JTextField tf = new JTextField(valorInicial) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(recursos.Color.BG); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(recursos.Color.BORDER); g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose(); super.paintComponent(g);
            }
        };
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13)); tf.setForeground(recursos.Color.INK); tf.setOpaque(false);
        tf.setMaximumSize(new Dimension(Short.MAX_VALUE, 38)); tf.setPreferredSize(new Dimension(Short.MAX_VALUE, 38));
        tf.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return tf;
    }
}