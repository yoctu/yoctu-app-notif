package com.yoctu.notif.android.yoctuappnotif.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings

/**
 * Created on 26.03.18.
 */

object IntentUtils {

    /**
     * @param context
     */
    fun openWifiSettings(context: Context) {
        context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    }
}