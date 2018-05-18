package com.yoctu.notif.android.yoctuappnotif.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE



/**
 * Created by gael on 18.05.18.
 */

object CopyPastUtils{

    /**
     * Use this function to save text in clip board manger
     * @param label
     * @param text
     * @param context
     */
    fun copyText(label: String,text: String, context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label,text)
        clipboard.primaryClip = clip
    }

    fun pastText(){}
}