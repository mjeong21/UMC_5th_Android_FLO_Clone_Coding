package com.example.flo.data.remote

import com.google.gson.annotations.SerializedName

data class AlbumResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<AlbumResult>
)
data class AlbumResult (
    @SerializedName("songIdx") val songIdx: Int,
    @SerializedName("title") val title: String,
    @SerializedName("singer") val singer: String,
    @SerializedName("isTitleSong") val isTitleSong: String
)