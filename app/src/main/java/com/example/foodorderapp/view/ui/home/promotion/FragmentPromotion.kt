package com.example.foodorderapp.view.ui.home.promotion

import android.graphics.Rect
import android.os.Bundle
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
import com.example.foodorderapp.adapter.AdapterPromotion
import com.example.foodorderapp.utils.applySystemBarPadding
import com.example.foodorderapp.view.MainActivity
import com.example.foodorderapp.view.ui.home.HomeFragment
import com.example.foodorderapp.viewmodel.ViewModelPromotion
import org.w3c.dom.Text

class FragmentPromotion : Fragment() {
    private lateinit var adapterPromotion: AdapterPromotion
    private lateinit var viewModelPromotion: ViewModelPromotion
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promotion, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.applySystemBarPadding(applyTop = true, applyBottomNav = true)
        val rvPromotion= view.findViewById<RecyclerView>(R.id.rvPromption)
        viewModelPromotion= ViewModelProvider(this)[ViewModelPromotion::class.java]
        adapterPromotion= AdapterPromotion(viewModelPromotion)
        // hiển thị các item khuyến mãi
        showPromtion(rvPromotion, viewModelPromotion)

        //xử lý sự kiện thoát màn hình khuyêns mãi
        val thoat = view.findViewById<TextView>(R.id.txtCacUuDai)
       thoat.setOnClickListener {

           findNavController().navigate(R.id.action_fragmentPromotion_to_homeFragment)
           (requireActivity() as MainActivity).setNavagationBarBottom(true)
       }

    }
    private fun showPromtion(
        rvPromotion: RecyclerView,
        viewModelPromotion: ViewModelPromotion
    ) {
        rvPromotion.adapter=adapterPromotion
        rvPromotion.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        viewModelPromotion.initData()
        viewModelPromotion.theoidoi.observe(viewLifecycleOwner){dsNew->
            adapterPromotion.submitList(dsNew)
        }
        rvPromotion.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom=50
            }
        })


    }

}