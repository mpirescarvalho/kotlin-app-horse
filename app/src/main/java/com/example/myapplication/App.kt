package com.example.myapplication

import android.app.Application
import android.content.Context

class App: Application() {

    companion object {
        var appContext: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

}