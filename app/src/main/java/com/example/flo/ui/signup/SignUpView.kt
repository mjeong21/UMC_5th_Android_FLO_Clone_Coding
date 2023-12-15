package com.example.flo.ui.signup

// activity와 AuthService를 연결시켜주기 위한 것
interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure(message: String)
}