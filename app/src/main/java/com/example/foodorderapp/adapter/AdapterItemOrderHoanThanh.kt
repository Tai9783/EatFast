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
import com.example.foodorderapp.model.OrderHoanThanh
import com.example.foodorderapp.utils.FormatterMoney
import org.w3c.dom.Text
import java.time.format.DateTimeFormatter

class AdapterItemOrderHoanThanh(private val list: List<OrderHoanThanh>): RecyclerView.Adapter<AdapterItemOrderHoanThanh.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_hoanthanh,parent,false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val tenNhanHang= findViewById<TextView>(R.id.txtTenNhahang)
            val thoiGianDatHangNgay= findViewById<TextView>(R.id.txtThoigian_ngay)
            val thoiGianDatHangGio= findViewById<TextView>(R.id.txtThoigian_gio)
            val tongTien= findViewById<TextView>(R.id.txtTongTien)
            val listDs= findViewById<RecyclerView>(R.id.rvMonAn)

            tenNhanHang.text= list[position].tenNhaHang
            val dateFormatter= DateTimeFormatter.ofPattern("dd/MM/yyyy")
            thoiGianDatHangNgay.text= list[position].thoigianDatHangNgay.format(dateFormatter)
            val timeFormatter= DateTimeFormatter.ofPattern("HH:mm")
            thoiGianDatHangGio.text=list[position].thoiGianDatHangGio.format(timeFormatter)
            tongTien.text= FormatterMoney.formatterMoney(list[position].tongTien)
            listDs.adapter= AdapterItemMonOrderDangGiao(list[position].list)
            listDs.layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }
}