package com.example.clientneocafe.view.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import com.example.clientneocafe.MainActivity
import com.example.clientneocafe.databinding.FragmentCodeBinding
import com.example.clientneocafe.model.auth.RegistrationRequest
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.viewModel.CodeViewModel
import com.example.clientneocafe.viewModel.RegistrationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Response
import java.io.Serializable

class CodeFragment : Fragment() {

    private lateinit var binding: FragmentCodeBinding
    private val codeViewModel: CodeViewModel by viewModel()
    private val registrationViewModel: RegistrationViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

    }

    private fun setupListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnEnter.setOnClickListener {
            dataCode()
        }
        binding.textSendAgain.setOnClickListener {
            val dataRepeatRegistration = arguments?.getParcelable<RegistrationRequest>("user")
            if (dataRepeatRegistration != null) {
                repeatRegistration(dataRepeatRegistration)
            }
        }
    }

    private fun dataCode() {
        val codeInput1 = binding.editCode1.text.toString().trim()
        val codeInput2 = binding.editCode2.text.toString().trim()
        val codeInput3 = binding.editCode3.text.toString().trim()
        val codeInput4 = binding.editCode4.text.toString().trim()
        val code = "$codeInput1$codeInput2$codeInput3$codeInput4"
        codeViewModel.confirmPhone(code)
        observe()

    }

    private fun observe() {
        codeViewModel.confirmPhoneResult.observe(viewLifecycleOwner){codeConfirm ->
            when(codeConfirm){
                is Resource.Success -> {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }
                is Resource.Error -> {
                    binding.textErrorCode.setText("Код введен неверно, попробуйте еще раз")

                }
                is Resource.Loading -> {

                }
            }
        }
    }

    private fun repeatRegistration(dataRepeatRegistration: RegistrationRequest) {
            registrationViewModel.registration(dataRepeatRegistration.phone_number, dataRepeatRegistration.first_name, dataRepeatRegistration?.birth_date)
    }


}