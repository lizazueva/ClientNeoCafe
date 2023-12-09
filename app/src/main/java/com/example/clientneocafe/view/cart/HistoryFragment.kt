package com.example.clientneocafe.view.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientneocafe.R
import com.example.clientneocafe.adapters.AdapterOrder
import com.example.clientneocafe.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapterOrder: AdapterOrder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        setUpListeners()

    }

    private fun setUpListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpAdapter() {
        adapterOrder = AdapterOrder()
        binding.recyclerOpenOrder.adapter = adapterOrder
        binding.recyclerOpenOrder.layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerClosedOrder.adapter = adapterOrder
        binding.recyclerClosedOrder.layoutManager = LinearLayoutManager(requireContext())

        adapterOrder.setOnClickListener {
            val action = HistoryFragmentDirections.actionHistoryFragmentToOrderFragment()
            findNavController().navigate(action)
        }
    }

}