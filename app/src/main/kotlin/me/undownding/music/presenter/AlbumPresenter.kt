package me.undownding.music.presenter

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.baidu.music.model.Album
import com.baidu.music.model.Music
import com.baidu.music.onlinedata.PlayinglistManager
import com.baidu.music.player.StreamPlayer
import com.trello.rxlifecycle.components.support.RxFragment
import me.undownding.music.MusicApplication
import me.undownding.music.ext.DataBaseExt
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

    override fun getFragment(): RxFragment {
        return fragment
    }

    override fun bind(view: View): View {
        super.bind(view)
        fragment.recyclerView.adapter = fragment.adapter
        fragment.recyclerView.layoutManager = LinearLayoutManager(view.context)
        fragment.fab.setOnClickListener {  }
        return super.bind(view)
    }

    override fun requestData() {
        val id = fragment.arguments.getLong(AlbumFragment.ALBUM_ID, 0)
        AlbumModel.requestAlbumById(id, true)
                .map { it ->
                    val searchResult = DoubanModel.search(query = it.mTitle, size = 1).toBlocking().single()
                    if (searchResult?.musics?.size ?: 0 > 0) {
                        it.mPicBig = searchResult.musics?.get(0)?.image?.replace("/spic", "/lpic")
                    }
                    it
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
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

    }


}