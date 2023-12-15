package com.example.flo.data.remote

import android.util.Log
import com.example.flo.ui.main.home.HomeView
import com.example.flo.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumListService() {
    private lateinit var homeView: HomeView

    fun setHomeView(homeView: HomeView) {
        this.homeView = homeView
    }

    fun getAlbums() {
        val albumService = getRetrofit().create(AlbumRetrofitInterfaces::class.java)

        homeView.onGetAlbumLoading()

        albumService.getAlbums().enqueue(object : Callback<AlbumListResponse>{
            override fun onResponse(call: Call<AlbumListResponse>, response: Response<AlbumListResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val albumResponse : AlbumListResponse = response.body()!!

                    Log.d("ALBUM-RESPONSE", albumResponse.toString())

                    when(val code = albumResponse.code) {
                        1000 -> {
                            homeView.onGetAlbumSuccess(code, albumResponse.result!!, albumResponse.result.albums)
                        }
                        else -> homeView.onGetAlbumFailure(code, albumResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<AlbumListResponse>, t: Throwable) {
                homeView.onGetAlbumFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
    }
}