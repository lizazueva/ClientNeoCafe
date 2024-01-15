package com.neobis.clientneocafe.view.login.registration

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.neobis.clientneocafe.R
import com.neobis.clientneocafe.databinding.FragmentDataBinding
import com.neobis.clientneocafe.utils.DateMask
import java.text.SimpleDateFormat
import java.util.Locale

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
            if (validateDate(dateInput)){
                val formattedDate = convertDateFormat(dateInput, "MM.dd.yyyy", "yyyy-MM-dd")
                val action = DataFragmentDirections.actionDataFragmentToRegistrationFragment(formattedDate)
                findNavController().navigate(action)
            }
        }
    }

    private fun convertDateFormat(dateInput: String, inputFormat: String, outputFormat: String): String {
            val inputFormatter = SimpleDateFormat(inputFormat, Locale.getDefault())
            val outputFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
            val parsedDate = inputFormatter.parse(dateInput)
            return outputFormatter.format(parsedDate)
    }

    private fun validateDate(dateInput: String):Boolean {
        val isDateEmpty = dateInput.isEmpty()
        val isDateMatches = dateInput.matches(Regex("\\d{2}\\.\\d{2}\\.\\d{4}"))

        if (!isDateMatches) {
            binding.textInputDate.error = "Неверный формат даты"
            binding.textInputDate.setTextColor(Color.RED)
            return false
        } else if (isDateEmpty) {
            binding.textInputDate.error = "Заполните это поле"
            binding.textInputDate.setTextColor(Color.RED)
            return false
        } else {
            binding.textInputDate.error = null
            return true
        }
    }
}