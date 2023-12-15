package com.example.flo.ui.main.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.flo.ui.main.home.HomeFragment
import com.example.flo.ui.main.MainActivity
import com.example.flo.R
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Like
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment() : Fragment() {
    lateinit var binding :FragmentAlbumBinding
//    private var gson: Gson = Gson()

//    private val songs = arrayListOf<Song>()
    lateinit var albumDB: SongDatabase
    lateinit var album: Album


    var nowPos = 0

    private val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)


//        val album = gson.fromJson(albumId, Album::class.java)

        var albumId = arguments?.getInt("albumId")

        if(albumId == 0) {
            initServerAlbum()
        } else {
            initAlbum()
        }

        isLiked = isLikedAlbum(album.id)

        setInit(album)
        setOnClickListeners(album)

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

//        val title=arguments?.getString("albumtitle")
//        val singer=arguments?.getString("albumsinger")
//        binding.albumMusicTitleTv1.text = title
//        binding.albumSingerTv1.text = singer
        var albumIdx = arguments?.getInt("server_albumId")

        val albumAdapter = AlbumVPAdapter(this, albumIdx!!)
        binding.albumContentVp.adapter = albumAdapter

        //tablayout을 viewpager2와 연결하는 중재자
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {
                tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }
    private fun initAlbum() {
        albumDB = SongDatabase.getInstance(requireContext())!!
        var albumId = arguments?.getInt("albumId")
        nowPos = albumId!!

        album = albumDB.albumDao().getAlbum(nowPos)

    }
    private fun initServerAlbum() {
        var albumTitle = arguments?.getString("server_title")
        var albumSinger = arguments?.getString("server_singer")
        var albumImgUrl = arguments?.getString("server_ImgUrl")

        binding.albumMusicTitleTv1.text = albumTitle
        binding.albumSingerTv1.text = albumSinger

        // 받아온 커버 이미지 아이템에서 바인딩한 커버 이미지에 적용
        Glide.with(requireContext()).load(albumImgUrl).into(binding.albumImageIv)

        album = Album(0, albumTitle, albumSinger, null)

    }
    private fun setInit(album: Album) {
        if (album.coverImg != null) {
            binding.albumImageIv.setImageResource(album.coverImg!!)
        }

        binding.albumMusicTitleTv1.text = album.title.toString()
        binding.albumSingerTv1.text = album.singer.toString()

        if(isLiked) {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("userIdx", 0) // sharedPreferences에서 가져온 값이 없다면 0 반환
    }

    private fun likeAlbum(userId : Int, albumId : Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId: Int): Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likeId : Int? = songDB.albumDao().isLikeAlbum(userId, albumId)

        // 앨범을 좋아요 하면 likeId가 null이 아님 -> return true
        // 앨범을 좋아요 하지 않으면 null임 -> return false
        return likeId != null
    }

    // 사용자가 좋아요 누른 것을 해제하면 liketable에서 해당 userid와 albumid를 같이 지움
    private fun disLikedAlbum(albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        songDB.albumDao().disLikeAlbum(userId, albumId)
    }

    private fun setOnClickListeners(album: Album) {
        val userId = getJwt()
        binding.albumLikeIv.setOnClickListener {
            if (isLiked) {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(album.id)
                isLiked = false
            } else {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
                Log.d("ALBUM/UserId", userId.toString())
                isLiked = true
            }
        }
    }


}