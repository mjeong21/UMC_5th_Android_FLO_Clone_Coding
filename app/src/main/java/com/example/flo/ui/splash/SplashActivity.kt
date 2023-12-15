package com.example.flo.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.ui.signin.AutoLoginView
import com.example.flo.data.remote.AuthService
import com.example.flo.databinding.ActivitySplashBinding
import com.example.flo.ui.main.MainActivity
import com.example.flo.ui.signin.LoginActivity

class SplashActivity : AppCompatActivity(), AutoLoginView {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            autoLogin()
        }, 1000)
    }

    private fun getJwt(): String? {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        return spf.getString("jwt", "") // sharedPreferences에서 가져온 값이 없다면 0 반환
    }

    private fun autoLogin() {
        val authService = AuthService()
        authService.setAutoLoginView(this)

        val token = getJwt()
        authService.autologin(token)
    }

    override fun onAutoLoginSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onAutoLoginFailure() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}