package com.example.foodorderapp.view.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodorderapp.R
import com.example.foodorderapp.utils.applySystemBarPadding
import com.example.foodorderapp.viewmodel.ViewModelGetInforUser


class AccountFragment : Fragment() {
    private lateinit var viewModelGetInforUser: ViewModelGetInforUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            view.applySystemBarPadding(applyTop = true, applyBottomNav = true)
        viewModelGetInforUser= ViewModelProvider(requireActivity())[ViewModelGetInforUser::class.java]
        val avatar= view.findViewById<ImageView>(R.id.account_avata)
        val fullName= view.findViewById<TextView>(R.id.txtTenUser)
        val phoneUser= view.findViewById<TextView>(R.id.txtSdt_User)
        viewModelGetInforUser.theodoiInforUser.observe(viewLifecycleOwner){newUser->
            fullName.text= newUser.full_name
            if(newUser.full_name.isNotEmpty()){
                Glide.with(this)
                    .load(newUser.avatar_url)
                    .error(R.drawable.avatar)
                    .into(avatar)
            }
            else{
               avatar.setImageResource(R.drawable.avatar)
            }
            if(newUser.phone.isNotEmpty()){
                phoneUser.text=newUser.phone
            }
            else
                phoneUser.text=newUser.email

        }
    }
}