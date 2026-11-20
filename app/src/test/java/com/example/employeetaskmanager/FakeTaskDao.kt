package com.example.employeetaskmanager

import com.example.employeetaskmanager.data.local.TaskDao
import com.example.employeetaskmanager.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeTaskDao : TaskDao {
    private val taskEntities = MutableStateFlow<List<TaskEntity>>(emptyList())

    override fun observeTasks(): Flow<List<TaskEntity>> = taskEntities.asStateFlow()

    override suspend fun insertTask(task: TaskEntity) {
        taskEntities.update { current ->
            current.filterNot { it.id == task.id } + task
        }
    }

    override suspend fun markTaskComplete(taskId: String) {
        taskEntities.update { current ->
            current.map { task ->
                if (task.id == taskId) task.copy(isCompleted = true) else task
            }
        }
    }
}
