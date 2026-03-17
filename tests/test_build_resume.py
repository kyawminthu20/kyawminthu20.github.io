# tests/test_build_resume.py
import pytest
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent.parent / "tools"))
from build_resume import validate


VALID = {
    "name": "Kyaw Min Thu", "email": "kyaw@kmtkn.me",
    "phone": "+1 510 909 3716", "location": "Bay Area, CA",
    "company": "Acme", "role": "Engineer",
    "summary": "A summary.",
    "competencies": ["PLC", "SCADA"],
    "experience": [{"company": "Co", "role": "Eng", "dates": "2020–2024",
                    "bullets": ["Did thing."]}],
    "education": [{"degree": "BS CS", "institution": "CSUEB",
                   "location": "Hayward, CA", "dates": "2018–2024"}],
}


def test_valid_data_passes():
    validate(VALID)  # should not raise


def test_missing_name_raises():
    with pytest.raises(ValueError, match="name"):
        validate({**VALID, "name": None})


def test_missing_email_raises():
    with pytest.raises(ValueError, match="email"):
        validate({**VALID, "email": None})


def test_missing_company_raises():
    with pytest.raises(ValueError, match="company"):
        validate({**VALID, "company": None})


def test_missing_role_raises():
    with pytest.raises(ValueError, match="role"):
        validate({**VALID, "role": None})


def test_missing_phone_raises():
    with pytest.raises(ValueError, match="phone"):
        validate({**VALID, "phone": None})


def test_missing_location_raises():
    with pytest.raises(ValueError, match="location"):
        validate({**VALID, "location": None})


def test_missing_summary_raises():
    with pytest.raises(ValueError, match="summary"):
        validate({**VALID, "summary": None})


def test_missing_competencies_raises():
    with pytest.raises(ValueError, match="competencies"):
        validate({**VALID, "competencies": None})


def test_missing_education_raises():
    with pytest.raises(ValueError, match="education"):
        validate({**VALID, "education": None})


def test_empty_experience_raises():
    with pytest.raises(ValueError, match="experience"):
        validate({**VALID, "experience": []})


def test_missing_bullets_raises():
    bad_exp = [{"company": "Co", "role": "Eng", "dates": "2020"}]
    with pytest.raises(ValueError, match="bullets"):
        validate({**VALID, "experience": bad_exp})


from build_resume import sanitize_filename


def test_spaces_become_underscores():
    assert sanitize_filename("Amazon Robotics") == "Amazon_Robotics"


def test_special_chars_stripped():
    assert sanitize_filename("C&W / JLL") == "CW__JLL"


def test_capped_at_50_chars():
    long = "A" * 60
    assert len(sanitize_filename(long)) == 50


def test_empty_string_returns_unknown():
    assert sanitize_filename("") == "Unknown"


def test_none_returns_unknown():
    assert sanitize_filename(None) == "Unknown"
