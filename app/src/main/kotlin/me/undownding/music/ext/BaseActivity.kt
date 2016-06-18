package me.undownding.music.ext

import android.support.v7.app.AppCompatDelegate
import com.trello.rxlifecycle.components.support.RxAppCompatActivity

/**
 * Created by undownding on 16-6-19.
 */
open class BaseActivity: RxAppCompatActivity() {
    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}