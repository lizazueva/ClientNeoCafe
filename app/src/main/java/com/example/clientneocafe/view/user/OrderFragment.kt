package com.example.clientneocafe.view.user

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientneocafe.R
import com.example.clientneocafe.adapters.AdapterMenuOrder
import com.example.clientneocafe.databinding.AlertDialogNoReorderBinding
import com.example.clientneocafe.databinding.AlertDialogReorderInfoBinding
import com.example.clientneocafe.databinding.FragmentOrderBinding
import com.example.clientneocafe.model.user.OrderDetail
import com.example.clientneocafe.model.user.ReorderInformation
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.viewModel.OrdersViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private lateinit var adapterProduct: AdapterMenuOrder
    private  val ordersViewModel: OrdersViewModel by viewModel()
    private var productId: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
        setDataOrder()
    }

    fun setDataOrder() {
        productId = arguments?.getInt("id")!!
        if (productId != null) {
            ordersViewModel.getOrderDetail(productId)
        }
        observeDetailOrder()
    }

    private fun observeDetailOrder() {
        ordersViewModel.detailOrder.observe(viewLifecycleOwner) { detailOrder ->
            when (detailOrder) {
                is Resource.Success -> {
                    detailOrder.data?.let { detailInfo ->
                        dataOrder(detailInfo)
                        setUpAdapter(detailInfo.items)
                    }
                }

                is Resource.Error -> {
                    detailOrder.message?.let {
                        Toast.makeText(
                            requireContext(),
                            "Не удалось загрузить заказ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun dataOrder(detailInfo: OrderDetail) {
        binding.textAmountBonuses.text = detailInfo.spent_bonus_points.toString()
        binding.textAmount.text = "${detailInfo.total_price.toDouble().toInt()} с"
        val inputDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd MMMM", Locale("ru", "RU"))
        val formattedDate = inputDateFormat.parse(detailInfo.created_at)
            ?.let { outputDateFormat.format(it) }
        binding.textNameBranches.text = "${detailInfo.branch_name},\n $formattedDate"
    }

    private fun setUpAdapter(items: List<OrderDetail.Item>) {
        adapterProduct = AdapterMenuOrder()
        binding.recyclerOrder.adapter = adapterProduct
        binding.recyclerOrder.layoutManager = LinearLayoutManager(requireContext())
        adapterProduct.differ.submitList(items)
    }

    private fun setUpListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigate(R.id.action_orderFragment_to_userFragment)
        }
        binding.imageBell.setOnClickListener {
            findNavController().navigate(R.id.action_orderFragment_to_notificationsFragment)
        }
        binding.btnRepeat.setOnClickListener {
            reorderInfo()
        }

    }

    private fun reorderInfo() {
        ordersViewModel.getReorderInformation(productId)
        ordersViewModel.reorderInformation.observe(viewLifecycleOwner){info->
            when (info) {
                is Resource.Success -> {
                    info.data?.let { detailInfo ->
                        dialogOrder(detailInfo)
                    }
                }

                is Resource.Error -> {
                    info.data?.let { detailInfo ->
                        dialogNoReorder(detailInfo)
                    }


                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun dialogOrder(detailInfo: ReorderInformation) {
        val dialogBinding = AlertDialogReorderInfoBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        dialogBinding.textBranch.text = detailInfo.message
        dialogBinding.textReorder.text = detailInfo.details

        dialogBinding.buttonNo.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.buttonYes.setOnClickListener {
            reorder()
            dialog.dismiss()
        }
    }

    private fun dialogNoReorder(detailInfo: ReorderInformation) {
        val dialogBinding = AlertDialogNoReorderBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        dialogBinding.textInfoReorder.text = detailInfo.message

        dialogBinding.buttonOk.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun reorder() {
        ordersViewModel.reorder(productId)
        ordersViewModel.reorder.observe(viewLifecycleOwner){reorder ->
            when (reorder) {
                is Resource.Success -> {
                    snackBar()
                    findNavController().navigateUp()
                }

                is Resource.Error -> {
                    reorder.message?.let {
                        Toast.makeText(
                            requireContext(),
                            "Не удалось оформить заказ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun snackBar() {
        val snackbar = Snackbar.make(binding.root, "", Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        val snackbarLayout = snackbarView as Snackbar.SnackbarLayout

        val layoutParams = snackbarView.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = Gravity.TOP

        snackbarLayout.setBackgroundColor(Color.TRANSPARENT)

        val customSnackbarView = layoutInflater.inflate(R.layout.snackbar_order, null)
        snackbarLayout.removeAllViews()
        snackbarLayout.addView(customSnackbarView)

        snackbar.show()
    }


}