package com.example.clientneocafe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clientneocafe.databinding.ItemOrderBinding
import com.example.clientneocafe.model.user.Orders
import java.text.SimpleDateFormat
import java.util.Locale

class AdapterOrder: RecyclerView.Adapter<AdapterOrder.ViewHolder> (){

    private var onItemClickListener: ((Orders.OpenedOrder)-> Unit)? = null

    fun setOnClickListener(listener: (Orders.OpenedOrder)-> Unit){
        onItemClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = differ.currentList[position]
        with(holder.binding){
            Glide.with(imageOrder).load(order.branch__image).into(imageOrder)
            val inputDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("dd MMMM", Locale("ru", "RU"))
            val formattedDate = outputDateFormat.format(inputDateFormat.parse(order.created_at))
            textTime.text = formattedDate
            textBranches.text = order.branch_name
            textDiscr.text = order.order_items

            holder.itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(order)
                }
            }

        }
    }

    inner class ViewHolder (var binding: ItemOrderBinding): RecyclerView.ViewHolder(binding.root){
    }

    private val differCallBack = object: DiffUtil.ItemCallback<Orders.OpenedOrder>(){
        override fun areItemsTheSame(oldItem: Orders.OpenedOrder, newItem: Orders.OpenedOrder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Orders.OpenedOrder, newItem: Orders.OpenedOrder): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

}