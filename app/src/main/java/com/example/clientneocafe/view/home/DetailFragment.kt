package com.example.clientneocafe.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientneocafe.R
import com.example.clientneocafe.adapters.AdapterSyrup
import com.example.clientneocafe.databinding.FragmentDetailBinding
import com.example.clientneocafe.model.Syrup

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var adapterSyrup: AdapterSyrup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners()
        setUpAdapterSyrup()
    }

    private fun setUpAdapterSyrup() {
        val syrup = arrayListOf(Syrup(1,false,"Кленовый"), Syrup(1,false,"Малиновый"), Syrup(1,false,"Вишневый"))

        adapterSyrup = AdapterSyrup()
        binding.recyclerSyrup.adapter = adapterSyrup
        binding.recyclerSyrup.layoutManager = LinearLayoutManager(requireContext())
        adapterSyrup.differ.submitList(syrup)
    }

    private fun setUpListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}