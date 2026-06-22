package vista.dialogos;

import controlador.ControladorVeterinaria;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import modelo.Medicamento;
import modelo.Vacuna;
import recursos.CargadorFuentes;
import vista.componentes.BotonPrimario;

public class DialogoAgregarMedicamento extends JDialog {

    private final ControladorVeterinaria controlador;
    private boolean agregado = false;

    private JRadioButton radioMedicamento;
    private JRadioButton radioVacuna;

    private JTextField campoNombre;
    private JTextField campoCategoria;
    private JTextField campoPresentacion;
    private JTextField campoUnidad;
    private JTextField campoStockInicial;
    private JTextField campoVigencia;

    private JPanel panelVigencia;
    private JLabel lblErrorNombre;
    private JLabel lblErrorVigencia;
    private JLabel lblErrorGeneral;

    public DialogoAgregarMedicamento(Window owner, ControladorVeterinaria controlador) {
        super(owner, "Agregar Medicamento / Vacuna", ModalityType.APPLICATION_MODAL);
        this.controlador = controlador;
        construir();
    }

    public boolean isAgregado() {
        return agregado;
    }

    private void construir() {
        setSize(820, 470);
        setResizable(false);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(recursos.Color.BG);

        JPanel tarjeta = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.setColor(new Color(201, 220, 243));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
                g2.setColor(new Color(30, 148, 235));
                g2.fillRoundRect(0, 0, getWidth(), 7, 24, 24);
                g2.dispose();
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setBorder(new EmptyBorder(18, 18, 16, 18));
        tarjeta.add(crearHeader(), BorderLayout.NORTH);
        tarjeta.add(crearFormulario(), BorderLayout.CENTER);
        tarjeta.add(crearAcciones(), BorderLayout.SOUTH);

        add(tarjeta, BorderLayout.CENTER);
        actualizarVisibilidad();
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(10, 8, 6, 8));

        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        izq.setOpaque(false);

        JLabel lblIcon = new JLabel("💊");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        JLabel lblTitulo = new JLabel("Nuevo medicamento");
        lblTitulo.setFont(CargadorFuentes.cargar(24f).deriveFont(Font.BOLD));
        lblTitulo.setForeground(recursos.Color.INK);

        izq.add(lblIcon);
        izq.add(lblTitulo);
        header.add(izq, BorderLayout.WEST);

        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        der.setOpaque(false);

        radioMedicamento = new JRadioButton("Medicamento", true);
        radioVacuna = new JRadioButton("Vacuna");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(radioMedicamento);
        grupo.add(radioVacuna);

        estilizarRadio(radioMedicamento);
        estilizarRadio(radioVacuna);

        radioMedicamento.addActionListener(e -> actualizarVisibilidad());
        radioVacuna.addActionListener(e -> actualizarVisibilidad());

        der.add(radioMedicamento);
        der.add(radioVacuna);
        header.add(der, BorderLayout.EAST);

