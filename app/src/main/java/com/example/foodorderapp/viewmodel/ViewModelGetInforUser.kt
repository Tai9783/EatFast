package com.example.foodorderapp.viewmodel

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderapp.model.InforUser
import com.example.foodorderapp.repository.FirebaseReposityGetInforUser

class ViewModelGetInforUser() : ViewModel() {
    private val _theodoiInforUser =MutableLiveData<InforUser>()
    val theodoiInforUser: LiveData<InforUser> get()= _theodoiInforUser

    private val _theoDoiEmail= MutableLiveData<String>()
    val theoDoiEmail :LiveData<String> get()=_theoDoiEmail

    private val _updateInforUser= MutableLiveData<Boolean?>()
    val updateInforUser :LiveData<Boolean?> get()=_updateInforUser

    fun getInforUser(email:String){
        Log.d("ViewModel","đang chuẩn bị đưa $email vào firestore để tìm")
        FirebaseReposityGetInforUser().getInforUser(email){newInforUser->
            _theodoiInforUser.value=newInforUser
            Log.d("ViewModel","Timd được $email Có tên là ${newInforUser.full_name}")
            Log.d("ViewModel","Timd được $email Có tên là ${newInforUser.full_name}")
        }

    }
    fun setEmail(email:String){
        _theoDoiEmail.value=email
    }

    fun updateInforUser(item: InforUser) {
            FirebaseReposityGetInforUser().updateInforUser(item){isCheck->
                if(isCheck) {
                    Log.d("ViewModelGetInforUser", "Cập nhật thành công")
                    _theodoiInforUser.value = item
                    _updateInforUser.value=true
                }
                else {
                    Log.d("ViewModelGetInforUser", "Cập nhật thất bại")
                    _updateInforUser.value=false
                }
            }
    }

    fun resetUpdateState(){
        _updateInforUser.value=null
    }


}