package com.example.nutrifindapp.data.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        // Add API key to all requests (Spoonacular expects 'apiKey' with lowercase 'i')
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("apiKey", apiKey)
            .build()
            
        // Log the URL for debugging (remove in production)
        println("API Request URL: $newUrl")

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
