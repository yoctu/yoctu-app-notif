package com.yoctu.notif.android.yoctulibrary.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Model allows to send email and device id to server
 *
 * Created on 27.03.18.
 */

class ParamBodyDeviceID() : Parcelable{

    @Expose
    var email : String = ""

    @Expose
    @SerializedName("device_id")
    var deviceId : String = ""

    constructor(parcel: Parcel) : this() {
        email = parcel.readString()
        deviceId = parcel.readString()
    }

    override fun toString() = email.plus(" - ").plus(deviceId)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(deviceId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParamBodyDeviceID> {
        override fun createFromParcel(parcel: Parcel): ParamBodyDeviceID {
            return ParamBodyDeviceID(parcel)
        }

        override fun newArray(size: Int): Array<ParamBodyDeviceID?> {
            return arrayOfNulls(size)
        }
    }
}
