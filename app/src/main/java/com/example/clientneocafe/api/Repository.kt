package com.example.clientneocafe.api

import com.example.clientneocafe.model.auth.CodeAuth
import com.example.clientneocafe.model.auth.LoginRequest
import com.example.clientneocafe.model.auth.User


class Repository(private val api: Api) {
    suspend fun login (request: LoginRequest) = RetrofitInstance.api.login(request)
    suspend fun confirmLogin (pre_token: String, request: CodeAuth) = RetrofitInstance.api.confirmLogin(pre_token, request)
    suspend fun registration (request: User) = RetrofitInstance.api.registration(request)
    suspend fun confirmPhone (request: CodeAuth) = RetrofitInstance.api.confirmPhone(request)
    suspend fun resendCode() = RetrofitInstance.api.resendCode()
    suspend fun getBranches() = RetrofitInstance.api.getBranches()
    suspend fun getProfile() = RetrofitInstance.api.getProfile()
    suspend fun updateProfile(request: User) = RetrofitInstance.api.updateProfile(request)

}