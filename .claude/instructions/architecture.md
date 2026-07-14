# Architecture Guidelines

## Architecture Style

AI QA Framework is a Maven multi-module Java project.

The architecture follows a modular design where each module has a clear responsibility.

The goal is maintainability and simplicity.

Do not introduce unnecessary architectural complexity.

---

## Project Modules

### qa-core

Purpose:
- Common abstractions
- Shared interfaces
- Base components used by other modules

Rules:
- Should not depend on higher-level modules.
- Contains reusable functionality only.

---

### qa-api

Purpose:
- API testing capabilities.
- REST API automation support.

Responsibilities:
- API clients
- Request/response handling
- API test utilities

Rules:
- API-specific logic stays here.
- Reusable components belong in qa-core.

---

### qa-web

Purpose:
- Web UI testing capabilities.

Responsibilities:
- Browser interaction
- Page objects
- Web automation utilities

Rules:
- Selenium-specific implementation stays here.
- Avoid mixing API and UI concerns.

---

### qa-test

Purpose:
- Test execution layer.

Responsibilities:
- Test scenarios
- Test suites
- Test orchestration

Rules:
- Tests should use framework capabilities.
- Avoid duplicating framework logic.

---

### qa-reporting

Purpose:
- Test result reporting.

Responsibilities:
- Reports
- Test execution information
- Result formatting

---

### qa-ai

Purpose:
- AI-related functionality.

Responsibilities:
- AI integrations
- AI-assisted testing capabilities
- Future MCP/client integrations

Rules:
- AI functionality should remain isolated.
- Do not spread AI dependencies across all modules.

---

## Dependency Direction

Preferred dependency flow:

qa-test
|
+--> qa-api
|
+--> qa-web
|
+--> qa-reporting
|
+--> qa-core


qa-ai remains independent unless integration is explicitly required.

---

## Architectural Rules

Before creating a new module:

Ask:
- Does an existing module already own this responsibility?
- Is the complexity justified?

Prefer:
- extending existing modules
- small focused changes
- clear ownership

Avoid:
- unnecessary layers
- premature abstractions
- duplicate functionality