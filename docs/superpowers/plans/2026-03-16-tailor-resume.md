# Tailor Resume Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build `tools/build_resume.py` — a Python script that reads `resumes/current.json` and produces a tailored ATS-safe `.docx` resume in `resumes/`.

**Architecture:** Claude Code handles the tailoring (reads RAG YAML + job description, writes `resumes/current.json`). The Python script is a pure formatter — reads JSON, builds `.docx` with `python-docx`, saves to `resumes/`. No AI, no API key.

**Tech Stack:** Python 3.12+, python-docx, uv (package manager), pytest

---

## Chunk 1: Project scaffolding

### Task 1: Create pyproject.toml, folder structure, and update .gitignore

**Files:**
- Create: `pyproject.toml`
- Create: `resumes/.gitkeep`
- Create: `tools/.gitkeep` (placeholder until script is added)
- Modify: `.gitignore`

- [ ] **Step 1: Create `pyproject.toml`**

```toml
[project]
name = "kyawminthu-tools"
version = "0.1.0"
requires-python = ">=3.12"
dependencies = [
    "python-docx",
]

[tool.pytest.ini_options]
testpaths = ["tests"]

[dependency-groups]
dev = [
    "pytest",
]
```

- [ ] **Step 2: Create `resumes/`, `tools/`, and `tests/` folders**

```bash
mkdir -p resumes tests tools
touch resumes/.gitkeep tools/.gitkeep tests/.gitkeep
```

- [ ] **Step 3: Update `.gitignore`**

Add to existing `.gitignore`:
```
resumes/*.docx
resumes/current.json
.venv/
```

- [ ] **Step 4: Install dependencies**

```bash
uv sync --dev
```

Expected: uv creates `.venv/` and installs `python-docx` and `pytest`.

- [ ] **Step 5: Commit**

```bash
git add pyproject.toml resumes/.gitkeep tools/.gitkeep tests/.gitkeep .gitignore
git commit -m "chore: scaffold tailor-resume project structure"
```

---

## Chunk 2: Validation and filename utilities

### Task 2: JSON validation

**Files:**
- Create: `tests/test_build_resume.py` (tests first)
- Create: `tools/build_resume.py` (implementation after)

- [ ] **Step 1: Write failing tests in `tests/test_build_resume.py`**

```python
# tests/test_build_resume.py
import pytest
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent.parent / "tools"))
from build_resume import validate


VALID = {
    "name": "Kyaw Min Thu", "email": "kyaw@kmtkn.me",
    "phone": "+1 510 909 3716", "location": "Bay Area, CA",
    "company": "Acme", "role": "Engineer",
    "summary": "A summary.",
    "competencies": ["PLC", "SCADA"],
    "experience": [{"company": "Co", "role": "Eng", "dates": "2020–2024",
                    "bullets": ["Did thing."]}],
    "education": [{"degree": "BS CS", "institution": "CSUEB",
                   "location": "Hayward, CA", "dates": "2018–2024"}],
}


def test_valid_data_passes():
    validate(VALID)  # should not raise


def test_missing_name_raises():
    with pytest.raises(ValueError, match="name"):
        validate({**VALID, "name": None})


def test_missing_email_raises():
    with pytest.raises(ValueError, match="email"):
        validate({**VALID, "email": None})


def test_missing_company_raises():
    with pytest.raises(ValueError, match="company"):
        validate({**VALID, "company": None})


def test_missing_role_raises():
    with pytest.raises(ValueError, match="role"):
        validate({**VALID, "role": None})


def test_missing_phone_raises():
    with pytest.raises(ValueError, match="phone"):
        validate({**VALID, "phone": None})


def test_missing_location_raises():
    with pytest.raises(ValueError, match="location"):
        validate({**VALID, "location": None})


def test_missing_summary_raises():
    with pytest.raises(ValueError, match="summary"):
        validate({**VALID, "summary": None})


def test_missing_competencies_raises():
    with pytest.raises(ValueError, match="competencies"):
        validate({**VALID, "competencies": None})


def test_missing_education_raises():
    with pytest.raises(ValueError, match="education"):
        validate({**VALID, "education": None})


def test_empty_experience_raises():
    with pytest.raises(ValueError, match="experience"):
        validate({**VALID, "experience": []})


def test_missing_bullets_raises():
    bad_exp = [{"company": "Co", "role": "Eng", "dates": "2020"}]
    with pytest.raises(ValueError, match="bullets"):
        validate({**VALID, "experience": bad_exp})
```

