package com.example.employeetaskmanager

import com.example.employeetaskmanager.data.repository.TaskRepository
import com.example.employeetaskmanager.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {
    private lateinit var dao: FakeTaskDao
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        dao = FakeTaskDao()
        viewModel = TaskViewModel(TaskRepository(dao))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_isCorrect() {
        val state = viewModel.uiState.value
        assertTrue(state.tasks.isEmpty())
        assertEquals(0, state.totalTasks)
        assertEquals(0, state.completedTasks)
        assertEquals(0, state.pendingTasks)
        assertEquals("", state.titleInput)
        assertEquals("", state.descriptionInput)
        assertNull(state.errorMessage)
    }

    @Test
    fun repositoryUpdates_areReflectedInUiStateAndCounts() {
        kotlinx.coroutines.runBlocking {
            dao.insertTask(
                com.example.employeetaskmanager.data.local.TaskEntity(
                    id = "external-task",
                    title = "Added outside ViewModel",
                    description = "",
                    isCompleted = false
                )
            )
        }

        val state = viewModel.uiState.value
        assertEquals("Added outside ViewModel", state.tasks.single().title)
        assertEquals(1, state.totalTasks)
        assertEquals(0, state.completedTasks)
        assertEquals(1, state.pendingTasks)
    }

    @Test
    fun onTitleInputChanged_updatesTitleAndClearsError() {
        viewModel.addTask()
        assertEquals("Task title cannot be empty", viewModel.uiState.value.errorMessage)

        viewModel.onTitleInputChanged("New Title")
        assertEquals("New Title", viewModel.uiState.value.titleInput)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun onDescriptionInputChanged_updatesDescription() {
        viewModel.onDescriptionInputChanged("New Description")
        assertEquals("New Description", viewModel.uiState.value.descriptionInput)
    }

    @Test
    fun addTask_validTitleAddsTaskAndResetsInputs() {
        viewModel.onTitleInputChanged("Complete Report")
        viewModel.onDescriptionInputChanged("Submit Q3 results")
        viewModel.addTask()

        val state = viewModel.uiState.value
        assertEquals("Complete Report", state.tasks.single().title)
        assertEquals("Submit Q3 results", state.tasks.single().description)
        assertEquals("", state.titleInput)
        assertEquals("", state.descriptionInput)
        assertNull(state.errorMessage)
    }

    @Test
    fun addTask_blankTitleSetsErrorAndBlocksAddition() {
        viewModel.onTitleInputChanged("   ")
        viewModel.addTask()

        assertEquals("Task title cannot be empty", viewModel.uiState.value.errorMessage)
        assertTrue(viewModel.uiState.value.tasks.isEmpty())
    }

    @Test
    fun completionKeepsTaskAndUpdatesSummaryCounts() {
        viewModel.onTitleInputChanged("Test Task")
        viewModel.addTask()
        viewModel.onTitleInputChanged("Another Task")
        viewModel.addTask()

        val id = viewModel.uiState.value.tasks.first().id
        viewModel.markTaskComplete(id)

        val state = viewModel.uiState.value
        assertEquals(2, state.tasks.size)
        assertEquals(2, state.totalTasks)
        assertEquals(1, state.completedTasks)
        assertEquals(1, state.pendingTasks)
        assertTrue(state.tasks.first().isCompleted)
    }
}
