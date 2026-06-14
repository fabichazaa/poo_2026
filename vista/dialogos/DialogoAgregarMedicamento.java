package vista.dialogos;

import controlador.ControladorVeterinaria;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import modelo.Medicamento;
import modelo.Vacuna;
import recursos.CargadorFuentes;

public class DialogoAgregarMedicamento extends JDialog {

    private final ControladorVeterinaria controlador;
    private boolean agregado = false;

    private JRadioButton radioMedicamento;
    private JRadioButton radioVacuna;
    private JTextField campoCodigoSenasa;
    private JTextField campoNombre;
    private JTextField campoFechaAplicacion;
    private JTextField campoFechaVencimiento;
    private JLabel lblErrorCodigo;
    private JLabel lblErrorNombre;
    private JLabel lblErrorFechas;

    public DialogoAgregarMedicamento(Window owner, ControladorVeterinaria controlador) {
        super((Frame) owner, "Agregar Medicamento / Vacuna", true);
        this.controlador = controlador;
        construir();
    }

    public boolean isAgregado() {
        return agregado;
    }

    private void construir() {
        setSize(520, 580);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(recursos.Color.BG);

        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(new EmptyBorder(24, 32, 24, 32));
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        // Título
        JLabel lblTitulo = new JLabel("Agregar medicamento al catálogo");
        lblTitulo.setFont(CargadorFuentes.cargar(16f));
        lblTitulo.setForeground(recursos.Color.INK);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblTitulo);

        panelCentral.add(Box.createVerticalStrut(4));

