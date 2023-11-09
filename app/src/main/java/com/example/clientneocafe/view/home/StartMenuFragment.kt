package com.example.clientneocafe.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientneocafe.R
import com.example.clientneocafe.adapters.AdapterMenu
import com.example.clientneocafe.databinding.FragmentStartMenuBinding
import com.example.clientneocafe.model.Product

class StartMenuFragment : Fragment() {

    private lateinit var binding: FragmentStartMenuBinding
    private lateinit var adapterProduct: AdapterMenu
    lateinit var testProduct: ArrayList<Product>

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
        dropdown()
        setUpAdapter()

    }

    private fun setUpListeners() {
        binding.cardForBakery.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_menuFragment)
        }
        binding.imageMoreCategory.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_menuFragment)
        }
    }

    private fun setUpAdapter() {
        adapterProduct = AdapterMenu()
        binding.recyclerPopular.adapter = adapterProduct
        binding.recyclerPopular.layoutManager = LinearLayoutManager(requireContext())
        testProduct = arrayListOf (
            Product(1,"Кофе", "Капучино","Кофейный напиток", 170, R.drawable.img_donat, 0),
            Product(1,"Выпечка", "Капучино", "Кофейный напиток", 170, R.drawable.img_rectangle_test, 3),
            Product(1,"Коктейли", "Капучино", "Кофейный напиток", 170, R.drawable.img_coctail, 0),
            Product(1,"Десерты", "Капучино", "Кофейный напиток", 170, R.drawable.img_rectangle_test, 0),
            Product(1,"Чай", "Капучино", "Кофейный напиток", 170, R.drawable.img_rectangle_test, 0))
        adapterProduct.differ.submitList(testProduct)

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

    private fun dropdown() {
        val options = listOf("Вариант 1", "Вариант 2", "Вариант 3")

        val spinner = binding.spinnerOptions
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }


}