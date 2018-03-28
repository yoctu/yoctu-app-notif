package com.yoctu.notif.android.yoctuappnotif.fcm

import android.app.Notification
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.yoctu.notif.android.yoctuappnotif.utils.BroadcastUtils
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils

/**
 * To receive notification when app is foreground state
 *
 * Created on 26.03.18.
 */

class MyFirebaseMessagingService : FirebaseMessagingService(){


    private val TAG = "Firebase"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG," --- message received --- ")
        var noti = com.yoctu.notif.android.yoctulibrary.models.Notification()
        if(remoteMessage!!.data!!.size > 0 ) {
            val maps = remoteMessage.data
            Log.d(TAG,remoteMessage.notification!!.title
                    .plus(" ")
                    .plus(remoteMessage.notification!!.body)
                    .plus(remoteMessage.data)
                    .plus(maps.get("key")))
            noti.title = remoteMessage.notification!!.title!!
            noti.body =remoteMessage.notification!!.body!!
        } else if(remoteMessage.notification!!.body != null) {
            Log.d(TAG,remoteMessage.notification!!.title.plus(" ").plus(remoteMessage.notification!!.body))
            noti.title = remoteMessage.notification!!.title!!
            noti.body =remoteMessage.notification!!.body!!
        }
        BroadcastUtils.sendMessage(applicationContext,YoctuUtils.createJsonFromObject(noti))
    }
}
