# **Design System: Happy Paws — Sistema de Gestión Veterinaria**

```yaml
name: Happy Paws — Sistema de Gestión Veterinaria
description: Sistema de diseño y especificaciones visuales optimizado para el Portal Clínico Veterinario (Web/PWA).
colors:
  # Identidad Principal
  primary: "#0D9488"         # Verde teal vibrante — Botones principales, barra de filtros activos y acentos de marca
  primary-light: "#E2F0EE"   # Teal pastel — Fondos de badges de estado (ej: "Activo")
  
  # Sistema Anti-Fatiga (Canvas & Superficies)
  bg: "#F1F5F9"              # Slate claro grisáceo — Fondo general de la aplicación (Canvas)
  surface: "#FFFFFF"         # Blanco puro — Tarjetas contenedoras, modales y formularios
  border-subtle: "#E2E8F0"   # Gris sutil — Bordes internos de inputs y contenedores menores
  divider: "#CBD5E1"         # Slate-300 — Divisores y líneas sutiles
  inactive-text: "#BCCCDC"   # Slate-200 — Textos deshabilitados o cancelados
  
  # Tipografía y Contraste
  ink: "#1E293B"             # Slate oscuro — Títulos principales, nombres de pacientes y texto de alto contraste
  muted: "#64748B"           # Gris medio — Subtítulos, descripciones secundarias y placeholders
  
  # Paleta de Categorías Clínicas (Bordes Superiores de Tarjetas e Iconos)
  cat-cirugia: "#FF6B00"     # Naranja — Cirugía (Bordes en Hulk)
  cat-consulta: "#3B82F6"    # Azul — Consulta General (Bordes en Luna)
  cat-analisis: "#A855F7"    # Morado — Análisis Clínicos (Bordes en Hulk - Turno 2)
  cat-vacuna: "#EC4899"      # Rosa/Fucsia — Vacunación (Bordes en Mochi)
  cat-seguimiento: "#EAB308" # Amarillo/Dorado — Seguimiento Clínico (Bordes en Rocky/Thor)
  cat-control: "#F97316"     # Naranja Alerta — Controles o advertencias en badges
  cat-inactivo: "#94A3B8"    # Gris — Pacientes o estados inactivos (Kira)
  
  # Alertas y Notificaciones Directas
  badge-alert: "#EF4444"     # Rojo — Círculos de notificación/atención urgente en avatares
  
  # Avatares Específicos
  avatar-dog: "#FEBA64"      # Naranja pastel — Fondo de avatar para perros
  avatar-cat: "#7DD3FC"      # Azul claro — Fondo de avatar para gatos

  # Variantes Claras para Badges y Estados
  red-light: "#FEE2E2"       # Rojo suave — Fondos de badges de peligro o cancelación
  blue-light: "#DBEAFE"      # Azul suave — Fondos de badges de consulta general
  blue-dark: "#2563EB"       # Azul fuerte — Texto de badges de consulta general
  purple-light: "#F3E8FF"    # Morado suave — Fondos de badges de análisis clínicos
  purple-dark: "#9333EA"     # Morado fuerte — Texto de badges de análisis clínicos
  success-light: "#DCFCE7"   # Verde suave — Fondos de badges de éxito o vacunación

  # Tarjetas de Estadísticas (Stats)
  stat-hoy-bg: "#F0F9FF"     # Celeste suave — Fondo de tarjeta de hoy
  stat-hoy-txt: "#0E7490"    # Celeste oscuro — Texto de tarjeta de hoy
  stat-pend-bg: "#FEF3C7"    # Ámbar suave — Fondo de tarjeta de pendientes
  stat-pend-txt: "#B45309"   # Ámbar oscuro — Texto de tarjeta de pendientes
  stat-real-bg: "#ECFDF5"    # Esmeralda suave — Fondo de tarjeta de realizados
  stat-real-txt: "#047857"   # Esmeralda oscuro — Texto de tarjeta de realizados

typography:
  main:
    fontFamily: "Inter, system-ui, -apple-system, sans-serif"
    weights:
      regular: 400
      medium: 500
      semibold: 600
      bold: 700
  sizes:
    h1: "1.75rem (28px)"     # Saludos principales ("¡Hola, Dr. Páez!")
    h2: "1.25rem (20px)"     # Títulos de contenedores y secciones internas
    body-bold: "0.9375rem (15px) Bold" # Nombres de pacientes en tarjetas
    body: "0.875rem (14px)"  # Datos de lectura, campos de texto y descripciones
    caption: "0.75rem (12px)" # Badges, horas de turnos y metadatos secundarios

rounded:
  sm: "6px"                  # Checkboxes, selectores mini
  md: "12px"                 # Inputs de formularios y botones secundarios
  lg: "20px"                 # Tarjetas de pacientes, bloques de contenido del Home y modales
  pill: "999px"              # Badges de estado y filtros de categorías
```

