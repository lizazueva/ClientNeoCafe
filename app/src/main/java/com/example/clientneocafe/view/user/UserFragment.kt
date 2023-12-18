package com.example.clientneocafe.view.user

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientneocafe.adapters.AdapterOrder
import com.example.clientneocafe.adapters.AdapterOrderClosed
import com.example.clientneocafe.databinding.AlertDialogExitBinding
import com.example.clientneocafe.databinding.AlertDialogInfoBonusesBinding
import com.example.clientneocafe.databinding.FragmentUserBinding
import com.example.clientneocafe.model.user.Orders
import com.example.clientneocafe.model.user.UserInfo
import com.example.clientneocafe.utils.Resource
import com.example.clientneocafe.view.login.LoginActivity
import com.example.clientneocafe.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private  val userViewModel: UserViewModel by viewModel()
    private lateinit var profile: UserInfo
    private lateinit var adapterOrder: AdapterOrder
    private lateinit var adapterOrderClosed: AdapterOrderClosed



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
        dataProfile()
        dataOrders()

    }

    private fun dataOrders() {
        userViewModel.getMyOrders()
        userViewModel.myOrders.observe(viewLifecycleOwner){orders ->
            when (orders) {
                is Resource.Success -> {
                    orders.data?.let { orders ->
                        setUpAdapter(orders.opened_orders)
                        setUpAdapterClosed(orders.closed_orders)
                    }
                }
                is Resource.Loading ->{

                }
                is Resource.Error ->{

                }
            }
        }

    }

    private fun setUpAdapterClosed(closedOrders: List<Orders.ClosedOrder>) {
        adapterOrderClosed = AdapterOrderClosed()
        binding.recyclerClosedOrder.adapter = adapterOrderClosed
        binding.recyclerClosedOrder.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerClosedOrder.layoutManager = LinearLayoutManager(requireContext())
        if (closedOrders.isEmpty()){
            binding.textClosedOrder.visibility = View.GONE
        }else{
            binding.textClosedOrder.visibility = View.VISIBLE

        }
        adapterOrderClosed.differ.submitList(closedOrders)
        adapterOrderClosed.setOnClickListener { closedOrder ->
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToOrderFragment(closedOrder.id))
        }
    }

    private fun setUpAdapter(orders: List<Orders.OpenedOrder>) {
        adapterOrder = AdapterOrder()
        binding.recyclerOpenOrder.adapter = adapterOrder
        binding.recyclerOpenOrder.layoutManager = LinearLayoutManager(requireContext())
        if (orders.isEmpty()){
            binding.textActualOrder.visibility = View.GONE
        }else{
            binding.textActualOrder.visibility = View.VISIBLE
        }
        adapterOrder.differ.submitList(orders)
        adapterOrder.setOnClickListener { order ->
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToOrderFragment(order.id))
        }
    }

     private fun dataProfile() {
        userViewModel.getProfile()
        userViewModel.user.observe(viewLifecycleOwner){user ->
            when (user) {
                is Resource.Success -> {
                    binding.textUserName.text = user.data?.first_name
                    binding.textOurBonuses.text = user.data?.bonus.toString()
                    user.data?.let { profile = it }
                }
                is Resource.Loading ->{

                }
                is Resource.Error ->{

                }
            }
        }
    }

    private fun setUpListeners() {
        binding.imageEditProfile.setOnClickListener {
            profile?.let {
                val action = UserFragmentDirections.actionUserFragmentToEditProfileFragment(it)
                findNavController().navigate(action)
            }
        }
        binding.cardForBonuses.setOnClickListener {
            showDialogBonuses()
        }
        binding.icnSignOut.setOnClickListener {
            showDialogExit()
        }
    }

    private fun showDialogExit() {
        val dialogBinding = AlertDialogExitBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        dialogBinding.buttonNo.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.buttonYes.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showDialogBonuses() {
        val dialogBinding = AlertDialogInfoBonusesBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        dialogBinding.buttonOk.setOnClickListener {
            dialog.dismiss()
        }
    }
}