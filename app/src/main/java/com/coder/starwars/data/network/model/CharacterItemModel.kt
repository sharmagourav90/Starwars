package com.coder.starwars.data.network.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Character item data model for list screen
 */
class CharacterItemModel(
    @SerializedName("url") val url: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("birth_year") val birthYear: String?
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<CharacterItemModel> {
            override fun createFromParcel(parcel: Parcel) = CharacterItemModel(parcel)
            override fun newArray(size: Int) = arrayOfNulls<CharacterItemModel>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        url = parcel.readString(),
        name = parcel.readString(),
        birthYear = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(name)
        parcel.writeString(birthYear)
    }

    override fun describeContents() = 0
}