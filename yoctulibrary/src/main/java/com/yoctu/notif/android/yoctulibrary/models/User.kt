package com.yoctu.notif.android.yoctulibrary.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Represents a logged User
 *
 * Created on 26.03.18.
 */

class User() : Parcelable{

    var lastName : String = ""
    var firstName : String = ""
    var email : String = ""
    var type : Int = LoginType.TYPE_GOOGLE
    var firebaseToken :String = ""

    constructor(parcel: Parcel) : this() {
        lastName = parcel.readString()
        firstName = parcel.readString()
        email = parcel.readString()
        type = parcel.readInt()
        firebaseToken = parcel.readString()
    }

    override fun toString() = lastName.plus(" ")
            .plus(firstName).plus(" ")
            .plus(email).plus(" - ")
            .plus(" type is ").plus(type).plus(" - ")
            .plus(firebaseToken)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(lastName)
        parcel.writeString(firstName)
        parcel.writeString(email)
        parcel.writeInt(type)
        parcel.writeString(firebaseToken)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}