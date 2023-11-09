package com.example.clientneocafe.view.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientneocafe.R
import com.example.clientneocafe.adapters.AdapterMenu
import com.example.clientneocafe.databinding.FragmentCartBinding
import com.example.clientneocafe.model.Product

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var adapterProduct: AdapterMenu
    lateinit var testProduct: ArrayList<Product>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
        setUpAdapter()

    }

    private fun setUpAdapter() {
        adapterProduct = AdapterMenu()
        binding.recyclerCart.adapter = adapterProduct
        binding.recyclerCart.layoutManager = LinearLayoutManager(requireContext())
        testProduct = arrayListOf (
            Product(1,"Кофе", "Капучино","Кофейный напиток", 170, R.drawable.img_donat, 1),
            Product(1,"Выпечка", "Капучино", "Кофейный напиток", 170, R.drawable.img_rectangle_test, 3),
            Product(1,"Коктейли", "Капучино", "Кофейный напиток", 170, R.drawable.img_coctail, 1),
            Product(1,"Чай", "Капучино", "Кофейный напиток", 170, R.drawable.img_rectangle_test, 5)
        )
        adapterProduct.differ.submitList(testProduct)
    }

    private fun setUpListeners() {
        binding.imageDots.setOnClickListener {
            setUpPopup()
        }
    }

    private fun setUpPopup() {
        val popup  = PopupMenu(requireContext(), binding.imageDots)
        popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.popup_history -> {
                    findNavController().navigate(R.id.action_cartFragment_to_historyFragment)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

}