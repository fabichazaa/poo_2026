"""
Genera el .docx a partir de INFORME_TECNICO.md con formato académico:
- Carátula centrada
- Índices automáticos
- Estilos para h1, h2, h3, párrafos, listas, tablas, bloques de código
- UML embebido (PNG)
- Salto de página entre secciones principales
"""
import re
from pathlib import Path
from docx import Document
from docx.shared import Pt, Cm, RGBColor, Inches
from docx.enum.text import WD_ALIGN_PARAGRAPH, WD_BREAK
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml.ns import qn
from docx.oxml import OxmlElement

ROOT = Path(r"C:\Users\el_be\OneDrive\Desktop\Universidad\desarrollo de software\Tercer cuatrimestre\Programacion Orientada a Objetos\poo_2026")
INFORME_DIR = ROOT / "informe"
MD_PATH = INFORME_DIR / "INFORME_TECNICO.md"
DOCX_PATH = INFORME_DIR / "INFORME_TECNICO_HappyPaws.docx"
UML_PNG = ROOT / "diagrama" / "VeterinariaUML.png"

# ----------------------------------------------------------------------
# Front matter (carátula)
# ----------------------------------------------------------------------
FRONT_MATTER = {
    "institution": "Tecnicatura Universitaria en Desarrollo de Software",
    "subject":     "Programación Orientada a Objetos",
    "title":       "Sistema de Gestión Veterinaria Happy Paws",
    "subtitle":    "Trabajo Integrador Final",
    "authors":     [
        "Fabiola Chazarreta",
        "Juan Carlos Fernandez",
        "Alejandro Gallo",
        "Juan Marengo",
    ],
    "professor":   "[NOMBRE DEL PROFESOR/A]",
    "commission":  "[COMISIÓN]",
    "date":        "Junio de 2026",
}

# ----------------------------------------------------------------------
# Helpers
# ----------------------------------------------------------------------
def add_horizontal_rule(paragraph):
    p_pr = paragraph._p.get_or_add_pPr()
    p_bdr = OxmlElement("w:pBdr")
    bottom = OxmlElement("w:bottom")
    bottom.set(qn("w:val"), "single")
    bottom.set(qn("w:sz"), "6")
    bottom.set(qn("w:space"), "1")
    bottom.set(qn("w:color"), "999999")
    p_bdr.append(bottom)
    p_pr.append(p_bdr)


def shade_cell(cell, fill_hex):
    tc_pr = cell._tc.get_or_add_tcPr()
    shd = OxmlElement("w:shd")
    shd.set(qn("w:val"), "clear")
    shd.set(qn("w:color"), "auto")
    shd.set(qn("w:fill"), fill_hex)
    tc_pr.append(shd)


def set_repeat_header(row):
    tr_pr = row._tr.get_or_add_trPr()
    th = OxmlElement("w:tblHeader")
    th.set(qn("w:val"), "true")
    tr_pr.append(th)


def add_page_break(doc):
    p = doc.add_paragraph()
    p.add_run().add_break(WD_BREAK.PAGE)


def add_inline_runs(paragraph, text, base_size=11, bold=False, italic=False, mono=False, color=None):
    """Parsea **bold**, *italic*, `code` dentro de un párrafo."""
    tokens = re.split(r"(\*\*[^*]+\*\*|\*[^*]+\*|`[^`]+`)", text)
    for tok in tokens:
        if not tok:
            continue
        run = paragraph.add_run()
        if tok.startswith("**") and tok.endswith("**"):
            run.text = tok[2:-2]; run.bold = True
        elif tok.startswith("*") and tok.endswith("*"):
            run.text = tok[1:-1]; run.italic = True
        elif tok.startswith("`") and tok.endswith("`"):
            run.text = tok[1:-1]; run.font.name = "Consolas"
            run.font.size = Pt(base_size)
            rPr = run._element.get_or_add_rPr()
            rFonts = OxmlElement("w:rFonts")
            rFonts.set(qn("w:ascii"), "Consolas"); rFonts.set(qn("w:hAnsi"), "Consolas")
            rPr.append(rFonts)
            shade = OxmlElement("w:shd")
            shade.set(qn("w:val"), "clear"); shade.set(qn("w:color"), "auto"); shade.set(qn("w:fill"), "F4F4F4")
            rPr.append(shade)
        else:
            run.text = tok
        if not run.font.name:
            run.font.name = "Calibri"
        if not mono:
            run.font.size = Pt(base_size)
        if color and not mono:
            run.font.color.rgb = color
        if bold and not run.bold:
            run.bold = True
        if italic and not run.italic:
            run.italic = True


