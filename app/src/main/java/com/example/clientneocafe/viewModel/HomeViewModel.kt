package com.example.clientneocafe.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientneocafe.api.Repository
import com.example.clientneocafe.model.DetailInfoProduct
import com.example.clientneocafe.model.Product
import com.example.clientneocafe.model.home.BranchesMenu
import com.example.clientneocafe.model.home.Category
import com.example.clientneocafe.model.home.ChangeBranch
import com.example.clientneocafe.model.home.SearchResultResponse
import com.example.clientneocafe.utils.Resource
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository): ViewModel() {


//получение списка категорий для меню
    private val _branchesMenu: MutableLiveData<Resource<List<BranchesMenu>>> = MutableLiveData()

    val branchesMenu: LiveData<Resource<List<BranchesMenu>>>
        get() = _branchesMenu

    private fun saveBranches(response: List<BranchesMenu>) {
        _branchesMenu.postValue(Resource.Success(response))
    }
// выбор филиала
    private  val _changedBranch: MutableLiveData<Resource<String>> = MutableLiveData()
    val changedBranch: LiveData<Resource<String>>
        get() = _changedBranch


//получение списка категорий для меню
    private val _categories: MutableLiveData<Resource<List<Category>>> = MutableLiveData()

    val categories: LiveData<Resource<List<Category>>>
        get() = _categories

    private fun saveCategories(response: List<Category>) {
        _categories.postValue(Resource.Success(response))
    }


//получение списка популярных позиций для меню
    private val _popularItems: MutableLiveData<Resource<List<DetailInfoProduct>>> = MutableLiveData()

    val popularItems: LiveData<Resource<List<DetailInfoProduct>>>
        get() = _popularItems

    private fun savePopularItems(response: List<DetailInfoProduct>) {
        _popularItems.postValue(Resource.Success(response))
    }

//получение списка позиций по категориям
    private val _menuCategory: MutableLiveData<Resource<List<DetailInfoProduct>>> = MutableLiveData()
    val menuCategory: LiveData<Resource<List<DetailInfoProduct>>>
        get() = _menuCategory

    private fun saveMenuCategory(response: List<DetailInfoProduct>) {
        _menuCategory.postValue(Resource.Success(response))
    }

    //получение списка найденных позиций для меню
    private val _searchItems: MutableLiveData<Resource<List<SearchResultResponse>>> = MutableLiveData()

    val searchItems: LiveData<Resource<List<SearchResultResponse>>>
        get() = _searchItems

    private fun saveSearchItems(response: List<SearchResultResponse>) {
        _searchItems.postValue(Resource.Success(response))
    }

    fun getSearchResult(q: String){
        viewModelScope.launch {
            _searchItems.postValue(Resource.Loading())
            try {
                val response = repository.getSearchResult(q)
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    productResponse?.let { saveSearchItems(it) }
                    Log.d("getSearchResult", "Successful: $productResponse")
                }else{
                    val errorBody = response.errorBody()?.toString()
                    _searchItems.postValue(Resource.Error(errorBody ?:"Ошибка загрузки товаров"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка загрузки: ${e.message}")
                _searchItems.postValue(Resource.Error(e.message ?: "Ошибка загрузки"))
            }
        }
    }

    fun getMenuCategory(id: Int) {
        viewModelScope.launch {
            _menuCategory.postValue(Resource.Loading())
            try {
                val response = repository.getMenuCategory(id)
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    productResponse?.let { saveMenuCategory(it) }
                    Log.d("getMenuCategory", "Successful: $productResponse")
                }else{
                    val errorBody = response.errorBody()?.toString()
                    _menuCategory.postValue(Resource.Error(errorBody ?:"Ошибка загрузки товаров"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка загрузки: ${e.message}")
                _menuCategory.postValue(Resource.Error(e.message ?: "Ошибка загрузки"))
            }
        }
    }


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

    fun getCategories(){
        viewModelScope.launch {
            _categories.postValue(Resource.Loading())
            try {
                val response = repository.getCategories()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let { saveCategories(it) }
                    Log.d("getCategories", "Successful: $responseBody")

                }else{
                    val errorBody = response.errorBody()?.toString()
                    _categories.postValue(Resource.Error(errorBody ?: "Ошибка получения категорий"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка загрузки: ${e.message}")
                _categories.postValue(Resource.Error(e.message ?: "Ошибка загрузки"))
            }
        }
    }

    fun getPopularItems(){
        viewModelScope.launch {
            _popularItems.postValue(Resource.Loading())
            try {
                val response = repository.getPopularItems()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let { savePopularItems(it) }
                    Log.d("getPopularItems", "Successful: $responseBody")

                }else{
                    val errorBody = response.errorBody()?.toString()
                    _popularItems.postValue(Resource.Error(errorBody ?: "Ошибка получения популярных позиций"))
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Ошибка загрузки: ${e.message}")
                _popularItems.postValue(Resource.Error(e.message ?: "Ошибка загрузки"))
            }
        }
    }
}