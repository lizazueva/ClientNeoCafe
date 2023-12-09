package com.example.clientneocafe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clientneocafe.databinding.ItemMenuBinding
import com.example.clientneocafe.utils.CartItem
import com.example.clientneocafe.utils.CartUtils

class AdapterCart: RecyclerView.Adapter<AdapterCart.ViewHolder>() {

    var onItemClickListener: ListClickListener<CartItem>? = null

    fun setOnItemClick(listClickListener: ListClickListener<CartItem>){
        this.onItemClickListener = listClickListener
    }

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

            textTitle.text = product.productName
            val productAmount = product.productPrice.toString()
            textAmount.text = "${productAmount.toDouble().toInt()} c"
            textDiscr.text = product.productCategory
            Glide.with(imageProduct).load(product.image).into(imageProduct)

            if (CartUtils.isInCart(product.productId)) {
                val quantity = CartUtils.getQuantity(product.productId)
                imageAdd.isEnabled = quantity <9
                textCount.visibility = View.VISIBLE
                textCount.text = quantity.toString()
                imageRemove.visibility = View.VISIBLE
            } else {
                textCount.visibility = View.INVISIBLE
                imageRemove.visibility = View.INVISIBLE
            }




            imageRemove.setOnClickListener {
                onItemClickListener?.onRemoveClick(product, position)
                notifyItemChanged(position)
            }
            imageAdd.setOnClickListener {
                onItemClickListener?.onAddClick(product, position)
                notifyItemChanged(position)
            }


        }

    }

    inner class ViewHolder (var binding: ItemMenuBinding): RecyclerView.ViewHolder(binding.root) {

    }

    interface ListClickListener<T>{
        fun onAddClick(data:T, position: Int)
        fun onRemoveClick(data:T, position: Int)
    }

    private val differCallBack = object: DiffUtil.ItemCallback<CartItem>(){
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
}