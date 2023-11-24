package com.example.clientneocafe.api

import com.example.clientneocafe.model.auth.CodeAuth
import com.example.clientneocafe.model.auth.ConfirmLoginResponse
import com.example.clientneocafe.model.auth.ConfirmRegisterResponse
import com.example.clientneocafe.model.auth.LoginRequest
import com.example.clientneocafe.model.auth.LoginResponse
import com.example.clientneocafe.model.auth.RegistrationResponse
import com.example.clientneocafe.model.auth.DetailRequest
import com.example.clientneocafe.model.auth.User
import com.example.clientneocafe.model.home.Category
import com.example.clientneocafe.model.map.Branches
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Api {

    @POST("accounts/login-for-client/")
    suspend fun login (@Body request: LoginRequest): Response<LoginResponse>

    //test
    @POST("accounts/temporary-login/")
    suspend fun login2 (@Body request: LoginRequest): Response<ConfirmLoginResponse>
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

    @GET("menu/categories/")
    suspend fun getCategories(): Response<List<Category>>
//    @GET("menu/check-ingredients/{item_id}/")
//    suspend fun getCheckIngredients(@Path("item_id") id: Int): Response<>
//    @POST("menu/choose-branch/")
//    suspend fun chooseBranch()
//    @GET("menu/product-info/{id}/")
//    suspend fun getProduct(@Path("id") id: Int): Response<>
//    @GET("menu/products-in-category/category_id/")
//    suspend fun getProductsInCategory(): Response<List<>>
//    @GET("menu/search-products/{branch_id}")
//    suspend fun getSearchProduct(): Response


}