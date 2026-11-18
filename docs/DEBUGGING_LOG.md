# Debugging and Verification Log

## Latest Final Codex Review — 2026-11-18

This review entry records checks performed on 2026-11-18. The Day 4 notes dated 2026-11-17 below are historical records of the implementation and checks at that time; they do not replace or establish the latest results.

### Build and unit tests

- **PASS:** Ran `.\\gradlew.bat app:assembleDebug app:testDebugUnitTest`; Gradle completed with exit code 0 and reported `BUILD SUCCESSFUL`.
- **PASS:** Forced a fresh unit-test execution with `.\\gradlew.bat app:testDebugUnitTest --rerun-tasks`. The command completed with exit code 0 and reported `BUILD SUCCESSFUL` (27 Gradle tasks executed).
- The source contains 13 unit-test methods: 5 in `TaskRepositoryTest`, 7 in `TaskViewModelTest`, and 1 in `ExampleUnitTest`. The forced unit-test task completed successfully. Instrumented tests, including `TaskDatabasePersistenceTest`, were not run.

### Emulator checks

- **PASS:** Added “Codex Review Check 2026”; the task appeared and the title field cleared.
- **PASS:** Submitted a blank title; “Task title cannot be empty” appeared and the task count remained unchanged.
- **PASS:** Marked the review task complete; its badge changed to “Completed” and its completion button disappeared.
- **PASS:** Rotated to landscape and back to portrait. The task and Completed status remained after rotation.
- **PASS:** Tapped “Task Detail Button Click”. Logcat recorded `onTaskDetailButtonClicked` and `showTaskDetailToast`.
- **NOT TESTED:** The brief Toast overlay was not captured onscreen; handler invocation was confirmed in Logcat.
- **PASS:** Logcat showed `EmployeeApp` lifecycle messages during launch and rotation, including start/resume and pause/stop.
- **FAIL:** In landscape, the form occupies the available screen and the summary and task list are not visible.
- **NOT TESTED:** Empty-list behavior, long-title wrapping, whitespace-only input through emulator interaction, rapid repeated taps, and process-death behavior. No instrumented test task was run.

### Unresolved architecture and documentation discrepancy

The approved Day 3 plan and architecture documents specify an in-memory repository-owned `MutableStateFlow<List<Task>>`. The current implementation constructs a Room-backed repository using `TaskDatabase` and `TaskDao`. This mismatch is unresolved. The Room implementation is not being re-described as the approved Day 3 architecture, and no architecture change was made as part of this review.

The full final review is recorded in `docs/CODEX_REVIEW.md`. No app source code was changed and no commit or push was made for the review.

---

## Historical Day 4 Implementation Notes (2026-11-17)

### Bug Challenge A: Button Tap Does Nothing

- **Bug Description:** Tapping the "Add Task" button or Day 2 "Task Detail Button Click" button does not execute the expected action or update the UI.
- **Steps to Reproduce:**
  1. Open `TaskListScreen`.
  2. Enter text into the "Task Title *" field.
  3. Tap "Add Task" button when the `onClick` listener lambda is un-wired or title state is not bound to `onTitleInputChanged`.
  4. Observe that no new task card appears in the list and input fields do not clear.
- **Breakpoint / Debugging Investigation:**
  - Set breakpoint on `Button.onClick` lambda in `TaskListScreen.kt` and `TaskViewModel.addTask()`.
  - Traced execution flow from Compose UI touch target down to `TaskViewModel`.
  - Inspected `uiState` snapshot values in Debugger (`titleInput`, `errorMessage`).
  - Discovered `titleInput` was evaluating to empty string due to unbound text change listener, or `onClick` lambda was empty `{}`.
- **Actual Root Cause:**
  - Unconnected `onClick` callback lambda or missing `onTitleInputChanged` call on `OutlinedTextField`, causing `addTask()` to validate against an empty string (`currentTitle.trim().isEmpty()`) and abort.
- **Fix Implemented:**
  - Explicitly wired `onValueChange = { viewModel.onTitleInputChanged(it) }` on the title `OutlinedTextField`.
  - Connected `Button(onClick = { viewModel.addTask() })` to invoke `TaskViewModel.addTask()`.
  - Connected `OutlinedButton(onClick = { onTaskDetailClick(context) })` to invoke `onTaskDetailButtonClicked(context)` for Day 2 Toast functionality.
- **Retest Procedure:**
  - Executed unit tests `addTask_validTitle_addsTaskAndResetsInputs` and `onTitleInputChanged_updatesTitleAndClearsError` via `./gradlew app:testDebugUnitTest`.
