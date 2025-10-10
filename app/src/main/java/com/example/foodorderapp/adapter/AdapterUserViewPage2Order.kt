package com.example.foodorderapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.foodorderapp.view.ui.order.OrderCanceledFragment
import com.example.foodorderapp.view.ui.order.OrderDeliveringFragment
import com.example.foodorderapp.view.ui.order.OrderCompletedFragment
import com.example.foodorderapp.view.ui.order.OrderPendingFragment

class AdapterUserViewPage2Order(fragmentManager: FragmentManager, lifecycle: Lifecycle):
            FragmentStateAdapter(fragmentManager,lifecycle){
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
           0->{OrderPendingFragment()}
           1->{OrderDeliveringFragment()}
           2->{OrderCompletedFragment()}
           else->{OrderCanceledFragment()}
       }
    }
}