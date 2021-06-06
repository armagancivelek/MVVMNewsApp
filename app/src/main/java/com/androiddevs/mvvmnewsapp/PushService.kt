package com.androiddevs.mvvmnewsapp

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.androiddevs.mvvmnewsapp.ui.NewActivity
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.ID
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.ID2
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage

class PushService : HmsMessageService() {
    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.i("abc", "Receive Token : $token");
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
        Log.i(
            "abc",
            "onMessageReceived() " + message!!.getDataOfMap().get("title")
        )

        val icon = R.mipmap.ic_launcher
        val title: String = message.getDataOfMap().get("title").toString()
        val text: String = message.getDataOfMap().get("text").toString()
        var channelId: String = message.getDataOfMap().get("channel_id").toString()

        if (channelId == null) {
            channelId = ID2
        }
        if (channelId !=ID) {
            channelId = ID2
        }

        val intent = Intent(this, NewActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notificationManager = NotificationManagerCompat.from(this)
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setColor(this.resources.getColor(R.color.colorPrimary))
            .build()

        notificationManager.notify(1, notification)
    }

    }
