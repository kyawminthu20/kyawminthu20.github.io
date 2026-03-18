# Personal Site Rewrite Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Rewrite `index.html` and correct `RAG/kmt_career.yaml` to present the career in a plain, factual voice with accurate project entries and a trimmed skills grid.

**Architecture:** Targeted edits to the existing single-page HTML file (CSS/JS untouched) and YAML career data file. No new files created. YAML corrected first, then HTML updated section by section.

**Tech Stack:** Static HTML, YAML. No build step. GitHub Pages serves `index.html` at repo root.

**Spec:** `docs/superpowers/specs/2026-03-18-personal-site-rewrite-design.md`

---

## Chunk 1: YAML Corrections

### Task 1: Correct Amazon project entry in YAML

**Files:**
- Modify: `RAG/kmt_career.yaml:447-525`

- [ ] **Step 1: Verify current state**

```bash
grep -n "proj-amazon-scada\|SCADA Optimization\|mentorship" RAG/kmt_career.yaml
```

Expected: lines with `id: "proj-amazon-scada"`, `type: "SCADA Optimization"`, `"mentorship"` tag

- [ ] **Step 2: Replace the Amazon project block**

In `RAG/kmt_career.yaml`, find and replace the `proj-amazon-scada` entry. Change:

```yaml
  - id: "proj-amazon-scada"
    title: "Material Handling & SCADA Optimization — Amazon / JLL"
    company: "C&W Services / JLL (Amazon)"
    industry: "Warehouse / Material Handling"
    type: "SCADA Optimization"
    description: >
      Lead automation engineer for Amazon Fulfillment Centers, supporting Rockwell
      PLCs, FactoryTalk SCADA, and Ignition-based visualization. Perform root cause
      analysis and troubleshoot complex PLC/HMI logic across conveyors, sorters, and
      robotic systems. Standardize Ignition HMI templates, manage version control
      with GitLab, and mentor technicians.
    outcomes:
      - "Improved throughput and reliability"
      - "Standardized HMI templates across site"
      - "Python-based fault trending and downtime analysis"
    technologies:
      - "Rockwell ControlLogix"
      - "FactoryTalk View"
      - "Ignition (Vision & Perspective)"
      - "Fanuc Robotics"
      - "Python"
      - "GitLab"
    tags:
      - "warehouse"
      - "material handling"
      - "SCADA"
      - "mentorship"
      - "Amazon"
```

With:

```yaml
  - id: "proj-amazon-plc-ipc"
    title: "PLC & IPC Control Monitoring — Amazon Fulfillment Centers"
    company: "C&W Services / JLL (Amazon)"
    industry: "Warehouse / Material Handling"
    type: "Control Monitoring"
    description: >
      Built monitoring dashboards reading live PLC and IPC data across conveyor
      and sortation systems in 24/7 fulfillment centers. Covered commissioning
      and maintenance of control panels and IPCs. Developed Python-based tools
      for fault trending and downtime analysis.
    outcomes:
      - "Improved fault visibility and downtime analysis"
      - "Commissioned and maintained control panels and IPCs"
      - "Python-based monitoring tools deployed to production"
    technologies:
      - "Rockwell ControlLogix"
      - "FactoryTalk"
      - "Python"
      - "IPC"
    tags:
      - "warehouse"
      - "material handling"
      - "PLC"
      - "IPC"
      - "control monitoring"
      - "Amazon"
```

- [ ] **Step 3: Verify the change**

```bash
grep -n "proj-amazon-plc-ipc\|Control Monitoring\|proj-amazon-scada\|SCADA Optimization" RAG/kmt_career.yaml
```

Expected: `proj-amazon-plc-ipc` and `Control Monitoring` found; `proj-amazon-scada` and `SCADA Optimization` NOT found

---

### Task 2: Correct MHWirth project title and summary core_identity

**Files:**
- Modify: `RAG/kmt_career.yaml:38-45` (summary), `RAG/kmt_career.yaml:390-415` (proj-siemens-upgrade)

- [ ] **Step 1: Update MHWirth project title**

In `RAG/kmt_career.yaml`, find:

```yaml
    title: "Full Automation Conversion — Siemens PLC Upgrade (Offshore Drilling)"
```

Replace with:

```yaml
    title: "Full PLC Conversion — Hydraulic Top Drive, Offshore Drilling"
```

- [ ] **Step 2: Rewrite core_identity**

Find:

