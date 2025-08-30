package com.example.foodorderapp.view.ui.home.fragment_quan_an

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapp.R
import com.example.foodorderapp.adapter.AdapterItemFoodShop
import com.example.foodorderapp.interfaces.OnClickItemFoodShop
import com.example.foodorderapp.repository.FirebaseRepositoryBannerShop
import com.example.foodorderapp.utils.GridSpacingItemDecoration
import com.example.foodorderapp.utils.applySystemBarPadding
import com.example.foodorderapp.view.MainActivity
import com.example.foodorderapp.view.ui.shoppingcart.ShoppingCartTemporary
import com.example.foodorderapp.view.ui.shoppingcart.custom.FragmentCustomizeFood
import com.example.foodorderapp.viewmodel.ShareViewModelFoodId
import com.example.foodorderapp.viewmodel.ShareViewModelSellerId
import com.example.foodorderapp.viewmodel.ViewModelCustom
import com.example.foodorderapp.viewmodel.ViewModelItemMonAnSeller
import kotlin.math.abs


class fragment_item_quan_an : Fragment() {

    private lateinit var adapterItemFoodShop: AdapterItemFoodShop
    private lateinit var viewModelItemMonAnSeller: ViewModelItemMonAnSeller
    private lateinit var shareViewModelSellerId: ShareViewModelSellerId
    private lateinit var shareViewModelFoodId: ShareViewModelFoodId
    private lateinit var viewModelCustom: ViewModelCustom
    private var dX = 0f
    private var dY = 0f
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_quan_an, container, false)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.applySystemBarPadding(applyTop = false, applySystemNavBar = true)

        val iconThoat= view.findViewById<ImageView>(R.id.imgThoat)
        val tenQuanAn= view.findViewById<TextView>(R.id.txtTenQuanAn)
        val tongTBDanhGia= view.findViewById<TextView>(R.id.txtTongTB_DanhGia)
        val star1= view.findViewById<ImageView>(R.id.star1)
        val star2= view.findViewById<ImageView>(R.id.star2)
        val star3= view.findViewById<ImageView>(R.id.star3)
        val star4= view.findViewById<ImageView>(R.id.star4)
        val star5= view.findViewById<ImageView>(R.id.star5)
        val tongSoLuotDanhGia= view.findViewById<TextView>(R.id.txtTongSoDanhGia)
        val logoShop= view.findViewById<ImageView>(R.id.imgAnhQuanAn)
        val bannerShop= view.findViewById<ImageView>(R.id.imgBanner)
        val iconCart= view.findViewById<ImageView>(R.id.imgGioHang)

        // Khởi tạo ViewModel
        shareViewModelSellerId= ViewModelProvider(requireActivity())[ShareViewModelSellerId::class.java]
        viewModelItemMonAnSeller = ViewModelProvider(this)[ViewModelItemMonAnSeller::class.java]
        shareViewModelFoodId= ViewModelProvider(requireActivity())[ShareViewModelFoodId::class.java]
        viewModelCustom= ViewModelProvider(requireActivity())[ViewModelCustom::class.java]

        // Setup RecyclerView
        val rvMonAnShop = view.findViewById<RecyclerView>(R.id.rvMonAnShop)
        adapterItemFoodShop = AdapterItemFoodShop(object : OnClickItemFoodShop {
            override fun onClickAddCart(foodId: String) {

                val bottomSheet= FragmentCustomizeFood()
                shareViewModelFoodId.updateFoodId(foodId)
                fragmentManager?.let { bottomSheet.show(it,bottomSheet.tag) }
            }
        })
        rvMonAnShop.adapter = adapterItemFoodShop
        rvMonAnShop.layoutManager = GridLayoutManager(context, 2)
        val spacing = resources.getDimensionPixelSize(R.dimen.spacing_10)
        rvMonAnShop.addItemDecoration(GridSpacingItemDecoration(2, spacing, true))

        iconThoat.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentQuanAn_to_homeFragment)
            (requireActivity() as MainActivity).setNavagationBarBottom(true)

        }

        // Observe sellerId changes
        shareViewModelSellerId.theoDoiSellerId.observe(viewLifecycleOwner) { sellerId ->
            Log.d("FragmentQuanAn","Lấy được seller có id là: $sellerId")

            if (sellerId.isNotEmpty()) {
                // Load thông tin banner
                FirebaseRepositoryBannerShop().getInforSeller(sellerId) { sellerInfor ->
                    tenQuanAn.text = sellerInfor.nameShop
                    //lưu tên quán ăn để fragment khác có thể dùng
                    viewModelCustom.updateTenQuan(sellerInfor.nameShop)
                    tongTBDanhGia.text = sellerInfor.rating.toString()
                    tongSoLuotDanhGia.text = "(" + sellerInfor.totalReviews + " đánh giá)"
                    Glide.with(requireActivity())
                        .load(sellerInfor.logoShop)
                        .placeholder(R.drawable.home_logo_nhahang_default)
                        .error(R.drawable.home_logo_nhahang_default)
                        .into(logoShop)

                    Glide.with(requireActivity())
                        .load(sellerInfor.bannerShop)
                        .placeholder(R.drawable.home_logo_nhahang_default)
                        .error(R.drawable.home_logo_nhahang_default)
                        .into(bannerShop)

                    val list = listOf(star1, star2, star3, star4, star5)
                    val rating: Int = sellerInfor.rating.toInt()
                    for (i in list.indices) {
                        if (i < rating) {
                            list[i].setImageResource(R.drawable.home_ngoisao_danhgia1)
                        } else
                            list[i].setImageResource(R.drawable.home_ngoisao_danhgia_emty)
                    }
                }

                // Load món ăn
                viewModelItemMonAnSeller.getListMonAn(sellerId)
            }
        }

        // Observe food list changes
        viewModelItemMonAnSeller.theoDoiListItemMonAn.observe(viewLifecycleOwner) { listFood ->
            adapterItemFoodShop.submitList(listFood)
        }




        setupDraggableCart(iconCart)


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupDraggableCart(iconCart: ImageView) {
        iconCart.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = event.rawX - view.x
                    dY = event.rawY - view.y
                }

                MotionEvent.ACTION_UP -> {
                    // Nếu chỉ tap nhẹ => xem là click
                    if (abs(event.rawX - (view.x + view.width / 2)) < 10 &&
                        abs(event.rawY - (view.y + view.height / 2)) < 10
                    ) {
                        Toast.makeText(context, "Bạn đã vào giỏ hàng", Toast.LENGTH_SHORT).show()
                        ShoppingCartTemporary().show(parentFragmentManager,"CartSideDialog")
                    }
                    snapToNearestEdge(
                        view,
                        (view.parent as View).width,
                        (view.parent as View).height
                    )
                }

                MotionEvent.ACTION_MOVE -> {
                    val parent = view.parent as View
                    view.x = (event.rawX - dX).coerceIn(0f, (parent.width - view.width).toFloat())
                    view.y = (event.rawY - dY).coerceIn(0f, (parent.height - view.height).toFloat())
                }
            }
            true
        }
    }

    // Tách riêng logic snap to edge
    private fun snapToNearestEdge(view: View, parentWidth: Int, parentHeight: Int) {
        val middleX = parentWidth / 2f
        val targetX = if (view.x + view.width / 2f < middleX) {
            0f
        } else {
            (parentWidth - view.width).toFloat()
        }

        // Sử dụng coerceIn thay vì if-else
        val targetY = view.y.coerceIn(
            0f,
            (parentHeight - view.height).toFloat()
        )

        // Animate đến vị trí target
        view.animate()
            .x(targetX)
            .y(targetY)
            .setDuration(200)
            .start()
    }

}