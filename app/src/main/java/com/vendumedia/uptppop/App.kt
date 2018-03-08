package com.vendumedia.uptppop

import android.app.Application

/**
 * Created by aldieemaulana on 5/24/17.
 */

class App : Application() {


    override fun onCreate() {
        super.onCreate()

    }

    companion object {

        var API = "http://uptppopkabbogor.com/api/v1/"
        var URL = "http://uptppopkabbogor.com"
    }


}