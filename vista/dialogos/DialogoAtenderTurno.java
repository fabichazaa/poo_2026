package vista.dialogos;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import modelo.*;
import recursos.CargadorFuentes;

public class DialogoAtenderTurno extends JDialog {

    private final ControladorVeterinaria controlador;
    private final Turno turno;
    private final Animal animal;
    private JPanel panelMedicamentos;
    private JLabel lblPaso;

    public DialogoAtenderTurno(Frame owner, ControladorVeterinaria controlador, Turno turno) {
        super(owner, "Atender turno — " + turno.getAnimal().getNombre(), true);
        this.controlador = controlador;
        this.turno = turno;
        this.animal = turno.getAnimal();
        setSize(560, 520);
        setLocationRelativeTo(owner);
        construir();
    }

    private void construir() {
        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(new Color(241, 245, 249));
        fondo.setBorder(new EmptyBorder(20, 24, 20, 24));

        // --- HEADER: datos del paciente ---
        JPanel headerPaciente = new JPanel(new BorderLayout(12, 0));
        headerPaciente.setBackground(Color.WHITE);
        headerPaciente.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel lblIcono = new JLabel();
        try {
            String ruta = animal instanceof Perro ? "imagenes/emojis/perro.png" : "imagenes/emojis/gato.png";
            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(new java.io.File(ruta));
            java.awt.image.BufferedImage mini = new java.awt.image.BufferedImage(48, 48, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = mini.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(img, 0, 0, 48, 48, null);
            g2.dispose();
            lblIcono.setIcon(new ImageIcon(mini));
        } catch (Exception e) {
            lblIcono.setText(animal instanceof Perro ? "🐕" : "🐈");
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        }

        JPanel infoPaciente = new JPanel();
        infoPaciente.setOpaque(false);
        infoPaciente.setLayout(new BoxLayout(infoPaciente, BoxLayout.Y_AXIS));
        JLabel lblNombre = new JLabel(animal.getNombre());
        lblNombre.setFont(CargadorFuentes.obtenerFuenteBase().deriveFont(Font.BOLD, 16f));
        lblNombre.setForeground(new Color(30, 41, 59));
        JLabel lblDetalle = new JLabel(animal.getEspecie() + " — " + turno.getTipo().getDescripcion() + " — " + turno.getFecha() + " " + turno.getHora());
        lblDetalle.setFont(CargadorFuentes.cargar(11f));
        lblDetalle.setForeground(new Color(100, 116, 139));
        infoPaciente.add(lblNombre);
        infoPaciente.add(lblDetalle);

        JLabel lblResponsable = new JLabel("Responsable: " + animal.getResponsable().getNombre() + " " + animal.getResponsable().getApellido());
        lblResponsable.setFont(CargadorFuentes.cargar(11f));
        lblResponsable.setForeground(new Color(71, 85, 105));
        infoPaciente.add(lblResponsable);

        headerPaciente.add(lblIcono, BorderLayout.WEST);
        headerPaciente.add(infoPaciente, BorderLayout.CENTER);

        fondo.add(headerPaciente, BorderLayout.NORTH);

        // --- CUERPO: pasos del flujo de atención ---
        JPanel cuerpoScroll = new JPanel(new BorderLayout());
        cuerpoScroll.setOpaque(false);

        JPanel pasos = new JPanel();
        pasos.setOpaque(false);
        pasos.setLayout(new BoxLayout(pasos, BoxLayout.Y_AXIS));
        pasos.setBorder(new EmptyBorder(16, 0, 0, 0));

        // PASO 1: Datos del turno + diagnóstico
        lblPaso = new JLabel();
        pasos.add(crearPaso(1, "Revisar paciente", turno.getAnimal().getNombre() + " (" + turno.getAnimal().getEspecie() + ") — " + turno.getTipo().getDescripcion(), false));

        // PASO 2: Recetar medicamento
        JPanel paso2 = crearPasoConBoton(2, "Recetar medicamento", "Agregar medicación desde el catálogo");
        JButton btnRecetar = new JButton("+ Recetar");
        btnRecetar.setFont(CargadorFuentes.cargar(12f));
        btnRecetar.setBackground(new Color(13, 148, 136));
        btnRecetar.setForeground(Color.WHITE);
        btnRecetar.setFocusPainted(false);
        btnRecetar.setOpaque(true);
        btnRecetar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRecetar.addActionListener(e -> abrirRecetar());
        paso2.add(btnRecetar);

        panelMedicamentos = new JPanel();
        panelMedicamentos.setOpaque(false);
        panelMedicamentos.setLayout(new BoxLayout(panelMedicamentos, BoxLayout.Y_AXIS));
        paso2.add(panelMedicamentos);

        pasos.add(paso2);

        // PASO 3: Vacunas
        JPanel paso3 = crearPasoConBoton(3, "Aplicar vacuna", "Registrar aplicación de vacuna en el historial");
        JButton btnVacuna = new JButton("+ Vacuna");
        btnVacuna.setFont(CargadorFuentes.cargar(12f));
        btnVacuna.setBackground(new Color(13, 148, 136));
        btnVacuna.setForeground(Color.WHITE);
        btnVacuna.setFocusPainted(false);
        btnVacuna.setOpaque(true);
        btnVacuna.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVacuna.addActionListener(e -> abrirVacuna());
        paso3.add(btnVacuna);
        pasos.add(paso3);

        // PASO 4: Finalizar + comprobante
        JPanel paso4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        paso4.setOpaque(false);
        paso4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        paso4.setBorder(new EmptyBorder(4, 0, 0, 0));

        JLabel num4 = new JLabel("4");
        num4.setFont(CargadorFuentes.obtenerFuenteBase().deriveFont(Font.BOLD, 14f));
        num4.setForeground(Color.WHITE);
        num4.setPreferredSize(new Dimension(28, 28));
        num4.setHorizontalAlignment(SwingConstants.CENTER);
        num4.setOpaque(true);
        num4.setBackground(new Color(13, 148, 136));

        JLabel lblPaso4 = new JLabel("Finalizar atención");
        lblPaso4.setFont(CargadorFuentes.obtenerFuenteBase().deriveFont(Font.BOLD, 13f));
        lblPaso4.setForeground(new Color(30, 41, 59));

        JButton btnFinalizar = new JButton("✓ Finalizar y generar comprobante");
        btnFinalizar.setFont(CargadorFuentes.obtenerFuenteBase().deriveFont(Font.BOLD, 12f));
        btnFinalizar.setBackground(new Color(22, 163, 74));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFocusPainted(false);
        btnFinalizar.setOpaque(true);
        btnFinalizar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFinalizar.addActionListener(e -> finalizarYAbrirComprobante());

        paso4.add(num4);
        paso4.add(lblPaso4);
        paso4.add(Box.createHorizontalStrut(8));
        paso4.add(btnFinalizar);

        pasos.add(paso4);

        cuerpoScroll.add(pasos, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(cuerpoScroll);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        fondo.add(scroll, BorderLayout.CENTER);
        add(fondo);
    }

    private JPanel crearPaso(int numero, String titulo, String descripcion, boolean completado) {
        JPanel paso = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        paso.setOpaque(false);
        paso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel num = new JLabel(String.valueOf(numero));
        num.setFont(CargadorFuentes.obtenerFuenteBase().deriveFont(Font.BOLD, 14f));
        num.setForeground(Color.WHITE);
        num.setPreferredSize(new Dimension(28, 28));
        num.setHorizontalAlignment(SwingConstants.CENTER);
        num.setOpaque(true);
        num.setBackground(completado ? new Color(22, 163, 74) : new Color(148, 163, 184));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(CargadorFuentes.obtenerFuenteBase().deriveFont(Font.BOLD, 13f));
        lblTit.setForeground(new Color(30, 41, 59));
        JLabel lblDesc = new JLabel(descripcion);
        lblDesc.setFont(CargadorFuentes.cargar(11f));
        lblDesc.setForeground(new Color(100, 116, 139));

        paso.add(num);
        paso.add(lblTit);
        paso.add(lblDesc);
        return paso;
    }

    private JPanel crearPasoConBoton(int numero, String titulo, String descripcion) {
        JPanel paso = new JPanel();
        paso.setOpaque(false);
        paso.setLayout(new BoxLayout(paso, BoxLayout.Y_AXIS));
        paso.setBorder(new EmptyBorder(8, 0, 8, 0));
        paso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel num = new JLabel(String.valueOf(numero));
        num.setFont(CargadorFuentes.obtenerFuenteBase().deriveFont(Font.BOLD, 14f));
        num.setForeground(Color.WHITE);
        num.setPreferredSize(new Dimension(28, 28));
        num.setHorizontalAlignment(SwingConstants.CENTER);
        num.setOpaque(true);
        num.setBackground(new Color(148, 163, 184));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(CargadorFuentes.obtenerFuenteBase().deriveFont(Font.BOLD, 13f));
        lblTit.setForeground(new Color(30, 41, 59));
        JLabel lblDesc = new JLabel(descripcion);
        lblDesc.setFont(CargadorFuentes.cargar(11f));
        lblDesc.setForeground(new Color(100, 116, 139));

        fila.add(num);
        fila.add(lblTit);
        fila.add(lblDesc);

        paso.add(fila);
        return paso;
    }

    private void abrirRecetar() {
        try {
            DialogoRecetarMedicamento d = new DialogoRecetarMedicamento(
                this, controlador, animal, false);
            d.setVisible(true);
            if (d.isRecetado()) {
                refrescarMedicamentos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir recetar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void abrirVacuna() {
        try {
            DialogoRecetarMedicamento d = new DialogoRecetarMedicamento(
                this, controlador, animal, true);
            d.setVisible(true);
            if (d.isRecetado()) {
                refrescarMedicamentos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir vacuna: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void refrescarMedicamentos() {
        panelMedicamentos.removeAll();
        ArrayList<Medicamento> recetados = animal.getHistorial().getMedicamentosRecetados();
        if (recetados.isEmpty()) {
            JLabel vacio = new JLabel("(Sin medicación registrada)");
            vacio.setFont(CargadorFuentes.cargar(11f));
            vacio.setForeground(new Color(148, 163, 184));
            vacio.setBorder(new EmptyBorder(4, 28, 4, 0));
            panelMedicamentos.add(vacio);
        } else {
            for (Medicamento m : recetados) {
                String icono = (m instanceof Vacuna) ? "💉" : "💊";
                String detalle = m.getNombreMedicamento() + " (" + m.getCodigoSenasa() + ")";
                if (m instanceof Vacuna) {
                    Vacuna v = (Vacuna) m;
                    detalle += " — Aplic: " + v.getFechaAplicacion().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            + "  Vence: " + v.getFechaVencimiento().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }
                JLabel lbl = new JLabel(icono + " " + detalle);
                lbl.setFont(CargadorFuentes.cargar(11f));
                lbl.setForeground(new Color(22, 163, 74));
                lbl.setBorder(new EmptyBorder(3, 28, 3, 0));
                panelMedicamentos.add(lbl);
            }
        }
        panelMedicamentos.revalidate();
        panelMedicamentos.repaint();
    }

    private void finalizarYAbrirComprobante() {
        if (!turno.estaCompletado()) {
            turno.completarTurno();
        }
        Veterinaria v = controlador.getVeterinaria();
        ComprobanteTurno comp = new ComprobanteTurno(turno, v.getNombreNegocio());
        DialogoComprobante dc = new DialogoComprobante((Frame) getOwner(), comp);
        dc.setVisible(true);
        dispose();
    }
}
