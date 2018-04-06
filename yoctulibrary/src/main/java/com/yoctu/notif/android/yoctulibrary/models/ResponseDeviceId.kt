package com.yoctu.notif.android.yoctulibrary.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

/**
 * Created on 06.04.18.
 */

class ResponseDeviceId(): Parcelable {
    @Expose
    var status: String = ""

    constructor(parcel: Parcel) : this() {
        status = parcel.readString()
    }

    constructor(status: String): this() {
        this.status = status
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseDeviceId> {
        override fun createFromParcel(parcel: Parcel): ResponseDeviceId {
            return ResponseDeviceId(parcel)
        }

        override fun newArray(size: Int): Array<ResponseDeviceId?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString() = status
}