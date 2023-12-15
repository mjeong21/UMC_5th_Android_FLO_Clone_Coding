package com.example.flo.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.entities.User
import com.example.flo.data.remote.AuthService
import com.example.flo.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity(), SignUpView {
    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupSignupTv.setOnClickListener {
            signUp()
        }
    }

    private fun getUser() : User {
        val email: String = binding.signupIdEt.text.toString() + "@" + binding.signupAddressEt.text.toString()
        val pwd : String = binding.signupPasswordEt.text.toString()
        var name: String = binding.signupNameEt.text.toString()

        return User(email, pwd, name)
    }

//    private fun signUp() {
//        if (binding.signupIdEt.text.toString().isEmpty() || binding.signupAddressEt.text.toString().isEmpty()) {
//            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (binding.signupPasswordEt.text.toString() != binding.signupPasswordCheckEt.text.toString()) {
//            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (binding.signupPasswordEt.text.toString().isEmpty() || binding.signupPasswordCheckEt.text.toString().isEmpty()) {
//            Toast.makeText(this, "비밀번호가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val userDB = SongDatabase.getInstance(this)!!
//        userDB.userDao().insert(getUser())
//
//        val user = userDB.userDao().getUsers()
//
//        Log.d("SIGNUPACT", user.toString())
//
//    }

    private fun signUp() {
        if (binding.signupIdEt.text.toString().isEmpty() || binding.signupAddressEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.signupNameEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.signupPasswordEt.text.toString() != binding.signupPasswordCheckEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.signupPasswordEt.text.toString().isEmpty() || binding.signupPasswordCheckEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setSignUpView(this)

        authService.signUp(getUser())
    }

    override fun onSignUpSuccess() {
        finish()
    }

    override fun onSignUpFailure(message: String) {
        binding.signUpEmailErrorTv.visibility = View.VISIBLE
        binding.signUpEmailErrorTv.text = message
    }
}