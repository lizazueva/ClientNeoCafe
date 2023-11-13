package com.example.clientneocafe.model.auth

data class RegistrationResponse(
    val phone_number: String,
    val refresh: String,
    val access: String,
    val message: String
)
