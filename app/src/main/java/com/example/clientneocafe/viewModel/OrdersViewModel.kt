package com.example.clientneocafe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientneocafe.api.Repository
import com.example.clientneocafe.api.RetrofitInstance
import com.example.clientneocafe.model.DetailInfoProduct
import com.example.clientneocafe.model.auth.DetailRequest
import com.example.clientneocafe.model.user.OrderDetail
import com.example.clientneocafe.model.user.ReorderInformation
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

    //получение информации о предварительном заказе
    private val _reorderInformation: MutableLiveData<Resource<ReorderInformation>> = MutableLiveData()
    val reorderInformation: LiveData<Resource<ReorderInformation>>
        get() = _reorderInformation

    private fun saveReorderInformation(response: ReorderInformation) {
        _reorderInformation.postValue(Resource.Success(response))
    }

    //получение информации о повторном заказе
    private val _reorder: MutableLiveData<Resource<DetailRequest>> = MutableLiveData()
    val reorder: LiveData<Resource<DetailRequest>>
        get() = _reorder

    private fun saveReorder(response: DetailRequest) {
        _reorder.postValue(Resource.Success(response))
    }

    fun reorder(id: Int){
        viewModelScope.launch {
            _reorder.postValue(Resource.Loading())
            try {
                val response = repository.reorder(id)
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    productResponse?.let { saveReorder(it) }
                    Log.d("reorder", "Successful: $productResponse")
                }else{
                    val errorBody = response.errorBody()?.toString()
                    _reorder.postValue(Resource.Error(errorBody ?:"Ошибка повторного заказа"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка загрузки: ${e.message}")
                _reorder.postValue(Resource.Error(e.message ?: "Ошибка загрузки"))
            }
        }
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

    fun getReorderInformation(id: Int){
        viewModelScope.launch {
            _reorderInformation.postValue(Resource.Loading())
            try {
                val response = repository.getReorderInformation(id)
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    productResponse?.let { saveReorderInformation(it) }
                    Log.d("getReorderInformation", "Successful: $productResponse")
                }else{
                    val errorBody = response.errorBody()?.toString()
                    _reorderInformation.postValue(Resource.Error(errorBody ?:"Ошибка загрузки заказа"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка загрузки: ${e.message}")
                _reorderInformation.postValue(Resource.Error(e.message ?: "Ошибка загрузки"))
            }
        }
    }
}
