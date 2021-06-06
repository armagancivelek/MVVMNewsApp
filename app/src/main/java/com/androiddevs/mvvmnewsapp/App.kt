package com.androiddevs.mvvmnewsapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.ID
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.ID2
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.NAME
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.NAME2

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                ID,
                NAME,
                NotificationManager.IMPORTANCE_HIGH

            )
            channel1.description

            val channel2 = NotificationChannel(
                ID2,
                NAME2,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel2.description
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel1)
            manager.createNotificationChannel(channel2)
        }

    }

}

