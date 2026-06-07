"""Genera GUION_DEFENSA.docx a partir de GUION_DEFENSA.md con el mismo estilo del informe."""
import re
from pathlib import Path
from docx import Document
from docx.shared import Pt, Cm, RGBColor, Inches
from docx.enum.text import WD_BREAK
from docx.oxml.ns import qn
from docx.oxml import OxmlElement

ROOT = Path(r"C:\Users\el_be\OneDrive\Desktop\Universidad\desarrollo de software\Tercer cuatrimestre\Programacion Orientada a Objetos\poo_2026")
INFORME_DIR = ROOT / "informe"
MD_PATH = INFORME_DIR / "GUION_DEFENSA.md"
DOCX_PATH = INFORME_DIR / "GUION_DEFENSA_HappyPaws.docx"

doc = Document()
style = doc.styles["Normal"]
style.font.name = "Calibri"
style.font.size = Pt(11)
style.paragraph_format.space_after = Pt(6)
style.paragraph_format.line_spacing = 1.25

for section in doc.sections:
    section.top_margin = Cm(2.0)
    section.bottom_margin = Cm(2.0)
    section.left_margin = Cm(2.5)
    section.right_margin = Cm(2.5)

for level, (size, color) in {1: (20, RGBColor(0x0D, 0x94, 0x88)),
                              2: (14, RGBColor(0x14, 0x4B, 0x70)),
                              3: (12, RGBColor(0x1E, 0x29, 0x3B))}.items():
    h = doc.styles[f"Heading {level}"]
    h.font.name = "Calibri"
    h.font.size = Pt(size)
    h.font.bold = True
    h.font.color.rgb = color


def add_inline_runs(paragraph, text, base_size=11, bold=False, italic=False, mono=False, color=None):
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
            run.text = tok[1:-1]
            run.font.name = "Consolas"
            run.font.size = Pt(10)
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


def shade_cell(cell, fill_hex):
    tc_pr = cell._tc.get_or_add_tcPr()
    shd = OxmlElement("w:shd")
    shd.set(qn("w:val"), "clear")
    shd.set(qn("w:color"), "auto")
    shd.set(qn("w:fill"), fill_hex)
    tc_pr.append(shd)


# Carátula simple
for _ in range(3):
    doc.add_paragraph()
p = doc.add_paragraph()
p.alignment = 1
add_inline_runs(p, "Guion para la defensa oral", base_size=22, bold=True, color=RGBColor(0x0D, 0x94, 0x88))
p = doc.add_paragraph()
p.alignment = 1
add_inline_runs(p, "Happy Paws — Sistema de Gestión Veterinaria", base_size=14, italic=True, color=RGBColor(0x14, 0x4B, 0x70))
for _ in range(2):
    doc.add_paragraph()
p = doc.add_paragraph()
p.alignment = 1
add_inline_runs(p, "Programación Orientada a Objetos — Trabajo Integrador Final", base_size=11, italic=True)
p = doc.add_paragraph()
p.alignment = 1
add_inline_runs(p, "Junio de 2026", base_size=11)

p = doc.add_paragraph()
p.add_run().add_break(WD_BREAK.PAGE)

# Parse markdown
md_text = MD_PATH.read_text(encoding="utf-8")
lines = md_text.split("\n")
i = 0
in_code = False
code_buffer = []
in_quote = False

while i < len(lines):
    line = lines[i]
    stripped = line.strip()

    if stripped.startswith("```"):
        if not in_code:
            in_code = True
            code_buffer = []
        else:
            tbl = doc.add_table(rows=1, cols=1)
            cell = tbl.cell(0, 0)
            shade_cell(cell, "F4F4F4")
            cell.paragraphs[0].text = ""
            for j, ln in enumerate(code_buffer):
                if j > 0:
                    cell.paragraphs[0].add_run().add_break()
                run = cell.paragraphs[0].add_run(ln)
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

    if stripped.startswith("# "):
        title = stripped[2:].strip()
        p = doc.add_heading(level=1)
        add_inline_runs(p, title, base_size=20, bold=True, color=RGBColor(0x0D, 0x94, 0x88))
        i += 1; continue
    if stripped.startswith("## "):
        title = stripped[3:].strip()
        p = doc.add_heading(level=2)
        add_inline_runs(p, title, base_size=14, bold=True, color=RGBColor(0x14, 0x4B, 0x70))
        i += 1; continue
    if stripped.startswith("### "):
        title = stripped[4:].strip()
        p = doc.add_heading(level=3)
        add_inline_runs(p, title, base_size=12, bold=True, color=RGBColor(0x1E, 0x29, 0x3B))
        i += 1; continue

    # Tablas
    if "|" in stripped and i + 1 < len(lines) and re.match(r"^\s*\|?\s*[-: ]+\s*\|", lines[i + 1]):
        header_cells = [c.strip() for c in stripped.strip("|").split("|")]
        i += 2
        rows = []
        while i < len(lines) and "|" in lines[i] and lines[i].strip():
            rows.append([c.strip() for c in lines[i].strip().strip("|").split("|")])
            i += 1
        tbl = doc.add_table(rows=1 + len(rows), cols=len(header_cells))
        for j, txt in enumerate(header_cells):
            cell = tbl.rows[0].cells[j]
            shade_cell(cell, "0D9488")
            cell.paragraphs[0].text = ""
            add_inline_runs(cell.paragraphs[0], txt, base_size=10, bold=True, color=RGBColor(0xFF, 0xFF, 0xFF))
        for r_idx, row in enumerate(rows):
            zebra = r_idx % 2 == 0
            for j, txt in enumerate(row):
                cell = tbl.rows[r_idx + 1].cells[j]
                if zebra:
                    shade_cell(cell, "F1F5F9")
                cell.paragraphs[0].text = ""
                add_inline_runs(cell.paragraphs[0], txt, base_size=10)
        doc.add_paragraph()
        continue

    # Listas
    list_match = re.match(r"^(\s*)([-*]|\d+\.)\s+(.*)", line)
    if list_match:
        indent, marker, content = list_match.groups()
        depth = len(indent) // 2
        ordered = marker.endswith(".") and marker[:-1].isdigit()
        p = doc.add_paragraph(style="List Bullet" if not ordered else "List Number")
        p.paragraph_format.left_indent = Cm(0.6 + depth * 0.5)
        p.paragraph_format.space_after = Pt(2)
        add_inline_runs(p, content)
        i += 1
        continue

    if stripped.startswith("> "):
        text = stripped[2:].strip()
        p = doc.add_paragraph()
        p.paragraph_format.left_indent = Cm(0.6)
        p.paragraph_format.right_indent = Cm(0.6)
        add_inline_runs(p, "“" + text + "”", base_size=11, italic=True, color=RGBColor(0x55, 0x55, 0x55))
        i += 1; continue

    if re.match(r"^---+$", stripped):
        i += 1; continue

    if stripped:
        p = doc.add_paragraph()
        p.paragraph_format.first_line_indent = Cm(0.5)
        add_inline_runs(p, stripped)

    i += 1

DOCX_PATH.parent.mkdir(parents=True, exist_ok=True)
doc.save(str(DOCX_PATH))
print(f"OK: {DOCX_PATH}")
print(f"Tamaño: {DOCX_PATH.stat().st_size / 1024:.1f} KB")
