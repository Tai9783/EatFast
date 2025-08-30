package com.example.foodorderapp.view.ui.shoppingcart

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.adapter.AdapterItemMonAnShoppingCart
import com.example.foodorderapp.datasource.DatabaseProvider
import com.example.foodorderapp.model.FoodItemCart
import com.example.foodorderapp.repository.FirebaseReposityGetCart
import com.example.foodorderapp.utils.FormatterMoney
import com.example.foodorderapp.utils.applySystemBarPadding
import com.example.foodorderapp.viewmodel.ShoppingCartViewModelFactory
import com.example.foodorderapp.viewmodel.ViewModelGetInforUser
import com.example.foodorderapp.viewmodel.ViewModelShoppingcart
import com.google.android.material.button.MaterialButton

class ShoppingCartFragment : Fragment() {

private lateinit var viewModelShoppingcart: ViewModelShoppingcart
private lateinit var adapterItemMonAnShoppingCart: AdapterItemMonAnShoppingCart
private lateinit var viewModelGetInforUser: ViewModelGetInforUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.applySystemBarPadding(applyTop = true, applyBottomNav = true)
        val rvMonAn = view.findViewById<RecyclerView>(R.id.rvMonAn)
        // 1. Lấy CartDao từ Room database
        val cartDao = DatabaseProvider.getDatabase(requireContext()).cartDao()
        val foodDao = DatabaseProvider.getDatabase(requireContext()).foodDao()
        val sellerDao = DatabaseProvider.getDatabase(requireContext()).sellerDao()

        val repository = FirebaseReposityGetCart(cartDao,sellerDao,foodDao) // khởi tạo repository
        val factory = ShoppingCartViewModelFactory(repository)


        viewModelShoppingcart= ViewModelProvider(requireActivity(),factory)[ViewModelShoppingcart::class.java]
        adapterItemMonAnShoppingCart= AdapterItemMonAnShoppingCart(viewModelShoppingcart)
        viewModelGetInforUser= ViewModelProvider(requireActivity())[ViewModelGetInforUser::class.java]

        val tongTienTamTinh= view.findViewById<TextView>(R.id.txtTongTienTamTinh)
        val soLuongMon= view.findViewById<TextView>(R.id.txtSoLuongMon)
        var dsMonDuocChon= emptyList<FoodItemCart>()
        val maGiamGia= view.findViewById<TextView>(R.id.txtTienGiamGia)
        val apDung=view.findViewById<MaterialButton>(R.id.txtApDung)
        val edtNhapMa=view.findViewById<EditText>(R.id.edtGiamGia)
        val phivanchuyen= view.findViewById<TextView>(R.id.txtTienGiaoHang)
        val tongCongTien= view.findViewById<TextView>(R.id.txtTongCongTien)
        val tongCongTienHang= view.findViewById<TextView>(R.id.txtTongTienHang)
        val tongTienHang= view.findViewById<ConstraintLayout>(R.id.layoutTongTien)
        val diaChi= view.findViewById<TextView>(R.id.txtDiaChi)
        val thayDoiThongTin= view.findViewById<TextView>(R.id.txtThayDoiDiaChi)

        //Xử lý Tổng Tiên Tạm Tính
        viewModelShoppingcart.theoDoiTongTienTamTinh.observe(viewLifecycleOwner){newTien->
            tongTienTamTinh.text= FormatterMoney.formatterMoney(newTien)
        }
        viewModelGetInforUser.theodoiInforUser.observe(viewLifecycleOwner){newUser->
            Log.d("ShoppingCart","đang Thay đổi địa chỉ ${newUser.address}")
            val address= newUser.address
            if(address.isEmpty())
                diaChi.text="Vui lòng cập nhật địa chỉ"
            else
                diaChi.text=address
        }


        // Xử lý Tổng số món đã chọn để tính tiền
        viewModelShoppingcart.dsDuocChon.observe(viewLifecycleOwner) { dsDuocChon ->
            dsMonDuocChon=dsDuocChon
            soLuongMon.text = dsMonDuocChon.size.toString()
        }
        //Xử lý mã giảm giá
        apDung.setOnClickListener {
            if (viewModelShoppingcart.dsDuocChon.value.isNullOrEmpty()){
                Toast.makeText(context,"Vui lòng chọn món trước khi áp mã giảm giá",Toast.LENGTH_SHORT).show()
                edtNhapMa.setText("")
                return@setOnClickListener
            }
            val ma= edtNhapMa.text.toString()
            if (ma.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập mã giảm giá", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (ma.trim() != "130504") {
                edtNhapMa.setText("")
                Toast.makeText(context, "Mã không hợp lê", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            edtNhapMa.setText("")
            viewModelShoppingcart.checkMaGiamGia(ma,dsMonDuocChon)
        }

        viewModelShoppingcart.theoDoiMaGiamGia.observe(viewLifecycleOwner){giamgia->
            maGiamGia.text=FormatterMoney.formatterMoney(giamgia)

        }

        //Xử lý phí vận chuyển
        viewModelShoppingcart.theoDoiPhiVanChuyen.observe(viewLifecycleOwner){tienGiaoHang->
            phivanchuyen.text= FormatterMoney.formatterMoney(tienGiaoHang)
        }
        //Xử lý tổng cộng tiền hàng
        viewModelShoppingcart.theoDoiTongTienHang.observe(viewLifecycleOwner){tongTienHang->
            tongCongTien.text= FormatterMoney.formatterMoney(tongTienHang)
            tongCongTienHang.text= FormatterMoney.formatterMoney(tongTienHang)
        }
        val progressBar= view.findViewById<ProgressBar>(R.id.progressBar)
        setupLoadingObserver(progressBar)
        showMonAn(rvMonAn)

        tongTienHang.setOnClickListener {
            Toast.makeText(context,"Bạn đã qua tóm tắt đơn hàng",Toast.LENGTH_SHORT).show()
        }

        thayDoiThongTin.setOnClickListener {
            findNavController().navigate(R.id.fragmentCustomInformation)
        }
    }

    private fun setupLoadingObserver(progressBar: ProgressBar) {
        viewModelShoppingcart.isLoading.observe(viewLifecycleOwner){isLoading->
            progressBar.visibility= if (isLoading) View.VISIBLE else   View.GONE
        }
    }


    private fun showMonAn( rvMonAn: RecyclerView) {
        rvMonAn.adapter=adapterItemMonAnShoppingCart
        rvMonAn.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                viewModelShoppingcart.theodsRoom?.observe(viewLifecycleOwner) { newList ->
                    when {
                        newList.isNotEmpty() -> {
                            Log.d("ShoppingCartFragment", "Lấy được ${newList.size} món từ Room")
                            adapterItemMonAnShoppingCart.submitList(newList)
                        }
                        newList.isEmpty() -> {
                            Log.d("ShoppingCartFragment", "Danh sách từ Room trống")
                            adapterItemMonAnShoppingCart.submitList(emptyList())
                        }
                        newList == null -> {
                            Log.d("ShoppingCartFragment", "Dữ liệu từ Room là null")
                        }
                    }
                }



        rvMonAn.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom=40
            }
        })

    }
}