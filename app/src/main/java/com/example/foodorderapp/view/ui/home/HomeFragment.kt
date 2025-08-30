package com.example.foodorderapp.view.ui.home
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapp.R
import com.example.foodorderapp.adapter.AdapterIconFoodHome
import com.example.foodorderapp.adapter.AdapterItemNhahangHome
import com.example.foodorderapp.interfaces.OnItemClickListenerItemShop
import com.example.foodorderapp.interfaces.OnItemClickListener_IconMonAn_Home
import com.example.foodorderapp.model.IconFood
import com.example.foodorderapp.model.RestaurantItem
import com.example.foodorderapp.utils.GridSpacingItemDecoration
import com.example.foodorderapp.utils.applySystemBarPadding
import com.example.foodorderapp.view.MainActivity
import com.example.foodorderapp.view.ui.home.promotion.FragmentPromotion
import com.example.foodorderapp.viewmodel.ShareViewModelSellerId
import com.example.foodorderapp.viewmodel.ViewModelGetInforUser
import com.example.foodorderapp.viewmodel.ViewModelRestaurant
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment : Fragment() {

    private lateinit var viewModelRestaurantItem: ViewModelRestaurant
    private lateinit var adapterNhaHang: AdapterItemNhahangHome

    private lateinit var recyclerViewIconFood: RecyclerView
    private lateinit var recyclerViewNhaHang: RecyclerView

    private lateinit var viewModelGetInforUser: ViewModelGetInforUser
    private lateinit var shareViewModelSellerId: ShareViewModelSellerId // cập nhật và lưu trữ seller_id

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate fragment_home.xml
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        view.applySystemBarPadding(applyTop = true, applyBottomNav = true)
        // Khởi tạo viewModel và adapter
        viewModelRestaurantItem = ViewModelProvider(this)[ViewModelRestaurant::class.java]
        shareViewModelSellerId= ViewModelProvider(requireActivity())[ShareViewModelSellerId::class.java]
        adapterNhaHang = AdapterItemNhahangHome(mutableListOf(),object : OnItemClickListenerItemShop{
            override fun onclick(shop: RestaurantItem) {
                Log.d("HomeFragment","Lấy đuọc Seller có id ${shop.sellerId}")
                shareViewModelSellerId.setSellerId(shop.sellerId)
                    (requireActivity() as MainActivity).setNavagationBarBottom(false)
                    findNavController().navigate(R.id.fragmentQuanAn)

            }
        })

        // Ánh xạ view
        recyclerViewIconFood = view.findViewById(R.id.lvFoodItem)
        recyclerViewNhaHang = view.findViewById(R.id.lvItemNhahang)

        // Setup icon món ăn (hamburger, pizza, phở, ...)
        showIconFood()

        // Setup danh sách nhà hàng từ ViewModel
        showItemRestaurant()

        // Xử lý nút bấm xem tất cả Khuyến Mãi
        val xemTatCaPromotion= view.findViewById<TextView>(R.id.txtAll)
        xemTatCaPromotion.setOnClickListener {
            xemTatCa()
        }
        // Update thông tin user phù hợp khi Login thành công
        viewModelGetInforUser= ViewModelProvider(requireActivity())[ViewModelGetInforUser::class.java]
        val nameUser= view.findViewById<TextView>(R.id.Name_user)
        val avatarUrl= view.findViewById<CircleImageView>(R.id.home_avata)
        viewModelGetInforUser.theodoiInforUser.observe(viewLifecycleOwner){newUser->
            nameUser.text=newUser.full_name
            if(newUser.avatar_url.isNotEmpty()){
                Glide.with(this)
                    .load(newUser.avatar_url)
                    .error(R.drawable.home_logo_nhahang_default)
                    .into(avatarUrl)
            }
            else{
                avatarUrl.setImageResource(R.drawable.avatar)
            }


        }

    }

    private fun xemTatCa() {
            (requireActivity() as MainActivity).setNavagationBarBottom(false)
        findNavController().navigate(R.id.fragment_Promtion)

    }

    private fun showItemRestaurant() {
        recyclerViewNhaHang.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewNhaHang.adapter = adapterNhaHang

        viewModelRestaurantItem.restaurant.observe(viewLifecycleOwner) { newList ->
            adapterNhaHang.upadte(newList)
        }

        viewModelRestaurantItem.fetchRestaurant()

        recyclerViewNhaHang.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom = 60
            }
        })
    }

    private fun showIconFood() {
        val listIconFoodHome = mutableListOf(
            IconFood(R.drawable.home_iconfood_hamburger1, "Hamburger","burger"),
            IconFood(R.drawable.home_iconfood_pizaa1, "Pizza","pizza"),
            IconFood(R.drawable.home_iconfood_pho1, "Phở","pho"),
            IconFood(R.drawable.home_iconfood_thit_nuong1, "Thịt nướng","thitnuong"),
            IconFood(R.drawable.home_iconfood_ga_ran1, "Gà rán","garan"),
            IconFood(R.drawable.home_iconfood_banh_ngot1, "Bánh ngọt","banhngot"),
            IconFood(R.drawable.home_iconfood_do_uong1, "Đồ uống","douong"),
            IconFood(R.drawable.home_iconfood_rau1, "Món chay","chay")
        )
        val adapter= AdapterIconFoodHome(listIconFoodHome, object : OnItemClickListener_IconMonAn_Home{
            override fun onItemClick(pos: Int) {
                val item= listIconFoodHome[pos]
                    Toast.makeText(context,"Bạn vừa click vào ${listIconFoodHome[pos].title}",Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).setNavagationBarBottom(false)

                val action= HomeFragmentDirections.actionHomeFragmentToFragmentFoodIcon(categoryType = item.categoryType, nameFood = item.title)
                findNavController().navigate(action)
            }
        })

        recyclerViewIconFood.layoutManager = GridLayoutManager(context, 4)
        recyclerViewIconFood.adapter = adapter
        val spacing = resources.getDimensionPixelSize(R.dimen.spacing_14) // spacing_10 = 10dp
        recyclerViewIconFood.addItemDecoration(GridSpacingItemDecoration(4, spacing, true))
        recyclerViewIconFood.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom = 55
            }
        })
    }
}
