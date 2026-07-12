# Claude Workflow

## Project

Personal portfolio site for Kyaw Min Thu (Control Systems Engineer), served by
GitHub Pages from `main`. No build step: `.nojekyll` disables Jekyll and the
live site is the root `index.html` (single file, inline CSS/JS). `presentation/`
is a separate self-contained RCA presentation microsite linked from the main page.

## Quick Commands

```bash
uv sync                                   # install Python deps (Python >= 3.12)
uv run pytest tests/ -q                   # run resume-tooling tests
uv run python tools/generate_resume.py    # regenerate Kyaw_Min_Thu_Resume.pdf
python3 -m http.server 8000               # preview the site locally
```

## Layout

- `index.html` — the live site (self-contained; edit in place)
- `presentation/` — RCA talk microsite (own `index.html` + service worker)
- `RAG/` — **private, gitignored** career source data (contains personal
  identifiers). `RAG/resume_data.yaml` feeds `tools/generate_resume.py`.
  Never track or publish anything under `RAG/`.
- `tools/` — resume generators (`generate_resume.py` PDF; `build_resume.py`
  DOCX, incomplete)
- `tests/` — pytest suite for the resume tooling
- `project_state/` — operational memory for this repository (see below)
- `planning/`, `temporary/` — gitignored local scratch

## Privacy Rules

- This repo is public. Never commit contact details, personal identifiers,
  credentials, or API keys.
- `RAG/` stays gitignored. If new source-data files are needed, put them there.
- The published resume PDF at the repo root is intentionally public.

## Required Automation Behavior

After any meaningful change, update the relevant files in `project_state/`
as part of the same task:

- `project_state/project_state.md` — current phase, implementation state,
  active priorities, what's next
- `project_state/change_log.md` — dated project-level changes and milestones
- `project_state/environment.md` — runtime/tooling/deployment requirements
- `project_state/how_to.md` — setup, run, validation, and deploy steps

Do not leave implementation changes without updating project tracking.

## Git

- Feature branches only (`feat/`, `fix/`, `docs/`, `refactor/` prefixes);
  never commit directly to `main`.
- Conventional commits: `type(scope): message`.
- Deployment is automatic: merging to `main` publishes to GitHub Pages.