# ----------------------------------------------------------------------
# Document creation
# ----------------------------------------------------------------------
doc = Document()

# Default style
style = doc.styles["Normal"]
style.font.name = "Calibri"
style.font.size = Pt(11)
style.paragraph_format.space_after = Pt(6)
style.paragraph_format.line_spacing = 1.25

# Page margins
for section in doc.sections:
    section.top_margin = Cm(2.5)
    section.bottom_margin = Cm(2.5)
    section.left_margin = Cm(2.8)
    section.right_margin = Cm(2.5)

# Headings styles
for level, (size, color) in {1: (22, RGBColor(0x0D, 0x94, 0x88)),
                              2: (16, RGBColor(0x14, 0x4B, 0x70)),
                              3: (13, RGBColor(0x1E, 0x29, 0x3B)),
                              4: (12, RGBColor(0x33, 0x33, 0x33))}.items():
    h = doc.styles[f"Heading {level}"]
    h.font.name = "Calibri"
    h.font.size = Pt(size)
    h.font.bold = True
    h.font.color.rgb = color
    h.paragraph_format.space_before = Pt(14 if level <= 2 else 10)
    h.paragraph_format.space_after  = Pt(6)
    h.paragraph_format.keep_with_next = True

# Code style (no existe por defecto, se aplica en línea)
try:
    cs = doc.styles["Code"]
    cs.font.name = "Consolas"
    cs.font.size = Pt(10)
except KeyError:
    pass

# ----------------------------------------------------------------------
# Carátula
# ----------------------------------------------------------------------
for _ in range(2):
    doc.add_paragraph()

p = doc.add_paragraph()
p.alignment = WD_ALIGN_PARAGRAPH.CENTER
add_inline_runs(p, FRONT_MATTER["institution"], base_size=13, bold=True, color=RGBColor(0x33, 0x33, 0x33))

p = doc.add_paragraph()
p.alignment = WD_ALIGN_PARAGRAPH.CENTER
add_inline_runs(p, FRONT_MATTER["subject"], base_size=12, italic=True, color=RGBColor(0x55, 0x55, 0x55))

doc.add_paragraph()

# Título grande
for _ in range(3):
    doc.add_paragraph()
p = doc.add_paragraph()
p.alignment = WD_ALIGN_PARAGRAPH.CENTER
add_inline_runs(p, "Happy Paws", base_size=36, bold=True, color=RGBColor(0x0D, 0x94, 0x88))

p = doc.add_paragraph()
p.alignment = WD_ALIGN_PARAGRAPH.CENTER
add_inline_runs(p, "Sistema de Gestión Veterinaria", base_size=18, color=RGBColor(0x14, 0x4B, 0x70))

p = doc.add_paragraph()
p.alignment = WD_ALIGN_PARAGRAPH.CENTER
add_inline_runs(p, FRONT_MATTER["subtitle"], base_size=14, italic=True, color=RGBColor(0x55, 0x55, 0x55))

for _ in range(6):
    doc.add_paragraph()

# Línea divisoria
divider = doc.add_paragraph()
divider.alignment = WD_ALIGN_PARAGRAPH.CENTER
add_horizontal_rule(divider)

doc.add_paragraph()

# Autores
p = doc.add_paragraph()
p.alignment = WD_ALIGN_PARAGRAPH.CENTER
add_inline_runs(p, "Autores", base_size=12, bold=True, color=RGBColor(0x33, 0x33, 0x33))
for autor in FRONT_MATTER["authors"]:
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    add_inline_runs(p, autor, base_size=12)

doc.add_paragraph()

p = doc.add_paragraph()
p.alignment = WD_ALIGN_PARAGRAPH.CENTER
add_inline_runs(p, f"Profesor/a: {FRONT_MATTER['professor']}", base_size=11, italic=True)
p = doc.add_paragraph()
p.alignment = WD_ALIGN_PARAGRAPH.CENTER
add_inline_runs(p, f"Comisión: {FRONT_MATTER['commission']}", base_size=11, italic=True)

for _ in range(4):
    doc.add_paragraph()

