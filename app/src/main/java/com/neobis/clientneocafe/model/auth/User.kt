package com.neobis.clientneocafe.model.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val phone_number: String,
    val first_name: String,
    val birth_date: String?
): Parcelable
