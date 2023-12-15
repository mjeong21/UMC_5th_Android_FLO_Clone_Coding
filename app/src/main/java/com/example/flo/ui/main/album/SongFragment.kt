package com.example.flo.ui.main.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.data.remote.AlbumResponse
import com.example.flo.data.remote.AlbumResult
import com.example.flo.data.remote.AlbumService
import com.example.flo.databinding.FragmentSongBinding

class SongFragment(private val albumIdx: Int?) : Fragment(), AlbumView {

    lateinit var binding: FragmentSongBinding
    lateinit var albumsongAdapter: AlbumSongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)
        binding.songMixOffIv.setOnClickListener {
            setMixStatus(false)
        }
        binding.songMixOnIv.setOnClickListener {
            setMixStatus(true)
        }

        getAlbums()
        return binding.root
    }
    private fun setMixStatus(isMix : Boolean) {
        if(isMix) {
            binding.songMixOffIv.visibility = View.VISIBLE
            binding.songMixOnIv.visibility=View.GONE
        }
        else {
            binding.songMixOffIv.visibility = View.GONE
            binding.songMixOnIv.visibility=View.VISIBLE
        }
    }

    private fun getAlbums() {
        val albumService = AlbumService()
        albumService.setAlbumView(this)

        albumService.getAlbums(albumIdx!!)
    }

    override fun onGetSongLoading() {
        binding.lookLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetAlbumSuccess(code: Int, result: ArrayList<AlbumResult>, albumResponse: AlbumResponse) {
        binding.lookLoadingPb.visibility = View.GONE
        initRecyclerView(result, albumResponse)
    }

    override fun onGetAlbumFailure() {
        binding.lookLoadingPb.visibility = View.GONE
    }

    private fun initRecyclerView(result: ArrayList<AlbumResult>, albumResponse: AlbumResponse) {
        albumsongAdapter = AlbumSongRVAdapter(requireContext(), result, albumResponse)

        binding.albumSongRv.adapter = albumsongAdapter
    }
}