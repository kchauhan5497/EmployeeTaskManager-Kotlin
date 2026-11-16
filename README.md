# Employee Task Manager

An Android application developed using **Kotlin, Android Studio, and Jetpack Compose** to streamline task assignment, status updates, and workflow management between administrators and employees.

![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple?style=flat-square&logo=kotlin)
![Android SDK](https://img.shields.io/badge/Platform-Android-green?style=flat-square&logo=android)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue?style=flat-square&logo=jetpackcompose)
![Gradle](https://img.shields.io/badge/Build-Gradle%20Kotlin%20DSL-02569B?style=flat-square&logo=gradle)

---

## Table of Contents

- [Project Overview](#project-overview)
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

The **Employee Task Manager** mobile application simplifies workflow coordination between management and team members. Built using modern Android architecture, declarative UI with Jetpack Compose, and Kotlin best practices, this application provides an intuitive platform for assigning tasks, tracking completion progress, and organizing employee task histories.

---

## Documentation

- **Code Walkthrough & Debugging Guide:** [CODE_WALKTHROUGH.md](CODE_WALKTHROUGH.md)  
  Contains detailed technical documentation on `MainActivity`, Android Activity lifecycle callbacks, Logcat execution tracking, runtime breakpoint workflows, and the Day 2 Debugging Lab crash report.

---

## Technologies & Tools

| Category | Technology / Tool |
| :--- | :--- |
| **Language** | Kotlin |
| **IDE** | Android Studio |
| **UI Framework** | Jetpack Compose (Material 3) |
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
│       └── main/
│           ├── java/com/example/employeetaskmanager/
│           │   ├── MainActivity.kt
│           │   └── ui/theme/
│           │       ├── Color.kt
│           │       ├── Theme.kt
│           │       └── Type.kt
│           ├── res/
│           └── AndroidManifest.xml
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── CODE_WALKTHROUGH.md
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
- [ ] **Day 3: UI Development & Screen Navigation** *(Upcoming)*
  - Multi-screen navigation setup with Jetpack Compose Navigation.
  - Task list and task detail screens.

*This project is under active development.*

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

- **Logcat Output Verification:** Confirmed that state transition logs and button click logs appear under tag `EmployeeApp`.
- **Live Breakpoint Execution:** Performed runtime breakpoint debugging in Android Studio on `onStart()`, `onResume()`, and `onTaskDetailButtonClicked()`. Inspected call stack frames and `savedInstanceState` variables.
- **Build Verification:** Verified clean compilation via Gradle (`app:assembleDebug`).

---

## Planned Features & Roadmap

- [ ] **Employee Login & Registration:** Role-based authentication system.
- [ ] **Admin Dashboard:** Administrative panel for managing employees and tasks.
- [ ] **Task Assignment:** Ability for admins to assign tasks with priorities and deadlines.
- [ ] **Task Progress Tracking:** Employee task status updates (Pending, In Progress, Completed).
- [ ] **Employee Task History:** Comprehensive history and task performance analytics.

---

## Developer Information

- **Developer:** [kchauhan5497](https://github.com/kchauhan5497)
- **Repository:** [EmployeeTaskManager-Kotlin](https://github.com/kchauhan5497/EmployeeTaskManager-Kotlin)
