package com.example.clientneocafe.view.loginAndRegistration

import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.clientneocafe.R
import com.example.clientneocafe.databinding.FragmentDataBinding
import com.example.clientneocafe.utils.DateMask

class DataFragment : Fragment() {

    private lateinit var binding: FragmentDataBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        checkInput()
    }

    private fun setupListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigate(R.id.action_dataFragment_to_startFragment)
        }
        binding.textSkip.setOnClickListener {
            findNavController().navigate(R.id.action_dataFragment_to_registrationFragment)
        }
    }

    private fun checkInput() {

        val editTextDate = binding.textInputDate
        val dateMask = DateMask(editTextDate)
        editTextDate.addTextChangedListener(dateMask)
        binding.btnEnter.setOnClickListener {
            val dateInput = editTextDate.text.toString().trim()
            validateDate(dateInput)
        }
    }

    private fun validateDate(dateInput: String) {
        val isDateEmpty = dateInput.isEmpty()
        val isDateMatches = dateInput.matches(Regex("\\d{2}\\.\\d{2}\\.\\d{4}"))

        if (!isDateMatches) {
            binding.textInputDate.error = "Неверный формат даты"
            binding.textInputDate.setTextColor(Color.RED)
        } else if (isDateEmpty) {
            binding.textInputDate.error = "Заполните это поле"
            binding.textInputDate.setTextColor(Color.RED)
        } else {
            binding.textInputDate.error = null
            findNavController().navigate(R.id.action_dataFragment_to_registrationFragment)
        }
    }

}