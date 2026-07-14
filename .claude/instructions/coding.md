# Coding Guidelines

## General Principles

Code in this project should prioritize:

- readability
- simplicity
- maintainability
- clear responsibility

Prefer simple solutions over complex designs.

Do not introduce abstractions unless they solve a real problem.

---

## Java Version

The project uses:

- Java 21

Use modern Java features when they improve readability.

Avoid using advanced language features only for demonstration purposes.

---

## Naming Conventions

Classes:

Use clear descriptive names.

Examples:

Good:
- LoginPage
- UserApiClient
- TestReportGenerator

Avoid:
- Manager
- Handler
- UtilityClass

unless the responsibility is truly generic.

---

Methods:

Methods should:
- have one clear responsibility
- use descriptive names
- avoid excessive parameters

Prefer:

```java
createUser()
authenticateUser()
generateReport()