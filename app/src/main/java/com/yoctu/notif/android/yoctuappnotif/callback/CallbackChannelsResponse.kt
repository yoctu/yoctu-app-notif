package com.yoctu.notif.android.yoctuappnotif.callback

import com.yoctu.notif.android.yoctulibrary.models.ResponseChannels

/**
 * Created by gael on 18.05.18.
 */
interface CallbackChannelsResponse {

    fun getChannels(code: Int, response: ResponseChannels?, error: String?)
}