## 1. Overview & Creative North Star

- **Creative North Star: "Fichas clínicas que respiran a través del color"**

El diseño del ecosistema visual de Happy Paws abandona las interfaces densas y grises de la gestión veterinaria tradicional para abrazar una experiencia limpia, intuitiva y sumamente visual. La interfaz se construye a partir de contenedores blancos redondeados (surface) que flotan sobre un fondo gris slate clínico (bg). El peso y la velocidad de escaneo se resuelven mediante el uso estratégico de acentos cromáticos en los bordes superiores de los módulos. Un veterinario puede identificar instantáneamente qué pacientes del día van a cirugía, a consulta o a vacunación simplemente reconociendo el código de color del marco, sin necesidad de leer el texto detallado.

**Key Characteristics:**

- **El teal #0D9488 como vector de marca y control:** Utilizado para botones de acción principal globales (+ Nuevo turno), botones conmutadores (Switches de recordatorios) y para resaltar los estados y filtros que se encuentran activos en las vistas superiores.

- **Codificación cromática por categoría clínica:** En lugar de colores fijos de éxito o error, la interfaz distribuye el peso visual a través de una paleta por tipo de consulta (Naranja para Cirugía, Azul para Consulta, Morado para Análisis, Rosa para Vacunación, Dorado para Seguimiento). Estos colores tiñen dinámicamente los bordes temáticos de las tarjetas y sus avatares.

- **El slate oscuro #1E293B para una legibilidad premium:** Reemplaza al negro puro para dar contraste a los nombres de los pacientes, títulos de secciones y métricas destacadas, logrando una lectura descansada durante jornadas clínicas prolongadas.

- **Estructura flotante de esquinas redondeadas y suaves sombras:** Las tarjetas blancas (surface) abandonan las esquinas rígidas y adoptan un radio de curvatura amplio y orgánico de 20px (rounded.lg), asentándose sobre el canvas gris claro (bg) mediante sombras sutiles y difusas que generan una clara sensación de profundidad y capas visuales.

- **Bordes superiores con degradados y acentos sólidos:** Los bloques principales de la interfaz (como los paneles del Home y las fichas de pacientes) incorporan atractivos degradados lineales en sus barras de cabecera o bordes superiores gruesos (de 4px), sirviendo como un ancla visual inmediata que sectoriza la información.

