package com.yoctu.notif.android.yoctulibrary.mapper

import com.google.gson.Gson
import com.yoctu.notif.android.yoctulibrary.models.ResponseChannels

/**
 * Created by gael on 18.05.18.
 */
object YoctuMapper {

    val gson = Gson()

    /**
     * @return ResponseChannels
     */
    fun getResponseChannel(obj: String) = gson.fromJson(obj, ResponseChannels::class.java)
}