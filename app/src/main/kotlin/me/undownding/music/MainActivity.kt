/**
 * Created by undownding on 16-6-11.
 */
package me.undownding.music

import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import me.undownding.music.fragment.AlbumListFragment

class MainActivity: RxAppCompatActivity() {
    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    val fragment = AlbumListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount < 1) {
            super.onBackPressed()
        } else {
            fragmentManager.popBackStack()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
    }
}