package com.yoctu.notif.android.yoctulibrary.models

import com.google.gson.annotations.Expose

/**
 * Created on 26.03.18.
 */

class Channel() : ViewType{

    @Expose
    var id: Int = 0
    @Expose
    var name : String = ""
    var checked : Boolean = false

    constructor(name : String) : this() {
        this.name = name
    }

    constructor(id: Int, name : String) : this() {
        this.name = name
        this.id = id
    }

    override fun toString() = id.toString().plus(" ").plus(name)

    override fun getViewType() = ConstantsViewType.VIEW_TYPE_CHANNEL
}