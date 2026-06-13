package vista.paneles;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import modelo.Medicamento;
import modelo.Vacuna;
import recursos.CargadorFuentes;
import recursos.Color;
import vista.dialogos.DialogoAgregarMedicamento;

public class PanelMedicamentos extends JPanel {

    private final ControladorVeterinaria controlador;
    private JPanel panelLista;

    public PanelMedicamentos(ControladorVeterinaria controlador) {
        this.controlador = controlador;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);
        setBackground(Color.BG);
        setBorder(new EmptyBorder(10, 25, 15, 25));

        // Header
        JPanel panelHeader = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.PRIMARY);
                g.fillRect(0, 0, getWidth(), 4);
            }
        };
        panelHeader.setBackground(Color.SURFACE);
        panelHeader.setBorder(new EmptyBorder(16, 22, 16, 22));

        JLabel lblTitulo = new JLabel("Catálogo de Medicamentos");
        lblTitulo.setFont(CargadorFuentes.cargar(16f).deriveFont(Font.BOLD));
        lblTitulo.setForeground(Color.INK);
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        JLabel lblSub = new JLabel("Medicamentos y vacunas disponibles en la clínica");
        lblSub.setFont(CargadorFuentes.cargar(11f));
        lblSub.setForeground(Color.MUTED);
        panelHeader.add(lblSub, BorderLayout.SOUTH);

        add(panelHeader, BorderLayout.NORTH);

        // --- CUERPO PRINCIPAL (panelBody con contenedores como en PanelCitas) ---
        JPanel panelBody = new JPanel(new BorderLayout(0, 15));
        panelBody.setOpaque(false);

        // Contenedor de controles (placeholder para filtros/estadísticas si se necesita)
        JPanel panelControles = new JPanel();
        panelControles.setOpaque(false);
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
        panelBody.add(panelControles, BorderLayout.NORTH);

        // Lista de medicamentos (contenedor interior)
        panelLista = new JPanel();
        panelLista.setBackground(Color.BG);
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        // Custom scrollbar (misma UI que PanelCitas)
        JScrollBar bar = scroll.getVerticalScrollBar();
        bar.setUI(new ModernScrollBarUI());
        bar.setPreferredSize(new Dimension(8, 0));
        bar.setUnitIncrement(16);

        panelBody.add(scroll, BorderLayout.CENTER);
        add(panelBody, BorderLayout.CENTER);

        // Footer con botón agregar
        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setOpaque(false);
        panelFooter.setBorder(new EmptyBorder(4, 0, 0, 0));

        JButton btnAgregar = new JButton("+ Agregar Medicamento") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(Color.PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnAgregar.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        btnAgregar.setForeground(Color.SURFACE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setContentAreaFilled(false);
        btnAgregar.setBorderPainted(false);
        btnAgregar.setPreferredSize(new Dimension(220, 36));
        btnAgregar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAgregar.setMargin(new Insets(6, 12, 6, 12));
        btnAgregar.setHorizontalAlignment(SwingConstants.CENTER);
        btnAgregar.addActionListener(e -> abrirDialogoAgregar());

        // Wrap the add button to preserve preferred size (avoid BorderLayout stretching)
        JPanel panelAccionesFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelAccionesFooter.setOpaque(false);
        panelAccionesFooter.add(btnAgregar);
        panelFooter.add(panelAccionesFooter, BorderLayout.EAST);

        add(panelFooter, BorderLayout.SOUTH);
    }

    public void actualizar() {
        panelLista.removeAll();
        ArrayList<Medicamento> catalogoMedicamentos = controlador.getVeterinaria().getCatalogoMedicamentos();

        if (catalogoMedicamentos.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay medicamentos en el catálogo.");
            lblVacio.setFont(CargadorFuentes.cargar(12f));
            lblVacio.setForeground(Color.MUTED);
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
        JPanel item = new JPanel(new BorderLayout(15, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(Color.SURFACE);
                // Más redondeado: radio aumentado a 36 px
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
                g2.setColor(Color.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 36, 36);
                g2.dispose();
            }
        };
        item.setOpaque(false);
        item.setBorder(new EmptyBorder(10, 14, 10, 14));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        // Icono y info general
        JPanel panelInfo = new JPanel(new BorderLayout(12, 4));
        panelInfo.setOpaque(false);

        // Tipo de medicamento
        JLabel lblTipo;
        java.awt.Color colorFondo;
        if (med instanceof Vacuna) {
            Vacuna vac = (Vacuna) med;
            lblTipo = new JLabel("💉 VACUNA");
            colorFondo = new java.awt.Color(220, 252, 231);
        } else {
            lblTipo = new JLabel("💊 MEDICAMENTO");
            colorFondo = new java.awt.Color(240, 245, 250);
        }
        lblTipo.setFont(CargadorFuentes.cargar(10f).deriveFont(Font.BOLD));
        lblTipo.setForeground(Color.INK);

        // Contenedor con fondo
        JPanel panelTipo = new JPanel(new BorderLayout());
        panelTipo.setBackground(colorFondo);
        panelTipo.setBorder(new EmptyBorder(2, 6, 2, 6));
        panelTipo.add(lblTipo, BorderLayout.CENTER);

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setOpaque(false);
        panelTop.add(panelTipo, BorderLayout.WEST);

        panelInfo.add(panelTop, BorderLayout.NORTH);

        // Nombre del medicamento
        JLabel lblNombre = new JLabel(med.getNombreMedicamento());
        lblNombre.setFont(CargadorFuentes.cargar(13f).deriveFont(Font.BOLD));
        lblNombre.setForeground(Color.INK);
        panelInfo.add(lblNombre, BorderLayout.CENTER);

        // Detalles
        JPanel panelDetalles = new JPanel();
        panelDetalles.setLayout(new BoxLayout(panelDetalles, BoxLayout.Y_AXIS));
        panelDetalles.setOpaque(false);

        JLabel lblCodigo = new JLabel("Código SENASA: " + med.getCodigoSenasa());
        lblCodigo.setFont(CargadorFuentes.cargar(11f));
        lblCodigo.setForeground(Color.MUTED);
        panelDetalles.add(lblCodigo);

        if (med instanceof Vacuna) {
            Vacuna vac = (Vacuna) med;
            JLabel lblVigencia = new JLabel("Vigencia: " + vac.getVigenciaDias() + " días");
            lblVigencia.setFont(CargadorFuentes.cargar(10f));
            lblVigencia.setForeground(Color.MUTED);
            panelDetalles.add(lblVigencia);
        }

        panelInfo.add(panelDetalles, BorderLayout.SOUTH);
        item.add(panelInfo, BorderLayout.CENTER);

        // Botón eliminar
        JButton btnEliminar = new JButton("Eliminar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btnEliminar.setPreferredSize(new Dimension(80, 24));
        btnEliminar.setBorder(new EmptyBorder(2, 0, 0, 0));
        btnEliminar.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.PLAIN));
        btnEliminar.setBackground(Color.RED_LIGHT);
        btnEliminar.setForeground(Color.ERROR);
        btnEliminar.setContentAreaFilled(false);
        btnEliminar.setBorderPainted(false);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setToolTipText("Eliminar medicamento del catálogo");
        btnEliminar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnEliminar.setBackground(Color.RED_LIGHT.darker());
                btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnEliminar.setBackground(Color.RED_LIGHT);
            }
        });
        btnEliminar.addActionListener(e -> eliminarMedicamento(med));

        // Wrap the button to avoid BorderLayout stretching it vertically
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelAcciones.setOpaque(false);
        panelAcciones.add(btnEliminar);
        item.add(panelAcciones, BorderLayout.EAST);

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

    private static class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            java.awt.Color finalColor = isDragging ? Color.MUTED
                    : (isThumbRollover() ? Color.CAT_INACTIVO : Color.DIVIDER);
            g2.setColor(finalColor);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                    thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
            g2.dispose();
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return crearBotonInvisible();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return crearBotonInvisible();
        }

        private JButton crearBotonInvisible() {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            btn.setMinimumSize(new Dimension(0, 0));
            btn.setMaximumSize(new Dimension(0, 0));
            return btn;
        }
    }
}
