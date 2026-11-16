package com.example.employeetaskmanager

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.employeetaskmanager.ui.theme.EmployeeTaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        android.util.Log.d("EmployeeApp", "MainActivity started")
        enableEdgeToEdge()
        setContent {
            EmployeeTaskManagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
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
}

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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EmployeeTaskManagerTheme {
        Greeting("Android")
    }
}
