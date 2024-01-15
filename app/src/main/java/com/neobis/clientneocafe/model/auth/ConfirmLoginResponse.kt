package com.neobis.clientneocafe.model.auth

data class ConfirmLoginResponse(
    val phone_number: String,
    val refresh: String,
    val access: String,
    val detail: String
)
