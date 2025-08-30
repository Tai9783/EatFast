package com.example.foodorderapp.adapter

import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapp.R
import com.example.foodorderapp.diffcallback.DiffCallBackItemMonAnShoppingCart
import com.example.foodorderapp.model.FoodItemCart


import com.example.foodorderapp.utils.FormatterMoney
import com.example.foodorderapp.viewmodel.ViewModelShoppingcart


class AdapterItemMonAnShoppingCart(private val viewModelShoppingcart: ViewModelShoppingcart)
            :ListAdapter<FoodItemCart,AdapterItemMonAnShoppingCart.ViewHolder>(
    DiffCallBackItemMonAnShoppingCart()) {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.shoppingcart_item_monan,parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= getItem(position)

        holder.itemView.apply {
            val anhNhaHang= findViewById<ImageView>(R.id.imgMonAn)
            val tenMonAn= findViewById<TextView>(R.id.txtTenMonAn)
            val tenNhaHang= findViewById<TextView>(R.id.txtTenNhahang)
            val ghiChu= findViewById<TextView>(R.id.txtSizeMonAn)
            val demSoLuong= findViewById<TextView>(R.id.txtDemSoLuong)
            val giaMonAn= findViewById<TextView>(R.id.txtGiaMonAn)
            val checkBox= findViewById<CheckBox>(R.id.checkbox)
            val iconXoaMonAn= findViewById<ImageView>(R.id.imgXoaMonAn)
            val giamSoLuong= findViewById<ImageView>(R.id.imgGiamSoLuong)
            val tangSoLuong= findViewById<ImageView>(R.id.imgTangSoLuong)

            Glide.with(this)
                .load(item.anhMonAn)
                .error(R.drawable.home_logo_nhahang_default)
                .into(anhNhaHang)

            tenMonAn.text= item.tenMonAn
            tenNhaHang.text=item.tenNhaHang
            ghiChu.text=item.option_description
            demSoLuong.text=item.quantity.toString()
            giaMonAn.text= FormatterMoney.formatterMoney(item.giaMonAn)
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked= item.checkBox

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                viewModelShoppingcart.isDone(isChecked,item) //xử lý trạng thái ủa checkbox tronng giỏ hàng
                viewModelShoppingcart.updateTrangThaiChon(item,isChecked)// xem xét và lấy các món ăn được chọn để hiển thị số món ăn tạm tính
                viewModelShoppingcart.updatePhiVanChuyen()// theo dõi và câp nhật phí vận chuyển khi người dùng nhấn o checkbox
                viewModelShoppingcart.updateTongTienHang()// Tổng Tiền hàng
                viewModelShoppingcart.updateCheckBox(item,isChecked)// Update checkBox len room
            }


            giamSoLuong.setOnClickListener{
                giamSoLuong.isEnabled=false
                android.os.Handler(Looper.getMainLooper()).postDelayed({giamSoLuong.isEnabled=true},300)
                val newSoLuong= (item.quantity-1).coerceAtLeast(1)// không giảm dưới 1
                viewModelShoppingcart.updateSoLuongMon(item,newSoLuong)
                if(item.checkBox && newSoLuong!= item.quantity)
                    viewModelShoppingcart.setTongTienGiam(newSoLuong,item)
                viewModelShoppingcart.updatePhiVanChuyen()// theo dõi và câp nhật phí vận chuyển khi người dùng nhấn o checkbox
                viewModelShoppingcart.updateTongTienHang()// Tổng Tiền hàng
            }
            tangSoLuong.setOnClickListener {
                tangSoLuong.isEnabled=false
                android.os.Handler(Looper.getMainLooper()).postDelayed({tangSoLuong.isEnabled=true},300)
                val newSoLuong= (item.quantity+1).coerceAtMost(50)// không quá 50 sản phẩm
                viewModelShoppingcart.updateSoLuongMon(item,newSoLuong)
                if(item.checkBox && newSoLuong!= item.quantity)
                    viewModelShoppingcart.setTongTienTang(newSoLuong,item)
                viewModelShoppingcart.updatePhiVanChuyen()// theo dõi và câp nhật phí vận chuyển khi người dùng nhấn o checkbox
                viewModelShoppingcart.updateTongTienHang()// Tổng Tiền hàng
            }
            iconXoaMonAn.setOnClickListener {
                viewModelShoppingcart.deleteItem(item)
                viewModelShoppingcart.observeCartChang()
                Toast.makeText(context,"Bạn vừa xóa món ${item.tenMonAn}",Toast.LENGTH_SHORT).show()
            }
        }
    }

}