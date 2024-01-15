package com.neobis.clientneocafe.di

import com.neobis.clientneocafe.api.Repository
import com.neobis.clientneocafe.api.RetrofitInstance
import com.neobis.clientneocafe.viewModel.CartViewModel
import com.neobis.clientneocafe.viewModel.CodeViewModel
import com.neobis.clientneocafe.viewModel.DetailProductViewModel
import com.neobis.clientneocafe.viewModel.HomeViewModel
import com.neobis.clientneocafe.viewModel.LoginViewModel
import com.neobis.clientneocafe.viewModel.MapViewModel
import com.neobis.clientneocafe.viewModel.NotificationsViewModel
import com.neobis.clientneocafe.viewModel.OrdersViewModel
import com.neobis.clientneocafe.viewModel.RegistrationViewModel
import com.neobis.clientneocafe.viewModel.UserViewModel
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