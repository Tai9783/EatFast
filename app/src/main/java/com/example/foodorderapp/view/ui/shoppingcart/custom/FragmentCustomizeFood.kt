package com.example.foodorderapp.view.ui.shoppingcart.custom

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapp.R
import com.example.foodorderapp.adapter.AdapterSizeOptionFood
import com.example.foodorderapp.adapter.AdapterTastesFood
import com.example.foodorderapp.datasource.DAO.CartDao
import com.example.foodorderapp.datasource.DatabaseProvider
import com.example.foodorderapp.model.FoodItemCart
import com.example.foodorderapp.repository.FirebaseRepositoryCustom
import com.example.foodorderapp.repository.FirebaseReposityInforMonAn
import com.example.foodorderapp.utils.FormatterMoney
import com.example.foodorderapp.utils.GridSpacingItemDecoration
import com.example.foodorderapp.utils.applySystemBarPadding
import com.example.foodorderapp.utils.removeVietNamAccents
import com.example.foodorderapp.viewmodel.CombinedViewModelCustom
import com.example.foodorderapp.viewmodel.CombinedViewModelFactory
import com.example.foodorderapp.viewmodel.ShareViewModelFoodId
import com.example.foodorderapp.viewmodel.ShareViewModelSellerId
import com.example.foodorderapp.viewmodel.ViewModelCustom
import com.example.foodorderapp.viewmodel.ViewModelGetInforUser
import com.example.foodorderapp.viewmodel.ViewModelSizeOptionFood
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import com.google.android.material.R as MaterialR



class FragmentCustomizeFood : BottomSheetDialogFragment() {
    private lateinit var viewModelSizeOptionFood: ViewModelSizeOptionFood
    private lateinit var adapterSizeOptionFood: AdapterSizeOptionFood
    private lateinit var adapterTastesFood: AdapterTastesFood
    private lateinit var shareViewModelSellerId: ShareViewModelSellerId
    private lateinit var combinedViewModelCustom: CombinedViewModelCustom
    private lateinit var viewModelGetInforUser: ViewModelGetInforUser
    private lateinit var shareViewModelFoodId: ShareViewModelFoodId
    private lateinit var viewModelCustom: ViewModelCustom

