package com.example.foodorderapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapp.model.FoodItemCart
import com.example.foodorderapp.repository.FirebaseReposityGetCart
import com.example.foodorderapp.repository.FirebaseReposityGetInforUser
import kotlinx.coroutines.launch

class ViewModelShoppingcart(private val repository: FirebaseReposityGetCart) : ViewModel() {


    private var dsRoom: List<FoodItemCart>? = emptyList()

    private val _dsDuocChon = MutableLiveData<List<FoodItemCart>>()
    val dsDuocChon: LiveData<List<FoodItemCart>> get() = _dsDuocChon

    private val _theoDoiTongTienTamTinh = MutableLiveData<Int>()
    val theoDoiTongTienTamTinh: LiveData<Int> get() = _theoDoiTongTienTamTinh

    private val _theoDoiMaGiamGia = MutableLiveData<Int>()
    val theoDoiMaGiamGia: LiveData<Int> get() = _theoDoiMaGiamGia

    private val _theoDoiPhiVanChuyen = MutableLiveData<Int>()
    val theoDoiPhiVanChuyen: LiveData<Int> get() = _theoDoiPhiVanChuyen

    private val _theoDoiTongTienHang = MutableLiveData<Int>()
    val theoDoiTongTienHang: LiveData<Int> get() = _theoDoiTongTienHang

    private var _theoDoiListCartRoom: LiveData<List<FoodItemCart>>? = null
    val theodsRoom :LiveData<List<FoodItemCart>>? get() = _theoDoiListCartRoom

    // Thêm loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Thêm sync completion state
    private val _isSyncComplete = MutableLiveData<Boolean>()
    val isSyncComplete: LiveData<Boolean> get() = _isSyncComplete
    //gắn cờ canh chỉ cho load từ Firestore lên Room lần đầu khi vào app
    private var hasSyncedOnce= false


    // theo dõi trạng thái của ô checkbox để cập nhật và lưu trên ds RecycleView
    private var tongtien = 0

    fun isDone(isDoneNew: Boolean, item: FoodItemCart) {
        dsRoom= _theoDoiListCartRoom?.value
        if (dsRoom!=null){
        val index = dsRoom!!.indexOfFirst { it.cartItemId == item.cartItemId }
        if (index != -1 && dsRoom!![index].checkBox!=isDoneNew) {
            val newItem = item.copy(checkBox = isDoneNew)
            // Tính tiền tạm tính
            if (newItem.checkBox) {
                tongtien += item.quantity * item.giaMonAn
                _theoDoiTongTienTamTinh.value = tongtien
            } else {
                tongtien -= item.quantity * item.giaMonAn
                _theoDoiTongTienTamTinh.value = tongtien
            }

        }

        }
    }
    //Update thay đổi của checkbox lên Room
    fun updateCheckBox(item: FoodItemCart,isCheck:Boolean){
        viewModelScope.launch {
            repository.updateItem(item.copy(checkBox = isCheck))
            Log.d("ViewModelShoppingCart_UpdateChecBox","đã update thành công checkbox $isCheck củ item ${item.tenMonAn}")
        }
    }




    fun setTongTienTang(newSoLuong: Int, item: FoodItemCart) {
        tongtien -= (newSoLuong - 1) * item.giaMonAn
        tongtien += newSoLuong * item.giaMonAn
        _theoDoiTongTienTamTinh.value = tongtien
    }

    fun setTongTienGiam(newSoLuong: Int, item: FoodItemCart) {
        tongtien -= (newSoLuong + 1) * item.giaMonAn
        tongtien += newSoLuong * item.giaMonAn
        _theoDoiTongTienTamTinh.value = tongtien
    }
    fun initDataShoppingCart(email:String){
        FirebaseReposityGetInforUser().getInforUser(email){newUser->
            val userId=newUser.user_id
            viewModelScope.launch {
                try {
                    Log.d("MainActivity", "Bắt đầu sync dữ liệu cho userId: $userId")
                    repository.syncData(userId)
                    _theoDoiListCartRoom = repository.getdsRooom(userId)
                    Log.d(
                        "MainActivity",
                        "Sync hoàn thành, thiết lập LiveData observer"
                    )
                } catch (e: Exception) {
                    Log.e("ViewModelShoppingcart", "Lỗi khi sync dữ liệu: ${e.message}")

                }

            }
        }
    }


    fun updateSoLuongMon(item: FoodItemCart, newSoLuong: Int) {

        val newItem = item.copy(quantity = newSoLuong)
        viewModelScope.launch {
            repository.updateItem(newItem)
        }
    }

    // Lấy ds gồm các món ăn được chọn để hiển thị lên tổng số món đã chọn
    private var dsChon: List<FoodItemCart> = emptyList()

    fun observeCartChang(){
        _theoDoiListCartRoom?.observeForever{cartList->
            cartList?.let {
                updateDschon(it)
                tinhLaiTongTien(it)
            }
        }
    }
    private fun tinhLaiTongTien(ds: List<FoodItemCart>){
        tongtien=ds.filter { it.checkBox }.sumOf { it.quantity*it.giaMonAn }
        _theoDoiTongTienTamTinh.value=tongtien
        updatePhiVanChuyen()
        updateTongTienHang()
    }
    fun deleteItem(item:FoodItemCart){
        viewModelScope.launch {
            repository.deleteItem(item)
        }

    }
    private fun updateDschon(ds: List<FoodItemCart>){
        dsChon = ds.filter { it.checkBox } ?: emptyList()
        _dsDuocChon.value = dsChon
    }

    fun updateTrangThaiChon(itemMonAn: FoodItemCart, isDoneNew: Boolean) {
        val ds = _theoDoiListCartRoom?.value?.map {
            if (it.cartItemId == itemMonAn.cartItemId) it.copy(checkBox = isDoneNew) else it
        }
        if (ds!=null)
            updateDschon(ds)
    }

    private var isMaApdung: Boolean = false // đặt cờ để tính giảm giá
    private var giamgia = 0

    fun checkMaGiamGia(ma: String, dsMonAnDuocChon: List<FoodItemCart>) {
        val ma1 = "130504"
        if (dsMonAnDuocChon.isEmpty()) {
            isMaApdung = false
            giamgia = 0
            _theoDoiMaGiamGia.value = giamgia
            return
        }
        if (ma.trim() == ma1) {
            giamgia = -30000
            _theoDoiMaGiamGia.value = giamgia
            isMaApdung = true
        } else {
            giamgia = 0
            _theoDoiMaGiamGia.value = giamgia
        }
        _theoDoiMaGiamGia.value = giamgia
        updateTongTienHang()

    }

    private var phiVanChuyen = 0

    fun updatePhiVanChuyen() {

        if (dsChon.isNotEmpty()) {
            phiVanChuyen = if (tongtien > 200000) 0 else 30000
            _theoDoiPhiVanChuyen.value = phiVanChuyen
        }
        if (dsChon.isEmpty()) {
            phiVanChuyen = 0
            _theoDoiPhiVanChuyen.value = phiVanChuyen
            isMaApdung = false
            _theoDoiMaGiamGia.value =0
        }
    }

    fun updateTongTienHang() {
        val tienGiamGia = if (isMaApdung) giamgia else 0

        _theoDoiTongTienHang.value = tongtien + tienGiamGia + phiVanChuyen

    }
}