        JLabel lblSub = new JLabel("Defina si es medicamento o vacuna con sus propiedades");
        lblSub.setFont(CargadorFuentes.cargar(12f));
        lblSub.setForeground(new Color(100, 116, 139));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblSub);

        panelCentral.add(Box.createVerticalStrut(20));

        // Selector de tipo
        JPanel panelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelRadios.setOpaque(false);
        panelRadios.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelRadios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        radioMedicamento = new JRadioButton("Medicamento", true);
        radioVacuna = new JRadioButton("Vacuna");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(radioMedicamento);
        grupo.add(radioVacuna);
        radioMedicamento.setFont(CargadorFuentes.cargar(12f));
        radioVacuna.setFont(CargadorFuentes.cargar(12f));
        radioMedicamento.setOpaque(false);
        radioVacuna.setOpaque(false);
        radioMedicamento.addActionListener(e -> actualizarVisibilidad());
        radioVacuna.addActionListener(e -> actualizarVisibilidad());
        panelRadios.add(radioMedicamento);
        panelRadios.add(radioVacuna);
        panelCentral.add(panelRadios);

        panelCentral.add(Box.createVerticalStrut(16));

        // Código SENASA
        campoCodigoSenasa = new JTextField();
        campoCodigoSenasa.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        JLabel lblCodigo = new JLabel("Código SENASA");
        lblCodigo.setFont(CargadorFuentes.cargar(11f));
        lblCodigo.setForeground(new Color(71, 85, 105));
        lblCodigo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblCodigo);
        panelCentral.add(Box.createVerticalStrut(4));
        campoCodigoSenasa.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        campoCodigoSenasa.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(campoCodigoSenasa);

        lblErrorCodigo = new JLabel(" ");
        lblErrorCodigo.setFont(CargadorFuentes.cargar(10f));
        lblErrorCodigo.setForeground(recursos.Color.ERROR);
        lblErrorCodigo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblErrorCodigo);

        panelCentral.add(Box.createVerticalStrut(10));

        // Nombre del medicamento
        campoNombre = new JTextField();
        campoNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        JLabel lblNombre = new JLabel("Nombre del medicamento");
        lblNombre.setFont(CargadorFuentes.cargar(11f));
        lblNombre.setForeground(new Color(71, 85, 105));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblNombre);
        panelCentral.add(Box.createVerticalStrut(4));
        campoNombre.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        campoNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(campoNombre);

        lblErrorNombre = new JLabel(" ");
        lblErrorNombre.setFont(CargadorFuentes.cargar(10f));
        lblErrorNombre.setForeground(recursos.Color.ERROR);
        lblErrorNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblErrorNombre);

        panelCentral.add(Box.createVerticalStrut(16));

        // Vigencia en días (solo para vacunas)
        campoFechaAplicacion = new JTextField("365");
        campoFechaAplicacion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        JLabel lblAplic = new JLabel("Vigencia (días)");
        lblAplic.setFont(CargadorFuentes.cargar(11f));
        lblAplic.setForeground(new Color(71, 85, 105));
        lblAplic.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblAplic);
        panelCentral.add(Box.createVerticalStrut(4));
        campoFechaAplicacion.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        campoFechaAplicacion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(campoFechaAplicacion);

        panelCentral.add(Box.createVerticalStrut(10));

        // Placeholder for second field (unused, hidden when not vacuna)
        campoFechaVencimiento = new JTextField();
        campoFechaVencimiento.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        JLabel lblVenc = new JLabel("(Reservado)");
        lblVenc.setFont(CargadorFuentes.cargar(11f));
        lblVenc.setForeground(new Color(71, 85, 105));
        lblVenc.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblVenc);
        panelCentral.add(Box.createVerticalStrut(4));
        campoFechaVencimiento.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        campoFechaVencimiento.setAlignmentX(Component.LEFT_ALIGNMENT);
        campoFechaVencimiento.setVisible(false);
        panelCentral.add(campoFechaVencimiento);

        lblErrorFechas = new JLabel(" ");
        lblErrorFechas.setFont(CargadorFuentes.cargar(10f));
        lblErrorFechas.setForeground(recursos.Color.ERROR);
        lblErrorFechas.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblErrorFechas);

        panelCentral.add(Box.createVerticalStrut(16));

        JButton btnGuardar = new JButton("Agregar a catálogo");
        btnGuardar.setFont(CargadorFuentes.cargar(13f));
        btnGuardar.setBackground(recursos.Color.ACCENT_BLUE);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setOpaque(true);
        btnGuardar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> intentarGuardar());
        panelCentral.add(btnGuardar);

        add(panelCentral, BorderLayout.CENTER);

        actualizarVisibilidad();
    }

    private void actualizarVisibilidad() {
        boolean esVacuna = radioVacuna.isSelected();
        campoFechaAplicacion.setEnabled(esVacuna);
        campoFechaVencimiento.setEnabled(esVacuna);

        // Cambiar color de fondo para indicar si está habilitado
        if (esVacuna) {
            campoFechaAplicacion.setBackground(Color.WHITE);
            campoFechaVencimiento.setBackground(Color.WHITE);
        } else {
            campoFechaAplicacion.setBackground(new Color(245, 245, 245));
            campoFechaVencimiento.setBackground(new Color(245, 245, 245));
        }
    }

    private void intentarGuardar() {
        // Limpiar errores previos
        lblErrorCodigo.setText(" ");
        lblErrorNombre.setText(" ");
        lblErrorFechas.setText(" ");

        String codigo = campoCodigoSenasa.getText().trim();
        String nombre = campoNombre.getText().trim();

        // Validar código
        if (codigo.isEmpty()) {
            lblErrorCodigo.setText("El código SENASA es obligatorio.");
            return;
        }

        // Validar nombre
        if (nombre.isEmpty()) {
            lblErrorNombre.setText("El nombre del medicamento es obligatorio.");
            return;
        }

        // Si es Vacuna, validar vigencia
        if (radioVacuna.isSelected()) {
            int vigenciaDias;
            try {
                vigenciaDias = Integer.parseInt(campoFechaAplicacion.getText().trim());
                if (vigenciaDias <= 0) {
                    lblErrorFechas.setText("La vigencia debe ser un número positivo de días.");
                    return;
                }
            } catch (NumberFormatException ex) {
                lblErrorFechas.setText("Ingrese un número válido de días para la vigencia.");
                return;
            }

            Vacuna v = new Vacuna(codigo, nombre, vigenciaDias);
            controlador.getVeterinaria().agregarMedicamentoAlCatalogo(v);
        } else {
            Medicamento m = new Medicamento(codigo, nombre);
            controlador.getVeterinaria().agregarMedicamentoAlCatalogo(m);
        }

        agregado = true;
        JOptionPane.showMessageDialog(this, "Medicamento agregado al catálogo con éxito.");
        dispose();
    }
}

