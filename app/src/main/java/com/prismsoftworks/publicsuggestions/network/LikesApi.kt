package com.prismsoftworks.publicsuggestions.network

import com.prismsoftworks.publicsuggestions.model.Hit
import com.prismsoftworks.publicsuggestions.model.Hits
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.PUT

interface LikesApi {
    companion object{
        fun create(): LikesApi{
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("")
                .build()
            return retrofit.create(LikesApi::class.java)
        }
    }

    @GET
    fun getLikesForEntity(@Query("entityId") entityId: String): Observable<Hits>

    @PUT //todo: send put
    fun putLikeForEntity(@Body hit: Hit): Observable<Boolean>

}