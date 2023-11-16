package com.example.clientneocafe.api

import com.example.clientneocafe.model.auth.CodeAuth
import com.example.clientneocafe.model.auth.ConfirmLoginResponse
import com.example.clientneocafe.model.auth.ConfirmRegisterResponse
import com.example.clientneocafe.model.auth.LoginRequest
import com.example.clientneocafe.model.auth.LoginResponse
import com.example.clientneocafe.model.auth.RegistrationResponse
import com.example.clientneocafe.model.auth.DetailRequest
import com.example.clientneocafe.model.auth.User
import com.example.clientneocafe.model.map.Branches
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface Api {

    @POST("accounts/login-for-client/")
    suspend fun login (@Body request: LoginRequest): Response<LoginResponse>
    @POST("accounts/confirm-login/")
    suspend fun confirmLogin (@Header("Authorization") pre_token: String, @Body request: CodeAuth): Response<ConfirmLoginResponse>
    @POST("accounts/register/")
    suspend fun registration (@Body request: User): Response<RegistrationResponse>
    @POST("accounts/confirm-phone-number/")
    suspend fun confirmPhone (@Body request: CodeAuth): Response<ConfirmRegisterResponse>
    @GET("accounts/my-profile/")
    suspend fun getProfile(): Response<User>
    @GET("accounts/resend-code/")
    suspend fun resendCode(): Response<DetailRequest>
    @GET("branches/")
    suspend fun getBranches(): Response<List<Branches>>
    @PUT("accounts/edit-profile/")
    suspend fun updateProfile(@Body request: User): Response<DetailRequest>

}