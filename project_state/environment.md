# Environment

**Last Updated:** 2026-03-16
**Status:** Active

## Purpose

This file records what is required to work on the repository in its current form.

## Current Runtime Baseline

- No site framework or package manager is required for the current prototype
- The current site prototype is plain static HTML, CSS, and JavaScript in `planning/index.html`
- The repository also contains YAML source data and archived Android Studio projects

## Current Project Surface

- `planning/index.html` is the current personal-site prototype
- `RAG/kmt_career.yaml` is the structured source for career content
- `software_projects/CS453Summer2024MobileProgramming/` contains Android coursework and media
- `project_state/` contains the working project memory for this repo

## Required Tools

- `git`
- a modern web browser

## Optional Tools

- `python3` for a simple local static server via `python3 -m http.server`
- a code editor

## Dependency Files

There is currently no root site build configuration such as:

- `package.json`
- `Gemfile`
- `pyproject.toml`
- GitHub Pages workflow files

Android coursework subprojects do include Gradle files, but those are source material and not part of the current website runtime.

## Environment Variables

- none required for the current personal-site prototype

Note: the Android coursework archive includes local app configuration files such as `secrets.properties`. Those files are not part of the website, and they should be reviewed before the repository is published more broadly.

## Deployment Target

- GitHub Pages
- static output only
- personal website

## Current Deployment Gap

- no finalized publish directory yet
- no GitHub Pages configuration committed in the current tree
- no automated build or deploy workflow defined yet
