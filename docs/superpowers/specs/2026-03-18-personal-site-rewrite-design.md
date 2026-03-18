# Personal Site Rewrite — Design Spec

**Date:** 2026-03-18
**Status:** Approved
**Repo:** kyawminthu20.github.io

---

## Goal

Rewrite the personal GitHub Pages site (`index.html`) to present Kyaw Min Thu's 20-year automation controls career in a grounded, factual voice. The current site has inflated copy and inaccurate project entries. This spec defines the content, structure, and tone for the rewrite.

---

## Approach

**Option B — Rewrite `index.html` from `kmt_career.yaml` as source of truth.**

Correct inaccuracies in the YAML first, then generate fresh HTML from it. This keeps YAML and HTML in sync for future updates. Jekyll templates are out of scope — the site stays as static HTML at the repo root.

---

## Visual Direction

Keep the existing dark terminal / SCADA aesthetic:
- Dark navy/black backgrounds
- Green glow accents (`#00e676`)
- Amber highlights (`#ffab00`)
- Monospace type for labels and section IDs
- Scanline overlay
- Section IDs (SYS.00 through SYS.05)

No visual redesign. Only content changes.

---

## Information Architecture

Six sections in this order:

| ID | Section | Notes |
|----|---------|-------|
| SYS.00 | Hero / About | Name, title, one plain sentence, location, contact |
| SYS.01 | Core Competencies | 5 trimmed clusters (down from 8) |
| SYS.02 | Experience | All 8 roles, 3–4 bullets each |
| SYS.03 | Selected Projects | 3 projects only |
| SYS.04 | Education | No changes |
| SYS.05 | Contact | Phone, primary email, location |

---

## Tone

Professional, plain, and factual. No buzzwords or superlatives.

**Remove:** results-driven, leveraged, spearheaded, proven ability, complex, comprehensive, strategic
**Replace with:** plain verb + what was done. Let the work speak.

---

## Section Detail

### SYS.00 — Hero / About

```
Kyaw Min Thu
AUTOMATION CONTROLS ENGINEER

20 years building and commissioning industrial control systems — PLC, SCADA,
and instrumentation across offshore drilling, R&D, manufacturing, and warehouse
automation. Based in the San Francisco Bay Area.
```

Contact details inline: phone, email, location.

---

### SYS.01 — Core Competencies

5 clusters only:

| Cluster | Items |
|---------|-------|
| PLC Platforms | Rockwell ControlLogix, Siemens S7-300/1500, Mitsubishi FX, Omron |
| SCADA / HMI | Ignition Vision & Perspective, FactoryTalk View, LabVIEW |
| Protocols | OPC UA, Modbus TCP, Ethernet/IP, MQTT, SECS/GEM |
| Programming | Python, SQL, C, Ladder Logic, ST, FBD |
| Hardware | Fanuc Robotics, VFDs, Servo Drives, NI CompactDAQ, Safety circuits |

Remove: Soft Skills cluster, Dev Tools cluster, Databases cluster (these appear naturally in experience entries).

---

### SYS.02 — Experience

All 8 roles, reverse chronological. 3–4 bullets per role. Plain verb at the start of each bullet.

**Roles (newest first):**
1. Automation Engineer / Controls Lead — C&W Services / JLL (Amazon), 2025–present
2. Electrical, Instrumentation & Controls Engineer — Energy Recovery Inc., 2018–2025
3. Maintenance Technician (Automation & Controls) — Tesla / SolarCity, 2015–2018
4. Field Service Engineer — MHWirth, 2012–2015
5. Tooling Center Technician — GE International, 2010–2012
6. System Engineer — Hexcel Solutions, 2009–2010
7. Associate Engineer (CIM) — Chartered Semiconductor, 2005–2008
8. Engineering Assistant — UTAC, 2004–2005

**Bullet selection guideline:** For each role, select 3–4 responsibilities from the YAML `responsibilities` list that best represent the distinct work done. Prefer bullets that mention specific technologies or concrete outcomes. Apply the tone rules (plain verb, no buzzwords) to each selected bullet. Do not carry over bullets wholesale — rewrite for concision.

**Exception for early/short roles:** GE International (`exp-ge`) has only 2 source bullets in the YAML; UTAC (`exp-utac`) has 3. For these roles, use all available bullets rather than padding to 4. 2 bullets is acceptable for roles with limited YAML source data.

**Amazon bullet guideline (approved sample — use these verbatim for role 1):**
- Controls lead for Rockwell ControlLogix PLCs across conveyor and sortation systems in 24/7 fulfillment centers.
- Primary Fanuc robot technical lead — troubleshooting, system recovery, and commissioning support.
- Built Python monitoring tools for fault trending and downtime analysis.
- Commissioned a conveyor inspection system integrating sensors, PLC logic, and HMI diagnostics.

