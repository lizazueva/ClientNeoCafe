package com.neobis.clientneocafe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neobis.clientneocafe.api.Repository
import com.neobis.clientneocafe.model.user.ClientId
import com.neobis.clientneocafe.utils.Resource
import kotlinx.coroutines.launch

class NotificationsViewModel(private val repository: Repository): ViewModel()  {

    //получение айди клиента
    private val _idClient: MutableLiveData<Resource<ClientId>> = MutableLiveData()
    val idClient: LiveData<Resource<ClientId>>
        get() = _idClient

    private fun saveIdClient(response: ClientId) {
        _idClient.postValue(Resource.Success(response))
    }

    //удаление уведомления
    private val _resultDelete: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val resultDelete: LiveData<Resource<Boolean>>
        get() = _resultDelete

    private fun saveResultDelete(response: Boolean) {
        _resultDelete.postValue(Resource.Success(response))
    }

    fun getIdClient(){
        viewModelScope.launch {
            _idClient.postValue(Resource.Loading())
            try {
                val response = repository.getIdClient()
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    productResponse?.let { saveIdClient(it) }
                    Log.d("getIdClient", "Successful: $productResponse")
                }else{
                    val errorBody = response.errorBody()?.toString()
                    _idClient.postValue(Resource.Error(errorBody ?:"Ошибка загрузки клиента"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка загрузки: ${e.message}")
                _idClient.postValue(Resource.Error(e.message ?: "Ошибка загрузки"))
            }
        }
    }

    fun deleteNotification(id: Int){
        viewModelScope.launch {
            _resultDelete.postValue(Resource.Loading())
            try {
                val response = repository.deleteNotification(id)
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    saveResultDelete(true)
                    Log.d("getOrderDetail", "Successful: $productResponse")
                }else{
                    val errorBody = response.errorBody()?.toString()
                    _resultDelete.postValue(Resource.Error(errorBody ?:"Ошибка удаления заказа"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка загрузки: ${e.message}")
                _resultDelete.postValue(Resource.Error(e.message ?: "Ошибка загрузки"))
            }
        }
    }
}
