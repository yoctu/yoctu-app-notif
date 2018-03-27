package com.yoctu.notif.android.yoctuappnotif.utils

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.yoctu.notif.android.yoctuappnotif.callback.CallbackBroadcast

/**
 * This class allows to mange broad cast in the project
 *
 * Created on 27.03.18.
 */
object BroadcastUtils {

    var callback : CallbackBroadcast? = null

    /**
     * Local broad cats for FCM
     */
    val mNotificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val message_fcm = intent?.getStringExtra(YoctuUtils.KEY_MESSAGE_FCM)
            val token_fcm = intent?.getStringExtra(YoctuUtils.KEY_TOKEN_FCM)

            if(!message_fcm.isNullOrEmpty())
                callback?.getMessage(message_fcm!!)

            if(!token_fcm.isNullOrEmpty())
                callback?.getToken(token_fcm!!)

            Log.d(YoctuUtils.TAG_DEBUG," --- local broadcast receiver ---")
        }
    }

    /**
     * Register login fragment as a callback
     */
    fun registerLoginFragment(callbackBroadcast: CallbackBroadcast) {
        callback = callbackBroadcast
    }


    /**
     * This function allows to send notification to broad cast
     *
     * @param context
     * @param token
     */
    fun sendMessage(context: Context, msg : String) {
        var intent = Intent(YoctuUtils.INTENT_FILTER_FCM)
        intent.putExtra(YoctuUtils.KEY_MESSAGE_FCM,msg)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    /**
     * This function allows to send token to broad cast
     *
     * @param context
     * @param token
     */
    fun sendToken(context: Context, token : String) {
        var intent = Intent(YoctuUtils.INTENT_FILTER_FCM)
        intent.putExtra(YoctuUtils.KEY_TOKEN_FCM,token)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

}
