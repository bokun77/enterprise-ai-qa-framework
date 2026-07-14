# Testing Guidelines

## Testing Philosophy

AI QA Framework focuses on creating reliable, maintainable automated tests.

Tests should provide confidence in application behaviour.

Prefer:
- stable tests
- readable scenarios
- reusable components
- meaningful assertions

Avoid:
- fragile tests
- duplicated logic
- tests without clear purpose

---

## Target Application

The primary application used for testing is:

OWASP Juice Shop

The application is started locally using Docker:

```bash
docker run --rm -p 3000:3000 bkimminich/juice-shop