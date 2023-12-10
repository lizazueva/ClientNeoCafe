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
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientneocafe.R
import com.example.clientneocafe.adapters.AdapterMenu
import com.example.clientneocafe.databinding.AlertDialogDebitingBonusesBinding
import com.example.clientneocafe.databinding.AlertDialogOurBonusBinding
import com.example.clientneocafe.databinding.FragmentCartBinding
import com.example.clientneocafe.model.DetailInfoProduct
import com.example.clientneocafe.model.cart.CreateOrder
import com.example.clientneocafe.utils.CartUtils
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.viewModel.CartViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var adapterProduct: AdapterMenu
    private val cartViewModel: CartViewModel by viewModel()

    private var bonuses: Int = 0
    private var spentBonus: Int = 0
    private var cart: List<DetailInfoProduct> = emptyList()
    private var selectInstitution: Boolean = true
    private var totalOrderAmount: Int = 0

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
        bonusAccount()
        selectedButton()
        cartViewModel.clearOrder()
        observeOrder()

    }

    private fun setDataCard() {
        cart = CartUtils.getCartItems()
        if (cart.isNullOrEmpty()) {
            showEmptyCartMessage()
        } else {
            setUpAdapter(cart)
            updateTotalAmount(cart)
        }
    }

    private fun updateTotalAmount(cart: List<DetailInfoProduct>) {
        totalOrderAmount = calculateTotalOrderPrice(cart)
        binding.textAmount.text = "${totalOrderAmount} c"
    }

    private fun updateCart() {
        cart = CartUtils.getCartItems()
        setDataCard()
    }

    fun calculateTotalOrderPrice(cart: List<DetailInfoProduct>): Int {
        return cart.sumBy { it.price.toDouble().toInt() * it.quantity }
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
        binding.recyclerCart.visibility = View.INVISIBLE

    }

    private fun selectedButton() {
        val buttonRestaurant = binding.btnRestaurant
        val buttonTakeAway = binding.btnTakeAway

        buttonRestaurant.setOnClickListener {
            buttonRestaurant.isSelected = true
            buttonTakeAway.isSelected = false
            selectInstitution = true
        }

        buttonTakeAway.setOnClickListener {
            buttonTakeAway.isSelected = true
            buttonRestaurant.isSelected = false
            selectInstitution = false
        }

        buttonRestaurant.isSelected = true
        buttonTakeAway.isSelected = false
    }

    fun bonusAccount() {
        cartViewModel.getMyBonus()
        cartViewModel.bonuses.observe(viewLifecycleOwner){myBonuses ->
            when(myBonuses){
                is Resource.Success -> {
                    myBonuses.data?.bonus?.let { bonus ->
                        bonuses = bonus
                    }
                }

                is Resource.Error -> {
                }

                is Resource.Loading -> {
                }

            }
        }
    }

    private fun resultSum(enteredBonusesInt: Int) {
        val productPrice = cart.sumBy { it.price.toDouble().toInt() * it.quantity }

        if (enteredBonusesInt == 0) {
            binding.textAmount.text = "${productPrice} c"
        } else {
            val newPrice = productPrice - enteredBonusesInt

            val oldPriceText = "$productPrice c"
            val spannableString = SpannableString(oldPriceText)

            spannableString.setSpan(
                StrikethroughSpan(),
                0,
                oldPriceText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.grey_hint)),
                0,
                oldPriceText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            binding.textAmount.text = TextUtils.concat(spannableString, " $newPrice c")
        }

    }

    private fun setUpAdapter(cart: List<DetailInfoProduct>) {
        adapterProduct = AdapterMenu()
        binding.recyclerCart.adapter = adapterProduct
        binding.recyclerCart.layoutManager = LinearLayoutManager(requireContext())
        adapterProduct.differ.submitList(cart)

        adapterProduct.setOnItemClick(object : AdapterMenu.ListClickListener<DetailInfoProduct> {

            override fun onClick(data: DetailInfoProduct, position: Int) {
            }

            override fun onAddClick(data: DetailInfoProduct, position: Int) {
                CartUtils.addItem(data)
                updateCart()

            }

            override fun onRemoveClick(data: DetailInfoProduct, position: Int) {
                if (data.quantity > 1) {
                    CartUtils.removeItem(data)
                } else {
                    CartUtils.removeItem(data)
                    adapterProduct.removeItem(position)
                    // Обновление списка после удаления
                }
                updateCart()
            }
        })
    }


    private fun setUpListeners() {
        binding.btnOrder.setOnClickListener {
            placeOrder()
        }
        binding.btnAddMore.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
        }
        binding.btnInMenu.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
        }
    }

    private fun placeOrder() {
        if (bonuses > 0) {
            alertDialogBonuses()
        } else {
            createOrder()
        }
    }

    private fun alertDialogBonuses() {
        val dialogBinding = AlertDialogDebitingBonusesBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        dialogBinding.textDebitingBonuses.text = getString(R.string.text_debiting_bonuses, bonuses)

        dialogBinding.buttonNo.setOnClickListener {
            dialog.dismiss()
            createOrder()
        }

        dialogBinding.buttonYes.setOnClickListener {
            dialog.dismiss()
            alertDialogOurBonuses()
        }
    }

    private fun alertDialogOurBonuses() {

        val dialogBinding = AlertDialogOurBonusBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        dialogBinding.textOurBonuses.text = getString(R.string.text_our_bonuses, bonuses)


        var editTextBonuses = dialogBinding.editTextBonuses

        dialogBinding.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.buttonWriteBonuses.setOnClickListener {
            //списание бонусов
            validateBonuses(editTextBonuses, dialog, totalOrderAmount)
        }

    }

    private fun validateBonuses(editTextBonuses: EditText, dialog: Dialog, amount: Int) {
        var enteredBonuses = editTextBonuses.text?.toString()?.trim()
        var enteredBonusesInt = enteredBonuses?.toIntOrNull()

        if (enteredBonuses.isNullOrEmpty()) {
            editTextBonuses.error = "Заполните поле"
        } else {
            if (enteredBonusesInt == null) {
                editTextBonuses.error = "Введите корректное количество бонусов"
            } else if (enteredBonusesInt == 0) {
                editTextBonuses.error = "Введите количество бонусов"
            } else {
                if (enteredBonusesInt > bonuses) {
                    editTextBonuses.error = "Введенное значение превышает доступные бонусы"
                } else if (enteredBonusesInt > totalOrderAmount) {
                    editTextBonuses.error = "Итоговая сумма меньше введенных бонусов"
                } else {
                    totalOrderAmount -= enteredBonusesInt
                    resultSum(enteredBonusesInt)
                    spentBonus = enteredBonusesInt
                    bonuses -= enteredBonusesInt
                    dialog.dismiss()
                    createOrder()
                }
            }
        }
    }

    fun createOrder() {
        val product = CartUtils.getCartItems()

        val items = product.map {
            CreateOrder.Item(
                item = it.id,
                quantity = it.quantity
            )
        }
        val order = CreateOrder(
            in_an_institution = selectInstitution,
            items = items,
            spent_bonus_points = spentBonus,
            total_price = totalOrderAmount
        )

        cartViewModel.createOrder(order)

    }

    private fun observeOrder() {
        cartViewModel.order.observe(viewLifecycleOwner) { order ->
            when (order) {
                is Resource.Success -> {
                    Log.d("Order", "Success: ${order.data}")
                    snackBar()
                    CartUtils.clearCartItems()
                    updateCart()
                }

                is Resource.Error -> {
                    Log.e("Order", "Error: ${order.message}")
                    order.message?.let {
                        Toast.makeText(requireContext(),
                            "Не удалось оформить заказ",
                            Toast.LENGTH_SHORT).show()
                    }
                    bonusAccount()
                }

                is Resource.Loading -> {
                }
                null -> {
                    // Ничего не делать при значении null
                }
            }
        }
    }
    private fun snackBar() {
        val snackbar = Snackbar.make(binding.root, "", Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        val snackbarLayout = snackbarView as Snackbar.SnackbarLayout

        val layoutParams = snackbarLayout.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.gravity = Gravity.TOP

        snackbarLayout.setBackgroundColor(Color.TRANSPARENT)

        val customSnackbarView = layoutInflater.inflate(R.layout.snackbar_order, null)
        snackbarLayout.removeAllViews()
        snackbarLayout.addView(customSnackbarView)

        snackbar.show()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        cartViewModel.order.removeObservers(viewLifecycleOwner)
    }
}
