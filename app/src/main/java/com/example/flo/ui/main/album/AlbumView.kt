package com.example.flo.ui.main.album

import com.example.flo.data.remote.AlbumResponse
import com.example.flo.data.remote.AlbumResult

interface AlbumView {
    fun onGetSongLoading()
    fun onGetAlbumSuccess(code: Int, result: ArrayList<AlbumResult>, albumResponse: AlbumResponse)
    fun onGetAlbumFailure()
}