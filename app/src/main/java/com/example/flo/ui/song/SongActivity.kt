package com.example.flo.ui.song

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.flo.ui.main.MainActivity
import com.example.flo.R
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivitySongBinding

@Suppress("DEPRECATION")
class SongActivity : AppCompatActivity() {
    lateinit var binding : ActivitySongBinding
//    lateinit var song: Song
    lateinit var timer: Timer

    // mediaPlayer를 해제하기 위해 nullable로 선언
    private var mediaPlayer: MediaPlayer? = null

//    private var gson: Gson = Gson() // gson: java object를 JSON or JSON을 java object로 변환을 도와줌

    private val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) { // bundle: 여러가지 타입을 저장하는 Map클래스, 액티비티 간 데이터를 주고 받을 때 사용
        // savedInstanceState: 화면 구성의 변경이 발생할 때 현재 인스턴스에서 데이터를 저장하고 새 인스턴스에서 다시 불러오기 위해 호출
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()
    }
    override fun onStart() {
        super.onStart()

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

        setPlayer(songs[nowPos])

    }

    private fun initSong() {
//        if(intent.hasExtra("title") && intent.hasExtra("singer")) {
//            song = Song(
//                intent.getStringExtra("title")!!,
//                intent.getStringExtra("singer")!!,
//                intent.getIntExtra("second", 0),
//                intent.getIntExtra("playTime", 0),
//                intent.getBooleanExtra("isPlaying", false),
//                intent.getStringExtra("music")!!,
//                intent.getIntExtra("coverImg", R.drawable.img_album_exp2)
//            )
//        }
//        else {
//            song = Song("라일락", "아이유(IU)", 0, 10, false, "music_lilac", R.drawable.img_album_exp2)
//        }
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID", songs[nowPos].id.toString())

        startTimer()

        setPlayer(songs[nowPos])
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

    private fun setPlayer(song: Song) {
        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.songProcessTime.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songProcessTotalTime.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)
        binding.songImageIv.setImageResource(song.coverImg!!)

        // 앱 리소스의 식별자를 알아내기 위해 사용
        // 검색할 리소스의 이름, 리소스의 유형(디렉토리명), 리소스가 속한 패키지 이름을 입력인자로 가짐
        val music = resources.getIdentifier(song.music, "raw", this.packageName)

        // context와 음악 리소스 id를 입력 값으로 갖는 MediaPlayer 객체 생성
        mediaPlayer = MediaPlayer.create(this, music)

        // song data에 따라 좋아요 버튼을 표시해주는 버튼
        if (song.isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus(isPlaying : Boolean) {
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying) { // 재생
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songBtnPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else { // 일시정지
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songBtnPauseIv.visibility = View.GONE

            // 재생 중이 아닐 때, pause를 호출하면 에러가 나기 때문에 이를 방지
            if(mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
    }
    private fun setRepeatStatus(isPlaying : Boolean) {
        if(isPlaying) {
            binding.songRepeatBtnIv.visibility = View.VISIBLE
            binding.songRepeatBtnIv2.visibility = View.GONE
        }
        else {
            binding.songRepeatBtnIv.visibility = View.GONE
            binding.songRepeatBtnIv2.visibility = View.VISIBLE

        }
    }
    private fun setRandomStatus(isPlaying : Boolean) {
        if(isPlaying) {
            binding.songRandomBtnIv.visibility = View.VISIBLE
            binding.songRandomBtnIv2.visibility = View.GONE
        }
        else {
            binding.songRandomBtnIv.visibility = View.GONE
            binding.songRandomBtnIv2.visibility = View.VISIBLE
        }
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

                            if(binding.songRepeatBtnIv2.visibility == View.VISIBLE) {
                                setPlayerStatus(true)
                            } else {
                                setPlayerStatus(false)
                            }
                            startTimer()
                        }

                        break
                    }
                    if(isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime)* 100).toInt()
                        }

                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.songProcessTime.text = String.format("%02d:%02d", second / 60, second % 60) // view 렌더링
                                binding.songProcessTotalTime.text = String.format("%02d:%02d", playTime / 60, playTime % 60)
                            }
                            second++
                        }
                    }
                }
            }catch (e: InterruptedException) {
                Log.d("Song", "Thread가 죽었습니다. ${e.message}")
            }
        }
    }

    // 사용자가 포커스를 잃었을 때 음악이 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)

        songs[nowPos].second = ((binding.songProgressSb.progress * songs[nowPos].playTime)/100)/1000

        // 앱의 sharedPreferences 객체를 가져오기 위해 사용
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) //song은 sharedPreference의 이름
        val editor = sharedPreferences.edit() // 에디터: 값 추가, 수정, 삭제

//        val songJson = gson.toJson(songs[nowPos])
//        editor.putString("songData", songJson)

        editor.putInt("songId", songs[nowPos].id)
        editor.putInt("second", songs[nowPos].second)

        editor.apply()

    }
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제

    }

    // 해당 데이터베이스에 저장되어있는 songlist를 뽑아와서 songs에 저장해줘야함
    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!

        // songDB.songDao().getSongs() - 데이터베이스에서 노래 목록을 가져옴
        // addAll() - 가져온 목록의 모든 요소를 'songs' ArrayList에 추가함
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener() {

        val song = binding.songTitleTv.text.toString()

        binding.songDownIb.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java).apply {
//                putExtra(MainActivity.STRING_INTENT_KEY, song)
//            }
//            setResult(Activity.RESULT_OK, intent)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
            serviceStart(this) // 알림창 뜨기
        }
        binding.songBtnPauseIv.setOnClickListener {
            setPlayerStatus(false)
            serviceStop(this) // 알림창 꺼지기
        }
        binding.songRepeatBtnIv.setOnClickListener {
            setRepeatStatus(false)
        }
        binding.songRepeatBtnIv2.setOnClickListener {
            setRepeatStatus(true)
        }
        binding.songRandomBtnIv.setOnClickListener {
            setRandomStatus(false)
        }
        binding.songRandomBtnIv2.setOnClickListener {
            setRandomStatus(true)
        }
        binding.songBtnNextIv.setOnClickListener {
            moveSong(+1)
        }
        binding.songBtnPrevIv.setOnClickListener {
            moveSong(-1)
        }

        // 좋아요 버튼을 누르면
        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
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
        setPlayerStatus(false)

        // 새로운 노래를 틀어야 하기 때문에 mediaPlayer를 해제시켜줌
        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[nowPos])
    }

    // 좋아요 버튼이 눌렸을 때
    private fun setLike(isLike: Boolean) {
        songs[nowPos].isLike = !isLike // DB의 값을 업데이트 한 것이 아님
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id) // DB 업데이트

        if (!isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
            Toast(this).showCustomToast("좋아요 한 곡에 담겼습니다.", this)
        } else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
            Toast(this).showCustomToast("좋아요 취소", this)
        }
    }

    private fun serviceStart(view: SongActivity) {
        val intent = Intent(this, Foreground::class.java)
        ContextCompat.startForegroundService(this, intent)
    }
    private fun serviceStop(view: SongActivity) {
        val intent = Intent(this, Foreground::class.java)
        stopService(intent)
    }
}