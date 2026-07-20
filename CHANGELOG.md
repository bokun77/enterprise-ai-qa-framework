# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [1.0.0] - 2026-07-20

### Added

- Multi-module Maven project structure: `qa-core`, `qa-web`, `qa-api`, `qa-reporting`, `qa-ai`, `qa-test`
- `TestConfig` for base URI and credential configuration via system properties
- Selenium WebDriver support via `WebDriverFactory`, with headless/CI-compatible Chrome configuration
- Page Object Model: `BasePage`, `JuiceShopHomePage`, `LoginPage`, `ProductSearchPage`
- `ApiClient`, a minimal REST Assured wrapper for API test scenarios
- API test coverage: products endpoint, login endpoint (valid/invalid credentials), and product search endpoint (matching results, no results, empty query, special characters, and a SQL-injection regression check)
- UI test coverage: home page load, login (valid/invalid credentials), logout, product search (including edge cases), and add-to-cart
- `ExecutionReportWriter` and `ExecutionListener` for a plain-text pass/fail/skip execution summary, with automatic screenshot capture on UI test failure
- TestNG suite configuration (`testng.xml`) grouping `api` and `ui` tests
- GitHub Actions CI workflow running the full suite against a Juice Shop service container, uploading execution summary and screenshots as build artifacts
- Architecture Decision Records: Java 21 ([ADR-0001](docs/adr/0001-use-java-21.md)), Selenium ([ADR-0002](docs/adr/0002-use-selenium.md)), TestNG ([ADR-0003](docs/adr/0003-use-testng.md)), AI architecture strategy ([ADR-0004](docs/adr/0004-ai-architecture.md)), and AI capability sequencing ([ADR-0005](docs/adr/0005-ai-capability-sequencing.md))
- `docs/AI_STRATEGY.md` describing the AI-assisted development approach for the project itself

### Fixed

- Stabilized `LogoutTest` and hardened `BasePage` click handling for reliability in CI
