package com.example.hrdepartmentclient.models

import android.os.Parcel
import android.os.Parcelable

data class Department(val id:Int, val nameDepartment: String, val phoneNumber: String, val adressOfDepartment: AdressOfDepartment)
    : Parcelable {
        constructor(parcel: Parcel) : this(
        parcel.readInt() ?: 0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
            parcel.readParcelable(AdressOfDepartment::class.java.classLoader) ?: AdressOfDepartment(0,"", "", "")
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(nameDepartment)
            parcel.writeString(phoneNumber)
            parcel.writeParcelable(adressOfDepartment, flags)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Department> {
            override fun createFromParcel(parcel: Parcel): Department {
                return Department(parcel)
            }

            override fun newArray(size: Int): Array<Department?> {
                return arrayOfNulls(size)
            }
        }
}