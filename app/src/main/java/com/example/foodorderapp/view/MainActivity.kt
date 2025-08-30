package com.example.foodorderapp.view
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodorderapp.R
import com.example.foodorderapp.databinding.ActivityMainBinding
import com.example.foodorderapp.datasource.DatabaseProvider
import com.example.foodorderapp.repository.FirebaseReposityGetCart
import com.example.foodorderapp.utils.applySystemBarMargin
import com.example.foodorderapp.utils.applySystemBarPadding
import com.example.foodorderapp.view.ui.home.HomeFragment
import com.example.foodorderapp.viewmodel.ShoppingCartViewModelFactory
import com.example.foodorderapp.viewmodel.ViewModelGetInforUser
import com.example.foodorderapp.viewmodel.ViewModelShoppingcart

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModelGetInforUser: ViewModelGetInforUser
    private lateinit var viewModelShoppingcart: ViewModelShoppingcart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNav) { view, insets ->
            val navBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(bottom = navBarInsets.bottom)
            insets
        }
        binding.bottomNav.applySystemBarMargin(applyBottom = true)
        window.statusBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
            }
        }


        // Cho phép layout ăn lên status bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val navController= findNavController(R.id.nav_host_fragment)
        binding.bottomNav.setupWithNavController(navController)

        //lấy email được gửi từ bên đăng nhập qua
        val email= intent.getStringExtra("email")

        //khi đăng nhập thành công thì đồng bộ thông tin giỏ hàng lên Room
        val cartDao=DatabaseProvider.getDatabase(applicationContext).cartDao()
        val foodDao= DatabaseProvider.getDatabase(applicationContext).foodDao()
        val sellerDao=DatabaseProvider.getDatabase(applicationContext).sellerDao()
        val repository= FirebaseReposityGetCart(cartDao,sellerDao,foodDao)
        val factory= ShoppingCartViewModelFactory(repository)
        viewModelShoppingcart= ViewModelProvider(this,factory)[ViewModelShoppingcart::class.java]

        //đưa email lấy được vào Livedata để theo dõi đồng thời dùng repository để lấy thông tin infor đó và cập nhập vào LiveData
        viewModelGetInforUser= ViewModelProvider(this)[ViewModelGetInforUser::class.java]

        if (email != null) {
            if(email.isNotEmpty())
            {
                viewModelGetInforUser.getInforUser(email)
                viewModelGetInforUser.setEmail(email)
                viewModelShoppingcart.initDataShoppingCart(email)
            }

        }
    }
    //ẩn thanh option chứa trang chủ, đơn hàng, giỏ hàng....
    fun setNavagationBarBottom(b: Boolean) {
        binding.bottomNav.visibility= if(b) View.VISIBLE else View.GONE
    }
    fun backHome(){
        setNavagationBarBottom(true)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,HomeFragment())
            .commit()
        binding.bottomNav.selectedItemId=R.id.nav_home
    }

}