package com.ccncode.beemovie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.unit.dp
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.ccncode.beemovie.ui.theme.BeeMovieScriptTheme
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {

    // Use MutableLiveData to hold the script content
    // This allows the Composable to react to changes in the script content
    private val scriptContentLiveData = MutableLiveData<String>("Press 'Open File' to load the script.")

    // ActivityResultLauncher for the file picker
    private lateinit var pickFileLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the ActivityResultLauncher
        // This is crucial for handling the result from the file picker
        pickFileLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let { fileUri ->
                // Ensure persistent read access to the URI
                // Without this, access might be revoked after the app restarts or goes to background
                contentResolver.takePersistableUriPermission(fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                readTextFromUri(fileUri)
            } ?: run {
                scriptContentLiveData.value = "No file selected."
            }
        }

        setContent {
            BeeMovieScriptTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Observe the LiveData for script content
                    val currentScriptContent by scriptContentLiveData.observeAsState("Loading...")

                    FilePickerScreen(
                        currentScriptContent = currentScriptContent,
                        onOpenFileClick = {
                            // Launch the file picker when the button is clicked
                            pickFileLauncher.launch(arrayOf("text/plain")) // Only show text files
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun readTextFromUri(uri: Uri) {
        val content = runCatching {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    reader.readText()
                }
            }
        }.getOrNull() ?: "Error reading file content."
        scriptContentLiveData.postValue(content) // Post value to LiveData on background thread if needed, or directly set if on main thread
    }
}

@Composable
fun FilePickerScreen(
    currentScriptContent: String,
    onOpenFileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ->
        Button(
            onClick = onOpenFileClick,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Open Bee Movie Script File")
        }

// Display the loaded script content in a scrollable Text Composable
        Text(
            text = currentScriptContent,
            modifier = Modifier
                .weight(1f) // Makes the Text fill remaining space
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp) // Apply horizontal padding first
                .padding(bottom = 16.dp)     // Then apply bottom padding
        )
    }
}


@Preview(showBackground = true)
@Composable
fun FilePickerScreenPreview() {
    BeeMovieScriptTheme {
        FilePickerScreen(
            currentScriptContent = "This is a preview of the loaded script content. It would be much longer in a real scenario.",
            onOpenFileClick = { /* No-op for preview */ },
            modifier = Modifier.fillMaxSize()
        )
    }
}