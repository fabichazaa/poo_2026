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
    private JTextField campoVigenciaDias;
    private JLabel lblError;
    private final boolean modoVacunaExclusivo;

    public DialogoRecetarMedicamento(Frame owner, ControladorVeterinaria controlador, Animal animal) {
        this((Window) owner, controlador, animal, false);
    }

    public DialogoRecetarMedicamento(Frame owner, ControladorVeterinaria controlador, Animal animal, boolean modoVacunaExclusivo) {
        this((Window) owner, controlador, animal, modoVacunaExclusivo);
    }

    public DialogoRecetarMedicamento(Dialog owner, ControladorVeterinaria controlador, Animal animal, boolean modoVacunaExclusivo) {
        this((Window) owner, controlador, animal, modoVacunaExclusivo);
    }

    private DialogoRecetarMedicamento(Window owner, ControladorVeterinaria controlador, Animal animal, boolean modoVacunaExclusivo) {
        super(owner, modoVacunaExclusivo ? "Aplicar vacuna — " + animal.getNombre() : "Recetar medicamento — " + animal.getNombre(), Dialog.ModalityType.APPLICATION_MODAL);
        this.controlador = controlador;
        this.animal = animal;
        this.modoVacunaExclusivo = modoVacunaExclusivo;
        construir();
    }

    public boolean isRecetado() {
        return recetado;
    }

    private void construir() {
        setSize(520, 460);
        setResizable(false);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(241, 245, 249));

        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(new EmptyBorder(24, 32, 24, 32));
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(modoVacunaExclusivo ? "Aplicar vacuna" : "Recetar medicamento");
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
        radioMedicamento = new JRadioButton("Medicamento", !modoVacunaExclusivo);
        radioVacuna = new JRadioButton("Vacuna", modoVacunaExclusivo);
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
        // Modo exclusivo: ocultar la opción que no corresponde
        radioMedicamento.setVisible(!modoVacunaExclusivo);
        radioVacuna.setVisible(modoVacunaExclusivo);

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
        lblCombo.setFont(CargadorFuentes.cargar(14f).deriveFont(Font.BOLD));
        lblCombo.setForeground(new Color(30, 41, 59));
        lblCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblCombo);
        panelCentral.add(Box.createVerticalStrut(8));
        comboMedicamento.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(comboMedicamento);

        panelCentral.add(Box.createVerticalStrut(16));

        campoFechaAplicacion = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        campoFechaAplicacion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        JLabel lblAplic = new JLabel("Fecha de aplicación (dd/MM/yyyy)");
        lblAplic.setFont(CargadorFuentes.cargar(14f).deriveFont(Font.BOLD));
        lblAplic.setForeground(new Color(30, 41, 59));
        lblAplic.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblAplic);
        panelCentral.add(Box.createVerticalStrut(8));
        campoFechaAplicacion.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));
        campoFechaAplicacion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(campoFechaAplicacion);
        panelCentral.add(Box.createVerticalStrut(12));

        campoVigenciaDias = new JTextField("365");
        campoVigenciaDias.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        JLabel lblVenc = new JLabel("Vigencia (en días)");
        lblVenc.setFont(CargadorFuentes.cargar(14f).deriveFont(Font.BOLD));
        lblVenc.setForeground(new Color(30, 41, 59));
        lblVenc.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblVenc);
        panelCentral.add(Box.createVerticalStrut(8));
        campoVigenciaDias.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));
        campoVigenciaDias.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(campoVigenciaDias);

        panelCentral.add(Box.createVerticalStrut(8));

        lblError = new JLabel(" ");
        lblError.setFont(CargadorFuentes.cargar(11f));
        lblError.setForeground(new Color(220, 38, 38));
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblError);

        panelCentral.add(Box.createVerticalStrut(16));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotones.setOpaque(false);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.PLAIN));
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(new Color(30, 41, 59));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);

        JButton btnGuardar = new JButton("Confirmar");
        btnGuardar.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        btnGuardar.setBackground(new Color(13, 148, 136));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setOpaque(true);
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> intentarGuardar());
        panelBotones.add(btnGuardar);

        panelCentral.add(panelBotones);

        add(panelCentral, BorderLayout.CENTER);

        actualizarVisibilidad();
    }

    private void actualizarVisibilidad() {
        boolean esVacuna = radioVacuna.isSelected();
        campoFechaAplicacion.setEnabled(esVacuna);
        campoVigenciaDias.setEnabled(esVacuna);
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
            LocalDate aplic;
            int vigencia;
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                aplic = LocalDate.parse(campoFechaAplicacion.getText().trim(), fmt);
                vigencia = Integer.parseInt(campoVigenciaDias.getText().trim());
            } catch (DateTimeParseException ex) {
                lblError.setText("Fecha de aplicación inválida. Usá dd/MM/yyyy.");
                return;
            } catch (NumberFormatException ex) {
                lblError.setText("Vigencia inválida. Ingrese un número entero.");
                return;
            }
            Vacuna v = new Vacuna(med.getCodigoSenasa(), med.getNombreMedicamento(), vigencia);
            controlador.registrarVacunacion(animal, v, aplic);
        }

        recetado = true;
        JOptionPane.showMessageDialog(this, "Operación registrada con éxito.");
        dispose();
    }
}
