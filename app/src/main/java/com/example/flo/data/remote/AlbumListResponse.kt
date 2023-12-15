package com.example.flo.data.remote

import com.google.gson.annotations.SerializedName

data class AlbumListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PodcastResult
)
data class PodcastResult (
    @SerializedName("albums") val albums: ArrayList<PodcastAlbums>
)

data class PodcastAlbums (
    @SerializedName("albumIdx") val albumIdx: Int,
    @SerializedName("singer") val singer: String,
    @SerializedName("title") val title: String,
    @SerializedName("coverImgUrl") val coverImgUrl: String
)