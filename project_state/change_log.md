# Project Change Log

**Last Updated:** 2026-07-12
**Status:** Active

## Purpose

This file tracks meaningful project-level changes for the current repository.

Keep entries concise and focused on changes that future work needs to remember.

## Change History

## 2026-07-12 — Git history purged (git filter-repo, force-push)

- Rewrote all history to remove the personal phone number (6 historical
  locations incl. the original `index.html` tel: link and once-tracked
  `planning/` files) and the Google Maps API key (`AndroidManifest.xml` +
  committed `secrets.properties`). Private paths (`RAG/`, `planning/`,
  coursework, `docs/superpowers/`, presentation working files, `__pycache__`)
  dropped from history entirely; `AIza…`/phone regex-scrubbed from all blobs.
- 67 → 46 commits; `main` `ddf33e4` → `59c558a`. Tip tree verified
  byte-identical before push; site content unchanged; Pages rebuilt; 30 tests pass.
- Pre-purge backup bundle: `Dev/_workspace/kyawminthu20.github.io-pre-purge-2026-07-12.bundle`.
- Residual: orphaned commits stay cached on GitHub until Support purges them;
  API key rotation still required (treat as leaked).

## 2026-07-12 — Whole-project housekeeping (branch docs/post-merge-state, PR #2)

- **PII sweep of all tracked files** found the real phone number still public in
  two places: `tests/test_build_resume.py` (fixture + assertion, now a 555
  placeholder) and `docs/superpowers/plans/2026-03-16-tailor-resume.md`. The
  `docs/superpowers/` plans/specs (internal working docs) were moved to
  gitignored `planning/docs-superpowers/` and removed from tracking. The phone
  number remains in git history — same purge/rotate decision as the API key.
- Untracked stale committed artifacts: three `__pycache__/*.pyc` files,
  presentation working files (`presentation_raw.rtf`, `slide_presentation.rtf`,
  `Kyaw_RCA_Presentation_Practice.pptx`, `speaker_script.md`) and one
  unreferenced photo — all kept locally and added to `.gitignore`, matching the
  existing "private presentation working files" convention.
- Removed redundant `tests/.gitkeep` / `tools/.gitkeep`; softened the
  `.gitignore` comment that itemized RAG's sensitive contents.
- Pruned merged branches (`feat/control-systems-repositioning`,
  `refactor/repo-cleanup` local + remote); deleted local caches/`.DS_Store`s.
- `KyawMinThu_Controls_Presentation.pptx` (repo root, 337 KB) is tracked but
  unreferenced by any page — left in place in case its direct URL is shared;
  candidate for removal.

## 2026-07-12 — Repo cleanup merged to main and verified live (PR #1)

- Merge commit `44b4544`; GitHub Pages build succeeded from it.
- Live-site verification passed: SYS.01–06 each appear exactly once; sitemap
  lists `/` and `/presentation/`; microsite and resume PDF return 200; removed
  coursework path returns 404; no phone number or `tel:` links on the page;
  title reflects the Control Systems positioning.
- Outstanding: rotate the Google Maps API key exposed in git history.

## 2026-07-12 — Repo cleanup: coursework excised, RAG/ privatized, agent docs rewritten (branch refactor/repo-cleanup)

- Archived `software_projects/CS453Summer2024MobileProgramming/` (343 files, ~45 MB)
  to local `Dev/_archive/` and removed it from the repo. It contained a hardcoded
  Google Maps API key in `AndroidManifest.xml` — the key still needs rotation and
  remains in git history until a history purge is decided.
- Gitignored `RAG/` entirely; untracked `RAG/kmt_career.yaml` (contained phone
  number). New local-only sources: `CAREER_AUTOBIOGRAPHY.md`, `CAREER_INDEX-1.md`,
  and `resume_data.yaml`.
- Refactored `tools/generate_resume.py` to load content from `RAG/resume_data.yaml`
  (was fully hardcoded, including personal phone). Added validating loader + tests
  (`tests/test_generate_resume.py`); added `pyyaml` dependency. Verified end-to-end.
- Rewrote `CLAUDE.md` and `AGENTS.md` — both previously described a different
  project ("Control System Tools" / `control-standards/rag/`) with dead commands.
  Fixed the SessionStart hook; removed RAG-workspace agents and skills from `.claude/`.
- Site fixes: sections renumbered SYS.01–06 (duplicate SYS.02 resolved); dead
  private Control-System-Tools link replaced with a "private repository" note;
  `/presentation/` added to `sitemap.xml`; `_config.yml` title/description aligned
  with the Control Systems repositioning.
- Untracked stray `.DS_Store` files; ignored `__pycache__/` and `.pytest_cache/`.
- Refreshed `environment.md` (was describing deleted `planning/index.html` era)
  and wrote `how_to.md` (was empty).

## 2026-06-07 — Portfolio repositioning toward Control Systems / semiconductor (root index.html)

Executed `planning/Portfolio_Site_Update_Prompt.md` against the live site (`index.html` — single-file, `.nojekyll`). Design language preserved; surgical content/markup edits only. Fanned out 6 subagents over disjoint regions to author the edit specs, applied sequentially in-thread.

- **Positioning:** title/meta/OG/Twitter + hero label + summary changed from "Automation Controls Engineer" to "Control Systems Engineer — Semiconductor & Critical Facilities".
- **Experience:** added WGNSTAR — Semiconductor Tool Research Site (APR 2026 — Present) as the active top entry (scope-only, no metrics — role ~2 months old); re-dated C&W/JLL to MAY 2025 — MAR 2026 (past role); GE International Inc. → GE Power; Energy Recovery title → "Electrical, Instrumentation & Controls Technician".
- **System Overview:** fixed inaccurate HMI stat (LabVIEW/CompactDAQ was DAQ, not HMI) → split into "HMI / SCADA: Ignition, FactoryTalk, Cimplicity" + "Data Acquisition: NI CompactDAQ, LabVIEW"; added a SCADA/HMI competency card.
- **Projects:** added "Off-Site Digital Twin — Ignition SCADA + Python" card, marked Personal Project · In Development.
- **Résumé:** added "DOWNLOAD RÉSUMÉ" hero CTA → `/Kyaw_Min_Thu_Resume.pdf` (exists at repo root).
- **Contact:** removed public phone (and JSON-LD `telephone`); added LinkedIn + GitHub cards; updated "open to" copy to direct-hire Control Systems roles.
- **SEO:** updated existing JSON-LD Person (jobTitle, description, expanded knowsAbout, added `sameAs` LinkedIn/GitHub).
- Also refined `planning/Portfolio_Site_Update_Prompt.md` to match repo reality (single-file site, résumé path, JSON-LD-already-present, SYS.02 duplicate).
- Verified: no stale strings; JSON-LD valid; one active timeline-item; `<div>` balance 209/209.
- **Known/deferred:** RCA and Experience sections both still labelled `SYS.02` (pre-existing); left as-is (nav uses `#anchors`, not SYS IDs). Not committed — awaiting review.

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
