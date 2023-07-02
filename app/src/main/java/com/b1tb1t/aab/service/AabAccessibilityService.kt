package com.b1tb1t.aab.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class AabAccessibilityService : AccessibilityService() {

    override fun onCreate() {
        super.onCreate()
        Log.v("lcl>>>", "onCreate")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        //Log.v("lcl>>>", "onAccessibilityEvent")
        val source = event?.source ?: return
        for (i in 0 until source.childCount) {

            source.getChild(i)?.text?.let {
                Log.v("lcl>>>", "source.getChild(i)?.text: " + source.getChild(i)?.text)
            }
            /**
            if (source.getChild(i)?.text?.contains("跳过") == true) {
            source.getChild(i).performAction(ACTION_CLICK)
            }
             */
        }
        source.recycle()

    }

    override fun onInterrupt() {
        Log.v("lcl>>>", "onInterrupt")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("lcl>>>", "onDestroy")
    }
}