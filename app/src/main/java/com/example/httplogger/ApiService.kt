import android.content.Context
import com.example.httplogger.JokesApi
import com.example.httplogger.RetrofitInstance
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("random_joke")
    suspend fun getRandomJoke(): Response<JokesApi>
}


fun getApiService(context: Context): ApiService {
    val retrofit = RetrofitInstance(context)
    return retrofit.create(ApiService::class.java)
}

