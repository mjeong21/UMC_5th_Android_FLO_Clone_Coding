package com.example.flo.ui.song

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.flo.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Foreground : Service() {

    private val ChannelID = "Foreground"
    private val NotiID = 153

    // 코루틴 스코프와 작업자 선언
    private var notificationJob: Job? = null

    // Dispatchers.Default: 안드로이드 기본 스레드풀 사용. CPU를 많이 쓰는 작업에 최적화
    private val notificationDispatches: CoroutineDispatcher = Dispatchers.Default

    // NotificationChannel 생성
    // Notification: 상태바에 앱의 상태를 출력하여 유저에게 알림을 보내는 기능 -> 사용자는 앱 외부에서도 알림을 받을 수 있음
    // 이 채널은 알림의 중요도, 사운드, 진동 패턴 등의 세부적인 설정 관리
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(ChannelID, "FOREGROUND", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // 내가 쓸 채널을 알림
        createNotificationChannel()

        // 백그라운드에서 progress 진행
        runBackground()

//        // 카운트를 시작하는 코루틴
//        notificationJob = CoroutineScope(notificationDispatches).launch {
//            for (i in 1..100) {
//                updateNotificationProgress(i)
//                delay(1000) // 1초마다 카운트
//            }
//        }

        return super.onStartCommand(intent, flags, startId)
    }
    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }
    private fun updateNotificationProgress(progress: Int) {
        // SongActivity로 전환할 Intent 생성
        val songIntent = Intent(this, SongActivity::class.java)

        // 호출된 액티비티가 현재 태스크의 top 이미 존재할 경우 재실행되지 않음 (기존 top 재사용)
        songIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        // PendingIntent: Intent를 포장하여 다른 어플리케이션에 전달하거나, 나중에 실행할 수 있도록 함
        // getActivity: 액티비티 시작을 위한 PendingIntent 반환
        // 0: PendingIntent의 요청 코드, 이 코드를 사용하여 나중에 동일한 PendingIntent를 검색하거나 취소할 수 있음
        // PendingIntent.FLAG_IMMUTABLE: PendingIntent의 내용이 변경되지 않도록 플래그 설정
        val songPendingIntent = PendingIntent.getActivity(this, 0, songIntent, PendingIntent.FLAG_MUTABLE)

        // 알림을 업데이트하여 프로그레스 바에 표시
        // NotificationCompat.Build: 알림 생성
        // 앞서 생성한 Notification의 ID를 지정해야함
        val notification = NotificationCompat.Builder(this, ChannelID)
            .setContentTitle("Music Playing")
            .setContentText("Count: $progress")
            .setSmallIcon(R.drawable.icon_browse_arrow_right)
            .setProgress(100, progress, false)
            .setContentIntent(songPendingIntent)
            .build() // Notification 객체 생성

        // NotificationManager로 알림 표시
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NotiID, notification) // 사용자에게 알림 표시

        // 사용하는 현재 서비스가 포그라운드로 동작한다는 것을 알려줌
        startForeground(NotiID, notification)
    }
    override fun onDestroy() {
        super.onDestroy()
        notificationJob?.cancel()
        notificationDispatches.cancel()
    }
    private fun runBackground() {
        if(notificationJob == null || notificationJob?.isCancelled == true) {
            notificationJob = CoroutineScope(notificationDispatches).launch {
                try {
                    repeat(100) { i ->
                    updateNotificationProgress(i)
                    delay(1000)

                    }
                } finally {

                }
            }
        }
    }
}