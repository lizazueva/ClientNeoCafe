package com.example.clientneocafe.di

import com.example.clientneocafe.api.Repository
import com.example.clientneocafe.api.RetrofitInstance
import com.example.clientneocafe.utils.SharedPreferencesBranch
import com.example.clientneocafe.viewModel.CartViewModel
import com.example.clientneocafe.viewModel.CodeViewModel
import com.example.clientneocafe.viewModel.DetailProductViewModel
import com.example.clientneocafe.viewModel.HomeViewModel
import com.example.clientneocafe.viewModel.LoginViewModel
import com.example.clientneocafe.viewModel.MapViewModel
import com.example.clientneocafe.viewModel.NotificationsViewModel
import com.example.clientneocafe.viewModel.OrdersViewModel
import com.example.clientneocafe.viewModel.RegistrationViewModel
import com.example.clientneocafe.viewModel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RetrofitInstance.api }
    factory { Repository(get()) }
}

val viewModules = module {
    viewModel {LoginViewModel (get())}
    viewModel {RegistrationViewModel (get())}
    viewModel {CodeViewModel (get())}
    viewModel {MapViewModel (get())}
    viewModel {UserViewModel (get())}
    viewModel {DetailProductViewModel (get())}
    viewModel {CartViewModel (get())}
    viewModel {OrdersViewModel (get())}
    viewModel {NotificationsViewModel (get())}

}
val homeScope = module {
    viewModel { HomeViewModel(get())}
}