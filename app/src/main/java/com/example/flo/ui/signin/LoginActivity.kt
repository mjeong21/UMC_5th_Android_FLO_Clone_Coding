package com.example.flo.ui.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.ui.main.MainActivity
import com.example.flo.ui.signup.SignUpActivity
import com.example.flo.data.entities.User
import com.example.flo.data.remote.AuthService
import com.example.flo.data.remote.Result
import com.example.flo.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), LoginView {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignupTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginSigninTv.setOnClickListener {
            login()
        }
    }
    private fun login() {
//        if (binding.loginIdEt.text.toString().isEmpty() || binding.loginAddressEt.text.toString().isEmpty()) {
//            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (binding.loginPasswordEt.text.toString().isEmpty()) {
//            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
        val email: String = binding.loginIdEt.text.toString() + "@" + binding.loginAddressEt.text.toString()
        val password : String = binding.loginPasswordEt.text.toString()

//        val songDB = SongDatabase.getInstance(this)!!
//        val user = songDB.userDao().getUser(email, pwd)
//
//        // user가 null이 아니면 실행
//        user?.let {
//            Log.d("LOGIN_ACT/GET_USER", "userId: ${user.id}, $user")
//            //saveJwt(user.id)
//            startMainActivity()
//            Toast.makeText(this, "성공적으로 로그인했습니다.", Toast.LENGTH_SHORT).show()
//        }
//
//        // user가 null이면 회원 정보가 없는 것
//        if(user == null) {
//            Toast.makeText(this, "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
//        }

        val authService = AuthService()
        authService.setLoginView(this)

        authService.login(User(email, password, ""))

    }

//    private fun saveJwt(jwt: Int) {
//        val spf = getSharedPreferences("auth", MODE_PRIVATE)
//        val editor = spf.edit()
//
//        editor.putInt("jwt", jwt)
//        editor.apply()
//    }

    private fun saveJwt(jwt: String, userIdx: Int) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.putInt("userIdx", userIdx)
        editor.apply()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onLoginSuccess(code: Int, result: Result) {
        when(code) {
            1000 -> {
                saveJwt(result.jwt, result.userIdx)
                Log.d("useridx", result.userIdx.toString())
                startMainActivity()
            }
        }
    }

    override fun onLoginFailure(msg: String) {
        // 실패 처리
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    }
}