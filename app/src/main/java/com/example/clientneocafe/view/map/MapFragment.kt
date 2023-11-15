package com.example.clientneocafe.view.map

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientneocafe.adapters.AdapterBranches
import com.example.clientneocafe.databinding.AlertDialogBranchesBinding
import com.example.clientneocafe.databinding.FragmentMapBinding
import com.example.clientneocafe.model.map.Branches
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.viewModel.MapViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private lateinit var adapterBranches: AdapterBranches
    private val mapViewModel: MapViewModel by viewModel()




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
        data()
        observe()

    }

    private fun observe() {
        mapViewModel.branches.observe(viewLifecycleOwner){response ->
            when(response){
                is Resource.Success ->{
                    response.data?.let { branches ->
                        binding.textNoBranches.isVisible = branches.isEmpty()
                        adapterBranches.differ.submitList(branches)
                        }

                }
                is Resource.Error ->{
                    response.message?.let {
                        Toast.makeText(requireContext(), "Не удалось загрузить филиалы", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading ->{

                }
            }
        }
    }

    private fun data() {
        mapViewModel.getBranches()
    }

    private fun setUpAdapter() {
        adapterBranches = AdapterBranches()
        binding.recyclerBranches.adapter = adapterBranches
        binding.recyclerBranches.layoutManager = LinearLayoutManager(requireContext())

        adapterBranches.setOnClickListener {selectedBranches ->
            alertDialog(selectedBranches)
        }
    }

    private fun alertDialog(selectedBranches: Branches) {
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
            mapGis(selectedBranches)
        }
    }

    private fun mapGis(selectedBranches: Branches) {
        val mapUri = Uri.parse(selectedBranches.link_to_map)
        val intent = Intent(Intent.ACTION_VIEW, mapUri)
        startActivity(intent)


    }

}