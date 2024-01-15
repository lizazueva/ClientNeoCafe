package com.neobis.clientneocafe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neobis.clientneocafe.databinding.ItemMenuBinding
import com.neobis.clientneocafe.model.user.OrderDetail

class AdapterMenuOrder: RecyclerView.Adapter<AdapterMenuOrder.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return differ.currentList.size

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = differ.currentList[position]
        with(holder.binding){

            textTitle.text = product.item_name
            val productAmount = product.item_price.toString()
            textAmount.text = "${productAmount.toDouble().toInt()} c"
            textDiscr.text = product.item_category
            Glide.with(imageProduct).load(product.item_image).into(imageProduct)

            textCount.text = product.quantity.toString()
            textCount.visibility = View.VISIBLE
            imageRemove.visibility = View.VISIBLE
            imageAdd.isEnabled = false
            imageRemove.isEnabled = false
            }
        }

    inner class ViewHolder (var binding: ItemMenuBinding): RecyclerView.ViewHolder(binding.root) {
    }


    private val differCallBack = object: DiffUtil.ItemCallback<OrderDetail.Item>(){
        override fun areItemsTheSame(oldItem: OrderDetail.Item, newItem: OrderDetail.Item): Boolean {
            return oldItem.item_id == newItem.item_id
        }

        override fun areContentsTheSame(oldItem: OrderDetail.Item, newItem: OrderDetail.Item): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

}