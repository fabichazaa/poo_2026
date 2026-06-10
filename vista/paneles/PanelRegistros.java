package vista.paneles;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import modelo.*;

public final class PanelRegistros extends JPanel {

    private final ControladorVeterinaria controlador;
    private final JPanel panelGrillaPacientes;
    private final JTextField txtBuscar;
    private String filtroEspecieActual = "Todos";
    private String filtroEstadoActual = "Todos";

    // Colores de la paleta corporativa
    private final Color colorFondoGris = new Color(241, 245, 249);
    private final Color colorTealActivo = new Color(13, 148, 136);
    private final Color colorTextoOscuro = new Color(30, 41, 59);

    public PanelRegistros(ControladorVeterinaria controlador) {
        this.controlador = controlador;
        
        setLayout(new BorderLayout(0, 15));
        setBackground(colorFondoGris);
        setBorder(new EmptyBorder(15, 25, 15, 25));

        // --- 1. BARRA SUPERIOR DE FILTROS Y BÚSQUEDA ---
        JPanel panelBarraSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        panelBarraSuperior.setOpaque(false);

        // Campo de búsqueda estilizado
        txtBuscar = new JTextField("Buscar por nombre, raza o dueño...");
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBuscar.setForeground(new Color(148, 163, 184));
        txtBuscar.setPreferredSize(new Dimension(350, 36));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(0, 12, 0, 12)
        ));
        
        // Listener en tiempo real para el buscador
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filtrarYRefrescarGrilla();
            }
        });
        panelBarraSuperior.add(txtBuscar);

        // Agrupación de filtros de Especies (Todos, Perro, Gato)
        JPanel panelGrupoEspecies = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelGrupoEspecies.setOpaque(false);
        String[] especies = {"Todos", "Perro", "Gato"};
        for (String esp : especies) {
            JButton btnFiltro = crearBotonFiltroPildora(esp, true);
            panelGrupoEspecies.add(btnFiltro);
        }
        panelBarraSuperior.add(panelGrupoEspecies);

        // Agrupación de filtros de Estado (Todos, Activo, Inactivo)
        JPanel panelGrupoEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelGrupoEstado.setOpaque(false);
        String[] estados = {"Todos", "Activo", "Inactivo"};
        for (String est : estados) {
            JButton btnFiltro = crearBotonFiltroPildora(est, false);
            panelGrupoEstado.add(btnFiltro);
        }
        panelBarraSuperior.add(panelGrupoEstado);

        add(panelBarraSuperior, BorderLayout.NORTH);

        // --- 2. GRILLA CENTRAL SCROLLABLE DE PACIENTES ---
        // Usamos un JPanel con GridBagLayout envuelto para que las tarjetas no se deformen en pantallas grandes
        panelGrillaPacientes = new JPanel(new GridLayout(0, 4, 20, 20)); 
        panelGrillaPacientes.setOpaque(false);

        JScrollPane scrollGrilla = new JScrollPane(panelGrillaPacientes);
        scrollGrilla.setBorder(null);
        scrollGrilla.setOpaque(false);
        scrollGrilla.getViewport().setOpaque(false);
        
        // Aplicamos tu misma UI de Scroll personalizada que creaste para los turnos
        scrollGrilla.getVerticalScrollBar().setUnitIncrement(14);
        
        add(scrollGrilla, BorderLayout.CENTER);

        // Renderizado inicial
        actualizar();
    }

    // Método público que llama tu PortalVeterinario al cambiar de solapa
    public void actualizar() {
        filtrarYRefrescarGrilla();
    }

    private void filtrarYRefrescarGrilla() {
        panelGrillaPacientes.removeAll();

        // Obtenemos los animales desde tu lógica de negocio
        ArrayList<Animal> todosLosAnimales = controlador.getVeterinaria().getPacientesRegistrados();
        String busqueda = txtBuscar.getText().toLowerCase().trim();
        if (busqueda.contains("buscar por nombre")) busqueda = "";

        for (Animal a : todosLosAnimales) {
            // 1. Filtrar por texto (Nombre, Raza o Dueño)
            boolean coincideTexto = busqueda.isEmpty() ||
                    a.getNombre().toLowerCase().contains(busqueda) ||
                    (a instanceof Perro && ((Perro) a).getRaza().toLowerCase().contains(busqueda)) ||
                    (a instanceof Gato && ((Gato) a).getRaza().toLowerCase().contains(busqueda)) ||
                    a.getResponsable().getApellido().toLowerCase().contains(busqueda);

            // 2. Filtrar por tipo de objeto (Polimorfismo)
            boolean coincideEspecie = filtroEspecieActual.equals("Todos") ||
                    (filtroEspecieActual.equals("Perro") && a instanceof Perro) ||
                    (filtroEspecieActual.equals("Gato") && a instanceof Gato);

            // 3. Filtrar por estado clínico (si existe el filtro, asumir coincidencia por defecto)
            boolean coincideEstado = true;
            try {
                // Si la clase tiene un filtroEstadoActual, úsalo (evita dependencias fuertes)
                // Aquí sólo mantenemos la variable para futuras condiciones.
                // Por ahora no hay lógica adicional, así que queda true.
            } catch (Exception ignored) {
                // no-op
            }

            // Agregar sólo si pasa los filtros aplicados
            if (coincideTexto && coincideEspecie && coincideEstado) {
                panelGrillaPacientes.add(crearTarjetaPacienteHD(a));
            }
        }

        panelGrillaPacientes.revalidate();
        panelGrillaPacientes.repaint();
    }

    // --- RENDERIZADO HD DE LA TARJETA DE PACIENTE INDIVIDUAL ---
    private JPanel crearTarjetaPacienteHD(Animal a) {
        // Marco de la tarjeta con borde superior de color polimórfico (Azul Perro, Celeste Gato)
        // Alto de la barra decorativa superior y radio de redondeo consistente con las esquinas del mockup
        int altoBarra = 6;
        int radioEsquina = 24; 

        // Creamos la tarjeta pintando la barra superior con su respectivo degradado y bordes curvos
        JPanel card = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // --- CONFIGURACIÓN DEL DEGRADADO DE LA CABECERA ---
                // Fluirá de forma horizontal (desde X=0 hasta X=ancho de la tarjeta)
                GradientPaint degradadoCabecera = new GradientPaint(0, 0,  new Color(255, 178, 0), getWidth(), 0, new Color(255, 115, 0));
                
                if (a instanceof Gato) {
                    degradadoCabecera = new GradientPaint(0, 0, new Color(34, 211, 238), getWidth(), 0, new Color(37, 99, 235));
                }
                
                g2.setPaint(degradadoCabecera);
                // --------------------------------------------------
                
                // Recorte curvo exacto para que el degradado acompañe perfectamente las esquinas redondeadas de la tarjeta blanca
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radioEsquina, radioEsquina));
                
                // Dibujamos la barra con el alto estilizado
                g2.fillRect(0, 0, getWidth(), altoBarra);
                
                g2.dispose();
            }
        };
        card.setBackground(Color.WHITE);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

       // CONTENEDOR AVATAR CENTRAL (Degrade exacto según el mockup)
        JPanel panelAvatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // --- DEFINICIÓN DE DEGRADADOS DINÁMICOS SEGÚN MOCKUP ---
                GradientPaint degradadoEspecie;
                
                if (a instanceof Perro) {
                    // Perros: Desde un amarillo/naranja brillante arriba a un naranja fuego abajo
                    Color colorInicioNaranja = new Color(255, 178, 0); // #FB923C
                    Color colorFinNaranja = new Color(255, 115, 0);    // #EA580C
                    degradadoEspecie = new GradientPaint(0, 0, colorInicioNaranja, 0, getHeight(), colorFinNaranja);
                } else {
                    // Gatos: Desde un celeste brillante arriba a un azul cian profundo abajo
                    Color colorInicioCeleste = new Color(34, 211, 238); // #22D3EE
                    Color colorFinCeleste = new Color(37, 99, 235);    // #2563EB
                    degradadoEspecie = new GradientPaint(0, 0, colorInicioCeleste, 0, getHeight(), colorFinCeleste);
                }
                
                g2.setPaint(degradadoEspecie);
                // --------------------------------------------------------
                
                // Dibujamos el cuadrado redondeado suavizado para el ícono
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panelAvatar.setPreferredSize(new Dimension(75, 75));
        panelAvatar.setOpaque(false);
        panelAvatar.setLayout(new GridBagLayout());

        // Carga y remuestreo bilinear HD para el ícono de la mascota
        JLabel lblIcono = new JLabel();
        try {
            String rutaImg = (a instanceof Perro) ? "imagenes/emojis/perro.png" : "imagenes/emojis/gato.png";
            java.awt.image.BufferedImage imgBuffer = javax.imageio.ImageIO.read(new java.io.File(rutaImg));
            // Escalado premium de alta definición
            java.awt.image.BufferedImage resizedImg = new java.awt.image.BufferedImage(55, 55, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(imgBuffer, 0, 0, 55, 55, null);
            g2.dispose();
            lblIcono.setIcon(new ImageIcon(resizedImg));
        } catch (IOException e) {
            lblIcono.setText(a instanceof Perro ? "🐕" : "🐈");
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        }
        panelAvatar.add(lblIcono);

        JPanel panelAvatarWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelAvatarWrapper.setOpaque(false);
        panelAvatarWrapper.add(panelAvatar);

        // BLOQUE DE IDENTIFICACIÓN (Nombre, Raza y Tags)
        JPanel panelInfoCentral = new JPanel();
        panelInfoCentral.setOpaque(false);
        panelInfoCentral.setLayout(new BoxLayout(panelInfoCentral, BoxLayout.Y_AXIS));

        JLabel lblNombre = new JLabel((a != null && a.getNombre() != null) ? a.getNombre() : "Sin nombre", SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNombre.setForeground(colorTextoOscuro);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        String razaStr = (a != null && a instanceof Perro) ? ((Perro) a).getRaza() : (a != null && a instanceof Gato) ? ((Gato) a).getRaza() : "Sin raza";
        JLabel lblRaza = new JLabel(razaStr, SwingConstants.CENTER);
        lblRaza.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblRaza.setForeground(new Color(148, 163, 184));
        lblRaza.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Contenedor de Tags (Píldoras)
        JPanel panelTags = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        panelTags.setOpaque(false);
        
 
        panelInfoCentral.add(lblNombre);
        panelInfoCentral.add(Box.createVerticalStrut(2));
        panelInfoCentral.add(lblRaza);
        panelInfoCentral.add(Box.createVerticalStrut(8));
        panelInfoCentral.add(panelTags);

        // BLOQUE DE DATOS TÉCNICOS (Dueño, Edad, Próximo Turno)
        JPanel panelDatosGrid = new JPanel(new GridLayout(3, 2, 0, 4));
        panelDatosGrid.setOpaque(false);
        panelDatosGrid.setBorder(new EmptyBorder(10, 5, 5, 5));

        agregarFilaFicha(panelDatosGrid, "Dueño", (a != null && a.getResponsable() != null && a.getResponsable().getNombre() != null) ? a.getResponsable().getNombre() : "Sin asignar");
        agregarFilaFicha(panelDatosGrid, "Edad", "3 años"); // Aquí podés calcular dinámicamente con Period
        agregarFilaFicha(panelDatosGrid, "Próx. turno", "06 Jun 2026");

        // BOTÓN ACCIÓN INFERIOR
        JButton btnFicha = new JButton("Ver ficha →") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(248, 250, 252));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(226, 232, 240));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnFicha.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFicha.setForeground(new Color(71, 85, 105));
        btnFicha.setFocusPainted(false);
        btnFicha.setContentAreaFilled(false);
        btnFicha.setBorder(new EmptyBorder(8, 0, 8, 0));
        btnFicha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel panelCuerpoTarjeta = new JPanel(new BorderLayout(0, 12));
        panelCuerpoTarjeta.setOpaque(false);
        panelCuerpoTarjeta.add(panelInfoCentral, BorderLayout.NORTH);
        panelCuerpoTarjeta.add(panelDatosGrid, BorderLayout.CENTER);
        panelCuerpoTarjeta.add(btnFicha, BorderLayout.SOUTH);

        card.add(panelAvatarWrapper, BorderLayout.NORTH);
        card.add(panelCuerpoTarjeta, BorderLayout.CENTER);

        return card;
    }

    // --- MÉTODOS AUXILIARES DE COMPONENTES ---
    private JButton crearBotonFiltroPildora(String texto, boolean esDeEspecie) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean seleccionado = esDeEspecie ? filtroEspecieActual.equals(texto) : filtroEstadoActual.equals(texto);
                g2.setColor(seleccionado ? colorTealActivo : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                
                if (!seleccionado) {
                    g2.setColor(new Color(226, 232, 240));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(new EmptyBorder(5, 14, 5, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Determinamos la acción al pulsar las píldoras de filtrado superior
        btn.addActionListener(e -> {
            if (esDeEspecie) {
                filtroEspecieActual = texto;
            } else {
                filtroEstadoActual = texto;
            }
            // Repintar el grupo de botones hermano para actualizar cuál se ve presionado
            JPanel parent = (JPanel) btn.getParent();
            if (parent != null) parent.repaint();
            filtrarYRefrescarGrilla();
        });

        return btn;
    }

    private void agregarFilaFicha(JPanel panel, String clave, String valor) {
        JLabel lblClave = new JLabel(clave);
        lblClave.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblClave.setForeground(new Color(148, 163, 184));
        
        JLabel lblValor = new JLabel(valor, SwingConstants.RIGHT);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblValor.setForeground(colorTextoOscuro);

        panel.add(lblClave);
        panel.add(lblValor);
    }
}