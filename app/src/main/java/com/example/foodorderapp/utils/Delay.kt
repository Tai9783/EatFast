package com.example.foodorderapp.utils

import android.os.Handler
import android.os.Looper

class Delay {
    fun runAfterMinDelay(startTime: Long, minDelayMillis: Long , action: () -> Unit) {
        val elapsed = System.currentTimeMillis() - startTime
        val delay = (minDelayMillis - elapsed).coerceAtLeast(0L)
        Handler(Looper.getMainLooper()).postDelayed(action, delay)
    }

}