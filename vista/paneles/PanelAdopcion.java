package vista.paneles;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import modelo.Animal;
import recursos.CargadorFuentes;

public class PanelAdopcion extends JPanel {

    private final ControladorVeterinaria controlador;
    private JPanel panelLista;

    private static final Color COLOR_TOPE = new Color(236, 72, 153);

    public PanelAdopcion(ControladorVeterinaria controlador) {
        this.controlador = controlador;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);
        setBorder(new EmptyBorder(10, 25, 15, 25));

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

        JLabel lblTitulo = new JLabel("Portal de Adopciones");
        lblTitulo.setFont(CargadorFuentes.cargar(16f));
        lblTitulo.setForeground(new Color(30, 41, 59));
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        JLabel lblSubtitulo = new JLabel("Mascotas disponibles para dar en adopción");
        lblSubtitulo.setFont(CargadorFuentes.cargar(11f));
        lblSubtitulo.setForeground(new Color(100, 116, 139));
        panelHeader.add(lblSubtitulo, BorderLayout.SOUTH);

        add(panelHeader, BorderLayout.NORTH);

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

        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setOpaque(false);
        panelFooter.setBorder(new EmptyBorder(4, 0, 0, 0));

        JButton btnGestionar = new JButton("Gestionar mascotas en adopción");
        btnGestionar.setFont(CargadorFuentes.cargar(12f));
        btnGestionar.setBackground(COLOR_TOPE);
        btnGestionar.setFocusPainted(false);
        btnGestionar.setOpaque(true);
        btnGestionar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGestionar.addActionListener(e -> abrirDialogoGestion());
        panelFooter.add(btnGestionar, BorderLayout.EAST);

        add(panelFooter, BorderLayout.SOUTH);
    }

    public void actualizar() {
        panelLista.removeAll();
        List<Animal> enAdopcion = controlador.obtenerAnimalesEnAdopcion();

        if (enAdopcion.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay mascotas en adopción actualmente.");
            lblVacio.setFont(CargadorFuentes.cargar(13f));
            lblVacio.setForeground(new Color(148, 163, 184));
            lblVacio.setHorizontalAlignment(SwingConstants.CENTER);
            lblVacio.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblVacio.setBorder(new EmptyBorder(40, 0, 40, 0));
            panelLista.add(lblVacio);
        } else {
            for (Animal a : enAdopcion) {
                panelLista.add(crearTarjetaAdopcion(a));
                panelLista.add(Box.createVerticalStrut(10));
            }
        }
        panelLista.revalidate();
        panelLista.repaint();
    }

    private JPanel crearTarjetaAdopcion(Animal a) {
        JPanel tarjeta = new JPanel(new BorderLayout(15, 0));
        tarjeta.setBackground(new Color(253, 242, 248));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(251, 232, 243), 1, true),
            new EmptyBorder(12, 16, 12, 16)
        ));

        JPanel panelIcono = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_TOPE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }
        };
        panelIcono.setOpaque(false);
        panelIcono.setPreferredSize(new Dimension(54, 54));
        panelIcono.setLayout(new GridBagLayout());
        try {
            String ruta = a.getRutaFoto();
            ImageIcon icon = new ImageIcon(ruta);
            Image img = icon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
            panelIcono.add(new JLabel(new ImageIcon(img)));
        } catch (Exception e) {
            JLabel lblEmoji = new JLabel(a instanceof modelo.Perro ? "\uD83D\uDC36" : "\uD83D\uDC31");
            lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            lblEmoji.setForeground(Color.WHITE);
            panelIcono.add(lblEmoji);
        }

        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        JLabel lblNombre = new JLabel(a.getNombre() + "  •  " + a.getEspecie());
        lblNombre.setFont(CargadorFuentes.cargar(14f));
        lblNombre.setForeground(new Color(30, 41, 59));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblDetalle = new JLabel("Edad: " + a.calcularEdad() + " años  •  Alimentación: " + a.getTipoAlimentacion().getDescripcion());
        lblDetalle.setFont(CargadorFuentes.cargar(11f));
        lblDetalle.setForeground(new Color(100, 116, 139));
        lblDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(2));
        panelInfo.add(lblDetalle);

        JButton btnQuitar = new JButton("Quitar");
        btnQuitar.setFont(CargadorFuentes.cargar(11f));
        btnQuitar.setBackground(Color.WHITE);
        btnQuitar.setForeground(COLOR_TOPE);
        btnQuitar.setFocusPainted(false);
        btnQuitar.setBorder(new LineBorder(COLOR_TOPE, 1, true));
        btnQuitar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnQuitar.addActionListener(e -> {
            controlador.marcarEnAdopcion(a, false);
            actualizar();
        });

        tarjeta.add(panelIcono, BorderLayout.WEST);
        tarjeta.add(panelInfo, BorderLayout.CENTER);
        tarjeta.add(btnQuitar, BorderLayout.EAST);
        return tarjeta;
    }

    private void abrirDialogoGestion() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        Frame frame = owner instanceof Frame ? (Frame) owner : null;

        List<Animal> todos = controlador.obtenerTodosLosAnimales();
        Animal[] arr = todos.toArray(Animal[]::new);
        Animal seleccionado = (Animal) JOptionPane.showInputDialog(
            frame,
            "Seleccioná la mascota que querés poner en adopción:",
            "Gestionar adopciones",
            JOptionPane.PLAIN_MESSAGE,
            null,
            arr,
            arr.length > 0 ? arr[0] : null
        );

        if (seleccionado != null) {
            controlador.marcarEnAdopcion(seleccionado, true);
            actualizar();
            JOptionPane.showMessageDialog(frame,
                seleccionado.getNombre() + " ahora está en adopción.");
        }
    }
}
