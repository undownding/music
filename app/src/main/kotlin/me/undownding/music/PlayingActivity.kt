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
        val PLAY_LIST  = "play_list"
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
    val btnPlay by lazy { findViewById(R.id.btn_play) as ImageView }
    val btnPrevious by lazy { findViewById(R.id.btn_previous) }
    val btnNext by lazy { findViewById(R.id.btn_next) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playing)
        presenter.doBind()
        slider.setOnValueChangedListener {
            tvCurrentTime.text = PlayingPresenter.getTime(it.toLong())
            presenter.changeValue(it)
        }
        btnPlay?.setOnClickListener { presenter.play() }
        btnPrevious?.setOnClickListener { presenter.previous() }
        btnNext?.setOnClickListener { presenter.next() }
    }

    override fun onDestroy() {
        presenter.doUnbind()
        super.onDestroy()
    }
}
