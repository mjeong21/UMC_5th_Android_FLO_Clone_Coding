package com.example.flo.ui.main

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.flo.R
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.ui.main.home.HomeFragment
import com.example.flo.ui.main.locker.LockerFragment
import com.example.flo.ui.main.look.LookFragment
import com.example.flo.ui.main.search.SearchFragment
import com.example.flo.ui.song.SongActivity

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

//    private var song: Song = Song()
//    private var gson: Gson = Gson()

//    private var album: Album = Album()

    lateinit var timer: Timer

    // mediaPlayer를 해제하기 위해 nullable로 선언
    private var mediaPlayer: MediaPlayer? = null

    private var songs = arrayListOf<Song>()
    private val albums = ArrayList<Album>()
    private lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_FLO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        inputDummyAlbums()
        initPlaylist()
        initSong()
        initBottomNavigation()

//        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 0, 15, false, "music_lilac")

        binding.mainPlayerCl.setOnClickListener {
//            val intent = Intent(this, SongActivity :: class.java)
//            intent.putExtra("title", song.title)
//            intent.putExtra("singer", song.singer)
//            intent.putExtra("second", song.second)
//            intent.putExtra("playTime", song.playTime)
//            intent.putExtra("isPlaying", song.isPlaying)
//            intent.putExtra("music", song.music)
//            intent.putExtra("coverImg", song.coverImg)
//            //startActivity(intent)
//            getResultText.launch(intent)
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", songs[nowPos].id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

//        Log.d("Song", song.title + song.singer)

        binding.mainNextBtnIv.setOnClickListener {
            moveSong(+1)
        }
        binding.mainPrevBtnIv.setOnClickListener {
            moveSong(-1)
        }
        binding.mainMiniplayerBtn.setOnClickListener {
            setMiniPlayerStatus(true)
        }
        binding.mainPauseBtn.setOnClickListener {
            setMiniPlayerStatus(false)
        }

        Log.d("MAIN/JWT_TO_SERVER", getJwt().toString())
    }
    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }
    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread() {
        private var second : Int = 0
        private var mills : Float = 0f

        override fun run() {
            super.run()
            try {
                while(true) {
                    if(second >= playTime) { // 진행 시간이 전체 시간만큼 진행됐을 때

                        second = 0
                        mills = 0f

                        runOnUiThread {
                            mediaPlayer?.seekTo(0) // 재생 위치 초기화
//                            binding.songProcessTime.text = "00:00"
                            startTimer()
                        }
                        break
                    }
                    if(isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.mainProgressSb.progress = ((mills / playTime)* 100).toInt()
                        }

                        if (mills % 1000 == 0f) {

                            second++
                        }
                    }
                }
            }catch (e: InterruptedException) {
                Log.d("Song", "Thread가 죽었습니다. ${e.message}")
            }
        }
    }

    private fun moveSong(direct: Int) {
        if(nowPos + direct < 0) {
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size) {
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        timer.interrupt()
        startTimer()
        setMiniPlayerStatus(true)

        // 새로운 노래를 틀어야 하기 때문에 mediaPlayer를 해제시켜줌
        mediaPlayer?.release()
        mediaPlayer = null

        setMiniPlayer(songs[nowPos])
    }

    private fun setMiniPlayerStatus(isPlaying : Boolean) {
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying) { // 재생
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else { // 일시정지
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE

            // 재생 중이 아닐 때, pause를 호출하면 에러가 나기 때문에 이를 방지
            if(mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
    }
    companion object { const val STRING_INTENT_KEY = "song_title"}
    private val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val returnString = result.data?.getStringExtra(STRING_INTENT_KEY).toString()
//            Toast.makeText(this@MainActivity, returnString, Toast.LENGTH_SHORT).show()
            Log.d("song_title", returnString)
        }
    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer

        // 데이터 불러오기
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val second = sharedPreferences.getInt("second", 0)

        // SongActivity에서 진행된 시간 불러와서 progress bar에 연동
        binding.mainProgressSb.progress = (second * 100000)/song.playTime

        // 앱 리소스의 식별자를 알아내기 위해 사용
        // 검색할 리소스의 이름, 리소스의 유형(디렉토리명), 리소스가 속한 패키지 이름을 입력인자로 가짐
        val music = resources.getIdentifier(song.music, "raw", this.packageName)

        // context와 음악 리소스 id를 입력 값으로 갖는 MediaPlayer 객체 생성
        mediaPlayer = MediaPlayer.create(this, music)

        setMiniPlayerStatus(song.isPlaying)


//        val songJson = sharedPreferences.getString("songData", null)
//        song = songs // song에 수록곡 정보 담기
    }

    private fun getJwt(): String? {
        val spf = this?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf!!.getString("jwt", "")
    }

    override fun onStart() {
        super.onStart()
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//
//        // null값일 때 default로 들어가는 값
//        song = if(songJson == null) {
//            Song("라일락", "아이유(IU)", 0, 10, false, "music_lilac", R.drawable.img_album_exp2)
//        } else {
//            gson.fromJson(songJson, Song::class.java)
//        }
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        // sharedPreference에 저장된 값이 0이라면 첫 번째 인덱스의 song을 받아옴
        // 아니라면 저장된 songId를 통해서 song을 가져옴
        songs[nowPos] = if (songId == 0) {
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId)
        }
        Log.d("song ID", songs[nowPos].id.toString())

        setMiniPlayer(songs[nowPos])

    }


    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreferences.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        setMiniPlayer(songs[nowPos])
    }

    override fun onPause() {
        super.onPause()

        // 앱의 sharedPreferences 객체를 가져오기 위해 사용
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) //song은 sharedPreference의 이름
        val editor = sharedPreferences.edit() // 에디터: 값 추가, 수정, 삭제

        editor.putInt("songId", songs[nowPos].id)

        editor.apply()

        setMiniPlayerStatus(false)

    }
    private fun initPlaylist() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            // songs의 id와 songId가 같을 때 index 리턴
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }
    private fun initSong() {

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID", songs[nowPos].id.toString())

        startTimer()

        setMiniPlayer(songs[nowPos])
    }

    fun clickAlbumPlay(album: Album) {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 1)

        songs = songDB.songDao().getSongsInAlbum(album.id) as ArrayList<Song>
        nowPos = getPlayingSongPosition(songId)

        songs[nowPos] = songs[0]

