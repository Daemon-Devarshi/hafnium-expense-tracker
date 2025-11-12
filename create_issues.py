#!/usr/bin/env python3

import subprocess
import json
import sys
import time

def get_gh_token():
    """Get GitHub token from gh CLI"""
    try:
        result = subprocess.run(['gh', 'auth', 'token'], capture_output=True, text=True, timeout=5)
        return result.stdout.strip()
    except Exception as e:
        print(f"Error getting token: {e}")
        sys.exit(1)

def create_issue_via_api(title, body, token):
    """Create an issue using GitHub API"""
    import urllib.request
    import urllib.error

    url = "https://api.github.com/repos/Daemon-Devarshi/hafnium-expense-tracker/issues"
    headers = {
        "Authorization": f"token {token}",
        "Accept": "application/vnd.github.v3+json",
        "Content-Type": "application/json"
    }

    data = json.dumps({
        "title": title,
        "body": body,
        "labels": ["task"]
    }).encode('utf-8')

    try:
        req = urllib.request.Request(url, data=data, headers=headers, method='POST')
        with urllib.request.urlopen(req, timeout=10) as response:
            result = json.loads(response.read().decode('utf-8'))
            return result.get('number')
    except Exception as e:
        print(f"Error creating issue: {e}")
        return None

def main():
    token = get_gh_token()

    tasks = [
        ("T045: Add retention config and default", "**Phase:** Phase 7: US5 – Data retention job\n**Details:** Default 365 days\n\nCreate retention configuration."),
        ("T046: Implement retentionCleanup() in repository", "**Phase:** Phase 7: US5 – Data retention job\n\nImplement cleanup logic for old expenses."),
        ("T047: Invoke cleanup on app start", "**Phase:** Phase 7: US5 – Data retention job\n\nCall retention cleanup on application initialization."),
        ("T048: Unit test retention logic", "**Phase:** Phase 7: US5 – Data retention job\n\nAdd tests for retention cleanup."),
        ("T049: Add content descriptions/labels and focus order", "**Phase:** Phase 8: Polish & Cross-cutting\n**Details:** Accessibility improvements\n\nAdd accessibility content descriptions to UI."),
        ("T050: Ensure OS text scaling respected", "**Phase:** Phase 8: Polish & Cross-cutting\n**Details:** Text scaling\n\nRespect OS text scaling settings."),
        ("T051: Android manifest permissions", "**Phase:** Phase 8: Polish & Cross-cutting\n\nAdd required Android manifest permissions."),
        ("T052: iOS Info.plist usage descriptions", "**Phase:** Phase 8: Polish & Cross-cutting\n\nAdd required iOS Info.plist usage descriptions."),
        ("T053: Smoke test script notes", "**Phase:** Phase 8: Polish & Cross-cutting\n**Details:** Launch within 2s\n\nAdd smoke test script documentation."),
        ("T054: Define lightweight logging & privacy guidelines", "**Phase:** Phase 8: Polish & Cross-cutting\n**Details:** Avoid sensitive data\n\nCreate logging and privacy guidelines document."),
        ("T055: Unit test ListViewModel date switching", "**Phase:** Phase 9: Testing & Verification\n\nAdd tests for date switching and empty state."),
        ("T056: Unit test repository create/update & photo fallback", "**Phase:** Phase 9: Testing & Verification\n\nAdd tests for repository operations and photo fallback."),
        ("T057: Android instrumentation smoke test", "**Phase:** Phase 9: Testing & Verification\n\nAdd basic Android instrumentation tests."),
        ("T058: Performance timing harness", "**Phase:** Phase 9: Testing & Verification\n**Details:** Measure launch + date filter\n\nCreate performance measurement harness."),
        ("T059: Accessibility audit checklist", "**Phase:** Phase 9: Testing & Verification\n\nCreate accessibility audit checklist document."),
    ]

    print("Creating remaining issues...")
    created_numbers = {}

    for title, body in tasks:
        number = create_issue_via_api(title, body, token)
        if number:
            print(f"✓ {title} (#{number})")
            created_numbers[title.split(':')[0]] = number
        else:
            print(f"✗ {title}")
        time.sleep(0.5)  # Rate limiting

    print("\nCreating Phase 1 parent issue...")
    parent_body = """**Parent issue for Phase 1: Setup**

Checklist of Phase 1 tasks:
- [ ] #1 T001
- [ ] #2 T002
- [ ] #3 T003
- [ ] #4 T004
"""
    parent_num = create_issue_via_api("Phase 1: Setup — Parent", parent_body, token)
    if parent_num:
        print(f"✓ Parent issue created (#{parent_num})")

    print(f"\nSummary: {len(created_numbers)} tasks created")

if __name__ == "__main__":
    main()

