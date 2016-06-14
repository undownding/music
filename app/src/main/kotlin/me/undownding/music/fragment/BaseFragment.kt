package me.undownding.music.fragment

import android.os.Bundle
import android.view.View
import com.baidu.music.model.Album
import com.baidu.music.model.AlbumList
import com.trello.rxlifecycle.components.support.RxFragment
import me.undownding.binding.BindingAdapter
import me.undownding.music.presenter.BasePresenter
import java.util.*

/**
 * Created by undownding on 16-6-12.
 */
abstract class BaseFragment<T>: RxFragment() {
    var list = ArrayList<T>()
    val adapter by lazy { onCreateAdapter() }

    abstract fun getPresenter(): BasePresenter<T>

    abstract fun onCreateAdapter(): BindingAdapter<T>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getPresenter().requestData()
    }

    fun append(list: List<T>?) {
        adapter.appendData(list)
    }
}