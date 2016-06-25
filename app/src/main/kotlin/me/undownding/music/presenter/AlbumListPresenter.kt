package me.undownding.music.presenter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.baidu.music.model.Album
import com.trello.rxlifecycle.components.support.RxFragment
import me.undownding.music.R
import me.undownding.music.ext.DataBaseExt
import me.undownding.music.fragment.AlbumFragment
import me.undownding.music.fragment.AlbumListFragment
import me.undownding.music.model.AlbumModel
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by undownding on 16-6-12.
 */
class AlbumListPresenter(fragment: AlbumListFragment): BasePresenter<Album>() {

    private val fragment = fragment

    override fun requestData() {
        AlbumModel.requestAlbums()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ result ->
                    fragment.list.addAll(result)
                    fragment.adapter.data = result
                    fragment.adapter.notifyDataSetChanged()
                    DataBaseExt.db.close()
                }
    }

    override fun bind(view: View): View {
        super.bind(view)
        fragment.recyclerView.adapter = fragment.adapter
        fragment.recyclerView.layoutManager = StaggeredGridLayoutManager(
                fragment.context.resources.getInteger(R.integer.colum_count), StaggeredGridLayoutManager.VERTICAL
        )
        return view
    }

    override fun getFragment(): RxFragment {
        return fragment
    }

    override fun onItemClick(item: Album) {
        fragment.activity.supportFragmentManager
                .beginTransaction()
                .hide(fragment)
                .add(R.id.container, AlbumFragment.newInstance(item.mAlbumId.toLong()))
                .addToBackStack(fragment.javaClass.simpleName)
                .commit()
    }
}