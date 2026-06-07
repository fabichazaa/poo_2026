---
title: "Sistema de Gestión Veterinaria Happy Paws"
subtitle: "Trabajo Integrador — Programación Orientada a Objetos"
authors:
  - "Fabiola Chazarreta"
  - "Juan Carlos Fernandez"
  - "Alejandro Gallo"
  - "Juan Marengo"
date: "Junio de 2026"
institution: "Tecnicatura Universitaria en Desarrollo de Software"
subject: "Programación Orientada a Objetos"
delivery_date: "22 de julio de 2026"
---

# Resumen

El presente trabajo describe el diseño y la implementación de **Happy Paws**, un sistema de gestión veterinaria desarrollado en Java con interfaz gráfica Swing como Trabajo Integrador de la materia Programación Orientada a Objetos. El sistema permite administrar clientes, mascotas, veterinarios, turnos, historias clínicas, medicamentos, vacunas y un portal de adopciones. La aplicación se construyó aplicando los pilares del paradigma orientado a objetos: abstracción, encapsulamiento, herencia, polimorfismo y composición, complementados con el patrón arquitectónico Modelo–Vista–Controlador (MVC). Se incorpora además un *seed* de datos realistas (4 veterinarios, 4 clientes, 10 mascotas, 16 turnos, 8 medicamentos y un portal de adopciones activo) y una batería de **59 pruebas integrales automatizadas** que validan todos los flujos funcionales del sistema con un 100 % de éxito.

**Palabras clave:** Java, Swing, POO, MVC, UML, herencia, polimorfismo, composición, agregación, colecciones.

---

# Índice

