package com.yoctu.notif.android.yoctuappnotif.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by gael on 18.05.18.
 */
object KeyboardUtils {

    /**
     * This function allows to close/hide the keyboard
     */
    fun hidesKeyboard(v: View) {
        val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
    }
}