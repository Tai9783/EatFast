package com.example.foodorderapp.view.ui.account.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderapp.R
import com.example.foodorderapp.databinding.ActivityResetPasswordBinding
import com.example.foodorderapp.repository.AuthReposity

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val defaultPadding = resources.getDimensionPixelSize(R.dimen.default_padding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.resetPassword)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + defaultPadding,
                systemBars.top + defaultPadding,
                systemBars.right + defaultPadding,
                systemBars.bottom + defaultPadding
            )
            insets
        }
        binding.imgThoat.setOnClickListener{
            val i= Intent(this,LoginActivity::class.java)
            startActivity(i)
        }
        binding.txtkhongTheXacThuc.setOnClickListener {
            val i2= Intent(this,ResetOther::class.java)
            startActivity(i2)
        }
        // xử lý quên mật khẩu
        binding.txtXacNhan.setOnClickListener {
            val email= binding.edtEmail.text.toString()
            if(email.isEmpty()){
                Toast.makeText(this,"Email không được bỏ trống",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
          binding.loadingOverlay.visibility = View.VISIBLE
            AuthReposity().resetPassword(email){isDone->
                if (isDone){
                    binding.edtEmail.setText("")
                    Toast.makeText(this,"Vui lòng kiểm tra email để đặt lại mật khẩu",Toast.LENGTH_LONG).show()
                    binding.loadingOverlay.visibility = View.GONE
                    val i= Intent(this,LoginActivity::class.java)
                    startActivity(i)
                }
                else {
                    binding.edtEmail.setText("")
                    Toast.makeText(this, "Gửi email thất bại", Toast.LENGTH_SHORT).show()
                    binding.loadingOverlay.visibility = View.GONE
                }

            }
        }
    }
}