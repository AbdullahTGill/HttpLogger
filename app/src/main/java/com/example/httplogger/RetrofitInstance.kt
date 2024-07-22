package com.example.httplogger

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val responseInfoFlow = MutableStateFlow<List<ResponseInfo>>(emptyList())



fun RetrofitInstance(context: Context, ): Retrofit {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

//    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }


    val responseCodeInterceptor = Interceptor { chain ->
        val request = chain.request()
        val startNs = System.nanoTime()
        val response = chain.proceed(request)
        val endNs = System.nanoTime()

        val responseCode = response.code
        val url = request.url.toString()
        val date = java.util.Date().toString()
        val responseTimeMs = (endNs - startNs) / 1_000_000
        val endpoint = request.url.encodedPath


        val responseInfo = ResponseInfo(
            code = responseCode,
            url = url,
            date = date,
            responseTime = responseTimeMs,
            endpoint = endpoint
        )

        println("Response Info: $responseInfo")

        val currentInfos = responseInfoFlow.value.toMutableList()
        currentInfos.add(responseInfo)
        responseInfoFlow.value = currentInfos

        response
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        //.addInterceptor(customLoggingInterceptor)
        .addInterceptor(responseCodeInterceptor)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-apihub-key", "gHWNKSwAKMut4fBv5a9PRhVIJkzT01qRizFNKWuO7PeZAu002u")
                .addHeader("x-apihub-host", "Jokes-API.allthingsdev.co")
                .addHeader("x-apihub-endpoint", "fba849ca-2257-445d-b5e2-ba6ce527a281")
                .build()
            chain.proceed(request)
        }
        .build()

    return Retrofit.Builder()
        .baseUrl("https://jokes-api.proxy-production.allthingsdev.co/") // Replace with your API base URL
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}