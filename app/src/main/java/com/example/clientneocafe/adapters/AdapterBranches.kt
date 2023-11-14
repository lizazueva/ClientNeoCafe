package com.example.clientneocafe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.clientneocafe.databinding.ItemBranchesBinding
import com.example.clientneocafe.model.Branches

class AdapterBranches: RecyclerView.Adapter<AdapterBranches.ViewHolder>() {

    private var onItemClickListener: ((Branches)-> Unit)? = null

    fun setOnClickListener(listener: (Branches)-> Unit){
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBranchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val branches = differ.currentList[position]
        with(holder.binding){

            holder.itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(branches)
                }
            }
        }
    }

    class ViewHolder(val binding: ItemBranchesBinding): RecyclerView.ViewHolder(binding.root){
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Branches>(){
        override fun areItemsTheSame(oldItem: Branches, newItem: Branches): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Branches, newItem: Branches): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
}