- [ ] **Step 2: Run tests to verify they fail**

```bash
uv run pytest tests/test_build_resume.py -v
```

Expected: FAIL — `ModuleNotFoundError: No module named 'build_resume'`

- [ ] **Step 3: Create `tools/build_resume.py` with validate() implementation**

```python
import json
import re
import sys
from datetime import date
from pathlib import Path


REQUIRED_TOP_LEVEL = ["name", "email", "phone", "location", "summary",
                       "competencies", "experience", "education", "company", "role"]


def validate(data: dict) -> None:
    """Raise ValueError with field name if any required field is missing or null."""
    for field in REQUIRED_TOP_LEVEL:
        if data.get(field) is None:
            raise ValueError(f"Missing required field: '{field}'")
    if not data["experience"]:
        raise ValueError("'experience' list is empty")
    for i, job in enumerate(data["experience"]):
        if "bullets" not in job:
            raise ValueError(f"experience[{i}] missing 'bullets' key")
```

- [ ] **Step 4: Run tests to verify they pass**

```bash
uv run pytest tests/test_build_resume.py -v
```

Expected: 12 PASSED

- [ ] **Step 5: Commit**

```bash
git add tools/build_resume.py tests/test_build_resume.py
git commit -m "feat: add JSON validation with tests"
```

---

### Task 3: Filename sanitization

**Files:**
- Modify: `tools/build_resume.py`
- Modify: `tests/test_build_resume.py`

- [ ] **Step 1: Write failing tests**

Add to `tests/test_build_resume.py`:

```python
from build_resume import sanitize_filename


def test_spaces_become_underscores():
    assert sanitize_filename("Amazon Robotics") == "Amazon_Robotics"


def test_special_chars_stripped():
    assert sanitize_filename("C&W / JLL") == "CW__JLL"


def test_capped_at_50_chars():
    long = "A" * 60
    assert len(sanitize_filename(long)) == 50


def test_empty_string_returns_unknown():
    assert sanitize_filename("") == "Unknown"


def test_none_returns_unknown():
    assert sanitize_filename(None) == "Unknown"
```

- [ ] **Step 2: Run tests to verify they fail**

```bash
uv run pytest tests/test_build_resume.py::test_spaces_become_underscores -v
```

Expected: FAIL — `sanitize_filename` not defined

- [ ] **Step 3: Implement `sanitize_filename` in `tools/build_resume.py`**

```python
def sanitize_filename(value: str | None) -> str:
    if not value:
        return "Unknown"
    cleaned = re.sub(r'[^A-Za-z0-9_-]', '', value.replace(' ', '_'))
    return cleaned[:50] if cleaned else "Unknown"
```

- [ ] **Step 4: Run all tests**

```bash
uv run pytest tests/test_build_resume.py -v
```

Expected: all PASSED

- [ ] **Step 5: Commit**

```bash
git add tools/build_resume.py tests/test_build_resume.py
git commit -m "feat: add filename sanitization with tests"
```

---

## Chunk 3: DOCX section builders

### Task 4: Document setup and header section

**Files:**
- Modify: `tools/build_resume.py`
- Modify: `tests/test_build_resume.py`

- [ ] **Step 1: Write failing tests for `build_header()` and `make_doc()`**

Add to `tests/test_build_resume.py`:

```python
import build_resume
from docx.shared import Pt, Inches
from build_resume import build_header, make_doc


def test_make_doc_margins():
    doc = make_doc()
    section = doc.sections[0]
    assert section.left_margin == Inches(1)
    assert section.right_margin == Inches(1)
    assert section.top_margin == Inches(1)
    assert section.bottom_margin == Inches(1)


def test_make_doc_no_default_paragraph():
    doc = make_doc()
    assert len(doc.paragraphs) == 0


def test_header_name_is_first_paragraph():
    doc = make_doc()
    build_header(doc, VALID)
    assert doc.paragraphs[0].text == "Kyaw Min Thu"


def test_header_name_is_bold():
    doc = make_doc()
    build_header(doc, VALID)
    assert doc.paragraphs[0].runs[0].bold is True


def test_header_name_font_size():
    doc = make_doc()
    build_header(doc, VALID)
    assert doc.paragraphs[0].runs[0].font.size == Pt(16)


def test_header_name_font_is_calibri():
    doc = make_doc()
    build_header(doc, VALID)
    assert doc.paragraphs[0].runs[0].font.name == "Calibri"


def test_header_contact_line_contains_email():
    doc = make_doc()
    build_header(doc, VALID)
    assert "kyaw@kmtkn.me" in doc.paragraphs[1].text


def test_header_contact_line_contains_phone():
    doc = make_doc()
    build_header(doc, VALID)
    assert "+1 510 909 3716" in doc.paragraphs[1].text
```

- [ ] **Step 2: Run tests to verify they fail**

```bash
uv run pytest tests/test_build_resume.py -k "header" -v
```

Expected: FAIL — `build_header` not defined

- [ ] **Step 3: Implement `build_header()` and `make_doc()`**

Add to `tools/build_resume.py`:

```python
from docx import Document
from docx.shared import Pt, Inches
from docx.enum.text import WD_ALIGN_PARAGRAPH


def make_doc() -> Document:
    doc = Document()
    section = doc.sections[0]
    section.top_margin = Inches(1)
    section.bottom_margin = Inches(1)
    section.left_margin = Inches(1)
    section.right_margin = Inches(1)
    # Remove default empty paragraph
    for para in doc.paragraphs:
        p = para._element
        p.getparent().remove(p)
    return doc


def _set_font(run, size_pt: int, bold: bool = False):
    run.font.name = "Calibri"
    run.font.size = Pt(size_pt)
    run.bold = bold


def build_header(doc: Document, data: dict) -> None:
    # Name
    p = doc.add_paragraph()
    run = p.add_run(data["name"])
    _set_font(run, 16, bold=True)

    # Contact line
    contact = f"{data['email']}  ·  {data['phone']}  ·  {data['location']}"
    p2 = doc.add_paragraph()
    run2 = p2.add_run(contact)
    _set_font(run2, 10)
```

- [ ] **Step 4: Run tests**

```bash
uv run pytest tests/test_build_resume.py -k "header" -v
```

Expected: all header tests PASSED

- [ ] **Step 5: Commit**

```bash
git add tools/build_resume.py tests/test_build_resume.py
git commit -m "feat: add document setup and header builder with tests"
```

---

### Task 5: Summary, competencies, and section headings

**Files:**
- Modify: `tools/build_resume.py`
- Modify: `tests/test_build_resume.py`

- [ ] **Step 1: Write failing tests**

```python
from build_resume import build_summary, build_competencies, add_section_heading


def test_section_heading_text_is_uppercased():
    doc = make_doc()
    add_section_heading(doc, "Professional Summary")
    assert doc.paragraphs[0].text == "PROFESSIONAL SUMMARY"


def test_section_heading_is_bold():
    doc = make_doc()
    add_section_heading(doc, "Test")
    assert doc.paragraphs[0].runs[0].bold is True


def test_section_heading_font_size():
    doc = make_doc()
    add_section_heading(doc, "Test")
    assert doc.paragraphs[0].runs[0].font.size == Pt(11)


def test_summary_paragraph_contains_text():
    doc = make_doc()
    build_summary(doc, VALID)
    texts = [p.text for p in doc.paragraphs]
    assert VALID["summary"] in texts


def test_competencies_all_items_present():
    doc = make_doc()
    build_competencies(doc, VALID)
    texts = [p.text for p in doc.paragraphs]
    for item in VALID["competencies"]:
        assert item in texts
```

- [ ] **Step 2: Run tests to verify they fail**

```bash
uv run pytest tests/test_build_resume.py -k "summary or competencies or heading" -v
```

Expected: FAIL

- [ ] **Step 3: Implement**