- **Actual Retest Result:** PASSED (13/13 unit tests passed cleanly).

---

### Bug Challenge B: Task List Fails to Display or Update Correctly

- **Bug Description:** Added tasks do not render in the `LazyColumn`, or marking a task complete fails to update the task status badge and UI card state.
- **Steps to Reproduce:**
  1. Add a new valid task.
  2. Task is added to repository, but `TaskListScreen` UI does not refresh to display the new task card.
  3. Tap "Mark Complete" on a task; status badge remains "Pending".
- **Breakpoint / Debugging Investigation:**
  - Inspected `TaskViewModel` UI state stream (`StateFlow<TaskListUiState>`) and `TaskRepository` state flow (`MutableStateFlow<List<Task>>`).
  - Analyzed Compose recomposition triggers and `StateFlow` emission equality rules (`oldState == newState`).
  - Observed that if `uiState.value` is read as a plain snapshot in Composable without `collectAsState()`, Compose fails to subscribe to state flow updates.
  - Observed that mutating an existing list in-place in `TaskRepository` prevents `MutableStateFlow` from emitting updates because reference equality holds.
- **Actual Root Cause:**
  - Non-reactive state reading in Compose UI (missing `collectAsState()`) and/or in-place list mutation in repository causing `StateFlow` to drop duplicate object reference emissions.
- **Fix Implemented:**
  1. Used `val uiState by viewModel.uiState.collectAsState()` in `TaskListScreen.kt` for reactive Compose state observation.
  2. Implemented immutable list creation (`currentList + newTask` and `currentList.map { ... }`) in `TaskRepository.kt` to guarantee distinct StateFlow emissions.
  3. Scoped state updates in `TaskViewModel` to update `TaskListUiState` synchronously upon repository modifications.
- **Retest Procedure:**
  - Executed full unit test suite `app:testDebugUnitTest` covering task list initialization, task addition, whitespace trimming, duplicate title unique ID generation, and idempotent completion.
  - Executed `./gradlew app:assembleDebug` to verify compilation.
- **Actual Retest Result:** PASSED (13/13 unit tests passed; `app:assembleDebug` BUILD SUCCESSFUL).

---

### Additional Confirmed Defects & Codex Review Fixes

#### Codex Fix 1: ViewModel Repository Observation
- **Defect Description:** `TaskViewModel` initialized `_uiState` snapshot with `repository.tasks.value`, but lacked a reactive collector for background or external updates to `TaskRepository.tasks`.
- **Root Cause:** If `TaskRepository` modified tasks outside of a direct ViewModel action method, `TaskListUiState` would not automatically react or update Compose UI.
- **Fix Implemented:** Added an asynchronous coroutine flow collector in `TaskViewModel.init`:
  ```kotlin
  init {
      viewModelScope.launch {
          repository.tasks.collect { tasks ->
              _uiState.update { it.copy(tasks = tasks) }
          }
      }
  }
  ```
- **Retest Procedure & Result:** Added unit test `repositoryUpdates_areReflectedInUiState` in `TaskViewModelTest.kt`. Verified via `./gradlew app:testDebugUnitTest` (13/13 tests PASSED).

#### Codex Fix 2: Activity-Scoped ViewModel & Configuration Change Persistence
- **Defect Description:** Creating a ViewModel instance inside Composable scope risks re-instantiation across Activity re-creations, causing in-memory task repository state to reset upon screen rotation.
- **Root Cause:** Missing Activity lifecycle scoping for `TaskViewModel`.
- **Fix Implemented:** Instantiated `TaskViewModel` using Activity lifecycle scope in `MainActivity.kt`:
  ```kotlin
  val taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
  ```
  and passed `taskViewModel` into `TaskListScreen`.
- **Retest Procedure & Result:** Verified clean compilation via `./gradlew app:assembleDebug` (BUILD SUCCESSFUL) and unit test suite (13/13 tests PASSED).

---

## Historical Audits

### 2026-11-17 — Day 3 documentation audit

- **Scope:** Documentation and project context only.
- **Findings corrected:** Completion requirement wording that implied toggling; undefined maximum title length; plan diagram’s unclear storage ownership/lifecycle; missing explicit step dependencies and Codex review findings; missing context/status records; stale README Day 3 roadmap wording.
- **Mermaid:** Source reviewed for declaration, nodes, edge syntax, labels, and no embedded Markdown fences.
- **Result:** Day 3 documentation aligned.
