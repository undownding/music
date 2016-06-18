package me.undownding.music

import android.app.Application
import com.baidu.music.SDKEngine
import com.baidu.music.onlinedata.PlayinglistManager
import com.baidu.utils.LogUtil
import com.facebook.drawee.backends.pipeline.Fresco
import me.undownding.music.model.OAuthHelper

/**
 * Created by undownding on 16-6-11.
 */
class MusicApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        LogUtil.setDebugMode(true);
        instance = this
        Fresco.initialize(this);
        SDKEngine.getInstance()
        PlayinglistManager.getInstance(this).initPlayer(this)
        OAuthHelper.auth(this)
    }

    companion object {
        lateinit var instance: MusicApplication
    }
}