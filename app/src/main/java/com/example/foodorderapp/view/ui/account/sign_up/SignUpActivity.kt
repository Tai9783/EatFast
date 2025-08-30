package com.example.foodorderapp.view.ui.account.sign_up

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderapp.R
import com.example.foodorderapp.databinding.ActivitySignUpBinding
import com.example.foodorderapp.model.InforUser
import com.example.foodorderapp.repository.AuthReposity
import com.example.foodorderapp.repository.FirebaseRepositoryGetUserMaxId
import com.example.foodorderapp.repository.FirebaseReposityGetInforUser
import com.example.foodorderapp.utils.Delay
import com.example.foodorderapp.view.ui.account.dialog.FragmentDialogLogin
import com.example.foodorderapp.view.ui.account.login.LoginActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var loadingDialog: FragmentDialogLogin? = null
    private var startTime: Long = 0 // Thời điểm bắt đầu hiển thị dialog
    private val MIN_DISPLAY_TIME = 5000L
   private var delayFinish: Boolean=false
    private var signUpFinsh: Boolean=false
    private var signUpSuccess: Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val defaultPadding= resources.getDimensionPixelSize(R.dimen.default_padding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_SignUp)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + defaultPadding,
                systemBars.top + defaultPadding,
                systemBars.right +defaultPadding,
                systemBars.bottom+defaultPadding
            )
            insets
        }
        binding.txtDangNhap.setOnClickListener {
            val i =Intent(this,LoginActivity::class.java)
            startActivity(i)
        }
        binding.txtDang.setOnClickListener {
            delayFinish= false
            signUpFinsh=false
            signUpSuccess=false
            val fullName = binding.edtTenDK.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtMatKhau.text.toString()
            val password2 = binding.edtXacNhanMK.text.toString()
            if(!isCheckEmail(email)){
                Toast.makeText(this,"Email không đúng định dạng!",Toast.LENGTH_SHORT).show()
                binding.edtEmail.setText("")
                return@setOnClickListener
            }
            if(!isCheckStrongPassword(password.trim())){
                Toast.makeText(this,"Mật khẩu bao gồm kí tự viết hoa,thường,số và kí tự đặc biệt",Toast.LENGTH_LONG).show()
                binding.edtMatKhau.setText("")
                binding.edtXacNhanMK.setText("")
                return@setOnClickListener
            }
            if(password!= password2){
                Toast.makeText(this,"Mật khẩu không khớp.Vui long Nhập lại!",Toast.LENGTH_SHORT).show()
                binding.edtMatKhau.setText("")
                binding.edtXacNhanMK.setText("")
                return@setOnClickListener
            }
            if(fullName.isEmpty()|| email.isEmpty()||password.isEmpty()||password2.isEmpty()){
                Toast.makeText(this,"Vui lòng nhập đủ hết các trường!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }




            /*FirebaseReposityGetInforUser().getInforUser(email) { user ->
                showLoadingDialog()
                Delay().runAfterMinDelay(startTime,MIN_DISPLAY_TIME){
                    delayFinish=true
                    checkDoneAndProceed()
                }

                val userRepo= FirebaseRepositoryGetUserMaxId()
                if (user.email!=email) {
                    userRepo.getUserMaxId { userId ->

                        val item = InforUser(
                            full_name = fullName,
                            email = email,
                            user_id = userId
                        )
                        AuthReposity().registerUser(password,item) { result ->
                                signUpFinsh= true
                                signUpSuccess=result
                                checkDoneAndProceed()
                        }
                    }
                }
                else{
                    signUpFinsh= true
                    return@getInforUser
                }
            }
*/
            dangky(email,fullName,password)
        }
    }
    fun dangky(email: String, fullName: String, pass: String){
        FirebaseReposityGetInforUser().getInforUser(email) { user ->
            showLoadingDialog()
            Delay().runAfterMinDelay(startTime,MIN_DISPLAY_TIME){
                delayFinish=true
                checkDoneAndProceed()
            }

            val userRepo= FirebaseRepositoryGetUserMaxId()
            if (user.email!=email) {
                userRepo.getUserMaxId { userId ->

                    val item = InforUser(
                        full_name = fullName,
                        email = email,
                        user_id = userId
                    )
                    AuthReposity().registerUser(pass,item) { result ->
                        signUpFinsh= true
                        signUpSuccess=result
                        checkDoneAndProceed()
                    }
                }
            }
            else{
                signUpFinsh= true
                return@getInforUser
            }
        }

    }

    private fun checkDoneAndProceed(){
        if(delayFinish && signUpFinsh){
            hideLoadingDialog()
            if(signUpSuccess){
                Toast.makeText(this,"Đăng ký thành công",Toast.LENGTH_SHORT).show()
                goToLogin()
            }
            else{
                Toast.makeText(this,"Email đã tồn tại!",Toast.LENGTH_SHORT).show()
                binding.edtEmail.setText("")
                binding.edtMatKhau.setText("")
                binding.edtXacNhanMK.setText("")
                return
            }
        }
    }

    private fun goToLogin(){
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }
    private fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = FragmentDialogLogin()
        }
        if (!loadingDialog!!.isAdded) {
            loadingDialog!!.show(supportFragmentManager, "LoadingDialog")
            startTime = System.currentTimeMillis() // Ghi lại thời điểm dialog bắt đầu hiển thị
        }
    }

    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    private fun isCheckEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isCheckStrongPassword(pass: String ): Boolean{
        val passwordPattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{5,}$"
        return Regex(passwordPattern).matches(pass)
    }
}