```python
def add_section_heading(doc: Document, title: str) -> None:
    p = doc.add_paragraph()
    run = p.add_run(title.upper())
    _set_font(run, 11, bold=True)
    # Underline via border — simple approach: add horizontal rule via paragraph border
    p.paragraph_format.space_before = Pt(8)
    p.paragraph_format.space_after = Pt(2)


def build_summary(doc: Document, data: dict) -> None:
    add_section_heading(doc, "Professional Summary")
    p = doc.add_paragraph()
    run = p.add_run(data["summary"])
    _set_font(run, 11)


def build_competencies(doc: Document, data: dict) -> None:
    add_section_heading(doc, "Core Competencies")
    for item in data["competencies"]:
        p = doc.add_paragraph(style="List Bullet")
        run = p.add_run(item)
        _set_font(run, 11)
```

- [ ] **Step 4: Run tests**

```bash
uv run pytest tests/test_build_resume.py -k "summary or competencies or heading" -v
```

Expected: all PASSED

- [ ] **Step 5: Commit**

```bash
git add tools/build_resume.py tests/test_build_resume.py
git commit -m "feat: add summary, competencies, and section heading builders"
```

---

### Task 6: Experience section

**Files:**
- Modify: `tools/build_resume.py`
- Modify: `tests/test_build_resume.py`

- [ ] **Step 1: Write failing tests**

```python
from build_resume import build_experience


def test_experience_role_run_is_bold():
    doc = make_doc()
    build_experience(doc, VALID)
    role_para = next(p for p in doc.paragraphs if p.runs and p.runs[0].text == VALID["experience"][0]["role"])
    assert role_para.runs[0].bold is True


def test_experience_dates_run_is_not_bold():
    doc = make_doc()
    build_experience(doc, VALID)
    role_para = next(p for p in doc.paragraphs if p.runs and p.runs[0].text == VALID["experience"][0]["role"])
    assert role_para.runs[1].bold is not True


def test_experience_company_present():
    doc = make_doc()
    build_experience(doc, VALID)
    texts = [p.text for p in doc.paragraphs]
    assert VALID["experience"][0]["company"] in texts


def test_experience_bullet_present():
    doc = make_doc()
    build_experience(doc, VALID)
    texts = [p.text for p in doc.paragraphs]
    assert "Did thing." in texts


def test_experience_bullet_font_size():
    doc = make_doc()
    build_experience(doc, VALID)
    bullet_para = next(p for p in doc.paragraphs if p.text == "Did thing.")
    assert bullet_para.runs[0].font.size == Pt(10)
```

- [ ] **Step 2: Run tests to verify they fail**

```bash
uv run pytest tests/test_build_resume.py -k "experience" -v
```

Expected: FAIL

- [ ] **Step 3: Implement**

```python
def build_experience(doc: Document, data: dict) -> None:
    add_section_heading(doc, "Professional Experience")
    for job in data["experience"]:
        # Role title + dates on same line using tab
        p = doc.add_paragraph()
        role_run = p.add_run(job["role"])
        _set_font(role_run, 11, bold=True)
        dates_run = p.add_run(f"\t{job['dates']}")
        _set_font(dates_run, 10)

        # Company name
        p2 = doc.add_paragraph()
        company_run = p2.add_run(job["company"])
        _set_font(company_run, 10)
        p2.paragraph_format.space_after = Pt(2)

        # Bullets
        for bullet in job["bullets"]:
            p3 = doc.add_paragraph(style="List Bullet")
            run = p3.add_run(bullet)
            _set_font(run, 10)
```

- [ ] **Step 4: Run tests**

```bash
uv run pytest tests/test_build_resume.py -k "experience" -v
```

Expected: all PASSED

- [ ] **Step 5: Commit**

```bash
git add tools/build_resume.py tests/test_build_resume.py
git commit -m "feat: add experience section builder with tests"
```

---

### Task 7: Education section

**Files:**
- Modify: `tools/build_resume.py`
- Modify: `tests/test_build_resume.py`

- [ ] **Step 1: Write failing tests**

```python
from build_resume import build_education


def test_education_degree_present():
    doc = Document()
    build_education(doc, VALID)
    texts = [p.text for p in doc.paragraphs]
    assert any("BS CS" in t for t in texts)


def test_education_institution_present():
    doc = Document()
    build_education(doc, VALID)
    texts = [p.text for p in doc.paragraphs]
    assert any("CSUEB" in t for t in texts)


def test_education_degree_is_bold():
    doc = Document()
    build_education(doc, VALID)
    degree_paras = [p for p in doc.paragraphs if "BS CS" in p.text]
    assert any(r.bold for p in degree_paras for r in p.runs)
```

