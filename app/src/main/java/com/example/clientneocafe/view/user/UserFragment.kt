package com.example.clientneocafe.view.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.clientneocafe.databinding.FragmentUserBinding
import com.example.clientneocafe.model.auth.User
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private  val userViewModel: UserViewModel by viewModel()
    private var profile: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
        dataProfile()

    }

     private fun dataProfile() {
        userViewModel.getProfile()
        userViewModel.user.observe(viewLifecycleOwner){user ->
            when (user) {
                is Resource.Success -> {
                    binding.textUserName.text = user.data?.first_name
//                    profile = user.data.takeIf { it != null }

                    user.data?.let { profile = it }
                }
                is Resource.Loading ->{

                }
                is Resource.Error ->{

                }
            }
        }
    }

    private fun setUpListeners() {
        binding.imageEditProfile.setOnClickListener {
            val action = UserFragmentDirections.actionUserFragmentToEditProfileFragment(profile!!)
            findNavController().navigate(action)
        }
    }
}