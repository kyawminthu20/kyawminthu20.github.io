# Tailor Resume — Design Spec

**Date:** 2026-03-16
**Status:** Approved by user
**Project:** kyawminthu20.github.io

---

## Purpose

A command-line Python script that takes a job description (pasted text or URL) and produces a tailored single-column `.docx` resume, saved to `resumes/`. The tailoring is done via Claude API using `RAG/kmt_career.yaml` as the single source of truth for career content.

---

## User Flow

```
$ python3 tools/tailor_resume.py

Paste job description or enter URL: <user input>

→ Fetching URL... (if URL provided)
→ Loading career data...
→ Tailoring resume with Claude...
→ Building document...
→ Saved: resumes/resume_Amazon_AutomationEngineer_2026-03-16.docx
```

---

## Components

### 1. Entry Point — `tools/tailor_resume.py`
- Accepts job description via interactive prompt
- Detects whether input is a URL (starts with `http`) or raw text
- Orchestrates all steps and reports output path

### 2. URL Fetcher
- Uses `requests` to fetch the page
- Uses `BeautifulSoup` to extract readable text (strips nav, scripts, ads)
- Falls back gracefully if fetch fails (prompts user to paste instead)

### 3. RAG Loader
- Reads `RAG/kmt_career.yaml` with `pyyaml`
- Passes full YAML content to Claude as career source data

### 4. Claude API Tailoring
- Uses `anthropic` SDK (claude-sonnet-4-6)
- Prompt instructs Claude to:
  - Extract company name and role title from JD (for filename)
  - Write a 3–4 sentence professional summary targeted to the JD
  - Select and reorder core competencies matching JD keywords
  - Reweight experience bullets across all 8 roles toward JD requirements
  - Return structured JSON with all resume sections
- All facts must come from the RAG YAML — no hallucinated metrics

### 5. DOCX Builder
- Uses `python-docx` to build ATS-safe single-column document
- Sections in order:
  1. Header — name, email, phone, location
  2. Professional Summary
  3. Core Competencies (two-column list inside single-column layout)
  4. Professional Experience (chronological, all 8 roles)
  5. Education
- Fonts: Calibri 11pt body, 10pt bullets; name 16pt bold
- Margins: 1 inch all sides
- No tables, no text boxes, no headers/footers (ATS compatibility)

### 6. Output
- Saved to `resumes/` folder in repo root
- Filename: `resume_<Company>_<Role>_YYYY-MM-DD.docx`
- Company and role sanitized for filesystem (spaces → underscores, special chars stripped)

---

## Dependencies

| Package | Purpose |
|---------|---------|
| `anthropic` | Claude API for tailoring |
| `python-docx` | .docx generation |
| `requests` | URL fetching |
| `beautifulsoup4` | HTML text extraction |
| `pyyaml` | RAG YAML parsing |

Add all to `pyproject.toml` (managed via `uv`).

---

## File Structure Changes

```
kyawminthu20.github.io/
├── tools/
│   └── tailor_resume.py       # new — main script
├── resumes/                   # new — output folder
│   └── .gitkeep
└── pyproject.toml             # updated — new dependencies
```

---

## Error Handling

- URL fetch failure → prompt user to paste text instead
- Missing `ANTHROPIC_API_KEY` → clear error message with setup instructions
- Claude API error → surface error, exit cleanly (no partial .docx)
- RAG file not found → error with expected path

---

## Environment

- Requires `ANTHROPIC_API_KEY` environment variable
- Python >= 3.12
- Run via `uv run python3 tools/tailor_resume.py` or `python3 tools/tailor_resume.py`

---

## Out of Scope

- Cover letter generation (separate future task)
- PDF output (print from Word/LibreOffice)
- GUI or web interface
- Automatic LinkedIn or job board integration
