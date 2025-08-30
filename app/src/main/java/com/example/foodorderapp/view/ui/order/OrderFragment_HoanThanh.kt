package com.example.foodorderapp.view.ui.order

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.adapter.AdapterItemOrderHoanThanh
import com.example.foodorderapp.model.FoodItemOrderDangGiao
import com.example.foodorderapp.model.OrderHoanThanh
import java.time.LocalDate
import java.time.LocalTime


class OrderFragment_HoanThanh : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order__hoan_thanh, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rvHoanThanh= view.findViewById<RecyclerView>(R.id.rvHoanThanh)
        val list= mutableListOf<OrderHoanThanh>()
        showHoanThanh(list,rvHoanThanh)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showHoanThanh(list: MutableList<OrderHoanThanh>, rvHoanThanh: RecyclerView) {
        val listDsMon= listOf(FoodItemOrderDangGiao("1 Heo rừng cuồng nhiệt",200000),FoodItemOrderDangGiao("1 Pizza hải sản",105000))
        var tongtien=0
        for(i in listDsMon){
            tongtien+= i.gia
        }
        val listDsMon2= listOf(FoodItemOrderDangGiao("1 Heo rừng cuồng nhiệt",200000),FoodItemOrderDangGiao("1 Pizza hải sản",105000))
        var tongtien2=0
        for(i in listDsMon){
            tongtien2+= i.gia
        }
        list.add(OrderHoanThanh("Phương Nam", LocalDate.now(), LocalTime.now(),listDsMon,tongtien))
        list.add(OrderHoanThanh("Quá Ngon", LocalDate.now(), LocalTime.now(),listDsMon2,tongtien2))
        rvHoanThanh.adapter=AdapterItemOrderHoanThanh(list)
        rvHoanThanh.layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        rvHoanThanh.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom= 40
            }
        })

    }


}