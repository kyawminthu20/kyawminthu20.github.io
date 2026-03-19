# Project Change Log

**Last Updated:** 2026-03-18
**Status:** Active

## Purpose

This file tracks meaningful project-level changes for the current repository.

Keep entries concise and focused on changes that future work needs to remember.

## Change History

## 2026-03-18 — Experience bullets and projects section rewritten (Tasks 6–7)

- Task 6: Trimmed Amazon experience (C&W Services / JLL) from 7 bullets to 4. Removed redundant SCADA/HMI line, merged Fanuc lead and support roles, consolidated Python and inspection system bullets. Focus: Controls lead, Fanuc lead, Python tools, conveyor system.
- Task 7a: Rewrote 3 project cards to remove marketing language and `project-stat` metrics. Siemens project: removed "30% uptime improvement" metric. R&D project: simplified, added "CompactDAQ" detail, "Safety interlocks" tag. Amazon project: retitled to "Control Monitoring" (was "Material Handling & SCADA Optimization"), refocused on monitoring dashboards and Python tools.
- Task 7b: Deleted firefighter training card (4th card removed). Grid now contains 3 project cards.
- Commit: e5a2068
- Phase: Content complete; ready for final verification and deployment.

## 2026-03-18 — Head metadata, hero copy, and competencies rewritten (Tasks 3–5)

- Task 3: Updated meta description, OG description, Twitter Card description to remove "20+" and "industrial automation, and control system design" phrasing. Added `description` field to JSON-LD Person block.
- Task 4: Replaced hero summary paragraph — removed "Results-driven", "full-lifecycle", "improve uptime" buzzwords. New copy is factual and concise (2 lines).
- Task 5: Replaced 8-card competency grid with 5-card grid. Removed Database & Historian, Version Control/Mentorship, and Control System Development cards. Added Programming and Hardware cards with specific tooling detail.
- Commit: e0f6e45

## 2026-03-16 — Domain strategy corrected; repo files finalized

- Removed CNAME file — CNAME was breaking GitHub Pages by pointing to an unconfigured domain.
- Domain strategy: kyawminthu20.github.io is the canonical site URL. kmtkn.me will redirect TO it via domain registrar URL forwarding (not via GitHub Pages CNAME).
- Updated all URL references (canonical, OG, sitemap, robots.txt, _config.yml) from kmtkn.me to kyawminthu20.github.io.
- Email addresses (kyaw@kmtkn.me) unchanged — those are email, not site URLs.

## 2026-03-16 — Repo files created; site ready for deploy

- Created `CNAME` (kmtkn.me) — custom domain configured for GitHub Pages.
- Created `.nojekyll` — bypasses Jekyll processing; index.html served as-is.
- Created `_config.yml` — GitHub Pages metadata (title, description, URL).
- Created `robots.txt` — allows all crawlers; references sitemap.
- Created `sitemap.xml` — single-page sitemap pointing to https://kmtkn.me/.
- Added SEO meta block to `index.html` head: meta description, canonical URL, Open Graph tags, Twitter Card tags, JSON-LD Person structured data.
- Confirmed repo: kyawminthu20/kyawminthu20.github.io.
- Confirmed domains: primary kmtkn.me (CNAME), fallback kyawminthu20.github.io (auto-redirect by GitHub Pages).
- Phase advanced to 5 — Ready to Deploy.

## 2026-03-16 — Documentation reset for personal website direction

- Rewrote `README.md` to describe the repository as a personal GitHub Pages site in progress instead of a finished portfolio or unrelated project.
- Replaced the stale `project_state` narrative from an older Jekyll and standards-oriented site with the actual repository inventory and current direction.
- Recorded the current working inputs for the site: `planning/index.html`, `RAG/kmt_career.yaml`, and `software_projects/CS453Summer2024MobileProgramming/`.
- Captured the requested tone explicitly: humble, factual, and not overconfident.

## 2026-03-16 — Publication risks and structure gaps documented

- Documented that the current prototype style and copy are still stronger than the desired public tone.
- Documented that direct contact details and coursework files such as `secrets.properties` should be reviewed before publication.
- Noted that the repository appears to be mid-reorganization, with older coursework paths being replaced by the current `software_projects/` layout.
- Recorded that GitHub Pages structure and deployment workflow have not been finalized yet.
