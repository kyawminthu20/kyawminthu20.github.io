import json
import re
import sys
from datetime import date
from pathlib import Path


REQUIRED_TOP_LEVEL = ["name", "email", "phone", "location", "summary",
                       "competencies", "experience", "education", "company", "role"]


def validate(data: dict) -> None:
    """Raise ValueError with field name if any required field is missing or null."""
    for field in REQUIRED_TOP_LEVEL:
        if data.get(field) is None:
            raise ValueError(f"Missing required field: '{field}'")
    if not data["experience"]:
        raise ValueError("'experience' list is empty")
    for i, job in enumerate(data["experience"]):
        if "bullets" not in job:
            raise ValueError(f"experience[{i}] missing 'bullets' key")


def sanitize_filename(value: str | None) -> str:
    if not value:
        return "Unknown"
    cleaned = re.sub(r'[^A-Za-z0-9_-]', '', value.replace(' ', '_'))
    return cleaned[:50] if cleaned else "Unknown"
