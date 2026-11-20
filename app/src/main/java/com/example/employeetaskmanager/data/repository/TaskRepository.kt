package com.example.employeetaskmanager.data.repository

import com.example.employeetaskmanager.data.local.TaskDao
import com.example.employeetaskmanager.data.local.toEntity
import com.example.employeetaskmanager.data.local.toTask
import com.example.employeetaskmanager.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class TaskRepository(private val taskDao: TaskDao) {
    val tasks: Flow<List<Task>> = taskDao.observeTasks().map { entities ->
        entities.map { it.toTask() }
    }

    suspend fun addTask(title: String, description: String = "") {
        taskDao.insertTask(
            Task(
                id = UUID.randomUUID().toString(),
                title = title.trim(),
                description = description.trim(),
                isCompleted = false
            ).toEntity()
        )
    }

    suspend fun markTaskComplete(taskId: String) {
        taskDao.markTaskComplete(taskId)
    }
}
