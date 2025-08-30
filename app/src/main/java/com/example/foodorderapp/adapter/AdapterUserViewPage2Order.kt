package com.example.foodorderapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.foodorderapp.view.ui.order.OrderFragment_DaHuy
import com.example.foodorderapp.view.ui.order.OrderFragment_DangGiao
import com.example.foodorderapp.view.ui.order.OrderFragment_HoanThanh

class AdapterUserViewPage2Order(fragmentManager: FragmentManager, lifecycle: Lifecycle):
            FragmentStateAdapter(fragmentManager,lifecycle){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
           0->{OrderFragment_DangGiao()}
           1->{OrderFragment_HoanThanh()}
           else->{OrderFragment_DaHuy()}
       }
    }
}