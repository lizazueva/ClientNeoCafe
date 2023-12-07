package com.example.clientneocafe.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.clientneocafe.model.home.Category
import com.example.clientneocafe.view.home.CategoryFragment


class SliderAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val categories: List<Category>) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return categories.size
    }

    override fun createFragment(position: Int): Fragment {
        val category = categories[position]
        return CategoryFragment.newInstance(category.id)
    }
}