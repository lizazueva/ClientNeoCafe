package com.neobis.clientneocafe.view.user

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
import com.neobis.clientneocafe.R
import com.neobis.clientneocafe.databinding.AlertDialogEditProfileBinding
import com.neobis.clientneocafe.databinding.FragmentEditProfileBinding
import com.neobis.clientneocafe.model.user.UserInfo
import com.neobis.clientneocafe.utils.DateMask
import com.neobis.clientneocafe.utils.Resource
import com.neobis.clientneocafe.viewModel.UserViewModel
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
        val profile = arguments?.getParcelable<UserInfo>("user")

        profile?.let {
            binding.textInputName.setText(it.first_name)
            binding.textInputPhone.setText(it.phone_number)
            val date = it.birth_date
            if (date.isNullOrEmpty()){
                binding.textInputDate.setText("")
            }else{
                val editTextDate = binding.textInputDate
                val dateMask = DateMask(editTextDate)
                editTextDate.addTextChangedListener(dateMask)
                val formattedDate = convertDateFormatGet(date, "yyyy-MM-dd", "MM.dd.yyyy")
                binding.textInputDate.setText(formattedDate)
            }
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