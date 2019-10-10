package com.neirx.youtubefinder

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by Waide Shery on 10.10.19.
 */
class PreferenceHelper(context: Context) {
    private val KEY_IS_FOUND = "is_youtube_found"

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    fun setFoundState(isFound: Boolean) {
        prefs.edit().putBoolean(KEY_IS_FOUND, isFound).apply()
    }

    fun getFoundState(): Boolean {
        return prefs.getBoolean(KEY_IS_FOUND, false)
    }
}