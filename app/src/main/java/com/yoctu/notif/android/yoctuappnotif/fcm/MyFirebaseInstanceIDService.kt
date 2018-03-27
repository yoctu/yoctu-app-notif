package com.yoctu.notif.android.yoctuappnotif.fcm

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * To handle the creation, rotation, and updating of registration tokens.
 *
 * Created  on 26.03.18.
 */
class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    private val TAG = "Firebase"

    override fun onTokenRefresh() {
        super.onTokenRefresh()

        val token = FirebaseInstanceId.getInstance().token
        Log.d(TAG,"the token is ".plus(token))

        //BroadcastUtils.sendToken(applicationContext,token!!)
    }
}
