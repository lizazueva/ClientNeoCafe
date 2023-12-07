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

    //получение детальной информации о продукте
    private val _detailProduct: MutableLiveData<Resource<DetailInfoProduct>> = MutableLiveData()
    val detailProduct: LiveData<Resource<DetailInfoProduct>>
        get() = _detailProduct

    private fun saveDetailProduct(response: DetailInfoProduct) {
        _detailProduct.postValue(Resource.Success(response))
    }

    //получение списка популярных позиций для меню
        private val _compatibleItems: MutableLiveData<Resource<List<DetailInfoProduct>>> = MutableLiveData()

        val compatibleItems: LiveData<Resource<List<DetailInfoProduct>>>
            get() = _compatibleItems

        private fun savePopularItems(response: List<DetailInfoProduct>) {
            _compatibleItems.postValue(Resource.Success(response))
        }

    fun getCompatibleItems(id: Int){
        viewModelScope.launch {
            _compatibleItems.postValue(Resource.Loading())
            try {
                val response = repository.getCompatibleItems(id)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let { savePopularItems(it) }
                    Log.d("getCompatibleItems", "Successful: $responseBody")

                }else{
                    val errorBody = response.errorBody()?.toString()
                    _compatibleItems.postValue(Resource.Error(errorBody ?: "Ошибка получения приятного дополнения"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка загрузки: ${e.message}")
                _compatibleItems.postValue(Resource.Error(e.message ?: "Ошибка загрузки"))
            }
        }
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