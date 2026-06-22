# Contexto del proyecto — Happy Paws (Sistema de Gestión Veterinaria)

> Documento vivo. Centraliza las definiciones operativas, el catálogo de funcionalidades real y el alcance del sistema de escritorio Happy Paws.
> Diseñado para alinear el desarrollo del modelo de dominio, la interfaz gráfica y la generación de contenido académico con datos de negocio verosímiles.
> Última actualización: Junio de 2026.

## 1. El Cliente y el Modelo de Negocio

- **Profesionales a cargo:** El Grupo POO 2026 (Fabiola Chazarreta, Juan Carlos Fernandez, Alejandro Gallo, Juan Marengo), actuando como equipo de desarrollo para la materia Programación Orientada a Objetos.
- **Cliente simulado:** "Happy Paws" — una veterinaria modelo del Área Metropolitana de Buenos Aires, con 4 veterinarios en plantilla, atención de lunes a sábados y un volumen estimado de 15-20 turnos diarios entre consultas generales, vacunación y cirugías menores.
- **Propósito del sistema:** Actuar como el sistema de gestión único de la clínica. Reemplaza las planillas de papel, los archivos sueltos de Excel y la memoria del veterinario, centralizando toda la información clínica y administrativa en una aplicación de escritorio rápida, confiable y portable.
- **Canal Estrella de Conversión (operativa):** El flujo Inicio → Citas → Atender → Recetar → Comprobante. El éxito operativo se mide por la cantidad de turnos completados exitosamente con su documentación asociada (receta, comprobante) en una jornada laboral.

## 2. Identidad Visual y Assets del Repositorio

