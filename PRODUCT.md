# Product Definition: Happy Paws — Sistema de Gestión Veterinaria

## Register

**Brand:** Happy Paws  
**Desarrollado por:** Grupo POO 2026  
**Profesional a cargo (dominio):** Veterinaria modelo del Área Metropolitana de Buenos Aires (tipología de clínica pequeña a mediana, 2-4 veterinarios, atención general y vacunación).  
**Registro / Matrícula de referencia:** MP-9854, MP-1024, MP-2255, MP-3344 (veterinarios simulados para demostración del sistema).

---

## Users

**Audiencia primaria — Veterinarios y personal administrativo de clínicas veterinarias pequeñas o medianas.**  
Profesionales que necesitan registrar la atención diaria de mascotas, gestionar turnos, acceder al historial clínico y prescribir tratamientos. Trabajan en un entorno ruidoso y con interrupciones constantes (dueños que entran, animales inquietos, teléfono sonando). Valoran la velocidad de interacción, la claridad de la información clínica y la capacidad de emitir comprobantes al instante. No son expertos en tecnología: necesitan una interfaz que se entienda a simple vista, sin tutoriales ni manuales.

**Audiencia secundaria — Dueños de mascotas (como actores representados en el sistema, no usuarios directos del software).**  
Personas que llevan a sus animales a la clínica y reciben comprobantes impresos o digitales. No operan el sistema, pero son la razón de ser de todos los datos que contiene. Su experiencia depende indirectamente de cuán rápido y preciso el veterinario pueda usar el software.

**El job-to-be-done:**  
"Gestionar la atención diaria de una clínica veterinaria — desde que entra un paciente hasta que se emite el comprobante — sin perder datos clínicos, sin duplicar turnos y sin depender de papel o planillas sueltas."

---

## Product Purpose

Un sistema de escritorio Java/Swing diseñado para centralizar y agilizar la operatoria diaria de una veterinaria, estructurado sobre cuatro pilares funcionales:

1. **Gestión de Pacientes y Agenda:** Alta de clientes y mascotas con su historial clínico completo (vacunas, medicamentos, diagnósticos). Agenda de turnos por veterinario con control de estados (pendiente, completado, cancelado). El corazón operativo del sistema.

2. **Portal de Adopciones:** Módulo dedicado para visibilizar animales en adopción dentro de la misma aplicación. Permite marcar/desmarcar animales como disponibles y consultar la lista completa. Un diferenciador frente a sistemas veterinarios genéricos.

3. **Prescripción y Seguimiento Farmacológico:** Catálogo de medicamentos con carga de recetas directa a la historia clínica. Control de vencimiento de vacunas con alerta visual. Trazabilidad completa de todo lo recetado al animal.

4. **Reporte y Comprobación:** Emisión de comprobantes detallados por turno atendido con datos del paciente, responsable, veterinario actuante, diagnóstico y medicación prescripta. La clase `ComprobanteTurno` delega en las entidades del dominio sin duplicar datos, demostrando el patrón de diseño solicitado.

**El éxito se ve como:**  
Un veterinario inicia sesión, ve en el dashboard sus turnos del día con indicadores de pendientes y completados, agenda una nueva consulta en menos de 10 segundos, atiende al paciente registrando medicación en la historia clínica, completa el turno y entrega un comprobante profesional al dueño — todo sin cerrar ninguna ventana ni escribir a mano.

**Doble objetivo (Estrategia de Producto):**

1. **Para el veterinario/usuario:** Reducir la fricción administrativa diaria. El sistema debe ser más rápido que escribir en papel y más confiable que una planilla Excel compartida.

2. **Para la cátedra (evaluación académica):** Demostrar la aplicación explícita y verificable de los 7 requisitos del PDF de POO: herencia, encapsulamiento, polimorfismo, composición/agregación, colecciones con búsqueda, clase de reporte con delegación y sobrecarga de constructores. El código debe ser legible, la arquitectura MVC debe ser clara y las pruebas deben cubrir todos los flujos.

---

## Brand Personality

**Profesional, cálida y precisa** — Una identidad que equilibra la seriedad del cuidado de la salud animal con la calidez que espera un dueño responsable.

