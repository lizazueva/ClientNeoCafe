package com.example.clientneocafe.view.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import com.example.clientneocafe.MainActivity
import com.example.clientneocafe.R
import com.example.clientneocafe.databinding.FragmentCodeBinding
import com.example.clientneocafe.model.auth.RegistrationRequest
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.viewModel.CodeViewModel
import com.example.clientneocafe.viewModel.LoginViewModel
import com.example.clientneocafe.viewModel.RegistrationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Response
import java.io.Serializable

class CodeFragment : Fragment() {

    private lateinit var binding: FragmentCodeBinding
    private val codeViewModel: CodeViewModel by viewModel()

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
        setUpTextPhone()


    }

    private fun setUpTextPhone() {
        if (arguments?.containsKey("phone") == true) {
            val phone = arguments?.getString("phone") as String
            binding.textErrorCode.text = getString(R.string.text_code, phone)

        } else if (arguments?.containsKey("user") == true) {
            val dataRepeatRegistration = arguments?.getParcelable<RegistrationRequest>("user")
            binding.textErrorCode.text = getString(R.string.text_code, dataRepeatRegistration?.phone_number)
        }
    }

    private fun setupListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnEnter.setOnClickListener {
            data()
        }
        binding.textSendAgain.setOnClickListener {
            repeatSentCode()
        }
    }

    private fun repeatSentCode() {
        codeViewModel.resendCode()
        codeViewModel.resendCodeResult.observe(viewLifecycleOwner){resendCodeResult->
            when(resendCodeResult){
            is Resource.Success -> {
                Toast.makeText(requireContext(), "Код отправлен повторно", Toast.LENGTH_SHORT).show()
            }
            is Resource.Error -> {
                Toast.makeText(requireContext(), "Не удалось отправить код", Toast.LENGTH_SHORT).show()
            }
            is Resource.Loading -> {

            }
        }
        }

    }

    private fun data() {
        val codeInput1 = binding.editCode1.text.toString().trim()
        val codeInput2 = binding.editCode2.text.toString().trim()
        val codeInput3 = binding.editCode3.text.toString().trim()
        val codeInput4 = binding.editCode4.text.toString().trim()
        val code = "$codeInput1$codeInput2$codeInput3$codeInput4"
        if (arguments?.containsKey("phone") == true) {
            val phone = arguments?.getString("phone") as String
            binding.textErrorCode.text = getString(R.string.text_code, phone)
            codeViewModel.confirmLogin(code)
            observePhone()

        } else if (arguments?.containsKey("user") == true) {
            val dataRepeatRegistration = arguments?.getParcelable<RegistrationRequest>("user")
            binding.textErrorCode.text = getString(R.string.text_code, dataRepeatRegistration?.phone_number)
            codeViewModel.confirmPhone(code)
            observeRegistration()
        }

    }

    private fun observeRegistration() {
        codeViewModel.confirmPhoneResult.observe(viewLifecycleOwner){codeConfirm ->
            when(codeConfirm){
                is Resource.Success -> {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }
                is Resource.Error -> {
                    binding.textErrorCode.setText("Код введен неверно, попробуйте еще раз")
                    binding.textErrorCode.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_error))
                }
                is Resource.Loading -> {

                }
            }
        }
    }

    private fun observePhone() {
        codeViewModel.token.observe(viewLifecycleOwner) { token ->
            when (token) {
                is Resource.Success -> {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }

                is Resource.Error -> {
                    binding.textErrorCode.setText("Код введен неверно, попробуйте еще раз")
                    binding.textErrorCode.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red_error
                        )
                    )
                }

                is Resource.Loading -> {

                }
            }
        }
    }
}