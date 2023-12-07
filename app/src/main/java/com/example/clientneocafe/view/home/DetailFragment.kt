package com.example.clientneocafe.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.clientneocafe.adapters.AdapterMilk
import com.example.clientneocafe.adapters.AdapterSyrup
import com.example.clientneocafe.databinding.FragmentDetailBinding
import com.example.clientneocafe.model.DetailInfoProduct
import com.example.clientneocafe.model.Product
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.viewModel.DetailProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var adapterSyrup: AdapterSyrup
    private lateinit var adapterMilk: AdapterMilk
    private lateinit var product: Product
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
//        setUpAdapterSyrup()
//        setUpAdapterMilk()

        setDataEditProfile()

    }

    private fun setDataEditProfile() {
        val product_id = arguments?.getInt("id")
        if (product_id != null) {
            detailProductViewModel.productDetail(product_id)
        }
        detailProductViewModel.detailProduct.observe(viewLifecycleOwner){ detailProduct ->
            when(detailProduct){
                is Resource.Success ->{
                    detailProduct.data?.let { detailInfo ->
                        addProduct(detailInfo)
                    }

                }
                is Resource.Error ->{
                    detailProduct.message?.let {
                        Toast.makeText(requireContext(),
                            "Не удалось загрузить товар",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading ->{

                }
            }

        }


    }

    private fun addProduct(detailProduct: DetailInfoProduct) {

        //для теста

        binding.textTitle.text = detailProduct.name
        binding.textPrice.text = "${detailProduct.price} c"
        binding.textDescription.text = detailProduct.description

        binding.imageAdd.setOnClickListener {
            product.county +=1
            updateCountVisibility()
        }
        binding.imageRemove.setOnClickListener {
            product.county -=1
            updateCountVisibility()
        }
        updateCountVisibility()


    }

    private fun updateCountVisibility() {
        if (product.county>0){
            binding.textCount.text = product.county.toString()
            binding.imageRemove.visibility = View.VISIBLE
            binding.textCount.visibility =View.VISIBLE
        }else{
            binding.imageRemove.visibility = View.INVISIBLE
            binding.textCount.visibility = View.INVISIBLE
        }
    }

//    private fun setUpAdapterMilk() {
//        val milk = arrayListOf(Milk(1,false,"Безлактозное"), Milk(1,false,"Соевое"), Milk(3,false,"Кокосовое"))
//        adapterMilk = AdapterMilk()
//        binding.recyclerMilk.adapter = adapterMilk
//        binding.recyclerMilk.layoutManager = LinearLayoutManager(requireContext())
//        adapterMilk.differ.submitList(milk)
//
//
//    }
//
//    private fun setUpAdapterSyrup() {
//        val syrup = arrayListOf(Syrup(1,false,"Кленовый"), Syrup(2,false,"Малиновый"), Syrup(3,false,"Вишневый"))
//
//        adapterSyrup = AdapterSyrup()
//        binding.recyclerSyrup.adapter = adapterSyrup
//        binding.recyclerSyrup.layoutManager = LinearLayoutManager(requireContext())
//        adapterSyrup.differ.submitList(syrup)
//    }

    private fun setUpListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}