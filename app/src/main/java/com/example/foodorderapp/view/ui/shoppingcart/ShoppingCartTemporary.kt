package com.example.foodorderapp.view.ui.shoppingcart

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.adapter.AdapterItemMonAnShoppingCart
import com.example.foodorderapp.datasource.DatabaseProvider
import com.example.foodorderapp.repository.FirebaseReposityGetCart
import com.example.foodorderapp.utils.FormatterMoney
import com.example.foodorderapp.view.MainActivity
import com.example.foodorderapp.viewmodel.ShoppingCartViewModelFactory
import com.example.foodorderapp.viewmodel.ViewModelGetInforUser
import com.example.foodorderapp.viewmodel.ViewModelShoppingcart
import org.w3c.dom.Text


class ShoppingCartTemporary : DialogFragment() {
    private lateinit var viewModelGetInforUser: ViewModelGetInforUser
    private lateinit var viewModelShoppingcart: ViewModelShoppingcart
    private lateinit var adapter: AdapterItemMonAnShoppingCart
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=Dialog(requireContext(),R.style.CartSideDialogTheme)
        dialog.setContentView(R.layout.fragment_shopping_cart_temporary)
        dialog.window?.apply {
            setLayout((resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.MATCH_PARENT)
            setGravity(Gravity.START)
            setBackgroundDrawable(ColorDrawable(Color.WHITE))
            attributes.windowAnimations=R.style.CartSideDialogAnimation
        }
        return dialog
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_cart_temporary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cartDao= DatabaseProvider.getDatabase(requireContext()).cartDao()
        val foodDao= DatabaseProvider.getDatabase(requireContext()).foodDao()
        val sellerDao= DatabaseProvider.getDatabase(requireContext()).sellerDao()

        val repository= FirebaseReposityGetCart(cartDao,sellerDao,foodDao)
        val factory= ShoppingCartViewModelFactory(repository)

        viewModelGetInforUser=ViewModelProvider(requireActivity())[ViewModelGetInforUser::class.java]
        viewModelShoppingcart= ViewModelProvider(requireActivity(),factory)[ViewModelShoppingcart::class.java]

        val tongTienTamTinh= view.findViewById<TextView>(R.id.txtTongCong)
        val exit= view.findViewById<ImageView>(R.id.iconThoat)
        val rv= view.findViewById<RecyclerView>(R.id.rvMonAn)
        val tienHanh= view.findViewById<TextView>(R.id.txtTienHanh)

        adapter= AdapterItemMonAnShoppingCart(viewModelShoppingcart)

        Log.d("ShoppingCartTemporary","doo ShoppingCartTemporary rooif")



        showDsMonAn(rv)


        viewModelShoppingcart.theoDoiTongTienTamTinh.observe(viewLifecycleOwner){tongTien->
            tongTienTamTinh.text= FormatterMoney.formatterMoney(tongTien)
        }
        exit.setOnClickListener {
            dismiss()
        }

        tienHanh.setOnClickListener {
            xuLy()
        }

    }

    private fun xuLy() {
        findNavController().navigate(R.id.nav_shoppingcart)
        dismiss()
    }


    private fun showDsMonAn(rv: RecyclerView?) {
        rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                Log.d("ShoppingCartTemporary", "Đang show món ăn")


        viewModelShoppingcart.theodsRoom?.observe(viewLifecycleOwner) { newList ->
            when {
                newList.isNotEmpty() -> {
                    Log.d("ShoppingCartFragment", "Lấy được ${newList.size} món từ Room")
                    adapter.submitList(newList)
                }
                newList.isEmpty() -> {
                    Log.d("ShoppingCartFragment", "Danh sách từ Room trống")
                    adapter.submitList(emptyList())
                }
                newList == null -> {
                    Log.d("ShoppingCartFragment", "Dữ liệu từ Room là null")
                }
            }
        }
        rv?.addItemDecoration(object : RecyclerView.ItemDecoration() {
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