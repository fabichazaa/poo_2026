# Diagrama UML — Sistema de Gestión Veterinaria

Este directorio contiene el diagrama de clases UML del sistema **Happy Paws** (gestión veterinaria), en tres formatos:

| Archivo | Formato | Para qué sirve |
|---|---|---|
| `VeterinariaUML.puml` | PlantUML (texto) | Editar, versionar en Git, regenerar la imagen |
| `VeterinariaUML.svg` | SVG (82 KB) | Imagen vectorial |
| `VeterinariaUML.mmd` | Mermaid | Visualización directa en GitHub (sin instalar nada) |

## Cómo regenerar el SVG

Si modificás el `.puml`, volvé a renderizar con PlantUML:

```bash
java -jar plantuml.jar -tsvg VeterinariaUML.puml
```

(El jar se descarga de <https://plantuml.com/download>).

## Resumen del modelo

**15 clases + 2 enums** organizados en 3 jerarquías de herencia:

1. `Persona` (abstract) → `Responsable` y `Veterinario`
2. `Animal` (abstract) → `Perro`, `Gato`, `Conejo`, `Loro` y `Tortuga`
3. `Medicamento` → `Vacuna`

**Relaciones principales con multiplicidades:**

- `Persona "1" --> "0..1" Direccion` — Asociación (puede no tener dirección)
- `Animal "1" *-- "1" HistoriaClinica` — **Composición** (nace con su historia)
- `Responsable "1" o-- "0..*" Animal` — Agregación (dueño de varias mascotas)
- `Turno "0..*" --> "1" Veterinario` — Asociación (cada turno un veterinario)
- `Turno "0..*" --> "1" Animal` — Asociación (cada turno un animal)
- `Turno "0..*" --> "1" TipoTurno` — Asociación con enum
- `Veterinaria "1" *-- "0..*" { Veterinario, Responsable, Turno }` — Composición
- `HistoriaClinica "1" o-- "0..*" Medicamento` — Agregación

**Clase de reporte (requisito f del PDF):** `ComprobanteTurno` no duplica datos; **delega** en `Turno` → `Animal` → `HistoriaClinica` y en `Veterinario` / `Responsable` para consolidar la información.

## Requisitos del trabajo integrador cubiertos por el diseño

- ✅ a. Herencia con clase abstracta + 2+ subclases concretas
- ✅ b. Encapsulamiento (todos los atributos `private`)
- ✅ c. Polimorfismo: `Animal.getTipoAlimentacion()` y `Animal.getEspecie()` sobrescritos en `Perro`, `Gato`, `Conejo`, `Loro` y `Tortuga`
- ✅ d. Composición (Animal↔HistoriaClinica, Veterinaria↔*) y agregación (Responsable↔Animal, HistoriaClinica↔Medicamento)
- ✅ e. Colecciones: múltiples `ArrayList` con alta (`registrar*`, `agregar*`), búsqueda (`buscar*`, `obtener*`) y recorrido
- ✅ f. Clase de reporte: `ComprobanteTurno`
- ✅ g. Sobrecarga de constructores: `Persona`, `Responsable`, `Veterinario`, `Animal`, `Perro`, `Gato`, `Turno`, `Veterinaria`
