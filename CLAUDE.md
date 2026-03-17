# Claude Workflow

## Project

## Quick Commands

```bash
python3 main.py                          # run current placeholder
python3 tools/project_automator.py       # refresh structure summary
python3 tools/validate_ai_boundaries.py  # validate AI content boundaries
bash tools/validate_reorg.sh all         # validate repo structure
```

> Python >=3.12 required; `uv` preferred (run `uv sync` to install deps).

Use `project_state/` as the operational memory for this repository.

## Required Automation Behavior

After any meaningful change, update the relevant files in `project_state/` as part of the same task.

Do not leave implementation changes without updating project tracking.

## File Ownership

- `project_state/project_state.md`
  - current phase
  - current implementation state
  - active priorities
  - what should be implemented next

- `project_state/change_log.md`
  - dated project-level changes
  - architecture or workflow changes
  - implementation milestones

- `project_state/environment.md`
  - versions
  - runtime requirements
  - tooling requirements
  - deployment requirements
  - environment variables

- `project_state/how_to.md`
  - setup steps
  - run steps
  - validation commands
  - deployment steps

## Update Triggers

Update `project_state/project_state.md` when:

- the phase changes
- the scope changes
- the current implementation state changes
- priorities or next implementation items change

Update `project_state/change_log.md` when:

- code changes materially
- documentation workflow changes
- architecture decisions change
- deployment direction changes

Update `project_state/environment.md` when:

- Python, Node, package, or tool requirements change
- build or deployment tooling changes
- environment variables are introduced or removed
- GitHub Pages or hosting assumptions change

Update `project_state/how_to.md` when:

- setup commands change
- run commands change
- validation commands change
- build or deploy steps change

## Current Project Direction

- Phase 1 target: GitHub Pages
- Intended use: personal use
- Delivery style: static-site friendly
- Authoritative knowledge stays in `control-standards/rag/`
- The app or site layer is a presentation layer, not the authoritative standards source

## Use Existing Automation

When relevant, use the local automation scripts to keep repository documentation current:

- `python3 tools/project_automator.py`
- `python3 tools/validate_ai_boundaries.py`
- `python3 tools/fix_ai_boundaries.py`
- `bash tools/validate_reorg.sh all`

## Tool Usage

Prefer the Bash tool for file operations, searches, and shell commands. You are not required to use dedicated Read/Edit/Grep/Glob tools when Bash is faster or more convenient.

## End-Of-Task Checklist

1. Make the requested changes.
2. Update the affected files under `project_state/`.
3. Run relevant automation or validation if the changes warrant it.
4. Report what changed and any remaining gaps.
