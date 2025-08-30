package com.example.foodorderapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapp.R
import com.example.foodorderapp.diffcallback.DiffCallBackFoodIcon
import com.example.foodorderapp.model.ItemFoodIcon
import com.example.foodorderapp.utils.FormatterMoney

class AdapterItemFoodIcon(): ListAdapter<ItemFoodIcon,AdapterItemFoodIcon.ViewHolder>(DiffCallBackFoodIcon()) {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_monan,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= getItem(position)
        holder.itemView.apply {
            val nameFood= findViewById<TextView>(R.id.txtTenMonAn)
            val  rating= findViewById<TextView>(R.id.home_kq)
            val nameShop= findViewById<TextView>(R.id.txtTenQuanAn)
            val  price= findViewById<TextView>(R.id.txtGiaMonAn)
            val imageFoodUrl= findViewById<ImageView>(R.id.imgAnhMonAn)
            val star1= findViewById<ImageView>(R.id.star1)
            val star2= findViewById<ImageView>(R.id.star2)
            val star3= findViewById<ImageView>(R.id.star3)
            val star4= findViewById<ImageView>(R.id.star4)
            val star5= findViewById<ImageView>(R.id.star5)

            nameFood.text= item.nameFood
            rating.text= item.rating.toString()
            nameShop.text= item.nameShop
            price.text= FormatterMoney.formatterMoney(item.price)

            Glide.with(this)
                .load(item.imageFoodUrl)
                .placeholder(R.drawable.home_logo_nhahang_default)
                .error(R.drawable.home_logo_nhahang_default)
                .into(imageFoodUrl)

            val sosao: Int= item.rating.toInt()
            val star= listOf(star1,star2,star3,star4,star5)
            for (i in star.indices)
            {
                if (i<sosao)
                    star[i].setImageResource(R.drawable.home_ngoisao_danhgia1)
                else
                    star[i].setImageResource(R.drawable.home_ngoisao_danhgia_emty)
            }

        }
    }
}