package com.example.foodorderapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapp.R
import com.example.foodorderapp.interfaces.OnItemClickListenerItemShop
import com.example.foodorderapp.model.RestaurantItem

class AdapterItemNhahangHome(private var list: MutableList<RestaurantItem>,private val onItemClickListener: OnItemClickListenerItemShop): RecyclerView.Adapter<AdapterItemNhahangHome.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

                fun onClickItem(shopItem: RestaurantItem) {
                    itemView.setOnClickListener{
                        onItemClickListener.onclick(shopItem)
                    }

                }


    }
    fun upadte(newlist: List<RestaurantItem>){
        list.clear()
        list.addAll(newlist)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_nhahang,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val logoImage= findViewById<ImageView>(R.id.logoNhahang)
            val nameNhahang=findViewById<TextView>(R.id.txtTenNhahang)
            val star1= findViewById<ImageView>(R.id.star1)
            val star2= findViewById<ImageView>(R.id.star2)
            val star3= findViewById<ImageView>(R.id.star3)
            val star4= findViewById<ImageView>(R.id.star4)
            val star5= findViewById<ImageView>(R.id.star5)
            val ratingScore= findViewById<TextView>(R.id.home_kq)
            val addressNhahang= findViewById<TextView>(R.id.txtTendiachi)


            Glide.with(context)
                .load(list[position].logoResId)
                .placeholder(R.drawable.home_logo_nhahang_default)
                .error(R.drawable.home_logo_nhahang_default)
                .into(logoImage)

            nameNhahang.text= list[position].name
            ratingScore.text = list[position].ratingScore.toString()
            val stars= listOf(star1,star2,star3,star4,star5)
            val sosao:Int=list[position].ratingScore.toInt()
            for(i in stars.indices) {
                if (i < sosao)
                    stars[i].setImageResource(R.drawable.home_ngoisao_danhgia1)
                else
                    stars[i].setImageResource(R.drawable.home_ngoisao_danhgia_emty)
            }
            addressNhahang.text= list[position].address
        }

        holder.onClickItem(list[position])

    }

    override fun getItemCount(): Int = list.size

}