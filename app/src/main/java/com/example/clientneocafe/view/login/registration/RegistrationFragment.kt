package com.example.clientneocafe.view.login.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.clientneocafe.R
import com.example.clientneocafe.databinding.FragmentRegistrationBinding
import com.example.clientneocafe.model.auth.User
import com.example.clientneocafe.utils.PhoneMask
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.viewModel.RegistrationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val registrationViewModel: RegistrationViewModel by viewModel()


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
                data()
            }
        }
    }
    private fun data() {
        val phoneCode = binding.textInputPhoneCode.text.toString()
        val phoneNumber = binding.textInputPhone.text.toString()
        val name = binding.textInputName.text.toString().trim()
        var date = arguments?.getString("date")
        val phone = "$phoneCode $phoneNumber"
        registrationViewModel.registration(phone, name, date)
        observe(phone, name, date)
    }

    private fun observe(phone: String, name: String, date: String? ) {
        registrationViewModel.token.observe(viewLifecycleOwner) { token ->
            when (token) {
                is Resource.Success -> {
                    val action = RegistrationFragmentDirections.actionRegistrationFragmentToCodeFragment(User(phone, name, date))
                    findNavController().navigate(action)
                }

                is Resource.Error -> {
                    binding.textInputPhone.error = token.message ?: getString(R.string.error_phone)
                }

                is Resource.Loading -> {
                }
            }
        }
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