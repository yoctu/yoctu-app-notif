package com.yoctu.notif.android.yoctulibrary.models

import android.os.Parcel
import android.os.Parcelable
import com.yoctu.notif.android.yoctulibrary.LibraryUtils
import io.realm.RealmObject
import java.util.*

/**
 * Represents a notification, used in list of notifications
 * 
 * Created on 26.03.18.
 */


open class Notification() :
        Parcelable,
        RealmObject(),
        ViewType {

    var title : String = ""
    var body : String = ""
    var time : Long = 0

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        body = parcel.readString()
        time = parcel.readLong()
    }

    override fun getViewType() = ConstantsViewType.VIEW_TYPE_NOTIFICATION

    fun formatTime() = LibraryUtils.formatDate(this.time)

    override fun toString() = title.plus(" - ")
            .plus(body)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(body)
        parcel.writeLong(time)
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