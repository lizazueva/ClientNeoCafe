package com.neobis.clientneocafe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neobis.clientneocafe.api.Repository
import com.neobis.clientneocafe.model.map.Branches
import com.neobis.clientneocafe.utils.Resource
import kotlinx.coroutines.launch

class MapViewModel(private val repository: Repository): ViewModel() {

    private val _branches: MutableLiveData<Resource<List<Branches>>> = MutableLiveData()

    val branches: LiveData<Resource<List<Branches>>>
        get() = _branches

    private fun saveBranches(response: List<Branches>) {
        _branches.postValue(Resource.Success(response))
    }


    fun getBranches(){
        viewModelScope.launch {
            _branches.postValue(Resource.Loading())
            try {
                val response = repository.getBranches()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let { saveBranches(it) }
                    Log.d("getBranches", "Successful: $responseBody")

                }else{
                    val errorBody = response.errorBody()?.toString()
                    _branches.postValue(Resource.Error(errorBody ?: "Ошибка получения филиалов"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка загрузки: ${e.message}")
                _branches.postValue(Resource.Error(e.message ?: "Ошибка загрузки"))
            }
        }

    }
}