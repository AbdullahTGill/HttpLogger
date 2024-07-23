package com.example.httplogger

import JokeDisplay
import NetworkViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.httplogger.ui.theme.HttpLoggerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: NetworkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HttpLoggerTheme {
                var selectedResponseInfo by remember { mutableStateOf<ResponseInfo?>(null) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        FetchButton { viewModel.fetchRandomJoke(applicationContext) }

                        if (selectedResponseInfo == null) {
                            ListView(viewModel) { responseInfo ->
                                selectedResponseInfo = responseInfo
                            }
                        } else {
                            ItemDetails(selectedResponseInfo) {
                                selectedResponseInfo = null
                            }
                        }
                    }
                }
            }
        }
    }
}


                @Composable
fun ListView(viewModel: NetworkViewModel, onItemClick: (ResponseInfo) -> Unit) {
    val responseInfos by viewModel.responseInfoFlow.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        items(responseInfos) { responseInfo ->
            Column(
                modifier = Modifier.padding(5.dp)
                    .fillMaxWidth()
                    .clickable { onItemClick(responseInfo) }
            ) {
                Text(
                    text = "Code: ${responseInfo.code}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Endpoint: ${responseInfo.endpoint}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "URL: ${responseInfo.url}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Date: ${responseInfo.date}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Response Time: ${responseInfo.responseTime} ms",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Divider()
        }
    }
}

@Composable
fun FetchButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Fetch Random Joke")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemDetails(responseInfo: ResponseInfo?, onBack: () -> Unit) {
    val titles = listOf(
        "Request", "Response",
    )

    val pagerState = rememberPagerState(pageCount = { titles.size })
    val coroutineScope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableStateOf(0) }

    // Zindigi tab component, since the requirement here was an indicator so I used an indicator tab
    ZindigiTabWithIndicator(
        tabTitles = titles,
        pagerState = pagerState,
        selectedTabIndex = selectedTabIndex,
        onTabSelected = { index ->
            selectedTabIndex = index
            coroutineScope.launch {
                pagerState.animateScrollToPage(index)
            }
        },

        modifier = Modifier.fillMaxWidth()
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        when (page) {
            0 -> RequestInfoList()
            1 -> JokeDisplay()

        }
    }

    LaunchedEffect(pagerState.currentPage) {
        coroutineScope.launch {
            selectedTabIndex = pagerState.currentPage
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HttpLoggerTheme {
        Column {
            FetchButton { }
        }
    }
}
