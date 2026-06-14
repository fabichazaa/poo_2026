# Guion para la defensa oral — Happy Paws

**Duración objetivo:** 15-20 minutos de exposición + 5-10 minutos de preguntas.
**Integrantes sugeridos por bloque** (a coordinar con el grupo):

| Bloque | Tiempo | A cargo de |
|---|---|---|
| 1. Apertura y dominio | 2 min | Portavoz principal |
| 2. Diagrama UML | 3 min | Responsable de modelo |
| 3. Decisiones de diseño (POO) | 5 min | Responsable técnico |
| 4. Demo en vivo | 4 min | Responsable de UI |
| 5. Pruebas | 2 min | Responsable de testing |
| 6. Cierre y preguntas | 2 min | Cualquiera |

---

## 0. Preparación previa (no se expone)

- Tener el proyecto compilado: `javac -d build` y `java -cp build Main`.
- Login fresco: `MP-9854` (Carlos) y `MP-1024` (Laura) listos.
- Tener el navegador de internet y `Demo.java` a mano por si piden ver las pruebas.
- UML impreso o en PDF en el USB por si falla el proyector.
- Tener abierto el `.docx` del informe en la laptop de respaldo.

---

## 1. Apertura y dominio (2 min)

> "Buen día. Somos [nombres] y vamos a presentar **Happy Paws**, un sistema de gestión veterinaria desarrollado en Java con Swing como trabajo integrador de Programación Orientada a Objetos.
>
> El sistema modela el día a día de una veterinaria: clientes, mascotas, veterinarios, turnos, historias clínicas, prescripciones y portal de adopciones. El dominio fue elegido a propósito porque tiene **dos jerarquías de herencia paralelas** — `Animal`/`Perro`/`Gato` y `Persona`/`Responsable`/`Veterinario` — y una variedad de relaciones de composición y agregación, lo que nos obligó a tomar decisiones de diseño explícitas en lugar de tratarlo todo como asociación genérica."

---

## 2. Diagrama UML (3 min)

> "Acá está el diagrama UML completo. Pueden ver las dos jerarquías de herencia, las composiciones — `Animal` con su `HistoriaClinica`, `Veterinaria` con todo lo que le pertenece — y las agregaciones, como `Responsable` con sus mascotas, donde las mascotas pueden cambiar de dueño.
>
> Decidimos entregar el diagrama en cuatro formatos: fuente `.puml` editable, una imagen PNG, una SVG vectorial, y una versión Mermaid que se ve directo en GitHub. Cualquiera puede regenerarlo con `java -jar plantuml.jar`.

*(Mostrar el UML en pantalla. Si preguntan, señalar la herencia de `Medicamento` a `Vacuna`.)*

---

## 3. Decisiones de diseño POO (5 min)

### 3.1 Herencia y polimorfismo (1.5 min)

> "Tenemos **dos clases abstractas**: `Animal` y `Persona`. `Animal` declara el método polimórfico `getTipoAlimentacion()`, que retorna un enum `TipoAlimentacion`. En `Perro` retorna `OMNIVORO`; en `Gato`, `CARNIVORO_ESTRICTO`. Esto demuestra polimorfismo dinámico real: el llamador no necesita saber el tipo concreto."

*(Si la profe pregunta, abrir `Animal.java` y mostrar el override en `Perro.java` y `Gato.java`.)*

### 3.2 Composición vs. agregación (1.5 min)

> "Distinguimos explícitamente composición y agregación:
> - **Composición**: `Animal` con su `HistoriaClinica` — el historial no existe sin el animal. Si eliminás al animal, se va con él.
> - **Agregación**: `Responsable` con sus `Animal`es — el dueño puede tener cero o muchas, y las mascotas pueden cambiar de dueño.
>
> En el UML se nota con la composición dibujada como rombo lleno y la agregación como rombo vacío."

### 3.3 Sobrecarga de constructores (1 min)

> "Las clases con varios niveles de uso exponen al menos dos constructores. Por ejemplo, `Veterinaria` tiene un constructor `Veterinaria(String nombreNegocio)` que delega a `Veterinaria(String, boolean autoSembrar)`. Esto permite crear la veterinaria con o sin datos sembrados — útil para testing. Lo mismo pasa en `Persona`, `Responsable`, `Veterinario`, `Animal`, `Perro`, `Gato` y `Turno`."

### 3.4 Clase de reporte (1 min)

