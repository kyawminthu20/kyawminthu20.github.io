# Tailor Resume — Design Spec

**Date:** 2026-03-16
**Status:** Draft
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

Non-interactive / CLI argument mode is out of scope. Interactive prompt only.

---

## Components

### 1. Entry Point — `tools/tailor_resume.py`
- Accepts job description via interactive prompt
- Detects whether input is a URL (starts with `http`) or raw text
- Orchestrates all steps and reports output path

### 2. URL Fetcher
- Uses `requests` to fetch the page
- Uses `BeautifulSoup(html, "html.parser")` — stdlib parser, no extra dependency
- Extracts readable text by removing `<script>`, `<style>`, `<nav>`, `<footer>` tags
- Falls back gracefully if fetch fails: prints error, prompts user to paste text instead

### 3. RAG Loader
- Reads `RAG/kmt_career.yaml` with `pyyaml`
- Passes full YAML as string to Claude (deliberate design: ~700 lines fits well within
  claude-sonnet-4-6 context window; no chunking needed at this data size)

### 4. Claude API Tailoring
- Model: `claude-sonnet-4-6`
- Passes RAG YAML + job description in a single prompt
- All facts must come from the RAG YAML — prompt explicitly forbids hallucinated metrics
- Claude returns a JSON object matching this schema:

```json
{
  "company": "string — company name extracted from JD",
  "role": "string — role title extracted from JD",
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
- `education`: both entries from RAG (CSUEB + Singapore Poly)

### 5. DOCX Builder
- Uses `python-docx` to build ATS-safe single-column document
- Sections in order:
  1. **Header** — name (16pt bold), then email · phone · location on one line (10pt)
  2. **Professional Summary** — plain paragraph, 11pt
  3. **Core Competencies** — bullet list, one item per line (no tables, no columns)
  4. **Professional Experience** — for each role: company + dates right-aligned on same
     line as role title (using tab stop), followed by bullet list
  5. **Education** — for each entry: degree bold, institution and dates below
- Fonts: Calibri throughout — 16pt name, 11pt body, 10pt bullets
- Margins: 1 inch all sides
- No tables, no text boxes, no headers/footers, no multi-column sections (ATS safe)

### 6. Output
- Saved to `resumes/` folder in repo root
- Filename pattern: `resume_<Company>_<Role>_YYYY-MM-DD.docx`
- Sanitization rule: `re.sub(r'[^A-Za-z0-9_-]', '', value.replace(' ', '_'))`
- Capped at 50 characters each for company and role
- Fallback if Claude returns empty/unparseable company or role: `resume_Unknown_Unknown_YYYY-MM-DD.docx`
- Generated `.docx` files are gitignored (contain personal contact data; not for public commit history)

---

## Dependencies

| Package | Purpose |
|---------|---------|
| `anthropic` | Claude API for tailoring |
| `python-docx` | .docx generation |
| `requests` | URL fetching |
| `beautifulsoup4` | HTML text extraction (uses stdlib `html.parser`) |
| `pyyaml` | RAG YAML parsing |

---

## File Structure Changes

```
kyawminthu20.github.io/
├── tools/
│   └── tailor_resume.py       # new — main script
├── resumes/                   # new — output folder (gitignored except .gitkeep)
│   └── .gitkeep
├── pyproject.toml             # new — project deps managed via uv
└── .gitignore                 # updated — add resumes/*.docx
```

### `pyproject.toml` skeleton

```toml
[project]
name = "kyawminthu-tools"
version = "0.1.0"
requires-python = ">=3.12"
dependencies = [
    "anthropic",
    "python-docx",
    "requests",
    "beautifulsoup4",
    "pyyaml",
]
```

---

## Error Handling

- URL fetch failure → print error, prompt user to paste text instead
- Missing `ANTHROPIC_API_KEY` → print "Set ANTHROPIC_API_KEY environment variable" and exit
- Claude returns invalid JSON → print raw response, exit cleanly (no partial .docx written)
- Claude API error → surface error message, exit cleanly
- RAG file not found → print expected path `RAG/kmt_career.yaml` and exit

---

## Environment

- Requires `ANTHROPIC_API_KEY` environment variable
- Python >= 3.12
- Run via `uv run python3 tools/tailor_resume.py` or `python3 tools/tailor_resume.py`

---

## Out of Scope

- Non-interactive / CLI argument mode
- Cover letter generation (separate future task)
- PDF output (print from Word/LibreOffice)
- GUI or web interface
- Automatic LinkedIn or job board integration
