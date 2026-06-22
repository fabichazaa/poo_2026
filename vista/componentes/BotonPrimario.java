package vista.componentes;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import recursos.CargadorFuentes;

public class BotonPrimario extends JButton {
    private boolean hovered = false;
    private final int radioBorde = 12;

    public BotonPrimario(String texto, int ancho, int alto) {
        super(texto);
        initEstilos(ancho, alto);
    }

    public BotonPrimario(String texto) {
        this(texto, 180, 36);
    }

    private void initEstilos(int ancho, int alto) {
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        
        setPreferredSize(new Dimension(ancho, alto));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setForeground(Color.WHITE);
        
        try {
            setFont(CargadorFuentes.cargar(14f).deriveFont(Font.BOLD));
        } catch (Exception e) {
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Se respetan a rajatabla tus colores del estado normal vs deep (hover)
        g2.setColor(hovered ? recursos.Color.PRIMARY_DEEP : recursos.Color.PRIMARY); 
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radioBorde, radioBorde);
        
        g2.dispose();
        super.paintComponent(g);
    }
}