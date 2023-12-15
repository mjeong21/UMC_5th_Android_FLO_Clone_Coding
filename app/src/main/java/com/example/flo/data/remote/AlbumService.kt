package com.example.flo.data.remote

import android.util.Log
import com.example.flo.ui.main.album.AlbumView
import com.example.flo.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumService() {
    private lateinit var albumView: AlbumView

    fun setAlbumView(albumView: AlbumView) {
        this.albumView = albumView
    }

    fun getAlbums(albumidx: Int) {
        val albumService = getRetrofit().create(AlbumsRetrofitInterfaces::class.java)

        albumView.onGetSongLoading()

        albumService.getAlbums(albumidx).enqueue(object : Callback<AlbumResponse> {
            override fun onResponse(call: Call<AlbumResponse>, response: Response<AlbumResponse>) {

                if (response.isSuccessful && response.code() == 200) {
                    val albumResponse : AlbumResponse = response.body()!!

                    Log.d("ALBUMSONG", albumResponse.toString())

                    when(val code = albumResponse.code) {
                        1000 -> {
                            albumView.onGetAlbumSuccess(code, albumResponse.result!!, albumResponse)
                        }
                        else -> albumView.onGetAlbumFailure()
                    }
                }
            }

            override fun onFailure(call: Call<AlbumResponse>, t: Throwable) {
                Log.d("AlbumSong/Fail", t.message.toString())
            }

        })
    }
}