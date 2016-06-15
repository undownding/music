package me.undownding.music.fragment

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import butterknife.BindView
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.baidu.music.model.Album
import com.baidu.music.model.Music
import com.trello.rxlifecycle.components.support.RxFragment
import me.undownding.binding.BindingAdapter
import me.undownding.music.BR
import me.undownding.music.MusicApplication
import me.undownding.music.R
import me.undownding.music.databinding.ItemListMusicBinding
import me.undownding.music.ext.FrescoExt
import me.undownding.music.presenter.AlbumPresenter
import me.undownding.music.presenter.BasePresenter
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by undownding on 16-6-12.
 */
class AlbumFragment: BaseFragment<Music>() {

    private val layoutId = R.layout.item_list_music
    private val brId = BR.item

    @BindView(android.R.id.list)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.toolbar_bg)
    lateinit var ivToolbar: ImageView

    @BindView(R.id.fab)
    lateinit var fab: FloatingActionButton

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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return presenter.bind(inflater?.inflate(R.layout.fragment_album, container, false)!!)
    }

    override fun getPresenter(): BasePresenter<Music> {
        return presenter
    }

    override fun onCreateAdapter(): BindingAdapter<Music> {
        return object : BindingAdapter<Music>(list, layoutId, brId) {
            override fun onClick(view: View?, item: Music?, position: Int) {
                presenter.onItemClick(item!!)
            }

            override fun onBindViewHolder(holder: Holder?, position: Int) {
                val binding = holder?.binding as ItemListMusicBinding
                val item = list[position]
                binding.item = item
                binding.container.setOnClickListener {
                    presenter.onItemClick(item)
                }
            }
        }
    }

    fun dispalyToolbarImage(url: String) {
        Observable.create<Bitmap> { subscriber ->
            val uri = Uri.parse(url)
            if (FrescoExt.isDownloaded(uri)) {
                subscriber.onNext(FrescoExt.getOfflineImage(uri))
                subscriber.onCompleted()
            } else {
                Volley.newRequestQueue(MusicApplication.instance)
                        .add(ImageRequest(url, {
                            subscriber.onNext(it)
                            subscriber.onCompleted()
                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, {
                            subscriber.onCompleted()
                        }))
            }
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    ivToolbar.setImageBitmap(it)
                    ivToolbar.visibility = View.VISIBLE
                }
    }
}