p = doc.add_paragraph()
p.alignment = WD_ALIGN_PARAGRAPH.CENTER
add_inline_runs(p, FRONT_MATTER["date"], base_size=11, color=RGBColor(0x55, 0x55, 0x55))

add_page_break(doc)

# ----------------------------------------------------------------------
# Parse markdown
# ----------------------------------------------------------------------
md_text = MD_PATH.read_text(encoding="utf-8")
# strip YAML front matter
md_text = re.sub(r"^---.*?---\s*", "", md_text, count=1, flags=re.DOTALL)

lines = md_text.split("\n")
i = 0
in_code = False
code_buffer = []
code_lang = ""
list_stack = []  # list of (depth, ordered)
in_blockquote = False

# Detectar bloque especial "Cuadro de resultados" (sección 7.2)
resultados_index = -1

def flush_paragraph(text):
    if not text.strip():
        return
    p = doc.add_paragraph()
    p.paragraph_format.first_line_indent = Cm(0.5)
    add_inline_runs(p, text)


def flush_list():
    while list_stack:
        list_stack.pop()


def flush_blockquote():
    pass


while i < len(lines):
    line = lines[i]
    stripped = line.strip()

    # Code block fences
    if stripped.startswith("```"):
        if not in_code:
            in_code = True
            code_lang = stripped[3:].strip()
            code_buffer = []
        else:
            # flush code
            tbl = doc.add_table(rows=1, cols=1)
            tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
            cell = tbl.cell(0, 0)
            shade_cell(cell, "F4F4F4")
            cell.paragraphs[0].text = ""
            run = cell.paragraphs[0].add_run("\n".join(code_buffer))
            run.font.name = "Consolas"
            run.font.size = Pt(9)
            rPr = run._element.get_or_add_rPr()
            rFonts = OxmlElement("w:rFonts")
            rFonts.set(qn("w:ascii"), "Consolas"); rFonts.set(qn("w:hAnsi"), "Consolas")
            rPr.append(rFonts)
            in_code = False
            code_buffer = []
        i += 1
        continue
    if in_code:
        code_buffer.append(line)
        i += 1
        continue

    # Page break antes de secciones principales (heurística: h1)
    if stripped.startswith("# ") and not stripped.startswith("## "):
        # cerrar listas pendientes
        flush_list()
        add_page_break(doc)
        title = stripped[2:].strip()
        # Quitar numeración tipo "1. Introducción" -> "1. Introducción"
        p = doc.add_heading(level=1)
        p.alignment = WD_ALIGN_PARAGRAPH.LEFT
        add_inline_runs(p, title, base_size=22, bold=True, color=RGBColor(0x0D, 0x94, 0x88))
        i += 1
        continue

    if stripped.startswith("## "):
        flush_list()
        title = stripped[3:].strip()
        p = doc.add_heading(level=2)
        add_inline_runs(p, title, base_size=16, bold=True, color=RGBColor(0x14, 0x4B, 0x70))
        i += 1
        continue

    if stripped.startswith("### "):
        flush_list()
        title = stripped[4:].strip()
        p = doc.add_heading(level=3)
        add_inline_runs(p, title, base_size=13, bold=True, color=RGBColor(0x1E, 0x29, 0x3B))
        i += 1
        continue

    if stripped.startswith("#### "):
        flush_list()
        title = stripped[5:].strip()
        p = doc.add_heading(level=4)
        add_inline_runs(p, title, base_size=12, bold=True, color=RGBColor(0x33, 0x33, 0x33))
        i += 1
        continue

    # Tablas Markdown (sencillas): detectar encabezado con |
    if "|" in stripped and i + 1 < len(lines) and re.match(r"^\s*\|?\s*[-: ]+\s*\|", lines[i + 1]):
        flush_list()
        # parse header
        header_cells = [c.strip() for c in stripped.strip("|").split("|")]
        i += 2  # skip separator
        rows = []
        while i < len(lines) and "|" in lines[i] and lines[i].strip():
            rows.append([c.strip() for c in lines[i].strip().strip("|").split("|")])
            i += 1
        # Crear tabla
        tbl = doc.add_table(rows=1 + len(rows), cols=len(header_cells))
        tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        # Header
        for j, txt in enumerate(header_cells):
            cell = tbl.rows[0].cells[j]
            shade_cell(cell, "0D9488")
            cell.paragraphs[0].text = ""
            add_inline_runs(cell.paragraphs[0], txt, base_size=10, bold=True, color=RGBColor(0xFF, 0xFF, 0xFF))
        set_repeat_header(tbl.rows[0])
        # Body
        for r_idx, row in enumerate(rows):
            zebra = r_idx % 2 == 0
            for j, txt in enumerate(row):
                cell = tbl.rows[r_idx + 1].cells[j]
                if zebra:
                    shade_cell(cell, "F1F5F9")
                cell.paragraphs[0].text = ""
                add_inline_runs(cell.paragraphs[0], txt, base_size=10)
        # add some space after table
        doc.add_paragraph()
        continue

    # Imágenes
    img_match = re.match(r"!\[([^\]]*)\]\(([^)]+)\)", stripped)
    if img_match:
        flush_list()
        alt, src = img_match.group(1), img_match.group(2)
        if src.startswith("/") or src.startswith("diagrama/"):
            # resolver relativo
            if src.startswith("/"):
                img_path = ROOT / src.lstrip("/")
            else:
                img_path = ROOT / src
            if img_path.exists():
                p = doc.add_paragraph()
                p.alignment = WD_ALIGN_PARAGRAPH.CENTER
                run = p.add_run()
                run.add_picture(str(img_path), width=Inches(5.8))
                cap = doc.add_paragraph()
                cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
                add_inline_runs(cap, alt, base_size=9, italic=True, color=RGBColor(0x55, 0x55, 0x55))
        i += 1
        continue

    # Blockquote
    if stripped.startswith("> "):
        flush_list()
        text = stripped[2:].strip()
        p = doc.add_paragraph()
        p.paragraph_format.left_indent = Cm(0.6)
        p.paragraph_format.right_indent = Cm(0.6)
        add_inline_runs(p, "“" + text + "”", base_size=11, italic=True, color=RGBColor(0x55, 0x55, 0x55))
        i += 1
        continue

    # Listas
    list_match = re.match(r"^(\s*)([-*]|\d+\.)\s+(.*)", line)
    if list_match:
        indent, marker, content = list_match.groups()
        depth = len(indent) // 2
        ordered = marker.endswith(".") and marker[:-1].isdigit()
        # ajustar stack
        while len(list_stack) > depth + 1:
            list_stack.pop()
        if len(list_stack) <= depth:
            # nuevo nivel (no usado: solo depth=0)
            list_stack.append((depth, ordered))
        # estilo de párrafo
        p = doc.add_paragraph(style="List Bullet" if not ordered else "List Number")
        p.paragraph_format.left_indent = Cm(0.6 + depth * 0.5)
        p.paragraph_format.space_after = Pt(2)
        add_inline_runs(p, content)
        i += 1
        continue
    else:
        flush_list()

    # Línea horizontal
    if re.match(r"^---+$", stripped):
        p = doc.add_paragraph()
        add_horizontal_rule(p)
        i += 1
        continue

    # Párrafo normal
    if stripped:
        # si empieza con "**Nota:**" etc, lo resaltamos
        p = doc.add_paragraph()
        p.paragraph_format.first_line_indent = Cm(0.5)
        add_inline_runs(p, stripped)

    i += 1

