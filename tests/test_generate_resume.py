"""Tests for the resume data loader in tools/generate_resume.py."""

import sys
from pathlib import Path

import pytest

sys.path.insert(0, str(Path(__file__).resolve().parent.parent / "tools"))

from generate_resume import DATA_PATH, load_resume_data


def _write_yaml(path: Path, text: str) -> Path:
    path.write_text(text, encoding="utf-8")
    return path


VALID_YAML = """
name: Test Person
tagline: Test Engineer
contact_line: test@example.com · Somewhere, CA
summary: A test summary.
skills:
  - label: PLC
    text: Rockwell, Siemens
experience:
  - role: Engineer
    company: TestCo
    dates: 2020 - Present
    bullets:
      - Did a thing.
education:
  - degree: BS Testing
    institution: Test University
"""


def test_load_valid_data(tmp_path: Path) -> None:
    data_file = _write_yaml(tmp_path / "resume.yaml", VALID_YAML)
    data = load_resume_data(data_file)
    assert data["name"] == "Test Person"
    assert data["experience"][0]["bullets"] == ["Did a thing."]


def test_missing_file_gives_meaningful_error(tmp_path: Path) -> None:
    with pytest.raises(FileNotFoundError, match="Resume data file not found"):
        load_resume_data(tmp_path / "nope.yaml")


def test_missing_required_key_is_reported_by_name(tmp_path: Path) -> None:
    data_file = _write_yaml(
        tmp_path / "resume.yaml", VALID_YAML.replace("summary: A test summary.\n", "")
    )
    with pytest.raises(ValueError, match="summary"):
        load_resume_data(data_file)


def test_experience_entry_missing_bullets_is_rejected(tmp_path: Path) -> None:
    data_file = _write_yaml(
        tmp_path / "resume.yaml",
        VALID_YAML.replace("    bullets:\n      - Did a thing.\n", ""),
    )
    with pytest.raises(ValueError, match="bullets"):
        load_resume_data(data_file)


@pytest.mark.skipif(not DATA_PATH.exists(), reason="local RAG data not present")
def test_real_local_data_is_valid() -> None:
    data = load_resume_data(DATA_PATH)
    assert data["name"] == "Kyaw Min Thu"
    assert len(data["experience"]) >= 5
