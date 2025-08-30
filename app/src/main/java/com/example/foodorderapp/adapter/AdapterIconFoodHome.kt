package com.example.foodorderapp.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.interfaces.OnItemClickListener_IconMonAn_Home
import com.example.foodorderapp.model.IconFood


class AdapterIconFoodHome(private val list: List<IconFood>, private val onItemClickListener: OnItemClickListener_IconMonAn_Home)
    :RecyclerView.Adapter<AdapterIconFoodHome.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            init {
                itemView.setOnClickListener{
                    val position= adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        onItemClickListener.onItemClick(position)
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view= LayoutInflater.from(parent.context).inflate(R.layout.home_item_icon_food,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val img= findViewById<ImageView>(R.id.img_IconFood_home)
            val title= findViewById<TextView>(R.id.txtFood_IconFood_home)

            img.setImageResource(list[position].image)
            title.text=list[position].title
        }

    }

    override fun getItemCount(): Int {
       return list.size
    }
}