package com.example.httplogger

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class FileViewModel(private val context: Context) : ViewModel() {

    private val _fileContents = MutableLiveData<List<String>>()
    val fileContents: LiveData<List<String>> = _fileContents

    fun loadLatestLogFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            val appSpecificDirectory = context.getExternalFilesDir(null)
            val logDir = File(appSpecificDirectory, "debug/network/session")

            val logFiles = logDir.listFiles()?.sortedByDescending { it.lastModified() }?.take(2)

            logFiles?.let { files ->
                val contents = files.map { it.readText() }
                _fileContents.postValue(contents)

                // Remove extra files if more than 2 exist
                if (logDir.listFiles()?.size ?: 0 > 2) {
                    logDir.listFiles()?.sortedByDescending { it.lastModified() }?.drop(2)?.forEach { it.delete() }
                }
            } ?: _fileContents.postValue(listOf("No log files found."))
        }
    }
    fun addLog(log: String) {
        val updatedLogs = _fileContents.value.orEmpty().toMutableList().apply {
            add(log)
        }
        _fileContents.value = updatedLogs
    }


}

