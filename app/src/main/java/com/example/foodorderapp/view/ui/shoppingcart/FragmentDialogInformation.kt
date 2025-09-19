package com.example.foodorderapp.view.ui.shoppingcart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.foodorderapp.R
import com.example.foodorderapp.utils.applySystemBarMargin
import com.example.foodorderapp.utils.applySystemBarPadding

class FragmentDialogInformation : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //bỏ nền trắng thừa các góc của hộp thoại
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        super.onViewCreated(view, savedInstanceState)
        val confirm= view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnConfirm)
        confirm.setOnClickListener {
            dismiss()
        }
    }


    // Xử lý dialog cách mép màn hình
    override fun onStart() {
        super.onStart()
        dialog?.window.let { window ->
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.decorView?.setPadding(50,0,50,0)
        }
    }
}