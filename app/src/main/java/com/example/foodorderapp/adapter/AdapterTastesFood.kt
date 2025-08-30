package com.example.foodorderapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.diffcallback.DiffCallBackTaste
import com.example.foodorderapp.model.TasteOptions

class AdapterTastesFood(): ListAdapter<TasteOptions,AdapterTastesFood.ViewHolder>(DiffCallBackTaste()) {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val name= itemView.findViewById<TextView>(R.id.txtKhauVi)

        fun bin(item: TasteOptions){
            name.text= item.nameTaste

            if(item.ischeck) {
                name.setBackgroundResource(R.drawable.bg_selected_option)
                name.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))

            }
            else {
                name.setBackgroundResource(R.drawable.bg_tuychon_khauvi)
                name.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))

            }

            itemView.setOnClickListener {
                val clickedPosition = adapterPosition

                if (clickedPosition != RecyclerView.NO_POSITION) {
                    val updatedList = currentList.mapIndexed { index, taste ->
                        taste.copy(ischeck = index == clickedPosition)
                    }
                    submitList(updatedList)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_item_tuychon_khauvi,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= getItem(position)
        holder.bin(item)

    }
    fun getSelectTastes(): TasteOptions?{
        return currentList.find { it.ischeck }
    }

}