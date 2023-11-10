package com.example.clientneocafe.di

import com.example.clientneocafe.api.Repository
import com.example.clientneocafe.api.RetrofitInstance
import com.example.clientneocafe.viewModel.HomeViewModel
import com.example.clientneocafe.viewModel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

val appModule = module {
    single { RetrofitInstance.api }
    factory { Repository(get()) }
}

val viewModules = module {
    viewModel {HomeViewModel (get())}
    viewModel {LoginViewModel (get())}
}