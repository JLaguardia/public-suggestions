package com.prismsoftworks.publicsuggestions.network

import androidx.lifecycle.LiveData
import com.prismsoftworks.publicsuggestions.model.Hit
import com.prismsoftworks.publicsuggestions.model.Hits
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.PUT

interface LikesApi {

    @GET
    fun getLikesForEntity(@Query("entityId") entityId: String): LiveData<Hits>

    @PUT //todo: send put
    fun putLikeForEntity(@Body hit: Hit): Boolean

}