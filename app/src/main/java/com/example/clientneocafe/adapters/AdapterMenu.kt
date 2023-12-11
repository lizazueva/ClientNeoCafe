package com.example.clientneocafe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clientneocafe.databinding.ItemMenuBinding
import com.example.clientneocafe.model.DetailInfoProduct
import com.example.clientneocafe.model.Product
import com.example.clientneocafe.utils.CartItem
import com.example.clientneocafe.utils.CartUtils

class AdapterMenu: RecyclerView.Adapter<AdapterMenu.ViewHolder>() {

    var onItemClickListener: ListClickListener<DetailInfoProduct>? = null

    fun setOnItemClick(listClickListener: AdapterMenu.ListClickListener<DetailInfoProduct>){
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

            textTitle.text = product.name
            val productAmount = product.price.toString()
            textAmount.text = "${productAmount.toDouble().toInt()} c"
            textDiscr.text = product.category.name
            Glide.with(imageProduct).load(product.image).into(imageProduct)

            layoutProduct.setOnClickListener {
                onItemClickListener?.onClick(product, position)
            }

                if (CartUtils.isInCart(product.id)) {
                    val quantity = CartUtils.getQuantity(product.id)
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
                notifyItemChanged(position, product)
            }
            imageAdd.setOnClickListener {
                onItemClickListener?.onAddClick(product, position)
                notifyItemChanged(position, product)
            }
        }
    }

    inner class ViewHolder (var binding: ItemMenuBinding): RecyclerView.ViewHolder(binding.root) {

    }

    interface ListClickListener<T>{
        fun onClick(data:T, position: Int)
        fun onAddClick(data:T, position: Int)
        fun onRemoveClick(data:T, position: Int)
    }

    private val differCallBack = object: DiffUtil.ItemCallback<DetailInfoProduct>(){
        override fun areItemsTheSame(oldItem: DetailInfoProduct, newItem: DetailInfoProduct): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DetailInfoProduct, newItem: DetailInfoProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    fun removeItem(position: Int) {
        val newList = ArrayList(differ.currentList)
        newList.removeAt(position)
        differ.submitList(newList)
        notifyItemRemoved(position)
    }
}