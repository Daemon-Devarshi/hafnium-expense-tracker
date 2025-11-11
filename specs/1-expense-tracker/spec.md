# Expense Tracker MVP

Status: Draft
Owner: TBD
Created: 2025-11-11

## Summary
A minimal mobile expense tracker that lets users quickly record an expense with date, amount (integer only), and an optional photo, and view/edit expenses by date. Data is stored locally on the device. UI/UX is intentionally simple and intuitive.

## Goals
- Enable fast capture of expenses with minimal required fields.
- Provide a list of expenses for a selected date (default: today).
- Allow editing an existing expense entry.
- Store all data locally on the device.

## Non-Goals
- Categories, tags, notes, or multi-currency management.
- Cloud sync, accounts, or multi-device support.
- Analytics, reports, export/sharing, or budgeting features.
- Deleting expenses (can be added later if needed).

## Actors
- Primary User: Any person using the mobile app on Android or iOS to record and review personal expenses on a given date.

## Assumptions
- Amount is an integer in whole currency units based on the device locale; decimals are not supported in this MVP.
- Photos are optional; users can add an expense without a photo.
- Dates may be chosen for past or present; future dates are allowed only if the user explicitly selects them.
- Local storage persists across app restarts; no external backups or cloud sync are required by the app (OS-level backups may still occur per platform settings).
- The app operates fully offline; no network access is required.
- No authentication is required; all interactions are local to the device.
- Data retention: by default, retain user data for at least 1 year; retention period should be configurable in app settings.

## Key Entities
- Expense
  - id: unique identifier (string or numeric)
  - date: calendar date (ISO-8601 date, no time zone effects on display)
  - amount: integer (whole currency units)
  - photo: optional reference to an optimized image stored locally (path/URI/handle)
  - createdAt: timestamp
  - updatedAt: timestamp

## User Scenarios & Acceptance Tests

1) Add expense (happy path)
- Given the app launches to the capture form
- When the user leaves the date as today
- And enters a positive integer amount (e.g., 25)
- And optionally attaches a photo
- And taps Save
- Then the expense is persisted locally
- And the list for today shows the new expense at the top

2) Validation: required fields
- Given the capture form
- When the user leaves the amount empty or enters a non-integer or zero/negative value
- Then the Save action is disabled or shows a clear error
- And the user sees what to correct

3) Change date and view list
- Given the list screen defaults to today’s date
- When the user selects another date
- Then the list updates to show expenses for that date
- And the empty state is shown if there are none

4) Edit existing expense
- Given an expense exists for today
- When the user selects that expense from the list
- And the pre-filled form appears
- And the user changes the amount to another valid integer
- And taps Save
- Then the updated value is persisted and reflected in the list

5) Photo optimization
- Given the user attaches a large photo (e.g., from device camera)
- When the expense is saved
- Then the stored photo is optimized to reduce file size while maintaining clarity
- And the stored image size is typically ≤ 1 MB, with the long edge ≤ 1600 px (targets, not hard errors)

6) Launch and smoke test
- Given a fresh app start
- When the app launches
- Then it reaches the capture form without crashing within 2 seconds on a baseline device

## Functional Requirements

Capture Form (Launch Screen)
- F1. The app launches to a capture form with fields: Date (required, default today), Amount (required, integer only), Photo (optional add/remove).
- F2. Date input must accept only valid calendar dates; default is the device’s current date.
- F3. Amount must be a positive integer; non-integer or ≤ 0 values must be rejected with a clear message.
- F4. Photo attachment is optional; users can add from camera or gallery if available. Removal of an attached photo must be possible before saving.
- F5. Save action persists the expense locally and navigates back to the list for the currently selected date, showing the saved item.

List Screen
- F6. The list defaults to today’s date and displays all expenses for that date.
- F7. The user can change the date; the list refreshes to show expenses for the chosen date.
- F8. Selecting a list item opens the capture form pre-filled with that expense for editing.
- F9. After editing and saving, the list reflects the updated values without duplication.
- F10. When there are no expenses for the selected date, an empty state message is shown with an affordance to add a new expense.

Local Data and Image Handling
- F11. Expenses are persisted locally and remain available across app restarts.
- F12. Each expense must store: id, date, amount, optional photo reference, createdAt, updatedAt.
- F13. Attached photos must be optimized on save (compressed and resized) with a target of ≤ 1 MB file size and long edge ≤ 1600 px, while remaining visually legible.
- F14. If photo optimization fails, the expense still saves without a photo and informs the user that the image could not be stored.
- F15. No authentication is required for this MVP; all interactions are local to the device.
- F16. Data retention: retain expenses for at least 1 year by default; make retention period configurable via app settings.

Usability & Accessibility
- F17. All interactive elements have accessible labels and appropriate focus order.
- F18. The UI supports OS text scaling/dynamic type where the stack allows it.
- F19. Error messages are concise and actionable.

Performance & Reliability
- F20. App launch to first interactive form within 2 seconds on a baseline device.
- F21. Date change updates the list within 1 second for up to 200 items on that date.
- F22. Attaching and optimizing a typical photo completes within 2 seconds for a 5 MB source image on a baseline device.
- F23. No crashes in the happy-path flows described in the scenarios above.

Privacy & Permissions
- F24. The app requests only necessary permissions (e.g., photos/camera if used) and explains why.
- F25. All data remains on-device; no network calls are required for core functionality.
- F26. Logs (if any in debug builds) must not include sensitive user content.

## Success Criteria
- S1. 95% of valid add-expense attempts complete successfully on first try.
- S2. 95th percentile time from Save to item visible in list ≤ 1 second for dates with ≤ 200 items.
- S3. App cold launch to interactive capture form ≤ 2 seconds on a baseline device.
- S4. 95% of stored photos are ≤ 1 MB and long edge ≤ 1600 px after optimization.
- S5. Task success rate: at least 90% of test users can add an expense without guidance within 10 seconds of launch.
- S6. Accessibility spot-check passes for labels, focus order, and text scaling.

## Edge Cases
- E1. Entering zero or negative or non-integer amount: prevent save and show guidance.
- E2. Changing date to one with no expenses: show empty state gracefully.
- E3. Large image attachment or low storage: save expense without photo and inform user.
- E4. Rapid date changes: list remains responsive and accurate.

## Dependencies
- Device date input, local storage, and media picker/camera capabilities provided by the chosen stack/platforms.

## Out of Scope (for this iteration)
- Deletion of expenses
- Categories, notes, currency conversion, budgets
- Cloud sync, backups management, or account features
- Export/share or reporting

## Open Questions
- None for this MVP; defaults are documented in Assumptions.

## Readiness
This spec is technology-agnostic and suitable for planning and estimation. Next steps: clarify baseline devices for performance measurements, define UX wireframes, and create a small test plan aligned to the acceptance scenarios.
