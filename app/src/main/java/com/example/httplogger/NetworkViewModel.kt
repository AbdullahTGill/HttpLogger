import android.content.Context
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

    //state flows
    private val _responseInfoFlow = MutableStateFlow<List<ResponseInfo>>(emptyList())
    val responseInfoFlow: StateFlow<List<ResponseInfo>> = _responseInfoFlow

    private val _requestInfoFlow = MutableStateFlow<List<RequestInfo>>(emptyList())
    val requestInfoFlow: StateFlow<List<RequestInfo>> = _requestInfoFlow

    private val _apiResponseFlow = MutableStateFlow<JokesApi?>(null)
    val jokeFlow: StateFlow<JokesApi?> = _apiResponseFlow

    fun fetchResponse(context: Context) {
        viewModelScope.launch {
            val apiService = getApiService(context, this@NetworkViewModel)
            try {
                val response = apiService.getRandomJoke()
                if (response.isSuccessful) {
                    val joke = response.body()
                    _apiResponseFlow.value = joke
                } else {
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
