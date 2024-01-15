package com.neobis.clientneocafe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neobis.clientneocafe.api.Repository
import com.neobis.clientneocafe.model.auth.User
import com.neobis.clientneocafe.model.user.Orders
import com.neobis.clientneocafe.model.user.UserInfo
import com.neobis.clientneocafe.utils.Resource
import kotlinx.coroutines.launch

class UserViewModel (private val repository: Repository): ViewModel() {

    private val _user: MutableLiveData<Resource<UserInfo>> = MutableLiveData()

    val user: LiveData<Resource<UserInfo>>
        get() = _user

    private fun saveUser (response: UserInfo){
        _user.postValue(Resource.Success(response))
    }

    private val _updateResult: MutableLiveData<Resource<String>> = MutableLiveData()

    val updateResult: LiveData<Resource<String>>
        get() = _updateResult

    //получение списка актуальных и завершенных заказов

    private val _myOrders: MutableLiveData<Resource<Orders>> = MutableLiveData()

    val myOrders: LiveData<Resource<Orders>>
        get() = _myOrders
    private fun saveMyOrders (response: Orders){
        _myOrders.postValue(Resource.Success(response))
    }


    fun getMyOrders(){
        viewModelScope.launch {
            try {
                val response = repository.getMyOrders()
                _myOrders.postValue(Resource.Loading())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let { saveMyOrders(it) }
                    Log.d("getMyOrders", "Successful: $responseBody")
                }else{
                    _myOrders.postValue(Resource.Error("Ошибка получения данных о заказах"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка получения данных клиента: ${e.message}")

                _myOrders.postValue(Resource.Error(e.message ?: "Ошибка получения данных клиента"))
            }
        }

    }


    fun getProfile(){
        viewModelScope.launch {
            try {
                val response = repository.getProfile()
                _user.postValue(Resource.Loading())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let { saveUser(it) }
                    Log.d("getProfile", "Successful: $responseBody")
                }else{
                    _user.postValue(Resource.Error("Ошибка получения данных клиента"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка получения данных клиента: ${e.message}")

                _user.postValue(Resource.Error(e.message ?: "Ошибка получения данных клиента"))
            }
        }

    }

    fun updateProfile(phone_number: String,
                      first_name: String,
                      birth_date: String?){
        viewModelScope.launch {
            try {
                val request = User(phone_number, first_name, birth_date)
                val response = repository.updateProfile(request)
                _updateResult.postValue(Resource.Loading())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.detail?.let { _updateResult.postValue(Resource.Success(it)) }
                    Log.d("updateProfile", "Successful: $responseBody")
                }else{
                    _updateResult.postValue(Resource.Error("Ошибка редактирования профиля"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка редактирования профиля: ${e.message}")

                _updateResult.postValue(Resource.Error(e.message ?: "Ошибка редактирования профиля"))
            }
        }
    }
    }