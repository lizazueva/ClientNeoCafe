package com.example.clientneocafe.api

import com.example.clientneocafe.utils.Constants.Companion.BASE_URL
import com.futuremind.recyclerviewfastscroll.Utils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.cache.CacheRequest
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object{
        private val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(AuthorizationInterceptor())
            .build()

        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api:Api by lazy {
            retrofit.create(Api::class.java)
        }

        private class AuthorizationInterceptor: Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val newRequest = if (requiresAuthorization(request)){
                    val token = com.example.clientneocafe.utils.Utils.access_token
                    val authHeader = "Bearer $token"
                    request.newBuilder()
                        .header("Authorization", authHeader)
                        .build()
                }else{
                    request
                }
                return chain.proceed(newRequest)
            }
        }

        private fun requiresAuthorization(request: okhttp3.Request): Boolean {
            val path = request.url.encodedPath
            return path.endsWith("my-profile/")||
                    path.endsWith("resend-code/")||
                    path.endsWith("confirm-phone-number/")||
                    path.endsWith("edit-profile/")||
                    path.endsWith("customers/branches/")||
                    path.endsWith("change-branch/")||
                    path.endsWith("customers/categories/")||
                    path.endsWith("customers/popular-items/")||
                    path.contains("customers/menu/") && request.method == "GET"||
                    path.contains("customers/menu") && request.method == "GET"||
                    path.contains("customers/search") && request.method == "GET"||
                    path.contains("customers/compatible-items/") && request.method == "GET"||
                    path.endsWith("ordering/create-order/")||
                    path.endsWith("customers/my-bonus/")||
                    path.endsWith("customers/check-if-item-can-be-made/")||
                    path.endsWith("customers/my-orders/")||
                    path.contains("customers/my-orders/") && request.method == "GET"||
                    path.endsWith("customers/my-id/")||
                    path.contains("notices/delete-client-notification") && request.method == "GET"

        }
    }
}