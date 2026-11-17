# Day 3 Architecture Decisions

**Status:** Proposed plan; trainer approval pending  
**Date:** 2026-11-17

## D3-1: Use MVVM with a repository
Keep Compose UI, UI state/event handling, and task data operations in separate layers:

`TaskListScreen → TaskViewModel → TaskRepository → In-Memory Storage`.

- The screen renders tasks and input controls, collects UI state, and forwards user actions.
- The ViewModel validates input, handles UI events, retains its repository reference across configuration changes, and exposes `StateFlow<TaskListUiState>`.
- The repository provides task read/write operations and owns `MutableStateFlow<List<Task>>`, the task data source.
- The in-memory flow is process-local; data loss after process destruction is an accepted limitation for this phase.

## D3-2: Keep task completion one-way and idempotent
A Pending task can become Completed. Repeating the completion action leaves it Completed; the action never returns it to Pending.

## D3-3: Do not add persistence or dependencies in Day 3
Use the existing dependencies and in-memory storage. Room persistence is a future enhancement, not part of this plan.

## D3-4: Preserve existing Day 1 and Day 2 behavior
The planned Activity integration must retain existing lifecycle logging and Toast behavior. The implementation plan does not authorize coding; Coding Status remains **NOT STARTED** until separately authorized.
