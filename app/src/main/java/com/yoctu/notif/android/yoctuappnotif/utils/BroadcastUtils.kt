package com.yoctu.notif.android.yoctuappnotif.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.yoctu.notif.android.yoctuappnotif.callback.CallbackBroadcast
import com.yoctu.notif.android.yoctuappnotif.callback.CallbackReloadNotification

/**
 * This class allows to mange broad cast in the project
 *
 * Created on 27.03.18.
 */
object BroadcastUtils {

    var callbackLoginFragment : CallbackBroadcast? = null
    var callbackNotifFragment : CallbackBroadcast? = null
    var callbackNotifLoginFragment : CallbackBroadcast? = null
    var callbackReloadNotification: CallbackReloadNotification? = null

    /**
     * Local broad cast for FCM
     * send token to login view
     * send message to notification view
     */
    val mNotificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val message_fcm = intent?.getStringExtra(YoctuUtils.KEY_MESSAGE_FCM)
            val token_fcm = intent?.getStringExtra(YoctuUtils.KEY_TOKEN_FCM)

            if (!message_fcm.isNullOrEmpty()) {
                callbackNotifFragment?.let { callbackNotifFragment?.getMessage(message_fcm!!) }
                callbackNotifLoginFragment?.let { callbackNotifLoginFragment?.getMessage(message_fcm!!) }
            }


            if (!token_fcm.isNullOrEmpty())
                callbackLoginFragment?.getToken(token_fcm!!)


            Log.d(YoctuUtils.TAG_DEBUG," --- local broadcast receiver notification ---")
        }
    }

    /**
     * Called when app is in background and receives a notification
     * and we save the notifications in database, it's necessary to reload the list of notification
     *
     * send to notification fragment
     */
    val mReloadNotificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            callbackReloadNotification?.let { callbackReloadNotification -> callbackReloadNotification.reload() }

            Log.d(YoctuUtils.TAG_DEBUG," --- local broadcast receiver reload notifications ---")
        }
    }

    /**
     * Register login fragment as a callbackLoginFragment
     * @param callbackBroadcast
     */
    fun registerLoginFragment(callbackBroadcast: CallbackBroadcast) {
        callbackLoginFragment = callbackBroadcast
    }

    /**
     * Register notification fragment as a callbackLoginFragment
     * @param callbackBroadcast
     */
    fun registerNotifFragment(callbackBroadcast: CallbackBroadcast) {
        callbackNotifFragment = callbackBroadcast
    }

    /**
     * Register login fragment as a callbackNotifLoginFragment to receive notification
     * @param callbackBroadcast
     */
    fun registerNotifLoginFragment(callbackBroadcast: CallbackBroadcast) {
        callbackNotifLoginFragment = callbackBroadcast
    }

    fun registerCallbackReloadNotification(callbackReload : CallbackReloadNotification) {
        callbackReloadNotification = callbackReload
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

    /**
     * This function allows to send a reload request of notification
     * @param context
     */
    fun reloadNotification(context: Context) {
        var intent = Intent(YoctuUtils.INTENT_FILTER_RELOAD)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

}
