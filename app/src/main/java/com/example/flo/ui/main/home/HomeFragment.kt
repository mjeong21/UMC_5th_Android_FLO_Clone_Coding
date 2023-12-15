package com.example.flo.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.ui.main.MainActivity
import com.example.flo.R
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.entities.Album
import com.example.flo.data.remote.AlbumListService
import com.example.flo.data.remote.PodcastAlbums
import com.example.flo.data.remote.PodcastResult
import com.example.flo.databinding.FragmentHomeBinding
import com.example.flo.ui.main.album.AlbumFragment
import me.relex.circleindicator.CircleIndicator3



class HomeFragment : Fragment(), HomeView {

    lateinit var binding: FragmentHomeBinding
    lateinit var podcastAdapter: PodcastRVAdapter
    private val albumDatas = ArrayList<Album>()

    // arraylist 선언
//    private var albumDatas = ArrayList<Album>()
    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums())


//        binding.homePannelAlbum3ImgIv.setOnClickListener {
//            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, AlbumFragment()).commitAllowingStateLoss()
//
//            //album값 데이터 AlbumFragment로 넘기기
//            val albumTitle=binding.homePannelAlbum3TitleTv.text.toString()
//            val albumSinger=binding.homePannelAlbum3SingerTv.text.toString()
//            var albumfragment=AlbumFragment()
//            var bundle=Bundle()
//            bundle.putString("albumtitle", albumTitle)
//            bundle.putString("albumsinger", albumSinger)
//            albumfragment.arguments=bundle
//
//            activity?.supportFragmentManager!!.beginTransaction()
//                .replace(R.id.main_frm, albumfragment)
//                .commit()
//        }

//        // 데이터 리스트 생성 더미 데이터
//        albumDatas.apply {
//            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp, ArrayList<Song>().apply {
//                add(Song("노래1","가수1",0,10,false,"music_boy", R.drawable.img_album_exp))
//                add(Song("SOS","SZA",0,10,false,"music_flu", R.drawable.img_album_exp3))
//            }))
//            add(Album("LILAC", "아이유 (IU)", R.drawable.img_album_exp2, ArrayList<Song>().apply {
//                add(Song("노래2","가수2",0,10,false,"music_lilac", R.drawable.img_album_exp2))
//                add(Song("SOS","SZA",0,10,false,"music_flu", R.drawable.img_album_exp3))
//            }))
//            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3, ArrayList<Song>().apply {
//                add(Song("노래3","가수3",0,10,false,"music_flu", R.drawable.img_album_exp4))
//                add(Song("SOS","SZA",0,10,false,"music_next", R.drawable.img_album_exp3))
//            }))
//            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4, ArrayList<Song>().apply {
//                add(Song("노래4","가수4",0,10,false,"music_bboom", R.drawable.img_album_exp3))
//                add(Song("SOS","SZA",0,10,false,"music_next", R.drawable.img_album_exp3))
//            }))
//            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5, ArrayList<Song>().apply {
//                add(Song("노래5","가수5",0,10,false,"music_boy", R.drawable.img_album_exp6))
//                add(Song("SOS","SZA",0,10,false,"music_butter", R.drawable.img_album_exp3))
//            }))
//            add(Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6, ArrayList<Song>().apply {
//                add(Song("노래6","가수6",0,10,false,"music_butter", R.drawable.img_album_exp5))
//                add(Song("SOS","SZA",0,10,false,"music_lilac", R.drawable.img_album_exp3))
//            }))
//        }

        val intent = Intent(this.context, MainActivity::class.java)
        // RecyclerView 어댑터 연결
        val albumRVAdapter = AlbumRVAdapter()
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // 메소드에 객체 전달
        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album) {
                changedAlbumFragment(album)
            }

            override fun onPlayItem(album: Album) {
                if(activity is MainActivity) {
                    val activity = activity as MainActivity
//                    val songs = songDB.songDao().getSongsInAlbum(album.id)
//                    activity.setMiniPlayer(songs.first()) // 첫 번째 수록곡 데이터 넘기기
                    activity.clickAlbumPlay(album)
                }
            }
        })

        albumRVAdapter.addAlbums(songDB.albumDao().getAlbums() as ArrayList<Album>)

        // banner viewpager
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment.newInstance(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment.newInstance(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment.newInstance(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment.newInstance(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL //좌우로 스크롤 될 수 있는 코드

        val panelAdapter = PanelVPAdapter(this)

        val indicator: CircleIndicator3 = binding.homeIndicator
        indicator.setViewPager(binding.homePannelVp)

        panelAdapter.addFragment(PanelFragment.newInstance(R.drawable.img_first_album_default))
        panelAdapter.addFragment(PanelFragment.newInstance(R.drawable.panel_background))
        panelAdapter.addFragment(PanelFragment.newInstance(R.drawable.img_first_album_default))
        binding.homePannelVp.adapter = panelAdapter // viewpager에 연결
        binding.homePannelVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.homeIndicator.setViewPager(binding.homePannelVp)   //indicator 연결

        getAlbums()

        return binding.root
    }

    private fun changedAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
//                    val gson = Gson()
//                    val albumJson = gson.toJson(album)
//                    putString("album", albumJson)
                    putInt("albumId", album.id)
                }
            })
            .commitAllowingStateLoss()
    }

    private fun getAlbums() {
        val albumListService = AlbumListService()
        albumListService.setHomeView(this)

        albumListService.getAlbums()
    }

    override fun onGetAlbumLoading() {
        binding.homeLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetAlbumSuccess(code: Int, result: PodcastResult, albums: ArrayList<PodcastAlbums>) {
        binding.homeLoadingPb.visibility = View.GONE
        initRecyclerView(result, albums)
    }

    override fun onGetAlbumFailure(code: Int, message: String) {
        binding.homeLoadingPb.visibility = View.GONE
        Log.d("HOME/ALBUM-RESPONSE", message)
    }

    private fun initRecyclerView(result: PodcastResult, albums: ArrayList<PodcastAlbums>) {
        podcastAdapter = PodcastRVAdapter(requireContext(), result)
        binding.homePodcastAlbumRv.adapter = podcastAdapter

        podcastAdapter.setMyItemClickListener(object: PodcastRVAdapter.MyItemClickListener {
            override fun onItemClick(position: Int) {
                changedAlbumFragment2(position, albums)
            }
        })

    }
    private fun changedAlbumFragment2(position: Int, albums: ArrayList<PodcastAlbums>) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    putInt("server_albumId", albums[position].albumIdx)
                    putString("server_title", albums[position].title)
                    putString("server_singer", albums[position].singer)
                    putString("server_ImgUrl", albums[position].coverImgUrl)
                }
            })
            .commitAllowingStateLoss()
//
//        var songFragment = SongFragment()
//
//        var bundle = Bundle()
//        bundle.putInt("albumidx", albums[position].albumIdx)
//        songFragment.arguments = bundle
//
//        val transaction = supportFragmentManager.beginTran
    }
}