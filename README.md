# Happy Paws — Sistema de Gestión Veterinaria

Trabajo Integrador de **Programación Orientada a Objetos** (Tecnicatura Universitaria en Desarrollo de Software).

Aplicación de escritorio en **Java + Swing** para gestionar una veterinaria: clientes, mascotas, veterinarios, turnos, historia clínica, medicamentos, vacunas y portal de adopciones.

## 🚀 Cómo ejecutar

```bash
# Compilar
javac -d build $(Get-ChildItem -Recurse -Filter *.java | ForEach-Object FullName)

# Ejecutar GUI (por defecto)
java -cp build Main

# Ejecutar demo por consola
java -cp build Main --consola

# Ejecutar batería de pruebas integrales (59 asserts, todas las funcionalidades)
java -cp build Demo
```

> Requisito: **JDK 11+**.

## 📂 Estructura del proyecto

```text
poo_2026/
├── modelo/             # Dominio (clases de negocio, sin UI)
│   ├── Animal.java
│   ├── Perro.java
│   ├── Gato.java
│   ├── Persona.java
│   ├── Responsable.java
│   ├── Veterinario.java
│   ├── Direccion.java
│   ├── HistoriaClinica.java
│   ├── Medicamento.java
│   ├── Vacuna.java
│   ├── Turno.java
│   ├── TipoTurno.java
│   ├── TipoAlimentacion.java
│   ├── Veterinaria.java
│   └── ComprobanteTurno.java
│
├── vista/              # Interfaz gráfica (Swing)
│   └── PortalVeterinario.java
│
├── controlador/        # Coordinación entre modelo y vista (MVC)
│   └── ControladorVeterinaria.java
│
├── recursos/           # Recursos estáticos + helpers
│   ├── GoogleSans.ttf
│   └── CargadorFuentes.java
│
├── imagenes/           # Imágenes de la UI
├── diagrama/           # Diagrama UML (PlantUML, PNG, SVG, Mermaid)
├── Main.java           # Punto de entrada
├── README.md
└── .gitignore
```

## 🧱 Principios POO aplicados

| Requisito PDF | Implementación |
|---|---|
| Herencia con clase abstracta | `Animal` (abstract) → `Perro`, `Gato`; `Persona` (abstract) → `Responsable`, `Veterinario`; `Medicamento` → `Vacuna` |
| Encapsulamiento | Todos los atributos `private`, acceso por getters/setters |
| Polimorfismo | `Animal.getTipoAlimentacion()` y `getEspecie()` sobrescritos en cada subclase |
| Composición / Agregación | `Animal` compone su `HistoriaClinica`; `Veterinaria` compone Veterinarios/Responsables/Turnos; `Responsable` agrega `Animal`es; `HistoriaClinica` agrega `Medicamento`s |
| Colecciones | `ArrayList` y `List` con alta, búsqueda y recorrido (`buscar*`, `obtener*`) |
| Clase de reporte | `ComprobanteTurno` — **delega** en `Turno`/`Animal`/`Veterinario`/`Responsable`/`HistoriaClinica` sin duplicar datos |
| Sobrecarga de constructores | `Persona`, `Responsable`, `Veterinario`, `Animal`, `Perro`, `Gato`, `Turno`, `Veterinaria` |

## 🗺 Diagrama UML

Ver `diagrama/README.md` para más detalle. Hay 4 versiones:

- `diagrama/VeterinariaUML.puml` — fuente editable (PlantUML)
- `diagrama/VeterinariaUML.png` — imagen rasterizada
- `diagrama/VeterinariaUML.svg` — vectorial
- `diagrama/VeterinariaUML.mmd` — Mermaid (se ve en GitHub)

## 📄 Informe y defensa

- `informe/INFORME_TECNICO_HappyPaws.docx` — informe técnico completo (carátula, índice, marco teórico, decisiones de diseño, UML embebido, pruebas, conclusiones y bibliografía).
- `informe/GUION_DEFENSA_HappyPaws.docx` — guion de 15-20 min para la defensa oral, con asignación de bloques por integrante y respuestas a preguntas frecuentes.
- `informe/*.md` — fuentes editables.
- `informe/generar_*.py` — scripts para regenerar los `.docx` si se modifican los `.md`.

## 🛠 Compilación con tu IDE

- **IntelliJ IDEA / Eclipse / NetBeans**: importar como proyecto Java estándar (no hay `pom.xml` ni `build.gradle`; las carpetas son los paquetes).
- Compilar desde raíz: `javac -d build modelo/*.java vista/*.java controlador/*.java recursos/*.java Main.java`.

## 👥 Grupo

- Integrante 1 — Fabiola Chazarreta
- Integrante 2 — Juan Carlos Fernandez
- Integrante 3 — Alejandro Gallo
- Integrante 4 — Juan Marengo

**Fecha de entrega / defensa**: 22 de julio de 2026.
