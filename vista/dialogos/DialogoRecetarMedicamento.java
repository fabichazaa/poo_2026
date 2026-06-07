package vista.dialogos;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.*;
import modelo.*;
import recursos.CargadorFuentes;

public class DialogoRecetarMedicamento extends JDialog {

    private final ControladorVeterinaria controlador;
    private final Animal animal;
    private boolean recetado = false;

    private JRadioButton radioMedicamento;
    private JRadioButton radioVacuna;
    private JComboBox<Medicamento> comboMedicamento;
    private JTextField campoFechaAplicacion;
    private JTextField campoFechaVencimiento;
    private JLabel lblError;

    public DialogoRecetarMedicamento(Frame owner, ControladorVeterinaria controlador, Animal animal) {
        super(owner, "Recetar / Aplicar — " + animal.getNombre(), true);
        this.controlador = controlador;
        this.animal = animal;
        construir();
    }

    public boolean isRecetado() {
        return recetado;
    }

    private void construir() {
        setSize(520, 460);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(241, 245, 249));

        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(new EmptyBorder(24, 32, 24, 32));
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Recetar medicamento / Aplicar vacuna");
        lblTitulo.setFont(CargadorFuentes.cargar(16f));
        lblTitulo.setForeground(new Color(30, 41, 59));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblTitulo);

        panelCentral.add(Box.createVerticalStrut(4));

        JLabel lblSub = new JLabel("Para: " + animal.getNombre() + " (" + animal.getEspecie() + ")");
        lblSub.setFont(CargadorFuentes.cargar(12f));
        lblSub.setForeground(new Color(100, 116, 139));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblSub);

        panelCentral.add(Box.createVerticalStrut(20));

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

        panelCentral.add(Box.createVerticalStrut(12));

        comboMedicamento = new JComboBox<>(
            controlador.getVeterinaria().getCatalogoMedicamentos().toArray(new Medicamento[0])
        );
        comboMedicamento.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel lbl = new JLabel(value == null ? "" :
                value.getCodigoSenasa() + " — " + value.getNombreMedicamento());
            if (isSelected) {
                lbl.setOpaque(true);
                lbl.setBackground(new Color(13, 148, 136));
                lbl.setForeground(Color.WHITE);
            }
            return lbl;
        });
        comboMedicamento.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        JLabel lblCombo = new JLabel("Producto del catálogo");
        lblCombo.setFont(CargadorFuentes.cargar(11f));
        lblCombo.setForeground(new Color(71, 85, 105));
        lblCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblCombo);
        panelCentral.add(Box.createVerticalStrut(4));
        comboMedicamento.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(comboMedicamento);

        panelCentral.add(Box.createVerticalStrut(16));

        campoFechaAplicacion = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        campoFechaAplicacion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        JLabel lblAplic = new JLabel("Fecha de aplicación (dd/MM/yyyy)");
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

        panelCentral.add(Box.createVerticalStrut(12));

        campoFechaVencimiento = new JTextField(LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        campoFechaVencimiento.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        JLabel lblVenc = new JLabel("Fecha de vencimiento (dd/MM/yyyy)");
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
        panelCentral.add(campoFechaVencimiento);

        panelCentral.add(Box.createVerticalStrut(8));

        lblError = new JLabel(" ");
        lblError.setFont(CargadorFuentes.cargar(11f));
        lblError.setForeground(new Color(220, 38, 38));
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblError);

        panelCentral.add(Box.createVerticalStrut(16));

        JButton btnGuardar = new JButton("Confirmar");
        btnGuardar.setFont(CargadorFuentes.cargar(13f));
        btnGuardar.setBackground(new Color(13, 148, 136));
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
    }

    private void intentarGuardar() {
        Medicamento med = (Medicamento) comboMedicamento.getSelectedItem();
        if (med == null) {
            lblError.setText("Seleccioná un producto del catálogo.");
            return;
        }

        if (radioMedicamento.isSelected()) {
            controlador.recetarMedicamento(animal, med);
        } else {
            LocalDate aplic, venc;
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                aplic = LocalDate.parse(campoFechaAplicacion.getText().trim(), fmt);
                venc = LocalDate.parse(campoFechaVencimiento.getText().trim(), fmt);
            } catch (DateTimeParseException ex) {
                lblError.setText("Fechas inválidas. Usá dd/MM/yyyy.");
                return;
            }
            Vacuna v = new Vacuna(med.getCodigoSenasa(), med.getNombreMedicamento(), aplic, venc);
            controlador.recetarMedicamento(animal, v);
        }

        recetado = true;
        JOptionPane.showMessageDialog(this, "Operación registrada con éxito.");
        dispose();
    }
}
