package com.example.flo.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://edu-api-test.softsquared.com" // /를 붙이게 되면 interface부분에서 /를 제외하고 적어줘야함

fun getRetrofit(): Retrofit {

    // 웹 브라우저 창 열기
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit
}
