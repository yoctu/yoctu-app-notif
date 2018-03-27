package com.yoctu.notif.android.yoctulibrary.models

/**
 * Created on 26.03.18.
 */

class Channel() : ViewType{

    var name : String = ""
    var checked : Boolean = false

    constructor(name : String) : this() {
        this.name = name
    }

    override fun getViewType() = ConstantsViewType.VIEW_TYPE_CHANNEL
}