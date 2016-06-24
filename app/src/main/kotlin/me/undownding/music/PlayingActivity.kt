package me.undownding.music


import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import me.undownding.music.presenter.PlayingPresenter
class PlayingActivity: RxAppCompatActivity() {

    companion object {
        val PLAY_MUSIC = "play_music"
        val MUSIC_BEAN = "music_bean"
    }

    val presenter by lazy { PlayingPresenter(this) }
    val rootView by lazy { findViewById(R.id.root) as ImageView }
    val picAlbum by lazy { findViewById(R.id.pic_album) as ImageView }
    val tvTitle by lazy { findViewById(R.id.tv_title) as TextView }
    val tvArtist by lazy { findViewById(R.id.tv_artist) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playing)
        presenter.doBind()
    }

    override fun onDestroy() {
        presenter.doUnbind()
        super.onDestroy()
    }
}
