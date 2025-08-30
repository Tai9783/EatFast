package com.example.foodorderapp.view.ui.account.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility
import com.example.foodorderapp.R
import com.example.foodorderapp.databinding.ActivityLoginBinding
import com.example.foodorderapp.repository.AuthReposity
import com.example.foodorderapp.repository.FirebaseReposityGetInforUser
import com.example.foodorderapp.utils.Delay
import com.example.foodorderapp.view.MainActivity
import com.example.foodorderapp.view.ui.account.dialog.FragmentDialogLogin
import com.example.foodorderapp.view.ui.account.sign_up.SignUpActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var loadingDialog: FragmentDialogLogin? = null
    private val handler = Handler(Looper.getMainLooper()) // Handler để xử lý delay
    private var startTime: Long = 0 // Thời điểm bắt đầu hiển thị dialog
    private val MIN_DISPLAY_TIME = 3000L

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == RESULT_OK && data != null) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                val account = task.result
                val idToken = account.idToken
                if (idToken != null) {
                    AuthReposity().firebaseAuthWithGoogle(idToken){isLoginFinish,email->
                        if(isLoginFinish){
                            goToMain(email)
                        }
                        else{
                            Toast.makeText(this,"Đăng nhập bằng google thất bại",Toast.LENGTH_SHORT).show()
                        }

                    }
                } else {
                    Toast.makeText(this, "Không lấy được ID Token", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        // Cấu hình Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // lấy từ google-services.json
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        val defaultPadding = resources.getDimensionPixelSize(R.dimen.default_padding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_Login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + defaultPadding,
                systemBars.top + defaultPadding,
                systemBars.right + defaultPadding,
                systemBars.bottom + defaultPadding
            )
            insets
        }
        //nút đăng ký
        binding.txtDangKy.setOnClickListener {
            val i = Intent(this, SignUpActivity::class.java)
            startActivity(i)
        }
        //nút đăng nhập
        binding.txtDangNhap.setOnClickListener {
            xulyDangNhap()
        }
        //xử lý ẩn hiện mật khẩu
        var isPasswordVisible = false
        binding.iconAn.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.edtMatKhau.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.iconAn.setImageResource(R.drawable.icon_visibility)
            } else {
                binding.edtMatKhau.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.iconAn.setImageResource(R.drawable.icon_visibility_off)
            }
            binding.edtMatKhau.setSelection(binding.edtMatKhau.text.length)
        }
        //nút quên mật khẩu
        binding.txtQuenMatKhau.setOnClickListener {
            val i =Intent(this,ResetPasswordActivity::class.java)
            startActivity(i)
        }
        // Xử lý tính năng đăng nhập bằng google
        binding.btnLoginWithGoogle.setOnClickListener {
            binding.loadingOverlay.visibility= View.VISIBLE
            Toast.makeText(this,"Bạn vừa click vào đăng nhập bằng google",Toast.LENGTH_SHORT).show()
            val signInIntent = googleSignInClient.signInIntent
            signInLauncher.launch(signInIntent)
        }
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
    private  fun goToMain(email: String){
        val i2 = Intent(this, MainActivity::class.java)
        i2.putExtra("email", email)
        startActivity(i2)
        binding.loadingOverlay.visibility= View.GONE
        finish()
    }


    private var isDelayFinish = false
    private var isLoginFinish = false
    private var loginSuccess = false
    private var userLoggedIn = ""

    private fun xulyDangNhap() {
        val email= binding.edtEmail.text.toString()
        val password=binding.edtMatKhau.text.toString()
        isDelayFinish = false
        isLoginFinish = false
        loginSuccess = false
        userLoggedIn = ""

        if(!isCheckEmail(email)){
            Toast.makeText(this,"Email không đúng định dạng",Toast.LENGTH_SHORT).show()
            binding.edtEmail.setText("")
            binding.edtMatKhau.setText("")
            return
        }

        showLoadingDialog()

        Delay().runAfterMinDelay(startTime,MIN_DISPLAY_TIME) {
            isDelayFinish = true
            checkDoneAndProceed()
        }

           AuthReposity().login(email,password) { mail,ischeck ->
            if (ischeck) {
                loginSuccess = true
                userLoggedIn = mail
            }
            isLoginFinish = true
            checkDoneAndProceed()
        }
    }

    private fun checkDoneAndProceed() {
        if (isDelayFinish && isLoginFinish) {
            hideLoadingDialog()
            if (loginSuccess) {
                goToMain(userLoggedIn)
            } else {
                Toast.makeText(this, "Email hoặc mật khẩu không đúng. Vui lòng xem lại!", Toast.LENGTH_SHORT).show()
                binding.edtEmail.setText("")
                binding.edtMatKhau.setText("")
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        // Đảm bảo hủy bỏ mọi callback đang chờ trong handler
        handler.removeCallbacksAndMessages(null)
        loadingDialog?.let {
            if (it.isAdded) {
                it.dismissAllowingStateLoss()
            }
            loadingDialog = null
        }
    }
    private fun isCheckEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
