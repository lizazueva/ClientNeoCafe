package com.example.clientneocafe.di

import com.example.clientneocafe.api.Repository
import com.example.clientneocafe.api.RetrofitInstance
import com.example.clientneocafe.viewModel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

val appModule = module {
    single { RetrofitInstance.api }
    single { Repository(get()) }
    viewModel {HomeViewModel (get())}
}