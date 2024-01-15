package com.neobis.clientneocafe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neobis.clientneocafe.api.Repository
import com.neobis.clientneocafe.model.CheckPosition
import com.neobis.clientneocafe.model.DetailInfoProduct
import com.neobis.clientneocafe.model.home.MessageResponse
import com.neobis.clientneocafe.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    //проверка на возможность приготовления позиции
    fun createProduct(
        positionCheck: CheckPosition,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        repository.checkPosition(positionCheck)
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(
                    call: Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess()
                    } else {
                        onError("Ошибка при выполнении запроса: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.e("AddProductViewModel", "Ошибка при выполнении запроса", t)
                    onError("")
                }
            })
    }

    fun getCompatibleItems(id: Int, is_ready_made_product: Boolean){
        viewModelScope.launch {
            _compatibleItems.postValue(Resource.Loading())
            try {
                val response = repository.getCompatibleItems(id, is_ready_made_product)
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

    fun productDetail(id: Int, isReady: Boolean) {
        viewModelScope.launch {
            _detailProduct.postValue(Resource.Loading())
            try {
                val response = repository.getProduct(id, isReady)
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