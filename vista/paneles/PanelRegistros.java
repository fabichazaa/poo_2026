package vista.paneles;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import modelo.*;
import recursos.CargadorFuentes;
import vista.dialogos.DialogoRecetarMedicamento;

public class PanelRegistros extends JPanel {

    private final ControladorVeterinaria controlador;
    private JPanel panelListaAnimales;
    private JPanel panelDetalle;
    private JLabel lblDetalleTitulo;
    private JTextArea areaHistoria;
    private Animal animalSeleccionado;

    private static final Color COLOR_TOPE = new Color(99, 102, 241);

    public PanelRegistros(ControladorVeterinaria controlador) {
        this.controlador = controlador;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);
        setBorder(new EmptyBorder(10, 25, 15, 25));

        JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 12));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setPreferredSize(new Dimension(320, 0));

        JPanel panelHeaderIzq = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(COLOR_TOPE);
                g.fillRect(0, 0, getWidth(), 4);
            }
        };
        panelHeaderIzq.setBackground(Color.WHITE);
        panelHeaderIzq.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(14, 16, 14, 16)
        ));
        JLabel lblTituloIzq = new JLabel("Pacientes");
        lblTituloIzq.setFont(CargadorFuentes.cargar(15f));
        lblTituloIzq.setForeground(new Color(30, 41, 59));
        panelHeaderIzq.add(lblTituloIzq, BorderLayout.WEST);
        panelIzquierdo.add(panelHeaderIzq, BorderLayout.NORTH);

        panelListaAnimales = new JPanel();
        panelListaAnimales.setLayout(new BoxLayout(panelListaAnimales, BoxLayout.Y_AXIS));
        panelListaAnimales.setBackground(Color.WHITE);
        panelListaAnimales.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(8, 8, 8, 8)
        ));
        JScrollPane scrollIzq = new JScrollPane(panelListaAnimales);
        scrollIzq.setBorder(null);
        scrollIzq.getViewport().setBackground(Color.WHITE);
        panelIzquierdo.add(scrollIzq, BorderLayout.CENTER);

        add(panelIzquierdo, BorderLayout.WEST);

        JPanel panelDetalleContenedor = new JPanel(new BorderLayout(0, 12));
        panelDetalleContenedor.setOpaque(false);

        JPanel panelHeaderDer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(COLOR_TOPE);
                g.fillRect(0, 0, getWidth(), 4);
            }
        };
        panelHeaderDer.setBackground(Color.WHITE);
        panelHeaderDer.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(14, 18, 14, 18)
        ));
        lblDetalleTitulo = new JLabel("Seleccioná un paciente de la lista");
        lblDetalleTitulo.setFont(CargadorFuentes.cargar(15f));
        lblDetalleTitulo.setForeground(new Color(30, 41, 59));
        panelHeaderDer.add(lblDetalleTitulo, BorderLayout.WEST);

        JButton btnRecetar = new JButton("+ Recetar / Vacunar");
        btnRecetar.setFont(CargadorFuentes.cargar(12f));
        btnRecetar.setBackground(COLOR_TOPE);
        btnRecetar.setForeground(Color.WHITE);
        btnRecetar.setFocusPainted(false);
        btnRecetar.setOpaque(true);
        btnRecetar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRecetar.addActionListener(e -> abrirDialogoReceta());
        btnRecetar.setEnabled(false);
        panelHeaderDer.add(btnRecetar, BorderLayout.EAST);
        panelDetalleContenedor.add(panelHeaderDer, BorderLayout.NORTH);

        panelDetalle = new JPanel(new BorderLayout());
        panelDetalle.setBackground(Color.WHITE);
        panelDetalle.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(16, 20, 16, 20)
        ));

        areaHistoria = new JTextArea("Seleccioná un paciente de la lista para ver su historia clínica.");
        areaHistoria.setFont(CargadorFuentes.cargar(13f));
        areaHistoria.setForeground(new Color(71, 85, 105));
        areaHistoria.setEditable(false);
        areaHistoria.setLineWrap(true);
        areaHistoria.setWrapStyleWord(true);
        areaHistoria.setBackground(Color.WHITE);
        panelDetalle.add(areaHistoria, BorderLayout.CENTER);

        panelDetalleContenedor.add(panelDetalle, BorderLayout.CENTER);
        add(panelDetalleContenedor, BorderLayout.CENTER);
    }

    public void actualizar() {
        panelListaAnimales.removeAll();
        java.util.List<Animal> animales = controlador.obtenerTodosLosAnimales();
        if (animales.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay pacientes registrados.");
            lblVacio.setFont(CargadorFuentes.cargar(12f));
            lblVacio.setForeground(new Color(148, 163, 184));
            lblVacio.setBorder(new EmptyBorder(20, 12, 20, 12));
            panelListaAnimales.add(lblVacio);
        } else {
            for (Animal a : animales) {
                panelListaAnimales.add(crearItemAnimal(a));
                panelListaAnimales.add(Box.createVerticalStrut(6));
            }
        }
        panelListaAnimales.revalidate();
        panelListaAnimales.repaint();
    }

    private JPanel crearItemAnimal(Animal a) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setBackground(new Color(248, 250, 252));
        item.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(241, 245, 249), 1, true),
            new EmptyBorder(10, 12, 10, 12)
        ));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblNombre = new JLabel(a.getNombre() + "  •  " + a.getEspecie());
        lblNombre.setFont(CargadorFuentes.cargar(13f));
        lblNombre.setForeground(new Color(30, 41, 59));

        JLabel lblEdad = new JLabel("Edad: " + a.calcularEdad() + " años");
        lblEdad.setFont(CargadorFuentes.cargar(11f));
        lblEdad.setForeground(new Color(100, 116, 139));

        JPanel panelTextos = new JPanel();
        panelTextos.setOpaque(false);
        panelTextos.setLayout(new BoxLayout(panelTextos, BoxLayout.Y_AXIS));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblEdad.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTextos.add(lblNombre);
        panelTextos.add(lblEdad);

        item.add(panelTextos, BorderLayout.CENTER);

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                seleccionarAnimal(a);
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                item.setBackground(new Color(238, 242, 255));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                item.setBackground(new Color(248, 250, 252));
            }
        });

        return item;
    }

    private void seleccionarAnimal(Animal a) {
        this.animalSeleccionado = a;
        lblDetalleTitulo.setText(a.getNombre() + "  •  " + a.getEspecie());

        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(a.getIdAnimal()).append("\n");
        sb.append("Especie: ").append(a.getEspecie()).append("\n");
        sb.append("Edad: ").append(a.calcularEdad()).append(" años\n");
        sb.append("Alimentación: ").append(a.getTipoAlimentacion().getDescripcion()).append("\n");
        if (a.getResponsable() != null) {
            sb.append("Responsable: ").append(a.getResponsable().getNombre())
              .append(" ").append(a.getResponsable().getApellido())
              .append("  (DNI ").append(a.getResponsable().getDNI()).append(")\n");
        }
        sb.append("\n--- Historia clínica ---\n");
        ArrayList<Medicamento> meds = a.getHistorial().getMedicamentosRecetados();
        if (meds.isEmpty()) {
            sb.append("(sin medicamentos ni vacunas registradas)\n");
        } else {
            int i = 1;
            for (Medicamento m : meds) {
                sb.append(i++).append(". ").append(m.getNombreMedicamento())
                  .append("  [").append(m.getCodigoSenasa()).append("]");
                if (m instanceof Vacuna) {
                    Vacuna v = (Vacuna) m;
                    sb.append("  — Vacuna aplicada: ").append(v.getFechaAplicacion())
                      .append(", vence: ").append(v.getFechaVencimiento())
                      .append(v.estaVencida() ? " (VENCIDA)" : " (vigente)");
                }
                sb.append("\n");
            }
        }

        areaHistoria.setText(sb.toString());
        areaHistoria.setCaretPosition(0);

        Component[] comps = panelDetalle.getParent().getParent().getComponents();
        for (Component c : comps) {
            if (c instanceof JPanel) {
                JButton btn = findButtonRecetar((JPanel) c);
                if (btn != null) btn.setEnabled(true);
            }
        }
    }

    private JButton findButtonRecetar(JPanel parent) {
        for (Component c : parent.getComponents()) {
            if (c instanceof JButton && ((JButton) c).getText().contains("Recetar")) {
                return (JButton) c;
            }
            if (c instanceof Container) {
                JButton b = findButtonRecetar((JPanel) c);
                if (b != null) return b;
            }
        }
        return null;
    }

    private void abrirDialogoReceta() {
        if (animalSeleccionado == null) return;
        Window owner = SwingUtilities.getWindowAncestor(this);
        Frame frame = owner instanceof Frame ? (Frame) owner : null;
        DialogoRecetarMedicamento dialogo = new DialogoRecetarMedicamento(frame, controlador, animalSeleccionado);
        dialogo.setVisible(true);
        if (dialogo.isRecetado()) {
            seleccionarAnimal(animalSeleccionado);
        }
    }
}
