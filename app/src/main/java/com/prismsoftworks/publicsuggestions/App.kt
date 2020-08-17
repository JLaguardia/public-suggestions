package com.prismsoftworks.publicsuggestions

import android.app.Application
import com.prismsoftworks.publicsuggestions.di.apiModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class App : Application(), KodeinAware{
    override val kodein by Kodein.lazy {
        bind<App>() with singleton { this@App }
        import(apiModule)
    }

    override fun onCreate() {
        super.onCreate()

//        GlobalScope.launch {
//            GlobalFireBase.setupTopics()
//        }
    }
}