1. [Introducción](#1-introducción)
2. [Marco teórico](#2-marco-teórico)
3. [Descripción del sistema](#3-descripción-del-sistema)
4. [Decisiones de diseño](#4-decisiones-de-diseño)
5. [Diagrama UML](#5-diagrama-uml)
6. [Implementación](#6-implementación)
7. [Pruebas](#7-pruebas)
8. [Conclusiones y trabajos futuros](#8-conclusiones-y-trabajos-futuros)
9. [Bibliografía](#9-bibliografía)
10. [Anexo A — Guía de uso](#anexo-a--guía-de-uso)

---

# 1. Introducción

## 1.1 Contexto

La gestión administrativa de una clínica veterinaria pequeña o mediana suele realizarse en planillas, cuadernos o sistemas propietarios de difícil adaptación. Esto genera problemas recurrentes: pérdida de historias clínicas, duplicación de turnos, falta de trazabilidad de vacunas y medicamentos, y escasa visibilidad sobre el estado de los animales en proceso de adopción.

## 1.2 Problema

Se requiere un sistema de escritorio, portable y sin dependencias externas, que centralice la operatoria diaria de una veterinaria: alta y búsqueda de clientes y mascotas, agenda de turnos por veterinario, registro de prácticas y prescripciones, gestión del portal de adopciones, y emisión de comprobantes para el cliente.

## 1.3 Objetivos

### 1.3.1 Objetivo general

Desarrollar una aplicación de escritorio en Java que modele el dominio de una veterinaria, aplicando de forma explícita y verificable los conceptos de la Programación Orientada a Objetos exigidos por el programa de la materia.

### 1.3.2 Objetivos específicos

- Modelar el dominio con clases, herencia, encapsulamiento, polimorfismo, composición y agregación.
- Implementar búsquedas y recorridos sobre colecciones (alta, búsqueda, listado).
- Incorporar una clase de reporte (`ComprobanteTurno`) que delegue en otras clases sin duplicar estado.
- Aplicar sobrecarga de constructores en las clases principales.
- Separar la solución en capas (MVC) con un único punto de coordinación (`ControladorVeterinaria`).
- Generar y documentar un diagrama UML de clases.
- Validar la implementación con pruebas automatizadas.

## 1.4 Alcance

El sistema cubre el ciclo completo desde el login del veterinario hasta la emisión de comprobantes. **No** contempla en esta entrega: persistencia en disco (los datos se siembran al iniciar la aplicación), envío de correos, integración con sistemas de pago ni aplicación móvil.

---

# 2. Marco teórico

## 2.1 Programación Orientada a Objetos

La POO modela un sistema como un conjunto de **objetos** que colaboran entre sí. Cada objeto encapsula **estado** (atributos) y **comportamiento** (métodos), pertenece a una **clase** que define su tipo, y se comunica con otros objetos a través de **mensajes** (llamadas a métodos).

## 2.2 Los cuatro pilares

| Pilar | Definición | Aplicación en Happy Paws |
|---|---|---|
| **Abstracción** | Identificar las características esenciales de una entidad, ignorando los detalles accidentales. | Clases `Animal` y `Persona` modelan lo común a todas las mascotas y a todas las personas del dominio. |
| **Encapsulamiento** | Ocultar el estado interno y exponer solo lo necesario mediante una interfaz controlada. | Todos los atributos son `private`; el acceso se realiza por *getters* y *setters* con validaciones. |
| **Herencia** | Mecanismo para crear jerarquías donde las subclases reutilizan y especializan el comportamiento de la superclase. | `Perro` y `Gato` heredan de `Animal`; `Responsable` y `Veterinario` heredan de `Persona`; `Vacuna` hereda de `Medicamento`. |
| **Polimorfismo** | Capacidad de un mismo mensaje de producir comportamientos distintos según el tipo concreto del receptor. | `Animal.getTipoAlimentacion()` retorna `OMNIVORO` en `Perro` y `CARNIVORO_ESTRICTO` en `Gato`. |

## 2.3 Composición vs. agregación

Ambos son tipos de asociación "todo–parte", pero con diferente强度的 (fuerza) del vínculo:

- **Composición**: la parte *no puede existir* sin el todo. Si el todo se destruye, las partes también. Ejemplo: una `HistoriaClinica` no tiene sentido fuera de su `Animal`; cuando el animal se elimina del sistema, su historial lo acompaña.
- **Agregación**: la parte *puede existir* sin el todo. El vínculo es más débil. Ejemplo: un `Responsable` puede tener cero, una o muchas `Animal`es; si el responsable se va, sus mascotas pueden ser reasignadas o transferidas.

## 2.4 Colecciones en Java

El framework Collections provee estructuras para almacenar y manipular grupos de objetos. En Happy Paws se utiliza principalmente `ArrayList<T>`, elegido por:

- acceso indexado en O(1);
- recorrido eficiente con *for-each* y streams;
- redimensionado dinámico, sin necesidad de definir capacidad máxima;
- compatibilidad directa con *contains*, *indexOf* y orden de inserción preservado.

## 2.5 Patrón Modelo–Vista–Controlador (MVC)

MVC separa tres responsabilidades:

- **Modelo**: reglas de negocio y estado. No conoce la vista.
- **Vista**: presentación e interacción con el usuario. No conoce el modelo.
- **Controlador**: recibe eventos de la vista, los traduce en operaciones sobre el modelo, y notifica a la vista los cambios producidos.

En Happy Paws el modelo vive en el paquete `modelo/`, la vista en `vista/` y el controlador en `controlador/`, con `ControladorVeterinaria` implementado como **Singleton** para garantizar un único punto de coordinación de la sesión.

---

# 3. Descripción del sistema

## 3.1 Caso de uso general

"Un veterinario matriculado inicia sesión en el sistema, agenda turnos para distintos clientes, atiende a las mascotas, registra prácticas y prescribe medicamentos o vacunas, y emite comprobantes para el cliente. Desde la misma aplicación puede gestionar el portal de adopciones y tomar notas internas."

## 3.2 Actores

| Actor | Tipo | Descripción |
|---|---|---|
| **Veterinario** | Primario | Profesional matriculado que opera el sistema. |
| **Cliente** (representado en datos) | Secundario | Dueño de la mascota; no interactúa con la UI. |
| **Animal** (representado en datos) | Secundario | Paciente; no interactúa con la UI. |

## 3.3 Requisitos funcionales (RF)

| ID | Descripción | Cumplido |
|---|---|---|
| RF-01 | Iniciar sesión mediante matrícula profesional. | ✅ |
| RF-02 | Listar y buscar veterinarios, clientes y mascotas. | ✅ |
| RF-03 | Registrar nuevos turnos con fecha, hora, veterinario, animal y tipo. | ✅ |
| RF-04 | Marcar turnos como completados y emitir comprobante. | ✅ |
| RF-05 | Recetar medicamentos y aplicar vacunas con control de vencimiento. | ✅ |
| RF-06 | Activar o desactivar animales en el portal de adopciones. | ✅ |
| RF-07 | Agregar y eliminar notas internas. | ✅ |
| RF-08 | Mostrar estadísticas dinámicas (atendidos, turnos, pendientes). | ✅ |

## 3.4 Requisitos no funcionales (RNF)

| ID | Descripción | Cumplido |
|---|---|---|
| RNF-01 | Portabilidad: ejecutar en cualquier máquina con JDK 11+ sin instalación adicional. | ✅ |
| RNF-02 | Carga de tipografías portable desde el *classpath* (no rutas absolutas). | ✅ |
| RNF-03 | Separación estricta entre capas (MVC). | ✅ |
| RNF-04 | Pruebas automatizadas con cobertura de los flujos principales. | ✅ (59/59) |
| RNF-05 | Documentación UML en al menos un formato editable y otro gráfico. | ✅ (`.puml`, `.png`, `.svg`, `.mmmd`) |

---

# 4. Decisiones de diseño

## 4.1 Herencia con clase abstracta

Se eligieron dos jerarquías de herencia paralelas:

```
Persona (abstract) ──┬── Responsable
                    └── Veterinario

Animal  (abstract) ──┬── Perro
                    └── Gato

Medicamento ─────────── Vacuna
```

- `Persona` agrupa DNI, nombre, apellido, teléfono, email y dirección.
- `Animal` agrupa nombre, edad, peso, sexo, responsable e historial clínico.
- `Medicamento` se especializa en `Vacuna` agregando fechas de aplicación y vencimiento.

## 4.2 Polimorfismo elegido

Se seleccionó `getTipoAlimentacion()` como método polimórfico obligatorio (retornando un `enum` `TipoAlimentacion` con `OMNIVORO` y `CARNIVORO_ESTRICTO`) por dos razones:

1. **Demostrabilidad**: en cualquier punto del sistema se puede preguntar a un `Animal` su tipo de alimentación y la respuesta depende del tipo concreto, sin necesidad de `instanceof`.
2. **Extensibilidad**: si en el futuro se agrega la clase `Ave` o `Reptil`, basta sobrescribir el método. El código cliente no cambia.

Adicionalmente, `getEspecie()` también es polimórfico y retorna `"Perro"` o `"Gato"`.

## 4.3 Composición y agregación

| Relación | Tipo | Justificación |
|---|---|---|
| `Animal *-- HistoriaClinica` | **Composición** | El historial no existe sin el animal. |
| `Animal --> Responsable` | **Asociación** | El animal tiene un dueño, pero el dueño no "posee" al animal en sentido técnico. |
| `Responsable o-- Animal` | **Agregación** | Un responsable puede tener varias mascotas; las mascotas pueden cambiar de dueño. |
| `HistoriaClinica o-- Medicamento` | **Agregación** | La historia acumula medicamentos recetados a lo largo del tiempo. |
| `Veterinaria *-- Veterinario` | **Composición** | La veterinaria es dueña de su equipo. |
| `Veterinaria *-- Responsable` | **Composición** | La veterinaria es dueña de su cartera de clientes. |
| `Veterinaria *-- Turno` | **Composición** | Los turnos son parte del sistema de la veterinaria. |
| `Turno --> Veterinario` | **Asociación** | Un turno es atendido *por* un veterinario. |
| `Turno --> Animal` | **Asociación** | Un turno es *para* un animal. |

## 4.4 Clase de reporte: `ComprobanteTurno`

`ComprobanteTurno` no almacena estado. Cada vez que se genera un comprobante, recibe un `Turno` y delega en él la consulta de datos: nombre del paciente (a través de `Turno.getAnimal()`), del responsable, del veterinario, del tipo de atención y de la fecha. Esto cumple el requisito de **delegación sin duplicación de datos** del PDF.

```java
public String generarTextoCompleto() {
    Turno t = this.turno;
    return "Comprobante — " + t.getVeterinaria().getNombreNegocio()
         + "\nPaciente: "   + t.getAnimal().getNombre()
         + "\nResponsable: "+ t.getAnimal().getResponsable().getNombreCompleto()
         + "\nVeterinario: "+ t.getVeterinario().getNombreCompleto()
         + "\nTipo: "       + t.getTipo().getDescripcion()
         + "\nFecha: "      + t.getFecha() + " " + t.getHora();
}
```

## 4.5 Sobrecarga de constructores

Las clases con varios niveles de uso exponen al menos dos constructores: uno completo y uno simplificado. Ejemplo en `Veterinaria`:

```java
public Veterinaria(String nombreNegocio) {
    this(nombreNegocio, true);   // delega con autoSiembra=true
}
public Veterinaria(String nombreNegocio, boolean autoSembrar) {
    this.nombreNegocio = nombreNegocio;
    if (autoSembrar) cargarCatalogoDemo();
}
```

`Persona`, `Responsable`, `Veterinario`, `Animal`, `Perro`, `Gato` y `Turno` también ofrecen sobrecarga, permitiendo instanciar tanto con datos completos como con valores por defecto.

## 4.6 Patrón MVC + Singleton

`ControladorVeterinaria` se implementa como Singleton para garantizar **una única fuente de verdad** de la sesión:

```java
public static ControladorVeterinaria getInstancia() {
    if (instancia == null) instancia = new ControladorVeterinaria();
    return instancia;
}
public static void reiniciar() { instancia = null; }  // útil para tests
```

Las vistas (`PortalVeterinario` y los diálogos) operan exclusivamente a través del controlador; nunca instancian clases del modelo directamente. Esta decisión facilita la trazabilidad, el testing y un eventual reemplazo de la vista (por ejemplo, una versión web).

## 4.7 Estructura de paquetes

```
poo_2026/
├── modelo/       → 15 clases (Animal, Perro, Gato, Persona, Responsable,
│                   Veterinario, Direccion, HistoriaClinica, Medicamento,
│                   Vacuna, Turno, TipoTurno, TipoAlimentacion,
│                   Veterinaria, ComprobanteTurno)
├── vista/        → PortalVeterinario + subpaquetes paneles/ y dialogos/
├── controlador/  → ControladorVeterinaria (Singleton)
├── recursos/     → GoogleSans.ttf + CargadorFuentes
├── imagenes/     → assets gráficos
├── diagrama/     → UML en 4 formatos
├── Main.java     → launcher
├── Demo.java     → 59 pruebas integrales
└── README.md
```

---

# 5. Diagrama UML

El diagrama completo de clases se entrega en cuatro formatos en la carpeta `diagrama/`:

- `VeterinariaUML.puml` — fuente editable (PlantUML).
- `VeterinariaUML.png` — imagen rasterizada.
- `VeterinariaUML.svg` — imagen vectorial.
- `VeterinariaUML.mmd` — fuente Mermaid (visible en GitHub).

> Ver `diagrama/README.md` para el detalle de cómo regenerarlos.

**Diagrama embebido (referencia):**

```
                     ┌──────────────────┐
                     │   Veterinaria    │
                     │  «business»      │
                     └──────────────────┘
                       │ *        │ *
            ┌──────────┘         └────────────┐
            ▼                                ▼
   ┌────────────────┐               ┌────────────────┐
   │  Veterinario   │               │  Responsable   │
   │ «Persona»      │               │ «Persona»      │
   └────────────────┘               └────────────────┘
                                          │ o
                                          ▼
                                     ┌─────────┐
                                     │ Animal  │ «abstract»
                                     │         │
                                     └─────────┘
                                       │ *   │ 1
                                       │     ▼
                                       │  ┌──────────────┐
                                       │  │HistoriaClín. │
                                       │  └──────────────┘
                                       │     │ o
                                       ▼     ▼
                                   ┌──────────────┐
                                   │  Medicamento │
                                   │              │
                                   └──────────────┘
                                         │  △
                                         ▼
                                     ┌─────────┐
                                     │ Vacuna  │
                                     └─────────┘
```

---

# 6. Implementación

## 6.1 Stack tecnológico

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 11+ (probado con JDK 21) |
| UI | Swing (incluido en el JDK) |
| Layouts | BorderLayout, FlowLayout, GridLayout, GridBagLayout, BoxLayout |
| Colecciones | `ArrayList<T>`, `List<T>` |
| Tipografías | `GoogleSans.ttf` (incluida) |
| Diagramas | PlantUML 1.2024.7 |
| Pruebas | Suite propia `Demo.java` (no requiere JUnit) |

## 6.2 Compilación y ejecución

```bash
# Compilar (PowerShell)
javac -d build $(Get-ChildItem -Recurse -Filter *.java | ForEach-Object FullName)

# Ejecutar GUI (login por matrícula)
java -cp build Main

# Ejecutar demo por consola
java -cp build Main --consola

# Ejecutar batería de pruebas integrales
java -cp build Demo
```

## 6.3 Credenciales de prueba

| Matrícula | Veterinario |
|---|---|
| `MP-9854` | Dr. Carlos Páez |
| `MP-1024` | Dra. Laura Gómez |
| `MP-2255` | Dr. Mariano Suárez |
| `MP-3344` | Dra. Sofía Méndez |

> El sistema distingue mayúsculas y minúsculas. La matrícula vacía o inexistente es rechazada.

## 6.4 Carga portable de tipografías

`recursos/CargadorFuentes.java` carga `GoogleSans.ttf` desde el *classpath* mediante `getResourceAsStream`, evitando rutas absolutas y haciendo la aplicación portable:

```java
public static Font cargar(String rutaInterna, float size, int estilo) {
    try (InputStream is = CargadorFuentes.class
            .getResourceAsStream(rutaInterna)) {
        Font base = Font.createFont(Font.TRUETYPE_FONT, is);
        return base.deriveFont(estilo, size);
    } catch (Exception e) { return new Font("SansSerif", estilo, (int) size); }
}
```

---

# 7. Pruebas

## 7.1 Estrategia

Se construyó una batería de pruebas integrales (no unitarias) en `Demo.java`. Cada prueba ejecuta un flujo real sobre el sistema: loguea un veterinario, agenda un turno, lo completa, emite el comprobante, receta un medicamento, marca una mascota en adopción, agrega notas y verifica invariantes.

## 7.2 Resultados

```
──── Smoke test — Happy Paws (Fase 5: Pruebas integrales)
  Sección 1: Verificación de datos seed              6/6 ✓
  Sección 2: Polimorfismo                            4/4 ✓
  Sección 3: Login por matrícula                     7/7 ✓
  Sección 4: Alta de turno → atender → comprobante  11/11 ✓
  Sección 5: Recetar medicamento y aplicar vacuna    5/5 ✓
  Sección 6: Portal de adopciones                    5/5 ✓
  Sección 7: Notas y recordatorios                   5/5 ✓
  Sección 8: Búsquedas y filtros                    10/10 ✓
  Sección 9: Composición y agregación                4/4 ✓
  Sección 10: Cierre de sesión                       2/2 ✓

══════════════════════════════════════════
  Resultado: 59 pasadas, 0 fallidas
══════════════════════════════════════════
```

## 7.3 Cobertura funcional

- ✅ Herencia y polimorfismo: `instanceof`, `getEspecie()`, `getTipoAlimentacion()`.
- ✅ Encapsulamiento: las búsquedas se hacen por métodos públicos, nunca por acceso directo.
- ✅ Composición: `Animal` siempre tiene `HistoriaClinica` no nula.
- ✅ Agregación: `Responsable.getMascotas()` contiene a sus animales.
- ✅ Colecciones: alta (`registrarTurno`, `agregarNota`, `recetarMedicamento`), búsqueda (`buscarVeterinarioPorMatricula`, `buscarClientePorDni`, `buscarMascotasPorNombre`) y recorrido (`obtenerTurnosPendientes`, `obtenerAnimalesEnAdopcion`).
- ✅ Clase de reporte: `ComprobanteTurno.generarTextoCompleto()` contiene los nombres esperados.
- ✅ Sobrecarga de constructores: uso de `Veterinaria(String)` y `Veterinaria(String, boolean)`.
- ✅ Control de vencimientos: una `Vacuna` con fecha pasada reporta `estaVencida() == true` y el sistema la preserva en la historia clínica para auditoría.

---

# 8. Conclusiones y trabajos futuros

## 8.1 Conclusiones

El trabajo permitió aplicar de forma integrada y verificable los conceptos centrales de la Programación Orientada a Objetos a un dominio real. La elección de un dominio con varios tipos de entidades y relaciones (animales, personas, turnos, medicamentos) hizo necesario tomar decisiones explícitas sobre composición y agregación, en lugar de tratar todas las relaciones como "asociación genérica". El polimorfismo, lejos de ser un requisito formal, demostró su valor concreto en la UI: el portal de adopciones y la pantalla de inicio recorren listas heterogéneas de `Animal` y consultan su tipo de alimentación sin necesidad de `instanceof`.

La separación MVC fue clave para mantener la productividad: la lógica de negocio evolucionó independientemente del rediseño visual. El patrón Singleton en el controlador facilitó la escritura de pruebas integrales.

## 8.2 Trabajos futuros

- **Persistencia**: incorporar serialización a JSON o base de datos embebida (H2, SQLite).
- **Roles múltiples**: diferenciar recepcionista, veterinario y administrador.
- **Reportes adicionales**: recetas en PDF, listado mensual de atendidos.
- **Búsqueda avanzada**: filtros combinables (por especie, edad, estado, fecha).
- **Internacionalización**: i18n mediante `ResourceBundle`.
- **Migración a JavaFX** o a una versión web con el mismo modelo (el desacople MVC lo habilita).
- **Pruebas unitarias con JUnit 5** + pruebas de UI con AssertJ Swing.

---

# 9. Bibliografía

- Eckstein, R., Loy, M. & Wood, D. (1998). *Java Swing*. O'Reilly Media.
- Gamma, E. et al. (1994). *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley.
- Gosling, J. et al. *The Java Language Specification*. <https://docs.oracle.com/javase/specs/>
- Oracle. *The Java Tutorials — Object-Oriented Programming Concepts*. <https://docs.oracle.com/javase/tutorial/java/concepts/>
- PlantUML. *Class Diagram*. <https://plantuml.com/class-diagram>

---

# Anexo A — Guía de uso

1. Al iniciar, la aplicación muestra un diálogo de **login**. Ingresar la matrícula (`MP-9854` para Carlos Páez, por ejemplo).
2. La pantalla principal tiene cinco secciones accesibles desde el panel lateral:
   - **Inicio**: saludo, estadísticas y accesos rápidos.
   - **Citas**: agenda de turnos con acciones (✓ Completar, 📄 Comprobante, + Nuevo turno).
   - **Portal de Adopción**: animales disponibles para adopción.
   - **Notas**: notas internas del equipo.
   - **Más**: configuración, exportación de comprobante y cierre de sesión.
3. Al **completar** un turno se abre el diálogo de **recetar medicamento o vacuna**. Tras confirmar, el turno queda marcado como Completado.
4. El botón **📄 Comprobante** abre un diálogo con el comprobante generado por `ComprobanteTurno`. Puede exportarse a texto.
5. En la sección **Más** se puede **cerrar sesión** y volver al login.
