package me.undownding.music.fragment

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.trello.rxlifecycle.components.support.RxFragment
import me.undownding.music.R
import me.undownding.music.model.AlbumModel
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by undownding on 16-6-24.
 */
class LoadingFragment: RxFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AlbumModel.requestAlbums()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    (activity as RxAppCompatActivity).supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, AlbumListFragment())
                            .commit()
                }, {
                    it.printStackTrace()
                    AlertDialog.Builder(context)
                            .setMessage(R.string.error)
                            .setNegativeButton(android.R.string.ok, { dialog, which -> activity.finish() })
                            .show().setOnDismissListener{ activity.finish() }
                })
    }
}