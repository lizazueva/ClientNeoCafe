package com.neobis.clientneocafe.view.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.neobis.clientneocafe.MainActivity
import com.neobis.clientneocafe.R
import com.neobis.clientneocafe.databinding.FragmentCodeBinding
import com.neobis.clientneocafe.model.auth.User
import com.neobis.clientneocafe.utils.Resource
import com.neobis.clientneocafe.viewModel.CodeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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
            val dataRepeatRegistration = arguments?.getParcelable<User>("user")
            binding.textErrorCode.text = getString(R.string.text_code, dataRepeatRegistration?.phone_number)
        }
    }

    private fun setupListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnEnter.setOnClickListener {
            //для теста
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
//            data()
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
            val dataRepeatRegistration = arguments?.getParcelable<User>("user")
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