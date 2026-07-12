# How To

**Last Updated:** 2026-07-12
**Status:** Active

## Setup

```bash
git clone https://github.com/kyawminthu20/kyawminthu20.github.io
cd kyawminthu20.github.io
uv sync            # only needed for resume tooling / tests
```

`RAG/` is gitignored — restore `RAG/resume_data.yaml` from your private backup
before regenerating the resume.

## Preview the site locally

```bash
python3 -m http.server 8000
# open http://localhost:8000 (site) and http://localhost:8000/presentation/
```

## Regenerate the resume PDF

```bash
uv run python tools/generate_resume.py   # writes Kyaw_Min_Thu_Resume.pdf at repo root
```

Edit content in `RAG/resume_data.yaml`, not in the script.

## Validate

```bash
uv run pytest tests/ -q
```

## Deploy

1. Work on a feature branch (`feat/`, `fix/`, `docs/`, `refactor/` prefix).
2. Merge to `main` — GitHub Pages publishes the repository root automatically.
3. Verify https://kyawminthu20.github.io after a minute or two.
