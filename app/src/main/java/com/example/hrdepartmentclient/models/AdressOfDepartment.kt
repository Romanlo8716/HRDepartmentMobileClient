package com.example.hrdepartmentclient.models

import android.os.Parcel
import android.os.Parcelable

data class AdressOfDepartment(
    val id: Int,
    val city: String,
    val street: String,
    val house: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt() ?: 0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(city)
        parcel.writeString(street)
        parcel.writeString(house)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdressOfDepartment> {
        override fun createFromParcel(parcel: Parcel): AdressOfDepartment {
            return AdressOfDepartment(parcel)
        }

        override fun newArray(size: Int): Array<AdressOfDepartment?> {
            return arrayOfNulls(size)
        }
    }
}