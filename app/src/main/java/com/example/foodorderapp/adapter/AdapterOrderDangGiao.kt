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
import com.example.foodorderapp.model.OrderDangGiao
import com.example.foodorderapp.utils.FormatterMoney
import java.time.format.DateTimeFormatter


class AdapterOrderDangGiao(private val list:List<OrderDangGiao>): RecyclerView.Adapter<AdapterOrderDangGiao.ViewHolder>() {
    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.order_item_danggiao,parent,false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.itemView.apply {
           val tenNhaHang= findViewById<TextView>(R.id.txtTenNhahang)
           val thoiGianNgay= findViewById<TextView>(R.id.txtThoigian_ngay)
           val thoiGianGio= findViewById<TextView>(R.id.txtThoigian_gio)
           val rvMonAn= findViewById<RecyclerView>(R.id.rvMonAn)
           val tongTien= findViewById<TextView>(R.id.txtTongTien)
           val thoiGianGiaoHang= findViewById<TextView>(R.id.txtThoiGianGiaohang)
           val diaDiem= findViewById<TextView>(R.id.txtDiaDiem)
           val soDienThoai= findViewById<TextView>(R.id.txtsdt)

           tenNhaHang.text= list[position].tenNhaHang
           val dateFormatter= DateTimeFormatter.ofPattern("dd/MM/yyyy")
           thoiGianNgay.text=list[position].ngayDatHang.format(dateFormatter)
           val timeFormatter=DateTimeFormatter.ofPattern("HH:mm")
           thoiGianGio.text=list[position].gioDatHang.format(timeFormatter)
           tongTien.text= FormatterMoney.formatterMoney(list[position].tongTien)
           thoiGianGiaoHang.text= list[position].thoiGianGiaoHang.toString()+" Phút"
           diaDiem.text= list[position].diaChiGiaoHang
           soDienThoai.text= list[position].soDienThoai

           rvMonAn.adapter= AdapterItemMonOrderDangGiao(list[position].listMonAn)
           rvMonAn.layoutManager= LinearLayoutManager(context)

       }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}