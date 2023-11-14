package com.example.clientneocafe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientneocafe.api.Repository
import com.example.clientneocafe.model.auth.CodeAuth
import com.example.clientneocafe.utils.Resource
import kotlinx.coroutines.launch


class CodeViewModel (private val repository: Repository): ViewModel() {

    private val _confirmPhoneResult: MutableLiveData<Resource<String>> = MutableLiveData()
    val confirmPhoneResult: LiveData<Resource<String>>
        get() = _confirmPhoneResult


    fun confirmPhone(code: String) {
        val codeAuth = CodeAuth(code)
        _confirmPhoneResult.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val request = repository.confirmPhone(codeAuth)
                if (request.isSuccessful) {
                    val responseBody = request.body()
                    if (responseBody != null) {
                        _confirmPhoneResult.postValue(Resource.Success(responseBody))
                    }
                        Log.d("confirmPhone", "Successful: $responseBody")
                } else {
                    val errorBody = request.errorBody()?.string()
                    _confirmPhoneResult.postValue(Resource.Error(errorBody ?: "Ошибка кода"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка кода: ${e.message}")
                _confirmPhoneResult.postValue(Resource.Error(e.message ?: "Ошибка кода"))
            }
        }
    }
}