package com.example.foodorderapp.model

import android.widget.CheckBox

data class ShoppingCartItemMonAn(
    var checkBox: Boolean= false,
    val anhMonAn: String="",
    val tenMonAn: String="",
    val tenNhaHang: String="",
    val ghiChu: String="",
    val giaMonAn: Int=0,
    var demSoLuong: Int=0
)
