# Project Instructions

Start each session by reading [PROJECT_STARTUP_CONTEXT.md](/Users/kyawminthu/Dev/Control System Tools/PROJECT_STARTUP_CONTEXT.md). Use [STRUCTURE_SUMMARY.md](/Users/kyawminthu/Dev/Control System Tools/STRUCTURE_SUMMARY.md) as a current tree reference, not as the primary project narrative.

## Project Focus

This repository is an industrial automation knowledge base and tooling workspace. The canonical authoritative knowledge lives under `control-standards/rag/`.

## Canonical Paths

- `control-standards/rag/`: authoritative AI-readable standards and engineering knowledge
- `control-standards/governance/`: policies, promotion checklists, decision logs
- `control-standards/templates/`: reusable product templates
- `tools/`: workspace automation and validation scripts

## Secondary Or Context-Only Paths

- `control-standards/archive/`: historical material and migration backups
- `control-standards/exports/`: generated deliverables
- `data/`: raw datasets and captures
- `planning/`: planning artifacts and manifests
- `.venv/`, `.git/`: environment and VCS internals

## Non-Authoritative Paths

- `control-standards/work/design/`: work in progress, readable with caution
- `control-standards/work/general/`: non-authoritative notes, not for factual answers

## AI-Forbidden Paths

- `control-standards/restricted/`

Only use restricted material if the user explicitly asks for it, and clearly label it as non-authoritative and AI-forbidden source material.

## Working Rules

- Prefer `control-standards/rag/standards_intelligence/` for standards questions.
- Prefer the grouped standards layout:
  - `us/`
  - `international/machinery/`
  - `international/functional_safety/`
  - `crosswalks/`
- Do not overstate completeness. `standards_intelligence/` is populated; several other RAG modules are still mostly scaffolding.
- Preserve AI boundary rules: authoritative content is paraphrased guidance, not copyrighted standards text.
- When adding authoritative Markdown, include metadata such as `AI_READ_ACCESS`, `CONTENT_CLASS`, and `STATUS`.
- After structural or metadata changes, consider running `python3 tools/validate_ai_boundaries.py`, `python3 tools/project_automator.py`, and `bash tools/validate_reorg.sh all`.
