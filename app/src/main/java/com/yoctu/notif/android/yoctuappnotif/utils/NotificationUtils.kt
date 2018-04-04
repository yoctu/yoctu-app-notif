package com.yoctu.notif.android.yoctuappnotif.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.yoctu.notif.android.yoctuappnotif.R
import android.app.PendingIntent
import android.content.Intent
import com.yoctu.notif.android.yoctuappnotif.ui.login.LoginActivity


/**
 * Created on 28.03.18.
 */

object NotificationUtils {

    private val CHANNEL_NAME = "YOCTU CHANNEL"
    private var yoctuChannel : NotificationChannel? = null
    private val notificationID = 58

    @SuppressLint("NewApi")
    /**
     * channel is a group for notifications of yoctu
     * it is easy for user to modify settings
     *
     * @param context
     */
    private fun createChannel (context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            yoctuChannel = NotificationChannel(
                    context.getString(R.string.notification_channel_id),
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT)
            yoctuChannel!!.enableLights(true)
            yoctuChannel!!.enableVibration(true)
            yoctuChannel!!.lightColor = context.getColor(R.color.colorPrimaryNoActionBar)
            yoctuChannel!!.lockscreenVisibility

            manager.createNotificationChannel(yoctuChannel)
        }
    }

    /**
     * create a notification
     *
     * @param context
     * @param title
     * @param text
     */
    fun createNotification(context: Context, title : String, text : String) {
        createChannel(context)

        var mBuilder =
                NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSmallIcon(R.drawable.ic_yoctu_notif_3)
                        .setContentIntent(createPendinInten(context)) //tap action
                        .setAutoCancel(true)  //remove itself
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationID,mBuilder.build())
    }

    /**
     * This function create an action when user taps on notification
     * redirect user to Login activity
     *
     * @param context
     * @return PendingIntent
     */
    private fun createPendinInten(context: Context) : PendingIntent {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(context, 0, intent, 0)
    }
}