package com.example.clientneocafe.view.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientneocafe.R
import com.example.clientneocafe.adapters.AdapterMenu
import com.example.clientneocafe.databinding.FragmentOrderBinding
import com.example.clientneocafe.model.Product
import com.example.clientneocafe.model.user.OrderDetail
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.viewModel.OrdersViewModel
import com.example.clientneocafe.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private lateinit var adapterProduct: AdapterMenu
    private  val ordersViewModel: OrdersViewModel by viewModel()


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
        setUpAdapter()
        setDataOrder()
    }

    fun setDataOrder() {
        val productId = arguments?.getInt("id")
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

    private fun setUpAdapter() {
        adapterProduct =AdapterMenu()
        binding.recyclerOrder.adapter = adapterProduct
        binding.recyclerOrder.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUpListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigate(R.id.action_orderFragment_to_userFragment)
        }
        binding.imageBell.setOnClickListener {
            findNavController().navigate(R.id.action_orderFragment_to_notificationsFragment)
        }

    }

}