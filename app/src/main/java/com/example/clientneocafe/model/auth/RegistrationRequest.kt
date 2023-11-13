package com.example.clientneocafe.model.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegistrationRequest(
    val phone_number: String,
    val first_name: String,
    val birth_date: String?
): Parcelable
