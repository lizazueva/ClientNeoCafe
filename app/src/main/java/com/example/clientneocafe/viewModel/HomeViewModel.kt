package com.example.clientneocafe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientneocafe.api.Repository
import com.example.clientneocafe.model.home.BranchesMenu
import com.example.clientneocafe.model.home.ChangeBranch
import com.example.clientneocafe.utils.Resource
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository): ViewModel() {
    private val _branchesMenu: MutableLiveData<Resource<List<BranchesMenu>>> = MutableLiveData()

    val branchesMenu: LiveData<Resource<List<BranchesMenu>>>
        get() = _branchesMenu

    private fun saveBranches(response: List<BranchesMenu>) {
        _branchesMenu.postValue(Resource.Success(response))
    }

    private  val _changedBranch: MutableLiveData<Resource<String>> = MutableLiveData()
    val changedBranch: LiveData<Resource<String>>
        get() = _changedBranch


    fun getBranchesForMenu(){
        viewModelScope.launch {
            _branchesMenu.postValue(Resource.Loading())
            try {
                val response = repository.getBranchesForMenu()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let { saveBranches(it) }
                    Log.d("getBranchesForMenu", "Successful: $responseBody")

                }else{
                    val errorBody = response.errorBody()?.toString()
                    _branchesMenu.postValue(Resource.Error(errorBody ?: "Ошибка получения филиалов"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка загрузки: ${e.message}")
                _branchesMenu.postValue(Resource.Error(e.message ?: "Ошибка загрузки"))
            }
        }
    }

    fun changeBranch(branchId:Int){
        _changedBranch.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val request = ChangeBranch(branchId)
                val response = repository.changeBranch(request)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _changedBranch.postValue(Resource.Success(responseBody.message))
                    }
                    Log.d("changeBranch", "Successful: $responseBody")
                }else{
                    val errorBody = response.errorBody()?.string()
                    _changedBranch.postValue(
                        Resource.Error(errorBody ?: "Ошибка выбора филиала"))
                }
            }catch (e:Exception){
                Log.e("MyViewModel", "Ошибка кода: ${e.message}")
                _changedBranch.postValue(Resource.Error(e.message ?: "Ошибка выбора филиала"))
            }
        }
    }
}