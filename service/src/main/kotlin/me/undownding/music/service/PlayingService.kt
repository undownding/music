package me.undownding.music.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.baidu.music.player.StreamPlayer

/**
 * Created by undownding on 16-6-19.
 */
class PlayingService: Service() {

    val player by lazy { StreamPlayer.getInstance(this) }

    override fun onBind(intent: Intent?): IBinder? {
        return ServiceBinder(this)
    }

    class ServiceBinder(service: PlayingService): Binder() {
        val service = service
    }

}