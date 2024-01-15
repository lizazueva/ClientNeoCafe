package com.neobis.clientneocafe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neobis.clientneocafe.api.Repository
import com.neobis.clientneocafe.model.auth.User
import com.neobis.clientneocafe.utils.Resource
import com.neobis.clientneocafe.utils.Utils
import kotlinx.coroutines.launch

class RegistrationViewModel(private val repository: Repository): ViewModel() {
    private val _token: MutableLiveData<Resource<String>> = MutableLiveData()
    val token: LiveData<Resource<String>>
        get() = _token
    private fun saveToken(response: String) {
        _token.postValue(Resource.Success(response))
    }

    fun registration (phone_number: String,
                      first_name: String,
                      birth_date: String?){
        viewModelScope.launch {
            try {
                val request = User(phone_number, first_name, birth_date)
                val response = repository.registration(request)
                if (response.isSuccessful) {
                    _token.postValue(Resource.Loading())
                    val responseBody = response.body()
                    responseBody?.access?.let { saveToken(it) }
                    if (responseBody != null) {
                        Utils.access_token = responseBody.access
                    }
                    Log.d("Registration", "Successful: $responseBody")
                }else{
                    _token.postValue(Resource.Error("Ошибка регистрации"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка регистрации: ${e.message}")

                _token.postValue(Resource.Error(e.message ?: "Ошибка регистрации"))
            }
        }
    }
}