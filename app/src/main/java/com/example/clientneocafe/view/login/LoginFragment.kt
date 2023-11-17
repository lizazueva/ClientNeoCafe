package com.example.clientneocafe.view.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.clientneocafe.MainActivity
import com.example.clientneocafe.R
import com.example.clientneocafe.api.RetrofitInstance
import com.example.clientneocafe.databinding.FragmentLoginBinding
import com.example.clientneocafe.model.auth.LoginRequest
import com.example.clientneocafe.utils.PhoneMask
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.utils.Utils
import com.example.clientneocafe.viewModel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageBack.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_startFragment)
        }
        binding.btnGetCode.setOnClickListener {



            //для теста
            val phoneCode = binding.textInputPhoneCode.text.toString()
            val phoneNumber = binding.textInputPhone.text.toString()
            val phone = LoginRequest("$phoneCode $phoneNumber")

            lifecycleScope.launch {
                try {
                    val response = RetrofitInstance.api.login2(phone)

                    if(response.isSuccessful){
                        val access = response.body()?.access
                        access?.let { Utils.access_token = it }

                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Обработка ошибки, если запрос не успешен
                    }
                } catch (e: Exception) {
                    // Обработка исключения, например, вывод сообщения об ошибке
                }
            }






//            data()
        }

        countryCode()
        checkInput()
    }

    private fun data() {
        val phoneCode = binding.textInputPhoneCode.text.toString()
        val phoneNumber = binding.textInputPhone.text.toString()
        val phone = "$phoneCode $phoneNumber"
        loginViewModel.login(phone)
        observe(phone)

    }

    private fun observe(phone: String) {
        loginViewModel.preToken.observe(viewLifecycleOwner){ preToken->
            when(preToken) {
                is Resource.Success ->{
                    val bundle = Bundle().apply {
                        putString("phone", phone)
                    }
                    findNavController().navigate(R.id.action_loginFragment_to_codeFragment, bundle)
                }
                is Resource.Error ->{
                    binding.textError.text = getText(R.string.error_phone)
                }
                is Resource.Loading ->{

                }
            }
        }
    }

    private fun checkInput() {
        val textInputPhone = binding.textInputPhone
        val phoneMask = PhoneMask(textInputPhone)
        textInputPhone.addTextChangedListener(phoneMask)
    }

    //Функция для ввода кода страны в textInputPhone
    private fun countryCode() {
        val ccp = binding.ccp
        val editTextPhone = binding.textInputPhoneCode
        val countryCode = ccp.selectedCountryCode
        editTextPhone.setText("+$countryCode")

        ccp.setOnCountryChangeListener {
            val countryCode = ccp.selectedCountryCode
            editTextPhone.setText("+$countryCode")
        }
    }

}