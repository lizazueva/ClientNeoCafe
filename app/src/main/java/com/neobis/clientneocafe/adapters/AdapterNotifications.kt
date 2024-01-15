package com.neobis.clientneocafe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.neobis.clientneocafe.databinding.ItemNotificationsBinding
import com.neobis.clientneocafe.model.NotificationsResponse

class AdapterNotifications: RecyclerView.Adapter<AdapterNotifications.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var notifications = differ.currentList[position]
        with(holder.binding){
            textTitleNotification.text = notifications.title
            textDiscrNotification.text= notifications.body
            textTimeNotification.text = notifications.exactly_time

        }
    }

    inner class ViewHolder ( var binding: ItemNotificationsBinding): RecyclerView.ViewHolder(binding.root)  {
    }

    private val differCallBack = object: DiffUtil.ItemCallback<NotificationsResponse.Notifications>(){
        override fun areItemsTheSame(oldItem: NotificationsResponse.Notifications, newItem: NotificationsResponse.Notifications): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NotificationsResponse.Notifications, newItem: NotificationsResponse.Notifications): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    fun removeItem(position: Int) {
        val newList = ArrayList(differ.currentList)
        newList.removeAt(position)
        differ.submitList(newList)
    }
}