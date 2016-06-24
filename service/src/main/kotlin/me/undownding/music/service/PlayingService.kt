package me.undownding.music.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import com.baidu.music.player.StreamPlayer
import me.undownding.music.MusicApplication
import me.undownding.music.receiver.IncomingCallHandler

/**
 * Created by undownding on 16-6-19.
 */
class PlayingService: Service() {

    val player by lazy { StreamPlayer.getInstance(MusicApplication.instance) }
    val incomingCallHandler by lazy { IncomingCallHandler(player) }
    val headsetReceiver by lazy { HeadSetReceiver() }

    override fun onCreate() {
        incomingCallHandler.listen()
        registerReceiver(headsetReceiver, IntentFilter(Intent.ACTION_HEADSET_PLUG))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return ServiceBinder(this)
    }

    class ServiceBinder(service: PlayingService): Binder() {
        val service = service
    }

    override fun onDestroy() {
        incomingCallHandler.release()
        player.stop()
        player.release()
        unregisterReceiver(headsetReceiver)
    }

    inner class HeadSetReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val state = intent?.getIntExtra("state", -1)
            when (state) {
                0 -> player.pause()
                1 -> player.start()
            }
        }

    }

}