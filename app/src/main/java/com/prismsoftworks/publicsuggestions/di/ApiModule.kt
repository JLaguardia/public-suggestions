package com.prismsoftworks.publicsuggestions.di

import com.prismsoftworks.publicsuggestions.network.LikesApi
import com.prismsoftworks.publicsuggestions.network.LoginApi
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val apiModule = Kodein.Module(name = "apiModule"){
    bind<MoshiConverterFactory>() with singleton { MoshiConverterFactory.create(instance()) }
    bind<OkHttpClient>() with singleton {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    bind<Retrofit>(tag = "BaseApi") with singleton {
        Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(instance())
            .client(instance())
            .build()
    }

    bind<LoginApi>() with singleton {
        instance<Retrofit>("BaseApi").create(LoginApi::class.java)
    }

    bind<LikesApi>() with singleton {
        instance<Retrofit>("BaseApi").create(LikesApi::class.java)
    }
}