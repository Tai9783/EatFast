package com.example.foodorderapp.model

import java.time.LocalDate
import java.time.LocalTime

data class OrderHoanThanh(
    val tenNhaHang: String,
    val thoigianDatHangNgay: LocalDate,
    val thoiGianDatHangGio: LocalTime,
    val list: List<FoodItemOrderDangGiao>,
    val tongTien: Int

)