        return header;
    }

    private JPanel crearFormulario() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(8, 8, 2, 8));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        campoNombre = crearTextField();
        campoNombre.setToolTipText("Ej: Cefalexina 250mg");
        campoCategoria = crearTextField();
        campoCategoria.setText("Antibiótico");
        campoPresentacion = crearTextField();
        campoPresentacion.setText("Comprimidos");
        campoUnidad = crearTextField();
        campoUnidad.setText("comp");
        campoStockInicial = crearTextField();
        campoStockInicial.setText("0");
        campoVigencia = crearTextField();
        campoVigencia.setText("365");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 0, 0, 0);
        form.add(crearCampo("NOMBRE DEL MEDICAMENTO *", campoNombre), gbc);

        lblErrorNombre = crearLabelError();
        gbc.gridy = 1;
        gbc.insets = new Insets(2, 0, 0, 0);
        form.add(lblErrorNombre, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.insets = new Insets(6, 0, 0, 12);
        form.add(crearCampo("CATEGORÍA", campoCategoria), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(6, 0, 0, 0);
        form.add(crearCampo("PRESENTACIÓN", campoPresentacion), gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(8, 0, 0, 12);
        form.add(crearCampo("UNIDAD (EJ: COMP, ML)", campoUnidad), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(8, 0, 0, 0);
        form.add(crearCampo("STOCK INICIAL", campoStockInicial), gbc);

        panelVigencia = crearCampo("VIGENCIA (DÍAS)", campoVigencia);
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 0, 0, 0);
        form.add(panelVigencia, gbc);

        lblErrorVigencia = crearLabelError();
        gbc.gridy = 5;
        gbc.insets = new Insets(2, 0, 0, 0);
        form.add(lblErrorVigencia, gbc);

        lblErrorGeneral = crearLabelError();
        gbc.gridy = 6;
        form.add(lblErrorGeneral, gbc);

        return form;
    }

    private JPanel crearAcciones() {
        JPanel acciones = new JPanel(new BorderLayout(12, 0));
        acciones.setOpaque(false);
        acciones.setBorder(new EmptyBorder(12, 8, 0, 8));

        JButton btnCancelar = crearBotonSecundario("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(92, 38));
        btnCancelar.addActionListener(e -> dispose());
        acciones.add(btnCancelar, BorderLayout.WEST);

        JButton btnGuardar = crearBotonPrimario("✓ Guardar en catálogo");
        btnGuardar.setPreferredSize(new Dimension(0, 38));
        btnGuardar.addActionListener(e -> intentarGuardar());
        acciones.add(btnGuardar, BorderLayout.CENTER);

        return acciones;
    }

    private JPanel crearCampo(String label, JTextField campo) {
        JPanel bloque = new JPanel();
        bloque.setOpaque(false);
        bloque.setLayout(new BoxLayout(bloque, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(CargadorFuentes.cargar(10.5f).deriveFont(Font.BOLD));
        lbl.setForeground(new Color(148, 163, 184));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        bloque.add(lbl);
        bloque.add(Box.createVerticalStrut(6));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        bloque.add(campo);
        return bloque;
    }

    private JTextField crearTextField() {
        JTextField campo = new JTextField();
        campo.setFont(CargadorFuentes.cargar(14f));
        campo.setForeground(new Color(71, 85, 105));
        campo.setBackground(new Color(249, 251, 253));
        campo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(214, 223, 234), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        return campo;
    }

    private JButton crearBotonPrimario(String texto) {
        JButton btn = new BotonPrimario(texto);
        return btn;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 244, 249));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(214, 223, 234));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        btn.setForeground(new Color(71, 85, 105));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(10, 16, 10, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel crearLabelError() {
        JLabel lbl = new JLabel(" ");
        lbl.setFont(CargadorFuentes.cargar(10f));
        lbl.setForeground(recursos.Color.ERROR);
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        return lbl;
    }

    private void estilizarRadio(JRadioButton radio) {
        radio.setOpaque(false);
        radio.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        radio.setForeground(new Color(71, 85, 105));
        radio.setFocusPainted(false);
    }

    private void actualizarVisibilidad() {
        boolean esVacuna = radioVacuna.isSelected();
        panelVigencia.setVisible(esVacuna);
        lblErrorVigencia.setVisible(esVacuna);

        campoCategoria.setEnabled(!esVacuna);
        campoPresentacion.setEnabled(!esVacuna);
        campoUnidad.setEnabled(!esVacuna);
        campoStockInicial.setEnabled(!esVacuna);

        Color bgHabilitado = Color.WHITE;
        Color bgDeshabilitado = new Color(245, 247, 250);

        campoCategoria.setBackground(esVacuna ? bgDeshabilitado : bgHabilitado);
        campoPresentacion.setBackground(esVacuna ? bgDeshabilitado : bgHabilitado);
        campoUnidad.setBackground(esVacuna ? bgDeshabilitado : bgHabilitado);
        campoStockInicial.setBackground(esVacuna ? bgDeshabilitado : bgHabilitado);
        campoVigencia.setBackground(esVacuna ? bgHabilitado : bgDeshabilitado);

        revalidate();
        repaint();
    }

    private void intentarGuardar() {
        lblErrorNombre.setText(" ");
        lblErrorVigencia.setText(" ");
        lblErrorGeneral.setText(" ");

        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            lblErrorNombre.setText("El nombre del medicamento es obligatorio.");
            return;
        }

        boolean esVacuna = radioVacuna.isSelected();
        String codigo = generarCodigo(esVacuna);

        if (esVacuna) {
            int vigencia;
            try {
                vigencia = Integer.parseInt(campoVigencia.getText().trim());
                if (vigencia <= 0) {
                    lblErrorVigencia.setText("La vigencia debe ser un número positivo de días.");
                    return;
                }
            } catch (NumberFormatException ex) {
                lblErrorVigencia.setText("Ingrese una vigencia válida.");
                return;
            }

            Vacuna v = new Vacuna(codigo, nombre, vigencia);
            controlador.getVeterinaria().agregarMedicamentoAlCatalogo(v);
        } else {
            String categoria = campoCategoria.getText().trim();
            if (categoria.isEmpty()) {
                categoria = "General";
            }
            Medicamento m = new Medicamento(codigo, nombre, categoria);
            controlador.getVeterinaria().agregarMedicamentoAlCatalogo(m);
        }

        agregado = true;
        JOptionPane.showMessageDialog(this, "Medicamento agregado al catálogo con éxito.");
        dispose();
    }

    private String generarCodigo(boolean vacuna) {
        String prefijo = vacuna ? "VAC" : "SEN";
        int correlativo = controlador.getVeterinaria().getCatalogoMedicamentos().size() + 1;
        String candidato = prefijo + "-" + String.format("%03d", correlativo);
        while (existeCodigo(candidato)) {
            correlativo++;
            candidato = prefijo + "-" + String.format("%03d", correlativo);
        }
        return candidato;
    }

    private boolean existeCodigo(String codigo) {
        for (Medicamento m : controlador.getVeterinaria().getCatalogoMedicamentos()) {
            if (m.getCodigoSenasa().equalsIgnoreCase(codigo)) {
                return true;
            }
        }
        return false;
    }
}
