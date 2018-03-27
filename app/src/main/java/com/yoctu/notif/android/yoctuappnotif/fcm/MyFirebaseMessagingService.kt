package com.yoctu.notif.android.yoctuappnotif.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

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
        if(remoteMessage!!.data!!.size > 0 ) {
            val maps = remoteMessage.data
            Log.d(TAG,remoteMessage.notification!!.title
                    .plus(" ")
                    .plus(remoteMessage.notification!!.body)
                    .plus(remoteMessage.data)
                    .plus(maps.get("key")))
        } else if(remoteMessage.notification!!.body != null) {
            Log.d(TAG,remoteMessage.notification!!.title.plus(" ").plus(remoteMessage.notification!!.body))
        }
    }
}
