package com.neobis.clientneocafe.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.neobis.clientneocafe.adapters.AdapterMenu
import com.neobis.clientneocafe.databinding.FragmentCategoryBinding
import com.neobis.clientneocafe.model.CheckPosition
import com.neobis.clientneocafe.model.DetailInfoProduct
import com.neobis.clientneocafe.utils.CartUtils
import com.neobis.clientneocafe.utils.Resource
import com.neobis.clientneocafe.viewModel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var adapterProduct: AdapterMenu
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryId = arguments?.getInt("categoryId")

        setUpListeners()

        if (categoryId != null) {
            dataMenuCategory(categoryId)
        }
        observeMenuCategory()


    }

    private fun setUpListeners() {

    }

    private fun observeMenuCategory() {
        homeViewModel.menuCategory.observe(viewLifecycleOwner){menuCategory ->
            when(menuCategory){
                is Resource.Success ->{
                    menuCategory.data?.let { menu ->
                        setUpAdapter(menu)
                    }

                }
                is Resource.Error ->{
                    menuCategory.message?.let {
                        Toast.makeText(requireContext(),
                            "Не удалось загрузить товары по категории",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading ->{

                }
            }
        }

    }

    private fun setUpAdapter(menu: List<DetailInfoProduct>) {
        adapterProduct = AdapterMenu()
        binding.recyclerCategory.adapter = adapterProduct
        binding.recyclerCategory.layoutManager = LinearLayoutManager(requireContext())
        adapterProduct.differ.submitList(menu)

        adapterProduct.setOnItemClick(object: AdapterMenu.ListClickListener<DetailInfoProduct>{
            override fun onClick(data: DetailInfoProduct, position: Int) {
                //для теста
                val idProduct = data.id
                val isReady = data.is_ready_made_product
                val action = MenuFragmentDirections.actionMenuFragmentToDetailFragment(idProduct, isReady)
                findNavController().navigate(action)

            }

            override fun onAddClick(data: DetailInfoProduct, position: Int) {
                if (CartUtils.isInCart(data.id)) {
                    val quantity = CartUtils.getQuantity(data.id) + 1
                    checkPosition(data, CheckPosition(data.is_ready_made_product, data.id, quantity))

                } else {
                    checkPosition(data, CheckPosition(data.is_ready_made_product, data.id,1))

                }
            }

            override fun onRemoveClick(data: DetailInfoProduct, position: Int) {
                CartUtils.removeItem(data)
            }

        })
    }

    private fun checkPosition(data: DetailInfoProduct, checkPosition: CheckPosition) {
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

    private fun dataMenuCategory(categoryId: Int) {
        homeViewModel.getMenuCategory(categoryId)
    }

    companion object {
        fun newInstance(categoryId: Int): CategoryFragment {
            val fragment = CategoryFragment()
            val args = Bundle()
            args.putInt("categoryId", categoryId)
            fragment.arguments = args
            return fragment
        }
    }
}