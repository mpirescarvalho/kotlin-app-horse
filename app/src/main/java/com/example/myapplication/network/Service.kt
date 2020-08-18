package com.example.myapplication.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.App
import com.example.myapplication.models.Client
import com.example.myapplication.models.Marca
import com.example.myapplication.models.Product
import com.example.myapplication.network.dto.SessionPost
import com.example.myapplication.network.dto.SessionResponse
import com.example.myapplication.util.Memoria
import com.example.myapplication.util.internetDisponivel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException

interface ApiService {

    //Session
    @POST("session")
    fun createSession(@Body body: SessionPost): Deferred<Response<SessionResponse>>

    @GET("clientes")
    fun clientGetAll(@Query("where") where: String? = null, @Query("page") page: Int = -1, @Query("page_size") pageSize: Int = 30): Deferred<List<Client>>

    @GET("produtos")
    fun productGetAll(@Query("page") page: Int = -1, @Query("page_size") pageSize: Int = 30): Deferred<List<Product>>


    @GET("marca")
    fun marcaGetAll(@Query("page") page: Int = -1, @Query("page_size") pageSize: Int = 30): Deferred<List<Marca>>

//    @GET("marca")
//    fun marcaGetAllString(@Query("page") page: Int = -1, @Query("page_size") pageSize: Int = 30): Deferred<String>

//    @GET("marca/{id}")
//    fun marcaGetOne(@Path("id") id: Int): Deferred<Marca>

    @POST ("marca")
    fun marcaInsert(@Body marca: Marca): Deferred<Response<Unit>>

    @PUT("marca/{id}")
    fun marcaUpdate(@Path("id") id: Int, @Body marca: Marca): Deferred<Response<Unit>>

    @DELETE("marca/{id}")
    fun marcaDelete(@Path("id") id: Int): Deferred<Response<Unit>>

}

object Service {

    val defaultUrl = Memoria.API_URL

    val httpClient = OkHttpClient.Builder()

    init {
        httpClient.addInterceptor { chain ->

            if (!internetDisponivel()) {
                throw IOException("Falha na conexão com a internet")
            }

            val url = chain.request().url()
            val newUrl = url.toString().replace(defaultUrl, Memoria.API_URL)

            val request = chain
                .request()
                .newBuilder()
                .url(newUrl)
                .addHeader("Authorization", "Bearer ${Memoria.session?.token}")
                .build()

            chain.proceed(request)
        }
    }

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(defaultUrl)
        .client(httpClient.build())
        .build()

    val api = retrofit.create(ApiService::class.java)

}