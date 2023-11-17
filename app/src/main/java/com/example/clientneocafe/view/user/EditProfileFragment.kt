package com.example.clientneocafe.view.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.clientneocafe.R
import com.example.clientneocafe.databinding.FragmentEditProfileBinding
import com.example.clientneocafe.model.auth.User
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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
            findNavController().navigate(R.id.action_editProfileFragment_to_userFragment)
        }
        binding.btnSave.setOnClickListener {
            updateDataProfile()
            observeEditDataProfile()
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

        profile?.let {
            binding.textInputName.setText(it.first_name)
            binding.textInputPhone.setText(it.phone_number)
            binding.textInputDate.setText(it.birth_date)

        }
    }

    fun updateDataProfile(){
        val name = binding.textInputName.text.toString().trim()
        val phone = binding.textInputPhone.text.toString().trim()
        val date = binding.textInputDate.text.toString().trim()

        userViewModel.updateProfile(phone, name, date)
    }
}