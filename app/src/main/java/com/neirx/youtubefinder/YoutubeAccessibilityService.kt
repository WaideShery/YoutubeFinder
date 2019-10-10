package com.neirx.youtubefinder

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.content.pm.PackageManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import timber.log.Timber

/**
 * Created by Waide Shery on 09.10.19.
 */
class YoutubeAccessibilityService : AccessibilityService() {
    private lateinit var preferenceHelper: PreferenceHelper
    private var found = false

    override fun onCreate() {
        super.onCreate()
        preferenceHelper = PreferenceHelper(this)
    }

    override fun onInterrupt() {
        Timber.tag("YouTag").d("onInterrupt")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Timber.tag("YouTag").d("event: $event")
        event?.run {
            when (this.eventType) {
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                    handleWindowsChanged(this)
                }
            }
        }
    }

    private fun handleWindowsChanged(event: AccessibilityEvent) {
        if (preferenceHelper.getFoundState()) return

        val source = event.source
        if (source == null) return

        var parent = source.parent
        if (parent == null) parent = event.source
        Timber.tag("YouTag2").d("parent: $parent")

        found = false
        findChildren(parent)
    }

    private fun findChildren(info: AccessibilityNodeInfo) {
        if (found) return
        if (info.childCount > 0) {
            for (i in 0 until info.childCount) {
                if (found) break
                val child = info.getChild(i)
                Timber.tag("YouTag2").d("child: $child")
                child?.let {
                    if (child.text != null && "YouTube".equals(
                            child.text.toString(),
                            ignoreCase = true
                        )
                    ) {
                        found = true
                        Timber.tag("YouTag3").d("YouTube child: $child")
                        Timber.tag("YouTag3").d("YouTube parent: ${child.parent}")
                        if (child.isClickable) {
                            if (child.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                                preferenceHelper.setFoundState(true)
                            }
                        } else if (child.parent != null) {
                            if (child.parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                                preferenceHelper.setFoundState(true)
                            }
                        }
                        return@findChildren
                    } else {
                        findChildren(it)
                    }
                }
            }
        }
    }

    override fun onServiceConnected() {
        Timber.tag("YouTag").d("onServiceConnected")
        super.onServiceConnected()
        val info = serviceInfo
        info.packageNames = arrayOf(getLauncherId())
        serviceInfo = info
    }

    private fun getLauncherId(): String {
        val intent = Intent("android.intent.action.MAIN")
        intent.addCategory("android.intent.category.HOME")
        return packageManager.resolveActivity(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        ).activityInfo.packageName
    }


}