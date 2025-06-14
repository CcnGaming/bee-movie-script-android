package com.ccncode.beemovie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ccncode.beemovie.ui.theme.BeeMovieScriptTheme
import androidx.compose.ui.platform.LocalContext // Import LocalContext
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeeMovieScriptTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Call the new Composable that loads the script from assets
                    BeeMovieScriptDisplay(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BeeMovieScriptDisplay(modifier: Modifier = Modifier) {
    val context = LocalContext.current // Get the current context
    val scriptContent: String = try {
        context.assets.open("bee_movie_script.txt").use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.readText() // Reads the entire file into a single string
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        "Error loading Bee Movie script: ${e.localizedMessage}"
    }

    // Use a Scrollable modifier since the text will be very long
    Text(
        text = scriptContent,
        modifier = modifier
            .verticalScroll(rememberScrollState()) // Enable vertical scrolling
            .padding(16.dp) // Add some padding for readability
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BeeMovieScriptTheme {
        // For preview, you might want a shorter text or a placeholder
        Text(text = "Preview of Bee Movie Script...", modifier = Modifier.padding(16.dp))
    }
}