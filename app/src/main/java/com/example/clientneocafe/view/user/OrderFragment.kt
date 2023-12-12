package com.example.clientneocafe.view.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientneocafe.R
import com.example.clientneocafe.adapters.AdapterMenu
import com.example.clientneocafe.databinding.FragmentOrderBinding
import com.example.clientneocafe.model.Product

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private lateinit var adapterProduct: AdapterMenu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
        setUpAdapter()
    }

    private fun setUpAdapter() {
        adapterProduct =AdapterMenu()
        binding.recyclerOrder.adapter = adapterProduct
        binding.recyclerOrder.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUpListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigate(R.id.action_orderFragment_to_userFragment)
        }
        binding.imageBell.setOnClickListener {
            findNavController().navigate(R.id.action_orderFragment_to_notificationsFragment)
        }

    }

}