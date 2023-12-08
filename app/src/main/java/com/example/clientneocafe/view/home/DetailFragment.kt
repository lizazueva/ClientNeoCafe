package com.example.clientneocafe.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.clientneocafe.adapters.AdapterMenu
import com.example.clientneocafe.adapters.AdapterMilk
import com.example.clientneocafe.adapters.AdapterSyrup
import com.example.clientneocafe.databinding.FragmentDetailBinding
import com.example.clientneocafe.model.DetailInfoProduct
import com.example.clientneocafe.model.Milk
import com.example.clientneocafe.model.Product
import com.example.clientneocafe.model.Syrup
import com.example.clientneocafe.utils.CartUtils
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.viewModel.DetailProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var adapterSyrup: AdapterSyrup
    private lateinit var adapterMilk: AdapterMilk
    private lateinit var adapterProduct: AdapterMenu
    private var currentDetailProduct: DetailInfoProduct? = null
    private val detailProductViewModel: DetailProductViewModel by viewModel()

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
        setUpAdapterMilk()

        setDataEditProfile()

    }

    private fun setDataEditProfile() {
        val productId = arguments?.getInt("id")
        if (productId != null) {
            detailProductViewModel.productDetail(productId)
        }
        observeDetailProduct()

        if (productId != null) {
            detailProductViewModel.getCompatibleItems(productId)
        }
        observeCompatibleItems()

    }

    private fun observeCompatibleItems() {
        detailProductViewModel.compatibleItems.observe(viewLifecycleOwner) { compatibleItems ->
            when (compatibleItems) {
                is Resource.Success -> {
                    compatibleItems.data?.let { items ->
                        setUpAdapter(items)
                    }
                    hideProgressBar()

                }

                is Resource.Error -> {
                    compatibleItems.message?.let {
                        Toast.makeText(
                            requireContext(),
                            "Не удалось загрузить товары",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    hideProgressBar()
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun observeDetailProduct() {
        detailProductViewModel.detailProduct.observe(viewLifecycleOwner) { detailProduct ->
            when (detailProduct) {
                is Resource.Success -> {
                    detailProduct.data?.let { detailInfo ->
                        addProduct(detailInfo)
                        currentDetailProduct = detailProduct.data
                        updateCartVisibility()
                        if (detailInfo.category.name == "Кофе") {
                            binding.textMilk.visibility = View.VISIBLE
                            binding.recyclerMilk.visibility = View.VISIBLE
                            binding.textSyrup.visibility = View.VISIBLE
                            binding.recyclerSyrup.visibility = View.VISIBLE
                            setUpAdapterSyrup()
                            setUpAdapterMilk()
                        }
                    }
                }

                is Resource.Error -> {
                    detailProduct.message?.let {
                        Toast.makeText(
                            requireContext(),
                            "Не удалось загрузить товар",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun setUpAdapter(items: List<DetailInfoProduct>) {
        adapterProduct = AdapterMenu()
        binding.recyclerAddition.adapter = adapterProduct
        binding.recyclerAddition.layoutManager = LinearLayoutManager(requireContext())
        adapterProduct.differ.submitList(items)

        adapterProduct.setOnItemClick(object : AdapterMenu.ListClickListener<DetailInfoProduct> {
            override fun onClick(data: DetailInfoProduct, position: Int) {
                //для теста
                val idProduct = data.id
                detailProductViewModel.productDetail(idProduct)
                observeDetailProduct()

                detailProductViewModel.getCompatibleItems(idProduct)
                observeCompatibleItems()
            }

            override fun onAddClick(data: DetailInfoProduct, position: Int) {
                CartUtils.addItem(data)
            }

            override fun onRemoveClick(data: DetailInfoProduct, position: Int) {
                CartUtils.removeItem(data)
            }
        })
    }

    private fun addProduct(detailProduct: DetailInfoProduct) {
        binding.textTitle.text = detailProduct.name
        binding.textPrice.text = "${detailProduct.price.toDouble().toInt()} c"
        binding.textDescription.text = detailProduct.description
        Glide.with(binding.imageProduct).load(detailProduct.image).into(binding.imageProduct)
    }


    private fun setUpAdapterMilk() {
        val milk = arrayListOf(
            Milk(1, false, "Безлактозное"),
            Milk(1, false, "Соевое"),
            Milk(3, false, "Кокосовое")
        )
        adapterMilk = AdapterMilk()
        binding.recyclerMilk.adapter = adapterMilk
        binding.recyclerMilk.layoutManager = LinearLayoutManager(requireContext())
        adapterMilk.differ.submitList(milk)
    }

    private fun setUpAdapterSyrup() {
        val syrup = arrayListOf(
            Syrup(1, false, "Кленовый"),
            Syrup(2, false, "Малиновый"),
            Syrup(3, false, "Вишневый")
        )

        adapterSyrup = AdapterSyrup()
        binding.recyclerSyrup.adapter = adapterSyrup
        binding.recyclerSyrup.layoutManager = LinearLayoutManager(requireContext())
        adapterSyrup.differ.submitList(syrup)
    }

    private fun setUpListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAdd.setOnClickListener {
            currentDetailProduct?.let { it1 ->
                CartUtils.addItem(it1)
            }
            updateCartVisibility()
        }
        binding.imageAdd.setOnClickListener {
            currentDetailProduct?.let { it1 ->
                CartUtils.addItem(it1)
            }
            updateCartVisibility()
        }
        binding.imageRemove.setOnClickListener {
            currentDetailProduct?.let { it1 ->
                CartUtils.removeItem(it1)
            }
            updateCartVisibility()
        }
    }
    private fun updateCartVisibility() {
        currentDetailProduct?.let { detailProduct ->
            val quantity = CartUtils.getQuantity(detailProduct.id)
            binding.textCount.text = quantity.toString()
            binding.imageAdd.visibility = if (quantity > 0) View.VISIBLE else View.INVISIBLE
            binding.textCount.visibility = if (quantity > 0) View.VISIBLE else View.INVISIBLE
            binding.imageRemove.visibility = if (quantity > 0) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.textNiceAddition.visibility = View.INVISIBLE
        binding.btnAdd.visibility = View.INVISIBLE

    }
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.textNiceAddition.visibility = View.VISIBLE
        binding.btnAdd.visibility = View.VISIBLE
    }
}