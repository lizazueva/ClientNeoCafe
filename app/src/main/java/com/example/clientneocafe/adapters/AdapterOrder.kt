package com.example.clientneocafe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.clientneocafe.databinding.ItemOrderBinding
import com.example.clientneocafe.model.Order

class AdapterOrder: RecyclerView.Adapter<AdapterOrder.ViewHolder> (){

    private var onItemClickListener: ((Order)-> Unit)? = null

    fun setOnClickListener(listener: (Order)-> Unit){
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

            holder.itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(order)
                }
            }

        }
    }

    inner class ViewHolder (var binding: ItemOrderBinding): RecyclerView.ViewHolder(binding.root){
    }

    private val differCallBack = object: DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

}