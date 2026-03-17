# How To

**Last Updated:** 2026-03-16
**Status:** Active

## Purpose

This file is the working runbook for the current repository state.

Use it to preview the current prototype, review source material, and keep project tracking up to date.

## View The Current Prototype

Open the prototype directly:

```bash
open planning/index.html
```

Or serve the repository locally:

```bash
python3 -m http.server 8000
```

Then visit:

```text
http://localhost:8000/planning/
```

## Review Portfolio Source Material

Career and resume source data:

```text
RAG/kmt_career.yaml
```

Archived Android coursework and media:

```text
software_projects/CS453Summer2024MobileProgramming/
```

## Update Project Tracking

After meaningful changes to scope, content, structure, or deployment direction:

1. Update `project_state/project_state.md`
2. Update `project_state/change_log.md`
3. Update `project_state/environment.md` if requirements changed
4. Update `project_state/how_to.md` if the working steps changed

## Prepare For GitHub Pages

The publish structure has not been finalized yet. The current likely options are:

1. Publish from the repository root with a root `index.html`
2. Publish from a future `docs/` directory

Before enabling GitHub Pages:

1. Move the selected public site files into the chosen publish location
2. Verify the site works as a static build with no local-only dependencies
3. Review the repository for secrets, private files, and non-public assets
4. Enable GitHub Pages in repository settings once the public structure is ready

## Pre-Publish Review

- Confirm whether phone number and personal email should appear on the public site
- Review coursework files such as `secrets.properties`
- Keep raw source material separate from public-facing content
