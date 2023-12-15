package com.example.flo.ui.main.home

import com.example.flo.data.remote.PodcastAlbums
import com.example.flo.data.remote.PodcastResult

interface HomeView {
    fun onGetAlbumLoading()
    fun onGetAlbumSuccess(code: Int, result: PodcastResult, albums: ArrayList<PodcastAlbums>)
    fun onGetAlbumFailure(code: Int, message: String)
}