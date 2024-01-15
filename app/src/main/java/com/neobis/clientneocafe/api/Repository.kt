package com.neobis.clientneocafe.api

import com.neobis.clientneocafe.model.CheckPosition
import com.neobis.clientneocafe.model.auth.CodeAuth
import com.neobis.clientneocafe.model.auth.LoginRequest
import com.neobis.clientneocafe.model.auth.User
import com.neobis.clientneocafe.model.cart.CreateOrder
import com.neobis.clientneocafe.model.home.ChangeBranch


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
    suspend fun getProduct(id: Int, is_ready_made_product: Boolean) = RetrofitInstance.api.getProduct(id, is_ready_made_product)
    suspend fun getMenuCategory(id: Int) = RetrofitInstance.api.getMenuCategory(id)
    suspend fun getCompatibleItems(id: Int, is_ready_made_product: Boolean) = RetrofitInstance.api.getCompatibleItems(id, is_ready_made_product)
    suspend fun getSearchResult(q: String) = RetrofitInstance.api.getSearchResult(q)

    suspend fun createOrder(request: CreateOrder) = RetrofitInstance.api.createOrder(request)
    suspend fun getMyBonus() = RetrofitInstance.api.getMyBonus()
    fun checkPosition(request: CheckPosition) = RetrofitInstance.api.checkPosition(request)
    suspend fun getMyOrders() = RetrofitInstance.api.getMyOrders()
    suspend fun getOrderDetail(id: Int) = RetrofitInstance.api.getOrderDetail(id)
    suspend fun getIdClient() = RetrofitInstance.api.getIdClient()
    suspend fun deleteNotification(id: Int) = RetrofitInstance.api.deleteNotification(id)
    suspend fun getReorderInformation(idOrder: Int) = RetrofitInstance.api.getReorderInformation(idOrder)
    suspend fun reorder (idOrder: Int) = RetrofitInstance.api.reorder(idOrder)
















}