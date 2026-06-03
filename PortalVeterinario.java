import objetos.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PortalVeterinario extends JFrame {
    // Search components
    private JTextField txtBuscarNombre;
    private JButton btnBuscar;

    // Modern Profile UI Components (Right Side Form)
    private JTextField txtNombre;
    private JTextField txtRaza;
    private JTextField txtNacimiento;
    private JTextField txtEdad;
    private JTextField txtSexo;

    // Left Square Photo Box and Bottom Alerts
    private JPanel panelFotoMascota;
    private JLabel lblTextoFoto;
    private JPanel panelAlertaSalud;
    private JLabel lblAlertaMensaje;

    // Data lists
    private ArrayList<Responsable> listaResponsables;

    public PortalVeterinario() {
        // Initialize data using your exact classes
        inicializarDatosDePrueba();

        // Window Configuration
        setTitle("San Roque - Portal Clínico Veterinario");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(245, 247, 250)); // Modern light-gray background

        // 1. TOP BAR: Modern Search Panel
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 225, 230)));

        JLabel lblBuscar = new JLabel("Buscar Paciente:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtBuscarNombre = new JTextField(15);
        txtBuscarNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        btnBuscar = new JButton("Ver Ficha");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuscar.setBackground(new Color(0, 122, 255)); // Modern iOS/Windows Blue
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscarNombre);
        panelBusqueda.add(btnBuscar);
        add(panelBusqueda, BorderLayout.NORTH);

        // 2. CENTER PIECE: The Dashboard Card
        JPanel panelDashboard = new JPanel(new BorderLayout(20, 20));
        panelDashboard.setBackground(Color.WHITE);
        panelDashboard.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 20, 10, 20),
                BorderFactory.createLineBorder(new Color(230, 235, 240), 1, true)
        ));

        // LEFT SIDE: The Photo Square Container
        panelFotoMascota = new JPanel(new BorderLayout());
        panelFotoMascota.setPreferredSize(new Dimension(180, 180));
        panelFotoMascota.setBackground(new Color(235, 240, 245)); // Gray placeholder
        panelFotoMascota.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 220), 1));
        
        lblTextoFoto = new JLabel("[ SIN FOTO ]", SwingConstants.CENTER);
        lblTextoFoto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTextoFoto.setForeground(new Color(120, 130, 140));
        panelFotoMascota.add(lblTextoFoto, BorderLayout.CENTER);
        
        JPanel contenedorFoto = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contenedorFoto.setBackground(Color.WHITE);
        contenedorFoto.add(panelFotoMascota);
        panelDashboard.add(contenedorFoto, BorderLayout.WEST);

        // RIGHT SIDE: Sleek Grid Form
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 12));
        panelFormulario.setBackground(Color.WHITE);

        // Styling helper for labels and modern non-editable fields
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        Font fieldsFont = new Font("Segoe UI", Font.PLAIN, 13);
        Color labelColor = new Color(70, 80, 90);

        panelFormulario.add(crearLabelForm("Nombre:", labelFont, labelColor));
        txtNombre = crearFieldForm(fieldsFont);
        panelFormulario.add(txtNombre);

        panelFormulario.add(crearLabelForm("Raza / Especie:", labelFont, labelColor));
        txtRaza = crearFieldForm(fieldsFont);
        panelFormulario.add(txtRaza);

        panelFormulario.add(crearLabelForm("Fecha Nacimiento:", labelFont, labelColor));
        txtNacimiento = crearFieldForm(fieldsFont);
        panelFormulario.add(txtNacimiento);

        panelFormulario.add(crearLabelForm("Edad Calculada:", labelFont, labelColor));
        txtEdad = crearFieldForm(fieldsFont);
        panelFormulario.add(txtEdad);

        panelFormulario.add(crearLabelForm("Sexo:", labelFont, labelColor));
        txtSexo = crearFieldForm(fieldsFont);
        panelFormulario.add(txtSexo);

        panelDashboard.add(panelFormulario, BorderLayout.CENTER);
        add(panelDashboard, BorderLayout.CENTER);

        // 3. BOTTOM BAR: Modern Health Status Notification Banner
        panelAlertaSalud = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelAlertaSalud.setBackground(new Color(210, 215, 225)); // Initial neutral gray
        
        lblAlertaMensaje = new JLabel("Ingrese el nombre de una mascota para comenzar el escaneo médico.");
        lblAlertaMensaje.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblAlertaMensaje.setForeground(new Color(50, 60, 70));
        panelAlertaSalud.add(lblAlertaMensaje);
        add(panelAlertaSalud, BorderLayout.SOUTH);

        // Search Action Trigger
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarBusqueda();
            }
        });
    }

    // Visual helpers to keep code clean and small
    private JLabel crearLabelForm(String texto, Font f, Color c) {
        JLabel label = new JLabel(texto);
        label.setFont(f);
        label.setForeground(c);
        return label;
    }

    private JTextField crearFieldForm(Font f) {
        JTextField field = new JTextField();
        field.setFont(f);
        field.setEditable(false);
        field.setBackground(Color.WHITE); // Uneditable, but white instead of boring gray
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(210, 215, 220))); // Sleek bottom line only
        return field;
    }

    /**
     * Logic Execution matching your exact architecture
     */
    /**
     * Logic Execution matching your exact architecture (Polymorphic version)
     */
    private void ejecutarBusqueda() {
        String nombreBuscado = txtBuscarNombre.getText().trim();

        if (nombreBuscado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, escribe un nombre.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Animal animalEncontrado = null;

        for (Responsable resp : listaResponsables) {
            for (Animal anim : resp.getMascotas()) {
                if (anim.getNombre().equalsIgnoreCase(nombreBuscado)) {
                    animalEncontrado = anim;
                    break;
                }
            }
            if (animalEncontrado != null) break;
        }

        if (animalEncontrado != null) {
            // 1. Populate Text fields using your exact getters
            txtNombre.setText(animalEncontrado.getNombre());
            txtSexo.setText(animalEncontrado.isSexo() ? "Macho ♂" : "Hembra ♀");
            txtEdad.setText(animalEncontrado.calcularEdad() + " años");
            
            // Format LocalDate nicely (DD/MM/YYYY)
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            txtNacimiento.setText(animalEncontrado.getFechaNacimiento().format(dtf));

            // -------------------------------------------------------------------------
            // NUEVA CONFIGURACIÓN DE IMAGEN Y DATOS POLIMÓRFICOS (SIN INSTANCEOF)
            // -------------------------------------------------------------------------
            
            // Limpiamos el texto que decía "[ SIN FOTO ]"
            lblTextoFoto.setText(""); 
            
            // Invocación polimórfica: Java decide en ejecución si va a Perro o Gato
            String rutaFoto = animalEncontrado.getRutaFoto(); 
            
            try {
                // Buscamos el recurso dentro de la carpeta 'src/imagenes/'
                ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/" + rutaFoto));
                // Escalamos la imagen para que calce perfecto en el recuadro de la interfaz
                Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                lblTextoFoto.setIcon(new ImageIcon(imagenEscalada));
            } catch (Exception ex) {
                // Si la imagen no se encuentra o el path está mal, muestra una alerta prolija en texto
                lblTextoFoto.setIcon(null);
                lblTextoFoto.setText("[ FOTO NO ENCONTRADA ]");
            }

            // Para la raza, si cambiaste tus clases hijas para que compartan un método getRaza() en Animal:
            // txtRaza.setText(animalEncontrado.getRaza());
            // Si no lo tocaste, provisoriamente lo dejamos así para que use el método de cada uno:
            if (animalEncontrado instanceof Perro) {
                txtRaza.setText(((Perro) animalEncontrado).getRaza() + " (Perro)");
            } else if (animalEncontrado instanceof Gato) {
                txtRaza.setText(((Gato) animalEncontrado).getRaza() + " (Gato)");
            }
            
            // -------------------------------------------------------------------------

            // 3. Process Health Banner Alerts using your Vacuna logic
            int vacunasVencidas = 0;
            for (Medicamento med : animalEncontrado.getHistorial().getMedicamentosRecetados()) {
                if (med instanceof Vacuna && ((Vacuna) med).estaVencida()) {
                    vacunasVencidas++;
                }
            }

            if (vacunasVencidas > 0) {
                panelAlertaSalud.setBackground(new Color(255, 230, 230)); // Flat pastel red
                lblAlertaMensaje.setText("🚨 ALERTA SANITARIA: La mascota registra " + vacunasVencidas + " vacuna/s vencida/s.");
                lblAlertaMensaje.setForeground(new Color(200, 20, 20));
            } else {
                panelAlertaSalud.setBackground(new Color(230, 245, 230)); // Flat pastel green
                lblAlertaMensaje.setText("✅ ESTADO SALUDABLE: Controles médicos y vacunas al día.");
                lblAlertaMensaje.setForeground(new Color(20, 140, 20));
            }

        } else {
            // Reset display if nothing is found
            txtNombre.setText(""); txtRaza.setText(""); txtNacimiento.setText(""); txtEdad.setText(""); txtSexo.setText("");
            panelFotoMascota.setBackground(new Color(235, 240, 245));
            lblTextoFoto.setIcon(null); // Borramos el icono anterior
            lblTextoFoto.setText("[ SIN FOTO ]");
            panelAlertaSalud.setBackground(new Color(210, 215, 225));
            lblAlertaMensaje.setText("Mascota no encontrada en los registros.");
            lblAlertaMensaje.setForeground(Color.BLACK);
            
            JOptionPane.showMessageDialog(this, "No se encontró ningún paciente con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void inicializarDatosDePrueba() {
        listaResponsables = new ArrayList<>();
        Direccion dir = new Direccion("Av. Melian", 1500, "Pilar");
        Responsable chiqui = new Responsable("12345678", "Claudio", "Chiqui", dir);
        listaResponsables.add(chiqui);

        // Dog: Hulk (Has a expired vaccine)
        Perro hulk = new Perro("Hulk", LocalDate.of(2022, 4, 15), true, chiqui, "Dogo de Burdeos");
        Vacuna vencida = new Vacuna("SENASA-1", "Antirrábica", LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1));
        hulk.getHistorial().recetarMedicamento(vencida);
        chiqui.agregarMascota(hulk);

        // Cat: Luna (Everything safe)
        Gato luna = new Gato("Luna", LocalDate.of(2024, 8, 20), false, chiqui, "Siamés");
        Vacuna alDia = new Vacuna("SENASA-2", "Triple Felina", LocalDate.of(2026, 1, 1), LocalDate.of(2027, 8, 1));
        luna.getHistorial().recetarMedicamento(alDia);
        chiqui.agregarMascota(luna);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PortalVeterinario().setVisible(true));
    }
}