> "La clase de reporte es `ComprobanteTurno`. Lo importante: **no almacena estado**. Cada vez que se genera un comprobante, recibe el `Turno` y delega en él la consulta de los datos — nombre del paciente, del responsable, del veterinario, del tipo de atención. Cero duplicación de datos, exactamente como pide la cátedra."

---

## 4. Demo en vivo (4 min)

> "Ahora vamos a ver el sistema funcionando."

1. **Login**: Iniciar la app. Tipear `MP-9854`. Mostrar que entra como Dr. Carlos Páez.
2. **Inicio**: Mostrar las **tres tarjetas dinámicas** arriba. "Esto no es hardcodeado: se calcula con `contarTurnosDelVeterinario()` aplicado a distintos estados."
3. **Citas**: Ir a *Citas*. Mostrar la lista de turnos del día. Tocar un turno pendiente y presionar **✓ Completar**. Mostrar el diálogo de comprobante. Tocar **📄 Comprobante** en un turno completado.
4. **Recetar**: Mientras se está completando el turno, abrir el diálogo de **recetar medicamento**. Elegir Amoxicilina, confirmar.
5. **Adopción**: Ir a *Portal de Adopción*. Mostrar a Bobby ya marcado en adopción. Tocar el toggle en otro animal para mostrar que se actualiza dinámicamente.
6. **Notas**: Ir a *Notas*. Agregar una nota. Mostrar que aparece con autor "Carlos Páez" y la fecha/hora actual.
7. **Cerrar sesión**: Salir a *Más* → cerrar sesión. Mostrar que vuelve al login.

> "Si intentamos con una matrícula que no existe, el login la rechaza sin mostrar la app."

---

## 5. Pruebas (2 min)

> "Desarrollamos una suite de 62 pruebas integrales automatizadas en `Demo.java`. No usamos JUnit a propósito, para mostrar que con un simple `main()` bien estructurado se puede tener un *runner* claro. Las pruebas se dividen en 11 secciones:"

*(Pasar el `Demo.java` por pantalla o terminal):*

```
Sección 1: Verificación de datos seed              6/6 ✓
Sección 2: Polimorfismo                            4/4 ✓
Sección 3: Login por matrícula                     7/7 ✓
Sección 4: Alta de turno → atender → comprobante  11/11 ✓
Sección 5: Recetar medicamento y aplicar vacuna    5/5 ✓
Sección 6: Portal de adopciones                    5/5 ✓
Sección 7: Notas y recordatorios                   5/5 ✓
Sección 8: Búsquedas y filtros                    10/10 ✓
Sección 9: Composición y agregación                4/4 ✓
Sección 10: Presentación de combos                 3/3 ✓
Sección 11: Cierre de sesión                       2/2 ✓
══════════════════════════════════════════
Resultado: 62 pasadas, 0 fallidas
```

> "Cubren todos los requisitos del PDF, incluido el control de vencimiento de vacunas: Cheese tiene una vacuna vieja en su historia y el sistema la preserva para auditoría sin romper el alta."

---

## 6. Cierre (1 min)

> "Para cerrar: aplicamos los cinco pilares de POO con casos reales, no con ejemplos de libros. Separamos MVC con un Singleton como punto único de coordinación. Tenemos UML en cuatro formatos, 62 pruebas que pasan y un sistema con datos seed realistas — no es un *Hola Mundo* con esteroides, es un dominio donde las decisiones de diseño se notan.
>
> Trabajos futuros: persistencia en disco, roles múltiples, migración a JavaFX preservando el mismo modelo.
>
> ¿Preguntas?"

---

## 7. Preguntas frecuentes (preparadas)

### "¿Por qué Java Swing y no JavaFX o web?"
> Swing viene en el JDK sin dependencias externas. El alcance de la materia es POO, no UI moderna, así que priorizamos el dominio. El MVC nos deja migrar la vista a JavaFX o web sin tocar el modelo.

### "¿Por qué Singleton en el controlador?"
> Para garantizar **un único punto de coordinación** de la sesión. Si hubiera dos instancias, las notas agregadas por un veterinario no las vería otro. Además facilita testing: con un `reiniciar()` reseteás todo y empezás limpio.

### "¿Y la persistencia? ¿No pierden los datos al cerrar?"
> Está en el alcance como trabajo futuro. El modelo está pensado para sumar serialización a JSON o una DB embebida (H2, SQLite) sin tocar el dominio. El `Veterinaria` ya es dueña de sus listas y tiene métodos de búsqueda listos para mapear a una capa de persistencia.

