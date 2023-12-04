package com.example.clientneocafe.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize data class UserInfo(
    val birth_date: String?,
    val bonus: Int,
    val first_name: String,
    val phone_number: String
): Parcelable