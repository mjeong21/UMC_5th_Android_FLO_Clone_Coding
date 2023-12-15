package com.example.flo.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumsRetrofitInterfaces {
    @GET("/albums/{albumIdx}")
    fun getAlbums(@Path(value = "albumIdx") albumidx : Int): Call<AlbumResponse>
}