# Environment

**Last Updated:** 2026-07-12
**Status:** Active

## Purpose

This file records what is required to work on the repository in its current form.

## Current Runtime Baseline

- The live site is the root `index.html` — plain static HTML/CSS/JS, fully
  self-contained, no build step. `.nojekyll` disables Jekyll processing.
- `presentation/` is a second self-contained static page (RCA talk microsite)
  with a service worker for offline viewing.
- Resume tooling is Python: `tools/generate_resume.py` (PDF, reportlab) and
  `tools/build_resume.py` (DOCX, incomplete).

## Required Tools

- `git`
- a modern web browser
- Python >= 3.12 with `uv` (only for resume tooling and tests)

## Dependency Files

- `pyproject.toml` + `uv.lock` — Python deps: `reportlab`, `python-docx`,
  `pyyaml`; dev: `pytest`. Install with `uv sync`.
- No Node/Ruby build configuration; `_config.yml` is GitHub Pages metadata only.

## Data Files

- `RAG/` is **gitignored and local-only** (contains personal identifiers).
  `RAG/resume_data.yaml` is the content source for `generate_resume.py`.
  Back it up privately; a fresh clone will not have it.

## Environment Variables

- none

## Deployment Target

- GitHub Pages serving the repository root from `main`
- Canonical URL: https://kyawminthu20.github.io (kmtkn.me forwards to it via
  registrar-level redirect, not a CNAME)
- Merging to `main` publishes automatically; no CI workflow files exist or are
  needed
