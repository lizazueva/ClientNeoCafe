package com.example.clientneocafe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientneocafe.api.Repository
import com.example.clientneocafe.model.DetailInfoProduct
import com.example.clientneocafe.utils.Resource
import kotlinx.coroutines.launch

class DetailProductViewModel(private val repository: Repository): ViewModel() {

    private val _detailProduct: MutableLiveData<Resource<DetailInfoProduct>> = MutableLiveData()
    val detailProduct: LiveData<Resource<DetailInfoProduct>>
        get() = _detailProduct

    private fun saveDetailProduct(response: DetailInfoProduct) {
        _detailProduct.postValue(Resource.Success(response))
    }

    fun productDetail(id: Int) {
        viewModelScope.launch {
            _detailProduct.postValue(Resource.Loading())
            try {
                val response = repository.getProduct(id)
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    productResponse?.let { saveDetailProduct(it) }
                    Log.d("productDetail", "Successful: $productResponse")
                }else{
                    val errorBody = response.errorBody()?.toString()
                    _detailProduct.postValue(Resource.Error(errorBody ?:"Ошибка загрузки товарa"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка загрузки: ${e.message}")
                _detailProduct.postValue(Resource.Error(e.message ?: "Ошибка загрузки"))
            }
        }
    }
}