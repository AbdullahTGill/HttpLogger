import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun JokeDisplay(viewModel: NetworkViewModel = viewModel()) {
    val context = LocalContext.current
    val joke by viewModel.jokeFlow.collectAsState()

    // Fetch the joke when the composable is first launched
    LaunchedEffect(Unit) {
        viewModel.fetchRandomJoke(context)
    }

    LaunchedEffect(joke) {
        Log.d("JokeDisplay", "Joke: $joke")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        joke?.let {
            Text(text = "Setup: ${it.setup}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Punchline: ${it.punchline}", style = MaterialTheme.typography.bodyLarge)
        } ?: run {
            Text(text = "Loading joke...", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
