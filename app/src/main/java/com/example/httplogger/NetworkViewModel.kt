import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.httplogger.JokesApi
import com.example.httplogger.RequestInfo
import com.example.httplogger.ResponseInfo
import com.example.httplogger.getApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NetworkViewModel : ViewModel() {
    private val _responseInfoFlow = MutableStateFlow<List<ResponseInfo>>(emptyList())
    val responseInfoFlow: StateFlow<List<ResponseInfo>> = _responseInfoFlow

    private val _requestInfoFlow = MutableStateFlow<List<RequestInfo>>(emptyList())
    val requestInfoFlow: StateFlow<List<RequestInfo>> = _requestInfoFlow

    private val _jokeFlow = MutableStateFlow<JokesApi?>(null)
    val jokeFlow: StateFlow<JokesApi?> = _jokeFlow

    fun fetchRandomJoke(context: Context) {
        viewModelScope.launch {
            val apiService = getApiService(context, this@NetworkViewModel)
            try {
                val response = apiService.getRandomJoke()
                if (response.isSuccessful) {
                    val joke = response.body()
                    _jokeFlow.value = joke
                    Log.d("NetworkViewModel", "Joke: $joke")
                } else {
                    Log.e("NetworkViewModel", "Error response: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("NetworkViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun addResponseInfo(responseInfo: ResponseInfo) {
        _responseInfoFlow.value += responseInfo
    }

    fun addRequestInfo(requestInfo: RequestInfo) {
        _requestInfoFlow.value += requestInfo
    }
}
