package com.example.clientneocafe.view.loginAndRegistration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.clientneocafe.R
import com.example.clientneocafe.databinding.FragmentRegistrationBinding
import com.example.clientneocafe.utils.PhoneMask

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        countryCode()
        checkInput()

    }

    private fun setupListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_dataFragment)
        }

        binding.btnGetCode.setOnClickListener {
            if (validateInput()) {
//                для теста
                findNavController().navigate(R.id.action_registrationFragment_to_codeFragment)
                //observe()
            }
        }
    }
    private fun observe() {
        val phoneCode = binding.textInputPhoneCode.text.toString()
        val phoneNumber = binding.textInputPhone.text.toString()
        val phone = "$phoneCode $phoneNumber"
        binding.textError.text = getText(R.string.error_phone)

    }

    private fun validateInput(): Boolean {
        val nameInput = binding.textInputName.text.toString().trim()
        val phoneInput = binding.textInputPhone.text.toString().trim()

        if (nameInput.isEmpty()) {
            binding.textInputName.error = "Заполните это поле"
            return false
        }

        if (phoneInput.isEmpty()) {
            binding.textInputPhone.error = "Заполните это поле"
            return false
        }

        return true
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
            editTextPhone.setText("+$countryCode") }
    }

}