---

### SYS.03 — Selected Projects

**3 projects only:**

**1. Full PLC Conversion — Hydraulic Top Drive, Offshore Drilling (MHWirth)**
Led a complete control system conversion for an offshore drilling top drive from Mitsubishi PLC to Siemens S7. Covered the full project lifecycle — architecture, I/O mapping, interlock logic, ATEX-certified cabinet fabrication, and on-site commissioning. Tuned hydraulic PID loops under live load conditions in a hazardous-area environment.
Tags: Siemens S7 · TIA Portal · PID Control · ATEX · Hydraulics

**2. High-Pressure Test Rig Automation — Water & Wastewater R&D (Energy Recovery Inc.)**
Built and commissioned automation systems for high-pressure R&D test rigs from concept through operational use. Integrated Allen-Bradley PLCs, NI CompactDAQ, and Ignition SCADA for pump, valve, and instrumentation control. Designed electrical schematics and safety interlocks. Used Python and SQL for data acquisition and experiment traceability.
Tags: Allen-Bradley · NI CompactDAQ · Ignition · Python · SQL · Safety interlocks

**3. PLC & IPC Control Monitoring — Fulfillment Center Automation (Amazon / C&W Services)**
Built monitoring dashboards reading live PLC and IPC data across conveyor and sortation systems in 24/7 fulfillment centers. Covered commissioning and maintenance of control panels and IPCs. Developed Python-based tools for fault trending and downtime analysis.
Tags: Rockwell ControlLogix · FactoryTalk · Python · IPC

---

### SYS.04 — Education

Copy from YAML (`education` entries). No rewrites needed. Display as:
- BS Computer Science — California State University, East Bay (2024)
- Diploma in Electronics, Computers & Communication Engineering — Singapore Polytechnic (2004)

---

### SYS.05 — Contact

Display directly (no contact form):
- Phone: +1 (510) 909-3716
- Email: kyaw@kmtkn.me
- Location: San Francisco Bay Area, CA

---

## YAML Corrections Required

Before regenerating the HTML, update `RAG/kmt_career.yaml`:

1. **`proj-amazon-scada`** — rename id to `proj-amazon-plc-ipc` first (prerequisite for the mapping in step 4), then update:
   - `title` → "PLC & IPC Control Monitoring — Amazon Fulfillment Centers"
   - `type` → "Control Monitoring"
   - `description` → dashboard monitoring of live PLC/IPC data + commissioning and maintenance of control panels and IPCs
   - `tags` → remove "SCADA" and "mentorship", keep/add: "PLC", "IPC", "control monitoring", "material handling", "Amazon"

2. **`proj-siemens-upgrade`** — update display title to "Full PLC Conversion — Hydraulic Top Drive, Offshore Drilling" (the YAML title reads "Full Automation Conversion — Siemens PLC Upgrade (Offshore Drilling)"). Update `title` field to match the spec's display title.

3. **`summary.core_identity`** — rewrite to: "20 years building and commissioning industrial control systems — PLC, SCADA, and instrumentation across offshore drilling, R&D, manufacturing, and warehouse automation. Based in the San Francisco Bay Area."

4. **Featured projects for the site** (explicit YAML ID mapping):
   - `proj-siemens-upgrade` → Project 1
   - `proj-rd-test-rig` → Project 2
   - `proj-amazon-plc-ipc` → Project 3
   - All others (`proj-conveyor-inspection`, `proj-leviathan`, `proj-python-monitoring`, `proj-firefighter`) remain in YAML as reference data but do not appear in the site's project section.

---

## GitHub Pages Deployment

- Publish from repo root (`index.html` at `/`)
- `.nojekyll` file keeps Jekyll disabled
- `_config.yml` provides metadata only
- No `docs/` subdirectory needed
- No GitHub Actions workflow needed for static HTML

---

## `<head>` Metadata

Update the `<head>` section to match the new tone:
- `<title>` — keep as-is: "Kyaw Min Thu | Automation Controls Engineer"
- `<meta name="description">` — rewrite to: "Kyaw Min Thu — Automation Controls Engineer with 20 years of experience in PLC, SCADA, and industrial control systems. Bay Area, CA."
- Open Graph and Twitter Card descriptions — update to match the new meta description
- JSON-LD structured data — update `description` field if present; keep all other structured data fields as-is

---

## Out of Scope

- Jekyll templates
- Contact form
- Blog or additional pages
- Android coursework portfolio section
- Any changes to the visual/CSS design
