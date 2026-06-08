# Project State

**Last Updated:** 2026-06-07
**Status:** Active
**Current Phase:** Phase 6 — Live site published; iterative content updates
**Next Phase:** Continued content maintenance as role/positioning evolves
**Delivery Target:** GitHub Pages static site for personal use

## Purpose

This file tracks what the repository actually is today, what is being built, and what should happen next.

## Current Direction

This repository is being used to build a personal website for GitHub Pages.

The intended tone is modest and factual. The site should introduce Kyaw Min Thu, show selected experience and projects, and avoid language that feels inflated or overly polished. The goal is to sound grounded, capable, and human.

## Current Reality

- The live site is the root `index.html` — a single-file static page (dark "control-room" aesthetic, inline `<style>`, `.nojekyll` disables Jekyll). There is no `_data/_includes/_layouts`; `planning/index.html` is an older prototype only.
- The site covers About/Hero, Core Competencies, Root Cause Analysis, Professional Experience, Project Highlights, System Thinking, Education, and Contact.
- As of 2026-06-07 the site is repositioned to **Control Systems Engineer — Semiconductor & Critical Facilities** (was "Automation Controls Engineer"), with WGNSTAR added as the current role and the public phone number removed. See change log 2026-06-07.
- `planning/Portfolio_Site_Update_Prompt.md` is the maintained prompt for this and future portfolio updates (kept in sync with repo reality).
- `Kyaw_Min_Thu_Resume.pdf` lives at the repo root and is linked from the hero "DOWNLOAD RÉSUMÉ" CTA.
- `RAG/kmt_career.yaml` is the current structured source for resume-style content, including work history, skills, education, and contact details.
- `software_projects/CS453Summer2024MobileProgramming/` holds Android coursework that can supply portfolio examples and supporting artifacts.
- The coursework archive currently includes `HelloWorld`, `Temperature Converter`, `ZooDirectory`, `GoogleMapAndSQLLite`, `Sensor`, `ShakeSensorDemo`, and `WeatherDemo`.
- Supporting artifacts already exist in the repo as assignment PDFs, screenshots, and screen recordings.
- The repository still needs ongoing privacy hygiene: career data includes direct contact info, and the coursework archive includes files such as `secrets.properties`.

## Active Priorities

- 2026-06-07 portfolio repositioning applied to `index.html` (Control Systems / semiconductor; WGNSTAR current role; phone removed; Digital Twin project; LinkedIn/GitHub contacts). Changes are **uncommitted — awaiting review**.
- Confirm the LinkedIn slug (`in/kyawminthu`) resolves before publishing.

## What Should Be Implemented Next

1. Review the uncommitted `index.html` diff; commit on a feature branch + push when approved.
2. Optional cleanup: both RCA and Experience sections are labelled `SYS.02` — renumber Experience→SYS.03 and cascade if a clean fix is wanted.
3. Optional: add a third RCA case from semiconductor facility work once available.

## Source Of Truth By Topic

- Current phase and priorities: `project_state/project_state.md`
- Project-level changes: `project_state/change_log.md`
- Runtime and deployment assumptions: `project_state/environment.md`
- Setup and preview steps: `project_state/how_to.md`
- Career source data: `RAG/kmt_career.yaml`
- Live site: root `index.html` (single-file). Older prototype: `planning/index.html`
- Portfolio update prompt: `planning/Portfolio_Site_Update_Prompt.md`
- Candidate project archive: `software_projects/CS453Summer2024MobileProgramming/`

## Risks And Gaps

- The current prototype style is stronger and more self-promotional than requested.
- Public contact details and coursework secrets need review before publication.
- The repository currently mixes source material, archived coursework, and site planning without a clean publish boundary.
- Project selection is not finalized yet. Featuring too much would make the site feel crowded and less personal.