```yaml
  core_identity: >
    Results-driven Automation Controls Engineer with over 20 years of
    experience in control systems, industrial automation, and electrical
    engineering. Skilled in PLC/SCADA design, Ignition development, and
    complex system integration across manufacturing, R&D, and material
    handling environments. Proven ability to lead full project lifecycles,
    mentor engineering teams, and implement automation strategies that
    improve system uptime, safety, and reliability.
```

Replace with:

```yaml
  core_identity: >
    20 years building and commissioning industrial control systems — PLC,
    SCADA, and instrumentation across offshore drilling, R&D, manufacturing,
    and warehouse automation. Based in the San Francisco Bay Area.
```

- [ ] **Step 3: Verify both changes**

```bash
grep -n "Results-driven\|Full Automation Conversion\|Full PLC Conversion\|20 years building" RAG/kmt_career.yaml
```

Expected: `Full PLC Conversion` and `20 years building` found; `Results-driven` and `Full Automation Conversion` NOT found

- [ ] **Step 4: Commit YAML corrections**

```bash
git add RAG/kmt_career.yaml
git commit -m "fix: correct amazon project entry and rewrite career summary in YAML"
```

---

## Chunk 2: HTML — Head, Hero, Competencies

### Task 3: Update `<head>` metadata

**Files:**
- Modify: `index.html:1-50` (meta description, OG tags, Twitter card, JSON-LD)

- [ ] **Step 1: Update meta description**

Find (around line 7):
```html
<meta name="description" content="Kyaw Min Thu — Automation Controls Engineer with 20+ years in PLC/SCADA, industrial automation, and control system design. Based in the San Francisco Bay Area.">
```

Replace with:
```html
<meta name="description" content="Kyaw Min Thu — Automation Controls Engineer with 20 years of experience in PLC, SCADA, and industrial control systems. Bay Area, CA.">
```

- [ ] **Step 2: Update Open Graph description**

Find:
```html
<meta property="og:description" content="Automation Controls Engineer with 20+ years in PLC/SCADA, industrial automation, and control system design. Bay Area, CA.">
```

Replace with:
```html
<meta property="og:description" content="Automation Controls Engineer with 20 years of experience in PLC, SCADA, and industrial control systems. Bay Area, CA.">
```

- [ ] **Step 3: Update Twitter Card description**

Find:
```html
<meta name="twitter:description" content="Automation Controls Engineer with 20+ years in PLC/SCADA, industrial automation, and control system design.">
```

Replace with:
```html
<meta name="twitter:description" content="Automation Controls Engineer with 20 years of experience in PLC, SCADA, and industrial control systems.">
```

- [ ] **Step 4: Add JSON-LD description field**

The current JSON-LD block (lines 26–41) has no `description` field. Add one after `"jobTitle"`:

Find:
```json
  "jobTitle": "Automation Controls Engineer",
  "email": "kyaw@kmtkn.me",
```

Replace with:
```json
  "jobTitle": "Automation Controls Engineer",
  "description": "Automation Controls Engineer with 20 years of experience in PLC, SCADA, and industrial control systems. Bay Area, CA.",
  "email": "kyaw@kmtkn.me",
```

- [ ] **Step 5: Verify no inflated meta copy remains**

```bash
grep -n "20+ years\|industrial automation, and control system design" index.html | head -20
```

Expected: no matches

---

### Task 4: Rewrite hero section (SYS.00)

**Files:**
- Modify: `index.html:909-955`

- [ ] **Step 1: Replace hero summary paragraph**

Find (lines 914–918):
```html
      <p class="hero-summary">
        Results-driven engineer with over 20 years building, programming, and commissioning industrial control systems.
        From offshore drilling rigs to Amazon fulfillment centers, I deliver full-lifecycle automation solutions —
        PLC logic, SCADA interfaces, safety interlocks, and data systems — that improve uptime, safety, and reliability.
      </p>
```

Replace with:
```html
      <p class="hero-summary">
        20 years building and commissioning industrial control systems — PLC, SCADA, and instrumentation
        across offshore drilling, R&D, manufacturing, and warehouse automation.
      </p>
```

- [ ] **Step 2: Verify buzzwords removed from hero**

```bash
grep -n "Results-driven\|full-lifecycle\|improve uptime" index.html
```

Expected: no matches

- [ ] **Step 3: Verify hero summary present**

```bash
grep -n "20 years building and commissioning" index.html
```

Expected: 1 match

---

### Task 5: Rewrite Core Competencies section (SYS.01)

**Files:**
- Modify: `index.html:959-975`

- [ ] **Step 1: Replace the competency grid**