- **3 palabras clave:** Confiable, ágil, cercana.
- **Voz:** Profesional pero no fría. Utiliza el tuteo rioplatense para generar cercanía con el veterinario usuario ("Completá el turno y entregá el comprobante"), pero mantiene una estructura técnica impecable en los reportes y la documentación clínica.
- **Emoción objetivo:** Control ("Tengo todo registrado y accesible") y profesionalismo ("Mi clínica funciona como un consultorio serio").
- **Inspiración visual y Craft:** Basada en el verde teal institucional (`#0D9488`) que transmite salud, frescura y cuidado. La interfaz huye del gris corporativo aburrido y del colorido infantil de juguetería. Es una herramienta de trabajo que se ve profesional sin ser intimidante. La tipografía Google Sans suma un toque moderno sin caer en lo informal.

---

## Anti-references

- **El sistema legacy gris:** Evitar a toda costa la estética de los sistemas de gestión de los 90: formularios cuadriculados de Java AWT, botones grises cuadrados, tablas sin separación visual y fondos beige. El usuario no debe sentir que está usando un sistema jubilado.
- **La app infantil de colores saturados:** Interfaces con verdes lima, naranjas fluorescentes y amarillos que parecen un juego para niños. Happy Paws trata con dueños de mascotas adultos y profesionales veterinarios; el diseño debe ser serio pero moderno.
- **El cajón-de-sastre sin arquitectura:** Pantallas que mezclan fichas de pacientes con notas internas con botones de administración sin jerarquía visual. Cada sección debe tener un propósito claro y un camino de navegación predecible.

---

## Design Principles

1. **Desktop-First con enfoque operativo:** El 100% de la interacción es en pantalla grande con teclado y mouse. Los botones y campos deben ser grandes (mínimo 32px de altura) para uso rápido sin apuntar milimétricamente. El flujo principal (citas → atender → recetar → comprobante) no debe requerir más de 3 clics desde el dashboard.

2. **Zero-Delay Information Retrieval:** Si un veterinario necesita ver el historial de "Bobby", debe encontrarlo escribiendo parcialmente el nombre en el buscador de pacientes, sin navegar menús ni acordarse del número de ficha. El buscador debe filtrar en tiempo real mientras se escribe.

3. **Estado Visible del Turno:** En todo momento el usuario debe saber si un turno está pendiente, completado o cancelado sin tener que abrirlo. Las tarjetas de turno usan colores de borde e iconos para comunicar el estado de un vistazo. Un turno pendiente de hoy no se ve igual que uno completado de la semana pasada.

4. **Delegación Clara (Demostración POO):** La arquitectura debe evidenciar el patrón MVC y la delegación. `ComprobanteTurno` no almacena datos clínicos duplicados; consulta a `Turno`, que consulta a `Animal`, que consulta a `Responsable`. Esta cadena debe ser visible en el código y explicable en la defensa.

---

## Accessibility & Inclusion

- **Contraste alto para lectura en consultorio:** Los textos sobre fondos claros deben mantener una relación de contraste mínima de 7:1 (WCAG AA+, superando el mínimo de 4.5:1) para que puedan leerse desde cualquier ángulo del escritorio, incluso con luz de ventana o tubos fluorescentes.
- **Tamaño de fuente no inferior a 12px:** Toda la interfaz debe ser legible sin necesidad de lentes de aumento. Las etiquetas de formulario y los datos de la historia clínica parten de 12px como mínimo absoluto.
- **Terminología veterinaria argentina precisa:** Uso de términos locales como "responsable" (dueño), "turno" (cita), "recetar", "vacuna antirrábica", "castración", "consulta general". Sin traducciones neutrales o extranjerismos que suenen a manual importado.
- **Feedback visual inmediato:** Cada acción del usuario (completar turno, recetar, agregar nota) debe tener una confirmación visual en menos de 200ms. Swing es síncrono, pero los cambios en la UI (cambio de estado del turno, actualización de lista) deben aplicarse sin que el usuario tenga que refrescar manualmente.
- **Operación sin internet:** El sistema funciona 100% offline al ser una app de escritorio Java. No hay dependencia de conectividad para registrar turnos, consultar historiales o emitir comprobantes.
