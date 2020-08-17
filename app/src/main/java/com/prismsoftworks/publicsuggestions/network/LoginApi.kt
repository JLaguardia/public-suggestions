package com.prismsoftworks.publicsuggestions.network

import retrofit2.http.POST

interface LoginApi {

    @POST
    fun doLogin()
}