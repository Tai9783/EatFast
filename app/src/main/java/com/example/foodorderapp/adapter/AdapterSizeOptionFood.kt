package com.example.foodorderapp.adapter

import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.diffcallback.DiffCallBackSizeOptionFood
import com.example.foodorderapp.model.SizeOptionFood
import com.example.foodorderapp.utils.FormatterMoney
import com.example.foodorderapp.viewmodel.ViewModelCustom

class AdapterSizeOptionFood(private val viewModelCustom: ViewModelCustom)  :
    ListAdapter<SizeOptionFood, AdapterSizeOptionFood.ViewHolder>(DiffCallBackSizeOptionFood()) {
        private var selectedPostion=-1
        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            private val radioButton: RadioButton = itemView.findViewById(R.id.rdbSize)
            val price: TextView = itemView.findViewById(R.id.txtPrice)
            fun bin(item: SizeOptionFood,position: Int){
                radioButton.text=item.nameSize
                price.text="+"+FormatterMoney.formatterMoney(item.price)
                radioButton.isChecked=item.isCheck

                radioButton.setOnClickListener {
                    val updateList= currentList.mapIndexed() { index,sizeOption->
                        sizeOption.copy(isCheck=index==position)
                    }
                    submitList(updateList)
                    selectedPostion=position
                    viewModelCustom.updateGiaSize(item.price)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_item_radio_size_mon_an,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item= getItem(position)
        holder.bin(item,position)
    }
    fun getSelectedSize():SizeOptionFood?{
        return currentList.find { it.isCheck }
    }

}