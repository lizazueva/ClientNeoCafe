package com.example.clientneocafe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.clientneocafe.databinding.ItemSyrupBinding
import com.example.clientneocafe.model.Syrup

class AdapterSyrup: RecyclerView.Adapter<AdapterSyrup.ViewHolder>(){

    private var selectedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSyrupBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val syrup = differ.currentList[position]
        with(holder.binding){
            checkboxSyrup.isChecked = holder.adapterPosition == selectedPosition
            checkboxSyrup.text = syrup.title
            checkboxSyrup.setOnClickListener {
                if (holder.adapterPosition != selectedPosition) {
                    selectedPosition = holder.adapterPosition
                    notifyDataSetChanged()
                }
            }
        }
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