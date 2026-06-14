package vista.paneles;

import controlador.ControladorVeterinaria;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelo.*;
import recursos.CargadorFuentes;
import recursos.Color;
import recursos.ImageLoader;
import vista.componentes.ModernScrollBarUI;

public class PanelCitas extends JPanel {

    private final ControladorVeterinaria controlador;
    private final Color colorFondoGris = Color.BG;
    private final Color colorTeal = Color.PRIMARY;

    private final JPanel panelListaGrouped;
    private final JLabel lblStatHoyVal;
    private final JLabel lblStatPendVal;
    private final JLabel lblStatRealVal;
    private final JLabel lblCountTurnos;

    private String filtroTipo = "Todos";

    private final List<String> tiposDeFiltro = List.of("Todos", "Consulta", "Vacuna", "Análisis", "Cirugía", "Seguimiento", "Pasados", "Cancelados");
    private final JPanel panelFiltrosGrid;

    public PanelCitas(ControladorVeterinaria controlador) {
        this.controlador = controlador;

        setLayout(new BorderLayout(0, 15));
        setBackground(colorFondoGris);
        setBorder(new EmptyBorder(15, 25, 15, 25));

        // ----------------- 1. HEADER -----------------
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        // Header Izquierdo (Logo + Títulos)
        JPanel panelHeaderIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelHeaderIzq.setOpaque(false);

        JPanel panelLogo = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(colorTeal);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        panelLogo.setPreferredSize(new Dimension(36, 36));
        panelLogo.setOpaque(false);
        JLabel lblLogoIcon = new JLabel("🐾");
        lblLogoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        ImageIcon icon = ImageLoader.loadScaled("imagenes/logo.png", 36, 36);
        if (icon.getImage() != null && icon.getIconWidth() > 0) {
            lblLogoIcon.setIcon(icon);
            lblLogoIcon.setText("");
        }

        panelLogo.add(lblLogoIcon);
        panelHeaderIzq.add(panelLogo);

        JPanel panelTitulos = new JPanel();
        panelTitulos.setOpaque(false);
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Mis Turnos");
        lblTitulo.setFont(CargadorFuentes.cargar(18f).deriveFont(Font.BOLD));
        lblTitulo.setForeground(Color.INK);

        Veterinario vetLogueado = controlador.getVeterinarioLogueado();
        String vetStr = (vetLogueado != null)
                ? "Dr/a. " + vetLogueado.getNombre() + " " + vetLogueado.getApellido() + " · " + vetLogueado.getMatricula()
                : "Veterinario no identificado";
        JLabel lblSubtitle = new JLabel(vetStr);
        lblSubtitle.setFont(CargadorFuentes.cargar(11f));
        lblSubtitle.setForeground(Color.MUTED);

        panelTitulos.add(lblTitulo);
        panelTitulos.add(lblSubtitle);
        panelHeaderIzq.add(panelTitulos);

        panelHeader.add(panelHeaderIzq, BorderLayout.WEST);

        // Header Derecho (+ Nuevo turno)
        JButton btnNuevoTurno = new JButton("+ Nuevo turno") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnNuevoTurno.setBackground(colorTeal);
        btnNuevoTurno.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        btnNuevoTurno.setForeground(Color.SURFACE);
        btnNuevoTurno.setContentAreaFilled(false);
        btnNuevoTurno.setBorderPainted(false);
        btnNuevoTurno.setFocusPainted(false);
        btnNuevoTurno.setPreferredSize(new Dimension(130, 36));
        btnNuevoTurno.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNuevoTurno.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnNuevoTurno.setBackground(recursos.Color.PRIMARY_DEEP);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnNuevoTurno.setBackground(colorTeal);
            }
        });
        btnNuevoTurno.addActionListener(e -> {
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(PanelCitas.this);
            vista.dialogos.DialogoNuevoTurno d = new vista.dialogos.DialogoNuevoTurno(parent, controlador);
            d.setVisible(true);
            actualizar();
        });
        panelHeader.add(btnNuevoTurno, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

        // ----------------- 2. BODY CONTENT (STATS + FILTERS + LIST) -----------------
        JPanel panelBody = new JPanel(new BorderLayout(0, 15));
        panelBody.setOpaque(false);

        // Stats Row + Filters Row Container
        JPanel panelControles = new JPanel();
        panelControles.setOpaque(false);
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));

        // --- STATS ROW ---
        JPanel panelStats = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelStats.setOpaque(false);
        panelStats.setBackground(Color.SURFACE);
        panelStats.setBorder(new EmptyBorder(0, 0, 5, 0));

        panelStats.add(crearCardEstadistica("Hoy", Color.STAT_HOY_BG, Color.STAT_HOY_TXT, "imagenes/emojis/calendario.png", "", lblStatHoyVal = new JLabel("0")));
        panelStats.add(crearCardEstadistica("Pendientes", Color.STAT_PEND_BG, Color.STAT_PEND_TXT, "imagenes/emojis/reloj_arena.png", "⏳", lblStatPendVal = new JLabel("0")));
        panelStats.add(crearCardEstadistica("Realizados", Color.STAT_REAL_BG, Color.STAT_REAL_TXT, "imagenes/emojis/exito.png", "✅", lblStatRealVal = new JLabel("0")));

        panelControles.add(panelStats);
        panelControles.add(Box.createVerticalStrut(12));

        // --- FILTERS ROW ---
        JPanel panelFiltros = new JPanel(new BorderLayout());
        panelFiltros.setOpaque(false);
        panelFiltros.setBorder(new EmptyBorder(0, 0, 0, 12));

        panelFiltrosGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2)) {
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
        panelFiltrosGrid.setOpaque(false);
        panelFiltrosGrid.setBorder(new EmptyBorder(2, 6, 2, 6));
        panelFiltros.add(panelFiltrosGrid, BorderLayout.WEST);

        // turnos count panel
        JPanel panelFiltrosDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelFiltrosDer.setOpaque(false);

        lblCountTurnos = new JLabel("0 turnos");
        lblCountTurnos.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        lblCountTurnos.setForeground(Color.MUTED);
        lblCountTurnos.setBorder(new EmptyBorder(4, 8, 0, 0));
        panelFiltrosDer.add(lblCountTurnos);

        panelFiltros.add(panelFiltrosDer, BorderLayout.EAST);

        panelControles.add(panelFiltros);
        panelBody.add(panelControles, BorderLayout.NORTH);

        // --- LIST GROUPED SCROLLABLE ---
        panelListaGrouped = new JPanel();
        panelListaGrouped.setBackground(Color.BG);
        panelListaGrouped.setLayout(new BoxLayout(panelListaGrouped, BoxLayout.Y_AXIS));

        JScrollPane scrollLista = new JScrollPane(panelListaGrouped);
        scrollLista.setBorder(null);
        scrollLista.setOpaque(false);
        scrollLista.getViewport().setOpaque(false);

        // Custom scrollbar
        JScrollBar bar = scrollLista.getVerticalScrollBar();
        bar.setUI(new ModernScrollBarUI());
        bar.setPreferredSize(new Dimension(8, 0));
        bar.setUnitIncrement(16);

        panelBody.add(scrollLista, BorderLayout.CENTER);
        add(panelBody, BorderLayout.CENTER);

        // Render initial filters
        refrescarFiltrosUI();

        // Update counts and list
        actualizarInterno();
    }

    public void actualizar() {
        actualizarInterno();
    }

    private void actualizarInterno() {
        // Recalcular estadísticas
        Veterinario vet = controlador.getVeterinarioLogueado();
        if (vet == null) {
            return;
        }

        // 1. Hoy (Carlos, no cancelados, fecha "06/06/2026")
        int hoyCount = 0;
        for (Turno t : controlador.getVeterinaria().getListaTurnos()) {
            if (vet.equals(t.getVeterinario())
                    && t.getFecha().equals("06/06/2026")
                    && !t.getEstado().equals(Turno.ESTADO_CANCELADO)) {
                hoyCount++;
            }
        }
        lblStatHoyVal.setText(String.valueOf(hoyCount));

        // 2. Pendientes en toda la clínica
        int pendientesCount = 0;
        for (Turno t : controlador.getVeterinaria().getListaTurnos()) {
            if (t.esPendiente()) {
                pendientesCount++;
            }
        }
        lblStatPendVal.setText(String.valueOf(pendientesCount));

        // 3. Realizados por Carlos
        int realizadosCount = 0;
        for (Turno t : controlador.getVeterinaria().getListaTurnos()) {
            if (vet.equals(t.getVeterinario()) && t.estaCompletado()) {
                realizadosCount++;
            }
        }
        lblStatRealVal.setText(String.valueOf(realizadosCount));

        refrescarGrilla();
    }

    private void refrescarFiltrosUI() {
        panelFiltrosGrid.removeAll();
        for (String f : tiposDeFiltro) {
            boolean sel = f.equals(filtroTipo);
            JButton btn = crearBotonFiltroPildora(f, sel);
            btn.addActionListener(e -> {
                filtroTipo = f;
                refrescarFiltrosUI();
                refrescarGrilla();
            });
            panelFiltrosGrid.add(btn);
        }
        panelFiltrosGrid.revalidate();
        panelFiltrosGrid.repaint();
    }

    private void refrescarGrilla() {
        panelListaGrouped.removeAll();
        panelListaGrouped.add(Box.createVerticalStrut(10));

        Veterinario vet = controlador.getVeterinarioLogueado();
        if (vet == null) {
            return;
        }

        // Filtrar y agrupar los turnos del veterinario
        List<Turno> turnosFiltrados = new ArrayList<>();
        for (Turno t : controlador.getVeterinaria().getListaTurnos()) {
            if (!t.getVeterinario().equals(vet)) {
                continue;
            }

            switch (filtroTipo) {
                case "Cancelados" -> {
                    if (!t.getEstado().equals(Turno.ESTADO_CANCELADO)) {
                        continue;
                    }
                }
                case "Pasados" -> {
                    if (!t.estaCompletado()) {
                        continue;
                    }
                }
                default -> {
                    if (!t.esPendiente()) {
                        continue;
                    }
                    if (!filtroTipo.equals("Todos")) {
                        if (filtroTipo.equals("Consulta") && t.getTipo() != TipoTurno.CONSULTA_GENERAL) {
                            continue;
                        }
                        if (filtroTipo.equals("Vacuna") && t.getTipo() != TipoTurno.VACUNACION) {
                            continue;
                        }
                        if (filtroTipo.equals("Análisis") && t.getTipo() != TipoTurno.ANALISIS) {
                            continue;
                        }
                        if (filtroTipo.equals("Cirugía") && t.getTipo() != TipoTurno.CIRUGIA) {
                            continue;
                        }
                        if (filtroTipo.equals("Seguimiento") && t.getTipo() != TipoTurno.SEGUIMIENTO) {
                            continue; // mapped as general
                        }
                    }
                }
            }

            turnosFiltrados.add(t);
        }

        // Ordenar cronológicamente: fecha y hora
        turnosFiltrados.sort((t1, t2) -> {
            int cmp = t1.getFechaParsed().compareTo(t2.getFechaParsed());
            if (cmp != 0) {
                return cmp;
            }
            return t1.getHoraParsed().compareTo(t2.getHoraParsed());
        });

        // Agrupar por fecha
        Map<String, List<Turno>> agrupados = new LinkedHashMap<>();
        for (Turno t : turnosFiltrados) {
            agrupados.computeIfAbsent(t.getFecha(), k -> new ArrayList<>()).add(t);
        }

        int totalVisibles = turnosFiltrados.size();
        lblCountTurnos.setText(totalVisibles + " turnos");

        if (totalVisibles == 0) {
            JPanel panelVacio = new JPanel(new GridBagLayout());
            panelVacio.setOpaque(false);
            panelVacio.setBorder(new EmptyBorder(40, 20, 40, 20));
            JLabel lblVacio = new JLabel("No hay turnos agendados con los filtros seleccionados");
            lblVacio.setFont(CargadorFuentes.cargar(13f));
            lblVacio.setForeground(Color.CAT_INACTIVO);
            panelVacio.add(lblVacio);
            panelListaGrouped.add(panelVacio);
        } else {
            for (Map.Entry<String, List<Turno>> entry : agrupados.entrySet()) {
                String fecha = entry.getKey();
                List<Turno> listaDeFecha = entry.getValue();

                // Fila Cabecera de Fecha
                panelListaGrouped.add(crearFilaCabeceraFecha(fecha, listaDeFecha.size()));
                panelListaGrouped.add(Box.createVerticalStrut(10));

                // Cartas de Turnos de esta fecha
                for (Turno t : listaDeFecha) {
                    panelListaGrouped.add(crearTarjetaTurnoCalendario(t));
                    panelListaGrouped.add(Box.createVerticalStrut(10));
                }
            }
        }

        panelListaGrouped.revalidate();
        panelListaGrouped.repaint();
    }

    private JPanel crearFilaCabeceraFecha(String fechaStr, int cant) {
        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        header.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Badge de Fecha
        String niceDate = formatNiceDate(fechaStr);
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(Color.PRIMARY); // Teal-600
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        badge.setOpaque(false);

        JLabel lblCal = new JLabel("📅");
        lblCal.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblCal.setForeground(Color.SURFACE);
        badge.add(lblCal);

        JLabel lblFechaText = new JLabel(niceDate);
        lblFechaText.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblFechaText.setForeground(Color.SURFACE);
        lblFechaText.setBorder(new EmptyBorder(0, 0, 1, 2));
        badge.add(lblFechaText);

        header.add(badge, BorderLayout.WEST);

        // Contador de turnos del día
        JLabel lblCant = new JLabel(cant + " turnos");
        lblCant.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        lblCant.setForeground(Color.CAT_INACTIVO);
        lblCant.setBorder(new EmptyBorder(6, 0, 0, 0));
        header.add(lblCant, BorderLayout.EAST);

        return header;
    }

    private JPanel crearTarjetaTurnoCalendario(Turno t) {
        boolean isCancelado = t.getEstado().equals(Turno.ESTADO_CANCELADO);
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                boolean hovered = Boolean.TRUE.equals(getClientProperty("hovered")) && !isCancelado;
                g2.setColor(hovered ? Color.CANVAS_GENERAL : Color.SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
                g2.setColor(hovered ? Color.PRIMARY : Color.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 36, 36);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        card.setPreferredSize(new Dimension(520, 64));
        card.setBorder(new EmptyBorder(8, 16, 8, 16));
        card.setCursor(isCancelado ? Cursor.getDefaultCursor() : Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // 1. HORA (West)
        String horaAMPM = convertToAMPM(t.getHora());
        JLabel lblHora = new JLabel(horaAMPM, SwingConstants.LEFT);
        lblHora.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.BOLD));
        lblHora.setForeground(Color.INK);
        lblHora.setPreferredSize(new Dimension(85, 48));

        gbc.gridx = 0;
        gbc.weightx = 0.0;
        card.add(lblHora, gbc);

        // 2. ACCENT BAR (Teal/Red/etc)
        java.awt.Color accentColor = t.getTipo().getAccentColor();

        JPanel panelAccent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        panelAccent.setPreferredSize(new Dimension(4, 36));
        panelAccent.setOpaque(false);

        JPanel accentWrapper = new JPanel(new GridBagLayout());
        accentWrapper.setOpaque(false);
        accentWrapper.setBorder(new EmptyBorder(0, 8, 0, 12));
        accentWrapper.add(panelAccent);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        card.add(accentWrapper, gbc);

        // 3. ANIMAL AVATAR
        java.awt.Color avatarBg = java.awt.Color.decode(t.getAnimal().getColorInicioHex());
        JPanel panelAvatar = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(avatarBg);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panelAvatar.setPreferredSize(new Dimension(48, 48));
        panelAvatar.setOpaque(false);

        JLabel lblAvatar = new JLabel();
        String path = t.getAnimal().getImagen();
        ImageIcon img = ImageLoader.loadScaled(path, 36, 36);
        lblAvatar.setIcon(img);
        panelAvatar.add(lblAvatar);

        JPanel avatarWrapper = new JPanel(new GridBagLayout());
        avatarWrapper.setOpaque(false);
        avatarWrapper.setBorder(new EmptyBorder(0, 0, 0, 12));
        avatarWrapper.add(panelAvatar);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        card.add(avatarWrapper, gbc);

        // 4. INFO TEXT BLOCK (Center)
        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));

        // Animal name + Tipo Badge Row
        JPanel panelRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelRow1.setOpaque(false);
        panelRow1.setMinimumSize(new Dimension(250, 20));
        panelRow1.setBorder(new EmptyBorder(6, 0, 0, 0));
        panelRow1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblName = new JLabel(t.getAnimal().getNombre());
        lblName.setFont(CargadorFuentes.cargar(13f).deriveFont(Font.BOLD));
        lblName.setForeground(Color.INK); // Slate-900
        panelRow1.add(lblName);

        JLabel lblDiv = new JLabel("—");
        lblDiv.setFont(CargadorFuentes.cargar(12f));
        lblDiv.setForeground(Color.DIVIDER);
        panelRow1.add(lblDiv);

        // Badge pill
        String pillText = t.getTipo().getDescripcion();
        java.awt.Color pBg = t.getTipo().getBadgeBgColor();
        java.awt.Color pFore = t.getTipo().getBadgeFgColor();

        JLabel lblPill = new JLabel(pillText) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(pBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblPill.setOpaque(false);
        lblPill.setFont(CargadorFuentes.cargar(9f).deriveFont(Font.BOLD));
        lblPill.setForeground(pFore);
        lblPill.setBorder(new EmptyBorder(2, 8, 2, 8));
        panelRow1.add(lblPill);

        if (isCancelado) {
            JLabel lblCanceladoPill = new JLabel("CANCELADO") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.RED_LIGHT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            lblCanceladoPill.setOpaque(false);
            lblCanceladoPill.setFont(CargadorFuentes.cargar(9f).deriveFont(Font.BOLD));
            lblCanceladoPill.setForeground(Color.ERROR);
            lblCanceladoPill.setBorder(new EmptyBorder(2, 8, 2, 8));
            panelRow1.add(lblCanceladoPill);
        }

        panelInfo.add(panelRow1);

        // Owner Row
        String ownerStr = t.getAnimal().getResponsable().getNombre() + " " + t.getAnimal().getResponsable().getApellido();
        JLabel lblOwner = new JLabel(ownerStr);
        lblOwner.setFont(CargadorFuentes.cargar(11f));
        lblOwner.setForeground(Color.MUTED);
        lblOwner.setBorder(new EmptyBorder(6, 6, 6, 0));
        lblOwner.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInfo.add(lblOwner);

        gbc.gridx = 3;
        gbc.weightx = 0.0;
        card.add(panelInfo, gbc);

        JPanel panelDescription = new JPanel();
        panelDescription.setOpaque(false);
        panelDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDescription.setBorder(new EmptyBorder(6, 12, 0, 12));

        JLabel lblTitleDescription = new JLabel("Nota:");
        lblTitleDescription.setFont(CargadorFuentes.cargar(13f).deriveFont(Font.BOLD));
        lblTitleDescription.setForeground(Color.INK); // Slate-900
        panelDescription.add(lblTitleDescription);
        // Description Row
        String descriptionStr = t.getObservaciones();
        JLabel lblDescription = new JLabel(descriptionStr);
        lblDescription.setFont(CargadorFuentes.cargar(11f));
        lblDescription.setForeground(Color.MUTED);
        lblDescription.setBorder(new EmptyBorder(6, 6, 6, 0));
        lblDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDescription.add(lblDescription);

        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        card.add(panelDescription, gbc);

        // 5. ACTION CHEVRON / CANCEL (East)
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        panelAcciones.setOpaque(false);

        // Cancel button (X) if pending
        if (t.esPendiente()) {
            JButton btnCancel = new JButton("Cancelar") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            btnCancel.setPreferredSize(new Dimension(60, 24));
            btnCancel.setBorder(new EmptyBorder(2, 0, 0, 0));
            btnCancel.setFont(CargadorFuentes.cargar(12f).deriveFont(Font.PLAIN));
            btnCancel.setBackground(Color.RED_LIGHT);
            btnCancel.setForeground(Color.ERROR);
            btnCancel.setContentAreaFilled(false);
            btnCancel.setBorderPainted(false);
            btnCancel.setFocusPainted(false);
            btnCancel.setToolTipText("Cancelar Turno");
            btnCancel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btnCancel.setBackground(Color.RED_LIGHT.darker());
                    btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor a mano
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btnCancel.setBackground(Color.RED_LIGHT);
                }
            });
            btnCancel.addActionListener(e -> {
                int res = JOptionPane.showConfirmDialog(PanelCitas.this,
                        "¿Estás seguro de que deseas cancelar el turno de " + t.getAnimal().getNombre() + "?",
                        "Cancelar Turno", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    t.cancelarTurno();
                    actualizar();
                }
            });
            panelAcciones.add(btnCancel);
        }

        // Chevron arrow
        // JLabel lblChevron = new JLabel(">");
        // lblChevron.setFont(new Font("Segoe UI", Font.BOLD, 14));
        // lblChevron.setForeground(new Color(203, 213, 225));
        // panelAcciones.add(lblChevron);
        gbc.gridx = 5;
        gbc.weightx = 1.0;
        card.add(panelAcciones, gbc);

        // Click actions
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isCancelado) {
                    return;
                }
                Frame parent = (Frame) SwingUtilities.getWindowAncestor(PanelCitas.this);
                if (t.esPendiente()) {
                    new vista.dialogos.DialogoAtenderTurno(parent, controlador, t).setVisible(true);
                    actualizar();
                } else if (t.estaCompletado()) {
                    modelo.ComprobanteTurno comp = new modelo.ComprobanteTurno(t, controlador.getVeterinaria().getNombreNegocio());
                    new vista.dialogos.DialogoComprobante(parent, comp).setVisible(true);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (isCancelado) {
                    return;
                }
                card.putClientProperty("hovered", true);
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isCancelado) {
                    return;
                }
                card.putClientProperty("hovered", false);
                card.repaint();
            }
        });

        // If cancelled or completed, style accordingly
        if (t.getEstado().equals(Turno.ESTADO_CANCELADO)) {
            lblName.setForeground(Color.CAT_INACTIVO);
            lblHora.setForeground(Color.CAT_INACTIVO);
            lblOwner.setForeground(Color.INACTIVE_TEXT);
            card.setEnabled(false);
        }

        return card;
    }

    private String convertToAMPM(String militaryTime) {
        try {
            String[] parts = militaryTime.split(":");
            int hour = Integer.parseInt(parts[0]);
            String min = parts[1];
            String suffix = (hour >= 12) ? "PM" : "AM";
            int displayHour = hour % 12;
            if (displayHour == 0) {
                displayHour = 12;
            }
            return String.format("%02d:%s %s", displayHour, min, suffix);
        } catch (NumberFormatException e) {
            return militaryTime;
        }
    }

    private String formatNiceDate(String dateStr) {
        try {
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(dateStr, parser);
            LocalDate todaySystem = LocalDate.of(2026, 6, 6); // Matches PortalVeterinario fixed today

            String formatted = date.getDayOfMonth() + " " + date.getMonth().getDisplayName(TextStyle.SHORT, java.util.Locale.forLanguageTag("es-AR")) + " " + date.getYear();
            // capitalize first letter of month
            String[] tokens = formatted.split(" ");
            if (tokens.length == 3) {
                String month = tokens[1];
                month = Character.toUpperCase(month.charAt(0)) + month.substring(1);
                formatted = tokens[0] + " " + month + " " + tokens[2];
            }

            if (date.equals(todaySystem)) {
                return "Hoy — " + formatted;
            } else {
                return formatted;
            }
        } catch (Exception e) {
            return dateStr;
        }
    }

    private JButton crearBotonFiltroPildora(String texto, boolean seleccionado) {
        JButton btn = new JButton(texto) {
            private boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        setForeground(seleccionado ? Color.SURFACE : Color.INK);
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        setForeground(seleccionado ? Color.SURFACE : Color.MUTED);
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (seleccionado) {
                    g2.setColor(hovered ? Color.PRIMARY_DEEP : colorTeal);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    setForeground(Color.SURFACE);
                } else {
                    if (hovered) {
                        g2.setColor(new java.awt.Color(241, 245, 249));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                        setForeground(Color.INK);
                    } else {
                        setForeground(Color.MUTED);
                    }
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(CargadorFuentes.cargar(11f).deriveFont(Font.BOLD));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel crearCardEstadistica(String label, Color bg, Color text, String iconPath, String unicodeIcon, JLabel lblVal) {
        JPanel card = new JPanel(new BorderLayout(8, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(Math.max(0, bg.getRed() - 15), Math.max(0, bg.getGreen() - 15), Math.max(0, bg.getBlue() - 15)));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(10, 14, 10, 14));
        card.setPreferredSize(new Dimension(120, 48));

        JLabel lblIcon = new JLabel();
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon icon = null;
        if (iconPath != null) {
            icon = ImageLoader.loadScaled(iconPath, 24, 24);
        }
        if (icon != null && icon.getImage() != null && icon.getIconWidth() > 0) {
            lblIcon.setIcon(icon);
        } else {
            lblIcon.setText(unicodeIcon);
            lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        }

        card.add(lblIcon, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        lblVal.setFont(CargadorFuentes.cargar(14f).deriveFont(Font.BOLD));
        lblVal.setForeground(text);

        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(CargadorFuentes.cargar(9f));
        lblLbl.setForeground(Color.MUTED);

        info.add(lblVal);
        info.add(lblLbl);
        card.add(info, BorderLayout.CENTER);

        return card;
    }
}
