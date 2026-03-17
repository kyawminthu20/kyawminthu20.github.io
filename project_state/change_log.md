# Project Change Log

**Last Updated:** 2026-03-16
**Status:** Active

## Purpose

This file tracks meaningful project-level changes for the current repository.

Keep entries concise and focused on changes that future work needs to remember.

## Change History

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
