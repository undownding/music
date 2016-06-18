package me.undownding.music.model

import android.content.BroadcastReceiver
import android.content.Context

/**
 * Created by undownding on 16-6-19.
 */
abstract class BaseReceiver: BroadcastReceiver() {

    abstract fun register(context: Context)

    open fun unregister(context: Context) {
        context.unregisterReceiver(this)
    }
}