package com.example.clientneocafe.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.clientneocafe.R
import com.example.clientneocafe.adapters.AdapterMenu
import com.example.clientneocafe.databinding.FragmentStartMenuBinding
import com.example.clientneocafe.model.Product
import com.example.clientneocafe.model.home.BranchesMenu
import com.example.clientneocafe.model.home.Category
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.viewModel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartMenuFragment : Fragment() {

    private lateinit var binding: FragmentStartMenuBinding
    private lateinit var adapterProduct: AdapterMenu
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()

        dataBranches()
        observeBranches()

        dataCategories()
        observeCategories()

        dataPopularItems()
        observePopularItems()

    }

    private fun observePopularItems() {
        homeViewModel.getPopularItems()
    }

    private fun dataPopularItems() {
        homeViewModel.popularItems.observe(viewLifecycleOwner){popularItems ->
            when(popularItems){
                is Resource.Success ->{
                    popularItems.data?.let { popularItems ->
                        setUpAdapter(popularItems)
                    }

                }
                is Resource.Error ->{
                    popularItems.message?.let {
                        Toast.makeText(requireContext(),
                            "Не удалось загрузить популярные товары",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading ->{

                }
            }
        }
    }

    private fun observeCategories() {
        homeViewModel.categories.observe(viewLifecycleOwner){categories ->
            when(categories){
                is Resource.Success ->{
                    val categoryList = categories.data
                    for (i in categoryList?.indices!!){
                        val category = categoryList[i]
                        when(i){
                            0 -> {
                                binding.includeMenu.textCoffee.text = category.name
                                Glide.with(binding.includeMenu.imageCoffee).load(category.image)
                                    .into(binding.includeMenu.imageCoffee)
                            }
                            1 -> {
                                binding.includeMenu.textDesert.text = category.name
                                Glide.with(binding.includeMenu.imageDesert).load(category.image)
                                    .into(binding.includeMenu.imageDesert)
                            }
                            2 -> {
                                binding.includeMenu.textCoctail.text = category.name
                                Glide.with(binding.includeMenu.imageCoctail).load(category.image)
                                    .into(binding.includeMenu.imageCoctail)
                            }
                            3 -> {
                                binding.includeMenu.textBakery.text = category.name
                                Glide.with(binding.includeMenu.imageBakery).load(category.image)
                                    .into(binding.includeMenu.imageBakery)
                            }
                            4 -> {
                                binding.includeMenu.textTea.text = category.name
                                Glide.with(binding.includeMenu.imageTea).load(category.image)
                                    .into(binding.includeMenu.imageTea)
                            }
                        }
                    }
                }
                is Resource.Error ->{
                    categories.message?.let {
                        Toast.makeText(requireContext(),
                            "Не удалось загрузить категории товаров",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading ->{

                }
            }
        }
    }

    private fun dataCategories() {
        homeViewModel.getCategories()
    }

    private fun observeBranches() {
        homeViewModel.branchesMenu.observe(viewLifecycleOwner){branches ->
            when(branches){
                is Resource.Success ->{
                    branches.data?.let { branches ->
                        dropdown(branches)
                    }

                }
                is Resource.Error ->{
                    branches.message?.let {
                        Toast.makeText(requireContext(),
                            "Не удалось загрузить филиалы",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading ->{

                }
            }
        }
    }

    private fun dataBranches() {
        homeViewModel.getBranchesForMenu()
    }

    private fun setUpListeners() {
        binding.includeMenu.cardForBakery.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_menuFragment)
        }
        binding.includeMenu.imageMoreCategory.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_menuFragment)
        }
    }

    private fun setUpAdapter(popularItems: List<Product>) {
        adapterProduct = AdapterMenu()
        binding.recyclerPopular.adapter = adapterProduct
        binding.recyclerPopular.layoutManager = LinearLayoutManager(requireContext())
        adapterProduct.differ.submitList(popularItems)

        adapterProduct.setOnItemClick(object: AdapterMenu.ListClickListener<Product>{
            override fun onClick(data: Product, position: Int) {
                //для теста
                findNavController().navigate(R.id.detailFragment)

            }

            override fun onAddClick(data: Product, position: Int) {
            }

            override fun onRemoveClick(data: Product, position: Int) {
            }

        })
    }

    private fun dropdown(branches: List<BranchesMenu>) {
        val branchNames = branches.map { it.name_of_shop }
        val spinner = binding.spinnerOptions
        val adapter: ArrayAdapter<String>

        if (branchNames.isNotEmpty()) {
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, branchNames)
        } else {
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf("Филиалов нет"))
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                val selectedBranch = branches[position]
                selectedBranch.id?.let { branchId ->
                    changeBranch(branchId)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        })
    }

    private fun changeBranch(branchId: Int) {
        homeViewModel.changeBranch(branchId)
        observeChangedBranch()
    }



    private fun observeChangedBranch() {
        homeViewModel.changedBranch.observe(viewLifecycleOwner){ chanchedBranch ->
            when(chanchedBranch){
                is Resource.Success ->{

                }
                is Resource.Error ->{
                    Toast.makeText(
                        requireContext(),
                        "Не удается поменять филиал",
                        Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading ->{

                }
            }
        }
    }


}