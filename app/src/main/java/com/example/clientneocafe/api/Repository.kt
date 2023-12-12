package com.example.clientneocafe.api

import com.example.clientneocafe.model.CheckPosition
import com.example.clientneocafe.model.DetailInfoProduct
import com.example.clientneocafe.model.auth.CodeAuth
import com.example.clientneocafe.model.auth.LoginRequest
import com.example.clientneocafe.model.auth.User
import com.example.clientneocafe.model.cart.Bonuses
import com.example.clientneocafe.model.cart.CreateOrder
import com.example.clientneocafe.model.home.ChangeBranch
import com.example.clientneocafe.model.home.MessageResponse
import com.example.clientneocafe.model.home.SearchResultResponse
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query


class Repository(private val api: Api) {
    suspend fun login (request: LoginRequest) = RetrofitInstance.api.login(request)
    suspend fun confirmLogin (pre_token: String, request: CodeAuth) = RetrofitInstance.api.confirmLogin(pre_token, request)
    suspend fun registration (request: User) = RetrofitInstance.api.registration(request)
    suspend fun confirmPhone (request: CodeAuth) = RetrofitInstance.api.confirmPhone(request)
    suspend fun resendCode() = RetrofitInstance.api.resendCode()

    suspend fun getBranches() = RetrofitInstance.api.getBranches()
    suspend fun getProfile() = RetrofitInstance.api.getProfile()
    suspend fun updateProfile(request: User) = RetrofitInstance.api.updateProfile(request)
    suspend fun getBranchesForMenu() = RetrofitInstance.api.getBranchesForMenu()
    suspend fun changeBranch(request: ChangeBranch) = RetrofitInstance.api.changeBranch(request)
    suspend fun getCategories() = RetrofitInstance.api.getCategories()
    suspend fun getPopularItems() = RetrofitInstance.api.getPopularItems()
    suspend fun getProduct(id: Int) = RetrofitInstance.api.getProduct(id)
    suspend fun getMenuCategory(id: Int) = RetrofitInstance.api.getMenuCategory(id)
    suspend fun getCompatibleItems(id: Int) = RetrofitInstance.api.getCompatibleItems(id)
    suspend fun getSearchResult(q: String) = RetrofitInstance.api.getSearchResult(q)

    suspend fun createOrder(request: CreateOrder) = RetrofitInstance.api.createOrder(request)
    suspend fun getMyBonus() = RetrofitInstance.api.getMyBonus()
    fun checkPosition(request: CheckPosition) = RetrofitInstance.api.checkPosition(request)











}