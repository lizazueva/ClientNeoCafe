package com.example.clientneocafe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clientneocafe.databinding.ItemMenuBinding
import com.example.clientneocafe.model.Product

class AdapterMenu: RecyclerView.Adapter<AdapterMenu.ViewHolder>() {

    var onItemClickListener: ListClickListener<Product>? = null

    fun setOnItemClick(listClickListener: ListClickListener<Product>){
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

            textTitle.text = product.title
            val productAmount = product.amount.toString()
            textAmount.text = "$productAmount c"
            textDiscr.text = product.discr
            Glide.with(imageProduct).load(product.image).into(imageProduct)

            layoutProduct.setOnClickListener {
                onItemClickListener?.onClick(product, position)
            }
            imageRemove.setOnClickListener {
                onItemClickListener?.onRemoveClick(product, position)
                product.county -=1
                notifyItemChanged(position)
            }
            imageAdd.setOnClickListener {
                onItemClickListener?.onAddClick(product, position)
                product.county +=1
                notifyItemChanged(position)
            }

            if (product.county>0){
                textCount.visibility = View.VISIBLE
                textCount.text = product.county.toString()
                imageRemove.visibility = View.VISIBLE
            } else{
                textCount.visibility= View.INVISIBLE
                imageRemove.visibility= View.INVISIBLE
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

    private val differCallBack = object: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
}