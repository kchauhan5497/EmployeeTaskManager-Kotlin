package com.example.employeetaskmanager

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.employeetaskmanager.data.local.TaskDatabase
import com.example.employeetaskmanager.data.local.TaskEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class TaskDatabasePersistenceTest {
    @Test
    fun tasksAndCompletionStatusSurviveDatabaseReopen() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val databaseName = "task-persistence-${UUID.randomUUID()}.db"
        var database = Room.databaseBuilder(context, TaskDatabase::class.java, databaseName).build()

        try {
            val dao = database.taskDao()
            dao.insertTask(TaskEntity("pending-id", "Pending task", "Keep this description", false))
            dao.insertTask(TaskEntity("completed-id", "Completed task", "", false))
            dao.markTaskComplete("completed-id")
            database.close()

            database = Room.databaseBuilder(context, TaskDatabase::class.java, databaseName).build()
            val restored = database.taskDao().observeTasks().first()

            assertEquals(2, restored.size)
            val pending = restored.single { it.id == "pending-id" }
            val completed = restored.single { it.id == "completed-id" }
            assertEquals("Pending task", pending.title)
            assertEquals("Keep this description", pending.description)
            assertTrue(!pending.isCompleted)
            assertTrue(completed.isCompleted)
            assertEquals(2, restored.size)
        } finally {
            database.close()
            context.deleteDatabase(databaseName)
        }
    }
}
