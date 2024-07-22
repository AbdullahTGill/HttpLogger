package com.example.httplogger

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.httplogger.ui.theme.HttpLoggerTheme
import getApiService
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val fileViewModel: FileViewModel by viewModels { FileViewModelFactory(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HttpLoggerTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    val fileContents = fileViewModel.fileContents.observeAsState(emptyList())
//                    //FileContentsScreen(fileContents.value, Modifier.padding(innerPadding))
//                }
                listview()
            }
        }

        // Load the latest log files
        fileViewModel.loadLatestLogFiles()

        lifecycleScope.launch {
            fetchRandomJoke(applicationContext, fileViewModel)
        }
    }

    private suspend fun fetchRandomJoke(context: Context, viewModel: FileViewModel) {
        val apiService = getApiService(context)
        try {
            val response = apiService.getRandomJoke()
            if (response.isSuccessful) {
                val joke = response.body()
                joke?.let {
                    viewModel.addLog(it.toString())
                }
            } else {
                viewModel.addLog("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            viewModel.addLog("Exception: ${e.message}")
        }
    }
}





@Composable
fun listview() {
    val responseInfos by responseInfoFlow.collectAsState(initial = emptyList())


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier,
            text = "All Endpoints"
        )

        LazyColumn {
            // RetrofitInstance()

            items(responseInfos) {responseinfo ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = "Response Code: ${responseinfo.code}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "URL: ${responseinfo.url}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Date: ${responseinfo.date}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Response Time: ${responseinfo.responseTime} ms",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Endpoint: ${responseinfo.endpoint} ms",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}





@Composable
fun FileContentsScreen(contents: List<String>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        contents.forEach { content ->
            Text(
                text = content,
                fontSize = 16.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FileContentsScreenPreview() {
    HttpLoggerTheme {
        FileContentsScreen(listOf("Sample JSON content 1", "Sample JSON content 2"))
    }
}
