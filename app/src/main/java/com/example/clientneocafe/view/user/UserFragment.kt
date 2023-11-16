package com.example.clientneocafe.view.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clientneocafe.R
import com.example.clientneocafe.databinding.FragmentUserBinding
import com.example.clientneocafe.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private  val userViewModel: UserViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}