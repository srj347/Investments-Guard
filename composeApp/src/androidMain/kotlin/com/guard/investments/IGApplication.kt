package com.guard.investments

import android.app.Application
import datasource.LocalPreference
import datasource.createDataStore

class IGApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        /* Initialize preference */
        LocalPreference.init(createDataStore(applicationContext))
    }
}