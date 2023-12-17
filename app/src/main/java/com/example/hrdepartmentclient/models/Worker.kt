package com.example.hrdepartmentclient.models

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Worker(
    val id: Int,
    val name: String,
    val surname: String,
    val patronymic: String,
    val phone: String,
    val dateOfBirth: Date,
    val city: String,
    val street: String,
    val house: String,
    val familyPosition: String,
    val countChildren: Int,
    val email: String,
    val seriesPass: String,
    val numberPass: String,
    val issuedByWhom: String,
    val dateOfIssue: Date,
    val divisionCode: String,
    val numberSnils: String,
    val numberInn: String,
    val gender: String,
    val militaryTitle: String,
    val shelfLife: String,
    val stockCategory: Int,
    val profile: String,
    val vus: String,
    val nameKomis: String,
    val photo: String,
    val descriptionWorker: String,
    val dismiss: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt() ?: 0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        Date(parcel.readLong()),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() ?: 0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        Date(parcel.readLong()),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() ?: 0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readBoolean() ?: false
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(surname)
        parcel.writeString(patronymic)
        parcel.writeString(phone)
        parcel.writeLong(dateOfBirth.time)
        parcel.writeString(city)
        parcel.writeString(street)
        parcel.writeString(house)
        parcel.writeString(familyPosition)
        parcel.writeInt(countChildren)
        parcel.writeString(email)
        parcel.writeString(seriesPass)
        parcel.writeString(numberPass)
        parcel.writeString(issuedByWhom)
        parcel.writeLong(dateOfIssue.time)
        parcel.writeString(divisionCode)
        parcel.writeString(numberSnils)
        parcel.writeString(numberInn)
        parcel.writeString(gender)
        parcel.writeString(militaryTitle)
        parcel.writeString(shelfLife)
        parcel.writeInt(stockCategory)
        parcel.writeString(profile)
        parcel.writeString(vus)
        parcel.writeString(nameKomis)
        parcel.writeString(photo)
        parcel.writeString(descriptionWorker)
        parcel.writeBoolean(dismiss)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Worker> {
        override fun createFromParcel(parcel: Parcel): Worker {
            return Worker(parcel)
        }

        override fun newArray(size: Int): Array<Worker?> {
            return arrayOfNulls(size)
        }
    }
}