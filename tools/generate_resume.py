"""Generate a one-page PDF resume from RAG/resume_data.yaml.

Content lives in RAG/ (gitignored — carries personal contact details);
this script is layout only.
"""

from pathlib import Path

import yaml
from reportlab.lib.pagesizes import letter
from reportlab.lib.styles import ParagraphStyle
from reportlab.lib.units import inch
from reportlab.lib import colors
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, HRFlowable, Table, TableStyle
)
from reportlab.lib.enums import TA_CENTER, TA_RIGHT

REPO_ROOT = Path(__file__).resolve().parent.parent
DATA_PATH = REPO_ROOT / "RAG" / "resume_data.yaml"
OUTPUT = REPO_ROOT / "Kyaw_Min_Thu_Resume.pdf"

REQUIRED_KEYS = (
    "name", "tagline", "contact_line", "summary",
    "skills", "experience", "education",
)


def load_resume_data(path: Path) -> dict:
    """Load and validate resume content, failing loudly on gaps."""
    if not path.exists():
        raise FileNotFoundError(
            f"Resume data file not found: {path}. RAG/ is local-only — "
            "restore resume_data.yaml from your private backup."
        )
    data = yaml.safe_load(path.read_text(encoding="utf-8"))
    missing = [key for key in REQUIRED_KEYS if not data.get(key)]
    if missing:
        raise ValueError(f"Resume data missing required keys: {', '.join(missing)}")
    for i, entry in enumerate(data["experience"]):
        if not entry.get("bullets"):
            raise ValueError(f"experience[{i}] ({entry.get('role', '?')}) has no bullets")
    return data


# ── Palette ────────────────────────────────────────────────────────────────
DARK   = colors.HexColor("#1a1a2e")
ACCENT = colors.HexColor("#2563eb")
MID    = colors.HexColor("#374151")
LIGHT  = colors.HexColor("#6b7280")
RULE   = colors.HexColor("#d1d5db")

# ── Styles ──────────────────────────────────────────────────────────────────
def S(name, **kw):
    return ParagraphStyle(name, **kw)

NAME   = S("name",    fontSize=20, textColor=DARK,   leading=24, spaceAfter=1)
TAGLINE= S("tagline", fontSize=9,  textColor=ACCENT, leading=12, spaceAfter=2)
CONTACT= S("contact", fontSize=7.5,textColor=LIGHT,  leading=10, alignment=TA_CENTER, spaceAfter=4)
SEC    = S("sec",     fontSize=8,  textColor=ACCENT, leading=10, spaceBefore=6, spaceAfter=2,
           fontName="Helvetica-Bold", textTransform="uppercase", letterSpacing=0.8)
ROLE   = S("role",    fontSize=8.5,textColor=DARK,   leading=11, fontName="Helvetica-Bold")
DATES  = S("dates",   fontSize=7.5,textColor=LIGHT,  leading=11, alignment=TA_RIGHT)
CO     = S("co",      fontSize=8,  textColor=MID,    leading=10, spaceAfter=1)
BULLET = S("bullet",  fontSize=7.5,textColor=MID,    leading=10, leftIndent=8,
           bulletIndent=0, spaceAfter=0.5)
SKILL  = S("skill",   fontSize=7.5,textColor=MID,    leading=10, spaceAfter=2)
EDU    = S("edu",     fontSize=8,  textColor=DARK,   leading=10, fontName="Helvetica-Bold")
EDUSUB = S("edusub",  fontSize=7.5,textColor=LIGHT,  leading=10, spaceAfter=1)

def rule():
    return HRFlowable(width="100%", thickness=0.5, color=RULE, spaceAfter=3, spaceBefore=0)

def section(title):
    return [Paragraph(title, SEC), rule()]

def job(role, company, dates, bullets):
    items = []
    # Role + dates on same line via table
    row = Table(
        [[Paragraph(role, ROLE), Paragraph(dates, DATES)]],
        colWidths=["75%", "25%"],
        style=TableStyle([
            ("VALIGN", (0,0), (-1,-1), "TOP"),
            ("LEFTPADDING",  (0,0), (-1,-1), 0),
            ("RIGHTPADDING", (0,0), (-1,-1), 0),
            ("TOPPADDING",   (0,0), (-1,-1), 0),
            ("BOTTOMPADDING",(0,0), (-1,-1), 0),
        ])
    )
    items.append(row)
    items.append(Paragraph(company, CO))
    for b in bullets:
        items.append(Paragraph(f"• {b}", BULLET))
    items.append(Spacer(1, 3))
    return items

def build(data_path: Path = DATA_PATH) -> None:
    data = load_resume_data(data_path)

    doc = SimpleDocTemplate(
        str(OUTPUT),
        pagesize=letter,
        leftMargin=0.55*inch,
        rightMargin=0.55*inch,
        topMargin=0.45*inch,
        bottomMargin=0.35*inch,
    )

    story = []

    # ── Header ──────────────────────────────────────────────────────────────
    story.append(Paragraph(data["name"], NAME))
    story.append(Paragraph(data["tagline"], TAGLINE))
    story.append(Paragraph(data["contact_line"], CONTACT))
    story.append(rule())

    # ── Summary ─────────────────────────────────────────────────────────────
    story += section("Summary")
    story.append(Paragraph(data["summary"], SKILL))

    # ── Skills ──────────────────────────────────────────────────────────────
    story += section("Technical Skills")

    skills_data = [
        [
            Paragraph(f"<b>{skill['label']}</b>", SKILL),
            Paragraph(skill["text"], SKILL),
        ]
        for skill in data["skills"]
    ]
    skills_table = Table(
        skills_data,
        colWidths=["22%", "78%"],
        style=TableStyle([
            ("VALIGN",       (0,0), (-1,-1), "TOP"),
            ("LEFTPADDING",  (0,0), (-1,-1), 0),
            ("RIGHTPADDING", (0,0), (-1,-1), 4),
            ("TOPPADDING",   (0,0), (-1,-1), 0),
            ("BOTTOMPADDING",(0,0), (-1,-1), 1),
        ])
    )
    story.append(skills_table)

    # ── Experience ───────────────────────────────────────────────────────────
    story += section("Experience")
    for entry in data["experience"]:
        story += job(entry["role"], entry["company"], entry["dates"], entry["bullets"])

    # ── Education ────────────────────────────────────────────────────────────
    story += section("Education")
    for entry in data["education"]:
        t = Table(
            [[Paragraph(entry["degree"], EDU), Paragraph(entry["institution"], EDUSUB)]],
            colWidths=["55%", "45%"],
            style=TableStyle([
                ("VALIGN",       (0,0), (-1,-1), "TOP"),
                ("LEFTPADDING",  (0,0), (-1,-1), 0),
                ("RIGHTPADDING", (0,0), (-1,-1), 4),
                ("TOPPADDING",   (0,0), (-1,-1), 0),
                ("BOTTOMPADDING",(0,0), (-1,-1), 1),
            ])
        )
        story.append(t)

    doc.build(story)
    print(f"✓ Resume written to {OUTPUT}")

if __name__ == "__main__":
    build()
