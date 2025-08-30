package com.example.foodorderapp.view.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.foodorderapp.R
import com.example.foodorderapp.adapter.AdapterUserViewPage2Order
import com.example.foodorderapp.utils.applySystemBarPadding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrderFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.applySystemBarPadding(applyTop = true, applyBottomNav  = true)
        val orderTab = view.findViewById<TabLayout>(R.id.order_Tab)
        val orderView= view.findViewById<ViewPager2>(R.id.order_View)
        orderView?.adapter= AdapterUserViewPage2Order(childFragmentManager,lifecycle)
        TabLayoutMediator(orderTab,orderView){tab,pos->
                when(pos){
                    0->{tab.text="Đang giao"}
                    1->{tab.text="Hoàn thành"}
                    2->{tab.text="Đã hủy"}
                }
        }.attach()
    }

}