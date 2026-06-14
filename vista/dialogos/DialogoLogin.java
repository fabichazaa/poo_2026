package vista.dialogos;

import controlador.ControladorVeterinaria;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import recursos.CargadorFuentes;

public class DialogoLogin extends JDialog {

    private final ControladorVeterinaria controlador;
    private JTextField campoMatricula;
    private JLabel lblError;
    private JButton btnIngresar;
    private boolean exito = false;

    public DialogoLogin(Frame owner, ControladorVeterinaria controlador) {
        super(owner, "Iniciar sesión — Happy Paws", true);
        this.controlador = controlador;
        construir();
    }

    public boolean isExito() {
        return exito;
    }

    private void construir() {
        setSize(500, 420);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        try {
            // Buscamos la imagen en la carpeta de recursos/imágenes
            ImageIcon iconoApp = new ImageIcon("imagenes/logo.png");
            setIconImage(iconoApp.getImage());
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono de la aplicación: " + e.getMessage());
        }
        getContentPane().setBackground(recursos.Color.BG);

        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(new EmptyBorder(30, 40, 30, 40));
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Happy Paws");
        lblTitulo.setFont(CargadorFuentes.cargar(24f));
        lblTitulo.setForeground(new Color(157, 106, 170));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblTitulo);

        panelCentral.add(Box.createVerticalStrut(4));

        JLabel lblSubtitulo = new JLabel("Sistema de Gestión Veterinaria");
        lblSubtitulo.setFont(CargadorFuentes.cargar(12f));
        lblSubtitulo.setForeground(new Color(100, 116, 139));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblSubtitulo);

        panelCentral.add(Box.createVerticalStrut(30));

        JLabel lblPrompt = new JLabel("Ingresá tu matrícula:");
        lblPrompt.setFont(CargadorFuentes.cargar(13f));
        lblPrompt.setForeground(recursos.Color.INK);
        lblPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblPrompt);

        panelCentral.add(Box.createVerticalStrut(8));

        campoMatricula = new JTextField();
        campoMatricula.setFont(CargadorFuentes.cargar(14f));
        campoMatricula.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        campoMatricula.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(203, 213, 225), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        campoMatricula.setAlignmentX(Component.CENTER_ALIGNMENT);
        campoMatricula.addActionListener(e -> intentarLogin());
        panelCentral.add(campoMatricula);

        panelCentral.add(Box.createVerticalStrut(8));

        lblError = new JLabel(" ");
        lblError.setFont(CargadorFuentes.cargar(11f));
        lblError.setForeground(new Color(220, 38, 38));
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblError);

        panelCentral.add(Box.createVerticalStrut(20));

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(CargadorFuentes.cargar(13f));
        btnIngresar.setBackground(new Color(157, 106, 170));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setOpaque(true);
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIngresar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnIngresar.addActionListener(e -> intentarLogin());
        panelCentral.add(btnIngresar);

        panelCentral.add(Box.createVerticalStrut(16));

        JLabel lblHint = new JLabel("<html><div style='color:#94A3B8; font-size:10px;'>"
            + "Matrículas de prueba: <b>MP-9854</b> (Carlos Páez), <b>MP-1024</b> (Laura Gómez)</div></html>");
        lblHint.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblHint);

        add(panelCentral, BorderLayout.CENTER);
    }

    private void intentarLogin() {
        String matricula = campoMatricula.getText();
        if (controlador.loginPorMatricula(matricula)) {
            exito = true;
            dispose();
        } else {
            lblError.setText("Matrícula no encontrada. Verificá e intentá de nuevo.");
            campoMatricula.requestFocus();
            campoMatricula.selectAll();
        }
    }
}
