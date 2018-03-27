package com.yoctu.notif.android.yoctulibrary.models

import android.os.Parcelable
import com.google.gson.annotations.Expose

/**
 * This model contains response from server for channels
 *
 * Created  on 27.03.18.
 */
class ResponseChannels(){

    @Expose
    var data : ArrayList<Channel>? = null

}