# ----------------------------------------------------------------------
# Insertar UML al final de la sección 5
# ----------------------------------------------------------------------
# (El md ya tiene un ascii-art; opcionalmente insertamos el PNG.)
# Buscamos "diagrama/VeterinariaUML" referenciado en el texto y, si existe el PNG,
# lo añadimos como anexo visual al final del documento.
if UML_PNG.exists():
    add_page_break(doc)
    p = doc.add_heading("Diagrama UML de clases (imagen)", level=2)
    add_inline_runs(p, "Diagrama UML de clases (imagen)", base_size=16, bold=True, color=RGBColor(0x14, 0x4B, 0x70))
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = p.add_run()
    run.add_picture(str(UML_PNG), width=Inches(6.5))
    cap = doc.add_paragraph()
    cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
    add_inline_runs(cap, "Figura 1. Diagrama UML completo del modelo de Happy Paws.", base_size=9, italic=True, color=RGBColor(0x55, 0x55, 0x55))

# ----------------------------------------------------------------------
# Save
# ----------------------------------------------------------------------
DOCX_PATH.parent.mkdir(parents=True, exist_ok=True)
doc.save(str(DOCX_PATH))
print(f"OK: {DOCX_PATH}")
print(f"Tamaño: {DOCX_PATH.stat().st_size / 1024:.1f} KB")
