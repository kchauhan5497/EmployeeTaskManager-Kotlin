package com.example.employeetaskmanager

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TaskRepositoryTest {
    private lateinit var dao: FakeTaskDao
    private lateinit var repository: com.example.employeetaskmanager.data.repository.TaskRepository

    @Before
    fun setUp() {
        dao = FakeTaskDao()
        repository = com.example.employeetaskmanager.data.repository.TaskRepository(dao)
    }

    @Test
    fun initialState_isEmpty() = runBlocking {
        assertTrue(repository.tasks.first().isEmpty())
    }

    @Test
    fun addTask_validTitle_addsTaskToList() = runBlocking {
        repository.addTask("Task 1", "Description 1")
        val task = repository.tasks.first().single()

        assertEquals("Task 1", task.title)
        assertEquals("Description 1", task.description)
        assertFalse(task.isCompleted)
    }

    @Test
    fun addTask_whitespaceTrimming_trimsInput() = runBlocking {
        repository.addTask("   Task Trim   ", "   Desc Trim   ")
        val task = repository.tasks.first().single()

        assertEquals("Task Trim", task.title)
        assertEquals("Desc Trim", task.description)
    }

    @Test
    fun addTask_duplicateTitles_assignsUniqueIds() = runBlocking {
        repository.addTask("Duplicate Title")
        repository.addTask("Duplicate Title")
        val tasks = repository.tasks.first()

        assertEquals(2, tasks.size)
        assertEquals(tasks[0].title, tasks[1].title)
        assertNotEquals(tasks[0].id, tasks[1].id)
    }

    @Test
    fun markTaskComplete_isOneWayAndIdempotent() = runBlocking {
        repository.addTask("Task 1")
        val taskId = repository.tasks.first().single().id

        repository.markTaskComplete(taskId)
        repository.markTaskComplete(taskId)

        val tasks = repository.tasks.first()
        assertEquals(1, tasks.size)
        assertTrue(tasks.single().isCompleted)
    }
}
