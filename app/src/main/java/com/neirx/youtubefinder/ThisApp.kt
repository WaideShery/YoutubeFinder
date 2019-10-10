package com.neirx.youtubefinder

import android.app.Application
import timber.log.Timber

/**
 * Created by Waide Shery on 03.09.19.
 */
class ThisApp : Application(){

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

    }
}