- **Alertas contextuales directas sobre los avatares:** Puntos de notificación de color rojo vibrante (#EF4444) se posicionan de manera flotante sobre los avatares redondeados de los animales para señalizar de un solo vistazo expedientes que requieren atención urgente (ej: vacunas vencidas o controles pendientes).

## 2. Colors

| Token / Categoría | Hex | Uso Principal   |
| :---- | :---- | :---- |
| Primary | \#0D9488 | Botones principales, acentos de marca, items activos |
| Primary Light | \#E2F0EE | Fondos de badges de estado positivo (ej: "Activo") |
| Background (Canvas) | \#F1F5F9 | Fondo general de la aplicación |
| Surface (Tarjetas) | \#FFFFFF | Tarjetas contenedoras, modales y formularios |
| Ink (Texto principal) | \#1E293B | Títulos principales, nombres de pacientes |
| Muted (Texto secundario) | \#64748B | Subtítulos, descripciones, placeholders |
| Cat: Cirugía | \#FF6B00 | Bordes y acentos para turnos de cirugía |
| Cat: Consulta | \#3B82F6 | Bordes y acentos para consultas generales |
| Badge Alert | \#EF4444 | Puntos de notificación de urgencia |
| Divider | \#CBD5E1 | Divisores de scroll y líneas divisorias sutiles |
| Inactive Text | \#BCCCDC | Textos deshabilitados o turnos cancelados |
| Avatar Dog | \#FEBA64 | Color de fondo de avatares caninos |
| Avatar Cat | \#7DD3FC | Color de fondo de avatares felinos |
| Red Light | \#FEE2E2 | Fondos de badges de alerta/error/cancelación |
| Blue Light | \#DBEAFE | Fondos de badges de consulta general |
| Blue Dark | \#2563EB | Texto de badges de consulta general |
| Purple Light | \#F3E8FF | Fondos de badges de análisis clínicos |
| Purple Dark | \#9333EA | Texto de badges de análisis clínicos |
| Success Light | \#DCFCE7 | Fondos de badges de éxito o vacunación |
| Stat Hoy BG | \#F0F9FF | Fondo de tarjeta estadística de hoy |
| Stat Hoy TXT | \#0E7490 | Texto de tarjeta estadística de hoy |
| Stat Pend BG | \#FEF3C7 | Fondo de tarjeta estadística de pendientes |
| Stat Pend TXT | \#B45309 | Texto de tarjeta estadística de pendientes |
| Stat Real BG | \#ECFDF5 | Fondo de tarjeta estadística de realizados |
| Stat Real TXT | \#047857 | Texto de tarjeta estadística de realizados |

### Primary

- **Teal Happy Paws** (`#0D9488`): El color insignia de la marca. Se utiliza para el logo tipográfico en el navbar lateral, los botones de acción principal (Completar turno, Agregar nota, + Nuevo turno) y los títulos del dashboard. Es un teal de saturación media-alta que comunica salud, frescura y confianza sin caer en el verde fluorescente.
- **Teal Profundo** (`#0B6C63`): Variante oscura para estados hover de botones principales y para el badge de sesión activa del veterinario. Aporta peso en la interacción.
- **Teal Claro** (`#E2F0EE`): Fondo de contenedores secundarios y resaltado del ítem de navegación activo en el panel lateral. Funciona como un acento de baja intensidad que no compite con el contenido.

### Accent

- **Azul Citas** (`#3B82F6`): Color funcional de la sección de turnos. Se aplica como color del icono "Citas" en la navegación, como borde de las tarjetas de turno en estado pendiente y como color de los botones "Ver Citas".
- **Azul Claro** (`#E8F0FE`): Fondo de las tarjetas de turno para dar contexto visual a la pantalla más usada del sistema.

### State Colors

- **Verde Éxito** (`#16A34A`): Estado "completado" en badges, número de atendidos en las estadísticas del dashboard y en el texto de confirmación de un turno finalizado.
- **Verde Suave** (`#DCFCE7`): Fondo de badges de éxito o vacunación.
- **Ámbar Pendiente** (`#D97706`): Estado "pendiente" en tarjetas de turno, contador de pendientes en las estadísticas y alertas suaves (ej: vacuna próxima a vencer).
- **Rojo Error** (`#DC2626`): Estado "cancelado", mensajes de error de validación en formularios y botones de acción destructiva como "Cancelar turno".
- **Rojo Suave** (`#FEE2E2`): Fondos de badges de alerta o cancelación.

### Neutral

- **Canvas General** (`#F8FAFC`): Fondo de la ventana principal. Un blanco con un levísimo tinte slate que evita el contraste agresivo del blanco puro en pantallas de escritorio con luz natural.
- **Superficie** (`#FFFFFF`): Blanco puro para tarjetas de turno, paneles de formularios y diálogos modales. Permite que los elementos floten nítidamente sobre el canvas.
- **Tinta Corporativa** (`#1E293B`): Texto principal del sistema. Reemplaza al negro absoluto (#000000) con un contraste premium y más descansado para lectura prolongada.
- **Gris Secundario** (`#64748B`): Texto de descripciones, placeholders en campos de búsqueda, etiquetas de fecha/hora y pies de tarjeta.
- **Borde** (`#E2E8F0`): Líneas divisorias delgadas (1px), contornos de tarjeta en reposo y bordes de campos de formulario no enfocados.
- **Divisor Sutil** (`#CBD5E1`): Divisores de scroll y líneas divisorias de scroll bars.
- **Texto Inactivo** (`#BCCCDC`): Textos deshabilitados, cancelados u opacados en tarjetas inactivas.

### Avatars & Stats Cards

- **Avatar Perro** (`#FEBA64`): Tono naranja pastel para el fondo de avatares de mascotas caninas.
- **Avatar Gato** (`#7DD3FC`): Tono azul cielo pastel para el fondo de avatares de mascotas felinas.
- **Tarjeta Hoy** (Fondo `#F0F9FF`, Texto `#0E7490`): Colores celestes específicos de contraste para la tarjeta estadística "Hoy".
- **Tarjeta Pendientes** (Fondo `#FEF3C7`, Texto `#B45309`): Colores ámbar específicos de contraste para la tarjeta estadística "Pendientes".
- **Tarjeta Realizados** (Fondo `#ECFDF5`, Texto `#047857`): Colores esmeralda específicos de contraste para la tarjeta estadística "Realizados".

## 3. Typography

**Display, Headline, Title & Body Font:** Google Sans (fallback: Inter, system-ui, sans-serif)

**Label/Mono Font:** Consolas (fallback: monospace)

**Character:** Google Sans es una tipografía geométrica limpia con curvas amables que combina legibilidad en pantalla con un aspecto moderno pero no informal. Se utiliza en toda la interfaz — desde los titulares del dashboard hasta los datos clínicos — para mantener una experiencia visual coherente. Consolas se reserva para los comprobantes exportados (`ComprobanteTurno.generarTextoCompleto()`) y para cualquier representación de código o identificadores del sistema (ej: códigos de medicamentos como "SEN-001").

### Java Hierarchy (aplicación Swing)

En Swing se usa `CargadorFuentes.cargar(float size, int style)` que carga Google Sans desde el classpath o cae en `SansSerif` del sistema si falla.

- **Display** (`font-size: 24f, Font.BOLD`): El gran saludo del dashboard ("¡Hola, Dr. Apellido!") y el título de la ventana de comprobante.
- **Headline** (`font-size: 16f, Font.BOLD`): Títulos de las secciones del menú lateral ("Inicio", "Citas", "Portal de Adopción") y encabezados de diálogos modales ("Nuevo Turno", "Recetar Medicamento").
- **Title** (`font-size: 14f, Font.BOLD`): Nombre del paciente en la tarjeta de turno, nombre del medicamento en el catálogo, etiquetas de formulario.
- **Body** (`font-size: 12f, Font.PLAIN`): Texto general del sistema: descripciones de turnos, contenido de notas, resultados de búsqueda, información de configuración.
- **Label** (`font-size: 10f, Font.PLAIN, Consolas`): Códigos de medicamentos, datos de la barra de estado (institución, materia), comprobantes exportados.

## 4. Shadows & Elevation

El sistema apuesta por un enfoque **plano pero con jerarquía sutil**. En Java Swing no existen las sombras CSS nativas, pero se simula elevación mediante:

- **Bordes de 1px en reposo**: Las tarjetas (`card-turno`, `card-stats`) se distinguen del fondo por su borde `#E2E8F0` de 1px, sin sombra, manteniendo una estética limpia.

- **Diferenciación por fondo**: Los paneles de contenido principal usan `surface` (#FFFFFF) sobre `bg` (#F8FAFC). Los diálogos modales agregan un `LineBorder` de 1px en `border` para destacar sobre el fondo con un velo semitransparente (opacidad de la ventana padre reducida vía `setOpacity(0.5f)` o simulado con un `JPanel` de fondo oscuro sobrepuesto).

- **Enfoque Activo / Interacción**: Los botones y campos de formulario cambian su fondo y borde al recibir foco:
  - Botón primario hover: fondo `primary-deep` con transición visual inmediata (Swing `setBackground()` directo, sin easing al ser síncrono).
  - Input focus: borde de 2px en `primary` en vez de 1px en `border`.
  - Tarjeta de turno hover: borde pasa de `border` a `primary-light`, y el cursor cambia a `hand` (`Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)`).

### Shadow Vocabulary (Equivalente conceptual en Swing)

| Estado | Swing Token | Comportamiento |
|---|---|---|
| **Reposo Inactivo** | `1px solid #E2E8F0`, sin sombra | Tarjetas y paneles se asientan sobre el canvas con un contorno mínimo |
| **Enfoque Activo / Hover** | Borde engrosado a `2px #0D9488`, fondo superficial sin cambio | Campos de texto, combos y botones reciben un anillo de foco teal que indica interactividad sin depender de sombras |
| **Diálogo Modal** | `1px solid #E2E8F0` + velo semitransparente de fondo | El diálogo se sobrepone con un panel oscuro (`Color(0,0,0,0.35)`) que oscurece la ventana padre, enfocando la atención |

## 5. Components

### Buttons

- **Primary CTA (Completar turno, Agregar nota, + Nuevo turno):** Fondo Teal Happy Paws (`#0D9488`), texto blanco en Google Sans 12f, padding vertical 10px + horizontal 20px, esquinas redondeadas a 8px (`rounded.md`), cursor hand. Al hacer hover, fondo Teal Profundo (`#0B6C63`). Se instancia como `JButton` con `setBackground(Color)` y `setForeground(Color.WHITE)`.
- **Secondary CTA (Ver requisitos, Cancelar):** Fondo blanco (`#FFFFFF`), texto Ink (`#1E293B`) en Google Sans 12f, borde de 1px `#E2E8F0`, esquinas redondeadas a 8px. Al hacer hover, borde pasa a `#0D9488`. Se usa para acciones complementarias.
- **Ghost / Link Button (Cerrar sesión, Ver historial):** Sin fondo ni borde, texto Teal (`#0D9488`) subrayado solo en hover. Se usa para acciones secundarias dentro de tarjetas o paneles densos.
- **Destructive Button (Cancelar turno, Eliminar nota):** Fondo rojo error (`#DC2626`) solo en hover, texto rojo con fondo blanco en reposo. Esquinas redondeadas a 8px. Se usa exclusivamente para acciones que no se pueden deshacer.

### Turn Cards (Tarjetas de Turno en sección Citas)

- **Estructura:** `JPanel` con `BoxLayout` vertical o `GridBagLayout` de una fila. Fondo blanco (`#FFFFFF`), borde de 1px `#E2E8F0`, esquinas a 12px (`rounded.lg`).
- **Cabecera:** Icono/avatar del animal (cargado desde `imagenes/emojis/` por especie) + nombre del paciente en Google Sans 14f bold + badge de estado coloreado (verde para completado, ámbar para pendiente, rojo para cancelado).
- **Cuerpo:** Hora del turno (Google Sans 14f, color `muted`), tipo de turno (`TipoTurno.getDescripcion()` en body 12f) y nombre del responsable.
- **Acciones:** Botones de acción alineados a la derecha: "✓ Completar" (primary), "📄 Comprobante" (secondary si completado, ghost si pendiente), "❌ Cancelar" (destructive).
- **Comportamiento hover:** Borde cambia a Teal Claro (`#E2F0EE`), cursor a hand, transición inmediata.

### Stats Cards (Tarjetas de Estadísticas en Dashboard)

- **Estructura:** `JPanel` con contenido centrado. Fondo blanco (`#FFFFFF`), sin borde, esquinas a 12px (`rounded.lg`).
- **Contenido:** Número grande en Google Sans 24f bold (color de la card: verde éxito, teal, ámbar respectivamente), etiqueta debajo en body 12f color `muted`, icono PNG a la izquierda.
- **Tres variantes:** "Atendidos" (verde éxito), "Mis Turnos" (teal), "Pendientes" (ámbar). Los números se actualizan dinámicamente vía `contarTurnosDelVeterinario()`.

### Step Cards (Pasos del Flujo — implícito en el flujo de atención)

- **Layout:** Filas horizontales expansibles. El borde lateral izquierdo tiene un grosor de 4 a 6px y actúa como identificador visual veloz del tipo de atención médica.  
- **Jerarquía Visual:** Hora del turno a la izquierda extrema, seguida por el avatar de la mascota, el nombre del paciente, badge de categoría en el centro y, finalmente, datos del propietario a la derecha.
- **Estructura:** Bloques secuenciales en el modal de atención (Completar turno). Fondo blanco con borde teal de 1px intermitente (dashed, simulado con `LineBorder` personalizado) para indicar el paso actual.
- **Números de paso:** Círculo con fondo teal y número blanco en Google Sans 14f bold a la izquierda, título del paso y descripción a la derecha.
- **Flujo:** 1. Revisar paciente → 2. Recetar medicación → 3. Aplicar vacunas → 4. Generar comprobante.

### Search Bar (Barra de búsqueda de pacientes y trámites)

- **Estructura:** `JTextField` con icono de lupa a la izquierda (PNG 16x16 desde `imagenes/emojis/`). Fondo blanco, borde de 1px `#E2E8F0`, esquinas a 12px (`rounded.lg`), padding 10px 14px.
- **Comportamiento:** Al enfocar, borde cambia a 2px Teal (`#0D9488`). El texto de placeholder ("Buscá un paciente por nombre...") usa color `muted` y desaparece al escribir. Cada `keyTyped` dispara un filtro en `buscarMascotasPorNombre()`.
- **Botón de borrado:** Cruz ("✕") que aparece a la derecha cuando hay texto, que al presionarse limpia el campo y restaura la lista completa.

### Form Inputs (Campos de formulario en diálogos)

- **Text Fields:** `JTextField` con fondo blanco, borde 1px `#E2E8F0`, esquinas 8px (`rounded.md`), padding 10px 14px, fuente body 12f. Focus: borde 2px teal.
- **Combo Boxes:** `JComboBox<T>` con renderer personalizado (`ListCellRenderer`) que muestra texto descriptivo en vez de `toString()`. Borde y focus igual que text fields. Altura mínima 32px.
- **Labels:** Etiquetas sobre los campos en `title` 14f bold, color `ink`. Separación vertical de 8px entre label y campo.
- **Validation:** Los campos inválidos (fecha mal formateada, campo vacío requerido) muestran borde rojo (`#DC2626`) y un mensaje de error debajo en `label` 10f color rojo. No se permite submit hasta que todos los campos requeridos sean válidos.
- **Selectores Visuales Abiertos:** En lugar de menús desplegables tradicionales, se emplean botones grandes e independientes con iconos y etiquetas de texto para elegir el "Tipo de Turno" de un solo clic.  
- **Prioridad:** Selector segmentado de 3 botones (Normal, Media, Alta) con indicadores circulares de color incorporados.  
- **Switches:** Para activar/desactivar comportamientos (como recordatorios automáticos) se usan toggles deslizables con el color primary cuando están activos.

### Dialog Modals (Diálogos de acción)

- **Estructura:** `JDialog` con `setModal(true)`, `setResizable(false)`. Fondo blanco, título en headline 16f bold, padding de 24px.
- **Barra de título:** Título del diálogo en Google Sans 16f bold, color `ink`, alineado a la izquierda. Sin barra de título del sistema operativo (se reemplaza por un panel Swing personalizado).
- **Botones de acción:** Primario (confirmar) a la derecha, Secundario (cancelar) a su izquierda. Separados por 12px. En Windows SE, el orden sigue el estándar (Confirmar → Cancelar).
- **Animación de apertura:** Apertura inmediata (Swing no soporta transiciones fluidas sin bibliotecas externas). Se usa `setLocationRelativeTo(parent)` para centrar sobre la ventana padre.

### Navigation (Panel Lateral)

- **Style:** `JPanel` fijo a la izquierda (180px de ancho), fondo blanco, con los 5 ítems de navegación en vertical. Logo "Happy Paws" en teal en la parte superior (Google Sans 18f bold).
- **Ítems de navegación:** Botones con icono PNG (16x16), texto en sidebar (body 12f), 40px de altura cada uno. Color de texto en reposo: `muted`. Al estar activo: fondo Teal Claro (`#E2F0EE`), texto Teal (`#0D9488`), icono teal.
- **Badge de usuario al pie:** Nombre y matrícula del veterinario logueado en body 12f, color `muted`, separado por un divisor `border` de 1px. No tiene acción de click.

### Footer / Barra de Estado

- **Estructura:** Tira horizontal de 28px de altura al fondo de la ventana principal. Fondo `bg` (#F8FAFC), borde superior de 1px en `border`.
- **Contenido:** Texto centrado en label 10f, color `muted`: "Tecnicatura Universitaria en Desarrollo de Software | Programación Orientada a Objetos | 2026".
- **Comportamiento:** Siempre visible, no interactivo. No scroll.

## 6. Named Rules

**La Regla del Teal Controlado:** El teal (`#0D9488`) no debe usarse para cuerpos de texto largos ni fondos de tarjetas de contenido. Su uso queda restringido a: botones primarios, logo, títulos del dashboard, indicador de sección activa en el menú lateral y enlaces interactivos. En cualquier otro contexto debe usarse una de sus variantes claras u oscuras según la función (hover, fondo, badge). La saturación del teal cansa la vista si se extiende sobre más del 15% de la superficie visible de la ventana.

**La Regla de Accesibilidad de Contraste:** Queda prohibido colocar texto blanco sobre el fondo Teal Claro (`#E2F0EE`) o cualquier variante pastel. Cualquier tipografía sobre fondos de baja saturación debe usar Ink (`#1E293B`) como color de texto para garantizar WCAG AA (relación de contraste mínima 7:1). Excepción: texto blanco sobre teal profundo (`#0D9488` o `#0B6C63`) que mantiene un contraste de 4.8:1, aceptable para titulares.

**La Regla de la Línea Cómoda:** Los textos informativos (descripciones de turnos, contenido de notas, historial clínico) no deben superar los 80 caracteres por línea dentro de una tarjeta. En Swing esto se controla con `JTextArea` con `setLineWrap(true)` y `setWrapStyleWord(true)` en lugar de `JLabel` para textos que excedan las 3 líneas. El objetivo es que el veterinario lea sin mover la cabeza horizontalmente.

**La Regla de la Tarjeta Útil:** Una tarjeta de turno no debe contener más de 6 elementos de información directa (nombre, especie, hora, tipo, responsable, estado). Si se requiere información adicional (historial completo, recetas anteriores), debe ir dentro de un modal al que se accede mediante un botón "Ver más" o "Historial". Esto evita que las tarjetas se conviertan en fichas densas que el ojo saltea.

**La Regla del Borde de Categoría:** Ninguna tarjeta de paciente o turno puede prescindir de su franja o borde acentuado. Este color es el primer nivel de lectura diagnóstica para el profesional veterinario y debe corresponder estrictamente al tipo de atención asignado.

**La Regla de las Esquinas Amables:** Toda tarjeta contenedora debe respetar de forma obligatoria el radio de curvatura de 20px. Los radios menores (12px) quedan reservados exclusivamente para componentes interactivos de control internos.

**La Regla del Indicador de Urgencia:** El punto de notificación rojo se colocará de manera exclusiva sobre los avatares para señalar alertas críticas (como planes sanitarios vencidos). Jamás debe emplearse de forma puramente estética.  

**Sombreado Dinámico:** Las tarjetas no deben poseer bordes grises duros en su contorno completo. La separación del fondo se logra mediante un difuminado sutil (drop-shadow perimetral) que añade profundidad sin ensuciar la visual.

**La Regla de la Centralización Visual (Evitar Hardcode):** Queda estrictamente prohibido hardcodear instancias de colores (`new Color(...)`) o fuentes dentro del código de los paneles, diálogos o componentes visuales. Toda utilización de la paleta de colores del sistema de diseño debe realizarse a través de la clase [Color.java](file:///c:/Users/el_be/OneDrive/Desktop/Universidad/desarrollo%20de%20software/Tercer%20cuatrimestre/Programacion%20Orientada%20a%20Objetos/poo_2026/recursos/Color.java) del paquete `recursos` (por ejemplo, `recursos.Color.PRIMARY`). Asimismo, todas las tipografías deben cargarse, gestionarse y derivarse a través de [CargadorFuentes.java](file:///c:/Users/el_be/OneDrive/Desktop/Universidad/desarrollo%20de%20software/Tercer%20cuatrimestre/Programacion%20Orientada%20a%20Objetos/poo_2026/recursos/CargadorFuentes.java). Esto garantiza la coherencia estética global de Happy Paws y simplifica el mantenimiento a largo plazo.
