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
import com.example.foodorderapp.adapter.AdapterItemOrderDahuy
import com.example.foodorderapp.model.FoodItemOrderDangGiao
import com.example.foodorderapp.model.Order
import java.time.LocalDate
import java.time.LocalTime

class OrderCanceledFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order__da_huy, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rvDahuy= view.findViewById<RecyclerView>(R.id.rvDahuy )
        val list= mutableListOf<Order>()
        showDaHuy(list,rvDahuy)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDaHuy(list: MutableList<Order>, rvDahuy: RecyclerView) {
        val listDsMon= listOf(FoodItemOrderDangGiao("1 Heo rừng cuồng nhiệt",200000),FoodItemOrderDangGiao("1 Pizza hải sản",105000))
        var tongtien=0
        for(i in listDsMon){
            tongtien+= i.gia
        }
        val listDsMon2= listOf(FoodItemOrderDangGiao("1 Heo rừng cuồng nhiệt",500000),FoodItemOrderDangGiao("1 Pizza hải sản",105000))
        var tongtien2=0
        for(i in listDsMon2){
            tongtien2+= i.gia
        }
        list.add(Order(shopName ="Phương Nam", orderDate =  LocalDate.now(), orderTime =  LocalTime.now(), listMonAn =  listDsMon, cancleReason =  "Sai địa chỉ",totalPrice = tongtien))
        list.add(Order(shopName ="Phương Nam", orderDate =  LocalDate.now(), orderTime =  LocalTime.now(), listMonAn =  listDsMon2, cancleReason =  "Sai địa chỉ",totalPrice = tongtien))
        list.add(Order(shopName ="Phương Nam", orderDate =  LocalDate.now(), orderTime =  LocalTime.now(), listMonAn =  listDsMon, cancleReason =  "Sai địa chỉ",totalPrice = tongtien))
        list.add(Order(shopName ="Phương Nam", orderDate =  LocalDate.now(), orderTime =  LocalTime.now(), listMonAn =  listDsMon, cancleReason =  "Sai địa chỉ",totalPrice = tongtien))
        list.add(Order(shopName ="Phương Nam", orderDate =  LocalDate.now(), orderTime =  LocalTime.now(), listMonAn =  listDsMon, cancleReason =  "Sai địa chỉ",totalPrice = tongtien))

        rvDahuy.adapter=AdapterItemOrderDahuy(list)
        rvDahuy.layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        rvDahuy.addItemDecoration(object : RecyclerView.ItemDecoration(){
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