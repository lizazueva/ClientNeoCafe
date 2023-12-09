package com.example.clientneocafe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientneocafe.api.Repository
import com.example.clientneocafe.model.cart.CreateOrder
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.utils.Utils
import kotlinx.coroutines.launch

class CartViewModel(private val repository: Repository): ViewModel() {
    private val _order: MutableLiveData<Resource<CreateOrder>> = MutableLiveData()
    val order: LiveData<Resource<CreateOrder>>
        get() = _order
    private fun saveOrder(response: CreateOrder) {
        _order.postValue(Resource.Success(response))
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
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка оформления заказа: ${e.message}")

                _order.postValue(Resource.Error(e.message ?: "Ошибка оформления заказа"))
            }
        }
    }
}