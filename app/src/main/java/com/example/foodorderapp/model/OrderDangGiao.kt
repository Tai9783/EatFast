package com.example.foodorderapp.model

import java.time.LocalDate
import java.time.LocalTime

data class OrderDangGiao(
    val tenNhaHang: String,
    val ngayDatHang: LocalDate,
    val gioDatHang: LocalTime,
    val listMonAn: List<FoodItemOrderDangGiao>,
    val tongTien: Int,
    val thoiGianGiaoHang: Int,
    val diaChiGiaoHang: String,
    val soDienThoai: String
)
