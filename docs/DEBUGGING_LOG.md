# Debugging and Verification Log

## Latest verification after the landscape scrolling fix — 2026-11-18

### Build and automated tests

- **PASS:** `.\gradlew.bat app:assembleDebug` completed successfully after the scrolling change.
- **PASS:** `.\gradlew.bat app:testDebugUnitTest --rerun-tasks` completed with exit code 0 and `BUILD SUCCESSFUL`; Gradle reported 13 tests completed (5 repository, 7 ViewModel, 1 example).
- **PASS:** `.\gradlew.bat app:connectedDebugAndroidTest` completed with exit code 0 and `BUILD SUCCESSFUL` on the connected emulator.
- The instrumented suite contains one test, `tasksAndCompletionStatusSurviveDatabaseReopen`. It inserts pending and completed rows, completes a row, closes and reopens the same uniquely named Room database, then verifies the rows and completion state.
- **NOT TESTED:** End-to-end application behavior after killing and relaunching the app. The instrumented test reopens a test database; it does not kill or relaunch the app or exercise the production `employee_tasks.db` singleton path.

### Landscape scrolling and orientation checks

- **PASS:** Deployed the updated app to the emulator. In landscape, the screen's single scrollable container included the Toast action, form, summary, and task cards. A vertical swipe revealed the summary and list; a further swipe advanced through task cards.
- **PASS:** The updated portrait screen also exposed the same screen-level scrollable container and task content.
- The implementation keeps the form and summary in the header item of the screen-level `LazyColumn`; task cards remain keyed lazy items in that list. No nested lazy list is used.

### Other recorded emulator checks

- **PASS (previous same-day emulator check):** A valid task was added and the title field cleared; blank-title submission displayed `Task title cannot be empty` without increasing the task count.
- **PASS (previous same-day emulator check):** Marking a pending task complete changed its badge to Completed and removed its completion action.
- **PASS (previous same-day emulator check):** Task and completion state remained after rotating landscape and back to portrait.
- **PASS:** Tapping the Day 2 action logged `onTaskDetailButtonClicked` and `showTaskDetailToast`; lifecycle logs under `EmployeeApp` were visible during launch and rotation.
- **NOT TESTED:** The transient Toast overlay itself was not captured onscreen.

### Current architecture and traceability

The current application source uses Room via `TaskDatabase`, `TaskDao`, and `TaskRepository`. `docs/ARCHITECTURE.md` now describes this as-built implementation. Day 3 planning documents retain the earlier in-memory proposal as planning history; they should not be read as a description of the current storage implementation.

- **PASS:** Room database close/reopen persistence and restoration of a completed status, as covered by the instrumented test.
- **PASS:** One-way completion and repeated completion through the unit-tested fake DAO. The Room test verifies a completion update and restored Completed state.
- **NOT TESTED:** Repeated completion against the real Room DAO, empty-state UI, long-title wrapping, and end-to-end app process restart.

No application source, Gradle file, or test was changed for this documentation update. No commit or push was made.

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
