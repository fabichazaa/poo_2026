package vista.paneles;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelo.*;
import vista.dialogos.DialogoEditarPaciente; // 🌟 Importación unificada a la nueva carpeta

public final class PanelMascotas extends JPanel {

    private final ControladorVeterinaria controlador;
    private final JPanel panelGrillaPacientes;
    private final JTextField txtBuscar;
    private String filtroEspecieActual = "Todos";
    private String filtroEstadoActual = "Todos";
    
    private JPanel panelBarraSuperior;
    private JScrollPane scrollGrilla;
    
    private final String PLACEHOLDER_BUSQUEDA = "Buscar por nombre de la mascota...";

    public PanelMascotas(ControladorVeterinaria controlador) {
        this.controlador = controlador;
        
        setLayout(new BorderLayout(0, 15));
        setBackground(recursos.Color.BG);
        setBorder(new EmptyBorder(15, 25, 15, 25));

        // 🌟 CONFIGURACIÓN ESTRATÉGICA: Usamos BorderLayout para poder separar los filtros a la izquierda y el botón a la derecha
        panelBarraSuperior = new JPanel(new BorderLayout());
        panelBarraSuperior.setOpaque(false);

        // Contenedor interno izquierdo para agrupar el buscador y los filtros hilos
        JPanel panelFiltrosIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 5));
        panelFiltrosIzquierda.setOpaque(false);

        final ImageIcon iconoLupaModerno;
        ImageIcon temporal = null;
        try {
            String rutaLupa = "imagenes/emojis/lupa.png"; 
            java.awt.image.BufferedImage imgBuffer = javax.imageio.ImageIO.read(new java.io.File(rutaLupa));
            
            java.awt.image.BufferedImage resizedImg = new java.awt.image.BufferedImage(18, 18, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(imgBuffer, 0, 0, 18, 18, null);
            g2.dispose();
            temporal = new ImageIcon(resizedImg);
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen de la lupa, usando respaldo de texto.");
        }
        iconoLupaModerno = temporal;

        txtBuscar = new JTextField(PLACEHOLDER_BUSQUEDA) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
                
                g2.setColor(recursos.Color.BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 36, 36);
                
                if (iconoLupaModerno != null) {
                    int xLupa = 14; 
                    int yLupa = (getHeight() - iconoLupaModerno.getIconHeight()) / 2; 
                    g2.drawImage(iconoLupaModerno.getImage(), xLupa, yLupa, null);
                } else {
                    g2.setColor(recursos.Color.CAT_INACTIVO);
                    g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                    g2.drawString("🔍", 14, (getHeight() / 2) + 5);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };

        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscar.setForeground(recursos.Color.CAT_INACTIVO); 
        txtBuscar.setPreferredSize(new Dimension(380, 42));
        txtBuscar.setOpaque(false);
        txtBuscar.setBorder(new EmptyBorder(0, 44, 0, 15)); 

        txtBuscar.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtBuscar.getText().equals(PLACEHOLDER_BUSQUEDA)) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(recursos.Color.INK); 
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtBuscar.getText().trim().isEmpty()) {
                    txtBuscar.setText(PLACEHOLDER_BUSQUEDA);
                    txtBuscar.setForeground(recursos.Color.CAT_INACTIVO); 
                }
            }
        });

    txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            filtrarYRefrescarGrilla();
        }
    });
        panelFiltrosIzquierda.add(txtBuscar);

        JPanel panelGrupoEspecies = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
                g2.setColor(recursos.Color.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 36, 36);
                g2.dispose();
            }
        };
        panelGrupoEspecies.setOpaque(false);
        panelGrupoEspecies.setBorder(new EmptyBorder(2, 6, 2, 6));
        
        String[] especies = {"Todos", "Perro", "Gato", "Otro"};
        for (String esp : especies) {
            panelGrupoEspecies.add(crearBotonFiltroPildora(esp, true));
        }
        panelFiltrosIzquierda.add(panelGrupoEspecies);

        JPanel panelGrupoEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
                g2.setColor(recursos.Color.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 36, 36);
                g2.dispose();
            }
        };
        panelGrupoEstado.setOpaque(false);
        panelGrupoEstado.setBorder(new EmptyBorder(2, 6, 2, 6));
        
        String[] estados = {"Todos", "Activo", "Inactivo"};
        for (String est : estados) {
            panelGrupoEstado.add(crearBotonFiltroPildora(est, false));
        }
        panelFiltrosIzquierda.add(panelGrupoEstado);

        panelBarraSuperior.add(panelFiltrosIzquierda, BorderLayout.WEST);

        JPanel panelContenedorBotonDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelContenedorBotonDerecha.setOpaque(false);

        JButton btnAgregarMascota = new JButton("Agregar Mascota") {
            private boolean hover = false;
            {
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setBorder(new EmptyBorder(0, 20, 0, 20));
                setPreferredSize(new Dimension(170, 40));

                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override public void mouseEntered(java.awt.event.MouseEvent e) { hover = true; repaint(); }
                    @Override public void mouseExited(java.awt.event.MouseEvent e) { hover = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Transición cromática interactiva (PRIMARY a HOVER VERDE)
                Color colorFondo = hover ? new Color(15, 118, 110) : recursos.Color.PRIMARY;
                g2.setColor(colorFondo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36); // Redondeo perfecto tipo píldora
                
                setForeground(Color.WHITE);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        // Escuchador dinámico: Levanta el diálogo pasando "null" como segundo parámetro para activar el Modo Registrar
        btnAgregarMascota.addActionListener(e -> {
            JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(this);
            DialogoEditarPaciente dialogoAlta = new DialogoEditarPaciente(ventanaPadre, null); //
            dialogoAlta.setVisible(true);
            
            // Cuando se cierra el diálogo, refrescamos automáticamente la grilla por si dio de alta una nueva mascota
            filtrarYRefrescarGrilla();
        });

        panelContenedorBotonDerecha.add(btnAgregarMascota);
        // Añadimos el contenedor del botón al lado ESTE de la barra superior
        panelBarraSuperior.add(panelContenedorBotonDerecha, BorderLayout.EAST);

        add(panelBarraSuperior, BorderLayout.NORTH);

        panelGrillaPacientes = new JPanel(new GridLayout(0, 4, 20, 20)); 
        panelGrillaPacientes.setOpaque(false);

        JPanel contenedorGrillaInmóvil = new JPanel(new BorderLayout());
        contenedorGrillaInmóvil.setOpaque(false);
        contenedorGrillaInmóvil.add(panelGrillaPacientes, BorderLayout.NORTH);

        scrollGrilla = new JScrollPane(contenedorGrillaInmóvil);
        scrollGrilla.setBorder(null);
        scrollGrilla.setOpaque(true);
        scrollGrilla.getViewport().setOpaque(true);
        scrollGrilla.setBackground(recursos.Color.BG);
        scrollGrilla.getViewport().setBackground(recursos.Color.BG);
        scrollGrilla.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        
        JScrollBar barraVertical = scrollGrilla.getVerticalScrollBar();
        barraVertical.setUI(new vista.componentes.ModernScrollBarUI()); 
        barraVertical.setPreferredSize(new Dimension(8, 0));
        barraVertical.setOpaque(false);
        barraVertical.setUnitIncrement(14);
        
        add(scrollGrilla, BorderLayout.CENTER);

        actualizar();
    }

    public void actualizar() {
        filtrarYRefrescarGrilla();
    }

   private void filtrarYRefrescarGrilla() {
        panelGrillaPacientes.removeAll();

        ArrayList<Animal> todosLosAnimales = controlador.getVeterinaria().getPacientesRegistrados();
        String busqueda = txtBuscar.getText().toLowerCase().trim();
        
        if (busqueda.equals(PLACEHOLDER_BUSQUEDA.toLowerCase())) {
            busqueda = "";
        }

        for (Animal a : todosLosAnimales) {
            boolean coincideTexto = busqueda.isEmpty() || 
                    a.getNombre().toLowerCase().contains(busqueda);

            boolean coincideEspecie = filtroEspecieActual.equals("Todos") || 
                                    a.getCategoriaFiltro().equals(filtroEspecieActual);

            boolean coincideEstado = filtroEstadoActual.equals("Todos") || 
                                    (filtroEstadoActual.equals("Activo") && a.isActivo()) || 
                                    (filtroEstadoActual.equals("Inactivo") && !a.isActivo());

            if (coincideTexto && coincideEspecie && coincideEstado) {
                panelGrillaPacientes.add(crearTarjetaPacienteHD(a));
            }
        }

        panelGrillaPacientes.revalidate();
        panelGrillaPacientes.repaint();
    }

    private JButton crearBotonFiltroPildora(String texto, boolean esDeEspecie) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean seleccionado = esDeEspecie ? filtroEspecieActual.equals(texto) : filtroEstadoActual.equals(texto);
                
                if (seleccionado) {
                    g2.setColor(recursos.Color.PRIMARY);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    setForeground(Color.WHITE);
                } else {
                    setForeground(recursos.Color.MUTED);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16)); 
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(e -> {
            if (esDeEspecie) {
                filtroEspecieActual = texto;
            } else {
                filtroEstadoActual = texto;
            }
            JPanel parent = (JPanel) btn.getParent();
            if (parent != null) {
                parent.repaint();
            }
            filtrarYRefrescarGrilla();
        });

        return btn;
    }

    private JPanel crearTarjetaPacienteHD(Animal a) {
        int altoBarra = 6;
        int radioEsquina = 24; 
 
        Color colorGrisTagFondo = recursos.Color.BG;       
        Color colorGrisTagBorde = recursos.Color.BORDER;       
        Color colorGrisTagTexto = new Color(71, 85, 105);         

        JPanel card = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radioEsquina, radioEsquina);
                
                Color colorInicio = Color.decode(a.getColorInicioHex());
                Color colorFin = Color.decode(a.getColorFinHex());

                GradientPaint degradadoCabecera = new GradientPaint(0, 0, colorInicio, getWidth(), 0, colorFin);
                g2.setPaint(degradadoCabecera);
                
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radioEsquina, radioEsquina));
                g2.fillRect(0, 0, getWidth(), altoBarra);
                
                g2.setClip(null); 
                g2.setColor(recursos.Color.BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radioEsquina, radioEsquina);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        Dimension tamanoFijoTarjeta = new Dimension(200, 300);
        card.setPreferredSize(tamanoFijoTarjeta);
        card.setMinimumSize(tamanoFijoTarjeta);
        card.setMaximumSize(tamanoFijoTarjeta);
        
        JPanel panelAvatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color colorInicio = Color.decode(a.getColorInicioHex());
                Color colorFin = Color.decode(a.getColorFinHex());

                GradientPaint degradadoEspecie = new GradientPaint(0, 0, colorInicio, 0, getHeight(), colorFin);
                
                g2.setPaint(degradadoEspecie);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panelAvatar.setPreferredSize(new Dimension(75, 75));
        panelAvatar.setMinimumSize(new Dimension(75, 75));
        panelAvatar.setMaximumSize(new Dimension(75, 75));
        panelAvatar.setOpaque(false);
        panelAvatar.setLayout(new GridBagLayout());

        JLabel lblIcono = new JLabel();
        try {
            String rutaImg = a.getImagen();
            java.awt.image.BufferedImage imgBuffer = javax.imageio.ImageIO.read(new java.io.File(rutaImg));
            
            java.awt.image.BufferedImage resizedImg = new java.awt.image.BufferedImage(42, 42, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(imgBuffer, 0, 0, 42, 42, null);
            g2.dispose();
            lblIcono.setIcon(new ImageIcon(resizedImg));
        } catch (Exception e) {
            lblIcono.setText("🐾");
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        }
        panelAvatar.add(lblIcono);

        JPanel panelAvatarWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelAvatarWrapper.setOpaque(false);
        panelAvatarWrapper.add(panelAvatar);

        JPanel panelInfoCentral = new JPanel();
        panelInfoCentral.setOpaque(false);
        panelInfoCentral.setLayout(new BoxLayout(panelInfoCentral, BoxLayout.Y_AXIS));

        JLabel lblNombre = new JLabel((a != null && a.getNombre() != null) ? a.getNombre() : "Sin nombre", SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNombre.setForeground(recursos.Color.INK);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panelTags = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelTags.setOpaque(false);
        
        JLabel lblEstadoActivo = new JLabel(a.getStringEstado(), SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
                
                Color fondoTag = a.isActivo() ? new Color(220, 252, 231) : colorGrisTagFondo;
                Color bordeTag = a.isActivo() ? new Color(187, 247, 208) : colorGrisTagBorde;
                
                g2.setColor(fondoTag); 
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                g2.setColor(bordeTag); 
                g2.setStroke(new BasicStroke(1.2f)); 
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblEstadoActivo.setFont(new Font("Segoe UI", Font.BOLD, 10));
        
        Color colorTextoTag = a.isActivo() ? recursos.Color.SUCCESS : colorGrisTagTexto;
        lblEstadoActivo.setForeground(colorTextoTag);
        
        lblEstadoActivo.setBorder(new EmptyBorder(3, 12, 3, 12));
        panelTags.add(lblEstadoActivo);
        
        panelInfoCentral.add(lblNombre);
        panelInfoCentral.add(Box.createVerticalStrut(2));
        panelInfoCentral.add(Box.createVerticalStrut(6));
        panelInfoCentral.add(panelTags);

        JPanel panelDatosGrid = new JPanel(new GridLayout(3, 2, 0, 4));
        panelDatosGrid.setOpaque(false);
        panelDatosGrid.setBorder(new EmptyBorder(10, 5, 5, 5));

        agregarFilaFicha(panelDatosGrid, "Dueño", a.getResponsable().getNombre());
        agregarFilaFicha(panelDatosGrid, "Edad", "3 años"); 
        agregarFilaFicha(panelDatosGrid, "Próx. turno", "06 Jun 2026");

        JButton btnFicha = new JButton("Ver ficha →") {
            private boolean mouseEncima = false;
            {
                setFocusPainted(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setOpaque(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setBorder(new EmptyBorder(8, 0, 8, 0));

                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override public void mouseEntered(java.awt.event.MouseEvent e) { mouseEncima = true; repaint(); }
                    @Override public void mouseExited(java.awt.event.MouseEvent e) { mouseEncima = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (mouseEncima) {
                    g2.setColor(recursos.Color.BORDER); 
                } else {
                    g2.setColor(recursos.Color.BG); 
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16); 
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnFicha.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFicha.setForeground(new Color(71, 85, 105)); 

        btnFicha.addActionListener(e -> {
            this.removeAll();
            
            FichaPaciente vistaPerfil = new FichaPaciente(a, () -> {
                this.removeAll();
                
                this.setLayout(new BorderLayout(0, 15));
                this.add(panelBarraSuperior, BorderLayout.NORTH); 
                this.add(scrollGrilla, BorderLayout.CENTER);
                this.actualizar();
                
                this.revalidate();
                this.repaint();
            });
            
            this.setLayout(new BorderLayout());
            this.add(vistaPerfil, BorderLayout.CENTER);
            
            this.revalidate();
            this.repaint();
        });

        JPanel panelCuerpoTarjeta = new JPanel(new BorderLayout(0, 12));
        panelCuerpoTarjeta.setOpaque(false);
        panelCuerpoTarjeta.add(panelInfoCentral, BorderLayout.NORTH);
        panelCuerpoTarjeta.add(panelDatosGrid, BorderLayout.CENTER);
        panelCuerpoTarjeta.add(btnFicha, BorderLayout.SOUTH);

        card.add(panelAvatarWrapper, BorderLayout.NORTH);
        card.add(panelCuerpoTarjeta, BorderLayout.CENTER);

        return card;
    }

    private void agregarFilaFicha(JPanel panel, String clave, String valor) {
        JLabel lblClave = new JLabel(clave);
        lblClave.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblClave.setForeground(recursos.Color.CAT_INACTIVO); 
        
        JLabel lblValor = new JLabel(valor, SwingConstants.RIGHT);
        lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 12)); 
        lblValor.setForeground(new Color(71, 85, 105)); 

        panel.add(lblClave);
        panel.add(lblValor);
    }
}