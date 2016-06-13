package me.undownding.music.presenter

import android.util.Log
import android.view.View
import com.baidu.music.model.Album
import com.baidu.music.model.Music
import com.baidu.music.onlinedata.PlayinglistManager
import com.baidu.music.player.StreamPlayer
import com.trello.rxlifecycle.components.support.RxFragment
import me.undownding.music.MusicApplication
import me.undownding.music.fragment.AlbumFragment
import me.undownding.music.model.AlbumModel
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
        return super.bind(view)
    }

    override fun requestData() {
        val id = fragment.arguments.getLong(AlbumFragment.ALBUM_ID, 0)
        val player = StreamPlayer(MusicApplication.instance)
        AlbumModel.requestAlbumById(id)
                .map { it ->
//                    val searchResult = DoubanModel.search(query = it.mTitle, size = 1).toBlocking().single()
//                    if (searchResult?.musics?.size ?: 0 > 0) {
//                        it.mPicBig = searchResult.musics?.get(0)?.image?.replace("/spic", "/lpic")
//                    }
                    it
                }
                .map {
                    it
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe {
                    player.prepare(it.items[0])
                    player.setOnPreparedListener {
                        player.start()
                    }
                }
    }

    override fun onItemClick(item: Music) {

    }
}