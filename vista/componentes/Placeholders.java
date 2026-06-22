package vista.componentes;

import java.awt.*;
import javax.swing.*;

public final class Placeholders {

    private Placeholders() {
    }

    public static class TextField extends JTextField {

        private final String placeholder;

        public TextField(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(recursos.Color.CAT_INACTIVO);
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
                Insets insets = getInsets();
                FontMetrics fm = g2.getFontMetrics();
                int y = (getHeight() - insets.top - insets.bottom - fm.getHeight()) / 2 + fm.getAscent() + insets.top;
                g2.drawString(placeholder, insets.left, y);
                g2.dispose();
            }
        }
    }

    public static class TextArea extends JTextArea {

        private final String placeholder;

        public TextArea(String placeholder, int rows, int columns) {
            super(rows, columns);
            this.placeholder = placeholder;
            setBorder(new javax.swing.border.EmptyBorder(8, 10, 8, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(recursos.Color.CAT_INACTIVO);
                g2.setFont(getFont().deriveFont(Font.ITALIC));
                Insets insets = getInsets();
                g2.drawString(placeholder, insets.left + 2, insets.top + g2.getFontMetrics().getAscent());
                g2.dispose();
            }
        }
    }
}
