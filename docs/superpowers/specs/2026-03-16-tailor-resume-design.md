# Tailor Resume — Design Spec

**Date:** 2026-03-16
**Status:** Draft
**Project:** kyawminthu20.github.io

---

## Purpose

A two-part workflow for producing a tailored single-column `.docx` resume from a job description:

1. **Claude Code (this session)** — reads the JD and `RAG/kmt_career.yaml`, tailors content, writes a JSON file
2. **Python script** — reads that JSON and builds the `.docx` (no AI, no API key, no cost)

---

## User Flow

```
1. User pastes job description into Claude Code chat

2. Claude reads RAG/kmt_career.yaml, tailors content,
   writes resumes/current.json

3. Claude runs:
   python3 tools/build_resume.py

4. Script reads resumes/current.json → builds .docx
   → Saved: resumes/resume_Amazon_AutomationEngineer_2026-03-16.docx
```

---

## Components

### 1. Claude Code (tailoring step — no script)
- Reads `RAG/kmt_career.yaml` directly
- Reads the pasted job description (or fetches URL if provided)
- Produces tailored resume content matching the JSON schema below
- Writes output to `resumes/current.json`
- Then runs `tools/build_resume.py`

### 2. `resumes/current.json` — intermediate file
Matches this schema:

```json
{
  "company": "string — company name extracted from JD",
  "role": "string — role title extracted from JD",
  "name": "string — full name (from RAG personal section)",
  "email": "string — email address (from RAG personal section)",
  "phone": "string — phone number (from RAG personal section)",
  "location": "string — city/region (from RAG personal section)",
  "summary": "string — 3-4 sentence professional summary tailored to JD",
  "competencies": ["string", "..."],
  "experience": [
    {
      "company": "string",
      "role": "string",
      "dates": "string",
      "bullets": ["string", "..."]
    }
  ],
  "education": [
    {
      "degree": "string",
      "institution": "string",
      "location": "string",
      "dates": "string"
    }
  ]
}
```

- `competencies`: 8–12 items selected and reordered to match JD keywords
- `experience`: all 8 roles included, bullets reweighted toward JD requirements
- `education`: both entries (CSUEB + Singapore Poly)
- All facts sourced from RAG YAML — no hallucinated metrics

### 3. `tools/build_resume.py` — DOCX formatter (no AI)
- Reads `resumes/current.json`
- Builds ATS-safe single-column `.docx` with `python-docx`
- Sections in order:
  1. **Header** — name (16pt bold), email · phone · location on one line (10pt)
  2. **Professional Summary** — plain paragraph, 11pt
  3. **Core Competencies** — bullet list, one item per line
  4. **Professional Experience** — for each role: role title bold + dates, company below, then bullets
  5. **Education** — degree bold, institution and dates below
- Fonts: Calibri throughout — 16pt name, 11pt body/summary/competency bullets, 10pt experience bullets
- Margins: 1 inch all sides
- No tables, no text boxes, no headers/footers (ATS safe)
- Saves to `resumes/resume_<Company>_<Role>_YYYY-MM-DD.docx`
- Filename sanitization: `re.sub(r'[^A-Za-z0-9_-]', '', value.replace(' ', '_'))`, capped at 50 chars per field (company and role independently)
- Fallback filename if company/role missing: `resume_Unknown_Unknown_YYYY-MM-DD.docx`

---

## Dependencies

| Package | Purpose |
|---------|---------|
| `python-docx` | .docx generation |

No `anthropic`, `requests`, `beautifulsoup4`, or `pyyaml` needed — Claude Code handles fetching, YAML parsing, and tailoring directly. The Python script only reads JSON.

---

## File Structure Changes

```
kyawminthu20.github.io/
├── tools/
│   └── build_resume.py        # new — DOCX formatter script
├── resumes/                   # new — output folder
│   ├── .gitkeep
│   └── current.json           # gitignored — overwritten each run
├── pyproject.toml             # new — project deps managed via uv
└── .gitignore                 # updated — add resumes/*.docx and resumes/current.json
                                 # resumes/.gitkeep is committed to track the folder
```

### `pyproject.toml` skeleton

```toml
[project]
name = "kyawminthu-tools"
version = "0.1.0"
requires-python = ">=3.12"
dependencies = [
    "python-docx",
]
```

---

## Error Handling

- `resumes/current.json` missing → print "Run the tailoring step in Claude Code first" and exit
- Invalid JSON → print parse error and exit cleanly
- `resumes/` folder missing → script creates it automatically
- Invalid/empty company or role in JSON → use fallback filename
- Missing or null required fields (`name`, `summary`, `experience`, etc.) → print which field is missing and exit cleanly; do not write a partial .docx
- Empty `experience` list or missing `bullets` key on an entry → print field path and exit

---

## Environment

- Python >= 3.12
- No API key required
- Run via `uv run python3 tools/build_resume.py` or `python3 tools/build_resume.py`

---

## Out of Scope

- CLI argument mode (interactive Claude Code session is the entry point)
- Cover letter generation (separate future task)
- PDF output (print from Word/LibreOffice)
- GUI or web interface
- Automatic LinkedIn or job board integration
