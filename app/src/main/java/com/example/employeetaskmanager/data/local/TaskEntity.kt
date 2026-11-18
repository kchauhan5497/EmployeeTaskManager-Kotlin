package com.example.employeetaskmanager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.employeetaskmanager.data.model.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean
)

fun TaskEntity.toTask() = Task(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted
)

fun Task.toEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted
)
