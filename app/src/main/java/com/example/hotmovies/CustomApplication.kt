package com.example.hotmovies

import android.app.Application
import com.example.hotmovies.appplication.DIContainer

class CustomApplication : Application() {
    companion object {
        lateinit var diContainer: DIContainer
    }

    init {
        diContainer = DIContainer(this)
    }
}
