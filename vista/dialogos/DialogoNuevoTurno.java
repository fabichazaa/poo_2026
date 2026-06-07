package vista.dialogos;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import modelo.*;
import recursos.CargadorFuentes;

public class DialogoNuevoTurno extends JDialog {

    private final ControladorVeterinaria controlador;
    private Turno turnoCreado;

    private JComboBox<Veterinario> comboVeterinarios;
    private JComboBox<Animal> comboAnimales;
    private JComboBox<TipoTurno> comboTipo;
    private JTextField campoFecha;
    private JTextField campoHora;
    private JLabel lblError;

    public DialogoNuevoTurno(Frame owner, ControladorVeterinaria controlador) {
        super(owner, "Registrar nuevo turno", true);
        this.controlador = controlador;
        this.turnoCreado = null;
        construir();
    }

    public Turno getTurnoCreado() {
        return turnoCreado;
    }

    private void construir() {
        setSize(480, 460);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(241, 245, 249));

        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(new EmptyBorder(24, 32, 24, 32));
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Nuevo turno");
        lblTitulo.setFont(CargadorFuentes.cargar(18f));
        lblTitulo.setForeground(new Color(30, 41, 59));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblTitulo);

        panelCentral.add(Box.createVerticalStrut(4));

        JLabel lblSub = new JLabel("Programá una atención para un paciente");
        lblSub.setFont(CargadorFuentes.cargar(11f));
        lblSub.setForeground(new Color(100, 116, 139));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblSub);

        panelCentral.add(Box.createVerticalStrut(20));

        comboVeterinarios = new JComboBox<>(
            controlador.getVeterinaria().getListaVeterinarios().toArray(new Veterinario[0])
        );
        comboVeterinarios.setRenderer((list, value, index, isSelected, cellHasFocus) ->
            new JLabel(value == null ? "" :
                "Dr/a. " + value.getNombre() + " " + value.getApellido()
                + " — " + value.getEspecialidad()
                + "  (" + value.getMatricula() + ")")
        );
        comboVeterinarios.setSelectedItem(controlador.getVeterinarioLogueado());
        comboVeterinarios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        agregarCampo(panelCentral, "Veterinario", comboVeterinarios);

        ArrayList<Animal> todos = new ArrayList<>();
        for (Responsable c : controlador.getVeterinaria().getListaClientes()) {
            todos.addAll(c.getMascotas());
        }
        comboAnimales = new JComboBox<>(todos.toArray(new Animal[0]));
        comboAnimales.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel lbl = new JLabel(value == null ? "" :
                value.getNombre() + " (" + value.getEspecie() + ")"
                + " — " + value.getResponsable().getNombre() + " " + value.getResponsable().getApellido());
            if (isSelected) {
                lbl.setOpaque(true);
                lbl.setBackground(new Color(13, 148, 136));
                lbl.setForeground(Color.WHITE);
            }
            return lbl;
        });
        comboAnimales.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        agregarCampo(panelCentral, "Paciente (Animal)", comboAnimales);

        comboTipo = new JComboBox<>(TipoTurno.values());
        comboTipo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel lbl = new JLabel(value == null ? "" : value.getDescripcion());
            if (isSelected) {
                lbl.setOpaque(true);
                lbl.setBackground(new Color(13, 148, 136));
                lbl.setForeground(Color.WHITE);
            }
            return lbl;
        });
        comboTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        agregarCampo(panelCentral, "Tipo de turno", comboTipo);

        campoFecha = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        campoFecha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        agregarCampo(panelCentral, "Fecha (dd/MM/yyyy)", campoFecha);

        campoHora = new JTextField("10:00");
        campoHora.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        agregarCampo(panelCentral, "Hora (HH:mm)", campoHora);

        panelCentral.add(Box.createVerticalStrut(8));

        lblError = new JLabel(" ");
        lblError.setFont(CargadorFuentes.cargar(11f));
        lblError.setForeground(new Color(220, 38, 38));
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCentral.add(lblError);

        panelCentral.add(Box.createVerticalStrut(20));

        JButton btnGuardar = new JButton("Registrar turno");
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
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(CargadorFuentes.cargar(11f));
        lbl.setForeground(new Color(71, 85, 105));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(4));
        if (campo instanceof JTextField) {
            ((JTextField) campo).setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(6, 10, 6, 10)
            ));
        }
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(campo);
        panel.add(Box.createVerticalStrut(12));
    }

    private void intentarGuardar() {
        String fechaTxt = campoFecha.getText().trim();
        String horaTxt = campoHora.getText().trim();

        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(fechaTxt, fmt);
        } catch (DateTimeParseException ex) {
            lblError.setText("Formato de fecha inválido. Usá dd/MM/yyyy.");
            return;
        }

        if (!horaTxt.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
            lblError.setText("Formato de hora inválido. Usá HH:mm (24h).");
            return;
        }

        Veterinario vet = (Veterinario) comboVeterinarios.getSelectedItem();
        Animal animal = (Animal) comboAnimales.getSelectedItem();
        TipoTurno tipo = (TipoTurno) comboTipo.getSelectedItem();

        if (vet == null || animal == null) {
            lblError.setText("Seleccioná veterinario y paciente.");
            return;
        }

        turnoCreado = controlador.registrarTurno(fechaTxt, horaTxt, vet, animal, tipo);
        JOptionPane.showMessageDialog(this, "Turno registrado con éxito.");
        dispose();
    }
}
