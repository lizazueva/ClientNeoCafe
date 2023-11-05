package com.example.clientneocafe.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(val id:Int,
                   val category: String,
                   val title: String,
                   val discr: String,
                   val amount: Int,
                   val image: Int,
                   var county:Int): Parcelable
