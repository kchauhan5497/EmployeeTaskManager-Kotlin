# Employee Task Manager

An Android application developed using **Kotlin, Android Studio, Jetpack Compose, and Room Database** to streamline task assignment, status updates, and workflow management between administrators and employees.

![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple?style=flat-square&logo=kotlin)
![Android SDK](https://img.shields.io/badge/Platform-Android-green?style=flat-square&logo=android)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue?style=flat-square&logo=jetpackcompose)
![Room](https://img.shields.io/badge/Database-Room-4285F4?style=flat-square&logo=android)
![Gradle](https://img.shields.io/badge/Build-Gradle%20Kotlin%20DSL-02569B?style=flat-square&logo=gradle)

---

## Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Documentation](#documentation)
- [Technologies & Tools](#technologies--tools)
- [Project Directory Structure](#project-directory-structure)
- [Installation & Setup](#installation--setup)
- [Application Execution](#application-execution)
- [Development Progress & Checklist](#development-progress--checklist)
- [Day 2 Development Details](#day-2-development-details)
- [Home to Task Details Button Click Flow](#home-to-task-details-button-click-flow)
- [Debugging & Verification](#debugging--verification)
- [Planned Features & Roadmap](#planned-features--roadmap)
- [Developer Information](#developer-information)

---

## Project Overview

The **Employee Task Manager** mobile application simplifies workflow coordination between management and team members. Built using modern Android MVVM architecture, declarative UI with Jetpack Compose, local Room database persistence, and Kotlin best practices, this application provides an intuitive platform for creating tasks, tracking completion progress, and organizing employee task histories.

---

## Architecture

The app follows a modern Android MVVM (Model-View-ViewModel) architecture backed by a local Room database (`employee_tasks.db`):

- **View (`TaskListScreen.kt`):** Declarative Jetpack Compose UI with screen-level scrolling via a single `LazyColumn`, exposing input fields, summary counters, and task cards.
- **ViewModel (`TaskViewModel.kt`):** Activity-scoped state holder managing `TaskListUiState`, validating task inputs (non-blank titles), and delegating actions to the repository.
- **Repository (`TaskRepository.kt`):** Single source of truth mapping domain `Task` models to `TaskEntity` objects and managing operations via `TaskDao`.
- **Database (`TaskDatabase.kt` & `TaskDao.kt`):** Room SQLite database providing persistent storage and reactive data streams (`Flow<List<TaskEntity>>`).

---

## Documentation

- [Day 3 Requirements](docs/REQUIREMENTS.md)
- [Day 3 Development Plan](docs/DEVELOPMENT_PLAN.md)
- [As-Built Architecture (Room)](docs/ARCHITECTURE.md)
- [Architecture Mermaid source](docs/diagrams/architecture.mmd)
- [Architecture Decisions](docs/DECISIONS.md)
- [Docs Project Status](docs/CURRENT_STATUS.md)
- [Root Project Status](CURRENT_STATUS.md)
- [Docs Debugging Log](docs/DEBUGGING_LOG.md)
- [Root Debugging Log](DEBUGGING_LOG.md)
- **Code Walkthrough & Debugging Guide:** [CODE_WALKTHROUGH.md](CODE_WALKTHROUGH.md)  
  Contains technical documentation on `MainActivity`, Activity lifecycle callbacks, Logcat, runtime breakpoints, and the Day 2 debugging exercise.

---

## Technologies & Tools

| Category | Technology / Tool |
| :--- | :--- |
| **Language** | Kotlin |
| **IDE** | Android Studio |
| **UI Framework** | Jetpack Compose (Material 3) |
| **Architecture Pattern** | MVVM (Model-View-ViewModel) |
| **Database / Persistence** | Room Persistence Library (`employee_tasks.db`) |
| **Core Components** | Android Activity Lifecycle (`ComponentActivity`), `android.util.Log`, `Toast` |
| **Build System** | Gradle (Kotlin DSL) |
| **Version Control** | Git & GitHub |

---

## Project Directory Structure

```text
EmployeeTaskManager/
│
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/com/example/employeetaskmanager/
│       │   │   ├── MainActivity.kt
│       │   │   ├── data/
│       │   │   │   ├── db/
│       │   │   │   │   ├── TaskDao.kt
│       │   │   │   │   └── TaskDatabase.kt
│       │   │   │   ├── model/
│       │   │   │   │   └── Task.kt
│       │   │   │   └── repository/
│       │   │   │       └── TaskRepository.kt
│       │   │   └── ui/
│       │   │       ├── screens/
│       │   │       │   └── TaskListScreen.kt
│       │   │       ├── theme/
│       │   │       └── viewmodel/
│       │   │           └── TaskViewModel.kt
│       │   └── AndroidManifest.xml
│       └── androidTest/
│           └── java/com/example/employeetaskmanager/
│               └── TaskDatabasePersistenceTest.kt
├── docs/
│   ├── REQUIREMENTS.md
│   ├── DEVELOPMENT_PLAN.md
│   ├── ARCHITECTURE.md
│   ├── CURRENT_STATUS.md
│   ├── DECISIONS.md
│   ├── DEBUGGING_LOG.md
│   └── diagrams/architecture.mmd
├── AGENTS.md
├── build.gradle.kts
├── settings.gradle.kts
├── CODE_WALKTHROUGH.md
├── CURRENT_STATUS.md
├── DEBUGGING_LOG.md
└── README.md
```

---

## Installation & Setup

1. **Install Android Studio:** Ensure Android Studio (Ladybug or newer) is installed with the latest Android SDK.
2. **Clone the Repository:**
   ```bash
   git clone https://github.com/kchauhan5497/EmployeeTaskManager-Kotlin.git
   ```
3. **Open Project:** Open Android Studio and select **Open**, then select the `EmployeeTaskManager` project folder.
4. **Gradle Sync:** Allow Android Studio to sync Gradle dependencies automatically.

---

## Application Execution

1. **Configure Emulator / Device:** Launch an Android Virtual Device (AVD) or connect a physical Android device via USB with USB Debugging enabled.
2. **Run Application:** Click the **Run 'app'** button (or press `Shift + F10`) in Android Studio.
3. **Logcat Monitoring:** Open the Logcat panel in Android Studio and filter by `tag:EmployeeApp` to observe lifecycle events and click interactions.

---

## Development Progress & Checklist

- [x] **Day 1: Project Setup & Environment Configuration**
  - Android Studio project initialization.
  - Gradle Kotlin DSL build configuration.
  - Initial repository structure setup.
- [x] **Day 2: Activity Lifecycle, Debugging Lab & Button Event Flow**
  - Override Activity lifecycle callbacks (`onCreate`, `onStart`, `onResume`, `onPause`, `onStop`).
  - `MainActivity` setup using `ComponentActivity` and Jetpack Compose `Scaffold`.
  - Logcat debug logging under tag `"EmployeeApp"`.
  - 4-step debugging lab methodology (Reproduce → Observe → Isolate → Verify).
  - Syntax error resolution for top-level function scoping.
  - Live breakpoint debugging during runtime and stack frame inspection.
  - Home screen button click event flow and Toast notification implementation.
  - Technical walkthrough document (`CODE_WALKTHROUGH.md`).
- [x] **Day 3: Plan Before Coding** — COMPLETED
  - Requirements, user stories, edge cases, and acceptance criteria documented.
  - Architecture planning documents created (`docs/REQUIREMENTS.md`, `docs/DEVELOPMENT_PLAN.md`, `docs/ARCHITECTURE.md`).
- [x] **Day 4: Build the First Feature, Debug and Deliver** — COMPLETED
  - Implemented MVVM architecture with Room database persistence (`Task`, `TaskEntity`, `TaskDao`, `TaskDatabase`, `TaskRepository`, `TaskViewModel`, and `TaskListScreen`).
  - Verified core features:
    - **Add Task:** Required non-blank title validation, optional description, sanitized input handling.
    - **View Task List:** Scrollable list displaying task items, status counters (Total, Pending, Completed).
    - **Mark Complete:** One-way, idempotent status transition with visual badge updates ("Pending" $\rightarrow$ "Completed").
    - **Day 2 Action:** Integrated Toast handler button (`"Task Detail Button Click"`).
  - Build and testing verification:
    - Debug APK build passed (`./gradlew app:assembleDebug`).
    - 13 unit tests passed (`./gradlew app:testDebugUnitTest --rerun-tasks`).
    - Instrumented Room database-reopen persistence test passed (`./gradlew app:connectedDebugAndroidTest`).
    - UI scrolling verified on emulator in both portrait and landscape orientations.
  - Note: End-to-end application behavior after full process restart remains untested.

---

## Day 2 Development Details

### Activity Lifecycle Implementation

`MainActivity.kt` overrides key Android Activity lifecycle callbacks to monitor activity state transitions:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    android.util.Log.d("EmployeeApp", "MainActivity started")
    enableEdgeToEdge()
    setContent {
        EmployeeTaskManagerTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Greeting(name = "Android", modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

override fun onStart() {
    super.onStart()
    android.util.Log.d("EmployeeApp", "Activity Started")
}

override fun onResume() {
    super.onResume()
    android.util.Log.d("EmployeeApp", "Activity Resumed")
}

override fun onPause() {
    super.onPause()
    android.util.Log.d("EmployeeApp", "Activity Paused")
}

override fun onStop() {
    super.onStop()
    android.util.Log.d("EmployeeApp", "Activity Stopped")
}
```

### Four-Step Debugging Methodology

During Day 2 development, a 4-step debugging process was applied to isolate and resolve compilation scope errors:

1. **Reproduce:** Triggered compilation failure where `override` functions were declared outside `class MainActivity`.
2. **Observe:** Examined compiler errors: `Modifier 'override' is not applicable to 'top level function'`.
3. **Isolate:** Identified a misplaced closing brace `}` ending `class MainActivity` prematurely before callback declarations.
4. **Verify:** Encapsulated lifecycle methods within `class MainActivity` and verified build success via `./gradlew app:assembleDebug`.

---

## Home to Task Details Button Click Flow

### UI Implementation & Layout
A button labeled **"Task Detail Button Click"** is placed on the Home screen inside the `Greeting` composable function (`MainActivity.kt`). It is centered vertically below the text message inside a Jetpack Compose `Column` container.

### Functions & Responsibilities

| Function Name | Location | Responsibility | Logcat Log Message |
| :--- | :--- | :--- | :--- |
| `Greeting(...)` | `MainActivity.kt` | `@Composable` UI component rendering the Home screen layout. | N/A |
| `Button(onClick)` | `MainActivity.kt` | Event callback lambda capturing touch events. | `"Button onClick triggered"` |
| `onTaskDetailButtonClicked(...)` | `MainActivity.kt` | Intermediate handler function delegating event execution. | `"onTaskDetailButtonClicked called"` |
| `showTaskDetailToast(...)` | `MainActivity.kt` | Utility function displaying the Android Toast popup. | `"showTaskDetailToast called - displaying Toast"` |

### Implementation Code

```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hello $name!")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                android.util.Log.d("EmployeeApp", "Button onClick triggered")
                onTaskDetailButtonClicked(context)
            }
        ) {
            Text(text = "Task Detail Button Click")
        }
    }
}

fun onTaskDetailButtonClicked(context: Context) {
    android.util.Log.d("EmployeeApp", "onTaskDetailButtonClicked called")
    showTaskDetailToast(context)
}

fun showTaskDetailToast(context: Context) {
    android.util.Log.d("EmployeeApp", "showTaskDetailToast called - displaying Toast")
    Toast.makeText(context, "Button is clicked!", Toast.LENGTH_SHORT).show()
}
```

### Execution Step Sequence

```text
User Taps "Task Detail Button Click"
  │
  ├──► Greeting(...) [Composable UI Component]
  │
  ├──► Button(onClick) [Event Callback Lambda] ──► Log: "Button onClick triggered"
  │
  ├──► onTaskDetailButtonClicked(...) [Handler Function] ──► Log: "onTaskDetailButtonClicked called"
  │
  └──► showTaskDetailToast(...) [Toast Utility] ──► Log: "showTaskDetailToast called - displaying Toast"
         │
         └──► Toast: "Button is clicked!" displayed on screen
```

---

## Debugging & Verification

- **Debug Build Verification:** `./gradlew app:assembleDebug` completed successfully (`BUILD SUCCESSFUL`).
- **Unit Testing:** `./gradlew app:testDebugUnitTest --rerun-tasks` passed with 13 unit tests verified (5 in `TaskRepositoryTest`, 7 in `TaskViewModelTest`, 1 in `ExampleUnitTest`).
- **Instrumented Database Persistence:** `./gradlew app:connectedDebugAndroidTest` passed on the connected emulator. `TaskDatabasePersistenceTest` verified that task insertion and task completion status persist across closing and reopening a Room database instance.
- **UI & Orientation Verification:** Evaluated `TaskListScreen` on emulator in both portrait and landscape orientations. Confirmed single screen-level `LazyColumn` scrolling allows access to Toast action button, form inputs, task summary header, and task item cards.
- **Logcat Output Verification:** Confirmed Activity lifecycle state transitions and button click events under tag `EmployeeApp`.
- **Live Breakpoint Execution:** Performed runtime breakpoint debugging on `onStart()`, `onResume()`, and `onTaskDetailButtonClicked()`.
- **Unverified Scope:** End-to-end application behavior after full process restart (process death / app relaunch) has not been tested.

---

## Planned Features & Roadmap

- [ ] **Employee Login & Registration:** Role-based authentication system.
- [ ] **Admin Dashboard:** Administrative panel for managing employees and tasks.
- [ ] **Task Assignment:** Ability for admins to assign tasks with priorities and deadlines.
- [ ] **Task Progress Tracking:** Enhanced task lifecycle analytics.
- [ ] **Employee Task History:** Comprehensive history and task performance reporting.

---

## Developer Information

- **Developer:** [kchauhan5497](https://github.com/kchauhan5497)
- **Repository:** [EmployeeTaskManager-Kotlin](https://github.com/kchauhan5497/EmployeeTaskManager-Kotlin)