Find (lines 965–974):
```html
  <div class="competency-grid">
    <div class="competency-card"><div class="comp-indicator"></div><div class="comp-text">Control System Development — Design, Installation & Commissioning</div></div>
    <div class="competency-card"><div class="comp-indicator"></div><div class="comp-text">PLC Programming — Siemens S7, Allen-Bradley ControlLogix, Mitsubishi FX</div></div>
    <div class="competency-card"><div class="comp-indicator"></div><div class="comp-text">SCADA & HMI — Ignition (Vision & Perspective), FactoryTalk, LabVIEW</div></div>
    <div class="competency-card"><div class="comp-indicator"></div><div class="comp-text">Protocols — OPC UA, Modbus TCP, Ethernet/IP, MQTT, SECS/GEM</div></div>
    <div class="competency-card"><div class="comp-indicator"></div><div class="comp-text">System Architecture, Electrical Design & Safety Compliance (ATEX)</div></div>
    <div class="competency-card"><div class="comp-indicator"></div><div class="comp-text">Database & Historian — PostgreSQL, MySQL, Ignition Historian</div></div>
    <div class="competency-card"><div class="comp-indicator"></div><div class="comp-text">Version Control (GitLab), Documentation & Team Mentorship</div></div>
    <div class="competency-card"><div class="comp-indicator"></div><div class="comp-text">Fanuc Robotics — Troubleshooting, Commissioning & System Recovery</div></div>
  </div>
```

Replace with:
```html
  <div class="competency-grid">
    <div class="competency-card"><div class="comp-indicator"></div><div class="comp-text">PLC Platforms — Rockwell ControlLogix, Siemens S7-300/1500, Mitsubishi FX, Omron</div></div>
    <div class="competency-card"><div class="comp-indicator"></div><div class="comp-text">SCADA / HMI — Ignition Vision & Perspective, FactoryTalk View, LabVIEW</div></div>
    <div class="competency-card"><div class="comp-indicator"></div><div class="comp-text">Protocols — OPC UA, Modbus TCP, Ethernet/IP, MQTT, SECS/GEM</div></div>
    <div class="competency-card"><div class="comp-indicator"></div><div class="comp-text">Programming — Python, SQL, C, Ladder Logic, Structured Text, FBD</div></div>
    <div class="competency-card"><div class="comp-indicator"></div><div class="comp-text">Hardware — Fanuc Robotics, VFDs, Servo Drives, NI CompactDAQ, Safety circuits</div></div>
  </div>
```

- [ ] **Step 2: Verify 5 cards, not 8**

```bash
grep -c "competency-card" index.html
```

Expected: `5`

- [ ] **Step 3: Verify removed clusters are gone**

```bash
grep -n "Mentorship\|Database & Historian\|Version Control" index.html
```

Expected: no matches in competency section (CSS classes are fine — check only lines inside `<section id="competencies">`)

- [ ] **Step 4: Commit head + hero + competencies**

```bash
git add index.html
git commit -m "content: update head metadata, hero copy, and competencies section"
```

---

## Chunk 3: HTML — Experience, Projects, Verification

### Task 6: Trim Amazon experience bullets (SYS.02, role 1)

**Files:**
- Modify: `index.html:994-1003`

- [ ] **Step 1: Replace Amazon bullets**

Find (lines 994–1002):
```html
          <li>Lead controls engineer for high-speed material handling systems in 24/7 Amazon warehouses.</li>
          <li>Maintain Rockwell ControlLogix PLCs on live conveyor and sortation systems.</li>
          <li>Support and maintain Ignition SCADA and FactoryTalk HMI dashboards.</li>
          <li>Main Fanuc robot technical lead onsite for troubleshooting and system recovery.</li>
          <li>Commissioned a conveyor visual inspection system integrating sensors, PLC logic, and HMI diagnostics.</li>
          <li>Developed Python-based maintenance monitoring programs for fault trending and downtime analysis.</li>
          <li>Standardize Ignition templates, manage version control with GitLab, and mentor technicians.</li>
```

Replace with:
```html
          <li>Controls lead for Rockwell ControlLogix PLCs across conveyor and sortation systems in 24/7 fulfillment centers.</li>
          <li>Primary Fanuc robot technical lead — troubleshooting, system recovery, and commissioning support.</li>
          <li>Built Python monitoring tools for fault trending and downtime analysis.</li>
          <li>Commissioned a conveyor inspection system integrating sensors, PLC logic, and HMI diagnostics.</li>
```

- [ ] **Step 2: Verify 4 bullets, not 7**

```bash
awk '/C&W Services \/ JLL/,/timeline-expand/' index.html | grep -c "<li>"
```

