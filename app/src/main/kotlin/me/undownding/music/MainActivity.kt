/**
 * Created by undownding on 16-6-11.
 */
package me.undownding.music

import android.os.Bundle
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import me.undownding.music.fragment.AlbumListFragment

class MainActivity: RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AlbumListFragment())
            .commit()
    }

}