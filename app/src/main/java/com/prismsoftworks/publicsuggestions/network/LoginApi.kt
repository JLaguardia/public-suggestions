package com.prismsoftworks.publicsuggestions.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST

interface LoginApi {
    companion object{
        fun create(): LoginApi{
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("")
                .build()
            return retrofit.create(LoginApi::class.java)
        }
    }

    @POST
    fun doLogin()
}