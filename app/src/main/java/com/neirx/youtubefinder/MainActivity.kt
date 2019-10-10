package com.neirx.youtubefinder

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    private var checkAfterResume = false
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferenceHelper = PreferenceHelper(this)

        findViewById<Button>(R.id.btnFind).setOnClickListener {
            if (isEnabled()) {
                start()
            } else {
                openSettings()
            }
        }
    }

    private fun start() {
        openHomeScreen()
    }

    private fun isEnabled(): Boolean {
        val am = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val list = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        val appId = BuildConfig.APPLICATION_ID
        Timber.tag("YouTag").d("appId: $appId")
        Timber.tag("YouTag").d("list size = ${list.size}")
        for (info in list) {
            Timber.tag("YouTag").d("info.id: ${info.id}")
            if (info.id.startsWith(appId)) return true
        }
        return false
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        )
        checkAfterResume = true
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if(checkAfterResume){
            checkAfterResume = false
            if (isEnabled()){
                openHomeScreen()
            }
        }
    }

    private fun openHomeScreen() {
        preferenceHelper.setFoundState(false)
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }

}
