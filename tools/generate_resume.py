"""Generate a one-page PDF resume for Kyaw Min Thu."""

from reportlab.lib.pagesizes import letter
from reportlab.lib.styles import ParagraphStyle
from reportlab.lib.units import inch
from reportlab.lib import colors
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, HRFlowable, Table, TableStyle
)
from reportlab.lib.enums import TA_LEFT, TA_CENTER, TA_RIGHT

OUTPUT = "Kyaw_Min_Thu_Resume.pdf"

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

def build():
    doc = SimpleDocTemplate(
        OUTPUT,
        pagesize=letter,
        leftMargin=0.55*inch,
        rightMargin=0.55*inch,
        topMargin=0.45*inch,
        bottomMargin=0.35*inch,
    )

    story = []

    # ── Header ──────────────────────────────────────────────────────────────
    story.append(Paragraph("Kyaw Min Thu", NAME))
    story.append(Paragraph("Automation Controls Engineer", TAGLINE))
    story.append(Paragraph(
        "kyaw@kmtkn.me  ·  +1 555 010 1234  ·  San Francisco Bay Area, CA  ·  kyawminthu20.github.io",
        CONTACT
    ))
    story.append(rule())

    # ── Summary ─────────────────────────────────────────────────────────────
    story += section("Summary")
    story.append(Paragraph(
        "Controls engineer with 20+ years across offshore drilling, semiconductor fabs, EV/solar manufacturing, "
        "high-pressure R&D, and large-scale fulfillment automation. Fluent in Rockwell, Siemens, and Mitsubishi "
        "PLC platforms; bridges hardware-level commissioning with Python-based data tooling.",
        SKILL
    ))

    # ── Skills ──────────────────────────────────────────────────────────────
    story += section("Technical Skills")

    skills_data = [
        [
            Paragraph("<b>PLC / Control Platforms</b>", SKILL),
            Paragraph("Rockwell ControlLogix / CompactLogix, Siemens S7-300/1500, Mitsubishi FX, Omron", SKILL),
        ],
        [
            Paragraph("<b>Programming</b>", SKILL),
            Paragraph("Ladder Logic, Structured Text, FBD  ·  Python, SQL, C  ·  LabVIEW", SKILL),
        ],
        [
            Paragraph("<b>Systems & Protocols</b>", SKILL),
            Paragraph("VFDs, Servo Drives, Fanuc Robotics, NI CompactDAQ  ·  EtherNet/IP, Modbus TCP, OPC UA, MQTT", SKILL),
        ],
        [
            Paragraph("<b>Other</b>", SKILL),
            Paragraph("Electrical schematics, panel fabrication, safety interlocks, ATEX, SCADA/HMI, data acquisition", SKILL),
        ],
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

    story += job(
        "Automation Engineer — Controls Lead",
        "C&W Services / JLL  ·  Amazon Fulfillment Centers",
        "Mar 2025 – Present",
        [
            "Controls lead for Rockwell ControlLogix PLCs on conveyor and sortation systems across Amazon FC sites.",
            "Primary technical lead for Fanuc robot troubleshooting, commissioning, and program modifications.",
            "Built Python fault-trending tools to identify downtime patterns and reduce repeat failures.",
            "Commissioned conveyor inspection system integrating sensors, PLC logic, and HMI diagnostics.",
        ]
    )

    story += job(
        "Electrical Technician — Automation & Controls",
        "Energy Recovery Inc.",
        "Mar 2018 – Feb 2025",
        [
            "Designed NI CompactDAQ / LabVIEW control systems for R&D high-pressure pump and valve test rigs.",
            "Programmed Allen-Bradley PLCs (ControlLogix, CompactLogix); integrated Keyence vision into test workflows.",
            "Full lifecycle ownership: schematics, BOM, panel build, safety interlocks, installation, commissioning.",
            "Python + SQL data acquisition pipelines for experiment traceability and regulatory reporting.",
        ]
    )

    story += job(
        "Maintenance Technician — Automation & Controls",
        "Tesla Motors / SolarCity",
        "Oct 2015 – Mar 2018",
        [
            "Supported high-volume EV and solar manufacturing automation (Fanuc robotics, Rockwell PLCs).",
            "Data-driven MTBF improvement programs; modified control logic for production startups and line expansions.",
        ]
    )

    story += job(
        "Field Service Engineer — Controls & Automation",
        "MHWirth (Singapore) Pte. Ltd.",
        "Apr 2012 – Apr 2015",
        [
            "Led full PLC conversion of offshore drilling top drive (Mitsubishi → Siemens S7): I/O mapping, "
            "interlock logic, ATEX cabinet fabrication, and offshore commissioning.",
            "Tuned hydraulic PID loops and verified performance under live load conditions on active rigs.",
        ]
    )

    story += job(
        "System Engineer",
        "Hexcel Solutions Pte. Ltd.",
        "May 2009 – Oct 2010",
        [
            "Designed and commissioned automated firefighter training systems for Singapore Civil Defence Academy; "
            "PLC programming and SCADA for fire, gas, and hydraulic control.",
        ]
    )

    story += job(
        "Associate Engineer — Computer Integrated Manufacturing",
        "Chartered Semiconductor Manufacturing",
        "Jun 2005 – Dec 2008",
        [
            "Maintained CIM systems (IBM SiView) and factory automation data platforms in a 300mm wafer fab.",
        ]
    )

    # ── Education ────────────────────────────────────────────────────────────
    story += section("Education")

    edu_data = [
        [
            Paragraph("BS Computer Science", EDU),
            Paragraph("California State University, East Bay", EDUSUB),
        ],
        [
            Paragraph("Diploma — Electronics, Computers & Communications Engineering", EDU),
            Paragraph("Singapore Polytechnic  ·  Apr 2004", EDUSUB),
        ],
    ]
    for row in edu_data:
        t = Table(
            [row],
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
