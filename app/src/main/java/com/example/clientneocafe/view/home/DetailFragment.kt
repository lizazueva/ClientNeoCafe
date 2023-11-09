package com.example.clientneocafe.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientneocafe.R
import com.example.clientneocafe.adapters.AdapterMilk
import com.example.clientneocafe.adapters.AdapterSyrup
import com.example.clientneocafe.databinding.FragmentDetailBinding
import com.example.clientneocafe.model.Milk
import com.example.clientneocafe.model.Product
import com.example.clientneocafe.model.Syrup

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var adapterSyrup: AdapterSyrup
    private lateinit var adapterMilk: AdapterMilk
    private lateinit var product: Product

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
        addProduct()
    }

    private fun addProduct() {
        product = Product(1, "Выпечка", "Брауни", "Свежая выпечка",170, R.drawable.img_rectangle_test,0 )

        //для теста

        binding.textTitle.text = product.title
        binding.textPrice.text = "${product.amount} c"
        binding.textDescription.text = product.discr

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

    private fun setUpAdapterMilk() {
        val milk = arrayListOf(Milk(1,false,"Безлактозное"), Milk(1,false,"Соевое"), Milk(3,false,"Кокосовое"))
        adapterMilk = AdapterMilk()
        binding.recyclerMilk.adapter = adapterMilk
        binding.recyclerMilk.layoutManager = LinearLayoutManager(requireContext())
        adapterMilk.differ.submitList(milk)


    }

    private fun setUpAdapterSyrup() {
        val syrup = arrayListOf(Syrup(1,false,"Кленовый"), Syrup(2,false,"Малиновый"), Syrup(3,false,"Вишневый"))

        adapterSyrup = AdapterSyrup()
        binding.recyclerSyrup.adapter = adapterSyrup
        binding.recyclerSyrup.layoutManager = LinearLayoutManager(requireContext())
        adapterSyrup.differ.submitList(syrup)
    }

    private fun setUpListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}