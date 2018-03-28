package com.yoctu.notif.android.yoctuappnotif.callback

/**
 * This callbackLoginFragment allows the communication with the fragment/activity
 *
 * Created on 27.03.18.
 */

interface CallbackBroadcast {

    fun getMessage(message : String)
    fun getToken(token : String)
}