### "¿Cómo justifican que `ComprobanteTurno` cumple el requisito de reporte?"
> Tres condiciones: 1) es una clase propia distinta del modelo, 2) genera una salida formateada, 3) **delega en otros objetos** para obtener los datos (Turno, Animal, Responsable, Veterinario) — no duplica atributos. Pueden verificarlo en el código.

### "¿Por qué `Animal` y `Persona` son abstract y no interface?"
> Porque tienen **estado y comportamiento común**. `Animal` tiene nombre, edad, peso, historia clínica. Una interface solo declara comportamiento, no estado. Usamos clases abstractas para reusar atributos y exigir al mismo tiempo que cada subclase implemente `getTipoAlimentacion()`.

### "¿Qué pasa si la matrícula tiene espacios o mayúsculas distintas?"
> Validamos exactamente: vacío falla, null falla, inexistente falla. Mayúsculas/minúsculas son sensibles (no se normaliza) porque las matrículas reales no tienen variantes.

### "¿Cómo hicieron para que las estadísticas de Inicio no sean números fijos?"
> Antes había "3 Atendidos", "4 Mis Turnos" hardcodeados. Los hicimos dinámicos con `controlador.contarTurnosDelVeterinario(vet, estado)`, donde el segundo parámetro es el estado a contar o `null` para total. Esto se nota especialmente cuando el vet completa un turno en vivo: la tarjeta "Pendientes" baja en 1.

### "¿Y si el sistema pide guardar turnos a futuro pero después se quiere cancelar?"
> `Turno` tiene estado `CANCELADO`. La `Veterinaria` tiene `obtenerTurnosCompletados()` y `obtenerTurnosPendientes()`. La UI hoy muestra los pendientes y completados; los cancelados están en el modelo listos para filtrar en una vista futura.

### "¿Cómo probaron que el polimorfismo anda?"
> En el `Demo` hay un test que toma un `Animal` (declarado como `Animal`, no como `Perro` o `Gato`), pregunta `getTipoAlimentacion()` y verifica el resultado sin hacer `instanceof`. Si fuera una `Perro` retorna OMNIVORO; si fuera `Gato` retorna CARNIVORO_ESTRICTO.

### "¿Y la sobrecarga de constructores en qué se nota?"
> Ejemplo concreto: `Veterinaria` se puede instanciar como `new Veterinaria("Happy Paws")` (autoSiembra=true) o `new Veterinaria("Happy Paws", false)` (vacía, para tests). En el `Demo` no hace falta porque usamos el controlador, pero la flexibilidad está disponible.

### "¿Por qué `Medicamento` y `Vacuna` no son abstract con subclases múltiples?"
> Decidimos que `Vacuna extends Medicamento` porque toda Vacuna **es** un Medicamento (mismo ID, mismo nombre) pero además tiene fechas de aplicación y vencimiento. El `instanceof Vacuna` se usa para activar `estaVencida()`. Si hubiera 5 tipos de medicamentos, habríamos ido a composición, pero acá la jerarquía es clara y económica.

### "¿Qué pasa con concurrencia? ¿Dos veterinarios a la vez?"
> No contemplado. Es una app de escritorio single-user. En la versión con persistencia, eso se delega a la capa de DB (transacciones, locks).

### "¿Cómo se carga la tipografía sin instalarla en el sistema?"
> `CargadorFuentes` usa `getResourceAsStream("/recursos/GoogleSans.ttf")` para leer el `.ttf` desde el classpath. El `.ttf` viaja dentro del JAR. Si falla, hace fallback a `SansSerif` del sistema. Esto hace la app portable: no hay rutas absolutas ni instalaciones manuales.

---

## 8. Checklist final el día de la defensa

- [ ] Laptop con batería cargada
- [ ] Proyector / adaptador probado
- [ ] `java -cp build Main` corre sin errores
- [ ] `java -cp build Demo` muestra 62/62
- [ ] `INFORME_TECNICO_HappyPaws.docx` abierto y PDF de respaldo
- [ ] UML en PNG y SVG en el escritorio (por si fallan los formatos)
- [ ] Código fuente en un editor con syntax highlighting (VSCode, IntelliJ)
- [ ] Cada integrante tiene claro su bloque
- [ ] Agua, nervios bajo control, llegar 10 min antes
