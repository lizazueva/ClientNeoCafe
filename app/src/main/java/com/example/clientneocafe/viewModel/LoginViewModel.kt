package com.example.clientneocafe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientneocafe.api.Repository
import com.example.clientneocafe.model.auth.LoginRequest
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.utils.Utils
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository): ViewModel() {
    private val _preToken: MutableLiveData<Resource<String>> = MutableLiveData()

    val preToken: LiveData<Resource<String>>
        get() = _preToken

    private fun savePreToken(response: String){
        _preToken.postValue(Resource.Success(response))
    }

    fun login (phone_number:String){
        viewModelScope.launch {
            val request = LoginRequest(phone_number)
            _preToken.postValue(Resource.Loading())
            try {
                val response = repository.login(request)
                if (response.isSuccessful){
                    val responseBody = response.body()
                    responseBody?.pre_token?.let { savePreToken(it) }
                    if (responseBody != null) {
                        Utils.pre_token = responseBody.pre_token
                    }
                    Log.d("Login", "Successful: $responseBody")
                }else{
                    val errorBody = response.errorBody()?.toString()
                    _preToken.postValue(Resource.Error(errorBody ?: "Ошибка кода"))
                }
            }catch (e: Exception){
                _preToken.postValue(Resource.Error(e.message ?: "Ошибка авторизации" ))
            }
        }
    }
}