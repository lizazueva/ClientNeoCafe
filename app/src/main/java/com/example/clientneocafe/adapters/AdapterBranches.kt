package com.example.clientneocafe.adapters

import android.graphics.Color
import android.graphics.LightingColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.clientneocafe.databinding.ItemBranchesBinding
import com.example.clientneocafe.model.map.Branches
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

            val calendar = Calendar.getInstance()
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            val currentWorkday = branches.workdays.find { it.workday == dayOfWeek }
            val workingHours = currentWorkday?.let {
                val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val startTime = inputFormat.parse(it.start_time)
                val endTime = inputFormat.parse(it.end_time)
                "Сегодня: с ${outputFormat.format(startTime)} до ${outputFormat.format(endTime)}"
            } ?: "Сегодня ресторан не работает"

            textTimeBranches.text = workingHours
            textNameBranches.text = branches.name_of_shop
            textAddress.text = branches.address
            textPhone.text = branches.phone_number

            val colorFilter = LightingColorFilter(Color.rgb(153, 143, 166), 0)
            imageBranches.colorFilter = colorFilter

            Glide.with(imageBranches).load(branches.image).into(imageBranches)
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