package com.example.clientneocafe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientneocafe.api.Repository
import com.example.clientneocafe.model.auth.ConfirmRegisterResponse
import com.example.clientneocafe.model.auth.User
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.utils.Utils
import kotlinx.coroutines.launch

class UserViewModel (private val repository: Repository): ViewModel() {

    private val _user: MutableLiveData<Resource<User>> = MutableLiveData()

    val user: LiveData<Resource<User>>
        get() = _user

    private fun saveUser (response: User){
        _user.postValue(Resource.Success(response))
    }

    private val _updateResult: MutableLiveData<Resource<String>> = MutableLiveData()

    val updateResult: LiveData<Resource<String>>
        get() = _updateResult

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