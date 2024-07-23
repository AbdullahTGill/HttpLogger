package com.example.httplogger

data class JokesApi(
    val id: Int,
    val punchline: String,
    val setup: String,
    val type: String
)

data class ResponseInfo(
    val code: Int,
    val url: String,
    val date: String,
    val responseTime: Long,
    val endpoint: String
)

data class RequestInfo(
    val headers: Map<String, String>,
  //  val body: String?
)


