package me.undownding.music

import android.content.Intent
import android.os.Bundle
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import me.undownding.music.service.PlayingService
import me.undownding.music.service.RxPlayingServiceImpl
import rx.schedulers.Schedulers

class PlayingActivity: RxAppCompatActivity() {

    var service: PlayingService? = null
    val rxBinding by lazy { RxPlayingServiceImpl() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playing)
        doBind()
    }

    fun doBind() {
        startService(Intent(this, PlayingService::class.java))
        rxBinding.bind(this)
                .subscribeOn(Schedulers.newThread())
                .subscribe { service = it }
    }

    override fun onDestroy() {
        if (service != null) {
            unbindService(rxBinding.serviceConnection)
        }
        super.onDestroy()
    }
}