//        val editor = spf.edit()
//        editor.putInt("second", 0)
//        editor.apply()

        setMiniPlayer(songs[nowPos])
    }


    // 데이터를 DB에 넣어주는 함수
    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                60,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                false,
                1
            )
        )
        songDB.songDao().insert(
            Song(
                "LILAC",
                "아이유 (IU)",
                0,
                60,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
                1
            )
        )
        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                60,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
                2
            )
        )
        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                60,
                false,
                "music_flu",
                R.drawable.img_album_exp3,
                false,
                3
            )
        )
        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "방탄소년단 (BTS)",
                0,
                60,
                false,
                "music_boy",
                R.drawable.img_album_exp4,
                false,
                4
            )
        )
        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                60,
                false,
                "music_bboom",
                R.drawable.img_album_exp5,
                false,
                5
            )
        )
        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }

    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                1,
                "IU 5th Album 'LILAC'",
                "아이유 (IU)",
                R.drawable.img_album_exp2
            )
        )
        songDB.albumDao().insert(
            Album(
                2,
                "Butter",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp
            )
        )
        songDB.albumDao().insert(
            Album(
                3,
                "iScreaM Vol.10: Next Level Remixes",
                "에스파 (AESPA)",
                R.drawable.img_album_exp3
            )
        )
        songDB.albumDao().insert(
            Album(
                4,
                "MAP OF THE SOUL: PERSONA",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp4
            )
        )
        songDB.albumDao().insert(
            Album(
                5,
                "GREAT!",
                "모모랜드",
                R.drawable.img_album_exp5
            )
        )
    }
}