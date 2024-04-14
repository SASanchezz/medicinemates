package com.test.medicinemates.notificationmanager

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.test.medicinemates.R

class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        var builder = NotificationCompat.Builder(context!!, mChannel.id)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Шиз, прийми таблетки")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            createNotificationChannel(mChannel)

            val canPostNotification = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!canPostNotification) {

            }

//            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
//            val r = RingtoneManager.getRingtone(context, notification)
//            r.play()

            notify(1, builder.build())
        }
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        val mChannel = NotificationChannel("main-id", "main", NotificationManager.IMPORTANCE_DEFAULT)
    }
}