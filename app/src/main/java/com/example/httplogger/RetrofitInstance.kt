package com.example.httplogger

import NetworkViewModel
import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun RetrofitInstance(context: Context, viewModel: NetworkViewModel): Retrofit {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val requestInterceptor = Interceptor { chain ->
        val request = chain.request()
        val requestInfo = extractRequestInfo(request)
        viewModel.addRequestInfo(requestInfo)

        chain.proceed(request)
    }

    val responseCodeInterceptor = Interceptor { chain ->
        val request = chain.request()
        val startNs = System.nanoTime()
        val response = chain.proceed(request)
        val endNs = System.nanoTime()

        val rootDomain = getRootDomain(request.url)
        val responseCode = response.code
        val url = rootDomain
        val date = java.util.Date().toString()
        val responseTimeMs = (endNs - startNs) / 1_000_000
        val endpoint = "${request.method}/${request.url.encodedPath}"

        val responseInfo = ResponseInfo(
            code = responseCode,
            url = url,
            date = date,
            responseTime = responseTimeMs,
            endpoint = endpoint
        )

        println("Response Info: $responseInfo")

        // Add the response info to the ViewModel's flow
        viewModel.addResponseInfo(responseInfo)

        response
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(requestInterceptor)
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
        .baseUrl("https://jokes-api.proxy-production.allthingsdev.co/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun extractRequestInfo(request: Request): RequestInfo {
    val headers = request.headers.toMultimap().mapValues { it.value.joinToString(", ") }
//    val body = request.body?.let { body ->
//        val buffer = okio.Buffer()
//        body.writeTo(buffer)
//        buffer.readUtf8()
//    }
    // Debug logging
    Log.d("RequestInfo", "Headers: $headers")
    //Log.d("RequestInfo", "Body: $body")

    return RequestInfo(
        headers = headers,
       // body = body
    )
}

fun getRootDomain(url: okhttp3.HttpUrl): String {
    return url.host.split(".").takeLast(2).joinToString(".")
}
