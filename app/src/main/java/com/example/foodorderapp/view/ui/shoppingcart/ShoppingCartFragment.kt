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
import androidx.appcompat.widget.AppCompatButton
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
import com.example.foodorderapp.utils.applySystemBarMargin
import com.example.foodorderapp.utils.applySystemBarPadding
import com.example.foodorderapp.view.MainActivity
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
        view.applySystemBarPadding(applyTop = true, applyBottomNav = true,applySystemNavBar = true)
        val rvMonAn = view.findViewById<RecyclerView>(R.id.rvMonAn)
        // 1. Lấy CartDao từ Room database
        val cartDao = DatabaseProvider.getDatabase(requireContext()).cartDao()
        val foodDao = DatabaseProvider.getDatabase(requireContext()).foodDao()
        val sellerDao = DatabaseProvider.getDatabase(requireContext()).sellerDao()

        val repository = FirebaseReposityGetCart(cartDao,sellerDao,foodDao) // khởi     tạo repository
        val factory = ShoppingCartViewModelFactory(repository)


        viewModelShoppingcart= ViewModelProvider(requireActivity(),factory)[ViewModelShoppingcart::class.java]
        adapterItemMonAnShoppingCart= AdapterItemMonAnShoppingCart(viewModelShoppingcart,"ShoppingCart")
        viewModelGetInforUser= ViewModelProvider(requireActivity())[ViewModelGetInforUser::class.java]

        val tongTienTamTinh= view.findViewById<TextView>(R.id.txtTongTienTamTinh)
        val soLuongMon= view.findViewById<TextView>(R.id.txtSoLuongMon)
        var dsMonDuocChon= emptyList<FoodItemCart>()
        val maGiamGia= view.findViewById<TextView>(R.id.txtTienGiamGia)
        val apDung=view.findViewById<MaterialButton>(R.id.txtApDung)
        val edtNhapMa=view.findViewById<EditText>(R.id.edtGiamGia)
        val phivanchuyen= view.findViewById<TextView>(R.id.txtTienGiaoHang)
        val tongCongTien= view.findViewById<TextView>(R.id.txtTongCongTien)
        val tongTienHang= view.findViewById<AppCompatButton>(R.id.btnXacNhan)
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
        viewModelShoppingcart.theoDoiTongTienHang.observe(viewLifecycleOwner){newTotal->
            tongCongTien.text= FormatterMoney.formatterMoney(newTotal)
            tongTienHang.text= "Tổng tiền hàng: ${FormatterMoney.formatterMoney(newTotal)}"
        }
        showMonAn(rvMonAn)
        viewModelShoppingcart.dsDuocChon.observe(viewLifecycleOwner) { newList ->
            tongTienHang.isEnabled = newList.isNotEmpty()
        }
        var isCheck= true
        viewModelGetInforUser.theodoiInforUser.observe(viewLifecycleOwner){newUser->
            if (newUser.address.isEmpty()|| newUser.phone.isEmpty())
                    isCheck=false
        }
        tongTienHang.setOnClickListener {
            if (isCheck) {
                val list = viewModelShoppingcart.dsDuocChon.value
               if (!list.isNullOrEmpty()){
                   // Chỉ cho đặt các món từ một quán trong một đơn
                   if (isFromOneShop(list)){
                       findNavController().navigate(R.id.action_shoppingCartFragment_to_fragmentOrderSummary)
                       (requireActivity() as MainActivity).setNavagationBarBottom(false)
                   }
                   else
                   {
                       val dialog= FragmentDialogInformation()
                       dialog.show(parentFragmentManager,"information")
                       Toast.makeText(context,"Chỉ cho phép đặt các món từ một quán", Toast.LENGTH_SHORT).show()
                       return@setOnClickListener
                   }
               }
            }
            else{
                Toast.makeText(context,"Vui lòng cập nhật thông tin",Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_shoppingCartFragment_to_fragmentCustomInformation)
                (requireActivity() as MainActivity).setNavagationBarBottom(false)
            }
        }
        thayDoiThongTin.setOnClickListener {
            findNavController().navigate(R.id.action_shoppingCartFragment_to_fragmentCustomInformation)
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
    //Hàm kiểm tra xem các món ăn có đến từ một quán hay không
    private fun isFromOneShop(selected: List<FoodItemCart>): Boolean {
       return selected.map { it.seller_id }.distinct().size==1
    }
}