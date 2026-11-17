# Employee Task Manager - Requirements Document

**Document Version:** 1.2 (Audited)  
**Phase:** Day 3 Planning  
**Project:** `EmployeeTaskManager-Kotlin`  

---

## 1. Project Objective

The objective of the **Employee Task Manager** application is to provide a streamlined mobile interface for employees to view assigned tasks, create new tasks, update task completion status, and monitor task status in real time using modern Android and Jetpack Compose standards.

---

## 2. Scope

### In-Scope Capabilities
- Displaying a list of employee tasks with titles, descriptions, and completion status.
- Interface for adding new tasks with validation.
- Marking pending tasks as complete.
- Visual status indicators distinguishing pending and completed tasks.
- Maintaining compatibility with existing Day 1 and Day 2 activity lifecycle tracking and logging.

### Core Requirement Baseline
> *"Employees must view tasks, add tasks, mark complete, and see status."*

---

## 3. Functional Requirements

| Requirement ID | Component | Description |
| :--- | :--- | :--- |
| **FR-1** | Task List View | The system shall display a list of all current tasks showing their title, description, and status. |
| **FR-2** | Add Task | The system shall provide an input interface allowing employees to submit a new task with a title and optional description. |
| **FR-3** | Mark Complete | The system shall allow employees to mark a Pending task as Completed. Completion is one-way and idempotent; repeating the action leaves the task Completed. |
| **FR-4** | Status Visibility | The system shall visually distinguish between "Pending" and "Completed" tasks using clear status badges or indicators. |
| **FR-5** | Input Validation | The system shall reject blank or whitespace-only task titles after trimming. No fixed maximum title length is defined for this phase; long titles must wrap in the UI. |

---

## 4. Non-Functional Requirements

| Requirement ID | Category | Description |
| :--- | :--- | :--- |
| **NFR-1** | Usability | The user interface shall use Jetpack Compose and Material 3 design principles for a clean, intuitive layout. |
| **NFR-2** | Performance | Task additions and status updates should appear promptly without noticeable UI delays. |
| **NFR-3** | Compatibility | The application shall be compatible with Android API 24+ (minSdk 24, targetSdk 37). |
| **NFR-4** | Architecture | Code shall follow separation of concerns separating Compose UI, State management, and Data models. |
| **NFR-5** | Preservation | Existing Day 1 and Day 2 functionality (Activity lifecycle callbacks, `MainActivity.kt` logging, and Toast handlers) shall remain untouched and fully operational. |

---

## 5. User Stories

- **US-1 (View Tasks):**  
  *As an employee, I want to view my list of assigned tasks so that I can see what work needs to be done.*

- **US-2 (Add Task):**  
  *As an employee, I want to add a new task so that I can track additional work assigned to me.*

- **US-3 (Mark Task Complete):**  
  *As an employee, I want to mark a task as complete so that my task list reflects my progress.*

- **US-4 (See Task Status):**  
  *As an employee, I want to clearly see the status of each task so that I can distinguish between pending and completed tasks.*

---

## 6. Edge Cases

1. **Empty Task List:**  
   When no tasks exist in the application, the system displays an explicit empty state placeholder message (*"No tasks available. Add a new task to get started!"*) rather than a blank screen.

2. **Empty Task Title:**  
   If an employee attempts to submit a task with an empty or whitespace-only title, the system blocks creation and displays an inline validation error (*"Task title cannot be empty"*).

3. **Duplicate Task:**  
   Duplicate task titles are explicitly allowed, but every task must be assigned a unique task ID (e.g., UUID) to ensure distinct identity and prevent state management key collisions.

4. **Very Long Task Title:**  
   If a task title exceeds standard line length (e.g., > 100 characters), the UI layout wraps text cleanly with multi-line support without clipping or breaking card boundaries.

5. **Multiple Task Additions:**  
   If multiple tasks are added in rapid succession, the state holder queues and processes state updates sequentially without crashing or dropping items.

6. **Marking an Already Completed Task Complete:**  
   If an employee attempts to complete a task that is already completed, the system handles the event idempotently without corrupting state or triggering errors.

7. **Task Status Visibility:**  
   Task status indicators maintain high color contrast and iconography (e.g., checkmarks, distinct badges) across both Light and Dark Material 3 themes.

