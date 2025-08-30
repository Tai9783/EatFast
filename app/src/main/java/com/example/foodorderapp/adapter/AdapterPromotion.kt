package com.example.foodorderapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.diffcallback.DiffCallBackPromotion
import com.example.foodorderapp.model.Promotion
import com.example.foodorderapp.viewmodel.ViewModelPromotion

class AdapterPromotion(private val viewModelPromotion: ViewModelPromotion):ListAdapter<Promotion,AdapterPromotion.ViewHolder>(DiffCallBackPromotion()) {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.home_item_promotion,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item= getItem(position)
        holder.itemView.apply {
            val giamPhanTram= findViewById<TextView>(R.id.txtTitle)
            val thoiGianGiam= findViewById<TextView>(R.id.txtThoiGianKhuyenMai)
            val tenMonAn= findViewById<TextView>(R.id.txtMonAn)
            val anhMonAn= findViewById<ImageView>(R.id.imgMonAn)

            giamPhanTram.text= "GIẢM "+ item.phanTramGiam.toString()+"%"
            thoiGianGiam.text= item.thoiGian.uppercase()
            tenMonAn.text= item.tenMonAn
            anhMonAn.setImageResource(item.imageResId)
        }
    }
}