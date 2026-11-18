package com.example.employeetaskmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeetaskmanager.data.model.Task
import com.example.employeetaskmanager.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val titleInput: String = "",
    val descriptionInput: String = "",
    val errorMessage: String? = null
) {
    val totalTasks: Int get() = tasks.size
    val completedTasks: Int get() = tasks.count(Task::isCompleted)
    val pendingTasks: Int get() = totalTasks - completedTasks
}

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.tasks.collect { tasks ->
                _uiState.update { it.copy(tasks = tasks) }
            }
        }
    }

    fun onTitleInputChanged(newTitle: String) {
        _uiState.update { it.copy(titleInput = newTitle, errorMessage = null) }
    }

    fun onDescriptionInputChanged(newDescription: String) {
        _uiState.update { it.copy(descriptionInput = newDescription) }
    }

    fun addTask() {
        val currentTitle = _uiState.value.titleInput
        if (currentTitle.trim().isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Task title cannot be empty") }
            return
        }

        val currentDescription = _uiState.value.descriptionInput
        viewModelScope.launch {
            repository.addTask(currentTitle, currentDescription)
            _uiState.update {
                it.copy(titleInput = "", descriptionInput = "", errorMessage = null)
            }
        }
    }

    fun markTaskComplete(taskId: String) {
        viewModelScope.launch {
            repository.markTaskComplete(taskId)
        }
    }
}
