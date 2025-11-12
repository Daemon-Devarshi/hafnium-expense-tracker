#!/bin/bash

# Script to create remaining GitHub issues for expense tracker project

TOKEN=$(gh auth token)
OWNER="Daemon-Devarshi"
REPO="hafnium-expense-tracker"
API_URL="https://api.github.com/repos/$OWNER/$REPO/issues"

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# Function to create an issue
create_issue() {
    local title="$1"
    local body="$2"

    response=$(curl -s -X POST "$API_URL" \
        -H "Authorization: token $TOKEN" \
        -H "Accept: application/vnd.github.v3+json" \
        -d "{\"title\":\"$title\",\"body\":\"$body\",\"labels\":[\"task\"]}")

    number=$(echo "$response" | jq -r '.number // empty')
    if [ -n "$number" ]; then
        echo -e "${GREEN}✓${NC} Created: $title (#$number)"
        echo "$number"
    else
        echo -e "${RED}✗${NC} Failed: $title"
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    fi
}

# Create remaining tasks (T045-T059)
echo "Creating Phase 7, 8, 9 tasks..."

create_issue "T045: Add retention config and default" "**Phase:** Phase 7: US5 – Data retention job\n**Details:** Default 365 days\n\nCreate retention configuration."
create_issue "T046: Implement retentionCleanup() in repository" "**Phase:** Phase 7: US5 – Data retention job\n\nImplement cleanup logic for old expenses."
create_issue "T047: Invoke cleanup on app start" "**Phase:** Phase 7: US5 – Data retention job\n\nCall retention cleanup on application initialization."
create_issue "T048: Unit test retention logic" "**Phase:** Phase 7: US5 – Data retention job\n\nAdd tests for retention cleanup."

create_issue "T049: Add content descriptions/labels and focus order" "**Phase:** Phase 8: Polish & Cross-cutting\n**Details:** Accessibility improvements\n\nAdd accessibility content descriptions to UI."
create_issue "T050: Ensure OS text scaling respected" "**Phase:** Phase 8: Polish & Cross-cutting\n**Details:** Text scaling\n\nRespect OS text scaling settings."
create_issue "T051: Android manifest permissions" "**Phase:** Phase 8: Polish & Cross-cutting\n\nAdd required Android manifest permissions."
create_issue "T052: iOS Info.plist usage descriptions" "**Phase:** Phase 8: Polish & Cross-cutting\n\nAdd required iOS Info.plist usage descriptions."
create_issue "T053: Smoke test script notes" "**Phase:** Phase 8: Polish & Cross-cutting\n**Details:** Launch within 2s\n\nAdd smoke test script documentation."
create_issue "T054: Define lightweight logging & privacy guidelines" "**Phase:** Phase 8: Polish & Cross-cutting\n**Details:** Avoid sensitive data\n\nCreate logging and privacy guidelines document."

create_issue "T055: Unit test ListViewModel date switching" "**Phase:** Phase 9: Testing & Verification\n\nAdd tests for date switching and empty state."
create_issue "T056: Unit test repository create/update & photo fallback" "**Phase:** Phase 9: Testing & Verification\n\nAdd tests for repository operations and photo fallback."
create_issue "T057: Android instrumentation smoke test" "**Phase:** Phase 9: Testing & Verification\n\nAdd basic Android instrumentation tests."
create_issue "T058: Performance timing harness" "**Phase:** Phase 9: Testing & Verification\n**Details:** Measure launch + date filter\n\nCreate performance measurement harness."
create_issue "T059: Accessibility audit checklist" "**Phase:** Phase 9: Testing & Verification\n\nCreate accessibility audit checklist document."

echo -e "\n${GREEN}All tasks created!${NC}"

