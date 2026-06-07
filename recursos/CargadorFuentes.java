package recursos;

import java.awt.Font;
import java.io.InputStream;

public final class CargadorFuentes {

    private static final String RUTA_FUENTE = "/recursos/GoogleSans.ttf";
    private static Font fuenteBaseRegistrada = null;

    private CargadorFuentes() {}

    public static Font cargar(float tamano) {
        Font base = obtenerFuenteBase();
        return base.deriveFont(tamano);
    }

    public static Font obtenerFuenteBase() {
        if (fuenteBaseRegistrada != null) {
            return fuenteBaseRegistrada;
        }
        try (InputStream is = CargadorFuentes.class.getResourceAsStream(RUTA_FUENTE)) {
            if (is == null) {
                fuenteBaseRegistrada = fallback();
            } else {
                Font f = Font.createFont(Font.TRUETYPE_FONT, is);
                java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);
                fuenteBaseRegistrada = f;
            }
        } catch (Exception e) {
            fuenteBaseRegistrada = fallback();
        }
        return fuenteBaseRegistrada;
    }

    private static Font fallback() {
        return new Font("Segoe UI", Font.PLAIN, 12);
    }
}
