package com.example.clientneocafe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.clientneocafe.databinding.ItemSyrupBinding
import com.example.clientneocafe.model.Syrup

class AdapterSyrup(var list: ArrayList<Syrup>): RecyclerView.Adapter<AdapterSyrup.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSyrupBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    inner class ViewHolder( var binding: ItemSyrupBinding): RecyclerView.ViewHolder(binding.root) {
    }

    private  val differCallBack = object: DiffUtil.ItemCallback<Syrup>(){
        override fun areItemsTheSame(oldItem: Syrup, newItem: Syrup): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Syrup, newItem: Syrup): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallBack)


}