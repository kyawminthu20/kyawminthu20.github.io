# Project State

**Last Updated:** 2026-03-18
**Status:** Active
**Current Phase:** Phase 5 — Content Complete, Ready for Deployment
**Next Phase:** Phase 6 — GitHub Pages Build & Publication
**Delivery Target:** GitHub Pages static site for personal use

## Purpose

This file tracks what the repository actually is today, what is being built, and what should happen next.

## Current Direction

This repository is being used to build a personal website for GitHub Pages.

The intended tone is modest and factual. The site should introduce Kyaw Min Thu, show selected experience and projects, and avoid language that feels inflated or overly polished. The goal is to sound grounded, capable, and human.

## Current Reality

- `planning/index.html` is the only website prototype in the repository right now.
- The current prototype already covers About, Experience, Projects, Skills, Education, and Contact sections, but the visual style and copy are more bold than the intended tone.
- `RAG/kmt_career.yaml` is the current structured source for resume-style content, including work history, skills, education, and contact details.
- `software_projects/CS453Summer2024MobileProgramming/` holds Android coursework that can supply portfolio examples and supporting artifacts.
- The coursework archive currently includes `HelloWorld`, `Temperature Converter`, `ZooDirectory`, `GoogleMapAndSQLLite`, `Sensor`, `ShakeSensorDemo`, and `WeatherDemo`.
- Supporting artifacts already exist in the repo as assignment PDFs, screenshots, and screen recordings.
- There is no finalized GitHub Pages deployment structure in the current tree yet. No root `index.html`, `docs/` site, workflow file, or site build configuration is present.
- The git worktree shows an in-progress folder reorganization from older top-level coursework paths into `software_projects/`. That should be handled deliberately and not overwritten accidentally.
- Before publication, the repository needs a privacy and secrets review. The career data includes direct contact information, and the coursework archive includes files such as `secrets.properties`.

## Active Priorities

- All content rewriting complete (Tasks 3–7).
- Task 6 & 7 committed as e5a2068: Amazon experience trimmed to 4 focused bullets, Projects section rewritten (3 cards instead of 4, Amazon card retitled to "Control Monitoring").
- Final task pending: Task 8 (final verification and project_state completion).

## What Should Be Implemented Next

1. Task 8 — Final verification pass and project_state files completion.
2. Prepare for deployment: review secrets, finalize GitHub Pages configuration.

## Source Of Truth By Topic

- Current phase and priorities: `project_state/project_state.md`
- Project-level changes: `project_state/change_log.md`
- Runtime and deployment assumptions: `project_state/environment.md`
- Setup and preview steps: `project_state/how_to.md`
- Career source data: `RAG/kmt_career.yaml`
- Current site prototype: `planning/index.html`
- Candidate project archive: `software_projects/CS453Summer2024MobileProgramming/`

## Risks And Gaps

- The current prototype style is stronger and more self-promotional than requested.
- Public contact details and coursework secrets need review before publication.
- The repository currently mixes source material, archived coursework, and site planning without a clean publish boundary.
- Project selection is not finalized yet. Featuring too much would make the site feel crowded and less personal.
