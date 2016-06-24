package me.undownding.music.presenter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.baidu.music.model.Album
import com.baidu.music.model.Music
import com.trello.rxlifecycle.components.support.RxFragment
import me.undownding.music.PlayingActivity
import me.undownding.music.ext.DataBaseExt
import me.undownding.music.ext.GsonUtils
import me.undownding.music.ext.toJson
import me.undownding.music.fragment.AlbumFragment
import me.undownding.music.model.AlbumModel
import me.undownding.music.model.DoubanModel
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by undownding on 16-6-12.
 */
class AlbumPresenter(fragment: AlbumFragment): BasePresenter<Music>() {

    val fragment = fragment
    var album: Album? = null

    override fun getFragment(): RxFragment {
        return fragment
    }

    override fun bind(view: View): View {
        super.bind(view)
        fragment.recyclerView.adapter = fragment.adapter
        fragment.recyclerView.layoutManager = LinearLayoutManager(view.context)
        fragment.fab.setOnClickListener {
            if (album != null) {
                val intent = Intent(fragment.context, PlayingActivity::class.java)
                intent.putExtra(PlayingActivity.PLAY_LIST, true)
                intent.putExtra(PlayingActivity.MUSIC_BEAN, GsonUtils.instance.toJson(album))
                fragment.context.startActivity(intent)
            }
        }
        return super.bind(view)
    }

    override fun requestData() {
        val id = fragment.arguments.getLong(AlbumFragment.ALBUM_ID, 0)
        AlbumModel.requestAlbumById(id, true)
                .map { album ->
                    val searchResult = DoubanModel.search(query = album.mTitle, size = 1).toBlocking().single()
                    if (searchResult?.musics?.size ?: 0 > 0) {
                        album.mPicBig = searchResult.musics?.get(0)?.image?.replace("/spic", "/lpic")
                    }
                    album.mItems.forEach {
                        if (TextUtils.isEmpty(it.mPicBig)) {
                            it.mPicBig = album.mPicBig
                        }
                    }
                    album
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    this.album = it
                    if (fragment.activity != null) {
                        fragment.list.addAll(it.items)
                        fragment.adapter.data = it.items
                        fragment.adapter.notifyDataSetChanged()
                        (fragment.activity as AppCompatActivity).setSupportActionBar(fragment.toolbar)
                        fragment.dispalyToolbarImage(if (TextUtils.isEmpty(it.mPic1000)) it.mPicBig else it.mPic1000)
                        fragment.activity.title = it.mTitle
                    }
                    DataBaseExt.db.close()
                }
    }

    override fun onItemClick(item: Music) {
        val intent = Intent(fragment.context, PlayingActivity::class.java)
        intent.putExtra(PlayingActivity.PLAY_MUSIC, true)
        intent.putExtra(PlayingActivity.MUSIC_BEAN, item.toJson())
        fragment.context.startActivity(intent)
    }


}