- [ ] **Step 2: Run tests to verify they fail**

```bash
uv run pytest tests/test_build_resume.py -k "education" -v
```

Expected: FAIL

- [ ] **Step 3: Implement**

```python
def build_education(doc: Document, data: dict) -> None:
    add_section_heading(doc, "Education")
    for entry in data["education"]:
        # Degree — bold
        p = doc.add_paragraph()
        run = p.add_run(entry["degree"])
        _set_font(run, 11, bold=True)

        # Institution, location, dates
        p2 = doc.add_paragraph()
        detail = f"{entry['institution']}  ·  {entry['location']}  ·  {entry['dates']}"
        run2 = p2.add_run(detail)
        _set_font(run2, 10)
        p2.paragraph_format.space_after = Pt(6)
```

- [ ] **Step 4: Run tests**

```bash
uv run pytest tests/test_build_resume.py -k "education" -v
```

Expected: all PASSED

- [ ] **Step 5: Commit**

```bash
git add tools/build_resume.py tests/test_build_resume.py
git commit -m "feat: add education section builder with tests"
```

---

## Chunk 4: Main orchestration and end-to-end

### Task 8: Main function — load JSON, build doc, save file

**Files:**
- Modify: `tools/build_resume.py`
- Modify: `tests/test_build_resume.py`

- [ ] **Step 1: Write failing end-to-end test**

```python
import json
import build_resume
from build_resume import main


def test_main_creates_docx(tmp_path, monkeypatch):
    resumes_dir = tmp_path / "resumes"
    resumes_dir.mkdir()
    (resumes_dir / "current.json").write_text(json.dumps(VALID))
    monkeypatch.setattr(build_resume, "RESUMES_DIR", resumes_dir)

    main()

    assert len(list(resumes_dir.glob("*.docx"))) == 1


def test_main_filename_starts_with_resume_prefix(tmp_path, monkeypatch):
    resumes_dir = tmp_path / "resumes"
    resumes_dir.mkdir()
    (resumes_dir / "current.json").write_text(json.dumps(VALID))
    monkeypatch.setattr(build_resume, "RESUMES_DIR", resumes_dir)

    main()

    name = list(resumes_dir.glob("*.docx"))[0].name
    assert name.startswith("resume_Acme_Engineer_")


def test_main_exits_if_no_json(tmp_path, monkeypatch, capsys):
    resumes_dir = tmp_path / "resumes"
    resumes_dir.mkdir()
    monkeypatch.setattr(build_resume, "RESUMES_DIR", resumes_dir)

    with pytest.raises(SystemExit):
        main()

    assert "Claude Code" in capsys.readouterr().out


def test_main_exits_on_invalid_json(tmp_path, monkeypatch, capsys):
    resumes_dir = tmp_path / "resumes"
    resumes_dir.mkdir()
    (resumes_dir / "current.json").write_text("{ not valid json }")
    monkeypatch.setattr(build_resume, "RESUMES_DIR", resumes_dir)

    with pytest.raises(SystemExit):
        main()

    assert "Invalid JSON" in capsys.readouterr().out


def test_main_exits_on_validate_failure(tmp_path, monkeypatch, capsys):
    resumes_dir = tmp_path / "resumes"
    resumes_dir.mkdir()
    bad_data = {**VALID, "name": None}
    (resumes_dir / "current.json").write_text(json.dumps(bad_data))
    monkeypatch.setattr(build_resume, "RESUMES_DIR", resumes_dir)

    with pytest.raises(SystemExit):
        main()

    assert "Resume data error" in capsys.readouterr().out
```

- [ ] **Step 2: Run tests to verify they fail**

```bash
uv run pytest tests/test_build_resume.py -k "main" -v
```

Expected: FAIL — `main` not defined

- [ ] **Step 3: Implement `main()`**

