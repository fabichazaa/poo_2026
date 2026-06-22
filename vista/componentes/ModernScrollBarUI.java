package vista.componentes;

import java.awt.*;
import javax.swing.*;

public class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
    private Color trackColor;

    public ModernScrollBarUI() {
        this.trackColor = recursos.Color.BG;
    }

    public ModernScrollBarUI(Color trackColor) {
        this.trackColor = trackColor;
    }

    @Override 
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(trackColor);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }
    
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Color colorFinal;
        if (isDragging) {
            colorFinal = new Color(100, 116, 139);
        } else if (isThumbRollover()) {
            colorFinal = recursos.Color.CAT_INACTIVO;
        } else {
            colorFinal = new Color(203, 213, 225);
        }
        
        g2.setColor(colorFinal);
        g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, 
                         thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
        g2.dispose();
    }

    @Override 
    protected JButton createDecreaseButton(int orientation) { return crearBotonInvisible(); }
    
    @Override 
    protected JButton createIncreaseButton(int orientation) { return crearBotonInvisible(); }

    private JButton crearBotonInvisible() {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(0, 0));
        btn.setMinimumSize(new Dimension(0, 0));
        btn.setMaximumSize(new Dimension(0, 0));
        return btn;
    } 
}