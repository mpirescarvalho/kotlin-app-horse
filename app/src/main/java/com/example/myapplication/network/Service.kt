package com.example.myapplication.network

import com.example.myapplication.models.Client
import com.example.myapplication.models.Marca
import com.example.myapplication.models.Product
import com.example.myapplication.network.dto.SessionPost
import com.example.myapplication.network.dto.SessionResponse
import com.example.myapplication.util.Memoria
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    //Session
    @POST("session")
    fun createSession(@Body body: SessionPost): Deferred<Response<SessionResponse>>

    @GET("clientes")
    fun clientGetAll(@Query("page") page: Int = -1, @Query("page_size") pageSize: Int = 30): Deferred<List<Client>>

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

    val httpClient = OkHttpClient.Builder()

    init {
        httpClient.addInterceptor { chain ->
            val request = chain
                .request()
                .newBuilder()
                .addHeader("Authorization", "Bearer ${Memoria.session?.token}")
                .build()
            chain.proceed(request)
        }
    }

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl("http://192.168.0.15:9000/")
        .client(httpClient.build())
        .build()

    val api = retrofit.create(ApiService::class.java)

}