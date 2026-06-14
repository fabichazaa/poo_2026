package vista.componentes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class CardPanel extends JPanel {

    private final Color topColor;
    private final Color topColorEnd;
    private final int radius;
    private final int topBarHeight;

    public CardPanel(Color topColor) {
        this(topColor, null, 16, 8, 16);
    }

    public CardPanel(Color topColor, int topBarHeight, int padding) {
        this(topColor, null, 16, topBarHeight, padding);
    }

    public CardPanel(Color topColor, Color topColorEnd) {
        this(topColor, topColorEnd, 16, 8, 16);
    }

    public CardPanel(Color topColor, Color topColorEnd, int radius, int topBarHeight, int padding) {
        this.topColor = topColor;
        this.topColorEnd = topColorEnd;
        this.radius = radius;
        this.topBarHeight = topBarHeight;
        setOpaque(false);
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(padding, padding, padding, padding));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar cuerpo de la tarjeta blanco
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Pintar la barra de color superior
        g2.setClip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
        if (topColorEnd != null) {
            GradientPaint gp = new GradientPaint(0, 0, topColor, getWidth(), 0, topColorEnd);
            g2.setPaint(gp);
        } else {
            g2.setColor(topColor);
        }
        g2.fillRect(0, 0, getWidth(), topBarHeight);

        // Dibujar el borde sutil
        g2.setClip(null);
        g2.setColor(new Color(226, 232, 240));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        g2.dispose();
    }
}
