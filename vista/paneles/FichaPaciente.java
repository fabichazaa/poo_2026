package vista.paneles;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelo.Animal;
import modelo.Responsable;

public class FichaPaciente extends JPanel {

    private final Animal animal;
    private final Runnable accionVolver;

    // Colores del Sistema de Diseño (Mockup accurate)
    private final Color colorFondoGris = new Color(241, 245, 249);
    private final Color colorTextoOscuro = new Color(30, 41, 59);
    private final Color colorTextoGrisBase = new Color(148, 163, 184);
    private final Color colorBordeTarjeta = new Color(226, 232, 240);

    public FichaPaciente(Animal animal, Runnable accionVolver) {
        this.animal = animal;
        this.accionVolver = accionVolver;

        setLayout(new BorderLayout(0, 15));
        setBackground(colorFondoGris);
        setBorder(new EmptyBorder(15, 25, 15, 25));

        initHeader();
        initCuerpo();
    }

    private void initHeader() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JButton btnVolver = new JButton("‹") {
            private boolean hover = false;
            {
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(java.awt.event.MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? new Color(226, 232, 240) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(colorBordeTarjeta);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 26));
        btnVolver.setForeground(colorTextoOscuro);
        btnVolver.setPreferredSize(new Dimension(42, 42));
        btnVolver.setBorder(new EmptyBorder(0, 0, 4, 0));
        btnVolver.addActionListener(e -> accionVolver.run());

        JPanel panelTextoHeader = new JPanel(new GridLayout(2, 1, 0, 2));
        panelTextoHeader.setOpaque(false);
        panelTextoHeader.setBorder(new EmptyBorder(0, 15, 0, 0));

        JLabel lblClinica = new JLabel("San Roque");
        lblClinica.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblClinica.setForeground(colorTextoOscuro);

        JLabel lblSub = new JLabel("Ficha del Paciente");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(colorTextoGrisBase);

        panelTextoHeader.add(lblClinica);
        panelTextoHeader.add(lblSub);

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.add(btnVolver);
        panelIzquierdo.add(panelTextoHeader);

        panelHeader.add(panelIzquierdo, BorderLayout.WEST);
        add(panelHeader, BorderLayout.NORTH);
    }

