package com.neobis.clientneocafe.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.neobis.clientneocafe.adapters.AdapterSearch
import com.neobis.clientneocafe.databinding.FragmentSearchBinding
import com.neobis.clientneocafe.model.CheckPosition
import com.neobis.clientneocafe.model.home.SearchResultResponse
import com.neobis.clientneocafe.utils.CartUtils
import com.neobis.clientneocafe.utils.Resource
import com.neobis.clientneocafe.viewModel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapterProduct: AdapterSearch
    private val homeViewModel: HomeViewModel by sharedViewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSearchResult(homeViewModel)
    }

    private fun observeSearchResult(homeViewModel: HomeViewModel) {
        homeViewModel.searchItems.observe(viewLifecycleOwner) { searchResult ->
            when (searchResult) {
                is Resource.Success -> {
                    val searchItems = searchResult.data
                    if (searchItems.isNullOrEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "Товары не найдены",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        setUpAdapter(searchItems)
                    }
                    hideProgressBar()
                }
                is Resource.Error -> {
                    searchResult.message?.let {
                        Toast.makeText(requireContext(),
                            "Не удалось загрузить найденные товары",
                            Toast.LENGTH_SHORT).show()
                    }
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun setUpAdapter(searchItems: List<SearchResultResponse>) {
        adapterProduct = AdapterSearch()
        binding.recyclerSearch.adapter = adapterProduct
        binding.recyclerSearch.layoutManager = LinearLayoutManager(requireContext())
        adapterProduct.differ.submitList(searchItems)

        adapterProduct.setOnItemClick(object: AdapterSearch.ListClickListener<SearchResultResponse>{
            override fun onClick(data: SearchResultResponse, position: Int) {
                val idProduct = data.id
                val isReady = data.is_ready_made_product
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(idProduct, isReady)
                findNavController().navigate(action)

            }

            override fun onAddClick(data: SearchResultResponse, position: Int) {
                if (CartUtils.isInCart(data.id)) {
                    val quantity = CartUtils.getQuantity(data.id) + 1
                    checkPosition(data,CheckPosition(data.is_ready_made_product, data.id, quantity))

                } else {
                    checkPosition(data,CheckPosition(data.is_ready_made_product, data.id,1))

                }
            }

            override fun onRemoveClick(data: SearchResultResponse, position: Int) {
                CartUtils.removeItem(data)

            }

        })
    }

    private fun checkPosition(data: SearchResultResponse, checkPosition: CheckPosition) {
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

}