> Isologotipo: El repositorio incluye dos versiones del logotipo: `recursos/logo.ai` (vectorial editable en Adobe Illustrator) y `imagenes/Logo.svg` (vectorial web). Adicionalmente, la aplicación utiliza un identificador tipográfico "Happy Paws" con la fuente Google Sans en tono teal (#0D9488) como marca visual secundaria, visible en la barra de título de la ventana principal y en los comprobantes emitidos.
> Iconografía PNG: El sistema utiliza pictogramas almacenados en `imagenes/emojis/` (17 archivos PNG) para comunicar estados visualmente: `exito.png` ✓ para completado, `carpeta.png` 📄 para comprobante, `calendario.png` para turnos, `perro.png`/`gato.png` según especie, `reloj_arena.png` para pendientes, `estetoscopio.png` para consultas, `patitas.png` para el portal de adopción, y `vet.png`/`casa.png`/`manito.png` para otros indicadores de la UI.

### Benchmarks Visuales de Referencia

Las siguientes imágenes de referencia conceptual se encuentran en `recursos/`. Son capturas de pantalla del sistema en desarrollo que sirven como guía visual para la implementación de cada sección:

- **`recursos/portal_inicio.png`:** (Referencia conceptual) Estructura del dashboard principal con saludo al veterinario logueado, tarjetas de estadísticas superiores (atendidos, turnos, pendientes) y acceso rápido a acciones.
- **`recursos/portal_calendario.png`:** (Referencia conceptual) Vista de la agenda de turnos con navegación lateral, lista de turnos del día con filtro visual por estado y botón flotante "+ Nuevo turno".
- **`recursos/portal_detalle_paciente.png`:** (Referencia conceptual) Diseño de las tarjetas de paciente con avatar de la mascota, nombre, especie, datos clínicos y botones de acción (Completar, Comprobante, Cancelar).
- **`recursos/portal_nuevo_turno.png`:** (Referencia conceptual) Diálogo modal de alta de turno con combos de veterinario, paciente y tipo de turno, más campos de fecha y hora.
- **`recursos/portal_pacientes.png`:** (Referencia conceptual) Vista del listado de pacientes con buscador en tiempo real y tarjetas de resumen clínico.

## 3. Catálogo Real de Funcionalidades (Arquitectura de Información)

> El catálogo de 10 funcionalidades se organiza en el menú lateral del sistema ordenado de **mayor a menor frecuencia de uso diario** para optimizar los movimientos del veterinario. A su vez, cada funcionalidad está categorizada bajo uno de los 4 grupos funcionales del menú:

1. **Inicio — Dashboard** (Inicio / Teal): Resumen del día. Saludo al veterinario, tarjetas de estadísticas dinámicas (atendidos, turnos totales, pendientes), accesos rápidos y un vistazo a los próximos turnos del día. (La pantalla más vista, primer destino tras el login).
2. **Citas — Agenda de Turnos** (Citas / Azul): Lista completa de turnos del día con filtro visual por estado. Tarjetas con datos del paciente, hora, tipo y botones de acción (Completar, Comprobante). Botón "+ Nuevo turno" para alta rápida. (Segunda pantalla más usada, corazón operativo).
3. **Atender Turno — Flujo de Consulta** (Citas / Azul): Modal que se abre al presionar "Completar". Permite registrar medicación recetada, aplicar vacunas y cerrar el turno. (Frecuencia media, usado 15-20 veces al día).
4. **Recetar Medicamento** (Citas / Azul): Diálogo de selección de producto del catálogo de la veterinaria con confirmación. Queda registrado en la historia clínica del animal.
5. **Portal de Adopción** (Portal de Adopción / Verde): Lista de animales marcados como "en adopción". Botón toggle para activar/desactivar. (Frecuencia baja pero visibilidad alta, valor emocional para la clínica).
6. **Notas Internas** (Notas / Amarillo): Bloc de notas rápido para el equipo veterinario. Cada nota se timbra automáticamente con autor y fecha/hora. (Frecuencia variable, útil en clínicas con múltiples veterinarios).
7. **Historial Clínico** (Inicio o Citas / Teal): Acceso al historial completo de un animal desde la tarjeta de turno. Muestra medicamentos recetados, vacunas aplicadas con fecha y control de vencimiento.
8. **Emisión de Comprobante** (Citas / Azul): Generación del `ComprobanteTurno` en formato texto con datos del paciente, responsable, veterinario, diagnóstico y medicación. Exportable a archivo de texto desde el diálogo.
9. **Búsqueda de Pacientes** (Inicio / Teal): Filtro por nombre de mascota desde la vista de inicio. Resultados en tiempo real.
10. **Configuración y Cierre de Sesión** (Más / Gris): Pantalla de configuración mínima (nombre de la veterinaria, datos de contacto para comprobantes) y botón de cierre de sesión que retorna al login.

## 4. El Framework de Confianza ("El Flujo del Turno")

> Para garantizar que ningún dato clínico se pierda y que el veterinario pueda demostrar trazabilidad en cada atención, el sistema implementa un proceso de punta a punta de 4 etapas:

- **Paso 1 — Registro del Paciente:** El cliente (responsable) se da de alta en el sistema junto con su mascota. Cada animal queda vinculado a su dueño y se crea automáticamente una historia clínica vacía lista para recibir registros.  
- **Paso 2 — Asignación de Turno:** El veterinario agenda una cita seleccionando fecha, hora, paciente, profesional y tipo de atención. El turno nace en estado "pendiente" y aparece en el dashboard del veterinario asignado.  
- **Paso 3 — Atención y Prescripción:** Durante la consulta, el veterinario registra los medicamentos recetados y las vacunas aplicadas directamente en la historia clínica del animal. El sistema verifica automáticamente si hay vacunas vencidas en el historial y las señala para auditoría.  
- **Paso 4 — Comprobante y Cierre:** Al completar el turno, el sistema genera un comprobante detallado que el veterinario puede revisar, imprimir o exportar. El turno pasa a estado "completado" y actualiza las estadísticas del dashboard en tiempo real.

## 5. Qué NO va en el sistema (Fuera de Alcance Inicial)

- **Persistencia en Base de Datos:** El sistema no guarda datos en disco de forma permanente. Los datos se siembran al iniciar la aplicación (seed data) y se pierden al cerrarla. La persistencia (JSON, SQLite, H2) está planteada como trabajo futuro y no forma parte del alcance de la entrega académica.

- **Módulo de Facturación y Cobros:** Happy Paws no emite facturas, no registra pagos ni integra ninguna pasarela de cobro (Mercado Pago, tarjetas, efectivo). El comprobante emitido es un resumen clínico-administrativo, no un comprobante fiscal.

- **Interfaz Web o Mobile:** Es una aplicación de escritorio pura (Java Swing). No hay versión web, API REST, ni app móvil. No hay planes de migración dentro del alcance académico.

- **Multi-sucursal / Multi-clínica:** El sistema modela una única veterinaria. No soporta múltiples sucursales, ni transferencia de historiales entre clínicas, ni consolidación de datos de varias sedes.

- **Envío de Comprobantes por Email/WhatsApp:** El comprobante se visualiza en pantalla y puede exportarse a archivo de texto, pero no se envía automáticamente por correo electrónico ni mensajería. El veterinario debe compartirlo manualmente.

## 6. Decisiones Operativas y Stack Técnico

- **Arquitectura:** MVC puro con separación física en paquetes (`modelo/`, `vista/`, `controlador/`). El modelo desconoce completamente la UI; la vista opera exclusivamente a través del controlador Singleton. Esta separación permite probar el modelo sin la UI y, eventualmente, reemplazar Swing por JavaFX o una interfaz web conservando el mismo modelo de dominio.

- **Estrategia de Datos:** Los datos de demostración (4 veterinarios, 4 clientes, 10 mascotas, 16 turnos, 8 medicamentos, notas) viven en `ControladorVeterinaria.cargarDatosDemo()`. La clase `Veterinaria` mantiene listas en memoria (`ArrayList`) de todas las entidades. No hay capa de persistencia. El método `Veterinaria.reiniciar()` permite resetear el estado global para las pruebas.

- **Tono del Copy:** Profesional, ágil y en español rioplatense. Los mensajes de la UI tutean al veterinario usuario con respeto ("Completá el turno", "Agregá una nota", "¿Recetar medicamento?"). Los comprobantes usan un tono institucional ("Comprobante — Happy Paws", "Responsable:", "Veterinario actuante:").

## 7. Estructura de Secciones de la Aplicación (Layout Unificado)

- **Login (DialogoLogin):** Diálogo modal con campo de matrícula profesional, botón "Ingresar" y mensaje de error para credenciales inválidas. Al confirmar, abre la ventana principal (PortalVeterinario).
- **Navbar Lateral (Panel Lateral):** Logo "Happy Paws" en teal (superior izq.) + 5 ítems de navegación vertical con iconos + Badge del veterinario logueado (nombre y matrícula) al pie.
- **Header Superior:** Saludo personalizado ("¡Hola, Dr. Apellido!") con contador de turnos del día + fecha actual formateada + tarjetas de estadísticas dinámicas (Atendidos / Mis Turnos / Pendientes).
- **Dashboard de Inicio (Inicio):**
  - **Fila de Stats:** 3 tarjetas con número grande y etiqueta (Atendidos, Mis Turnos, Pendientes). Los valores se calculan en vivo con `contarTurnosDelVeterinario()`.
  - **Acceso Rápido:** Botones para nuevo turno y ver citas de hoy.
  - **Mini contadores:** Versión compacta de las estadísticas en la parte inferior.
- **Sección Citas:**
  - **Lista de turnos:** Grilla vertical de tarjetas con foto/icono del animal, nombre, especie, hora, tipo de turno y estado.
  - **Botones de acción por tarjeta:** "✓ Completar" (abre flujo de atención), "📄 Comprobante" (genera comprobante), "❌ Cancelar".
  - **Botón flotante:** "+ Nuevo turno" que abre `DialogoNuevoTurno`.
- **Sección Portal de Adopción:**
  - **Lista de animales en adopción:** Tarjetas con foto, nombre, especie y botón "✓ En adopción" toggle.
  - **Acción:** Marcar/desmarcar animales del portal.
- **Sección Notas:**
  - **Lista vertical (LIFO):** Las notas más recientes arriba. Cada nota muestra el texto completo con timestamp y autor.
  - **Campo de texto + botón "Agregar":** Para crear nuevas notas. Validación de contenido no vacío.
- **Sección Más (Configuración):**
  - **Datos de la veterinaria:** Nombre del negocio (editable), datos de contacto.
  - **Exportar comprobante:** Botón para exportar el último comprobante generado.
  - **Cerrar sesión:** Botón que cierra la sesión y retorna al login.
- **Diálogos Modales:**
  - **DialogoNuevoTurno:** Formulario con combos de veterinario (con renderer nombre + especialidad + matrícula), paciente (con renderer nombre + especie + responsable), tipo de turno (con descripción legible), fecha y hora.
  - **DialogoRecetarMedicamento:** Combo de medicamentos del catálogo (con código y nombre), selector de tipo (medicamento/vacuna), botón confirmar.
  - **DialogoComprobante:** Vista previa del texto del comprobante generado por `ComprobanteTurno.generarTextoCompleto()`. Botón "Cerrar" y "Exportar a archivo".
- **Footer / Barra de Estado:** Institución académica (Tecnicatura Universitaria en Desarrollo de Software), materia (Programación Orientada a Objetos), año (2026).

## 8. Operación del Sistema

- **Stack:** **Java 11+ con Swing** (sin frameworks externos, sin Maven/Gradle, compilación manual con `javac` desde PowerShell).
- **Distribución:** Código fuente compilado a `.class` en carpeta `build/`. Distribución como carpeta de clases (no JAR empaquetado por simplicidad académica).
- **Sincronización de Datos:** No aplica. Sin conexión a red ni a base de datos externa. Todos los datos residen en memoria durante la sesión.
- **Soporte Offline:** 100% offline por definición. Aplicación de escritorio Java sin dependencias de red.
- **Pruebas:** Suite integrada en `Demo.java` con 62 aserciones que validan login, polimorfismo, prescripciones, adopciones, notas y búsquedas. Ejecución: `java -cp build Demo`.
- **¿Quién lo va a mantener/actualizar?** El Grupo POO 2026 hasta la fecha de entrega (22 de julio de 2026). No hay plan de mantenimiento posterior por tratarse de un trabajo académico.
- **Idioma/tono:** español rioplatense, tuteo (definido). Mensajes de UI, comprobantes, nombres de métodos y comentarios en español (salvo palabras clave del lenguaje Java y nombres de patrones).
