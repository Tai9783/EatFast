package com.example.foodorderapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapp.R
import com.example.foodorderapp.diffcallback.DiffCallBackItemMonAnShop
import com.example.foodorderapp.interfaces.OnClickItemFoodShop
import com.example.foodorderapp.model.ItemMonAnSeller
import com.example.foodorderapp.utils.FormatterMoney

class AdapterItemFoodShop(private val onClick: OnClickItemFoodShop): ListAdapter<ItemMonAnSeller, AdapterItemFoodShop.ViewHolder>(DiffCallBackItemMonAnShop()){

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val btnAddCart: ImageView= itemView.findViewById(R.id.imgGioHang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_item_monan_quan_an,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= getItem(position)
        Log.d("AdapterItemFoodShop", "Bind món ăn: ${item.nameFood}")
        holder.itemView.apply {
            val nameFood= findViewById<TextView>(R.id.txtTenMonAn)
            val quantitySold= findViewById<TextView>(R.id.txtSoLuotMua)
            val price= findViewById<TextView>(R.id.txtGiaMonAn)
            val imageFood= findViewById<ImageView>(R.id.imgAnhMonAn)

            nameFood.text= item.nameFood
            quantitySold.text= item.quantitySold.toString()+"+"
            price.text= FormatterMoney.formatterMoney(item.price)
           Glide.with(context)
               .load(item.imageFoodUrl)
               .placeholder(R.drawable.home_logo_nhahang_default)
               .error(R.drawable.home_logo_nhahang_default)
               .into(imageFood)
        }
        holder.btnAddCart.setOnClickListener{
            onClick.onClickAddCart(item.food_id)
        }
    }
}