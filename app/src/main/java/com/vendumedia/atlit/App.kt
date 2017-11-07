package com.vendumedia.atlit

import android.app.Application

/**
 * Created by aldieemaulana on 5/24/17.
 */

class App : Application() {


    override fun onCreate() {
        super.onCreate()

    }

    companion object {

        var API = "http://sushitei.craniumstaging.xyz/api/v1/"
        var URL = "http://sushitei.craniumstaging.xyz/"
    }


}