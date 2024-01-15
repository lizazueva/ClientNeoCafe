package com.neobis.clientneocafe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neobis.clientneocafe.databinding.ItemMenuBinding
import com.neobis.clientneocafe.model.home.SearchResultResponse
import com.neobis.clientneocafe.utils.CartUtils

class AdapterSearch : RecyclerView.Adapter<AdapterSearch.ViewHolder>() {

    var onItemClickListener: ListClickListener<SearchResultResponse>? = null

    fun setOnItemClick(listClickListener: ListClickListener<SearchResultResponse>){
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
            textAmount.text = "$productAmount c"
            textDiscr.text = product.category_name
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
        fun onClick(data:T, position: Int)
        fun onAddClick(data:T, position: Int)
        fun onRemoveClick(data:T, position: Int)
    }

    private val differCallBack = object: DiffUtil.ItemCallback<SearchResultResponse>(){
        override fun areItemsTheSame(oldItem: SearchResultResponse, newItem: SearchResultResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchResultResponse, newItem: SearchResultResponse): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
}