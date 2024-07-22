package com.example.httplogger

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException

/**
 * This class is used for debugging api calls. All api calls will be
 * logged into a file specified by the path builder. [link](https://developer.android.com/training/data-storage/app-specific)
 *
 * @param pathBuilder
 */
class HttpFileLoggingInterceptor(pathBuilder: () -> String) : Interceptor {

    private val httpLoggingInterceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor(FileLogger(pathBuilder))

    init {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response =
        httpLoggingInterceptor.intercept(chain)

}

class FileLogger(
    private val pathBuilder: () -> String,
    private val separator: String = "\n\n\t__________________________________________________________\t"
) : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        val root = File(pathBuilder())
        try {
            if (!root.exists()) {
                root.mkdirs()
            }
            val file = File(root.path, "test.txt")
            if (!file.exists()) {
                file.createNewFile()
            }
            file.writeText(message)
            if (message.contains("END")) {
                file.writeText(separator)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}