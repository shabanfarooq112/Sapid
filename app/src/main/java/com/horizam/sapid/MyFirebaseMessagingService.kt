package com.horizam.sapid

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import android.R

import android.app.PendingIntent

import android.content.Intent
import androidx.core.app.NotificationCompat


import com.horizam.sapid.activities.MainActivity
import android.app.NotificationManager

import android.app.NotificationChannel
import android.content.Context

import android.os.Build











class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
//            showNotification(
//                remoteMessage.notification!!.title,
//                remoteMessage.notification!!.body
//            )
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, MainActivity::class.java)

        val channelId = "notification_channel"

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            channelId
        )
            .setSmallIcon(R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setVibrate(
                longArrayOf(
                    1000, 1000, 1000,
                    1000, 1000
                )
            )
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, builder.build())

    }


    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

}