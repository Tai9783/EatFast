package com.example.foodorderapp.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.model.Order
import com.example.foodorderapp.utils.FormatterMoney
import java.time.format.DateTimeFormatter


class AdapterOrderDangGiao(private val list:List<Order>): RecyclerView.Adapter<AdapterOrderDangGiao.ViewHolder>() {
    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.order_item_danggiao,parent,false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.itemView.apply {
           val shopName= findViewById<TextView>(R.id.txtTenNhahang)
           val orderDate= findViewById<TextView>(R.id.txtThoigian_ngay)
           val orderTime= findViewById<TextView>(R.id.txtThoigian_gio)
           val rvMonAn= findViewById<RecyclerView>(R.id.rvMonAn)
           val totalPrice= findViewById<TextView>(R.id.txtTongTien)
           val deliveryTime= findViewById<TextView>(R.id.txtThoiGianGiaohang)
           val address= findViewById<TextView>(R.id.txtDiaDiem)
           val phoneNumber= findViewById<TextView>(R.id.txtsdt)

           shopName.text= list[position].shopName
           val dateFormatter= DateTimeFormatter.ofPattern("dd/MM/yyyy")
           orderDate.text=list[position].orderDate.format(dateFormatter)
           val timeFormatter=DateTimeFormatter.ofPattern("HH:mm")
           orderTime.text=list[position].orderTime.format(timeFormatter)
           totalPrice.text= FormatterMoney.formatterMoney(list[position].totalPrice)
           deliveryTime.text= list[position].deliveryTime.toString()+" Phút"
           address.text= list[position].address
           phoneNumber.text= list[position].phoneNumber

           rvMonAn.adapter= AdapterItemMonOrderDangGiao(list[position].listMonAn)
           rvMonAn.layoutManager= LinearLayoutManager(context)

       }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}