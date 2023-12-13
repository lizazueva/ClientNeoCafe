package com.example.clientneocafe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientneocafe.api.Repository
import com.example.clientneocafe.api.RetrofitInstance
import com.example.clientneocafe.model.DetailInfoProduct
import com.example.clientneocafe.model.user.OrderDetail
import com.example.clientneocafe.utils.Resource
import kotlinx.coroutines.launch

class OrdersViewModel(private val repository: Repository): ViewModel()  {

    //получение детальной информации о заказе
    private val _detailOrder: MutableLiveData<Resource<OrderDetail>> = MutableLiveData()
    val detailOrder: LiveData<Resource<OrderDetail>>
        get() = _detailOrder

    private fun saveDetailOrder(response: OrderDetail) {
        _detailOrder.postValue(Resource.Success(response))
    }

    fun getOrderDetail(id: Int){
        viewModelScope.launch {
            _detailOrder.postValue(Resource.Loading())
            try {
                val response = repository.getOrderDetail(id)
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    productResponse?.let { saveDetailOrder(it) }
                    Log.d("getOrderDetail", "Successful: $productResponse")
                }else{
                    val errorBody = response.errorBody()?.toString()
                    _detailOrder.postValue(Resource.Error(errorBody ?:"Ошибка загрузки заказа"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка загрузки: ${e.message}")
                _detailOrder.postValue(Resource.Error(e.message ?: "Ошибка загрузки"))
            }
        }
    }
}
