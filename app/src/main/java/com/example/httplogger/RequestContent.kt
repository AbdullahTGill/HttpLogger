package com.example.httplogger

import NetworkViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RequestInfoList(viewModel: NetworkViewModel = viewModel()) {
    val requestInfoList by viewModel.requestInfoFlow.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(requestInfoList) { requestInfo ->
            RequestInfoItem(requestInfo)
        }
    }
}

@Composable
fun RequestInfoItem(requestInfo: RequestInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
      //  elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = "URL: ${requestInfo.url}", style = MaterialTheme.typography.body1)
//            Text(text = "Method: ${requestInfo.method}", style = MaterialTheme.typography.body1)
            Text(text = "Headers: ${requestInfo.headers}", style = MaterialTheme.typography.bodyMedium)
           // Text(text = "Body: ${requestInfo.body}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
