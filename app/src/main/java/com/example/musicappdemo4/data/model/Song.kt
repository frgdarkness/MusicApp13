package com.example.musicappdemo4.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Song (val title:String, val artist:String, val path:String, val timeTotal:Int) : Parcelable{
}
