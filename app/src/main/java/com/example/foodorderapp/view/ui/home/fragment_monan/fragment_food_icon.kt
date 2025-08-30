package com.example.foodorderapp.view.ui.home.fragment_monan

import android.graphics.Rect
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.adapter.AdapterItemFoodIcon
import com.example.foodorderapp.utils.applySystemBarMargin
import com.example.foodorderapp.utils.applySystemBarPadding
import com.example.foodorderapp.view.MainActivity
import com.example.foodorderapp.viewmodel.ViewModelItemFoodIcon


class fragment_food_icon : Fragment() {
    private lateinit var viewModelItemFoodIcon: ViewModelItemFoodIcon
    private lateinit var adapterItemFoodIcon: AdapterItemFoodIcon
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_icon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.applySystemBarPadding(applyTop = true, applyBottomNav = true  )
        // lấy được loại từ ăn đươcj gửi từ bên HomeFragment
        val categoryType= arguments?.let { fragment_food_iconArgs.fromBundle(it).categoryType }
        val nameFood= arguments?.let { fragment_food_iconArgs.fromBundle(it).nameFood }
        val title= view.findViewById<TextView>(R.id.txtTieuDe_MonAn)
        title.text= nameFood
        title.setOnClickListener {
            (requireActivity() as MainActivity).setNavagationBarBottom(true)
            findNavController().navigate(R.id.action_fragmentFoodIcon_to_homeFragment)
        }
        val rvFoodIcon= view.findViewById<RecyclerView>(R.id.rvDsMonAn)
        viewModelItemFoodIcon= ViewModelProvider(this)[ViewModelItemFoodIcon::class.java]
        adapterItemFoodIcon= AdapterItemFoodIcon()
        rvFoodIcon.adapter=adapterItemFoodIcon
        rvFoodIcon.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        rvFoodIcon.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom=50
            }
        })
        if(categoryType!=null)
        {
            if(categoryType.isNotEmpty())
            {
                viewModelItemFoodIcon.getListFoodIcon(categoryType)
                viewModelItemFoodIcon.theoDoiListFoodIcon.observe(viewLifecycleOwner){dsNew->
                    adapterItemFoodIcon.submitList(dsNew)
                }
            }
        }



    }

}