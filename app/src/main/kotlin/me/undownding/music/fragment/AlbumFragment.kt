package me.undownding.music.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.baidu.music.model.Album
import com.baidu.music.model.Music
import com.trello.rxlifecycle.components.support.RxFragment
import me.undownding.binding.BindingAdapter
import me.undownding.music.BR
import me.undownding.music.R
import me.undownding.music.presenter.AlbumPresenter
import me.undownding.music.presenter.BasePresenter

/**
 * Created by undownding on 16-6-12.
 */
class AlbumFragment: BaseSwipeableFragment<Music>() {

    private val layoutId = R.layout.item_list_album
    private val brId = BR.item

    // @BindView(R.id.swipe_container)
    // lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @BindView(android.R.id.list)
    lateinit var recyclerView: RecyclerView

    val presenter by lazy { AlbumPresenter(this) }

    companion object {
        val ALBUM_ID = "album_id"

        fun newInstance(albumId: Long): AlbumFragment {
            val instance = AlbumFragment()
            val data = Bundle()
            data.putLong(ALBUM_ID, albumId)
            instance.arguments = data
            return instance
        }
    }

    override fun getPresenter(): BasePresenter<Music> {
        return presenter
    }

    override fun onCreateAdapter(): BindingAdapter<Music> {
        return object : BindingAdapter<Music>(list, layoutId, brId) {
            override fun onClick(view: View?, item: Music?, position: Int) {
                presenter.onItemClick(item!!)
            }
        }
    }
}