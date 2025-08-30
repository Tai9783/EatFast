package com.example.foodorderapp.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object FormatterMoney {
    fun formatterMoney(amount: Int):String{
        val symbols= DecimalFormatSymbols().apply {
            groupingSeparator='.'
        }
        val formatter= DecimalFormat("#,###",symbols)
        return formatter.format(amount)+"đ"
    }
}