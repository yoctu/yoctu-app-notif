package com.yoctu.notif.android.yoctulibrary.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Represents a notification, used in list of notifications
 * 
 * Created on 26.03.18.
 */

class Notification() : Parcelable{

    var title : String = ""
    var body : String = ""

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        body = parcel.readString()
    }

    override fun toString() = title.plus(" - ")
            .plus(body)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(body)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Notification> {
        override fun createFromParcel(parcel: Parcel): Notification {
            return Notification(parcel)
        }

        override fun newArray(size: Int): Array<Notification?> {
            return arrayOfNulls(size)
        }
    }
}