package vista.paneles;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import modelo.Medicamento;
import modelo.Vacuna;
import recursos.CargadorFuentes;
import vista.dialogos.DialogoAgregarMedicamento;

public class PanelMedicamentos extends JPanel {

    private final ControladorVeterinaria controlador;
    private JPanel panelLista;
    private static final Color COLOR_TOPE = new Color(59, 130, 246);

    public PanelMedicamentos(ControladorVeterinaria controlador) {
        this.controlador = controlador;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);
        setBorder(new EmptyBorder(10, 25, 15, 25));

        // Header
        JPanel panelHeader = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(COLOR_TOPE);
                g.fillRect(0, 0, getWidth(), 4);
            }
        };
        panelHeader.setBackground(Color.WHITE);
        panelHeader.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(16, 22, 16, 22)
        ));

        JLabel lblTitulo = new JLabel("Catálogo de Medicamentos");
        lblTitulo.setFont(CargadorFuentes.cargar(16f));
        lblTitulo.setForeground(new Color(30, 41, 59));
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        JLabel lblSub = new JLabel("Medicamentos y vacunas disponibles en la clínica");
        lblSub.setFont(CargadorFuentes.cargar(11f));
        lblSub.setForeground(new Color(100, 116, 139));
        panelHeader.add(lblSub, BorderLayout.SOUTH);

        add(panelHeader, BorderLayout.NORTH);

        // Lista de medicamentos
        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(Color.WHITE);
        panelLista.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(16, 20, 16, 20)
        ));

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        // Footer con botón agregar
        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setOpaque(false);
        panelFooter.setBorder(new EmptyBorder(4, 0, 0, 0));

        JButton btnAgregar = new JButton("+ Agregar Medicamento");
        btnAgregar.setFont(CargadorFuentes.cargar(12f));
        btnAgregar.setBackground(COLOR_TOPE);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setOpaque(true);
        btnAgregar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(e -> abrirDialogoAgregar());
        panelFooter.add(btnAgregar, BorderLayout.EAST);

        add(panelFooter, BorderLayout.SOUTH);
    }

    public void actualizar() {
        panelLista.removeAll();
        ArrayList<Medicamento> catalogoMedicamentos = controlador.getVeterinaria().getCatalogoMedicamentos();

        if (catalogoMedicamentos.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay medicamentos en el catálogo.");
            lblVacio.setFont(CargadorFuentes.cargar(12f));
            lblVacio.setForeground(new Color(148, 163, 184));
            lblVacio.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblVacio.setBorder(new EmptyBorder(40, 0, 40, 0));
            panelLista.add(lblVacio);
        } else {
            for (Medicamento med : catalogoMedicamentos) {
                panelLista.add(crearItemMedicamento(med));
                panelLista.add(Box.createVerticalStrut(10));
            }
        }
        panelLista.revalidate();
        panelLista.repaint();
    }

    private JPanel crearItemMedicamento(Medicamento med) {
        JPanel item = new JPanel(new BorderLayout(15, 0));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(14, 18, 14, 18)
        ));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Icono y info general
        JPanel panelInfo = new JPanel(new BorderLayout(12, 4));
        panelInfo.setOpaque(false);

        // Tipo de medicamento
        JLabel lblTipo;
        Color colorFondo;
        if (med instanceof Vacuna) {
            Vacuna vac = (Vacuna) med;
            lblTipo = new JLabel("💉 VACUNA");
            colorFondo = new Color(220, 252, 231);
        } else {
            lblTipo = new JLabel("💊 MEDICAMENTO");
            colorFondo = new Color(240, 245, 250);
        }
        lblTipo.setFont(CargadorFuentes.cargar(10f));
        lblTipo.setForeground(new Color(71, 85, 105));

        // Contenedor con fondo
        JPanel panelTipo = new JPanel(new BorderLayout());
        panelTipo.setBackground(colorFondo);
        panelTipo.setBorder(new EmptyBorder(2, 6, 2, 6));
        panelTipo.add(lblTipo, BorderLayout.CENTER);

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setOpaque(false);

        panelInfo.add(panelTop, BorderLayout.NORTH);

        // Nombre del medicamento
        JLabel lblNombre = new JLabel(med.getNombreMedicamento());
        lblNombre.setFont(CargadorFuentes.cargar(13f));
        lblNombre.setForeground(new Color(30, 41, 59));
        panelInfo.add(lblNombre, BorderLayout.CENTER);

        // Detalles
        JPanel panelDetalles = new JPanel();
        panelDetalles.setLayout(new BoxLayout(panelDetalles, BoxLayout.Y_AXIS));
        panelDetalles.setOpaque(false);

        JLabel lblCodigo = new JLabel("Código SENASA: " + med.getCodigoSenasa());
        lblCodigo.setFont(CargadorFuentes.cargar(11f));
        lblCodigo.setForeground(new Color(100, 116, 139));
        panelDetalles.add(lblCodigo);

        if (med instanceof Vacuna) {
            Vacuna vac = (Vacuna) med;
            JLabel lblVigencia = new JLabel("Vigencia: " + vac.getVigenciaDias() + " días");
            lblVigencia.setFont(CargadorFuentes.cargar(10f));
            lblVigencia.setForeground(new Color(120, 113, 108));
            panelDetalles.add(lblVigencia);
        }

        panelInfo.add(panelDetalles, BorderLayout.SOUTH);
        item.add(panelInfo, BorderLayout.CENTER);

        // Botón eliminar
        JButton btnEliminar = new JButton("🗑️");
        btnEliminar.setFont(CargadorFuentes.cargar(11f));
        btnEliminar.setBackground(new Color(254, 226, 226));
        btnEliminar.setForeground(new Color(220, 38, 38));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setOpaque(true);
        btnEliminar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEliminar.setPreferredSize(new Dimension(50, 40));
        btnEliminar.addActionListener(e -> eliminarMedicamento(med));
        item.add(btnEliminar, BorderLayout.EAST);

        return item;
    }

    private void abrirDialogoAgregar() {
        DialogoAgregarMedicamento dialogo = new DialogoAgregarMedicamento(
                SwingUtilities.getWindowAncestor(this), controlador
        );
        dialogo.setVisible(true);
        actualizar();
    }

    private void eliminarMedicamento(Medicamento med) {
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea eliminar este medicamento?\n\n" + med.getNombreMedicamento(),
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (respuesta == JOptionPane.YES_OPTION) {
            controlador.getVeterinaria().getCatalogoMedicamentos().remove(med);
            actualizar();
        }
    }
}

