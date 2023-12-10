package com.example.clientneocafe.viewModel

import android.graphics.Color
import android.util.Log
import android.view.Gravity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientneocafe.api.Repository
import com.example.clientneocafe.model.cart.Bonuses
import com.example.clientneocafe.model.cart.CreateOrder
import com.example.clientneocafe.model.map.Branches
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.utils.Utils
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class CartViewModel(private val repository: Repository): ViewModel() {

//создание заказа
    private val _order: MutableLiveData<Resource<CreateOrder>?> = MutableLiveData()
    val order: MutableLiveData<Resource<CreateOrder>?>
        get() = _order
    private fun saveOrder(response: CreateOrder) {
        _order.postValue(Resource.Success(response))
    }
//получение бонусов
    private val _bonuses: MutableLiveData<Resource<Bonuses>> = MutableLiveData()

    val bonuses: LiveData<Resource<Bonuses>>
        get() = _bonuses

    private fun saveBonuses(response: Bonuses) {
        _bonuses.postValue(Resource.Success(response))
    }


    fun getMyBonus(){
        viewModelScope.launch {
            _bonuses.postValue(Resource.Loading())
            try {
                val response = repository.getMyBonus()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let { saveBonuses(it) }
                    Log.d("getMyBonus", "Successful: $responseBody")

                }else{
                    val errorBody = response.errorBody()?.toString()
                    _bonuses.postValue(Resource.Error(errorBody ?: "Ошибка получения бонусов"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка загрузки: ${e.message}")
                _bonuses.postValue(Resource.Error(e.message ?: "Ошибка загрузки"))
            }
        }

    }

    fun createOrder (order: CreateOrder){
        viewModelScope.launch {
            try {
                val response = repository.createOrder(order)
                if (response.isSuccessful) {
                    _order.postValue(Resource.Loading())
                    val orderResponse = response.body()
                    orderResponse?.let { saveOrder(it) }
                    Log.d("createOrder", "Successful: $orderResponse")
                }else{
                    _order.postValue(Resource.Error("Ошибка оформления заказа"))
                    Log.e("MyViewModel", "createOrder Error: ${response.errorBody()?.toString()}")
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка оформления заказа: ${e.message}")

                _order.postValue(Resource.Error(e.message ?: "Ошибка оформления заказа"))
            }
        }
    }
    fun clearOrder() {
        _order.value = null
    }
    override fun onCleared() {
        _order.value = null
        super.onCleared()
    }
}