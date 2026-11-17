# Project Guidance

## Project context
- Android application: Employee Task Manager, with a single `:app` subproject.
- Day 3 is a planning phase titled “Plan before coding.”
- The task workflow is documented in `docs/REQUIREMENTS.md`, `docs/DEVELOPMENT_PLAN.md`, and `docs/ARCHITECTURE.md`.

## Day 3 constraints and status
- Current Coding Status: **NOT STARTED**.
- Current Trainer Approval: **PENDING**.
- Do not represent proposed Day 3 components as implemented.
- Keep Day 3 planning changes in documentation unless the user explicitly authorizes implementation.
- Do not claim builds, app tests, or Mermaid parser checks passed unless they were actually run.

## Architecture decision
The planned flow is `TaskListScreen → TaskViewModel → TaskRepository → In-Memory Storage`. The repository owns the task `MutableStateFlow<List<Task>>`; the ViewModel exposes UI state to the Compose screen. See `docs/ARCHITECTURE.md`.
