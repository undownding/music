package me.undownding.music.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import rx.Observable
import rx.Subscriber

/**
 * Created by undownding on 16-6-19.
 */
class RxPlayingServiceImpl {

    var serviceConnection: RxConnection? = null

    fun bind(context: Context): Observable<PlayingService> {
        return Observable.create<PlayingService> {
            if (serviceConnection == null) {
                serviceConnection = RxConnection(it)
            }
            context.bindService(Intent(context, PlayingService::class.java), serviceConnection, Service.BIND_AUTO_CREATE)
        }
    }

    class RxConnection(it: Subscriber<in PlayingService>): ServiceConnection {
        val it = it

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            it.onNext((service as PlayingService.ServiceBinder).service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            it.onCompleted()
        }

    }
}