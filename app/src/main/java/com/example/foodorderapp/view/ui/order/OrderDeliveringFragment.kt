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
import com.example.foodorderapp.adapter.AdapterOrderDangGiao
import com.example.foodorderapp.model.FoodItemOrderDangGiao
import com.example.foodorderapp.model.Order
import java.time.LocalDate
import java.time.LocalTime


class OrderDeliveringFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order__dang_giao, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rvOrderDangGiao= view.findViewById<RecyclerView>(R.id.rvOrderDangGiao)
        val list= mutableListOf<Order>( )
        showOrderDangGiao(list,rvOrderDangGiao)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showOrderDangGiao(list: MutableList<Order>, rvOrderDangGiao: RecyclerView) {
        val listItemMonAn= listOf(FoodItemOrderDangGiao("1 Bánh burger thịt bò đặt biệt",89000),
            FoodItemOrderDangGiao("1 khoai tây chiên(size L)",35000))
        var tongtien1: Int=0
        for (i in listItemMonAn)
        {
            tongtien1+= i.gia
        }
        val listItemMonAn2= listOf(FoodItemOrderDangGiao("1 Gà vàng rộn ràng",180000),
            FoodItemOrderDangGiao("Bò hảo hạn",120000))
        var tongtien2: Int=0
        for (i in listItemMonAn2)
        {
            tongtien2+= i.gia
        }
        list.add(
            Order(shopName = "Cô Ba Sài Gòn", orderDate =  LocalDate.now(), orderTime =  LocalTime.now(), listMonAn =  listItemMonAn, totalPrice =  tongtien1,
                deliveryTime = 25,address= "123 Nguyễn Văn Linh, Quận 7, TP HCM", phoneNumber ="0397589783")

        )
        list.add(
            Order(shopName = "Cô Ba Sài Gòn", orderDate =  LocalDate.now(), orderTime =  LocalTime.now(), listMonAn =  listItemMonAn, totalPrice =  tongtien1,
                deliveryTime = 25,address= "123 Nguyễn Văn Linh, Quận 7, TP HCM", phoneNumber ="0397589783")

        )
        list.add(
            Order(shopName = "Cô Ba Sài Gòn", orderDate =  LocalDate.now(), orderTime =  LocalTime.now(), listMonAn =  listItemMonAn, totalPrice =  tongtien1,
                deliveryTime = 25,address= "123 Nguyễn Văn Linh, Quận 7, TP HCM", phoneNumber ="0397589783")

        )

        rvOrderDangGiao.adapter= AdapterOrderDangGiao(list)
        rvOrderDangGiao.layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        rvOrderDangGiao.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom=40
            }

        })
    }


}