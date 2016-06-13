package me.undownding.music.model

import android.content.Context
import rx.Observable
import rx.schedulers.Schedulers

/**
 * Created by undownding on 16-6-11.
 */
class OAuthHelper {
    companion object {

        private val APP_KEY = "H7THeqTNzDcUOpNuvs0gXGIL"
        private val APP_SECRET = "KCzK1bvf7yAmzNbMsPTwcw7uNaqmGBox"
        private val SCOPE = "music_media_basic,music_musicdata_basic,music_search_basic"

        fun auth(context: Context) {
            Observable.create<Void> {
                com.baidu.music.oauth.OAuthHelper.requestClientCredentialsToken(context,
                        APP_KEY,
                        APP_SECRET,
                        SCOPE
                )
            }
                    .subscribeOn(Schedulers.newThread())
                    .subscribe()
        }
    }
}
