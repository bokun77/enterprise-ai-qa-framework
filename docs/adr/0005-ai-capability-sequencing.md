# ADR 0005: AI Capability Sequencing

## Status

Accepted

## Context

ADR-0004 defined the AI Architecture Strategy and identified six AI capabilities for ai-qa-framework:

- Test Generator
- Locator Intelligence
- Failure Analyzer
- Test Data Generator
- Documentation Assistant
- Reporting Intelligence

The `qa-ai` module exists in the repository but contains no implementation. It must remain isolated from the core automation engine, as established in ADR-0004.

`qa-reporting` has progressed ahead of `qa-ai` and now produces execution artifacts, including screenshots and an execution summary, via `ExecutionReportWriter`. This is the first module to generate structured, historical execution data that an AI capability could consume.

Attempting to implement all six AI capabilities at once would violate the modularity principles set out in ADR-0004 and would introduce AI dependencies before the framework has a reliable source of input data. A sequencing decision is needed to determine which AI capability should be implemented first, and what must be true before that work begins.

## Decision

The first AI capability to be implemented in `qa-ai` will be the **Failure Analyzer**.

This choice is driven by data availability, not implementation priority in the abstract: `qa-reporting` already produces the execution artifacts (screenshots, execution summaries) that a Failure Analyzer needs as input. None of the other five capabilities currently have an equivalent upstream data source in the framework:

- Test Generator needs requirements/user-story input that does not yet exist in the framework.
- Locator Intelligence needs locator usage and failure history not yet captured.
- Test Data Generator needs schema/data-model input not yet defined.
- Documentation Assistant needs a stable set of artifacts across multiple capabilities to summarize.
- Reporting Intelligence needs trend data across multiple executions, not just a single run.

Failure Analyzer is therefore the capability with the shortest path from "data exists" to "AI can act on it."

This ADR defines sequencing and prerequisites only. It does not authorize or begin AI implementation work. `qa-ai` remains an empty, isolated module until its trigger condition (below) is met and a follow-up ADR/plan authorizes implementation. No new modules or dependencies are introduced by this decision.

## Consequences

Any future implementation must consume reporting artifacts through a defined contract and must not introduce dependencies from existing automation modules into `qa-ai`.

Positive:

- Establishes a clear, data-driven order for AI capability work instead of an arbitrary one.
- Keeps `qa-ai` isolated and dependency-free until there is a concrete reason to change that.
- Gives `qa-reporting` a defined consumer, validating its execution artifact design.
- Reduces risk of building AI capabilities against speculative or unstable input data.

Trade-offs:

- Other AI capabilities (Test Generator, Locator Intelligence, etc.) are explicitly deferred and will need their own prerequisite data sources defined before they can be sequenced.
- Failure Analyzer's design will be constrained by whatever `ExecutionReportWriter` currently produces; changes to that output shape may require revisiting this ADR.

## Future Trigger

Implementation work on `qa-ai` (starting with the Failure Analyzer) may begin once:

1. `qa-reporting` execution artifacts (screenshots, execution summary) are considered stable, and
2. A follow-up ADR or implementation plan defines the Failure Analyzer's scope, inputs, and how `qa-ai` will consume `qa-reporting` output without breaking module isolation.

Until both conditions are met, `qa-ai` remains a placeholder module.
