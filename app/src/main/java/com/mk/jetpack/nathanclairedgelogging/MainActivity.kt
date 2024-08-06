package com.mk.jetpack.nathanclairedgelogging

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mk.jetpack.edgencg.Edge
import com.mk.jetpack.nathanclairedgelogging.ui.theme.NathanClairEdgeLoggingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NathanClairEdgeLoggingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        // Test logging functionality
        Edge.d("This is a debug message")
        Edge.i("This is an info message")
        Edge.w("This is a warning message")
        Edge.e("This is an error message")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        GenerateLogButton()
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NathanClairEdgeLoggingTheme {
        Greeting("Android")
    }
}

@Composable
fun GenerateLogButton() {
    Button(onClick = { generateLargeLog() }) {
        Text("Generate 1MB Log")
    }
}

fun generateLargeLog() {
    val logSize = 1 * 1024 * 1024 // 1MB in bytes
    val loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
            "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. "
    val repeatCount = logSize / loremIpsum.length + 1
    val logMessage = loremIpsum.repeat(repeatCount).take(logSize)
    Edge.d(logMessage)
}