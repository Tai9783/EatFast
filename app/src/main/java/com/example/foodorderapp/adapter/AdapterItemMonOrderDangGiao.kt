package com.example.foodorderapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.model.FoodItemOrderDangGiao

class AdapterItemMonOrderDangGiao(private val list: List<FoodItemOrderDangGiao>): RecyclerView.Adapter<AdapterItemMonOrderDangGiao.ViewHolder>()  {
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.order_layout_monan_danggiao,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.itemView.apply {
           val tenmon= findViewById<TextView>(R.id.txtTenmon)
           val giamon= findViewById<TextView>(R.id.txtgiamon)

           tenmon.text= list[position].tenmon
           giamon.text= list[position].gia.toString()+"đ"
       }
    }

    override fun getItemCount(): Int {
       return list.size
    }
}