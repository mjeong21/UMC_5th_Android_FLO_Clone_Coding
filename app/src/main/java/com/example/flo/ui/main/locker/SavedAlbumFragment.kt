package com.example.flo.ui.main.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.data.local.SongDatabase
import com.example.flo.databinding.FragmentSavedalbumBinding

class SavedAlbumFragment : Fragment() {
    lateinit var binding: FragmentSavedalbumBinding
    lateinit var albumDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedalbumBinding.inflate(inflater, container, false)

        albumDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview() {
        // RecyclerView 어댑터 연결
        val albumRVAdapter = AlbumLockerRVAdapter()
        binding.lockerSavedSongRv.adapter = albumRVAdapter
        binding.lockerSavedSongRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        albumRVAdapter.setMyItemClickListener(object : AlbumLockerRVAdapter.MyItemClickListener {
            override fun onRemoveSong(albumId: Int) {
                albumDB.albumDao().getLikedAlbums(getJwt())
                albumDB.albumDao().disLikeAlbum(getJwt(), albumId)
            }
        })

        albumRVAdapter.addAlbums(albumDB.albumDao().getLikedAlbums(getJwt()) as ArrayList)

    }
    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("userIdx", 0) // sharedPreferences에서 가져온 값이 없다면 0 반환
    }
}