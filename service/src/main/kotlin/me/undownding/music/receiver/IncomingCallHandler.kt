package me.undownding.music.receiver

import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.baidu.music.player.StreamPlayer
import me.undownding.music.MusicApplication

/**
 * Created by undownding on 16-6-19.
 */
class IncomingCallHandler(player: StreamPlayer) {

    val player = player
    val listener by lazy { IncomingStateListener(this.player) }
    val telephonyManager by lazy { MusicApplication.instance.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager }

    fun listen() {
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    fun release() {
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE)
    }

    class IncomingStateListener(player: StreamPlayer): PhoneStateListener() {

        val player = player

        override fun onCallStateChanged(state: Int, incomingNumber: String?) {
            when (state) {
                TelephonyManager.CALL_STATE_RINGING, TelephonyManager.CALL_STATE_OFFHOOK -> player.pause()
                TelephonyManager.CALL_STATE_IDLE -> player.start()
            }
        }
    }
}