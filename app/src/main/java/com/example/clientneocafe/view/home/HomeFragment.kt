package com.example.clientneocafe.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientneocafe.R
import com.example.clientneocafe.adapters.AdapterMenu
import com.example.clientneocafe.databinding.FragmentHomeBinding
import com.example.clientneocafe.databinding.FragmentStartMenuBinding
import com.example.clientneocafe.model.Product

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFragment()
        showStartMenuFragment()
    }

    private fun showStartMenuFragment() {
        val transaction = childFragmentManager.beginTransaction()
        val fragment = StartMenuFragment()
        transaction.replace(R.id.fragment_start_menu, fragment)
        transaction.commit()
    }

    private fun setUpFragment() {
        binding.searchMenu.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val query = p0?.trim()
                if (query.isNullOrEmpty()) {
                    val transaction = childFragmentManager.beginTransaction()
                    val fragment = StartMenuFragment()
                    transaction.replace(R.id.fragment_start_menu, fragment)
                    transaction.commit()
                } else {
                    val transaction = childFragmentManager.beginTransaction()
                    val fragment = SearchFragment()
                    transaction.replace(R.id.fragment_start_menu, fragment)
                    transaction.commit()
                }
                return true
            }
        })
    }

}