package com.example.foodorderapp.view.ui.shoppingcart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.foodorderapp.R
import com.example.foodorderapp.utils.applySystemBarMargin

class FragmentConfirmOrder : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_order, container, false)
    }
    override fun  onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.applySystemBarMargin(applyTop = true, applyBottom = true)

        val exit= view.findViewById<ImageView>(R.id.imgThoat)

        exit.setOnClickListener {
           findNavController().popBackStack()
        }


    }


}