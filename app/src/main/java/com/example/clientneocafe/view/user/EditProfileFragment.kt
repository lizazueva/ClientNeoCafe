package com.example.clientneocafe.view.user

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.clientneocafe.R
import com.example.clientneocafe.databinding.AlertDialogBranchesBinding
import com.example.clientneocafe.databinding.AlertDialogEditProfileBinding
import com.example.clientneocafe.databinding.FragmentEditProfileBinding
import com.example.clientneocafe.model.auth.User
import com.example.clientneocafe.utils.DateMask
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private  val userViewModel: UserViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
        setDataEditProfile()
    }

    private fun setUpListeners() {
        binding.imageBack.setOnClickListener {
            showAlertDialog()
        }
        binding.btnSave.setOnClickListener {
            val date = binding.textInputDate.text.toString()
            if (validateDate(date)){
                updateDataProfile()
                observeEditDataProfile()
            }
        }
    }

    private fun showAlertDialog() {
        val dialogBinding = AlertDialogEditProfileBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        dialogBinding.buttonNo.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.buttonYes.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.action_editProfileFragment_to_userFragment)
        }
    }

    private fun observeEditDataProfile() {
        userViewModel.updateResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Resource.Success ->{
                    Toast.makeText(requireContext(),"Профиль успешно изменен", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_editProfileFragment_to_userFragment)
                }
                is Resource.Loading ->{
                }
                is Resource.Error ->{
                    Toast.makeText(requireContext(),"Профиль не удалось изменить", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun setDataEditProfile() {
        val profile = arguments?.getParcelable<User>("user")

        val editTextDate = binding.textInputDate
        val dateMask = DateMask(editTextDate)
        editTextDate.addTextChangedListener(dateMask)

        profile?.let {
            binding.textInputName.setText(it.first_name)
            binding.textInputPhone.setText(it.phone_number)
            val date = it.birth_date.toString()
            val formattedDate = convertDateFormatGet(date, "yyyy-MM-dd", "MM.dd.yyyy")
            binding.textInputDate.setText(formattedDate)

        }
    }

    private fun updateDataProfile(){
        val name = binding.textInputName.text.toString().trim()
        val phone = binding.textInputPhone.text.toString().trim()
        val date = binding.textInputDate.text.toString().trim()
        val formattedDate = convertDateFormatSent(date, "MM.dd.yyyy", "yyyy-MM-dd")

        userViewModel.updateProfile(phone, name, formattedDate)
    }

    private fun convertDateFormatSent(dateInput: String, inputFormat: String, outputFormat: String): String {
        val inputFormatter = SimpleDateFormat(inputFormat, Locale.getDefault())
        val outputFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
        val parsedDate = inputFormatter.parse(dateInput)
        return outputFormatter.format(parsedDate)
    }

    private fun convertDateFormatGet(dateInput: String, inputFormat: String, outputFormat: String): String {
        val inputFormatter = SimpleDateFormat(inputFormat, Locale.getDefault())
        val outputFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
        val parsedDate = inputFormatter.parse(dateInput)
        return outputFormatter.format(parsedDate)
    }

    private fun validateDate(dateInput: String):Boolean {
        val isDateMatches = dateInput.matches(Regex("\\d{2}\\.\\d{2}\\.\\d{4}"))

        if (!isDateMatches) {
            binding.textInputDate.error = "Неверный формат даты"
            binding.textInputDate.setTextColor(Color.RED)
            return false
        } else {
            binding.textInputDate.error = null
            return true
        }
    }
}