```python
RESUMES_DIR = Path(__file__).parent.parent / "resumes"


def main() -> None:
    json_path = RESUMES_DIR / "current.json"

    if not json_path.exists():
        print("resumes/current.json not found. Run the tailoring step in Claude Code first.")
        sys.exit(1)

    try:
        data = json.loads(json_path.read_text())
    except json.JSONDecodeError as e:
        print(f"Invalid JSON in resumes/current.json: {e}")
        sys.exit(1)

    try:
        validate(data)
    except ValueError as e:
        print(f"Resume data error: {e}")
        sys.exit(1)

    RESUMES_DIR.mkdir(exist_ok=True)

    doc = make_doc()
    build_header(doc, data)
    build_summary(doc, data)
    build_competencies(doc, data)
    build_experience(doc, data)
    build_education(doc, data)

    company = sanitize_filename(data.get("company"))
    role = sanitize_filename(data.get("role"))
    today = date.today().strftime("%Y-%m-%d")
    filename = f"resume_{company}_{role}_{today}.docx"

    output_path = RESUMES_DIR / filename
    doc.save(str(output_path))
    print(f"Saved: {output_path}")


if __name__ == "__main__":
    main()
```

- [ ] **Step 4: Run all tests**

```bash
uv run pytest tests/ -v
```

Expected: all PASSED

- [ ] **Step 5: Commit**

```bash
git add tools/build_resume.py tests/test_build_resume.py
git commit -m "feat: add main orchestration — tailor-resume script complete"
```

---

### Task 9: Smoke test with real data

**Files:**
- Use: `resumes/current.json` (create manually for smoke test)

- [ ] **Step 1: Create a sample `resumes/current.json`**

```json
{
  "company": "TestCo",
  "role": "Automation Engineer",
  "name": "Kyaw Min Thu",
  "email": "kyaw@kmtkn.me",
  "phone": "+1 (510) 909-3716",
  "location": "San Francisco Bay Area, CA",
  "summary": "Automation Controls Engineer with 20+ years of experience in PLC/SCADA systems.",
  "competencies": ["Rockwell ControlLogix", "Siemens S7", "Ignition SCADA"],
  "experience": [
    {
      "company": "C&W Services / Amazon",
      "role": "Automation Engineer",
      "dates": "Mar 2025 — Present",
      "bullets": ["Lead controls engineer for Amazon fulfillment centers."]
    }
  ],
  "education": [
    {
      "degree": "Bachelor of Science in Computer Science",
      "institution": "California State University, East Bay",
      "location": "Hayward, CA",
      "dates": "Jun 2018 — Dec 2024"
    }
  ]
}
```

- [ ] **Step 2: Run the script**

```bash
uv run python3 tools/build_resume.py
```

Expected output (date will match today's date):
```
Saved: resumes/resume_TestCo_Automation_Engineer_<today>.docx
```

- [ ] **Step 3: Open the .docx in Word or LibreOffice and verify — PASS all items or fix before committing**

| Check | How to verify | Expected |
|-------|--------------|---------|
| Name large and bold | Visual — top of doc | "Kyaw Min Thu" in large bold text |
| Contact line present | Visual | email · phone · location on one line below name |
| Font is Calibri | Select name → check font picker | "Calibri", not "Times New Roman" or "Calibri Body" |
| Margins are 1 inch | Page Layout → Margins | 1" all sides |
| Sections in order | Scroll through | Summary → Competencies → Experience → Education |
| Section headings uppercased | Visual | "PROFESSIONAL SUMMARY", not "Professional Summary" |
| Competency bullets one per line | Visual | No two items on same line |
| Experience bullets 10pt | Select a bullet → font size | 10pt |
| Body text 11pt | Select summary text → font size | 11pt |
| No tables | Word: Insert → Table (count should be 0) | None |

**If any check FAILS:** note which section, delete the output .docx, fix the implementation in `tools/build_resume.py`, re-run the script, and recheck.

- [ ] **Step 4: Delete the smoke test files (they are gitignored anyway)**

```bash
rm resumes/current.json
```

- [ ] **Step 5: Final commit**

```bash
git add tools/build_resume.py tests/ pyproject.toml resumes/.gitkeep .gitignore
git commit -m "chore: finalize tailor-resume tool — smoke test passed"
```

---

## Usage After Implementation

When you want to generate a tailored resume:

1. Paste the job description into Claude Code chat
2. Claude reads `RAG/kmt_career.yaml`, tailors content, writes `resumes/current.json`
3. Claude runs `python3 tools/build_resume.py`
4. Open `resumes/resume_<Company>_<Role>_<date>.docx`
