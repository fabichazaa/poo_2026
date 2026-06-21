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
import vista.dialogos.DialogoEditarPaciente;
import modelo.TipoTurno;

public class FichaPaciente extends JPanel {

    private final Animal animal;
    private final Runnable accionVolver;
    private final ControladorVeterinaria controlador;

    private JLabel lblNombre;
    private JLabel lblRaza;
    private JPanel panelDatosGrid;

    public FichaPaciente(Animal animal, Runnable accionVolver) {
        this.animal = animal;
        this.accionVolver = accionVolver;
        this.controlador = ControladorVeterinaria.getInstancia();

        setLayout(new BorderLayout(0, 15)); // flex-direction: column; column-gap: 0px; row-gap: 15px
        setBackground(recursos.Color.BG);
        setBorder(new EmptyBorder(15, 25, 15, 25)); // margin

        initHeader();
        initCuerpo();
    }

    private void initHeader() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false); // habilita transparencia

        // --- BOTÓN DE VOLVER (Lado Izquierdo) ---
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
                g2.setColor(hover ? recursos.Color.BORDER : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(recursos.Color.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 26));
        btnVolver.setForeground(recursos.Color.INK);
        btnVolver.setPreferredSize(new Dimension(42, 42));
        btnVolver.setBorder(new EmptyBorder(0, 0, 4, 0));
        btnVolver.addActionListener(e -> accionVolver.run());

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.add(btnVolver);

        // --- BOTÓN DE EDITAR (Lado Derecho) ---
        // LLamamos a tu función para generar el botón y lo envolvemos en un FlowLayout derecho
        JButton btnEditar = crearBtnEditarFicha();
        JPanel panelDerechoHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelDerechoHeader.setOpaque(false);
        panelDerechoHeader.add(btnEditar);

        // --- ENSAMBLE DE LA CABECERA ---
        panelHeader.add(panelIzquierdo, BorderLayout.WEST);   // Flecha a la izquierda
        panelHeader.add(panelDerechoHeader, BorderLayout.EAST); // Tres puntitos a la derecha
        
        add(panelHeader, BorderLayout.NORTH);
    }

    private void initCuerpo() {
        JPanel panelColumnasUnificadas = new JPanel(new BorderLayout(20, 0));
        panelColumnasUnificadas.setOpaque(false);

        // ========================================================
        // LEFT COLUMN: Patient & Owner
        // ========================================================
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setPreferredSize(new Dimension(260, 0)); 

        // Patient Card
        JPanel cardPaciente = new JPanel(new BorderLayout(0, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                
                // gradient for the animal card top border
                Color colorInicio = Color.decode(animal.getColorInicioHex());
                Color colorFin = Color.decode(animal.getColorFinHex());
                GradientPaint deg = new GradientPaint(0, 0, colorInicio , getWidth(), 0, colorFin);
                g2.setPaint(deg);
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                g2.fillRect(0, 0, getWidth(), 8); 
                
                g2.setClip(null);
                g2.setColor(recursos.Color.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        cardPaciente.setOpaque(false);
        cardPaciente.setBorder(new EmptyBorder(20, 15, 15, 15));

        Dimension dimensionesPaciente = new Dimension(260, 360);
        cardPaciente.setPreferredSize(dimensionesPaciente);
        cardPaciente.setMinimumSize(dimensionesPaciente);
        cardPaciente.setMaximumSize(dimensionesPaciente);

        // Patient Picture Box
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

        // Patient Picture
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

        // PROTECTOR WRAPPER: BorderLayout.NORTH forces panelAvatar width stretch to 100% 
        // By using FlowLayout(CENTER), we protect the dimensions of panel avatar
        JPanel panelAvatarWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelAvatarWrapper.setOpaque(false);
        panelAvatarWrapper.add(panelAvatar);

        JPanel panelInfoBasica = new JPanel();
        panelInfoBasica.setOpaque(false);
        panelInfoBasica.setLayout(new BoxLayout(panelInfoBasica, BoxLayout.Y_AXIS));

        lblNombre = new JLabel(animal.getNombre(), SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblNombre.setForeground(recursos.Color.INK);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblRaza = new JLabel(animal.getEspecie() + " · " + animal.getStringSexo(), SwingConstants.CENTER);
        lblRaza.setFont(new Font("Segoe UI", Font.BOLD, 12)); 
        lblRaza.setForeground(Color.decode(animal.getColorFinHex())); 
        lblRaza.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelInfoBasica.add(lblNombre);
        panelInfoBasica.add(Box.createVerticalStrut(4));
        panelInfoBasica.add(lblRaza);

        panelDatosGrid = new JPanel(new GridLayout(4, 1, 0, 6));
        panelDatosGrid.setOpaque(false);
        panelDatosGrid.setBorder(new EmptyBorder(10, 5, 5, 5));

        actualizarGrillaValoresFicha();

        JPanel panelContenedorSuperiorMascota = new JPanel(new BorderLayout(0, 6));
        panelContenedorSuperiorMascota.setOpaque(false);
        panelContenedorSuperiorMascota.add(panelAvatarWrapper, BorderLayout.NORTH);
        panelContenedorSuperiorMascota.add(panelInfoBasica, BorderLayout.CENTER);

        cardPaciente.add(panelContenedorSuperiorMascota, BorderLayout.NORTH);
        cardPaciente.add(panelDatosGrid, BorderLayout.SOUTH);

        // Owner Card - Completamente compactada y sin título superior
        // Owner Card - Ajustada con el toquecito justo de aire vertical y el borde azul completo
        JPanel cardDueno = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo blanco de la tarjeta
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                
                // =========================================================================
                // CORRECCIÓN: El degradado azul ahora ocupa el 100% del ancho original (8px de alto)
                // =========================================================================
                Color blueInicio = new Color(14, 165, 233); 
                Color blueFin = new Color(37, 99, 235);    
                g2.setPaint(new GradientPaint(0, 0, blueInicio, getWidth(), 0, blueFin));
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                g2.fillRect(0, 0, getWidth(), 8); // Recuperamos los 8px de grosor original
                
                g2.setClip(null);
                g2.setColor(recursos.Color.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        cardDueno.setOpaque(false);
        
        // AJUSTE DE PADDING: Le damos 12px arriba/abajo para que respire el contenido
        cardDueno.setBorder(new EmptyBorder(12, 12, 12, 12));

        // DIMENSIONES FINALES BALACEADAS: Pasamos el alto de 64 a 72 para el tamaño justo
        Dimension dimensionesDueno = new Dimension(260, 72); 
        cardDueno.setPreferredSize(dimensionesDueno);
        cardDueno.setMinimumSize(dimensionesDueno);
        cardDueno.setMaximumSize(dimensionesDueno);

        Responsable responsable = animal.getResponsable();

        if (responsable != null) {
            JPanel panelContenedorInterno = new JPanel(new BorderLayout(12, 0));
            panelContenedorInterno.setOpaque(false);
            panelContenedorInterno.setBorder(new EmptyBorder(8, 0, 4, 0));
            // ==========================================
            // 1. AVATAR CUADRADO COMPACTO (Lado Izquierdo)
            // ==========================================
            String iniciales = "";
            if (!responsable.getNombre().isBlank()) iniciales += responsable.getNombre().toUpperCase().charAt(0);
            if (!responsable.getApellido().isBlank()) iniciales += responsable.getApellido().toUpperCase().charAt(0);
            
            final String textoIniciales = iniciales;

            JPanel panelAvatarDueno = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color colorInicio = new Color(14, 165, 233); 
                    Color colorFin = new Color(37, 99, 235);    
                    g2.setPaint(new GradientPaint(0, 0, colorInicio, 0, getHeight(), colorFin));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16); // Bordes redondeados perfectos
                    g2.dispose();
                }
            };
            panelAvatarDueno.setOpaque(false);
            
            // Achicamos las dimensiones del avatar a un cuadrado perfecto de 42x42 para que no se estire
            panelAvatarDueno.setPreferredSize(new Dimension(42, 42));
            panelAvatarDueno.setMinimumSize(new Dimension(42, 42));
            panelAvatarDueno.setMaximumSize(new Dimension(42, 42));

            JLabel lblIniciales = new JLabel(textoIniciales);
            lblIniciales.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblIniciales.setForeground(Color.WHITE);
            panelAvatarDueno.add(lblIniciales);

            // ==========================================
            // 2. TEXTOS: NOMBRE Y TELÉFONO (Centro)
            // ==========================================
            JPanel panelTextos = new JPanel(new GridLayout(2, 1, 0, 1));
            panelTextos.setOpaque(false);

            JLabel lblNombreDueno = new JLabel(responsable.getNombre() + " " + responsable.getApellido());
            lblNombreDueno.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Modificado a 14 para balancear con el tamaño de la tarjeta
            lblNombreDueno.setForeground(recursos.Color.INK);

            JPanel filaCelular = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            filaCelular.setOpaque(false);
            
            JLabel lblIconoCel = new JLabel();
            ImageIcon iconoCelRaw = cargarIconoHD("imagenes/emojis/celular.png", 14, 14);
            if (iconoCelRaw != null) {
                lblIconoCel.setIcon(iconoCelRaw);
            } else {
                lblIconoCel.setText("📱");
                lblIconoCel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
            }
            
            JLabel lblTextoCel = new JLabel(responsable.getCelular());
            lblTextoCel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblTextoCel.setForeground(recursos.Color.CAT_INACTIVO);
            
            filaCelular.add(lblIconoCel);
            filaCelular.add(lblTextoCel);

            panelTextos.add(lblNombreDueno);
            panelTextos.add(filaCelular);

            // ==========================================
            // 3. INDICADOR / FLECHITA DE DETALLE (Derecha)
            // ==========================================
            JLabel lblFlecha = new JLabel("›");
            lblFlecha.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblFlecha.setForeground(recursos.Color.BORDER);
            lblFlecha.setBorder(new EmptyBorder(0, 0, 0, 2));

            // Ensamblamos todo adentro del contenedor
            panelContenedorInterno.add(panelAvatarDueno, BorderLayout.WEST);
            panelContenedorInterno.add(panelTextos, BorderLayout.CENTER);
            panelContenedorInterno.add(lblFlecha, BorderLayout.EAST);

            cardDueno.add(panelContenedorInterno, BorderLayout.CENTER);
            
        } else {
            JPanel panelVacio = new JPanel(new GridBagLayout());
            panelVacio.setOpaque(false);
            JLabel lblMensajeVacio = new JLabel("Sin responsable asignado");
            lblMensajeVacio.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblMensajeVacio.setForeground(recursos.Color.CAT_INACTIVO); 
            panelVacio.add(lblMensajeVacio);
            cardDueno.add(panelVacio, BorderLayout.CENTER);
        }
        panelIzquierdo.add(cardPaciente);
        panelIzquierdo.add(Box.createVerticalStrut(15));
        panelIzquierdo.add(cardDueno);
        panelIzquierdo.add(Box.createVerticalGlue());

        // ========================================================
        // COLUMNA DERECHA: GRILLA DE TURNOS
        // ========================================================
        JPanel panelDerecho = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                
                Color violetaFigma = new Color(139, 92, 246); 
                Color azulFigma = recursos.Color.ACCENT_BLUE;
                GradientPaint degradadoSuperior = new GradientPaint(0, 0, violetaFigma, getWidth(), 0, azulFigma);
                g2.setPaint(degradadoSuperior);
                
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                g2.fillRect(0, 0, getWidth(), 8);
                g2.setClip(null);
                g2.setColor(recursos.Color.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        panelDerecho.setOpaque(false);
        panelDerecho.setBorder(new EmptyBorder(18, 15, 12, 15)); 

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setOpaque(false);
        
        // Removes uglyness of default tabs
        tabs.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {}
            @Override protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {}
            @Override protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            }
        });
        
        List<Turno> todosLosTurnos = controlador.getVeterinaria().getListaTurnos();
        List<Turno> turnosHistorial = new ArrayList<>();
        List<Turno> turnosPendientes = new ArrayList<>();

        // Populates appointments list
        for (Turno t : todosLosTurnos) {
            if (t.getAnimal() != null && t.getAnimal().getIdAnimal().equals(animal.getIdAnimal())) {
                if (t.estaCompletado()) {
                    turnosHistorial.add(t);
                } else if (t.esPendiente()) {
                    turnosPendientes.add(t);
                }
            }
        }

        // Tab 1: HISTORIAL
        JPanel panelHistorial = new JPanel();
        panelHistorial.setBackground(Color.WHITE);
        panelHistorial.setLayout(new BoxLayout(panelHistorial, BoxLayout.Y_AXIS));
        
        JPanel contenedorHistorialInmovil = new JPanel(new BorderLayout());
        contenedorHistorialInmovil.setBackground(Color.WHITE);
        contenedorHistorialInmovil.add(panelHistorial, BorderLayout.NORTH); 
        
        if (turnosHistorial.isEmpty()) {
            panelHistorial.setBorder(new EmptyBorder(30, 10, 10, 10));
            JLabel lblVacio = new JLabel("No hay registros médicos completados.", SwingConstants.CENTER);
            lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lblVacio.setForeground(recursos.Color.CAT_INACTIVO);
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelHistorial.add(lblVacio);
        } else {
            panelHistorial.setBorder(new EmptyBorder(15, 5, 15, 5));
            for (int i = 0; i < turnosHistorial.size(); i++) {
                Turno t = turnosHistorial.get(i);
                panelHistorial.add(crearFilaTurnoDinamica(t));
                if (i < turnosHistorial.size() - 1) {
                    panelHistorial.add(Box.createVerticalStrut(14));
                }
            }
        }

        // Tab 2: VACUNAS
        JPanel panelVacunas = new JPanel();
        panelVacunas.setBackground(Color.WHITE);
        panelVacunas.setLayout(new BoxLayout(panelVacunas, BoxLayout.Y_AXIS));

        JPanel contenedorVacunasInmovil = new JPanel(new BorderLayout());
        contenedorVacunasInmovil.setBackground(Color.WHITE);
        contenedorVacunasInmovil.add(panelVacunas, BorderLayout.NORTH);

        List<modelo.RegistroVacunacion> vacunasAplicadas = animal.getHistorial().getRegistroVacunas();

        if (vacunasAplicadas.isEmpty()) {
            panelVacunas.setBorder(new EmptyBorder(30, 10, 10, 10));
            JLabel lblVacunasVacio = new JLabel("No hay registro de vacunas aplicadas.", SwingConstants.CENTER);
            lblVacunasVacio.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lblVacunasVacio.setForeground(recursos.Color.CAT_INACTIVO);
            lblVacunasVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelVacunas.add(lblVacunasVacio);
        } else {
            panelVacunas.setBorder(new EmptyBorder(15, 5, 15, 5));
            for (int i = 0; i < vacunasAplicadas.size(); i++) {
                modelo.RegistroVacunacion reg = vacunasAplicadas.get(i);
                panelVacunas.add(crearFilaVacunaDinamica(reg));
                
                if (i < vacunasAplicadas.size() - 1) {
                    panelVacunas.add(Box.createVerticalStrut(14));
                }
            }
        }

        // Pestaña 3: TURNOS
        JPanel panelTurnosFuturos = new JPanel();
        panelTurnosFuturos.setBackground(Color.WHITE);
        panelTurnosFuturos.setLayout(new BoxLayout(panelTurnosFuturos, BoxLayout.Y_AXIS));

        JPanel contenedorTurnosInmovil = new JPanel(new BorderLayout());
        contenedorTurnosInmovil.setBackground(Color.WHITE);
        contenedorTurnosInmovil.add(panelTurnosFuturos, BorderLayout.NORTH); 

        if (turnosPendientes.isEmpty()) {
            panelTurnosFuturos.setBorder(new EmptyBorder(30, 10, 10, 10));
            JLabel lblVacio = new JLabel("No hay turnos próximos agendados.", SwingConstants.CENTER);
            lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lblVacio.setForeground(recursos.Color.CAT_INACTIVO);
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelTurnosFuturos.add(lblVacio);
        } else {
            panelTurnosFuturos.setBorder(new EmptyBorder(15, 5, 15, 5));
            for (int i = 0; i < turnosPendientes.size(); i++) {
                Turno t = turnosPendientes.get(i);
                panelTurnosFuturos.add(crearFilaTurnoDinamica(t));
                if (i < turnosPendientes.size() - 1) {
                    panelTurnosFuturos.add(Box.createVerticalStrut(14));
                }
            }
        }

        tabs.addTab("Historial", contenedorHistorialInmovil);
        tabs.addTab("Vacunas", contenedorVacunasInmovil);
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
        barraVerticalGlobal.setUI(new vista.componentes.ModernScrollBarUI()); 
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
                        
                    } else if (mouseEncima) {
                        setForeground(new Color(109, 40, 217)); 
                        g2.setColor(recursos.Color.BG); 
                        g2.fillRoundRect(0, 2, getWidth(), getHeight() - 4, 12, 12);
                    } else {
                        setForeground(recursos.Color.CAT_INACTIVO); 
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
                
                g2.setColor(recursos.Color.BORDER);
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
        lblTitulo.setForeground(recursos.Color.INK);
        
        String nombreVet = (t.getVeterinario() != null) ? "Dr. " + t.getVeterinario().getApellido() : "Sin asignar";
        JLabel lblMedico = new JLabel(nombreVet + "  •  " + t.getHora() + " hs");
        lblMedico.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMedico.setForeground(recursos.Color.CAT_INACTIVO);
        
        panelTextosLabels.add(lblTitulo);
        panelTextosLabels.add(lblMedico);

        panelIzquierdoInfo.add(lblEmojiTurno);
        panelIzquierdoInfo.add(panelTextosLabels);

        JLabel lblFecha = new JLabel(t.getFecha(), SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(recursos.Color.BG); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFecha.setForeground(recursos.Color.CAT_INACTIVO);
        lblFecha.setBorder(new EmptyBorder(4, 10, 4, 10)); 

        panelLineaSuperior.add(panelIzquierdoInfo, BorderLayout.WEST);
        panelLineaSuperior.add(lblFecha, BorderLayout.EAST);

        fila.add(panelLineaSuperior, BorderLayout.NORTH);

        String obs = t.getObservaciones();
        if (obs != null && !obs.isBlank()) {
            JPanel panelTextBoxObs = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(recursos.Color.CANVAS_GENERAL); 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.dispose();
                }
            };
            panelTextBoxObs.setOpaque(false);
            panelTextBoxObs.setBorder(new EmptyBorder(8, 12, 8, 12)); 

            JLabel lblNotas = new JLabel(obs);
            lblNotas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblNotas.setForeground(new Color(100, 116, 139)); 
            
            ImageIcon iconoComentario = cargarIconoHD("imagenes/emojis/comentario.png", 16, 16);
            if (iconoComentario != null) {
                lblNotas.setIcon(iconoComentario);
                lblNotas.setIconTextGap(8);
            } else {
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

    private JPanel crearFilaVacunaDinamica(modelo.RegistroVacunacion reg) {
        JPanel fila = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(recursos.Color.BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        fila.setOpaque(false);
        fila.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel panelLineaSuperior = new JPanel(new BorderLayout());
        panelLineaSuperior.setOpaque(false);

        JPanel panelIzquierdoInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelIzquierdoInfo.setOpaque(false);

        JLabel lblEmojiVacuna = new JLabel();
        ImageIcon iconoHD = cargarIconoHD("imagenes/emojis/jeringa.png", 20, 20); // Intentamos cargar tu PNG premium
        if (iconoHD != null) {
            lblEmojiVacuna.setIcon(iconoHD);
        } else {
            lblEmojiVacuna.setText("💉");
            lblEmojiVacuna.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        }

        JPanel panelTextosLabels = new JPanel(new GridLayout(2, 1, 0, 1));
        panelTextosLabels.setOpaque(false);
        
        JLabel lblTitulo = new JLabel(reg.getVacunaAplicada().getNombreMedicamento());
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(recursos.Color.INK);
        
        String detalles = reg.getTipoDosis() + "  •  Vence: " + reg.getFechaVencimiento().toString();
        JLabel lblDetalles = new JLabel(detalles);
        lblDetalles.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        if (reg.estaVencida()) {
            lblDetalles.setForeground(new Color(220, 38, 38)); // Rojo carmín vibrante
        } else {
            lblDetalles.setForeground(recursos.Color.CAT_INACTIVO);
        }
        
        panelTextosLabels.add(lblTitulo);
        panelTextosLabels.add(lblDetalles);

        panelIzquierdoInfo.add(lblEmojiVacuna);
        panelIzquierdoInfo.add(panelTextosLabels);

        JLabel lblFecha = new JLabel(reg.getFechaAplicacion().toString(), SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(reg.estaVencida() ? new Color(254, 226, 226) : new Color(241, 245, 249)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblFecha.setForeground(reg.estaVencida() ? new Color(220, 38, 38) : recursos.Color.CAT_INACTIVO);
        lblFecha.setBorder(new EmptyBorder(4, 10, 4, 10)); 

        panelLineaSuperior.add(panelIzquierdoInfo, BorderLayout.WEST);
        panelLineaSuperior.add(lblFecha, BorderLayout.EAST);

        fila.add(panelLineaSuperior, BorderLayout.NORTH);

        String obs = reg.getObservaciones();
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

            JLabel lblNotas = new JLabel(obs);
            lblNotas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblNotas.setForeground(new Color(100, 116, 139)); 
            
            ImageIcon iconoComentario = cargarIconoHD("imagenes/emojis/comentario.png", 16, 16);
            if (iconoComentario != null) {
                lblNotas.setIcon(iconoComentario);
                lblNotas.setIconTextGap(8);
            } else {
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
                g2.setColor(recursos.Color.CANVAS_GENERAL); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        fila.setOpaque(false);
        fila.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel lblClave = new JLabel(clave);
        lblClave.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblClave.setForeground(recursos.Color.CAT_INACTIVO);
        
        JLabel lblValor = new JLabel(valor, SwingConstants.RIGHT);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        lblValor.setForeground(recursos.Color.INK); 

        fila.add(lblClave, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);
        panel.add(fila);
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
            return null;
        }
    }

    private JButton crearBtnEditarFicha() {
        JButton btn = new JButton("Editar Mascota") {
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

        // =========================================================================
        // ESTILADO DEL TEXTO INTERNO
        // Modificamos las dimensiones: ahora mide 130px de ancho para que entre el texto completo
        // =========================================================================
        btn.setPreferredSize(new Dimension(130, 42)); 
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE); // Letra blanca obligatoria siempre
        
        // La lógica del clic para abrir el modal de edición se mantiene exactamente igual
        btn.addActionListener(e -> {
            JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(this);
            DialogoEditarPaciente modal = new DialogoEditarPaciente(ventanaPadre, animal);
            modal.setVisible(true);
            
            this.removeAll();
            initHeader();
            initCuerpo();
            
            this.revalidate();
            this.repaint();
        });
        
        return btn;
    }

    private void actualizarGrillaValoresFicha() {
        panelDatosGrid.removeAll(); 
        
        agregarFilaDatosFicha(panelDatosGrid, "Edad", animal.calcularEdad() + " años");
        agregarFilaDatosFicha(panelDatosGrid, "Peso", animal.getPeso() + " kg");
        agregarFilaDatosFicha(panelDatosGrid, "Estado", animal.getStringEstado());
        agregarFilaDatosFicha(panelDatosGrid, "Raza", animal.getRaza());

        panelDatosGrid.revalidate();
        panelDatosGrid.repaint();
    }
}