    private void initCuerpo() {
        JPanel panelCuerpo = new JPanel(new BorderLayout(20, 0));
        panelCuerpo.setOpaque(false);

        // ========================================================
        // COLUMNA IZQUIERDA: CONTENEDOR CON SCROLL (ANCHO CONGELADO)
        // ========================================================
        JPanel panelContenidoIzquierdo = new JPanel();
        panelContenidoIzquierdo.setOpaque(false);
        panelContenidoIzquierdo.setLayout(new BoxLayout(panelContenidoIzquierdo, BoxLayout.Y_AXIS));

        // 1. TARJETA HD DEL PACIENTE
        JPanel cardPaciente = new JPanel(new BorderLayout(0, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                
                Color colorInicio = animal.isActivo() ? Color.decode(animal.getColorInicioHex()) : new Color(148, 163, 184);
                Color colorFin = animal.isActivo() ? Color.decode(animal.getColorFinHex()) : new Color(100, 116, 139);
                GradientPaint deg = new GradientPaint(0, 0, colorInicio, 0, getHeight(), colorFin);
                g2.setPaint(deg);
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                g2.fillRect(0, 0, getWidth(), 8); 
                
                g2.setClip(null);
                g2.setColor(colorBordeTarjeta);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        cardPaciente.setOpaque(false);
        cardPaciente.setBorder(new EmptyBorder(25, 15, 20, 15));

        JPanel panelAvatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color colorInicio = animal.isActivo() ? Color.decode(animal.getColorInicioHex()) : new Color(148, 163, 184);
                Color colorFin = animal.isActivo() ? Color.decode(animal.getColorFinHex()) : new Color(100, 116, 139);
                g2.setPaint(new GradientPaint(0, 0, colorInicio, 0, getHeight(), colorFin));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panelAvatar.setPreferredSize(new Dimension(85, 85));
        panelAvatar.setMinimumSize(new Dimension(85, 85));
        panelAvatar.setMaximumSize(new Dimension(85, 85));
        panelAvatar.setOpaque(false);
        panelAvatar.setLayout(new GridBagLayout());

        JLabel lblIcono = new JLabel();
        try {
            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(new java.io.File(animal.getImagen()));
            Image scaled = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            lblIcono.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            lblIcono.setText("🐾");
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        }
        panelAvatar.add(lblIcono);

        JPanel panelAvatarWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelAvatarWrapper.setOpaque(false);
        panelAvatarWrapper.add(panelAvatar);

        JPanel panelInfoBasica = new JPanel();
        panelInfoBasica.setOpaque(false);
        panelInfoBasica.setLayout(new BoxLayout(panelInfoBasica, BoxLayout.Y_AXIS));

        JLabel lblNombre = new JLabel(animal.getNombre(), SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblNombre.setForeground(colorTextoOscuro);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRaza = new JLabel(animal.getSexo() ? "Macho" : "Hembra", SwingConstants.CENTER);
        lblRaza.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblRaza.setForeground(new Color(217, 119, 6)); 
        lblRaza.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelInfoBasica.add(lblNombre);
        panelInfoBasica.add(Box.createVerticalStrut(4));
        panelInfoBasica.add(lblRaza);

        JPanel panelDatosGrid = new JPanel(new GridLayout(4, 1, 0, 6));
        panelDatosGrid.setOpaque(false);
        panelDatosGrid.setBorder(new EmptyBorder(10, 5, 5, 5));

        agregarFilaDatosFicha(panelDatosGrid, "Edad", animal.calcularEdad() + " años");
        agregarFilaDatosFicha(panelDatosGrid, "Peso", animal.getPeso() + " kg");

        cardPaciente.add(panelAvatarWrapper, BorderLayout.NORTH);
        cardPaciente.add(panelInfoBasica, BorderLayout.CENTER);
        cardPaciente.add(panelDatosGrid, BorderLayout.SOUTH);

        // 2. TARJETA COMPLETA DEL RESPONSABLE (DISEÑO PREMIUM EN DEGRADÉ)
        JPanel cardDueno = new JPanel(new BorderLayout(0, 14)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo Blanco de la tarjeta
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                
                // 🔥 MODIFICADO: Ahora el borde superior tiene un degradé continuo y mide 8px de ancho (idéntico al paciente)
                Color azulInicioDegrade = new Color(14, 165, 233); // Sky 550 / Cian
                Color azulFinDegrade = new Color(37, 99, 235);    // Blue 600 / Azul eléctrico
                GradientPaint degradadoCabeceraDueno = new GradientPaint(0, 0, azulInicioDegrade, getWidth(), 0, azulFinDegrade);
                g2.setPaint(degradadoCabeceraDueno);
                
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                g2.fillRect(0, 0, getWidth(), 8); // Se engrosó a 8px para simetría
                
                g2.setClip(null);
                g2.setColor(colorBordeTarjeta);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        cardDueno.setOpaque(false);
        cardDueno.setBorder(new EmptyBorder(18, 15, 18, 15));

        JLabel lblTagDueno = new JLabel("RESPONSABLE");
        lblTagDueno.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTagDueno.setForeground(colorTextoGrisBase);
        cardDueno.add(lblTagDueno, BorderLayout.NORTH);

        Responsable responsable = animal.getResponsable();

        if (responsable != null) {
            JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
            panelUsuario.setOpaque(false);

            // 🔥 MODIFICADO: Dimensiones reducidas de 60x60 a 44x44 píxeles para que no compita jerárquicamente
            JPanel panelAvatarCuadrado = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    Color azulInicio = new Color(14, 165, 233); 
                    Color azulFin = new Color(37, 99, 235);    
                    g2.setPaint(new GradientPaint(0, 0, azulInicio, 0, getHeight(), azulFin));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12); // Redondeado acorde al nuevo tamaño
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            panelAvatarCuadrado.setPreferredSize(new Dimension(44, 44));
            panelAvatarCuadrado.setMinimumSize(new Dimension(44, 44));
            panelAvatarCuadrado.setMaximumSize(new Dimension(44, 44));
            panelAvatarCuadrado.setOpaque(false);
            panelAvatarCuadrado.setLayout(new GridBagLayout());

            // 🔥 MODIFICADO: Se redujo la escala del emoji de usuario a 24x24 píxeles
            JLabel lblImagenUsuario = new JLabel();
            ImageIcon iconoUser = cargarIconoHD("imagenes/emojis/usuario.png", 24, 24);
            if (iconoUser != null) {
                lblImagenUsuario.setIcon(iconoUser);
            } else {
                lblImagenUsuario.setText("👤");
                lblImagenUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                lblImagenUsuario.setForeground(Color.WHITE);
            }
            panelAvatarCuadrado.add(lblImagenUsuario);

            JLabel lblNombreDueno = new JLabel(responsable.getNombre() + " " + responsable.getApellido());
            lblNombreDueno.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblNombreDueno.setForeground(colorTextoOscuro);
            
            panelUsuario.add(panelAvatarCuadrado);
            panelUsuario.add(lblNombreDueno);

            JPanel panelContacto = new JPanel(new GridLayout(2, 1, 0, 8));
            panelContacto.setOpaque(false);
            panelContacto.setBorder(new EmptyBorder(10, 2, 0, 2));

            JPanel filaCelular = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            filaCelular.setOpaque(false);
            JLabel lblIconoCel = new JLabel(cargarIconoHD("imagenes/emojis/celular.png", 16, 16));
            JLabel lblTextoCel = new JLabel(responsable.getCelular());
            lblTextoCel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblTextoCel.setForeground(colorTextoOscuro);
            filaCelular.add(lblIconoCel);
            filaCelular.add(lblTextoCel);

            JPanel filaUbicacion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            filaUbicacion.setOpaque(false);
            JLabel lblIconoUbi = new JLabel(cargarIconoHD("imagenes/emojis/ubicacion.png", 16, 16));
            JLabel lblTextoUbi = new JLabel(responsable.getDireccionCompleta());
            lblTextoUbi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblTextoUbi.setForeground(colorTextoOscuro);
            filaUbicacion.add(lblIconoUbi);
            filaUbicacion.add(lblTextoUbi);

            panelContacto.add(filaCelular);
            panelContacto.add(filaUbicacion);

            cardDueno.add(panelUsuario, BorderLayout.CENTER);
            cardDueno.add(panelContacto, BorderLayout.SOUTH);
        } else {
            JPanel panelVacio = new JPanel(new GridBagLayout());
            panelVacio.setOpaque(false);
            
            JLabel lblMensajeVacio = new JLabel("<html><center>Este animalito no cuenta<br>con un responsable</center></html>");
            lblMensajeVacio.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lblMensajeVacio.setForeground(new Color(148, 163, 184)); 
            lblMensajeVacio.setHorizontalAlignment(SwingConstants.CENTER);
            
            panelVacio.add(lblMensajeVacio);
            cardDueno.add(panelVacio, BorderLayout.CENTER);
        }

        panelContenidoIzquierdo.add(cardPaciente);
        panelContenidoIzquierdo.add(Box.createVerticalStrut(15));
        panelContenidoIzquierdo.add(cardDueno);

        JScrollPane scrollIzquierdo = new JScrollPane(panelContenidoIzquierdo);
        scrollIzquierdo.setBorder(null);
        scrollIzquierdo.setOpaque(false);
        scrollIzquierdo.getViewport().setOpaque(false);
        scrollIzquierdo.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollIzquierdo.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        scrollIzquierdo.setPreferredSize(new Dimension(260, 0));

        // ========================================================
        // COLUMNA DERECHA: PANEL GRANDE CON EL DEGRADADO VIOLETA
        // ========================================================
        JPanel panelDerecho = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                
                Color violetaMockup = new Color(139, 92, 246); 
                Color azulMockup = new Color(59, 130, 246);    
                GradientPaint degradadoSuperior = new GradientPaint(0, 0, violetaMockup, getWidth(), 0, azulMockup);
                g2.setPaint(degradadoSuperior);
                
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                g2.fillRect(0, 0, getWidth(), 6); 
                
                g2.setClip(null);
                g2.setColor(colorBordeTarjeta);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        panelDerecho.setOpaque(false);
        panelDerecho.setBorder(new EmptyBorder(12, 10, 10, 10));
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JPanel panelHistorial = new JPanel();
        panelHistorial.setBackground(Color.WHITE);
        panelHistorial.setLayout(new BoxLayout(panelHistorial, BoxLayout.Y_AXIS));
        panelHistorial.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        panelHistorial.add(crearFilaConsulta("Análisis de sangre", "Dr. Páez", "Valores normales. Seguimiento en 6 meses.", "28 May 2026"));
        panelHistorial.add(Box.createVerticalStrut(12));
        panelHistorial.add(crearFilaConsulta("Consulta General", "Dr. Páez", "Revisión anual. Peso: 28 kg. Sin novedades.", "10 Abr 2026"));
        panelHistorial.add(Box.createVerticalStrut(12));
        panelHistorial.add(crearFilaConsulta("Cirugía menor", "Dra. Ruiz", "Extracción de quiste. Recuperación exitosa.", "15 Feb 2026"));

        tabs.addTab("Historial", panelHistorial);
        tabs.addTab("Vacunas", new JPanel());
        tabs.addTab("Turnos", new JPanel());
        panelDerecho.add(tabs, BorderLayout.CENTER);

        panelCuerpo.add(scrollIzquierdo, BorderLayout.WEST); 
        panelCuerpo.add(panelDerecho, BorderLayout.CENTER);  

        add(panelCuerpo, BorderLayout.CENTER);
    }

    private ImageIcon cargarIconoHD(String ruta, int ancho, int alto) {
        try {
            java.awt.image.BufferedImage imgBuffer = javax.imageio.ImageIO.read(new java.io.File(ruta));
            java.awt.image.BufferedImage resizedImg = new java.awt.image.BufferedImage(ancho, alto, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(imgBuffer, 0, 0, ancho, alto, null);
            g2.dispose();
            return new ImageIcon(resizedImg);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el ícono: " + ruta);
            return null;
        }
    }

    private void agregarFilaDatosFicha(JPanel panel, String clave, String valor) {
        JPanel fila = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(248, 250, 252)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        fila.setOpaque(false);
        fila.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel lblClave = new JLabel(clave);
        lblClave.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblClave.setForeground(colorTextoGrisBase);
        
        JLabel lblValor = new JLabel(valor, SwingConstants.RIGHT);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        lblValor.setForeground(colorTextoOscuro); 

        fila.add(lblClave, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);
        panel.add(fila);
    }

    private JPanel crearFilaConsulta(String titulo, String medico, String notas, String fecha) {
        JPanel fila = new JPanel(new BorderLayout(10, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(248, 250, 252)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(colorBordeTarjeta);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        fila.setOpaque(false);
        fila.setBorder(new EmptyBorder(14, 18, 14, 18));

        JPanel textosIzquierda = new JPanel(new GridLayout(2, 1, 0, 2));
        textosIzquierda.setOpaque(false);
        
        JLabel lblT = new JLabel(titulo);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblT.setForeground(colorTextoOscuro);
        
        JLabel lblM = new JLabel(medico);
        lblM.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblM.setForeground(colorTextoGrisBase);
        
        textosIzquierda.add(lblT);
        textosIzquierda.add(lblM);

        JLabel lblFecha = new JLabel(fecha);
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFecha.setForeground(colorTextoGrisBase);

        JLabel lblNotas = new JLabel("💬 " + notas);
        lblNotas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNotas.setForeground(new Color(71, 85, 105));
        lblNotas.setBorder(new EmptyBorder(6, 0, 0, 0));

        fila.add(textosIzquierda, BorderLayout.WEST);
        fila.add(lblFecha, BorderLayout.EAST);
        fila.add(lblNotas, BorderLayout.SOUTH);

        return fila;
    }
}