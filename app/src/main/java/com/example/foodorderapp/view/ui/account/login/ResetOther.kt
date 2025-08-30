package com.example.foodorderapp.view.ui.account.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderapp.R
import com.example.foodorderapp.databinding.ActivityResetOtherBinding

class ResetOther : AppCompatActivity() {
    private lateinit var binding: ActivityResetOtherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityResetOtherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val defaultPadding = resources.getDimensionPixelSize(R.dimen.default_padding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.resetOther)) { v, insets ->
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
            val i =Intent(this,ResetPasswordActivity::class.java)
            startActivity(i)
        }
        binding.txtQuaylai.setOnClickListener {
            val i2= Intent(this,LoginActivity::class.java)
            startActivity(i2)
        }
    }
}