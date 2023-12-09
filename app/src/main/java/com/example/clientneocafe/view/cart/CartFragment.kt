package com.example.clientneocafe.view.cart

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientneocafe.R
import com.example.clientneocafe.adapters.AdapterCart
import com.example.clientneocafe.adapters.AdapterMenu
import com.example.clientneocafe.databinding.AlertDialogDebitingBonusesBinding
import com.example.clientneocafe.databinding.AlertDialogOurBonusBinding
import com.example.clientneocafe.databinding.FragmentCartBinding
import com.example.clientneocafe.model.DetailInfoProduct
import com.example.clientneocafe.model.Product
import com.example.clientneocafe.utils.CartItem
import com.example.clientneocafe.utils.CartUtils

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var adapterProduct: AdapterMenu
    private var bonuses: Int = 0
    private var enteredBonusesInt: Int = 0
    private var cart: List<DetailInfoProduct> = emptyList()

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
        setDataCard()
//        resultSum(enteredBonusesInt)
//        bonusAccount()
        selectedButton()

    }

    private fun setDataCard() {
        cart = CartUtils.getCartItems()
        if (cart.isNullOrEmpty()){
            showEmptyCartMessage()
        }else{
            setUpAdapter(cart)
        }
    }

    private fun updateCart() {
        cart = CartUtils.getCartItems()
        setDataCard()
    }


    private fun showEmptyCartMessage() {
        binding.imageCat.visibility = View.VISIBLE
        binding.btnRestaurant.visibility = View.INVISIBLE
        binding.btnOrder.visibility = View.INVISIBLE
        binding.btnTakeAway.visibility = View.INVISIBLE
        binding.btnInMenu.visibility = View.VISIBLE
        binding.btnAddMore.visibility = View.INVISIBLE
        binding.textNoChoose.visibility = View.VISIBLE
        binding.textCart.visibility = View.INVISIBLE
        binding.textResult.visibility = View.INVISIBLE
        binding.textAmount.visibility = View.INVISIBLE

    }

    private fun selectedButton() {
        val buttonRestaurant = binding.btnRestaurant
        val buttonTakeAway = binding.btnTakeAway

        buttonRestaurant.setOnClickListener {
            buttonRestaurant.isSelected = true
            buttonTakeAway.isSelected = false
        }

        buttonTakeAway.setOnClickListener {
            buttonTakeAway.isSelected = true
            buttonRestaurant.isSelected = false
        }

        buttonRestaurant.isSelected = true
        buttonTakeAway.isSelected = false
    }

    fun bonusAccount() {
        bonuses = 150
    }

//    private fun resultSum(enteredBonusesInt: Int) {
//        val productPrice = testProduct.sumOf { it.amount }
//
//        if (enteredBonusesInt == 0){
//            binding.textAmount.text = "${productPrice} c"
//        } else{
//            val newPrice = productPrice - enteredBonusesInt
//
//        val oldPriceText = "$productPrice c"
//        val spannableString = SpannableString(oldPriceText)
//
//        spannableString.setSpan(
//            StrikethroughSpan(),
//            0,
//            oldPriceText.length,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        spannableString.setSpan(
//            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.grey_hint)),
//            0,
//            oldPriceText.length,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//
//        binding.textAmount.text = TextUtils.concat(spannableString, " $newPrice c")
//    }
//
//    }

    private fun setUpAdapter(cart: List<DetailInfoProduct>) {
        adapterProduct = AdapterMenu()
        binding.recyclerCart.adapter = adapterProduct
        binding.recyclerCart.layoutManager = LinearLayoutManager(requireContext())
        adapterProduct.differ.submitList(cart)

        adapterProduct.setOnItemClick(object: AdapterMenu.ListClickListener<DetailInfoProduct>{

            override fun onClick(data: DetailInfoProduct, position: Int) {
            }

            override fun onAddClick(data: DetailInfoProduct, position: Int) {
                CartUtils.addItem(data)
            }

            override fun onRemoveClick(data: DetailInfoProduct, position: Int) {
                if (data.quantity > 1) {
                    CartUtils.removeItem(data)
                } else {
                    CartUtils.removeItem(data)
                    adapterProduct.removeItem(position)
                    // Обновление списка после удаления
                    updateCart()
                }
            }
        })
    }

    private fun setUpListeners() {

//        binding.btnOrder.setOnClickListener {
//            if (bonuses == 0) {
//            } else {
//                alertDialogBonuses()
//            }
//        }
        binding.btnAddMore.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
        }
        binding.btnInMenu.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
        }
    }

//    private fun alertDialogBonuses() {
//        val dialogBinding = AlertDialogDebitingBonusesBinding.inflate(layoutInflater)
//        val dialog = Dialog(requireContext())
//
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(true)
//        dialog.setContentView(dialogBinding.root)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.show()
//
//        dialogBinding.textDebitingBonuses.text = getString(R.string.text_debiting_bonuses, bonuses)
//
//        dialogBinding.buttonNo.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialogBinding.buttonYes.setOnClickListener {
//            dialog.dismiss()
//            alertDialogOurBonuses()
//        }
//    }

//    private fun alertDialogOurBonuses() {
//
//        val dialogBinding = AlertDialogOurBonusBinding.inflate(layoutInflater)
//        val dialog = Dialog(requireContext())
//
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(true)
//        dialog.setContentView(dialogBinding.root)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.show()
//
//        var editTextBonuses = dialogBinding.editTextBonuses
//
//        dialogBinding.buttonCancel.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialogBinding.buttonWriteBonuses.setOnClickListener {
//            //списание бонусов
//            validateBonuses(editTextBonuses,dialog)
//        }
//
//    }

//    private fun validateBonuses(editTextBonuses: EditText, dialog: Dialog) {
//        var enteredBonuses = editTextBonuses.text?.toString()?.trim()
//        var enteredBonusesInt = enteredBonuses?.toInt()
//        var productPrice = testProduct.sumOf { it.amount }
//
//        if (enteredBonuses.isNullOrEmpty()) {
//            editTextBonuses.error = "Заполните поле"
//        }else if (enteredBonusesInt == 0){
//            editTextBonuses.error = "Введите количество бонусов"
//        } else if (enteredBonusesInt != null) {
//            if (enteredBonusesInt > bonuses) {
//                editTextBonuses.error = "Введенное значение превышает доступные бонусы"
//            } else if (enteredBonusesInt > productPrice) {
//                editTextBonuses.error = "Итоговая сумма меньше введенных бонусов"
//            }else{
//                productPrice -= enteredBonusesInt
//                resultSum(enteredBonusesInt)
//                bonuses -= enteredBonusesInt
//                dialog.dismiss()
//            }
//        }
//    }
}
