package me.undownding.music


import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.gc.materialdesign.views.Slider
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
    val slider by lazy { findViewById(R.id.slider) as Slider }
    val tvTotalTime by lazy { findViewById(R.id.total_time) as TextView }
    val tvCurrentTime by lazy { findViewById(R.id.current_time) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playing)
        presenter.doBind()
        slider.setOnValueChangedListener {
            tvCurrentTime.text = PlayingPresenter.getTime(it.toLong())
            presenter.changeValue(it)
        }
    }

    override fun onDestroy() {
        presenter.doUnbind()
        super.onDestroy()
    }
}
