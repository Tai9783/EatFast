package com.example.foodorderapp.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


fun View.applySystemBarPadding(
    applyTop: Boolean = true,
    applyBottomNav: Boolean = false,
    applySystemNavBar: Boolean = false
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
        val bottomNavHeight = if (applySystemNavBar)
            insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
        else 0
        view.setPadding(
            view.paddingLeft,
            if (applyTop) statusBarHeight else view.paddingTop,
            view.paddingRight,
            if (applyBottomNav || applySystemNavBar) bottomNavHeight else view.paddingBottom
        )
        insets
    }
}
fun View.applySystemBarMargin(
    applyTop: Boolean=false,
    applyBottom: Boolean= false
){
    ViewCompat.setOnApplyWindowInsetsListener(this){view, inserts->
        val topInsert=inserts.getInsets(WindowInsetsCompat.Type.statusBars()).top
        val bottomInsert= inserts.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
        val params= view.layoutParams as? ViewGroup.MarginLayoutParams
        params?.let {
            if(applyTop) it.topMargin= topInsert
            if(applyBottom) it.bottomMargin=bottomInsert
            view.layoutParams= it
        }
        inserts
    }
}

