package com.example.httplogger

import NetworkViewModel
import android.content.Context
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("random_joke")
    suspend fun getRandomJoke(): Response<JokesApi>
}

fun getApiService(context: Context, viewModel: NetworkViewModel): ApiService {
    val retrofit = RetrofitInstance(context, viewModel)
    return retrofit.create(ApiService::class.java)
}