8. **Invalid Input:**  
   Inputs containing leading/trailing whitespace are sanitized automatically using `trim()`.

9. **App Restart and Data Persistence:**  
   - Initial implementation may use in-memory storage appropriate to the existing project structure.
   - Tasks are not guaranteed to survive application process destruction.
   - UI state should be retained across configuration changes (such as screen rotation) where applicable.
   - Persistent database storage (e.g., Room database) is designated as a future architectural enhancement.

10. **Unexpected User Interactions:**  
    Rapid navigation or configuration changes (e.g., screen rotation, activity pause/resume) preserve UI state without losing uncommitted input or crashing.

---

## 7. Acceptance Criteria

### AC-1: View Task List
- **Given** the employee opens the Task Manager application with existing tasks,
- **When** the task screen renders,
- **Then** all tasks are displayed in a scrollable list with their title, description, and status.

### AC-2: Empty Task List Display
- **Given** the task list contains zero tasks,
- **When** the screen renders,
- **Then** an empty state indicator with the text `"No tasks available"` is displayed.

### AC-3: Add Valid Task
- **Given** the employee is on the task input interface,
- **When** the employee enters `"Prepare Q3 Presentation"` into the title field and taps `"Add Task"`,
- **Then** the task is added to the list with `"Pending"` status, and the title input field is cleared.

### AC-4: Reject Blank Task Title
- **Given** the task title field is blank or contains only spaces,
- **When** the employee taps `"Add Task"`,
- **Then** the task is not added, and an error message `"Task title cannot be empty"` is displayed.

### AC-5: Mark Task as Complete
- **Given** a task in the list has status `"Pending"`,
- **When** the employee taps the `"Complete"` action button on that task,
- **Then** the task status updates to `"Completed"`, and its visual badge reflects completion.

### AC-6: Day 1 & Day 2 Preservation
- **Given** the app is compiled and launched,
- **When** `MainActivity` lifecycle events occur or Day 2 buttons are tapped,
- **Then** `Logcat` debug logs under tag `"EmployeeApp"` and Day 2 Toast handlers execute without errors or regression.

### AC-7: One-Way, Idempotent Completion
- **Given** a task is Pending,
- **When** the employee marks it complete,
- **Then** its status becomes Completed and remains Completed after any repeated completion action; it cannot return to Pending.

---

## 8. Out-of-Scope Features & Resolved Questions

- **Task Priority and Due Date Fields:** OUT OF SCOPE for initial task management requirements.
- **Pre-populated Sample Tasks:** OUT OF SCOPE; application will initially display an empty task list rather than sample tasks.
- Remote cloud server database synchronization (Firebase / REST APIs).
- User authentication and role-based access control (Admin vs. Employee accounts).
- Scheduled background push notifications or system alarms.
- Exporting task lists to external formats (PDF, CSV, Excel).

---

## 9. Assumptions and Constraints

### Data Persistence & State Assumptions
- **In-Memory Storage:** Initial implementation uses in-memory storage suitable for the existing project structure.
- **Process Lifetime:** Tasks exist for the duration of the active app process and are not guaranteed to survive process destruction.
- **Configuration Resilience:** UI state and task input should be retained across configuration changes (e.g., device rotation).
- **Future Enhancement:** Persistent storage (e.g., Room database / DataStore) is planned as a future enhancement.

### Project Constraints
1. **Source Code Protection:** All Day 1 and Day 2 implementations in `MainActivity.kt` (lifecycle callbacks, `onTaskDetailButtonClicked`, `showTaskDetailToast`, `Logcat` logging under tag `"EmployeeApp"`) must remain 100% intact.
2. **Build Configuration Protection:** No modifications to Gradle dependencies (`build.gradle.kts`, `libs.versions.toml`) or `AndroidManifest.xml`.
3. **Planning Phase Constraint:** No Kotlin code implementation during this requirement step.
4. **Scope Constraint:** No unnecessary screen navigation added unless strictly justified by the core requirements.

---

## Verification & File Location Confirmation

- **Exact File Path:** `docs/REQUIREMENTS.md`
- **Application Source Files Modified:** **0 application source files modified.**