Expected: `4`

---

### Task 7: Rewrite Projects section (SYS.03)

**Files:**
- Modify: `index.html:1136-1216`

- [ ] **Step 1: Replace the full projects grid**

Find (lines 1142–1215, from `<div class="projects-grid">` to closing `</div>` before `</section>`):

```html
  <div class="projects-grid">

    <div class="project-card">
      <div class="project-tag">Offshore · Controls Conversion</div>
      <div class="project-title">Siemens PLC Upgrade — Hydraulic Top Drive</div>
      <div class="project-stat"><span class="arrow">↑</span> 30% system uptime improvement</div>
      <div class="project-desc">
        Led a complete control system conversion from Mitsubishi PLC to Siemens S7 for an offshore drilling rig's hydraulic top drive.
        Managed architecture, I/O mapping, ATEX-certified cabinet fabrication, and on-site commissioning. Tuned hydraulic PID loops
        under live load. Achieved 100% functional equivalence and reduced maintenance time by 30%.
      </div>
      <div class="project-techs">
        <span class="tech-tag">Siemens S7</span>
        <span class="tech-tag">TIA Portal</span>
        <span class="tech-tag">PID Control</span>
        <span class="tech-tag">ATEX</span>
        <span class="tech-tag">Hydraulics</span>
      </div>
    </div>

    <div class="project-card">
      <div class="project-tag">R&D · Test Automation</div>
      <div class="project-title">High-Pressure R&D Test Rig Automation</div>
      <div class="project-stat"><span class="arrow">↑</span> Enhanced data quality & repeatability</div>
      <div class="project-desc">
        Developed and commissioned automation test rigs for high-pressure energy recovery testing in water and wastewater sectors.
        Integrated Allen-Bradley PLCs, NI CompactDAQ, and Ignition SCADA. Designed electrical schematics, safety interlocks,
        and Python/SQL-based data acquisition for full traceability.
      </div>
      <div class="project-techs">
        <span class="tech-tag">Allen-Bradley</span>
        <span class="tech-tag">NI DAQ</span>
        <span class="tech-tag">Ignition</span>
        <span class="tech-tag">Python</span>
        <span class="tech-tag">SQL</span>
      </div>
    </div>

    <div class="project-card">
      <div class="project-tag">Warehouse · SCADA</div>
      <div class="project-title">Material Handling & SCADA Optimization — Amazon</div>
      <div class="project-stat"><span class="arrow">↑</span> Improved throughput & reliability</div>
      <div class="project-desc">
        Lead automation engineer for Amazon Fulfillment Centers supporting Rockwell PLCs, FactoryTalk SCADA, and
        Ignition-based visualization. Root cause analysis across conveyors, sorters, and robotic systems. Standardized
        Ignition HMI templates and Python-based fault trending programs.
      </div>
      <div class="project-techs">
        <span class="tech-tag">Rockwell</span>
        <span class="tech-tag">FactoryTalk</span>
        <span class="tech-tag">Ignition</span>
        <span class="tech-tag">Fanuc</span>
        <span class="tech-tag">GitLab</span>
      </div>
    </div>
```

Replace with:
```html
  <div class="projects-grid">

    <div class="project-card">
      <div class="project-tag">Offshore · Controls Conversion</div>
      <div class="project-title">Full PLC Conversion — Hydraulic Top Drive, Offshore Drilling</div>
      <div class="project-desc">
        Led a complete control system conversion for an offshore drilling top drive from Mitsubishi PLC to Siemens S7.
        Covered the full project lifecycle — architecture, I/O mapping, interlock logic, ATEX-certified cabinet fabrication,
        and on-site commissioning. Tuned hydraulic PID loops under live load conditions in a hazardous-area environment.
      </div>
      <div class="project-techs">
        <span class="tech-tag">Siemens S7</span>
        <span class="tech-tag">TIA Portal</span>
        <span class="tech-tag">PID Control</span>
        <span class="tech-tag">ATEX</span>
        <span class="tech-tag">Hydraulics</span>
      </div>
    </div>

    <div class="project-card">
      <div class="project-tag">R&D · Test Automation</div>
      <div class="project-title">High-Pressure Test Rig Automation — Water &amp; Wastewater R&amp;D</div>
      <div class="project-desc">
        Built and commissioned automation systems for high-pressure R&amp;D test rigs from concept through operational use.
        Integrated Allen-Bradley PLCs, NI CompactDAQ, and Ignition SCADA for pump, valve, and instrumentation control.
        Designed electrical schematics and safety interlocks. Used Python and SQL for data acquisition and experiment traceability.
      </div>
      <div class="project-techs">
        <span class="tech-tag">Allen-Bradley</span>
        <span class="tech-tag">NI CompactDAQ</span>
        <span class="tech-tag">Ignition</span>
        <span class="tech-tag">Python</span>
        <span class="tech-tag">SQL</span>
        <span class="tech-tag">Safety interlocks</span>
      </div>
    </div>

    <div class="project-card">
      <div class="project-tag">Warehouse · Control Monitoring</div>
      <div class="project-title">PLC &amp; IPC Control Monitoring — Amazon Fulfillment Centers</div>
      <div class="project-desc">
        Built monitoring dashboards reading live PLC and IPC data across conveyor and sortation systems in 24/7 fulfillment centers.
        Covered commissioning and maintenance of control panels and IPCs. Developed Python-based tools for fault trending
        and downtime analysis.
      </div>
      <div class="project-techs">
        <span class="tech-tag">Rockwell ControlLogix</span>
        <span class="tech-tag">FactoryTalk</span>
        <span class="tech-tag">Python</span>
        <span class="tech-tag">IPC</span>
      </div>
    </div>
```

