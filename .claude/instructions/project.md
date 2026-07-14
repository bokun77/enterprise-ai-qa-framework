# Project Context

## Project Name

AI QA Framework


## Purpose

AI QA Framework is a Java-based test automation framework designed to demonstrate AI-assisted software testing and development workflows.

The framework provides reusable capabilities for:
- API testing
- Web UI testing
- Test execution
- Test reporting
- AI-assisted QA activities


## Target Application

The primary application used for testing is OWASP Juice Shop.

The application runs locally using Docker:

docker run --rm -p 3000:3000 bkimminich/juice-shop


## Technology Stack

- Java 21
- Maven
- TestNG
- Selenium
- Rest Assured
- Jackson
- SLF4J / Logback


## Project Philosophy

This project focuses on practical AI-assisted engineering.

The goal is not to create a complex enterprise platform.

Prefer:
- simple solutions
- understandable code
- incremental improvements
- reusable components


Avoid:
- unnecessary abstractions
- premature optimization
- adding dependencies without clear value


## AI Development Principles

When assisting with this project:

1. Understand existing code before making changes.
2. Prefer modifying existing components over creating new ones.
3. Explain architectural decisions.
4. Keep changes small and focused.
5. Ask before introducing major structural changes.