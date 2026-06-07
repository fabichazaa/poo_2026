package vista.dialogos;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import modelo.ComprobanteTurno;
import recursos.CargadorFuentes;

public class DialogoComprobante extends JDialog {

    public DialogoComprobante(Frame owner, ComprobanteTurno comprobante) {
        super(owner, "Comprobante de Atención", true);
        construir(comprobante);
    }

    private void construir(ComprobanteTurno comprobante) {
        setSize(640, 620);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(241, 245, 249));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(13, 148, 136));
        panelHeader.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel lblTitulo = new JLabel("Comprobante de Atención N° " + comprobante.getTurno().getIdTurno());
        lblTitulo.setFont(CargadorFuentes.cargar(16f));
        lblTitulo.setForeground(Color.WHITE);
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        JLabel lblFecha = new JLabel("Emitido: " + comprobante.getFechaEmision().toLocalDate());
        lblFecha.setFont(CargadorFuentes.cargar(11f));
        lblFecha.setForeground(new Color(204, 251, 241));
        panelHeader.add(lblFecha, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);

        JTextArea areaTexto = new JTextArea(comprobante.generarTextoCompleto());
        areaTexto.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaTexto.setEditable(false);
        areaTexto.setBackground(Color.WHITE);
        areaTexto.setBorder(new EmptyBorder(16, 20, 16, 20));
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(16, 24, 16, 24),
            new LineBorder(new Color(226, 232, 240), 1, true)
        ));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(0, 24, 16, 24));

        JButton btnCopiar = new JButton("Copiar");
        btnCopiar.setFont(CargadorFuentes.cargar(12f));
        btnCopiar.addActionListener(e -> {
            java.awt.datatransfer.StringSelection sel =
                new java.awt.datatransfer.StringSelection(comprobante.generarTextoCompleto());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
            JOptionPane.showMessageDialog(this, "Comprobante copiado al portapapeles.");
        });

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(CargadorFuentes.cargar(12f));
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnCopiar);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);
    }
}
