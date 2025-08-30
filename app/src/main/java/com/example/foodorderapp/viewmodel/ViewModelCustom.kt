package com.example.foodorderapp.viewmodel

import android.provider.MediaStore.Audio.Media
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelCustom: ViewModel() {

    private val _theoDoiSoLuong= MutableLiveData<Int>().apply { value=1 }
    private val _theoDoiGiaSize= MutableLiveData(0)
    private val _theoDoiGiaGoc= MutableLiveData(0)
    private val _theoDoiTenQuan= MutableLiveData<String>()


    val theoDoiSoLuong : LiveData<Int> get()= _theoDoiSoLuong
    val theoDoiGiaSize: LiveData<Int> get()= _theoDoiGiaSize
    val theoDoiGiaGoc: LiveData<Int> get()= _theoDoiGiaGoc
    val theoDoiTenQuan: LiveData<String> get()= _theoDoiTenQuan
    val combinedData= MediatorLiveData<Pair<Int,Int>>().apply {

    }

    init {

        combinedData.addSource(_theoDoiGiaSize){giaSize->
            val soLuong =_theoDoiSoLuong.value
            val giaGoc=_theoDoiGiaGoc.value
            if(soLuong!=null && giaGoc!=null)
                combinedData.value= Pair(soLuong,giaSize)
        }
        combinedData.addSource(_theoDoiSoLuong){soLuong->
            val giaSize=_theoDoiGiaSize.value
            val giaGoc=_theoDoiGiaGoc.value
            if (giaGoc!=null&&giaSize!=null)
                combinedData.value=Pair(soLuong,giaSize)
        }
    }


    val tongTienCuoiCung= MediatorLiveData<Int>().apply {
        addSource(_theoDoiSoLuong){tinhTong()}
        addSource(_theoDoiGiaSize){tinhTong()}
        addSource(_theoDoiGiaGoc){tinhTong()}
    }
    private fun MediatorLiveData<Int>.tinhTong(){
        val sl= _theoDoiSoLuong.value ?:1
        val giaSize= _theoDoiGiaSize.value?:0
        val giaGoc= _theoDoiGiaGoc.value?:0
        value= sl*(giaGoc+giaSize)
    }
    fun updateGiaSize(newGiaSize: Int){
        _theoDoiGiaSize.value= newGiaSize
    }
    fun updateGiaGoc(newGiaGoc: Int){
        _theoDoiGiaGoc.value=newGiaGoc
    }

    fun updateSoLuongGiam(){
        val current= _theoDoiSoLuong.value ?:1
        if (current>1){
            _theoDoiSoLuong.value= current-1
        }
    }
    fun updateSoLuongTang(){
        val current= _theoDoiSoLuong.value ?:1
        if(current<50){
            _theoDoiSoLuong.value= current+1

        }
    }
    fun updateTenQuan(tenQuan: String){
        _theoDoiTenQuan.value=tenQuan
    }

}