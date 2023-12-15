package com.example.flo.data.remote

import android.util.Log
import com.example.flo.ui.signin.AutoLoginView
import com.example.flo.ui.signin.LoginView
import com.example.flo.ui.signup.SignUpView
import com.example.flo.data.entities.User
import com.example.flo.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView
    private lateinit var autologinView: AutoLoginView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun setAutoLoginView(autologinView: AutoLoginView) {
        this.autologinView = autologinView
    }

    fun signUp(user : User) {

        // 어떤 주소로 입력함
        // RetroFit 객체를 이용해서 API 연결을 할 수 있는 객체 생성
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        // 입력한 주소 중에 하나로 연결 시도
        // 비동기적으로 작동해야 하기 때문에 queue에 넣음
        authService.signUp(user).enqueue(object: Callback<AuthResponse> {

            // 어떠한 응답을 받았을 때
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//                Log.d("SIGNUP/SUCCESS", response.toString())

                // body: 정보가 들어있는 곳, 정보를 꺼냄
                // AuthResponse 데이터 클래스에 있는 내용을 가져옴
                val resp: AuthResponse = response.body()!!
                when(resp.code) {
                    1000 -> {
                        signUpView.onSignUpSuccess()
                        Log.d("SIGNUP/SUCCESS", resp.toString())
                    }
                    else -> signUpView.onSignUpFailure(resp.message)
                }
            }

            // 서버와 연결을 실패했을 때
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE", t.message.toString())
            }
        })

        Log.d("SIGNUP", "HELLO")
    }

    fun login(user : User) {

        // Retrofit 객체와 Service 객체를 생성, API 호출
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.login(user).enqueue(object: Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGIN/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!
                when(val code = resp.code) {
                    1000 -> loginView.onLoginSuccess(code, resp.result!!)
                    else -> loginView.onLoginFailure(resp.message)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGIN/FAILURE", t.message.toString())
            }
        })

    }

    fun autologin(accessToken: String?) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.autologin(accessToken).enqueue(object: Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val resp: AuthResponse = response.body()!!
                when(resp.code) {
                    1000 -> autologinView.onAutoLoginSuccess()
                    else -> autologinView.onAutoLoginFailure()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("AUTOLOGIN/FAILURE", t.message.toString())
            }
        })
    }
}