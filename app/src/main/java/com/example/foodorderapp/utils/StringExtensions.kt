package com.example.foodorderapp.utils

import java.text.Normalizer

fun removeVietNamAccents(input: String): String{
    val temp= Normalizer.normalize(input,Normalizer.Form.NFD)
    return temp.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(),"")
}