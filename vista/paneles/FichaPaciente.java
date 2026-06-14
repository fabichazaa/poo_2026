package vista.paneles;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelo.Animal;
import modelo.Responsable;
import modelo.Turno;
import vista.EstiloPaleta;
import modelo.TipoTurno;

public class FichaPaciente extends JPanel {

    private final Animal animal;
    private final Runnable accionVolver;
    private final ControladorVeterinaria controlador;

    public FichaPaciente(Animal animal, Runnable accionVolver) {
        this.animal = animal;
        this.accionVolver = accionVolver;
        this.controlador = ControladorVeterinaria.getInstancia();

        setLayout(new BorderLayout(0, 15)); // flex-direction: column; column-gap: 0px; row-gap: 15px
        setBackground(EstiloPaleta.FONDO_GRIS);
        setBorder(new EmptyBorder(15, 25, 15, 25)); // margin

        initHeader();
        initCuerpo();
    }

    private void initHeader() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false); // enables transparency

        JButton btnVolver = new JButton("‹") {
            private boolean hover = false;
            {
                setFocusPainted(false); // no outline
                setContentAreaFilled(false); // no background color
                setBorderPainted(false); // no border
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new java.awt.event.MouseAdapter() { // repaint() triggers paintComponent()
                    public void mouseEntered(java.awt.event.MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(java.awt.event.MouseEvent e) { hover = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // smoothe edges (supposedly)
                g2.setColor(hover ? new Color(226, 232, 240) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16); // dimensions and border-radius
                g2.setColor(EstiloPaleta.BORDE_TARJETA);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16); // border
                g2.dispose(); // ???
                super.paintComponent(g);
            }
        };

        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 26));
        btnVolver.setForeground(EstiloPaleta.TEXTO_OSCURO);
        btnVolver.setPreferredSize(new Dimension(42, 42));
        btnVolver.setBorder(new EmptyBorder(0, 0, 4, 0));
        btnVolver.addActionListener(e -> accionVolver.run()); // goes back

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.add(btnVolver);

        panelHeader.add(panelIzquierdo, BorderLayout.WEST);
        add(panelHeader, BorderLayout.NORTH);
    }

    private void initCuerpo() {
        JPanel panelColumnasUnificadas = new JPanel(new BorderLayout(20, 0));
        panelColumnasUnificadas.setOpaque(false);

        // ========================================================
        // LEFT COLUMN: Animal & Responsable
        // ========================================================
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setPreferredSize(new Dimension(260, 0)); 

        // Animal Card
        JPanel cardPaciente = new JPanel(new BorderLayout(0, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                
                // gradient for the top border
                Color colorInicio = Color.decode(animal.getColorInicioHex());
                Color colorFin = Color.decode(animal.getColorFinHex());
                GradientPaint deg = new GradientPaint(0, 0, colorInicio, 0, getHeight(), colorFin);
                g2.setPaint(deg);
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                g2.fillRect(0, 0, getWidth(), 8); 
                
                g2.setClip(null);
                g2.setColor(EstiloPaleta.BORDE_TARJETA);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        cardPaciente.setOpaque(false);
        cardPaciente.setBorder(new EmptyBorder(20, 15, 15, 15));

        Dimension dimensionesPaciente = new Dimension(260, 310);
        cardPaciente.setPreferredSize(dimensionesPaciente);
        cardPaciente.setMinimumSize(dimensionesPaciente);
        cardPaciente.setMaximumSize(dimensionesPaciente);

        JPanel panelAvatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color colorInicio = Color.decode(animal.getColorInicioHex());
                Color colorFin = Color.decode(animal.getColorFinHex());
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

        // Cambiamos a un panel intermedio con BoxLayout vertical para la info básica, evitando colisiones
        JPanel panelInfoBasica = new JPanel();
        panelInfoBasica.setOpaque(false);
        panelInfoBasica.setLayout(new BoxLayout(panelInfoBasica, BoxLayout.Y_AXIS));

        JLabel lblNombre = new JLabel(animal.getNombre(), SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 22)); // Restaurado tu Bold original de nombre
        lblNombre.setForeground(EstiloPaleta.TEXTO_OSCURO);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRaza = new JLabel(animal.getEspecie() + " · " + (animal.getSexo() ? "Macho" : "Hembra"), SwingConstants.CENTER);
        lblRaza.setFont(new Font("Segoe UI", Font.BOLD, 12)); 
        lblRaza.setForeground(new Color(217, 119, 6)); 
        lblRaza.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelInfoBasica.add(lblNombre);
        panelInfoBasica.add(Box.createVerticalStrut(4));
        panelInfoBasica.add(lblRaza);

        // Contenedor de la grilla de datos de 3 filas apiladas
        JPanel panelDatosGrid = new JPanel(new GridLayout(3, 1, 0, 6));
        panelDatosGrid.setOpaque(false);
        panelDatosGrid.setBorder(new EmptyBorder(10, 5, 5, 5));

        agregarFilaDatosFicha(panelDatosGrid, "Edad", animal.calcularEdad() + " años");
        agregarFilaDatosFicha(panelDatosGrid, "Peso", animal.getPeso() + " kg");
        agregarFilaDatosFicha(panelDatosGrid, "Estado", animal.isActivo() ? "Activo" : "Inactivo");

        // Agrupamos el header y los textos en el Norte/Centro, dejando la grilla limpia en el Sur
        JPanel panelContenedorSuperiorMascota = new JPanel(new BorderLayout(0, 6));
        panelContenedorSuperiorMascota.setOpaque(false);
        panelContenedorSuperiorMascota.add(panelAvatarWrapper, BorderLayout.NORTH);
        panelContenedorSuperiorMascota.add(panelInfoBasica, BorderLayout.CENTER);

        cardPaciente.add(panelContenedorSuperiorMascota, BorderLayout.NORTH);
        cardPaciente.add(panelDatosGrid, BorderLayout.SOUTH);

        // 2. TARJETA COMPLETA DEL RESPONSABLE
        JPanel cardDueno = new JPanel(new BorderLayout(0, 14)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                
                Color blueInicio = new Color(14, 165, 233); 
                Color blueFin = new Color(37, 99, 235);    
                g2.setPaint(new GradientPaint(0, 0, blueInicio, getWidth(), 0, blueFin));
                
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                g2.fillRect(0, 0, getWidth(), 8); 
                
                g2.setClip(null);
                g2.setColor(EstiloPaleta.BORDE_TARJETA);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        cardDueno.setOpaque(false);
        cardDueno.setBorder(new EmptyBorder(18, 15, 18, 15));

        Dimension dimDueno = new Dimension(260, 180);
        cardDueno.setPreferredSize(dimDueno);
        cardDueno.setMinimumSize(dimDueno);
        cardDueno.setMaximumSize(dimDueno);

        JLabel lblTagDueno = new JLabel("RESPONSABLE");
        lblTagDueno.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTagDueno.setForeground(EstiloPaleta.TEXTO_GRIS_BASE);
        cardDueno.add(lblTagDueno, BorderLayout.NORTH);

        Responsable responsable = animal.getResponsable();

        if (responsable != null) {
            JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
            panelUsuario.setOpaque(false);

            JPanel panelAvatarCuadrado = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color azulInicio = new Color(14, 165, 233); 
                    Color azulFin = new Color(37, 99, 235);    
                    g2.setPaint(new GradientPaint(0, 0, azulInicio, 0, getHeight(), azulFin));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12); 
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            panelAvatarCuadrado.setPreferredSize(new Dimension(44, 44));
            panelAvatarCuadrado.setMinimumSize(new Dimension(44, 44));
            panelAvatarCuadrado.setMaximumSize(new Dimension(44, 44));
            panelAvatarCuadrado.setOpaque(false);
            panelAvatarCuadrado.setLayout(new GridBagLayout());

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
            lblNombreDueno.setForeground(EstiloPaleta.TEXTO_OSCURO);
            
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
            lblTextoCel.setForeground(EstiloPaleta.TEXTO_OSCURO);
            filaCelular.add(lblIconoCel);
            filaCelular.add(lblTextoCel);

            JPanel filaUbicacion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            filaUbicacion.setOpaque(false);
            JLabel lblIconoUbi = new JLabel(cargarIconoHD("imagenes/emojis/ubicacion.png", 16, 16));
            JLabel lblTextoUbi = new JLabel(responsable.getDireccionCompleta());
            lblTextoUbi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblTextoUbi.setForeground(EstiloPaleta.TEXTO_OSCURO);
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

        panelIzquierdo.add(cardPaciente);
        panelIzquierdo.add(Box.createVerticalStrut(15));
        panelIzquierdo.add(cardDueno);
        panelIzquierdo.add(Box.createVerticalGlue()); // Absorbe el aire restante

        // ========================================================
        // COLUMNA DERECHA: GRILLA DE TURNOS DINÁMICOS REALES
        // ========================================================
       // ========================================================
        // COLUMNA DERECHA: PANEL GRANDE CON EL DEGRADADO PREMIUM FIGMA
        // ========================================================
        JPanel panelDerecho = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo Blanco de la Tarjeta Contenedora
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                
                // 🎨 REPARADO: Degradado horizontal premium exacto al mockup (Violeta a Azul Eléctrico)
                Color violetaFigma = new Color(139, 92, 246); // #8B5CF6 (Violeta vibrante)
                Color azulFigma = new Color(59, 130, 246);    // #3B82F6 (Azul corporativo)
                GradientPaint degradadoSuperior = new GradientPaint(0, 0, violetaFigma, getWidth(), 0, azulFigma);
                g2.setPaint(degradadoSuperior);
                
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                g2.fillRect(0, 0, getWidth(), 8); // Grosor de 8px simétrico
                
                g2.setClip(null);
                g2.setColor(EstiloPaleta.BORDE_TARJETA);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        panelDerecho.setOpaque(false);
        panelDerecho.setBorder(new EmptyBorder(18, 15, 12, 15)); 
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setOpaque(false); // Anula fondos cuadrados grises automáticos
        
        // 🔄 RESTAURACIÓN DE LA UI DE TABS: Desactivamos la pintura de solapas nativas
        tabs.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {}
            @Override protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {}
            @Override protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                // Dejar vacío de forma intencional destruye los bloques grises de fondo de Swing
            }
        });
        
        // FILTRADO DESDE EL CONTROLADOR
        List<Turno> todosLosTurnos = controlador.getVeterinaria().getListaTurnos();
        List<Turno> turnosHistorialReal = new ArrayList<>();
        List<Turno> turnosPendientesReal = new ArrayList<>();

        for (Turno t : todosLosTurnos) {
            if (t.getAnimal() != null && t.getAnimal().getIdAnimal().equals(animal.getIdAnimal())) {
                if (t.estaCompletado()) {
                    turnosHistorialReal.add(t);
                } else if (t.esPendiente()) {
                    turnosPendientesReal.add(t);
                }
            }
        }

        // Pestaña 1: HISTORIAL
        JPanel panelHistorial = new JPanel();
        panelHistorial.setBackground(Color.WHITE);
        panelHistorial.setLayout(new BoxLayout(panelHistorial, BoxLayout.Y_AXIS));
        
        JPanel contenedorHistorialInmovil = new JPanel(new BorderLayout());
        contenedorHistorialInmovil.setBackground(Color.WHITE);
        contenedorHistorialInmovil.add(panelHistorial, BorderLayout.NORTH); 
        
        if (turnosHistorialReal.isEmpty()) {
            panelHistorial.setBorder(new EmptyBorder(30, 10, 10, 10));
            JLabel lblVacio = new JLabel("No hay registros médicos completados.", SwingConstants.CENTER);
            lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lblVacio.setForeground(EstiloPaleta.TEXTO_GRIS_BASE);
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelHistorial.add(lblVacio);
        } else {
            panelHistorial.setBorder(new EmptyBorder(15, 5, 15, 5));
            for (int i = 0; i < turnosHistorialReal.size(); i++) {
                Turno t = turnosHistorialReal.get(i);
                panelHistorial.add(crearFilaTurnoDinamica(t));
                if (i < turnosHistorialReal.size() - 1) {
                    panelHistorial.add(Box.createVerticalStrut(14));
                }
            }
        }

        // Pestaña 2: VACUNAS
        JPanel panelVacunas = new JPanel(new BorderLayout());
        panelVacunas.setBackground(Color.WHITE);
        panelVacunas.setBorder(new EmptyBorder(30, 10, 10, 10));
        JLabel lblVacunasVacio = new JLabel("No hay registro de vacunas aplicadas.", SwingConstants.CENTER);
        lblVacunasVacio.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblVacunasVacio.setForeground(EstiloPaleta.TEXTO_GRIS_BASE);
        panelVacunas.add(lblVacunasVacio, BorderLayout.NORTH);

        // Pestaña 3: TURNOS
        JPanel panelTurnosFuturos = new JPanel();
        panelTurnosFuturos.setBackground(Color.WHITE);
        panelTurnosFuturos.setLayout(new BoxLayout(panelTurnosFuturos, BoxLayout.Y_AXIS));

        JPanel contenedorTurnosInmovil = new JPanel(new BorderLayout());
        contenedorTurnosInmovil.setBackground(Color.WHITE);
        contenedorTurnosInmovil.add(panelTurnosFuturos, BorderLayout.NORTH); 

        if (turnosPendientesReal.isEmpty()) {
            panelTurnosFuturos.setBorder(new EmptyBorder(30, 10, 10, 10));
            JLabel lblVacio = new JLabel("No hay turnos próximos agendados.", SwingConstants.CENTER);
            lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lblVacio.setForeground(EstiloPaleta.TEXTO_GRIS_BASE);
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelTurnosFuturos.add(lblVacio);
        } else {
            panelTurnosFuturos.setBorder(new EmptyBorder(15, 5, 15, 5));
            for (int i = 0; i < turnosPendientesReal.size(); i++) {
                Turno t = turnosPendientesReal.get(i);
                panelTurnosFuturos.add(crearFilaTurnoDinamica(t));
                if (i < turnosPendientesReal.size() - 1) {
                    panelTurnosFuturos.add(Box.createVerticalStrut(14));
                }
            }
        }

        tabs.addTab("Historial", contenedorHistorialInmovil);
        tabs.addTab("Vacunas", panelVacunas);
        tabs.addTab("Turnos", contenedorTurnosInmovil);
        
        configurarEstiloPestanas(tabs);
        panelDerecho.add(tabs, BorderLayout.CENTER);

        panelColumnasUnificadas.add(panelIzquierdo, BorderLayout.WEST); 
        panelColumnasUnificadas.add(panelDerecho, BorderLayout.CENTER);  

        JScrollPane scrollGlobalFicha = new JScrollPane(panelColumnasUnificadas);
        scrollGlobalFicha.setBorder(null);
        scrollGlobalFicha.setOpaque(false);
        scrollGlobalFicha.getViewport().setOpaque(false);
        scrollGlobalFicha.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        JScrollBar barraVerticalGlobal = scrollGlobalFicha.getVerticalScrollBar();
        barraVerticalGlobal.setUI(new ModernScrollBarUI()); 
        barraVerticalGlobal.setPreferredSize(new Dimension(8, 0));
        barraVerticalGlobal.setOpaque(false);
        barraVerticalGlobal.setUnitIncrement(16); 

        add(scrollGlobalFicha, BorderLayout.CENTER);
    }

    private void configurarEstiloPestanas(JTabbedPane tabs) {
        tabs.setOpaque(false);
        for (int i = 0; i < tabs.getTabCount(); i++) {
            String titulo = tabs.getTitleAt(i);
            final int indicePestana = i;
            
            JLabel lblTabCustom = new JLabel(titulo, SwingConstants.CENTER) {
                private boolean mouseEncima = false;
                {
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                    setPreferredSize(new Dimension(95, 36)); 
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    
                    // 🔥 LA LÍNEA CLAVE: Evita que Swing dibuje el recuadro punteado negro al hacer click
                    setFocusable(false); 
                    
                    setOpaque(false); 
                    
                    addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseEntered(java.awt.event.MouseEvent e) { mouseEncima = true; repaint(); }
                        public void mouseExited(java.awt.event.MouseEvent e) { mouseEncima = false; repaint(); }
                        public void mousePressed(java.awt.event.MouseEvent e) { tabs.setSelectedIndex(indicePestana); }
                    });
                }

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    boolean estaSeleccionada = (tabs.getSelectedIndex() == indicePestana);
                    Color colorVioletaFigma = new Color(139, 92, 246); 
                    
                    if (estaSeleccionada) {
                        setForeground(colorVioletaFigma);
                        g2.setColor(new Color(243, 232, 255)); // Fondo píldora suave
                        g2.fillRoundRect(0, 2, getWidth(), getHeight() - 4, 12, 12);
                        
                        // 🔥 REMOVIDO: Se eliminó la línea g2.fillRect que pintaba la barrita violeta oscuro inferior
                    } else if (mouseEncima) {
                        setForeground(new Color(109, 40, 217)); 
                        g2.setColor(new Color(241, 245, 249)); 
                        g2.fillRoundRect(0, 2, getWidth(), getHeight() - 4, 12, 12);
                    } else {
                        setForeground(new Color(148, 163, 184)); 
                    }
                    
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            tabs.setTabComponentAt(i, lblTabCustom);
        }
        tabs.addChangeListener(e -> tabs.repaint());
    }
    
    private JPanel crearFilaTurnoDinamica(Turno t) {
        JPanel fila = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                
                g2.setColor(EstiloPaleta.BORDE_TARJETA);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        fila.setOpaque(false);
        fila.setBorder(new EmptyBorder(12, 16, 12, 16));

        TipoTurno tipo = t.getTipo();

        JPanel panelLineaSuperior = new JPanel(new BorderLayout());
        panelLineaSuperior.setOpaque(false);

        JPanel panelIzquierdoInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelIzquierdoInfo.setOpaque(false);

        JLabel lblEmojiTurno = new JLabel();
        ImageIcon iconoHD = cargarIconoHD(tipo.getRutaEmoji(), 20, 20);
        if (iconoHD != null) {
            lblEmojiTurno.setIcon(iconoHD);
        } else {
            lblEmojiTurno.setText(tipo.getEmojiRespaldo());
            lblEmojiTurno.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        }

        JPanel panelTextosLabels = new JPanel(new GridLayout(2, 1, 0, 1));
        panelTextosLabels.setOpaque(false);
        
        JLabel lblTitulo = new JLabel(tipo.getDescripcion());
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(EstiloPaleta.TEXTO_OSCURO);
        
        String nombreVet = (t.getVeterinario() != null) ? "Dr. " + t.getVeterinario().getApellido() : "Sin asignar";
        JLabel lblMedico = new JLabel(nombreVet + "  •  " + t.getHora() + " hs");
        lblMedico.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMedico.setForeground(EstiloPaleta.TEXTO_GRIS_BASE);
        
        panelTextosLabels.add(lblTitulo);
        panelTextosLabels.add(lblMedico);

        panelIzquierdoInfo.add(lblEmojiTurno);
        panelIzquierdoInfo.add(panelTextosLabels);

        JLabel lblFecha = new JLabel(t.getFecha(), SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(241, 245, 249)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFecha.setForeground(EstiloPaleta.TEXTO_GRIS_BASE);
        lblFecha.setBorder(new EmptyBorder(4, 10, 4, 10)); 

        panelLineaSuperior.add(panelIzquierdoInfo, BorderLayout.WEST);
        panelLineaSuperior.add(lblFecha, BorderLayout.EAST);

        fila.add(panelLineaSuperior, BorderLayout.NORTH);

        // --- BLOQUE INFERIOR (Caja de Texto Gris de Observaciones simulada) ---
        String obs = t.getObservaciones();
        if (obs != null && !obs.isBlank()) {
            JPanel panelTextBoxObs = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(248, 250, 252)); 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.dispose();
                }
            };
            panelTextBoxObs.setOpaque(false);
            panelTextBoxObs.setBorder(new EmptyBorder(8, 12, 8, 12)); 

            // 🔥 MODIFICADO: Removemos el emoji en texto y creamos el Label limpio con espaciado
            JLabel lblNotas = new JLabel(obs);
            lblNotas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblNotas.setForeground(new Color(100, 116, 139)); 
            
            // 🔥 NUEVO: Cargamos la imagen HD del emoji e inyectamos espacio de colchón
            ImageIcon iconoComentario = cargarIconoHD("imagenes/emojis/comentario.png", 16, 16);
            if (iconoComentario != null) {
                lblNotas.setIcon(iconoComentario);
                lblNotas.setIconTextGap(8); // Agrega un espacio elegante entre la imagen y el texto
            } else {
                // Respaldo clásico por si el archivo físico no se encuentra en la carpeta
                lblNotas.setText("💬  " + obs);
            }
            
            panelTextBoxObs.add(lblNotas, BorderLayout.CENTER);
            
            JPanel contenedorMargenSouth = new JPanel(new BorderLayout());
            contenedorMargenSouth.setOpaque(false);
            contenedorMargenSouth.setBorder(new EmptyBorder(4, 0, 0, 0));
            contenedorMargenSouth.add(panelTextBoxObs, BorderLayout.CENTER);

            fila.add(contenedorMargenSouth, BorderLayout.CENTER);
        }

        return fila;
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
        lblClave.setForeground(EstiloPaleta.TEXTO_GRIS_BASE);
        
        JLabel lblValor = new JLabel(valor, SwingConstants.RIGHT);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        lblValor.setForeground(EstiloPaleta.TEXTO_OSCURO); 

        fila.add(lblClave, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);
        panel.add(fila);
    }

    // =========================================================================
    // SCROLLBAR INTERNA PERSONALIZADA (ModernScrollBarUI)
    // =========================================================================
    private static class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {}
        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color colorFinal = isDragging ? new Color(100, 116, 139) : (isThumbRollover() ? new Color(148, 163, 184) : new Color(203, 213, 225));
            g2.setColor(colorFinal);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
            g2.dispose();
        }
        @Override protected JButton createDecreaseButton(int orientation) { return crearBotonInvisible(); }
        @Override protected JButton createIncreaseButton(int orientation) { return crearBotonInvisible(); }
        private JButton crearBotonInvisible() {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            return btn;
        }
    }

    // 🔥 MÉTODO AUXILIAR RECUPERADO: Escala las imágenes pixel-perfect sin romper nada
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
            return null; // Si no encuentra la imagen, devuelve null de forma segura
        }
    }
}