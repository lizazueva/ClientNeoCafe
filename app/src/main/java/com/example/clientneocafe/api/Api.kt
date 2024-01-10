package com.example.clientneocafe.api

import com.example.clientneocafe.model.CheckPosition
import com.example.clientneocafe.model.DetailInfoProduct
import com.example.clientneocafe.model.auth.CodeAuth
import com.example.clientneocafe.model.auth.ConfirmLoginResponse
import com.example.clientneocafe.model.auth.ConfirmRegisterResponse
import com.example.clientneocafe.model.auth.LoginRequest
import com.example.clientneocafe.model.auth.LoginResponse
import com.example.clientneocafe.model.auth.RegistrationResponse
import com.example.clientneocafe.model.auth.DetailRequest
import com.example.clientneocafe.model.auth.User
import com.example.clientneocafe.model.cart.Bonuses
import com.example.clientneocafe.model.cart.CreateOrder
import com.example.clientneocafe.model.home.BranchesMenu
import com.example.clientneocafe.model.home.Category
import com.example.clientneocafe.model.home.ChangeBranch
import com.example.clientneocafe.model.home.MessageResponse
import com.example.clientneocafe.model.home.SearchResultResponse
import com.example.clientneocafe.model.map.Branches
import com.example.clientneocafe.model.user.ClientId
import com.example.clientneocafe.model.user.OrderDetail
import com.example.clientneocafe.model.user.Orders
import com.example.clientneocafe.model.user.ReorderInformation
import com.example.clientneocafe.model.user.UserInfo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun getProfile(): Response<UserInfo>
    @GET("accounts/resend-code/")
    suspend fun resendCode(): Response<DetailRequest>
    @GET("branches/")
    suspend fun getBranches(): Response<List<Branches>>
    @PUT("accounts/edit-profile/")
    suspend fun updateProfile(@Body request: User): Response<DetailRequest>

//home
    @GET("customers/branches/")
    suspend fun getBranchesForMenu(): Response<List<BranchesMenu>>
    @POST("customers/change-branch/")
    suspend fun changeBranch(@Body request: ChangeBranch): Response<MessageResponse>
    @GET("customers/categories/")
    suspend fun getCategories(): Response<List<Category>>
    @GET("customers/popular-items/")
    suspend fun getPopularItems(): Response<List<DetailInfoProduct>>
    @GET("customers/menu/{id}/")
    suspend fun getProduct(@Path("id") id: Int, @Query("is_ready_made_product") is_ready_made_product: Boolean): Response<DetailInfoProduct>
    @GET("customers/compatible-items/{id}/")
    suspend fun getCompatibleItems(@Path("id") id: Int, @Query("is_ready_made_product") is_ready_made_product: Boolean): Response<List<DetailInfoProduct>>

    @GET("customers/menu")
    suspend fun getMenuCategory(@Query("category_id") id: Int): Response<List<DetailInfoProduct>>
    @GET("customers/search")
    suspend fun getSearchResult(@Query("query") q: String): Response<List<SearchResultResponse>>

//cart
    @POST("ordering/create-order/")
    suspend fun createOrder(@Body request: CreateOrder): Response<CreateOrder>
    @GET("customers/my-bonus/")
    suspend fun getMyBonus(): Response<Bonuses>
    @POST("customers/check-if-item-can-be-made/")
    fun checkPosition(@Body request: CheckPosition): Call<MessageResponse>

    //order
    @GET("customers/my-orders/")
    suspend fun getMyOrders(): Response<Orders>
    @GET("customers/my-orders/{id}/")
    suspend fun getOrderDetail(@Path("id") id: Int): Response<OrderDetail>
    @GET("ordering/reorder-information/")
    suspend fun getReorderInformation(@Query("order_id") idOrder: Int): Response<ReorderInformation>
    @GET("ordering/reorder/")
    suspend fun reorder (@Query("order_id") idOrder: Int): Response<DetailRequest>

    @GET("customers/my-id/")
    suspend fun getIdClient(): Response<ClientId>
    @GET("notices/delete-client-notification")
    suspend fun deleteNotification(@Query("id") id: Int): Response<Unit>










}