    override fun onStart() {
        super.onStart()
        dialog?.let { dlg->
            val bottomSheet = dlg.findViewById<View>(MaterialR.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height=(resources.displayMetrics.heightPixels*0.7).toInt()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customize_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.applySystemBarPadding(applyTop = true,applyBottomNav = true)
        val rvSizeOption= view.findViewById<RecyclerView>(R.id.rvSizeOption)
        val thoat= view.findViewById<ImageView>(R.id.imgIconThoat)
        val rvKhauVi= view.findViewById<RecyclerView>(R.id.rvKhauVi)
        val imageFood= view.findViewById<ImageView>(R.id.imgMonAn)
        val nameFood=view.findViewById<TextView>(R.id.txtTenMonAn)
        val nameShop=view.findViewById<TextView>(R.id.txtTenQuanAn)
        val price= view.findViewById<TextView>(R.id.txtGiaMonAn)
        val tongtien= view.findViewById<TextView>(R.id.txtTongTien)
        val themVaoGio= view.findViewById<TextView>(R.id.txtThemVaoGio)

        val  iconGiam= view.findViewById<ImageView>(R.id.imgGiam)
        val iconTang= view.findViewById<ImageView>(R.id.imgTang)
        val demSoLuong= view.findViewById<TextView>(R.id.txtDemSoLuong)

        shareViewModelSellerId= ViewModelProvider(requireActivity())[ShareViewModelSellerId::class.java]
        viewModelSizeOptionFood= ViewModelProvider(this)[ViewModelSizeOptionFood::class.java]
        shareViewModelFoodId= ViewModelProvider(requireActivity())[ShareViewModelFoodId::class.java]
        viewModelCustom= ViewModelProvider(requireActivity())[ViewModelCustom::class.java]
        viewModelGetInforUser= ViewModelProvider(requireActivity())[ViewModelGetInforUser::class.java]

        combinedViewModelCustom= ViewModelProvider(this,CombinedViewModelFactory(
            viewModelCustom,
            shareViewModelSellerId,
            shareViewModelFoodId,
            viewModelGetInforUser

        ))[CombinedViewModelCustom::class.java]
        // 1. Lấy CartDao từ Room database
        val cartDao = DatabaseProvider.getDatabase(requireContext()).cartDao()

        adapterSizeOptionFood= AdapterSizeOptionFood(viewModelCustom)
        adapterTastesFood= AdapterTastesFood()
            shareViewModelFoodId.theoDoiFoodId.observe(viewLifecycleOwner){newFoodId->
            //Load thông tin món ăn
                if (newFoodId != null) {
                    Log.d("Custom","Lấy được foodId là: $newFoodId")
                    viewModelSizeOptionFood.getCustomizeOptionFood(newFoodId)
                    viewModelCustom.theoDoiTenQuan.observe(viewLifecycleOwner){newTenQuan->
                        nameShop.text= newTenQuan
                    }
                    FirebaseReposityInforMonAn().getInforMonAn(newFoodId) { newNameFood, newPrice, newImage ->
                        nameFood.text = newNameFood
                        price.text = FormatterMoney.formatterMoney(newPrice)

                        Glide.with(this)
                            .load(newImage)
                            .error(R.drawable.home_logo_nhahang_default)
                            .into(imageFood)

                        viewModelCustom.updateGiaGoc(newPrice)

                    }

                }
                //theo dõi và cập nhật giá tiền.
                viewModelCustom.tongTienCuoiCung.observe(viewLifecycleOwner) { newTongTien ->
                    tongtien.text =
                        FormatterMoney.formatterMoney(newTongTien)
                    themVaoGio.text="Thêm vào giỏ hàng  "+FormatterMoney.formatterMoney(newTongTien)
                }

                viewModelCustom.theoDoiSoLuong.observe(viewLifecycleOwner) { newSl ->
                    demSoLuong.text = newSl.toString()

                }
                //lấy thông tin món ăn khi user nhấn vào icon thêm món ăn vào giỏ
                combinedViewModelCustom.combinedData.observe(viewLifecycleOwner){data->
                    currentCombinedData=data
                }
                themVaoGio.setOnClickListener {
                    xacNhanThemVaoGio(themVaoGio,cartDao)
                }


        }



        // load danh sách Size món ăn
        loadSizeOption(rvSizeOption)
        // load danh sách list các khẩu vị món ăn.
        loadTastes(rvKhauVi)

        thoat.setOnClickListener {
           dismiss()
        }


        iconGiam.setOnClickListener {
            iconGiam.isEnabled= false
            Handler(Looper.getMainLooper()).postDelayed({iconGiam.isEnabled=true},300)
            viewModelCustom.updateSoLuongGiam()
        }
        iconTang.setOnClickListener {
            iconTang.isEnabled=false
            Handler(Looper.getMainLooper()).postDelayed({iconTang.isEnabled=true},300)
            viewModelCustom.updateSoLuongTang()

        }


    }

    private var currentCombinedData: CombinedViewModelCustom.CombinedData?=null
    private fun xacNhanThemVaoGio(themVaoGio: TextView, cartDao: CartDao) {
        val data= currentCombinedData?: return// lấy giá trị từ biến currentCombinedData

        val taste= adapterTastesFood.getSelectTastes()
        val optionSize= adapterSizeOptionFood.getSelectedSize()

        var optionDescription=""
        // Kiểm tra null trước
        if (optionSize == null) {
            Toast.makeText(context, "Vui lòng chọn size", Toast.LENGTH_SHORT).show()
            return
        }
        if (taste == null) {
            Toast.makeText(context, "Vui lòng chọn khẩu vị", Toast.LENGTH_SHORT).show()
            return
        }

        // Kiểm tra tên rỗng
        if (taste.nameTaste.isEmpty()) {
            Toast.makeText(context, "Vui lòng chọn khẩu vị", Toast.LENGTH_SHORT).show()
            return
        }
        if (optionSize.nameSize.isEmpty()) {
            Toast.makeText(context, "Vui lòng chọn size", Toast.LENGTH_SHORT).show()
            return
        }
            if (taste.nameTaste.isNotEmpty() && optionSize.nameSize.isNotEmpty()) {
                optionDescription = "Size " + optionSize.nameSize + "," + taste.nameTaste
                themVaoGio.text = "Đang thêm..."
                FirebaseRepositoryCustom(cartDao = cartDao).getCartId { newCart ->
                    val item = FoodItemCart(
                        cartItemId = data.foodId+ removeVietNamAccents(optionDescription),
                        cart_id = newCart,
                        food_id = data.foodId,
                        option_description = optionDescription,
                        quantity = data.quantity,
                        seller_id = data.sellerId,
                        user_id = data.userId,
                        priceSize = data.priceSize
                    )
                    lifecycleScope.launch {
                        FirebaseRepositoryCustom(cartDao = cartDao).addCartAndRoom(item)
                            if (!isAdded) return@launch // Fragment đã detach, không làm gì cả
                                themVaoGio.text = "Đã thêm vào giỏ hàng!!"
                                Handler(Looper.getMainLooper()).postDelayed({
                                    dismiss()
                                }, 100) // chờ 1s rồi đóng

                    }

                }

            }



    }

    private fun loadSizeOption(rvSizeOption: RecyclerView) {
        rvSizeOption.adapter= adapterSizeOptionFood
        rvSizeOption.layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        viewModelSizeOptionFood.theoDoiListSizeOptionFood.observe(viewLifecycleOwner){dsNew->
            adapterSizeOptionFood.submitList(dsNew)
        }

        rvSizeOption.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom=10
            }
        })
    }

    private fun loadTastes(rvKhauVi: RecyclerView) {
        rvKhauVi.adapter=adapterTastesFood
        rvKhauVi.layoutManager=GridLayoutManager(context,2)
        val spacing= resources.getDimensionPixelSize(R.dimen.spacing_5)
        rvKhauVi.addItemDecoration(GridSpacingItemDecoration(2,spacing,true))
        viewModelSizeOptionFood.theoDoiListTaste.observe(viewLifecycleOwner){dsNew->
            adapterTastesFood.submitList(dsNew)
        }
    }

}