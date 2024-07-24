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
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Headers: ${requestInfo.headers}", style = MaterialTheme.typography.bodyMedium)
        }
    }

