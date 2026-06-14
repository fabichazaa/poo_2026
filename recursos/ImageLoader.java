package recursos;

import java.awt.Image;
import java.net.URL;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.ImageIcon;

public final class ImageLoader {

    private static final Map<String, ImageIcon> cache = new ConcurrentHashMap<>();
    private static final Map<String, ImageIcon> scaledCache = new ConcurrentHashMap<>();

    private ImageLoader() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Carga una imagen de forma segura con caché. Intenta cargarla primero
     * desde el classpath (recurso) y, si no existe, desde el sistema de archivos local.
     *
     * @param path Ruta del recurso o archivo (ej. "imagenes/logo.png").
     * @return ImageIcon cargado, o una imagen vacía si no se encuentra.
     */
    public static ImageIcon load(String path) {
        if (path == null || path.isBlank()) {
            return new ImageIcon();
        }
        return cache.computeIfAbsent(path, p -> {
            try {
                // 1. Intentar cargar desde el ClassLoader (ideal para empaquetado JAR)
                URL resourceURL = ImageLoader.class.getClassLoader().getResource(p);
                if (resourceURL != null) {
                    return new ImageIcon(resourceURL);
                }

                // 2. Intentar cargar desde el ClassLoader contextual
                resourceURL = Thread.currentThread().getContextClassLoader().getResource(p);
                if (resourceURL != null) {
                    return new ImageIcon(resourceURL);
                }

                // 3. Fallback: Cargar desde el sistema de archivos local
                File file = new File(p);
                if (file.exists()) {
                    return new ImageIcon(file.getAbsolutePath());
                }
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen " + p + ": " + e.getMessage());
            }
            // Devolver un ImageIcon vacío si todo falla para evitar NullPointerExceptions
            return new ImageIcon();
        });
    }

    /**
     * Carga y escala una imagen con caché de manera segura.
     *
     * @param path   Ruta de la imagen.
     * @param width  Ancho de destino.
     * @param height Alto de destino.
     * @return ImageIcon escalado.
     */
    public static ImageIcon loadScaled(String path, int width, int height) {
        String key = path + "_" + width + "x" + height;
        return scaledCache.computeIfAbsent(key, k -> {
            ImageIcon original = load(path);
            if (original.getImage() == null || original.getIconWidth() <= 0) {
                return original;
            }
            try {
                Image scaled = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            } catch (Exception e) {
                System.err.println("Error al escalar la imagen " + path + ": " + e.getMessage());
                return original;
            }
        });
    }
}
