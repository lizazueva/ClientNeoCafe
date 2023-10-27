package com.example.clientneocafe.view.loginAndRegistration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clientneocafe.R
import com.example.clientneocafe.databinding.FragmentLoginBinding
import com.example.clientneocafe.utils.PhoneMask


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

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
            findNavController().navigate(R.id.action_loginFragment_to_codeFragment)

            observe()
        }

        countryCode()
        checkInput()
    }

    private fun observe() {
        val phoneCode = binding.textInputPhoneCode.text.toString()
        val phoneNumber = binding.textInputPhone.text.toString()
        val phone = "$phoneCode $phoneNumber"
//        binding.textError.text = getText(R.string.error_phone)

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