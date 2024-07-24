package com.example.httplogger
import ReponseDisplay
import NetworkViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.httplogger.ui.theme.HttpLoggerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: NetworkViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HttpLoggerTheme {
                var selectedResponseInfo by remember { mutableStateOf<ResponseInfo?>(null) }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center){

                                        Text("HTTP Logger")
                                    }

                                    },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary, // Background color
                                titleContentColor = MaterialTheme.colorScheme.onPrimary // Text color
                        )

                        )
                    },
                    bottomBar = {
                        if (selectedResponseInfo == null) {
                            BottomAppBar(
                                content = {
                                    Button(
                                        onClick = { viewModel.fetchResponse(applicationContext) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text("Make API Call")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    content = { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
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
                )
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
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .clickable { onItemClick(responseInfo) }
            ) {
                Text(
                    text = "${responseInfo.code}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${responseInfo.endpoint}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${responseInfo.url}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${responseInfo.date}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${responseInfo.responseTime} ms",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Divider()
        }
    }
}

//items to display when you click an item on the lazy column
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemDetails(responseInfo: ResponseInfo?, onBack: () -> Unit) {
    val titles = listOf(
        "Request", "Response",
    )

    val pagerState = rememberPagerState(pageCount = { titles.size })
    val coroutineScope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp) // Space for the back button
        ) {
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
                    1 -> ReponseDisplay()
                }
            }

            LaunchedEffect(pagerState.currentPage) {
                coroutineScope.launch {
                    selectedTabIndex = pagerState.currentPage
                }
            }
        }

        // Add the back button at the bottom
        Button(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Back")
        }
    }
}
