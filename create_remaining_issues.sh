#!/bin/bash
set -e

# Simple bash script to create remaining GitHub issues

echo "Getting GitHub token..."
TOKEN=$(gh auth token 2>/dev/null)
if [ -z "$TOKEN" ]; then
    echo "Failed to get token"
    exit 1
fi

OWNER="Daemon-Devarshi"
REPO="hafnium-expense-tracker"
API_URL="https://api.github.com/repos/$OWNER/$REPO/issues"

create_issue() {
    local title="$1"
    local body="$2"

    response=$(curl -s -X POST "$API_URL" \
        -H "Authorization: token $TOKEN" \
        -H "Accept: application/vnd.github.v3+json" \
        -H "Content-Type: application/json" \
        -d "{\"title\":\"$title\",\"body\":\"$body\",\"labels\":[\"task\"]}")

    number=$(echo "$response" | jq -r '.number // empty' 2>/dev/null)
    if [ -n "$number" ]; then
        echo "✓ #$number: $title"
        return 0
    else
        echo "✗ Failed: $title"
        return 1
    fi
}

echo "Creating Phase 7 tasks..."
create_issue "T045: Add retention config and default" "**Phase:** Phase 7: US5 – Data retention job\n\nCreate retention configuration with default 365 days."
create_issue "T046: Implement retentionCleanup() in repository" "**Phase:** Phase 7: US5 – Data retention job\n\nImplement cleanup logic for old expenses."
create_issue "T047: Invoke cleanup on app start" "**Phase:** Phase 7: US5 – Data retention job\n\nCall retention cleanup on application initialization."
create_issue "T048: Unit test retention logic" "**Phase:** Phase 7: US5 – Data retention job\n\nAdd tests for retention cleanup."

echo ""
echo "Creating Phase 8 tasks..."
create_issue "T049: Add content descriptions/labels and focus order" "**Phase:** Phase 8: Polish & Cross-cutting\n\nAdd accessibility improvements."
create_issue "T050: Ensure OS text scaling respected" "**Phase:** Phase 8: Polish & Cross-cutting\n\nRespect OS text scaling settings."
create_issue "T051: Android manifest permissions" "**Phase:** Phase 8: Polish & Cross-cutting\n\nAdd required Android manifest permissions."
create_issue "T052: iOS Info.plist usage descriptions" "**Phase:** Phase 8: Polish & Cross-cutting\n\nAdd required iOS Info.plist usage descriptions."
create_issue "T053: Smoke test script notes" "**Phase:** Phase 8: Polish & Cross-cutting\n\nAdd smoke test script documentation."
create_issue "T054: Define lightweight logging & privacy guidelines" "**Phase:** Phase 8: Polish & Cross-cutting\n\nCreate logging and privacy guidelines document."

echo ""
echo "Creating Phase 9 tasks..."
create_issue "T055: Unit test ListViewModel date switching" "**Phase:** Phase 9: Testing & Verification\n\nAdd tests for date switching and empty state."
create_issue "T056: Unit test repository create/update & photo fallback" "**Phase:** Phase 9: Testing & Verification\n\nAdd tests for repository operations."
create_issue "T057: Android instrumentation smoke test" "**Phase:** Phase 9: Testing & Verification\n\nAdd basic Android instrumentation tests."
create_issue "T058: Performance timing harness" "**Phase:** Phase 9: Testing & Verification\n\nCreate performance measurement harness."
create_issue "T059: Accessibility audit checklist" "**Phase:** Phase 9: Testing & Verification\n\nCreate accessibility audit checklist document."

echo ""
echo "Creating Phase 1 parent issue..."
parent_body="**Parent issue for Phase 1: Setup**

Checklist:
- [ ] #1 T001: Update versions catalog
- [ ] #2 T002: Add KSP plugin alias
- [ ] #3 T003: Configure Compose Multiplatform
- [ ] #4 T004: Add dependencies"

create_issue "Phase 1: Setup — Parent" "$parent_body"

echo ""
echo "Done! All issues created."

