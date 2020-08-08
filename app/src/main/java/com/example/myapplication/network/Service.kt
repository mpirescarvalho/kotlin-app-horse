package com.example.myapplication.network

import com.example.myapplication.models.Client
import com.example.myapplication.models.Marca
import com.example.myapplication.models.Product
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface ApiService {

    @GET("clientes")
    fun clientGetAll(@Query("page") page: Int = -1, @Query("page_size") pageSize: Int = 30): Deferred<List<Client>>

    @GET("produtos")
    fun productGetAll(@Query("page") page: Int = -1, @Query("page_size") pageSize: Int = 30): Deferred<List<Product>>


    @GET("marca")
    fun marcaGetAll(@Query("page") page: Int = -1, @Query("page_size") pageSize: Int = 30): Deferred<List<Marca>>

    @GET("marca/{id}")
    fun marcaGetOne(@Path("id") id: Int): Deferred<Marca>

    @POST ("marca")
    fun marcaInsert(@Body marca: Marca): Deferred<Response<Unit>>

    @PUT("marca/{id}")
    fun marcaUpdate(@Path("id") id: Int, @Body marca: Marca): Deferred<Response<Unit>>

    @DELETE("marca/{id}")
    fun marcaDelete(@Path("id") id: Int): Deferred<Response<Unit>>

}

object Service {

//    private val moshi = Moshi.Builder()
//        .add(KotlinJsonAdapterFactory())
//        .build()

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl("http://192.168.1.11:9000/")
        .build()

    val api = retrofit.create(ApiService::class.java)

}