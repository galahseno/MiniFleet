package id.dev.core.data.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import id.dev.core.data.R

class NotificationHelper(private val context: Context) {
    @SuppressLint("MissingPermission")
    fun showAlertNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(context, "alerts_channel")
            .setSmallIcon(R.drawable.ic_app_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
    }
}