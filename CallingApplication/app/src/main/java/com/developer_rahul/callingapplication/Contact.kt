package com.developer_rahul.callingapplication

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

@SuppressLint("ParcelCreator")
data class Contact(
    var id: String = "",
    val name: String = "",
    val mobileNo: String = "",
    val email: String = "",
    val nickname: String = ""
) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
        dest.writeString(mobileNo)
        dest.writeString(email)
        dest.writeString(nickname)
    }
    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(
                id = parcel.readString() ?: "",
                name = parcel.readString() ?: "",
                mobileNo = parcel.readString() ?: "",
                email = parcel.readString() ?: "",
                nickname = parcel.readString() ?: ""
            )
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}
