# Data Model: Expense Tracker MVP

Entities

Expense
- id: String (UUID)
- date: LocalDate (ISO-8601 yyyy-MM-dd, stored as string)
- amount: Int (positive)
- photoRef: String? (URI/path)
- createdAt: Long (epoch millis)
- updatedAt: Long (epoch millis)

Validation Rules
- amount > 0 and integer
- date must be a valid calendar date

Derived/Indexes
- Index on (date)

Relationships
- None (MVP). PhotoRef is a file reference, not a relation.

Migrations
- v1: Create table `expenses` with columns matching fields above.

Retention Policy
- Configurable retentionDays (default 365). On app start, delete rows where date < today - retentionDays.

