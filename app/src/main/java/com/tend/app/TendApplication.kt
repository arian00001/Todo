package com.tend.app

import android.app.Application
import com.tend.app.data.Store

class TendApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Store.init(this)
    }
}
