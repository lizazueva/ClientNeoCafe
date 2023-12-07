package com.example.clientneocafe.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.clientneocafe.R
import com.example.clientneocafe.adapters.SliderAdapter
import com.example.clientneocafe.databinding.FragmentMenuBinding
import com.example.clientneocafe.model.home.Category
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.viewModel.HomeViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private val homeViewModel: HomeViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
        dataCategories()
        observeCategories()



    }

    private fun setUpListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_homeFragment)
        }

    }

    private fun dataCategories() {
        homeViewModel.getCategories()
    }

    private fun observeCategories() {
        homeViewModel.categories.observe(viewLifecycleOwner){categories ->
            when(categories){
                is Resource.Success ->{
                    val categoryList = categories.data
                    categoryList?.let { categories ->
//                        val selectPosition = arguments?.getInt("id")
                        viewPager(categories)
                    }

                }
                is Resource.Error ->{
                    categories.message?.let {
                        Toast.makeText(requireContext(),
                            "Не удалось загрузить позиции по категориям",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading ->{

                }
            }
        }
    }

    private fun viewPager(categories: List<Category>) {
        val tabLayout = binding.tabLayout
        val viewPager2 = binding.pager
        val tabArray = categories.map { it.name }.toTypedArray()
        val adapter = SliderAdapter(childFragmentManager, lifecycle, categories)
        viewPager2.adapter = adapter

        val selectCategoryId = arguments?.getInt("id")
        if (selectCategoryId != null) {
            val selectPosition = categories.indexOfFirst { it.id == selectCategoryId }
            if (selectPosition != -1) {
                viewPager2.setCurrentItem(selectPosition, false)
            }
        }

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = tabArray[position]
        }.attach()
    }
}