package com.example.flo.ui.main.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.entities.Song
import com.example.flo.databinding.FragmentSavedBinding

class SavedFragment : Fragment() {
    lateinit var binding : FragmentSavedBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

//        songDatas.apply {
//            add(Song("Butter", "방탄소년단 (BTS)", 0, 0, false, "music_lilac", R.drawable.img_album_exp))
//            add(Song("LILAC", "아이유 (IU)", 0, 0, false, "music_lilac", R.drawable.img_album_exp2))
//            add(Song("Next Level", "에스파 (AESPA)", 0, 0, false, "music_lilac", R.drawable.img_album_exp3))
//            add(Song("Boy with Luv", "방탄소년단 (BTS)", 0, 0, false, "music_lilac", R.drawable.img_album_exp4))
//            add(Song("BBoom BBoom", "모모랜드 (MOMOLAND)",  0, 0, false, "music_lilac", R.drawable.img_album_exp5))
//            add(Song("Weekend", "태연 (Tae Yeon)", 0, 0, false, "music_lilac", R.drawable.img_album_exp6))
//            add(Song("Butter", "방탄소년단 (BTS)", 0, 0, false, "music_lilac", R.drawable.img_album_exp))
//            add(Song("LILAC", "아이유 (IU)", 0, 0, false, "music_lilac", R.drawable.img_album_exp2))
//            add(Song("Next Level", "에스파 (AESPA)", 0, 0, false, "music_lilac", R.drawable.img_album_exp3))
//            add(Song("Boy with Luv", "방탄소년단 (BTS)", 0, 0, false, "music_lilac", R.drawable.img_album_exp4))
//            add(Song("BBoom BBoom", "모모랜드 (MOMOLAND)", 0, 0, false, "music_lilac", R.drawable.img_album_exp5))
//            add(Song("Weekend", "태연 (Tae Yeon)", 0, 0, false, "music_lilac", R.drawable.img_album_exp6))
//        }
//        val view = inflater.inflate(R.layout.dialog, container, false)


        return binding.root
    }


    override fun onStart() {
        super.onStart()
        initRecyclerView()
    }
    private fun initRecyclerView() {
        // RecyclerView 어댑터 연결
        val savedRVAdapter = SavedRVAdapter()
        binding.savedAlbumRv.adapter = savedRVAdapter
        binding.savedAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        savedRVAdapter.setItemClickListener(object: SavedRVAdapter.ItemClickListener {
            // [...]을 눌렀을 때 좋아요 목록에서 삭제
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
            }
        })

        // true라면 보관함에 곡이 추가
        savedRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)

    }


}