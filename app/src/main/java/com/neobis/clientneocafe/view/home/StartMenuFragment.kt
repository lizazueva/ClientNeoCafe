package com.neobis.clientneocafe.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.neobis.clientneocafe.R
import com.neobis.clientneocafe.adapters.AdapterMenu
import com.neobis.clientneocafe.databinding.FragmentStartMenuBinding
import com.neobis.clientneocafe.model.CheckPosition
import com.neobis.clientneocafe.model.DetailInfoProduct
import com.neobis.clientneocafe.model.home.BranchesMenu
import com.neobis.clientneocafe.model.home.Category
import com.neobis.clientneocafe.utils.CartUtils
import com.neobis.clientneocafe.utils.Resource
import com.neobis.clientneocafe.utils.SharedPreferencesBranch
import com.neobis.clientneocafe.viewModel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class StartMenuFragment : Fragment() {

    private lateinit var binding: FragmentStartMenuBinding
    private lateinit var adapterProduct: AdapterMenu
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private var selectedCategoryId: Int? = null
    private lateinit var branchPreferences: SharedPreferencesBranch
    private  var selectedBranchId = 0





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        branchPreferences = SharedPreferencesBranch(requireContext())
        selectedBranchId = branchPreferences.loadSelectedBranchId()
        // Установка выбранного филиала в дропдауне

        setUpListeners()

        dataBranches()
        observeBranches()

    }

    private fun dataPopularItems() {
        homeViewModel.getPopularItems()
        observePopularItems()
    }

    private fun observePopularItems() {
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
                                listenerForCategories(binding.includeMenu.textCoffee,
                                    binding.includeMenu.cardForCoffee,
                                    binding.includeMenu.imageCoffee, category)
                            }
                            1 -> {
                                listenerForCategories(binding.includeMenu.textDesert,
                                    binding.includeMenu.cardForDesert,
                                    binding.includeMenu.imageDesert, category)
                            }
                            2 -> {
                                listenerForCategories(binding.includeMenu.textCoctail,
                                    binding.includeMenu.cardForCocktail,
                                    binding.includeMenu.imageCoctail, category )
                            }
                            3 -> {
                                listenerForCategories(binding.includeMenu.textBakery,
                                    binding.includeMenu.cardForBakery,
                                    binding.includeMenu.imageBakery, category )
                            }
                            4 -> {
                                listenerForCategories(binding.includeMenu.textTea,
                                    binding.includeMenu.cardForTea,
                                    binding.includeMenu.imageTea, category)
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

    private fun listenerForCategories(
        name: TextView,
        card: CardView,
        image: ImageView,
        category: Category
    ) {
        name.text = category.name
        Glide.with(image).load(category.image)
            .into(image)
        card.setOnClickListener {
            selectedCategoryId = category.id
            val bundle = Bundle().apply {
                putInt("id", selectedCategoryId!!)
            }
            findNavController().navigate(R.id.action_homeFragment_to_menuFragment, bundle)
        }
    }

    private fun dataCategories() {
        homeViewModel.getCategories()
        observeCategories()
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

        binding.includeMenu.imageMoreCategory.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_menuFragment)
        }
    }

    private fun setUpAdapter(popularItems: List<DetailInfoProduct>) {
        adapterProduct = AdapterMenu()
        binding.recyclerPopular.adapter = adapterProduct
        binding.recyclerPopular.layoutManager = LinearLayoutManager(requireContext())
        adapterProduct.differ.submitList(popularItems)

        adapterProduct.setOnItemClick(object: AdapterMenu.ListClickListener<DetailInfoProduct>{
            override fun onClick(data: DetailInfoProduct, position: Int) {
                //для теста
                val idProduct = data.id
                val isReady = data.is_ready_made_product
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(idProduct, isReady)
                findNavController().navigate(action)

            }

            override fun onAddClick(data: DetailInfoProduct, position: Int) {

                    if (CartUtils.isInCart(data.id)) {
                        val quantity = CartUtils.getQuantity(data.id) + 1
                        check1(data,CheckPosition(data.is_ready_made_product, data.id, quantity))
//                        checkPosition(data,CheckPosition(data.is_ready_made_product, data.id, quantity))

                    } else {
                        check1(data,CheckPosition(data.is_ready_made_product, data.id,1))


//                        checkPosition(data,CheckPosition(data.is_ready_made_product, data.id, 1))

                    }

            }

            override fun onRemoveClick(data: DetailInfoProduct, position: Int) {
                CartUtils.removeItem(data)
            }

        })
    }

    private fun check1(data: DetailInfoProduct, checkPosition: CheckPosition) {
        homeViewModel.createProduct(checkPosition,
            onSuccess = {
                CartUtils.addItem(data)
                adapterProduct.notifyDataSetChanged()

            },
            onError = {
                Toast.makeText(
                    requireContext(),
                    "Товара больше нет",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }


    private fun dropdown(branches: List<BranchesMenu>) {
        val branchNames = branches.map { it.name_of_shop }
        val spinner = binding.spinnerOptions
        val adapter: ArrayAdapter<String>

        if (branchNames.isNotEmpty()) {
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, branchNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            // Найти позицию выбранного филиала в списке
            val position = branches.indexOfFirst { it.id == selectedBranchId }
            if (position != -1) {
                spinner.setSelection(position)
            }
        } else {
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf("Филиалов нет"))
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                val selectedBranch = branches[position]
                selectedBranch.id.let { branchId ->
                    changeBranch(branchId)
                    dataCategories()
                    dataPopularItems()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        })
    }

    private fun changeBranch(branchId: Int) {
        branchPreferences.saveSelectedBranchId(branchId)

        homeViewModel.changeBranch(branchId)
        observeChangedBranch()
        //загрузки данных после изменения филиала
        dataCategories()
        dataPopularItems()
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