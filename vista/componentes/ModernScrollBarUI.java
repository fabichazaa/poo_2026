package vista.componentes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ModernScrollBarUI extends BasicScrollBarUI {

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // El track es transparente
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color colorFinal = isDragging ? new Color(100, 116, 139)
                : (isThumbRollover() ? new Color(148, 163, 184) : new Color(203, 213, 225));

        g2.setColor(colorFinal);
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