- [ ] **Step 2: Remove the firefighter project card**

After replacing the 3 cards in Step 1, the firefighter card (lines 1198–1213 in the original) still remains in the grid. Delete only this block — from its opening `<div class="project-card">` through its closing `</div>`, leaving the grid's own closing `</div>` (line 1215 originally) intact:

```html
    <div class="project-card">
      <div class="project-tag">Safety · Training Simulator</div>
      <div class="project-title">Automated Firefighter Training System — SCDA</div>
      <div class="project-stat"><span class="arrow">↑</span> Improved safety &amp; reliability in live training</div>
      <div class="project-desc">
        Designed and commissioned a PLC/SCADA-based firefighter training simulator for the Singapore Civil Defence Academy.
        Integrated hydraulics, motion control, fire suppression, LPG and kerosene control systems. Developed alarm management,
        data logging, and automated safety sequences.
      </div>
      <div class="project-techs">
        <span class="tech-tag">Siemens PLC</span>
        <span class="tech-tag">SCADA</span>
        <span class="tech-tag">Hydraulics</span>
        <span class="tech-tag">Motion Control</span>
      </div>
    </div>
```

The line `  </div>` that closes the `projects-grid` div, followed by `</section>`, must remain.

- [ ] **Step 3: Verify exactly 3 project cards (HTML only, not CSS)**

```bash
grep -c '<div class="project-card">' index.html
```

Expected: `3` (CSS selectors use `.project-card` without quotes, so this targets only HTML elements)

- [ ] **Step 4: Verify Amazon project tag corrected**

```bash
grep -n "Warehouse · SCADA\|Material Handling & SCADA Optimization\|Control Monitoring" index.html
```

Expected: `Control Monitoring` found; `Warehouse · SCADA` and `Material Handling & SCADA Optimization` NOT found

---

### Task 8: Final verification

- [ ] **Step 1: Check no buzzwords remain in visible content**

```bash
grep -in "results-driven\|leveraged\|spearheaded\|proven ability\|comprehensive\|strategically" index.html
```

Expected: no matches

- [ ] **Step 2: Verify all 6 section IDs present**

```bash
grep -n 'id="about"\|id="competencies"\|id="experience"\|id="projects"\|id="education"\|id="contact"' index.html
```

Expected: 6 matches, one per section

- [ ] **Step 3: Verify contact details present**

```bash
grep -n "510.*909\|kyaw@kmtkn.me\|Bay Area" index.html
```

Expected: at least one match per item

- [ ] **Step 4: Open in browser and do a visual check**

```bash
open index.html
```

Walk through each section: hero → competencies → experience → projects → education → contact. Confirm layout intact, no broken sections.

- [ ] **Step 5: Commit final HTML changes**

```bash
git add index.html
git commit -m "content: rewrite experience bullets, correct projects section, final site content update"
```

- [ ] **Step 6: Update project_state files**

Update `project_state/project_state.md`:
- Change phase from "Phase 5 — Ready to Deploy" to "Phase 5 — Deployed (content complete)"
- Update current reality to reflect index.html has been rewritten
- Update active priorities to reflect next step is publishing/making repo public

Update `project_state/change_log.md`:
- Add entry for 2026-03-18: site content rewrite completed — hero, competencies, experience, projects corrected

```bash
git add project_state/
git commit -m "docs: update project state after site content rewrite"
```
