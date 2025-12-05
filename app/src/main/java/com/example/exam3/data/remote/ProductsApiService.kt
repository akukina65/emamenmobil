package com.example.exam3.data.remote
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object ProductsApiService {

    private val BASE_URL = "https://atflvwmbvilatyfudqqn.supabase.co"
    private val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImF0Zmx2d21idmlsYXR5ZnVkcXFuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjQ2NzI5MjUsImV4cCI6MjA4MDI0ODkyNX0.BuEFQeiKbymU81maiBLQTniHSAM7TL85Ph9VOwvxozk"

    fun create(): ProductsApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("apiKey", API_KEY)
                    .addHeader("Authorization", "Bearer $API_KEY") // Используем тот же API ключ как Bearer
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ProductsApi::class.java)
    }
}