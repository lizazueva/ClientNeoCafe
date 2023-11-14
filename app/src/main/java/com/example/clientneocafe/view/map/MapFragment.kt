package com.example.clientneocafe.view.map

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientneocafe.R
import com.example.clientneocafe.adapters.AdapterBranches
import com.example.clientneocafe.adapters.AdapterOrder
import com.example.clientneocafe.databinding.AlertDialogBranchesBinding
import com.example.clientneocafe.databinding.AlertDialogDebitingBonusesBinding
import com.example.clientneocafe.databinding.FragmentMapBinding
import com.example.clientneocafe.view.cart.HistoryFragmentDirections

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private lateinit var adapterBranches: AdapterBranches


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()

    }

    private fun setUpAdapter() {
        adapterBranches = AdapterBranches()
        binding.recyclerBranches.adapter = adapterBranches
        binding.recyclerBranches.layoutManager = LinearLayoutManager(requireContext())

        adapterBranches.setOnClickListener {
            alertDialog()
        }
    }

    private fun alertDialog() {
        val dialogBinding = AlertDialogBranchesBinding.inflate(layoutInflater)
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
            mapGis()
        }
    }

    private fun mapGis() {

    }

}